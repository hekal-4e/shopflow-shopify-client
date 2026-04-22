
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
