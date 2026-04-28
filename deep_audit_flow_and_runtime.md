# ShopFlow — Deep Audit: Flow, Runtime & Production Readiness

> **Audit Date:** 2026-04-23  
> **Branch:** `001-shopflow-ecommerce-app`  
> **Scope:** Gradle IDE sync, Auth/Routing logic, Navigation crashes, ViewModel data flow, UI interactivity gaps

---

## Table of Contents

1. [Gradle Configuration & IDE Sync Error](#1-gradle-configuration--ide-sync-error)
2. [Authentication & Routing Logic (The Bypass)](#2-authentication--routing-logic-the-bypass)
3. [Navigation & Runtime Crashes](#3-navigation--runtime-crashes)
4. [Data Flow & ViewModel Architecture](#4-data-flow--viewmodel-architecture)
5. [UI Interactivity Audit](#5-ui-interactivity-audit)
6. [Severity Summary Matrix](#6-severity-summary-matrix)

---

## 1. Gradle Configuration & IDE Sync Error

### Symptom

The IDE (Android Studio) shows **"Unresolved reference: libs"** in the `plugins {}` block and red-underlines `alias(libs.plugins.*)` calls. Despite this, `./gradlew assembleDebug` compiles successfully.

### Root Cause

**Line 1 of `app/build.gradle.kts`:**

```kotlin
import java.util.Properties  // ← THIS is the problem

plugins {
    alias(libs.plugins.android.application)
    // ...
}
```

In Kotlin DSL build scripts, the `plugins {}` block has special status — it is **evaluated in a pre-compilation phase** before the rest of the script. The Gradle Script Kotlin compiler requires that `plugins {}` be the **absolute first statement** in the file (after blank lines/comments only). Any `import` statement placed before it breaks the IDE's script model resolution.

### Technical Explanation

- **Build succeeds** because the Gradle daemon uses its own resolution pipeline that is more lenient; it pre-processes the `plugins` block separately from the file parse tree.
- **IDE fails** because Android Studio's Kotlin Script editor uses JetBrains' Gradle Kotlin DSL IDE resolver, which treats the entire file as one compilation unit. An `import` before `plugins {}` invalidates the "precompiled accessor" lookup for `libs.*`, causing the red squiggles.
- This is a well-documented Gradle quirk: [Gradle Docs — Constraints on the plugins block](https://docs.gradle.org/current/userguide/plugins.html#sec:constrained_syntax).

### Step-by-Step Fix

**Move the `import` statement BELOW the `plugins {}` block:**

```diff
- import java.util.Properties
- 
  plugins {
      alias(libs.plugins.android.application)
      alias(libs.plugins.kotlin.android)
      alias(libs.plugins.kotlin.compose)
      alias(libs.plugins.kotlin.ksp)
      alias(libs.plugins.hilt.android)
      alias(libs.plugins.apollo)
      alias(libs.plugins.room)
  }
  
+ import java.util.Properties
+ 
  val secretsProperties = Properties().apply {
```

**File:** `app/build.gradle.kts`  
**Lines Affected:** 1–11

After this change:
1. Sync Gradle in Android Studio (`File → Sync Project with Gradle Files`)
2. All `libs.*` references will resolve correctly
3. The `Properties` import still works because it's evaluated during the script body phase, not the plugin phase

---

## 2. Authentication & Routing Logic (The Bypass)

### Symptom

The app navigates from **Splash → Home** directly, completely bypassing the Onboarding and Login screens. First-time users never see onboarding. Unauthenticated users land on the Home screen and interact with the app as if they are logged in.

### Root Cause

The `SplashScreen` routing logic checks **only** the `OnboardingViewModel.hasCompletedOnboarding` DataStore flag. It **never checks** whether the user is actually authenticated (i.e., whether a valid `accessToken` exists in `TokenDataStore`).

### Detailed Trace

#### File 1: `SplashScreen.kt` — Lines 53–68

```kotlin
@Composable
fun SplashScreen(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit,
    onAutoNavigateHome: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()  // ← Only checks onboarding
) {
    val hasCompletedOnboarding by viewModel.hasCompletedOnboarding.collectAsState()

    LaunchedEffect(hasCompletedOnboarding) {
        if (hasCompletedOnboarding) {  // ← Bug: if onboarding was done once, auto-nav to Home
            delay(2500)
            onAutoNavigateHome()       // ← Goes DIRECTLY to Home. No auth check!
        }
    }
```

**The logic flaw:**
- `hasCompletedOnboarding` defaults to `false` initially (correct for first launch).
- Once the user completes onboarding (even without logging in), `PreferencesDataStore` stores `onboardingCompleted = true`.
- On subsequent app launches, `hasCompletedOnboarding` emits `true`, and the `LaunchedEffect` fires `onAutoNavigateHome()` after 2.5 seconds — **with zero authentication validation**.
- There is no `SplashViewModel` — the screen directly injects `OnboardingViewModel`, which has no concept of auth state.

#### File 2: `ShopFlowNavGraph.kt` — Lines 87–104

```kotlin
composable(Route.Splash.route) {
    SplashScreen(
        onGetStarted = {
            navController.navigate(Route.Onboarding.route) { ... }
        },
        onSignIn = {
            navController.navigate(Route.Login.route) { ... }
        },
        onAutoNavigateHome = {
            navController.navigate(Route.Home.route) { ... }  // ← No guard
        }
    )
}
```

The NavGraph blindly navigates to Home when `onAutoNavigateHome` fires. There is no interceptor or auth-guard at the `Route.Home` destination either.

#### File 3: `AuthRepositoryImpl.kt` — Lines 30, 33–38

```kotlin
private val authState = MutableStateFlow(false)  // ← Defaults to false

init {
    repositoryScope.launch {
        tokenDataStore.accessTokenFlow.collect { storedToken ->
            authState.value = !storedToken.isNullOrBlank()
        }
    }
}
```

The `authState` flow correctly reacts to stored tokens, but **nothing in the Splash/NavGraph ever reads it** for routing decisions.

### Step-by-Step Fix

**1. Create a `SplashViewModel` that reads BOTH onboarding AND auth state:**

```kotlin
// New file: presentation/screens/splash/SplashViewModel.kt
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    data class SplashDecision(
        val isReady: Boolean = false,
        val hasCompletedOnboarding: Boolean = false,
        val isAuthenticated: Boolean = false
    )

    val decision: StateFlow<SplashDecision> = combine(
        preferencesRepository.getPreferences().map { it.onboardingCompleted },
        authRepository.getAuthState()
    ) { onboarded, authed ->
        SplashDecision(isReady = true, hasCompletedOnboarding = onboarded, isAuthenticated = authed)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SplashDecision())
}
```

**2. Update `SplashScreen` to use the new ViewModel:**

```kotlin
LaunchedEffect(decision) {
    if (!decision.isReady) return@LaunchedEffect
    delay(2500)
    when {
        !decision.hasCompletedOnboarding -> onGetStarted()
        !decision.isAuthenticated -> onSignIn()
        else -> onAutoNavigateHome()
    }
}
```

**3. Hide the "Get Started" / "Sign In" buttons behind a non-ready state** so the user doesn't tap them before the decision resolves.

---

## 3. Navigation & Runtime Crashes

### 3.1 Product Click Crash

#### Symptom

Tapping any product card on the Home screen causes an immediate crash (or a blank loading state that never resolves).

#### Root Cause

**Shopify GID format mismatch.** Shopify product IDs are in GID format:
```
gid://shopify/Product/7654321012345
```

When `HomeScreen` navigates to `ProductDetail`:

```kotlin
// ShopFlowNavGraph.kt:141-142
onNavigateToProductDetail = { productId ->
    navController.navigate(Route.ProductDetail.createRoute(productId))
}
```

```kotlin
// Routes.kt:16
fun createRoute(productId: String): String = "product/$productId"
```

This produces a navigation route like:
```
product/gid://shopify/Product/7654321012345
```

The `://` and `/` characters in the GID **corrupt the route path**. Navigation framework interprets them as path separators, causing either:
1. A `IllegalArgumentException` — no matching composable found for the mangled route.
2. The route partially matches but the argument extracted is truncated to `gid:` only.

#### Secondary Crash Vector

In `ProductDetailViewModel.kt` — Line 34:

```kotlin
private val productId: String = checkNotNull(savedStateHandle["productId"])
```

`checkNotNull` will throw `IllegalStateException` if the argument is null — which happens when the route parsing fails or when the argument key doesn't match due to the GID corruption.

#### Step-by-Step Fix

**Option A (Recommended): URL-encode the productId before navigating:**

```kotlin
// Routes.kt — update createRoute
import java.net.URLEncoder
import java.net.URLDecoder

data object ProductDetail : Route {
    const val ARG_PRODUCT_ID = "productId"
    override val route: String = "product/{$ARG_PRODUCT_ID}"
    fun createRoute(productId: String): String =
        "product/${URLEncoder.encode(productId, "UTF-8")}"
}
```

**And decode it in the ViewModel:**

```kotlin
// ProductDetailViewModel.kt
private val productId: String = URLDecoder.decode(
    checkNotNull(savedStateHandle["productId"]), "UTF-8"
)
```

**Option B: Use NavType-safe approach by defining `navArgument` with `NavType.StringType`** and passing the ID as a query parameter instead of a path segment:

```kotlin
// Routes.kt
override val route: String = "product?productId={productId}"
fun createRoute(productId: String): String =
    "product?productId=${Uri.encode(productId)}"
```

```kotlin
// ShopFlowNavGraph.kt — update the composable
composable(
    route = Route.ProductDetail.route,
    arguments = listOf(navArgument(Route.ProductDetail.ARG_PRODUCT_ID) {
        type = NavType.StringType
    })
) { ... }
```

### 3.2 OrderConfirmation Route — Same GID Issue

The same URL-encoding problem exists for `Route.OrderConfirmation`:

```kotlin
// Routes.kt:24-25
override val route: String = "order-confirmation/{$ARG_ORDER_ID}"
fun createRoute(orderId: String): String = "order-confirmation/$orderId"
```

Apply the same URL-encoding fix here.

---

## 4. Data Flow & ViewModel Architecture

### 4.1 Home Screen Renders Empty

#### Symptom

The Home screen shows "Good morning, Guest" with no collections, no featured products, and no banners (only the mock Picsum URLs render).

#### Root Cause Chain

```
HomeViewModel.fetchHomeData()
  ├── getCollectionsUseCase()  → ProductRepository.getCollections()  → ShopifyDataSource.fetchCollections()
  ├── getFeaturedProductsUseCase() → ProductRepository.getFeaturedProducts() → ShopifyDataSource.fetchFeaturedProducts()
  └── getCustomerProfileUseCase() → AuthRepository.getStoredAccessToken() → returns NULL (not logged in)
                                                                             → returns GraphQLError("No authenticated customer session found")
```

**Issue 1: Empty Shopify credentials produce empty API results.**

The `secrets.properties` file exists and contains a domain + token. However, the `BuildConfig` fields are injected at **build time**:

```kotlin
// app/build.gradle.kts:35-36
buildConfigField("String", "SHOPIFY_STORE_DOMAIN", "\"${getSecret("SHOPIFY_STORE_DOMAIN")}\"")
buildConfigField("String", "SHOPIFY_STOREFRONT_ACCESS_TOKEN", "\"${getSecret("SHOPIFY_STOREFRONT_ACCESS_TOKEN")}\"")
```

If the store has no products configured in Shopify Admin, or if the Storefront API version is wrong, Apollo will return successful but **empty** responses (`data.products.edges = []`). The app maps this correctly to empty lists — which is technically not an error, just no data.

**Issue 2: Customer name is always empty (unauthenticated).**

`getCustomerProfileUseCase()` calls `authRepository.getStoredAccessToken()`. Since the user was never required to log in (see Issue #2), this returns `null`, causing the use case to return `GraphQLError`. The HomeViewModel then sets `name = ""`:

```kotlin
// HomeViewModel.kt:73-75
if (profileResult is ApiResult.Success) {
    name = profileResult.data.firstName
}
// Else: name stays "" — "Guest" is displayed
```

**Issue 3: No loading indicator for initial data fetch.**

`HomeUiState.isLoading` defaults to `false` (line 23). While `fetchHomeData()` sets it to `true`, there is no loading UI in `HomeScreen` — the screen immediately renders the empty state without any feedback.

#### Step-by-Step Fix

1. **Verify Shopify store has products:** Open Shopify Admin for `shopflow-dev-sf2odppv.myshopify.com` and ensure at least one published product exists. The Storefront API only surfaces published products.

2. **Verify API version:** `NetworkModule.kt:39` uses API version `2024-10`. Ensure this version is still supported by the store's Shopify plan. Consider updating to `2025-01`.

3. **Add a loading state to HomeScreen:**

```kotlin
// HomeScreen.kt — wrap content in a conditional
if (uiState.isLoading) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = NeonMagenta)
    }
} else {
    // Existing content
}
```

4. **Handle the unauthenticated profile gracefully:** Don't call `getCustomerProfileUseCase()` if there's no token. Or, catch the error silently:

```kotlin
// HomeViewModel.kt — guard the profile call
val token = authRepository.getStoredAccessToken()
if (token != null) {
    val profileResult = getCustomerProfileUseCase()
    if (profileResult is ApiResult.Success) {
        name = profileResult.data.firstName
    }
}
```

### 4.2 Settings Screen Renders Default Preferences

#### Symptom

The Settings screen renders, but all toggles show default values and changes may not persist across app restarts.

#### Root Cause

This one actually **works correctly**. `SettingsViewModel` reads from `PreferencesRepository` via a `StateFlow` with `SharingStarted.WhileSubscribed(5000)` and an `initialValue` of `UserPreferences()` (defaults). The data is correctly wired:

```
SettingsViewModel.preferences
  └── preferencesRepository.getPreferences()  ← DataStore Flow (works)
```

The "empty" appearance is because the initial `UserPreferences()` data class uses sensible defaults. This is correct behavior — not a bug. The toggles will reflect stored values once the DataStore Flow emits.

**However, there is a minor issue:** If the user has never toggled anything, the default values display. This is expected UX, but the "Dark Theme" toggle should reflect the actual app theme, which is always dark. Fix: Set `themeMode = "DARK"` as the default in `UserPreferences`:

```kotlin
data class UserPreferences(
    val themeMode: String = "DARK",  // ← Match the actual app theme
    // ...
)
```

### 4.3 Profile Screen Shows Empty State

Same root cause as 4.1 — `GetCustomerProfileUseCase` returns `GraphQLError` because there's no stored access token (user was never forced to log in). The profile screen shows a spinner forever or falls through to the error branch.

---

## 5. UI Interactivity Audit

### Complete Inventory of Dead/Empty `onClick` Lambdas

| # | File | Line | Component | Empty Lambda | Impact |
|---|------|------|-----------|-------------|--------|
| 1 | `SettingsScreen.kt` | 113 | `NavigationRow("Change Password")` | `{}` | Clicking does nothing |
| 2 | `SettingsScreen.kt` | 115 | `NavigationRow("Privacy Settings")` | `{}` | Clicking does nothing |
| 3 | `SettingsScreen.kt` | 117 | `NavigationRow("Help Center")` | `{}` | Clicking does nothing |
| 4 | `SettingsScreen.kt` | 119 | `NavigationRow("Log Out", isDestructive = true)` | `{}` | **CRITICAL:** Logout button does nothing |
| 5 | `ProfileScreen.kt` | 138 | `NavigationRow("Saved Addresses")` | `{}` | Clicking does nothing |
| 6 | `ProfileScreen.kt` | 139 | `NavigationRow("Payment Methods")` | `{}` | Clicking does nothing |
| 7 | `ProfileScreen.kt` | 141 | `NavigationRow("Rewards & Points")` | `{}` | Clicking does nothing |
| 8 | `LoginScreen.kt` | 165 | `"Forgot Password?"` text | `{ /* Handle forgot password */ }` | Clicking does nothing |
| 9 | `LoginScreen.kt` | 187 | `OutlinedButton("Continue with Google")` | `{ /* Handle Google Login */ }` | Clicking does nothing |
| 10 | `LoginScreen.kt` | 195 | `OutlinedButton("Continue with Apple")` | `{ /* Handle Apple Login */ }` | Clicking does nothing |
| 11 | `RegisterScreen.kt` | 241 | `OutlinedButton("Sign up with Google")` | `{ /* Handle Google Login */ }` | Clicking does nothing |
| 12 | `HomeScreen.kt` | 198 | `CategoryIcon(onClick)` | `{ /* Navigate to collection */ }` | Clicking category does nothing |
| 13 | `HomeScreen.kt` | 142, 224 | `ProductCard.onWishlistToggle` | `{}` | Heart icon does nothing |
| 14 | `HomeScreen.kt` | 143, 225 | `ProductCard.onAddToCart` | `{}` | Add-to-cart icon does nothing |
| 15 | `ProductDetailScreen.kt` | 134 | `Share IconButton` | `{ /* Share */ }` | Share icon does nothing |
| 16 | `WishlistScreen.kt` | 84 | `ProductCard.onAddToCart` | `{ /* Add to cart */ }` | Add-to-cart on wishlist items does nothing |
| 17 | `HomeScreen.kt` | 188 | `"See All" Text` | `.clickable { }` | "See All" does nothing |

### Priority Actions

| Priority | Action | Files Affected |
|----------|--------|---------------|
| **CRITICAL** | Wire "Log Out" to `AuthRepository.logout()` + navigate to Splash | `SettingsScreen.kt`, `SettingsViewModel.kt` |
| **HIGH** | Wire `onWishlistToggle` on Home/ProductDetail to `WishlistRepository` | `HomeScreen.kt`, `HomeViewModel.kt` |
| **HIGH** | Wire `onAddToCart` on Home/Wishlist to `CartRepository.addToCart()` | `HomeScreen.kt`, `WishlistScreen.kt`, `HomeViewModel.kt` |
| **HIGH** | Wire Share button to Android share intent | `ProductDetailScreen.kt` |
| **MEDIUM** | Wire Category click to collection-filtered product list | `HomeScreen.kt`, `ShopFlowNavGraph.kt` |
| **LOW** | Google/Apple login (requires OAuth integration) | `LoginScreen.kt`, `RegisterScreen.kt` |
| **LOW** | Forgot Password, Saved Addresses, Payment Methods, Rewards | Various |

### Example Fix — "Log Out" Button

```kotlin
// SettingsViewModel.kt — add logout function
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val authRepository: AuthRepository  // ← ADD
) : ViewModel() {

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            preferencesRepository.setOnboardingCompleted(false) // Reset onboarding
            onLoggedOut()
        }
    }
}

// SettingsScreen.kt — pass navController callback
NavigationRow("Log Out", isDestructive = true) {
    viewModel.logout { onNavigateToSplash() }  // ← Wire it
}
```

---

## 6. Severity Summary Matrix

| ID | Category | Severity | Issue Summary |
|----|----------|----------|--------------|
| G1 | Gradle | **MEDIUM** | `import` before `plugins {}` breaks IDE sync |
| A1 | Auth/Routing | **CRITICAL** | Splash bypasses auth check — unauthenticated users reach Home |
| A2 | Auth/Routing | **CRITICAL** | No `SplashViewModel` — routing logic is incomplete |
| N1 | Navigation | **CRITICAL** | Product click crash — Shopify GID corrupts route path |
| N2 | Navigation | **HIGH** | OrderConfirmation route has same GID encoding issue |
| D1 | Data Flow | **HIGH** | Home screen empty — profile call fails without auth token |
| D2 | Data Flow | **MEDIUM** | No loading indicator on Home initial load |
| D3 | Data Flow | **LOW** | Default UserPreferences `themeMode` mismatches actual theme |
| U1 | UI Interactivity | **CRITICAL** | Log Out button is a no-op |
| U2 | UI Interactivity | **HIGH** | 6 interactive buttons on Home are no-ops (wishlist, cart, categories) |
| U3 | UI Interactivity | **MEDIUM** | 3 Settings navigation rows are no-ops |
| U4 | UI Interactivity | **MEDIUM** | 3 Profile navigation rows are no-ops |
| U5 | UI Interactivity | **LOW** | Social login buttons (Google/Apple) are stubs |

### Recommended Fix Order

```
1. G1 — Fix Gradle import (5 min, unblocks IDE productivity)
2. N1 — Fix product ID URL-encoding (15 min, unblocks core shopping flow)
3. A1+A2 — Create SplashViewModel with auth check (30 min, fixes security)
4. U1 — Wire logout button (10 min, fixes critical user flow)
5. D1 — Guard profile call in HomeViewModel (10 min, fixes empty state)
6. U2 — Wire wishlist/cart buttons on Home (30 min, enables commerce)
7. N2 — Fix OrderConfirmation GID encoding (5 min, same pattern as N1)
8. D2 — Add loading indicator to HomeScreen (10 min, UX polish)
9. U3+U4 — Wire remaining Settings/Profile rows (varies, feature work)
```

---

> **End of Audit.** This document is read-only analysis. No files were modified.
> Would you like me to begin implementing fixes for the top CRITICAL issues?
