# Tasks: ShopFlow E-Commerce App

**Input**: Design documents from `specs/001-shopflow-ecommerce-app/`
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/
**Scope**: Phase 1 — Core Architecture & Project Scaffolding ONLY
**Subsequent phases**: Pending architect review

## Format: `[ID] [P?] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- Include exact file paths in descriptions

---

## Phase 1: Setup — Android Project Initialization

**Purpose**: Create the Android project skeleton with correct Gradle config,
dependency declarations, and Compose + Hilt plugin wiring.

- [x] T001 [COMPLETED MANUALLY BY USER] Create new Android project named ShopFlow with application ID `com.shopflow.app`, min SDK 26, target SDK latest, Compose activity template. The project is already created in the root directory. ✅ 2026-04-22
- [x] T002 Create version catalog file at `gradle/libs.versions.toml` with all dependency versions and library aliases: Kotlin, Compose BOM, Compose UI/Material3/Navigation/Animation, Apollo Kotlin, Dagger Hilt, Room, DataStore, Coil, WorkManager, Coroutines, OkHttp, JUnit5, MockK, Turbine, Compose Testing
- [x] T003 Configure root `build.gradle.kts` with Kotlin, Hilt, Apollo, Room KSP plugins (apply false) and version catalog references
- [x] T004 Configure `app/build.gradle.kts` with all plugins (kotlin-android, hilt, ksp, apollo, compose), dependency declarations from version catalog, Java 17 target, buildConfig for Shopify secrets, and Compose compiler options
- [x] T005 Create `secrets.properties` template file at project root with placeholder keys `SHOPIFY_STORE_DOMAIN` and `SHOPIFY_STOREFRONT_ACCESS_TOKEN`; add `secrets.properties` to `.gitignore`
- [x] T006 Create `app/proguard-rules.pro` with baseline keep rules for Apollo, Hilt, Room, and Kotlin serialization

**Checkpoint**: Project syncs in Android Studio with zero Gradle errors.

---

## Phase 2: Foundational — Core App Shell & DI Framework

**Purpose**: Application class, single Activity, Hilt module stubs, and
the package structure enforcing Clean Architecture layers.

**⚠️ CRITICAL**: All subsequent phases depend on this foundation.

- [x] T007 Create base package directory structure under `app/src/main/java/com/shopflow/app/`: `data/remote/`, `data/remote/interceptor/`, `data/local/`, `data/local/dao/`, `data/local/entity/`, `data/local/datastore/`, `data/repository/`, `data/mapper/`, `domain/model/`, `domain/repository/`, `domain/usecase/`, `presentation/navigation/`, `presentation/theme/`, `presentation/components/`, `presentation/screens/splash/`, `presentation/screens/onboarding/`, `presentation/screens/auth/`, `presentation/screens/home/`, `presentation/screens/product/`, `presentation/screens/cart/`, `presentation/screens/checkout/`, `presentation/screens/confirmation/`, `presentation/screens/profile/`, `presentation/screens/orders/`, `presentation/screens/wishlist/`, `presentation/screens/notifications/`, `presentation/screens/settings/`, `di/`
- [x] T008 Create `app/src/main/java/com/shopflow/app/ShopFlowApp.kt` — Application class annotated with `@HiltAndroidApp`, empty body
- [x] T009 Create `app/src/main/java/com/shopflow/app/MainActivity.kt` — Single activity annotated with `@AndroidEntryPoint`, `setContent { ShopFlowTheme { ShopFlowNavGraph() } }` skeleton with TODO placeholder for nav graph call
- [x] T010 Create `app/src/main/java/com/shopflow/app/di/NetworkModule.kt` — Hilt `@Module` `@InstallIn(SingletonComponent::class)` stub with `@Provides` placeholder for OkHttpClient and ApolloClient (return TODO stubs with inline comments describing future wiring)
- [x] T011 [P] Create `app/src/main/java/com/shopflow/app/di/DatabaseModule.kt` — Hilt `@Module` `@InstallIn(SingletonComponent::class)` stub with `@Provides` placeholder for Room database and DAOs (return TODO stubs)
- [x] T012 [P] Create `app/src/main/java/com/shopflow/app/di/RepositoryModule.kt` — Hilt `@Module` `@InstallIn(SingletonComponent::class)` stub with TODO comment listing future `@Binds` entries for each repository interface
- [x] T013 [P] Create `app/src/main/java/com/shopflow/app/di/DataStoreModule.kt` — Hilt `@Module` `@InstallIn(SingletonComponent::class)` stub with `@Provides` placeholder for DataStore<Preferences> instance

**Checkpoint**: App compiles. `ShopFlowApp` and `MainActivity` are wired. Hilt
modules are recognized (no compile-time Hilt errors).

---

## Phase 3: Foundational — ShopFlowTheme (Midnight Cyber-Chic Design System)

**Purpose**: Implement the complete design token system so all future UI
composables reference theme values — never raw literals.

- [x] T014 Create `app/src/main/java/com/shopflow/app/presentation/theme/Color.kt` — Define all color constants: `TrueBlack = Color(0xFF09090B)`, `NeonMagenta = Color(0xFFFF2D78)`, `ElectricViolet = Color(0xFF7B2FFF)`, `TextPrimary = Color(0xFFF4F4F5)`, `TextSecondary = Color(0xFFA1A1AA)`, `SurfaceGlass = Color(0x0AFFFFFF)`, `SurfaceGlassElevated = Color(0x14FFFFFF)`, `StatusDelivered = Color(0xFF22C55E)`, `StatusProcessing = Color(0xFFF59E0B)`, `StatusShipped = Color(0xFF06B6D4)`, plus light theme counterpart colors. Define `ShopFlowGradient` as a `Brush.horizontalGradient(listOf(NeonMagenta, ElectricViolet))`.
- [x] T015 Create `app/src/main/java/com/shopflow/app/presentation/theme/Type.kt` — Define `ShopFlowTypography` using Material3 `Typography()` with Google Fonts provider for Inter (or Outfit). Set display (~32sp bold), headlineMedium (~22sp bold), titleMedium (~20sp bold), bodyLarge (~15sp), bodyMedium (~14sp), labelLarge (~13sp), labelSmall (~10sp). All using the custom font family.
- [x] T016 [P] Create `app/src/main/java/com/shopflow/app/presentation/theme/Shape.kt` — Define `ShopFlowShapes` with `RoundedCornerShape` values: `CardShape = 16.dp`, `HeroShape = 20.dp`, `PillShape = 28.dp`, `ChipShape = 12.dp`, `InputShape = 14.dp`.
- [x] T017 Create `app/src/main/java/com/shopflow/app/presentation/theme/ShopFlowTheme.kt` — Composable function wrapping `MaterialTheme` with custom `darkColorScheme()` using Color.kt values as the default, `lightColorScheme()` as alternate, ShopFlowTypography, ShopFlowShapes. Expose `ShopFlowTheme.colors`, `ShopFlowTheme.gradient` via `CompositionLocal` or a custom object for direct access to non-Material tokens (gradient, glass surfaces). Include `@Preview` with sample content.

**Checkpoint**: `ShopFlowTheme` composable compiles. Preview renders true black
background with magenta accent text.

---

## Phase 4: Foundational — Navigation Graph & Route Definitions

**Purpose**: Define all app routes and wire the NavHost with placeholder
screens so the full navigation skeleton is in place.

- [x] T018 Create `app/src/main/java/com/shopflow/app/presentation/navigation/Routes.kt` — Sealed class/interface `Route` with data objects for each screen: `Splash`, `Onboarding`, `Login`, `Register`, `Home`, `ProductDetail(productId: String)`, `Cart`, `Checkout`, `OrderConfirmation(orderId: String)`, `Profile`, `OrderHistory`, `Wishlist`, `Notifications`, `Settings`. Each route has a `route: String` property.
- [x] T019 Create `app/src/main/java/com/shopflow/app/presentation/navigation/BottomNavBar.kt` — Composable implementing a `NavigationBar` (Material3) with 5 items: Home (house icon), Search (magnifier), Wishlist (heart), Cart (bag), Profile (person). Active state: magenta filled circle behind icon (use `Box` with gradient background + `clip(CircleShape)`). Inactive: zinc icon, no background. No text labels. Accept `currentRoute` and `onNavigate` lambda params. Include `@Preview`.
- [x] T020 Create `app/src/main/java/com/shopflow/app/presentation/navigation/ShopFlowNavGraph.kt` — Composable with `Scaffold` (containing `BottomNavBar`) and `NavHost`. Register `composable` entries for every Route, each rendering a temporary placeholder `Box` with centered `Text` showing the screen name. Bottom nav visible only on Home, Search, Wishlist, Cart, Profile routes. Wire `navController` for tab switching. Include back-stack handling for bottom nav (single top, restore state).
- [x] T021 Update `app/src/main/java/com/shopflow/app/MainActivity.kt` — Replace TODO placeholder: call `ShopFlowNavGraph()` inside `ShopFlowTheme { ... }`. Set `startDestination` to `Route.Splash`. Verify edge-to-edge setup with `enableEdgeToEdge()`.

**Checkpoint**: App launches → shows "Splash" placeholder text on true black
background → can manually navigate to Home → bottom nav displays 5 icons →
tapping tabs switches between placeholder screens.

---

## Phase 5: Foundational — Splash Screen Implementation

**Purpose**: Build the actual Splash screen (first real UI) as a validation
that the theme, navigation, and Compose pipeline are fully operational.

- [x] T022 Create `app/src/main/java/com/shopflow/app/presentation/screens/splash/SplashScreen.kt` — Composable implementing the splash layout per `spec.md` Screen 01: true black background with radial magenta glow (`radialGradient` behind logo area), centered ShopFlow logo block (rounded-square icon ~64dp with gradient fill + white sparkle, "Shop" in white bold + "Flow" in gradient bold ~32sp), tagline lines, page dot indicator, "Get Started →" `GradientButton` (magenta→violet gradient pill, ~56dp height, full-round corners), "Already have an account? Sign in" footer link. Use a 2–3 second `LaunchedEffect` delay on subsequent launches to auto-navigate to Home. Include `@Preview`.
- [x] T023 Update `app/src/main/java/com/shopflow/app/presentation/navigation/ShopFlowNavGraph.kt` — Replace the Splash placeholder `composable` entry with the real `SplashScreen`, passing `onGetStarted = { navController.navigate(Route.Onboarding) }` and `onSignIn = { navController.navigate(Route.Login) }` callbacks. Use `popUpTo(Route.Splash) { inclusive = true }` to prevent back-navigation to splash.

**Checkpoint**: App launches → ShopFlow branded splash screen with logo,
gradient glow, tagline, and "Get Started" button renders correctly on true
black → tapping "Get Started" navigates forward (to onboarding placeholder)
→ back press does not return to splash.

- [ ] T024 Configure `.gitignore` to exclude AI and local environment folders (e.g., `.agents/`, `.specify/`,`/docs`, `local.properties`, `secrets.properties`). Initialize Git repository, make an initial commit containing ONLY the clean application source code and documentation, add the remote origin (URL provided), and push to the main branch.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: No dependencies — start immediately
- **Phase 2 (App Shell & DI)**: Depends on Phase 1 (Gradle must sync)
- **Phase 3 (Theme)**: Depends on Phase 2 (needs package structure + app class)
- **Phase 4 (Navigation)**: Depends on Phase 3 (NavGraph uses ShopFlowTheme)
- **Phase 5 (Splash)**: Depends on Phase 4 (needs NavGraph + Routes)

### Strict Sequential Order

```text
T001 → T002 → T003 → T004 → T005 → T006
  └──→ T007 → T008 → T009 → T010
                         ├──→ T011 [P]
                         ├──→ T012 [P]
                         └──→ T013 [P]
  └──→ T014 → T015
         ├──→ T016 [P]
         └──→ T017
  └──→ T018 → T019 → T020 → T021
  └──→ T022 → T023
```

### Parallel Opportunities

```text
# After T010 completes, launch these in parallel:
T011: DatabaseModule.kt
T012: RepositoryModule.kt
T013: DataStoreModule.kt

# After T014 completes, launch in parallel:
T015: Type.kt
T016: Shape.kt
```

---

## Implementation Strategy

### Phase 1 MVP Validation

1. Complete all 23 tasks sequentially (with noted parallel opportunities)
2. **STOP and VALIDATE**: App must:
   - Build with zero errors (`./gradlew assembleDebug`)
   - Launch to the branded Splash screen
   - Navigate via bottom nav between placeholder screens
   - Display correct Midnight Cyber-Chic theme (true black, magenta accents)
3. If validation passes → Phase 1 is complete. Report back for Phase 2 task generation.

---

## Notes

- [P] tasks = different files, no dependencies on incomplete tasks
- All paths are relative to project root
- Commit after each logical task group (e.g., after all Gradle setup, after DI stubs, after theme)
- The Implementer Agent (Codex) should use `android-cli` for project creation (T001) and standard file operations for all other tasks
- No user story labels on Phase 1 tasks — these are all shared infrastructure
- Subsequent phases (2–8) will be generated after Phase 1 review
