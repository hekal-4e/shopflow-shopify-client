
---

## Phase 2: Design System & Reusable Components

**Goal**: Build the complete shared composable library implementing the
Midnight Cyber-Chic design system. Every component MUST use `ShopFlowTheme`
tokens — zero raw color/dimension literals.

**Prerequisites**: Phase 1 complete (T001–T024). Theme, navigation, and DI operational.

### Core Buttons

- [x] T025 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/GradientButton.kt` — Full-width pill button composable with `Brush.horizontalGradient(NeonMagenta → ElectricViolet)` background, white bold text (~16sp), height ~56dp, corner radius 28dp (full pill). Parameters: `text: String`, `onClick: () -> Unit`, `modifier: Modifier`, `enabled: Boolean`, `leadingIcon: ImageVector?`. Disabled state: 40% alpha gradient. Include `@Preview` on dark background.
- [x] T026 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/OutlinedButton.kt` — Full-width pill button composable with transparent background, 1dp zinc `#A1A1AA` border, white bold text (~16sp), height ~52dp, corner radius 28dp. Parameters: `text: String`, `onClick: () -> Unit`, `modifier: Modifier`. Include `@Preview` on dark background.

### Card Components

- [x] T027 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/GlassmorphismCard.kt` — Reusable card container composable with semi-transparent surface (`SurfaceGlass` / `SurfaceGlassElevated`), rounded corners (default 16dp), optional subtle 1dp magenta/violet border glow. Parameters: `modifier: Modifier`, `cornerRadius: Dp = 16.dp`, `glowBorder: Boolean = false`, `content: @Composable () -> Unit`. Include `@Preview` with sample child content.
- [x] T028 Create `app/src/main/java/com/shopflow/app/presentation/components/ProductCard.kt` — Product card composable wrapping `GlassmorphismCard` with: product image (~120dp height, `AsyncImage` via Coil), heart/wishlist icon button (top-right, zinc outline, toggleable to magenta filled), "Premium" label in magenta ~10sp, product name in white bold ~14sp, price row with magenta bold ~16sp (left) and circular magenta "+" add-to-cart `IconButton` (right, ~32dp). Parameters: `imageUrl: String`, `name: String`, `price: String`, `isPremium: Boolean`, `isWishlisted: Boolean`, `onWishlistToggle: () -> Unit`, `onAddToCart: () -> Unit`, `onClick: () -> Unit`, `modifier: Modifier`. Include `@Preview`.

### Search & Filter Components

- [x] T029 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/SearchBar.kt` — Glassmorphism pill composable: dark semi-transparent surface `SurfaceGlass`, rounded 28dp, height ~52dp, leading search icon (zinc), centered placeholder text in zinc ~14sp, trailing microphone icon inside a small magenta circular badge (~28dp). Parameters: `query: String`, `onQueryChange: (String) -> Unit`, `placeholder: String`, `onMicClick: () -> Unit`, `modifier: Modifier`. Uses `BasicTextField` internally with no default underline. Include `@Preview`.
- [x] T030 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/ChipSelector.kt` — Horizontal scrollable chip row composable using `LazyRow`. Each chip: pill-shaped (~32dp height, full-round corners). Selected state: magenta gradient fill + white text. Unselected state: dark surface + zinc text. Parameters: `options: List<String>`, `selectedOption: String`, `onSelect: (String) -> Unit`, `modifier: Modifier`. Include `@Preview` with sample options ("Popular", "Newest", "Price ↑", "Price ↓").

### Category & Navigation Components

- [x] T031 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/CategoryIcon.kt` — Circular icon composable (~52dp) with icon content inside. Active state: neon magenta ring border (2dp) + gradient-tinted fill. Inactive state: dark surface border. Label below in white ~11sp. Parameters: `icon: ImageVector`, `label: String`, `isActive: Boolean`, `onClick: () -> Unit`, `modifier: Modifier`. Include `@Preview` showing active and inactive states.
- [x] T032 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/NavigationRow.kt` — Full-width dark glassmorphism card row composable, rounded 16dp, ~64dp height, horizontal padding ~16dp. Left column: title in white bold ~15sp + subtitle in zinc ~12sp. Trailing: chevron "›" icon in zinc. Parameters: `title: String`, `subtitle: String`, `onClick: () -> Unit`, `modifier: Modifier`. Include `@Preview`.

### Checkout & Order Components

- [x] T033 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/StepIndicator.kt` — Horizontal 3-step progress composable. Each step: numbered circle (completed = magenta check icon, active = magenta filled circle with white number, pending = zinc circle with zinc number). Steps connected by a horizontal line (completed segment = magenta, pending = zinc). Step labels below circles (active = white bold, inactive = zinc). Parameters: `steps: List<String>`, `currentStep: Int` (0-indexed), `modifier: Modifier`. Include `@Preview` with steps ("Address", "Payment", "Confirm") at step index 1.
- [x] T034 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/QuantityStepper.kt` — Inline horizontal composable: minus button (zinc circle ~28dp, "−" icon), quantity count in white bold ~14sp (centered, min width ~24dp), plus button (magenta filled circle ~28dp, "+" icon). Minus disabled (30% alpha) when quantity = 1. Parameters: `quantity: Int`, `onIncrement: () -> Unit`, `onDecrement: () -> Unit`, `modifier: Modifier`. Include `@Preview`.
- [x] T035 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/StatusBadge.kt` — Small pill badge composable with color-coded background + uppercase bold white text (~10sp). Color mapping: "DELIVERED" → `StatusDelivered` green, "PROCESSING" → `StatusProcessing` amber, "SHIPPED" → `StatusShipped` cyan, "CANCELLED" → `Color(0xFFEF4444)` red, default → zinc. Parameters: `status: String`, `modifier: Modifier`. Include `@Preview` showing all 4 states using `Row`.

### Settings & Profile Components

- [x] T036 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/ToggleRow.kt` — Full-width row composable inside dark surface, ~60dp height. Leading: optional icon (zinc). Center column: title in white ~15sp + subtitle in zinc ~12sp. Trailing: `Switch` composable styled with magenta `thumbColor` and `trackColor` when checked, zinc when unchecked. Parameters: `title: String`, `subtitle: String`, `isChecked: Boolean`, `onToggle: (Boolean) -> Unit`, `leadingIcon: ImageVector? = null`, `modifier: Modifier`. Include `@Preview` with checked and unchecked states.
- [x] T037 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/AvatarCircle.kt` — Circular composable (~96dp default, configurable) with 2dp magenta ring border. Content: initials text in white bold (~28sp for large, ~14sp for small) centered on dark surface background. Parameters: `initials: String`, `size: Dp = 96.dp`, `modifier: Modifier`. Include `@Preview`.
- [x] T038 [P] Create `app/src/main/java/com/shopflow/app/presentation/components/NotificationBadge.kt` — Bell icon composable (~24dp) with an overlaid unread count badge. Badge: small magenta filled circle (top-right offset) with white bold count text (~8sp). Badge hidden when count = 0. Parameters: `unreadCount: Int`, `onClick: () -> Unit`, `modifier: Modifier`. Include `@Preview` showing count = 0 (no badge) and count = 5.

### Verification & Commit

- [x] T039 Run `./gradlew assembleDebug` from project root and verify zero build errors. Verify all `@Preview` composables render in Android Studio preview panel without crashes.
- [x] T040 Git commit all Phase 2 changes with message `feat: add Midnight Cyber-Chic design system components` and push to remote branch `001-shopflow-ecommerce-app`.

---

## Phase 3: Shopify GraphQL Network Layer

**Goal**: Apollo client, data sources, domain models, mappers, repositories.
**Prerequisites**: Phase 1-2 complete (T001–T040).

### Schema & Queries

- [x] T041 Download Shopify Storefront API schema to `app/src/main/graphql/com/shopflow/app/schema.graphqls` and configure Apollo plugin in `app/build.gradle.kts` with `service("shopify")` block pointing to schema path
- [x] T042 [P] Create `app/src/main/graphql/com/shopflow/app/queries/FetchCollections.graphql` — per contract Q-001 in `contracts/shopify-storefront-api.md`
- [x] T043 [P] Create `app/src/main/graphql/com/shopflow/app/queries/FetchFeaturedProducts.graphql` — per contract Q-002
- [x] T044 [P] Create `app/src/main/graphql/com/shopflow/app/queries/FetchProductsByCollection.graphql` — per contract Q-003
- [x] T045 [P] Create `app/src/main/graphql/com/shopflow/app/queries/FetchProductDetail.graphql` — per contract Q-004
- [x] T046 [P] Create `app/src/main/graphql/com/shopflow/app/queries/SearchProducts.graphql` — per contract Q-005
- [x] T047 [P] Create `app/src/main/graphql/com/shopflow/app/queries/FetchCustomerOrders.graphql` — per contract Q-006
- [x] T048 [P] Create `app/src/main/graphql/com/shopflow/app/queries/FetchCustomerProfile.graphql` — per contract Q-007
- [x] T049 [P] Create `app/src/main/graphql/com/shopflow/app/mutations/CustomerLogin.graphql` — per contract M-001
- [x] T050 [P] Create `app/src/main/graphql/com/shopflow/app/mutations/CustomerRegister.graphql` — per contract M-002
- [x] T051 [P] Create `app/src/main/graphql/com/shopflow/app/mutations/CustomerUpdate.graphql` — per contract M-003
- [x] T052 [P] Create `app/src/main/graphql/com/shopflow/app/mutations/CheckoutCreate.graphql` — per contract M-004
- [x] T053 [P] Create `app/src/main/graphql/com/shopflow/app/mutations/CustomerAccessTokenRenew.graphql` — per contract M-005
- [x] T054 Run `./gradlew generateApolloSources` and verify codegen completes with zero errors

### Domain Models

- [x] T055 [P] Create `app/src/main/java/com/shopflow/app/domain/model/Product.kt` — data class per `data-model.md` Product entity with all fields
- [x] T056 [P] Create `app/src/main/java/com/shopflow/app/domain/model/ProductVariant.kt` — data class with SelectedOption, Money, PriceRange, ProductImage
- [x] T057 [P] Create `app/src/main/java/com/shopflow/app/domain/model/Collection.kt` — data class per data-model
- [x] T058 [P] Create `app/src/main/java/com/shopflow/app/domain/model/Customer.kt` — data class with Address
- [x] T059 [P] Create `app/src/main/java/com/shopflow/app/domain/model/Order.kt` — data class with OrderLineItem, FulfillmentStatus enum
- [x] T060 [P] Create `app/src/main/java/com/shopflow/app/domain/model/Cart.kt` — data class with CartLineItem
- [x] T061 [P] Create `app/src/main/java/com/shopflow/app/domain/model/ApiResult.kt` — sealed class: Success, GraphQLError, NetworkError, Empty

### Data Layer

- [x] T062 Create `app/src/main/java/com/shopflow/app/data/remote/interceptor/ShopifyAuthInterceptor.kt` — OkHttp interceptor adding `X-Shopify-Storefront-Access-Token` header from BuildConfig
- [x] T063 Update `app/src/main/java/com/shopflow/app/di/NetworkModule.kt` — replace stubs with real `@Provides` for OkHttpClient (with interceptor) and ApolloClient (with OkHttp, Shopify endpoint URL from BuildConfig)
- [x] T064 Create `app/src/main/java/com/shopflow/app/data/remote/ShopifyDataSource.kt` — class wrapping ApolloClient calls for all 7 queries and 5 mutations, returning raw Apollo responses
- [x] T065 [P] Create `app/src/main/java/com/shopflow/app/data/mapper/ProductMapper.kt` — extension functions mapping Apollo-generated product types to domain `Product`/`ProductVariant`
- [x] T066 [P] Create `app/src/main/java/com/shopflow/app/data/mapper/CollectionMapper.kt` — maps Apollo collection types to domain `Collection`
- [x] T067 [P] Create `app/src/main/java/com/shopflow/app/data/mapper/CustomerMapper.kt` — maps Apollo customer types to domain `Customer`/`Address`
- [x] T068 [P] Create `app/src/main/java/com/shopflow/app/data/mapper/OrderMapper.kt` — maps Apollo order types to domain `Order`/`OrderLineItem`

### Repository Interfaces & Implementations

- [x] T069 [P] Create `app/src/main/java/com/shopflow/app/domain/repository/ProductRepository.kt` — interface: `getCollections()`, `getFeaturedProducts()`, `getProductsByCollection()`, `getProductDetail()`, `searchProducts()`
- [x] T070 [P] Create `app/src/main/java/com/shopflow/app/domain/repository/AuthRepository.kt` — interface: `login()`, `register()`, `refreshToken()`, `logout()`, `getAuthState()`
- [x] T071 [P] Create `app/src/main/java/com/shopflow/app/domain/repository/OrderRepository.kt` — interface: `getOrders()`, `getOrderDetail()`
- [x] T072 [P] Create `app/src/main/java/com/shopflow/app/domain/repository/CustomerRepository.kt` — interface: `getProfile()`, `updateProfile()`
- [x] T073 [P] Create `app/src/main/java/com/shopflow/app/domain/repository/CheckoutRepository.kt` — interface: `createCheckout()`
- [x] T074 Create `app/src/main/java/com/shopflow/app/data/repository/ProductRepositoryImpl.kt` — implements ProductRepository using ShopifyDataSource + mappers, wraps in ApiResult
- [x] T075 [P] Create `app/src/main/java/com/shopflow/app/data/repository/AuthRepositoryImpl.kt` — implements AuthRepository using ShopifyDataSource + token persistence
- [x] T076 [P] Create `app/src/main/java/com/shopflow/app/data/repository/OrderRepositoryImpl.kt` — implements OrderRepository
- [x] T077 [P] Create `app/src/main/java/com/shopflow/app/data/repository/CustomerRepositoryImpl.kt` — implements CustomerRepository
- [x] T078 [P] Create `app/src/main/java/com/shopflow/app/data/repository/CheckoutRepositoryImpl.kt` — implements CheckoutRepository
- [x] T079 Update `app/src/main/java/com/shopflow/app/di/RepositoryModule.kt` — add `@Binds` entries for all 5 repository interfaces to their implementations
- [x] T080 Run `./gradlew assembleDebug` to verify full network layer compiles
- [x] T081 Git commit with message `feat: add Shopify GraphQL network layer` and push

---

## Phase 4: Local Storage Layer

**Goal**: Room database, DAOs, DataStore, offline-capable repositories.
**Prerequisites**: Phase 3 complete (T041–T081).

- [x] T082 [P] Create `app/src/main/java/com/shopflow/app/data/local/entity/WishlistItemEntity.kt` — Room `@Entity` per data-model WishlistItem with `@PrimaryKey(autoGenerate = true)` id
- [x] T083 [P] Create `app/src/main/java/com/shopflow/app/data/local/entity/NotificationEntity.kt` — Room `@Entity` per data-model Notification with NotificationType enum
- [x] T084 [P] Create `app/src/main/java/com/shopflow/app/data/local/dao/WishlistDao.kt` — `@Dao` interface: `getAll(): Flow<List>`, `insert()`, `deleteByProductId()`, `getCount(): Flow<Int>`, `exists(productId): Flow<Boolean>`
- [x] T085 [P] Create `app/src/main/java/com/shopflow/app/data/local/dao/NotificationDao.kt` — `@Dao` interface: `getAll(): Flow<List>`, `insert()`, `markAsRead()`, `getUnreadCount(): Flow<Int>`, `deleteAll()`
- [x] T086 Create `app/src/main/java/com/shopflow/app/data/local/ShopFlowDatabase.kt` — `@Database` with WishlistItemEntity and NotificationEntity, version 1, exportSchema false
- [x] T087 Update `app/src/main/java/com/shopflow/app/di/DatabaseModule.kt` — replace stubs with real `@Provides` for ShopFlowDatabase (Room.databaseBuilder), WishlistDao, NotificationDao
- [x] T088 Create `app/src/main/java/com/shopflow/app/data/local/datastore/PreferencesDataStore.kt` — wrapper class for DataStore<Preferences>: read/write onboarding_completed, theme_mode, language_code, biometric_enabled, push_notifications, email_marketing per data-model UserPreferences
- [x] T089 Create `app/src/main/java/com/shopflow/app/data/local/datastore/TokenDataStore.kt` — wrapper for encrypted DataStore: store/retrieve/clear customer access token and expiry timestamp
- [x] T090 Update `app/src/main/java/com/shopflow/app/di/DataStoreModule.kt` — replace stubs with `@Provides` for DataStore<Preferences>, PreferencesDataStore, TokenDataStore
- [x] T091 [P] Create `app/src/main/java/com/shopflow/app/domain/repository/WishlistRepository.kt` — interface: `getWishlist()`, `addItem()`, `removeItem()`, `isWishlisted()`, `getCount()`
- [x] T092 [P] Create `app/src/main/java/com/shopflow/app/domain/repository/NotificationRepository.kt` — interface: `getNotifications()`, `addNotification()`, `markAsRead()`, `getUnreadCount()`
- [x] T093 [P] Create `app/src/main/java/com/shopflow/app/domain/repository/PreferencesRepository.kt` — interface: `getPreferences()`, `setTheme()`, `setLanguage()`, `setOnboardingCompleted()`, `setBiometric()`, etc.
- [x] T094 Create `app/src/main/java/com/shopflow/app/data/repository/WishlistRepositoryImpl.kt` — implements WishlistRepository using WishlistDao with entity↔domain mapping
- [x] T095 [P] Create `app/src/main/java/com/shopflow/app/data/repository/NotificationRepositoryImpl.kt` — implements NotificationRepository using NotificationDao
- [x] T096 [P] Create `app/src/main/java/com/shopflow/app/data/repository/PreferencesRepositoryImpl.kt` — implements PreferencesRepository using PreferencesDataStore
- [x] T097 Update `app/src/main/java/com/shopflow/app/di/RepositoryModule.kt` — add `@Binds` for WishlistRepository, NotificationRepository, PreferencesRepository
- [x] T098 Run `./gradlew assembleDebug` and verify zero errors
- [x] T099 Git commit `feat: add local storage layer (Room + DataStore)` and push

---

## Phase 5: Authentication Flow (US3)

**Goal**: Login, Register screens with social login. Auth-gated navigation.
**Prerequisites**: Phases 3-4 complete.

### Use Cases

- [x] T100 [P] [US3] Create `app/src/main/java/com/shopflow/app/domain/usecase/auth/LoginUseCase.kt` — takes email+password, calls AuthRepository.login(), returns ApiResult<Customer>
- [x] T101 [P] [US3] Create `app/src/main/java/com/shopflow/app/domain/usecase/auth/RegisterUseCase.kt` — takes name+email+password, calls AuthRepository.register()
- [x] T102 [P] [US3] Create `app/src/main/java/com/shopflow/app/domain/usecase/auth/LogoutUseCase.kt` — clears token store, resets auth state
- [x] T103 [P] [US3] Create `app/src/main/java/com/shopflow/app/domain/usecase/auth/RefreshTokenUseCase.kt` — renews token if near expiry
- [x] T104 [P] [US3] Create `app/src/main/java/com/shopflow/app/domain/usecase/auth/GetAuthStateUseCase.kt` — returns Flow<Boolean> for isAuthenticated

### ViewModel & Screens

- [x] T105 [US3] Create `app/src/main/java/com/shopflow/app/presentation/screens/auth/AuthViewModel.kt` — `@HiltViewModel` with LoginUiState/RegisterUiState sealed classes, email/password validation, login()/register() functions emitting StateFlow, error handling via SharedFlow
- [x] T106 [US3] Create `app/src/main/java/com/shopflow/app/presentation/screens/auth/LoginScreen.kt` — per spec Screen 06: magenta glow background, ShopFlow icon, "Welcome back" title, EMAIL/PASSWORD fields (dark surface inputs, icons), "Forgot Password?" link, "Sign In →" GradientButton, "OR" divider, Google+Apple social buttons, footer "Sign up" link
- [x] T107 [US3] Create `app/src/main/java/com/shopflow/app/presentation/screens/auth/RegisterScreen.kt` — per spec Screen 07: "Create account" title, FULL NAME/EMAIL/PASSWORD fields, password strength badge (green pill), terms checkbox (magenta), "Create Account →" GradientButton, social buttons (vertical), footer "Sign in" link
- [x] T108 [US3] Update `app/src/main/java/com/shopflow/app/presentation/navigation/ShopFlowNavGraph.kt` — replace Login/Register placeholders with real screens, wire AuthViewModel, add auth-gate logic (redirect to Login if unauthenticated on checkout)
- [x] T109 Run `./gradlew assembleDebug` and verify zero errors
- [x] T110 Git commit `feat: add authentication flow (login, register, social)` and push

---

## Phase 6: Shopping Flow — Browse → Cart → Checkout → Confirmation

**Goal**: Full purchase funnel end-to-end.
**Prerequisites**: Phases 2-5 complete.

### Onboarding (US8)

- [ ] T111 [P] [US8] Create `app/src/main/java/com/shopflow/app/presentation/screens/onboarding/OnboardingViewModel.kt` — reads/writes onboarding_completed via PreferencesRepository
- [ ] T112 [US8] Create `app/src/main/java/com/shopflow/app/presentation/screens/onboarding/OnboardingScreen.kt` — 3-slide HorizontalPager (Browse, Shop, Track), dot indicator, "Get Started" GradientButton, "Skip" text link. Persists completion.
- [ ] T113 [US8] Update NavGraph — replace Onboarding placeholder, wire splash→onboarding→home flow with onboarding skip logic

### Home (US1)

- [ ] T114 [P] [US1] Create `app/src/main/java/com/shopflow/app/domain/usecase/product/GetCollectionsUseCase.kt` — calls ProductRepository.getCollections()
- [ ] T115 [P] [US1] Create `app/src/main/java/com/shopflow/app/domain/usecase/product/GetFeaturedProductsUseCase.kt` — calls ProductRepository.getFeaturedProducts()
- [ ] T116 [P] [US1] Create `app/src/main/java/com/shopflow/app/domain/usecase/product/SearchProductsUseCase.kt` — calls ProductRepository.searchProducts()
- [ ] T117 [US1] Create `app/src/main/java/com/shopflow/app/presentation/screens/home/HomeViewModel.kt` — `@HiltViewModel` with HomeUiState (collections, featured products, banners), fetch on init, search handler
- [ ] T118 [US1] Create `app/src/main/java/com/shopflow/app/presentation/screens/home/HomeScreen.kt` — per spec Screen 02: greeting bar + avatar + notification badge, SearchBar, hero banner HorizontalPager, CategoryIcon LazyRow, "Featured" header + 2-column LazyVerticalGrid of ProductCards
- [ ] T119 [US1] Update NavGraph — replace Home placeholder with real HomeScreen, wire product tap → ProductDetail, category tap → filtered list

### Product Details (US1)

- [ ] T120 [P] [US1] Create `app/src/main/java/com/shopflow/app/domain/usecase/product/GetProductDetailUseCase.kt` — calls ProductRepository.getProductDetail(id)
- [ ] T121 [P] [US1] Create `app/src/main/java/com/shopflow/app/domain/usecase/cart/AddToCartUseCase.kt` — adds variant+quantity to local cart state
- [ ] T122 [US1] Create `app/src/main/java/com/shopflow/app/presentation/screens/product/ProductDetailViewModel.kt` — fetches product, manages selected variant (size/color), exposes add-to-cart action
- [ ] T123 [US1] Create `app/src/main/java/com/shopflow/app/presentation/screens/product/ProductDetailScreen.kt` — per spec Screen 03: hero image with glow, back/share/heart icons, brand breadcrumb, title + price pill, rating stars, SIZE ChipSelector, COLOR circular swatches, description, "Add to Cart" GradientButton
- [ ] T124 [US1] Update NavGraph — replace ProductDetail placeholder, pass productId argument

### Cart & Checkout (US2)

- [ ] T125 [P] [US2] Create `app/src/main/java/com/shopflow/app/domain/usecase/cart/UpdateCartUseCase.kt` — updates quantity for a cart line item
- [ ] T126 [P] [US2] Create `app/src/main/java/com/shopflow/app/domain/usecase/cart/RemoveFromCartUseCase.kt` — removes line item from cart
- [ ] T127 [P] [US2] Create `app/src/main/java/com/shopflow/app/domain/usecase/cart/GetCartUseCase.kt` — returns current cart state as Flow
- [ ] T128 [P] [US2] Create `app/src/main/java/com/shopflow/app/domain/usecase/checkout/CreateCheckoutUseCase.kt` — calls CheckoutRepository.createCheckout() with cart line items, returns webUrl
- [ ] T129 [US2] Create `app/src/main/java/com/shopflow/app/presentation/screens/cart/CartViewModel.kt` — manages cart state, quantity updates, subtotal calc, remove items
- [ ] T130 [US2] Create `app/src/main/java/com/shopflow/app/presentation/screens/cart/CartScreen.kt` — per spec Screen 04: StepIndicator, cart item cards with QuantityStepper, delivery address card, payment card visual (gradient), order summary (subtotal/tax/total), "Proceed to Checkout" GradientButton
- [ ] T131 [US2] Create `app/src/main/java/com/shopflow/app/presentation/screens/checkout/CheckoutViewModel.kt` — creates Shopify checkout, launches Chrome Custom Tab with webUrl
- [ ] T132 [US2] Create `app/src/main/java/com/shopflow/app/presentation/screens/confirmation/OrderConfirmationScreen.kt` — per spec Screen 05: particle confetti animation, pulsing checkmark halo, "Order Confirmed" title, tracking card with StatusBadge, "Track Order" GradientButton + "Continue Shopping" OutlinedButton
- [ ] T133 [US2] Update NavGraph — replace Cart/Checkout/Confirmation placeholders, wire deep-link receiver for checkout completion callback
- [ ] T134 Run `./gradlew assembleDebug` and verify zero errors
- [ ] T135 Git commit `feat: add complete shopping flow (browse, cart, checkout, confirmation)` and push

---

## Phase 7: Account & Retention Features

**Goal**: Profile, orders, wishlist, notifications, settings.
**Prerequisites**: Phase 6 complete.

### Wishlist (US4)

- [ ] T136 [P] [US4] Create `app/src/main/java/com/shopflow/app/domain/usecase/wishlist/GetWishlistUseCase.kt`
- [ ] T137 [P] [US4] Create `app/src/main/java/com/shopflow/app/domain/usecase/wishlist/AddToWishlistUseCase.kt`
- [ ] T138 [P] [US4] Create `app/src/main/java/com/shopflow/app/domain/usecase/wishlist/RemoveFromWishlistUseCase.kt`
- [ ] T139 [US4] Create `app/src/main/java/com/shopflow/app/presentation/screens/wishlist/WishlistViewModel.kt` — CRUD operations, filtering/sorting by price/brand, Room-backed
- [ ] T140 [US4] Create `app/src/main/java/com/shopflow/app/presentation/screens/wishlist/WishlistScreen.kt` — per spec Screen 08: item cards, filter ModalBottomSheet (price RangeSlider, brand ChipSelector, sort ChipSelector, "Apply Filters" GradientButton)
- [ ] T141 [US4] Update NavGraph — replace Wishlist placeholder

### Profile (US6)

- [ ] T142 [P] [US6] Create `app/src/main/java/com/shopflow/app/domain/usecase/profile/GetCustomerProfileUseCase.kt`
- [ ] T143 [P] [US6] Create `app/src/main/java/com/shopflow/app/domain/usecase/profile/UpdateCustomerProfileUseCase.kt`
- [ ] T144 [US6] Create `app/src/main/java/com/shopflow/app/presentation/screens/profile/ProfileViewModel.kt` — fetches customer, exposes stats (order count, wishlist count, points)
- [ ] T145 [US6] Create `app/src/main/java/com/shopflow/app/presentation/screens/profile/ProfileScreen.kt` — per spec Screen 09: AvatarCircle, name/email, "PREMIUM MEMBER" badge, 3-stat row, NavigationRows (My Orders, Saved Addresses, Payment Methods, Wishlist, Rewards)
- [ ] T146 [US6] Update NavGraph — replace Profile placeholder, wire menu row taps to respective screens

### Order History (US5)

- [ ] T147 [P] [US5] Create `app/src/main/java/com/shopflow/app/domain/usecase/order/GetOrderHistoryUseCase.kt`
- [ ] T148 [US5] Create `app/src/main/java/com/shopflow/app/presentation/screens/orders/OrderHistoryViewModel.kt` — fetches orders, filters by fulfillment status
- [ ] T149 [US5] Create `app/src/main/java/com/shopflow/app/presentation/screens/orders/OrderHistoryScreen.kt` — per spec Screen 10: filter tabs (All/Active/Delivered/Cancelled) via ChipSelector, order cards with StatusBadge, "Track Order" OutlinedButton
- [ ] T150 [US5] Update NavGraph — replace OrderHistory placeholder

### Settings (US6)

- [ ] T151 [US6] Create `app/src/main/java/com/shopflow/app/presentation/screens/settings/SettingsViewModel.kt` — reads/writes PreferencesRepository for all toggles and language
- [ ] T152 [US6] Create `app/src/main/java/com/shopflow/app/presentation/screens/settings/SettingsScreen.kt` — per spec Screen 11: PREFERENCES section (ToggleRows for push/dark theme/biometric/email), REGION section (Language NavigationRow + language ChipSelector), ACCOUNT & SECURITY section (NavigationRows: Change Password, Privacy, Help Center)
- [ ] T153 [US6] Update NavGraph — replace Settings placeholder, wire gear icon from Profile

### Notifications (US7)

- [ ] T154 [P] [US7] Create `app/src/main/java/com/shopflow/app/domain/usecase/notification/GetNotificationsUseCase.kt`
- [ ] T155 [P] [US7] Create `app/src/main/java/com/shopflow/app/domain/usecase/notification/MarkNotificationReadUseCase.kt`
- [ ] T156 [US7] Create `app/src/main/java/com/shopflow/app/presentation/screens/notifications/NotificationViewModel.kt` — Room-backed notification list, marks as read, exposes unread count
- [ ] T157 [US7] Create `app/src/main/java/com/shopflow/app/presentation/screens/notifications/NotificationScreen.kt` — notification list with GlassmorphismCards, read/unread styling, tap to navigate via deepLink
- [ ] T158 [US7] Create `app/src/main/java/com/shopflow/app/data/worker/OrderStatusWorker.kt` — WorkManager PeriodicWorkRequest polling Shopify for order status changes, creates local notifications on change
- [ ] T159 [US7] Update NavGraph — replace Notifications placeholder, wire bell icon from Home
- [ ] T160 Run `./gradlew assembleDebug` and verify zero errors
- [ ] T161 Git commit `feat: add account features (wishlist, profile, orders, settings, notifications)` and push

---

## Phase 8: Polish, Testing & Quality

**Goal**: Production-readiness.
**Prerequisites**: Phase 7 complete.

- [ ] T162 [P] Add shimmer loading state composable `app/src/main/java/com/shopflow/app/presentation/components/ShimmerEffect.kt` and apply to Home, ProductDetail, OrderHistory screens during data fetch
- [ ] T163 [P] Add scale/fade micro-animations to ProductCard and NavigationRow press interactions using `Modifier.clickable` + `animateScale`
- [ ] T164 [P] Add confetti particle animation to OrderConfirmationScreen using Canvas and `LaunchedEffect` loop
- [ ] T165 [P] Add offline error state composable `app/src/main/java/com/shopflow/app/presentation/components/ErrorState.kt` — "No connection" illustration, retry GradientButton. Apply to all data-fetching screens
- [ ] T166 [P] Add empty state composable `app/src/main/java/com/shopflow/app/presentation/components/EmptyState.kt` — illustration + message + CTA. Apply to OrderHistory, Wishlist, Notifications
- [ ] T167 Extract all hardcoded user-facing strings to `app/src/main/res/values/strings.xml`
- [ ] T168 [P] Create `app/src/main/res/values-fr/strings.xml` — French translations
- [ ] T169 [P] Create `app/src/main/res/values-de/strings.xml` — German translations
- [ ] T170 Add `contentDescription` to all icons and images across all screens for accessibility (WCAG AA)
- [ ] T171 [P] Write unit tests for all mappers in `app/src/test/java/com/shopflow/app/data/mapper/` — ProductMapper, CollectionMapper, CustomerMapper, OrderMapper
- [ ] T172 [P] Write unit tests for cart use cases in `app/src/test/java/com/shopflow/app/domain/usecase/cart/` — AddToCart, UpdateCart, RemoveFromCart, GetCart
- [ ] T173 [P] Write unit tests for auth use cases in `app/src/test/java/com/shopflow/app/domain/usecase/auth/` — Login, Register, Logout
- [ ] T174 [P] Write ViewModel tests for HomeViewModel, CartViewModel, AuthViewModel in `app/src/test/java/com/shopflow/app/presentation/` using Turbine + MockK
- [ ] T175 Update `app/proguard-rules.pro` — finalize R8 keep rules for Apollo, Hilt, Room, Coil, ensure release build works
- [ ] T176 Run `./gradlew lintDebug` and fix all warnings
- [ ] T177 Run `./gradlew assembleRelease` and verify zero errors
- [ ] T178 Git commit `feat: add polish, tests, accessibility, localization` and push

---

## Dependencies & Execution Order

```text
Phase 3 (T041-T081): GraphQL Network → depends on Phase 1-2
Phase 4 (T082-T099): Local Storage   → depends on Phase 3
Phase 5 (T100-T110): Auth Flow       → depends on Phases 3-4
Phase 6 (T111-T135): Shopping Flow   → depends on Phases 2-5
Phase 7 (T136-T161): Account Features→ depends on Phase 6
Phase 8 (T162-T178): Polish & Test   → depends on Phase 7
```

## Implementation Strategy

1. Execute phases sequentially (3→4→5→6→7→8)
2. Within each phase, exploit [P] parallel tasks on independent files
3. **Validate** after each phase with `./gradlew assembleDebug`
4. **Commit** after each phase completes
5. Total: 138 tasks (T041–T178) across 6 phases
