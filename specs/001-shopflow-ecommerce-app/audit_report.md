# ShopFlow Codebase Audit Report — Phases 1–7

**Generated**: 2026-04-23  
**Scope**: Cross-reference of `tasks.md` (T001–T161) against actual Kotlin source files  
**Authority**: `CONSTITUTION.md` v1.0.0

---

## Executive Summary

The project compiles successfully and all 161 tasks are marked complete. However, a deep audit reveals **6 CRITICAL**, **11 HIGH**, and **10 MEDIUM** issues. The most severe problems are:

1. **Hardcoded mock access tokens** in 4 domain UseCases — breaks authentication flow entirely.
2. **OrderHistory route still a placeholder** in NavGraph — screen was built but never wired.
3. **Pervasive `Color.White` / `Color.Red` / `Color.Gray` usage** instead of `ShopFlowTheme` tokens — constitution violation.
4. **Zero `@Preview` annotations** on any screen composable (only `SplashScreen` has one) — constitution violation.
5. **Zero `stringResource()` calls** — every user-facing string is hardcoded in Kotlin — blocks i18n.
6. **`OrderStatusWorker` is a no-op** — does nothing but `delay(1000)`.

---

## Findings Table

| ID | Category | Severity | Location(s) | Summary | Recommendation |
|----|----------|----------|-------------|---------|----------------|
| C1 | Mock Data | **CRITICAL** | `GetOrderHistoryUseCase.kt:12`, `GetOrderUseCase.kt:15`, `GetCustomerProfileUseCase.kt:16`, `UpdateCustomerProfileUseCase.kt:15` | All 4 use cases hardcode `val accessToken = "mock-access-token"` instead of reading from `TokenDataStore` via a repository. The Shopify API will reject every call. | Inject `PreferencesRepository` or a dedicated `TokenRepository` and read the real stored token. |
| C2 | NavGraph | **CRITICAL** | `ShopFlowNavGraph.kt:194` | `Route.OrderHistory` is still `PlaceholderScreen("Order History")` despite `OrderHistoryScreen` being fully implemented and imported. Task T150 is marked `[x]` but the work was never done. | Replace with `OrderHistoryScreen(onNavigateBack = …, onNavigateToOrderDetails = …)`. |
| C3 | NavGraph | **CRITICAL** | `ShopFlowNavGraph.kt:145` | Home screen `onNavigateToNotifications` callback is a no-op `// TODO: Navigate to notifications`. The Notifications screen exists and is wired at `Route.Notifications`, but Home's bell icon does nothing. | Change to `navController.navigate(Route.Notifications.route)`. |
| C4 | NavGraph | **CRITICAL** | `ShopFlowNavGraph.kt:149` | `Route.Search` is still `PlaceholderScreen("Search")`. This is a bottom-nav tab with no implementation. | Implement `SearchScreen` or redirect to HomeScreen with search focused. |
| C5 | Constitution | **CRITICAL** | All screens except `SplashScreen` | Constitution Principle I mandates: "Compose previews MUST accompany every public composable." Zero `@Preview` annotations exist on any screen (Home, Cart, Profile, Orders, Settings, Wishlist, Notifications, Checkout, Onboarding, Login, Register, ProductDetail, OrderConfirmation). Only `SplashScreen` has one. | Add `@Preview` to every screen composable. |
| C6 | Implementation | **CRITICAL** | `OrderStatusWorker.kt:17-27` | Worker body is a no-op (`delay(1000); return Result.success()`). It does not inject `OrderRepository`, does not query Shopify, and does not create local notifications. The task description says "polling Shopify for order status changes, creates local notifications on change." | Inject `OrderRepository` + `NotificationRepository`, implement real polling logic. |
| H1 | Design System | **HIGH** | 50+ occurrences across 14 screen files | `Color.White` used directly instead of `TextPrimary` (`#F4F4F5`). Constitution Principle VI: "Raw `MaterialTheme` colors MUST NOT be used directly in screens." `Color.White` is `#FFFFFF`, not the design token `#F4F4F5`. | Replace all `Color.White` with `TextPrimary` or `ShopFlowTheme.colors.textPrimary`. |
| H2 | Design System | **HIGH** | `SettingsScreen.kt:167`, `OrderHistoryScreen.kt:162` | `Color.Red` used directly for destructive/cancelled states. No `StatusCancelled` token exists in `Color.kt`. | Add `val StatusCancelled = Color(0xFFEF4444)` to `Color.kt` and use it. |
| H3 | Design System | **HIGH** | `SettingsScreen.kt:69,71,73,113,115,117` | `Color.DarkGray` used for dividers — this is a system default, not a design token. | Define a `DividerColor` token in `Color.kt` (e.g., `Color(0xFF27272A)`) and use it. |
| H4 | Design System | **HIGH** | `WishlistScreen.kt:65,100,121` | `Color.Gray` used for empty state and label text. This is `#888888`, which is not `TextSecondary` (`#A1A1AA`). | Replace with `TextSecondary`. |
| H5 | Design System | **HIGH** | `WishlistScreen.kt:94` | Raw `Color(0xFF1A1A1A)` hardcoded for bottom sheet container. This is not a theme token. | Use `SurfaceGlassElevated` or add a dedicated `SurfaceBottomSheet` token. |
| H6 | Design System | **HIGH** | `OnboardingScreen.kt:89` | Placeholder text `"Image $page"` instead of actual onboarding illustrations. The spec calls for real slide content. | Use `generate_image` or add actual drawable assets for onboarding pages. |
| H7 | Hardcoded Data | **HIGH** | `HomeScreen.kt:81,89-90,99` | Avatar URL hardcoded (`pravatar.cc`), user name hardcoded as `"Alex"`, notification count hardcoded as `"2"`. Should be driven by ViewModel state from `CustomerRepository` + `NotificationRepository`. | Wire `HomeViewModel` to fetch customer name/avatar from profile, and unread count from notification repo. |
| H8 | Hardcoded Data | **HIGH** | `CartRepositoryImpl.kt:84` | Currency hardcoded as `"USD"`. Should be derived from Shopify store settings or user locale. | Read currency from the Shopify store configuration or cart line items. |
| H9 | Constitution | **HIGH** | All screen files | Zero `stringResource()` calls found. Constitution mandates: "Resource strings remain in `strings.xml` for i18n compliance." Every single user-facing string (button labels, titles, placeholders, error messages) is hardcoded in Kotlin. | Extract all strings to `strings.xml` and use `stringResource(R.string.…)`. |
| H10 | Architecture | **HIGH** | `WishlistScreen.kt:83` | `onAddToCart = { /* Add to cart */ }` — the add-to-cart callback from wishlist items is a no-op. Users cannot add wishlist items to cart. | Wire to `CartRepository.addToCart()` via ViewModel. |
| H11 | Duplicate Code | **HIGH** | `ui/theme/Color.kt`, `ui/theme/Theme.kt`, `ui/theme/Type.kt` | Legacy `ui.theme` package exists alongside the canonical `presentation.theme` package. These are dead files that should be deleted. | Delete the `app/src/main/java/com/shopflow/app/ui/theme/` directory entirely. |
| M1 | Constitution | **MEDIUM** | All domain classes | Constitution mandates "All public APIs MUST have KDoc documentation." Zero KDoc comments found on any domain model, repository interface, or use case. | Add KDoc to all public classes and functions. |
| M2 | Tasks Integrity | **MEDIUM** | `tasks.md:168-170` | Onboarding tasks T111–T113 are marked `[ ]` (unchecked) but the files exist and are functional. Tasks should be marked `[x]`. | Update tasks.md to mark T111-T113 as complete. |
| M3 | Implementation | **MEDIUM** | `CheckoutScreen.kt` | Checkout uses `Intent.ACTION_VIEW` to open a URL in browser, then immediately calls `onNavigateBack()`. No deep-link callback handles return from Shopify checkout. Order confirmation is unreachable via this flow. | Implement Shopify checkout callback handling or Chrome Custom Tab with result. |
| M4 | Architecture | **MEDIUM** | `ProductDetailScreen.kt:236-237` | Color swatches are hardcoded as `"White" to Color.White, "Red" to Color.Red`. Should be derived from `Product.variants[].selectedOptions`. | Parse color options from product variant data. |
| M5 | Design System | **MEDIUM** | `OnboardingScreen.kt:96`, `ProductDetailScreen.kt:177`, `LoginScreen.kt:93`, `RegisterScreen.kt:77` | `MaterialTheme.typography.headlineMedium` used directly. Constitution says these SHOULD go through `ShopFlowTheme`. While the theme wraps MaterialTheme, screens should prefer custom token access. | Access via `ShopFlowTheme` typography tokens for consistency. |
| M6 | Architecture | **MEDIUM** | `WishlistScreen.kt:124-128` | `RangeSlider` value is local `remember` state and never syncs to ViewModel. Closing and reopening the filter sheet resets the slider. The slider `onValueChange` updates local state but never calls `viewModel.setPriceRangeFilter()` until "Apply" is clicked — which is correct UX, but the range is lost on sheet dismiss. | Seed slider from ViewModel state (`uiState.priceRange`). |
| M7 | Implementation | **MEDIUM** | `NotificationScreen.kt` | No `GlassmorphismCard` used despite task T157 requiring it. Uses plain `Card` with `SurfaceGlass`. | Wrap notification items in `GlassmorphismCard` composable. |
| M8 | Implementation | **MEDIUM** | `OrderHistoryScreen.kt` | Task T149 requires "Track Order" `OutlinedButton` on each order card. None exists. | Add `OutlinedButton(text = "Track Order")` to `OrderCard`. |
| M9 | Implementation | **MEDIUM** | `OrderHistoryScreen.kt` | Task T149 requires `StatusBadge` component for order status. Uses inline `Box` with `CircleShape` + raw text instead. | Replace inline status indicator with the `StatusBadge` composable from `presentation/components/`. |
| M10 | Deprecated API | **MEDIUM** | 6 screen files | `Icons.Default.ArrowBack` is deprecated in Material 3. Should use `Icons.AutoMirrored.Filled.ArrowBack`. | Replace all occurrences. |

---

## Coverage Summary

| Phase | Task Range | Marked Done | Verified in Code | Issues |
|-------|-----------|-------------|-----------------|--------|
| 1: Core Architecture | T001–T024 | ✅ All | ✅ Files exist | — |
| 2: Design System | T025–T040 | ✅ All | ✅ All components exist | Legacy `ui/theme` package remains (H11) |
| 3: GraphQL Network | T041–T081 | ✅ All | ✅ Files exist | — |
| 4: Local Storage | T082–T099 | ✅ All | ✅ Files exist | — |
| 5: Auth Flow | T100–T110 | ✅ All | ✅ Files exist | No `@Preview` (C5) |
| 6: Shopping Flow | T111–T135 | ⚠️ T111-T113 unchecked | ✅ Files exist | Onboarding tasks not marked done (M2) |
| 7: Account Features | T136–T161 | ✅ All | ⚠️ OrderHistory not wired | OrderHistory placeholder (C2), Worker no-op (C6) |

---

## Constitution Alignment Issues

| Principle | Status | Details |
|-----------|--------|---------|
| I. Kotlin-First, Compose-Only | ⚠️ VIOLATION | `@Preview` missing on all screen composables |
| II. Strict MVVM + Clean Architecture | ⚠️ VIOLATION | Domain UseCases contain hardcoded mock data instead of injecting repositories |
| III. Dagger Hilt DI | ✅ Compliant | All ViewModels use `@HiltViewModel`, repositories use `@Inject` |
| IV. StateFlow / SharedFlow | ✅ Compliant | All state exposed via `StateFlow`, collected via `collectAsState()` |
| V. Shopify Storefront API | ⚠️ VIOLATION | Mock access tokens bypass real API authentication |
| VI. Midnight Cyber-Chic | ⚠️ VIOLATION | `Color.White`, `Color.Red`, `Color.Gray`, `Color.DarkGray`, raw hex literals used in screens |
| VII. SOLID / DRY / KISS | ⚠️ MINOR | No KDoc documentation on public APIs |

---

## Priority Fix Order

### Tier 1 — Must fix before any demo/testing (CRITICAL)

1. **C1**: Replace `"mock-access-token"` with real token retrieval in all 4 UseCases
2. **C2**: Wire `OrderHistoryScreen` into NavGraph (replace placeholder)
3. **C3**: Wire Home bell icon to `Route.Notifications`
4. **C6**: Implement real `OrderStatusWorker` logic or remove `@HiltWorker` annotation

### Tier 2 — Constitution compliance (HIGH)

5. **H1**: Replace all `Color.White` → `TextPrimary` across all 14 screen files
6. **H2/H3/H4/H5**: Replace all raw color references with theme tokens
7. **H7**: Remove hardcoded "Alex", avatar URL, notification count from HomeScreen
8. **H9**: Extract all hardcoded strings to `strings.xml`
9. **H11**: Delete legacy `ui/theme/` package
10. **C5**: Add `@Preview` to all screen composables

### Tier 3 — Spec compliance & polish (MEDIUM)

11. **M2**: Mark T111–T113 as complete in tasks.md
12. **C4**: Implement Search screen (or merge with Home)
13. **H6**: Replace onboarding placeholder images with real assets
14. **H10**: Wire wishlist add-to-cart action
15. **M7/M8/M9**: Use `GlassmorphismCard`, `StatusBadge`, `OutlinedButton` in OrderHistory/Notifications per spec

---

## Metrics

| Metric | Value |
|--------|-------|
| Total Tasks (T001–T161) | 161 |
| Tasks Marked Complete | 158 / 161 (T111-T113 unchecked but implemented) |
| Task Accuracy (code matches claim) | 155 / 161 (96.3%) |
| CRITICAL Issues | 6 |
| HIGH Issues | 11 |
| MEDIUM Issues | 10 |
| Screens with `@Preview` | 1 / 14 (7.1%) |
| Screens using `stringResource()` | 0 / 14 (0%) |
| Design token violations | 50+ raw color usages across 14 files |
| Mock data instances | 4 hardcoded tokens + 3 hardcoded UI values |

---

## Next Actions

> [!CAUTION]
> **6 CRITICAL issues must be resolved before Phase 8 or any release testing.**

1. **Immediate**: Fix C1–C3, C6 (mock tokens, NavGraph wiring, worker no-op)
2. **Before Phase 8**: Fix H1–H11 (design system + constitution compliance)
3. **During Phase 8**: Address M1–M10 as part of polish tasks (T162–T178 already cover some)
4. **Manual edit**: Update `tasks.md` to mark T111–T113 as `[x]`

---

*Would you like me to suggest concrete remediation edits for the top N issues?*

*This audit is read-only. No files were modified.*
