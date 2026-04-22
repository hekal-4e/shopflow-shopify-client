# Feature Specification: ShopFlow E-Commerce App

**Feature Branch**: `001-shopflow-ecommerce-app`
**Created**: 2026-04-21
**Status**: Draft
**Input**: User description: "Core e-commerce mobile app powered by Shopify Storefront API with full shopping flow, account management, wishlists, notifications, and n8n webhook automation on checkout success."

## User Scenarios & Testing *(mandatory)*

### User Story 1 — Browse & Discover Products (Priority: P1)

A shopper opens the app for the first time, is guided through a brief
onboarding carousel, then lands on the Home screen. The Home screen displays
promotional offers, product categories, and a featured-products section. The
shopper taps a category to see a filtered product list, then taps a product to
view its details — images, description, price, and available variants (size,
color, etc.).

**Why this priority**: Browsing is the entry point to all revenue. Without
product discovery there is no purchase funnel.

**Independent Test**: Launch the app → complete onboarding → verify Home screen
displays offers, categories, and featured products → tap a product → verify
product detail screen shows images, description, price, and variant selector.

**Acceptance Scenarios**:

1. **Given** the app is launched for the first time, **When** onboarding
   completes, **Then** the Home screen loads with at least one offer banner,
   one category tile, and one featured product card — all sourced from Shopify.
2. **Given** the Home screen is visible, **When** the shopper taps a category,
   **Then** a filtered product list from Shopify is displayed.
3. **Given** a product list is visible, **When** the shopper taps a product,
   **Then** the Product Details screen shows an image carousel, title,
   description, price, variant selector, and an "Add to Cart" button.
4. **Given** the Product Details screen is visible, **When** no variants exist,
   **Then** the variant selector is hidden and the default variant is
   pre-selected.

---

### User Story 2 — Cart, Checkout & Order Confirmation (Priority: P1)

A shopper selects a product variant, taps "Add to Cart," reviews the cart, then
proceeds to checkout. Checkout is handled via Shopify's web checkout flow. Upon
successful payment, the app displays an Order Confirmation screen. The checkout
success event enables Shopify to trigger external n8n webhooks for email
confirmation and inventory adjustment.

**Why this priority**: The purchase flow is the primary revenue driver and the
critical path for the n8n automation trigger.

**Independent Test**: Add a product to cart → view cart → proceed to checkout →
complete payment → verify Order Confirmation screen → verify checkout success
state is properly communicated back so Shopify can fire n8n webhooks.

**Acceptance Scenarios**:

1. **Given** a product variant is selected, **When** the shopper taps
   "Add to Cart," **Then** the item is added with correct variant, quantity,
   and price, and a cart badge updates.
2. **Given** items are in the cart, **When** the shopper opens the cart,
   **Then** all items are listed with line-item totals, quantities are
   adjustable, and a subtotal is shown.
3. **Given** the cart is reviewed, **When** the shopper taps "Checkout,"
   **Then** Shopify's web checkout is launched with the current cart contents.
4. **Given** checkout completes successfully, **When** Shopify returns a
   completed order, **Then** the app displays an Order Confirmation screen with
   order number, items purchased, and total paid.
5. **Given** checkout completes successfully, **When** the order is finalized
   on Shopify, **Then** Shopify is in a state to fire configured n8n webhooks
   (email confirmation, inventory update) without additional app-side logic.

---

### User Story 3 — User Authentication (Priority: P1)

A visitor registers or logs in using Shopify customer authentication. Once
authenticated, the shopper gains access to their profile, order history, and
wishlist. Unauthenticated users can still browse and add to cart but must
authenticate before checkout.

**Why this priority**: Authentication gates checkout and unlocks personalized
features (profile, history, wishlist).

**Independent Test**: Tap Login → enter credentials → verify successful auth →
verify access to profile tab. Attempt checkout while unauthenticated → verify
redirect to login.

**Acceptance Scenarios**:

1. **Given** the user is unauthenticated, **When** they tap "Login,"
   **Then** a login screen with email/password fields is displayed.
2. **Given** the user has no account, **When** they tap "Register," **Then** a
   registration form (name, email, password) is displayed and upon submission
   a Shopify customer account is created.
3. **Given** valid credentials are entered, **When** the user submits login,
   **Then** an access token is obtained from Shopify and the user is
   redirected to the Home screen with authenticated state.
4. **Given** the user is unauthenticated, **When** they attempt to proceed to
   checkout, **Then** they are redirected to the login screen with a message
   indicating authentication is required.

---

### User Story 4 — Wishlist (Priority: P2)

An authenticated shopper can save products to a wishlist for future purchase.
The wishlist is accessible from the user's profile area and persists across
sessions.

**Why this priority**: Wishlists improve retention and conversion by allowing
users to defer purchases without losing interest.

**Independent Test**: Log in → navigate to a product → tap "Add to Wishlist" →
navigate to wishlist screen → verify the product appears → remove it → verify
it disappears.

**Acceptance Scenarios**:

1. **Given** the user is authenticated and viewing a product, **When** they tap
   the wishlist icon, **Then** the product is saved to their wishlist.
2. **Given** the user is authenticated, **When** they open the Wishlist screen,
   **Then** all saved products are listed with image, name, and price.
3. **Given** a product is in the wishlist, **When** the user taps "Remove,"
   **Then** the product is removed from the wishlist.
4. **Given** a product is in the wishlist, **When** the user taps the product
   card, **Then** they are navigated to the Product Details screen.

---

### User Story 5 — Order History (Priority: P2)

An authenticated shopper can view a list of their past orders, including order
status, date, items, and totals. Tapping an order reveals full details.

**Why this priority**: Order history builds trust and reduces support inquiries
about order status.

**Independent Test**: Log in → navigate to Order History → verify past orders
are listed → tap an order → verify full order details are displayed.

**Acceptance Scenarios**:

1. **Given** the user is authenticated, **When** they open Order History,
   **Then** a chronological list of past orders (order number, date, status,
   total) is displayed from Shopify.
2. **Given** the order list is visible, **When** the user taps an order,
   **Then** order details (items, quantities, prices, shipping address,
   fulfillment status) are shown.
3. **Given** the user has no past orders, **When** they open Order History,
   **Then** an empty state with a prompt to start shopping is displayed.

---

### User Story 6 — User Profile & Settings (Priority: P2)

An authenticated shopper can view and edit their profile (name, email, phone,
address) and adjust app settings including theme toggle (dark/light — defaulting
to the Midnight Cyber-Chic dark theme) and language selection.

**Why this priority**: Profile management is essential for checkout
pre-population and user personalization.

**Independent Test**: Log in → open Profile → verify personal info is shown →
edit name → save → verify update persists. Toggle theme → verify UI changes.
Switch language → verify strings update.

**Acceptance Scenarios**:

1. **Given** the user is authenticated, **When** they open the Profile screen,
   **Then** their name, email, phone, and default address are displayed
   (sourced from Shopify).
2. **Given** the Profile screen is visible, **When** the user edits a field
   and saves, **Then** the update is persisted to Shopify and reflected in the
   UI.
3. **Given** the Settings screen is visible, **When** the user toggles the
   theme, **Then** the entire app UI updates accordingly.
4. **Given** the Settings screen is visible, **When** the user changes the
   language, **Then** all app strings reload in the selected language.

---

### User Story 7 — Notifications (Priority: P3)

The app displays in-app notifications for order status updates, promotional
offers, and wishlist price drops. A notification bell icon on the Home screen
shows the unread count.

**Why this priority**: Notifications drive re-engagement but are not critical
for core purchase flow.

**Independent Test**: Trigger a notification (e.g., order status change) →
verify the bell icon badge updates → tap to view notification list → verify
notification content is displayed → mark as read → verify badge decrements.

**Acceptance Scenarios**:

1. **Given** a new notification is received, **When** the user views the Home
   screen, **Then** the notification bell shows an unread count badge.
2. **Given** the notification list is open, **When** the user views a
   notification, **Then** the notification content (title, body, timestamp) is
   displayed.
3. **Given** unread notifications exist, **When** the user taps a notification,
   **Then** it is marked as read and the badge count decrements.

---

### User Story 8 — Splash & Onboarding (Priority: P3)

On first launch, the app displays a branded splash screen with the ShopFlow
logo, then transitions to a 3-screen onboarding carousel highlighting key
features (Browse, Shop, Track). The user can skip or complete onboarding.
Subsequent launches skip onboarding and go directly to Home.

**Why this priority**: Onboarding improves first-run experience but does not
block core functionality.

**Independent Test**: Install fresh → verify splash screen displays → verify
onboarding carousel with 3 slides → complete or skip → verify Home loads →
restart app → verify onboarding is skipped.

**Acceptance Scenarios**:

1. **Given** the app is launched for the first time, **When** the splash screen
   appears, **Then** the ShopFlow logo and brand gradient are displayed for
   2–3 seconds.
2. **Given** the splash screen completes, **When** onboarding has not been
   completed before, **Then** a 3-screen carousel with skip and next controls
   is displayed.
3. **Given** onboarding is shown, **When** the user taps "Skip" or completes
   all slides, **Then** they are navigated to the Home screen and onboarding
   completion is persisted locally.
4. **Given** onboarding was previously completed, **When** the app is launched,
   **Then** the splash screen transitions directly to the Home screen.

---

### Edge Cases

- What happens when the network is unavailable during product browsing?
  The app displays a cached version of previously loaded products (if
  available) or shows an offline state with a retry button.
- What happens when the Shopify API returns an error during checkout?
  The app displays a user-friendly error message and allows the user to retry
  without losing cart contents.
- What happens when a product in the cart becomes unavailable?
  The app marks the unavailable item in the cart with a warning and prevents
  checkout until the item is removed.
- What happens when the authentication token expires mid-session?
  The app silently attempts token refresh; if refresh fails, the user is
  redirected to the login screen with their navigation state preserved.
- What happens when the wishlist exceeds a practical limit?
  The app enforces a reasonable maximum (e.g., 100 items) and notifies the
  user when the limit is reached.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The app MUST display a branded splash screen on every launch.
- **FR-002**: The app MUST show a 3-screen onboarding carousel on first launch,
  with the option to skip.
- **FR-003**: The app MUST persist onboarding completion locally so it is not
  re-shown on subsequent launches.
- **FR-004**: The app MUST authenticate users via Shopify Customer Account API
  (email/password login and registration).
- **FR-005**: The app MUST allow unauthenticated users to browse products and
  add items to a local cart.
- **FR-006**: The app MUST require authentication before proceeding to checkout.
- **FR-007**: The Home screen MUST display promotional offer banners, category
  tiles, and featured product cards — all sourced from Shopify.
- **FR-008**: The app MUST provide category-based product filtering with results
  from Shopify Storefront API.
- **FR-009**: The Product Details screen MUST display an image carousel, title,
  description, price, available variants, and an "Add to Cart" action.
- **FR-010**: The cart MUST support adding, removing, and updating quantities of
  product variants.
- **FR-011**: The cart MUST display line-item details with subtotal calculation.
- **FR-012**: The app MUST launch Shopify web checkout with the current cart for
  payment processing.
- **FR-013**: Upon successful checkout, the app MUST display an Order
  Confirmation screen with order number, items, and total.
- **FR-014**: The checkout success state MUST be communicated to Shopify such
  that configured webhooks (targeting n8n for email and inventory automation)
  can fire without additional app-side intervention.
- **FR-015**: Authenticated users MUST be able to view and edit their profile
  (name, email, phone, default address) via Shopify Customer API.
- **FR-016**: Authenticated users MUST be able to view a chronological list of
  past orders with details from Shopify.
- **FR-017**: Authenticated users MUST be able to add/remove products to/from a
  wishlist that persists across sessions.
- **FR-018**: The app MUST display in-app notifications with an unread count
  badge.
- **FR-019**: The Settings screen MUST offer a theme toggle (dark mode default)
  and language selection.
- **FR-020**: The app MUST handle offline states gracefully with cached data
  and retry mechanisms.
- **FR-021**: The login screen MUST offer social sign-in options (Google and
  Apple) in addition to email/password authentication.
- **FR-022**: The registration form MUST display a real-time password strength
  indicator (e.g., Weak / Medium / Strong) as the user types.
- **FR-023**: The Home screen MUST include a search bar with placeholder text
  and a trailing voice-input microphone icon.
- **FR-024**: The Wishlist screen MUST provide a filter/sort bottom sheet with
  price range slider, brand chip selection, and sort-by options (Popular,
  Newest, Price ascending, Price descending).
- **FR-025**: The Settings screen MUST offer a biometric login toggle
  (fingerprint / Face ID) for quick re-authentication.
- **FR-026**: The Profile screen MUST display a stats summary row showing
  total orders, wishlist item count, and reward points.
- **FR-027**: The Profile screen MUST include navigation rows for My Orders,
  Saved Addresses, Payment Methods, Wishlist, and Rewards.
- **FR-028**: The checkout flow MUST present a multi-step stepper indicator
  (Address → Payment → Confirm) showing completed, active, and pending steps.

### Key Entities

- **Product**: Represents a Shopify product with title, description, images,
  price range, and a collection of variants.
- **Product Variant**: A specific buyable SKU of a product (size, color)
  with its own price, availability, and inventory status.
- **Cart**: A local collection of line items (variant + quantity) with
  calculated subtotal, ready for Shopify checkout creation.
- **Cart Line Item**: A single entry in the cart linking a product variant to
  a desired quantity.
- **Customer**: A Shopify customer account with profile data (name, email,
  phone, addresses) and authentication tokens.
- **Order**: A completed purchase record from Shopify containing order number,
  line items, totals, fulfillment status, and timestamps.
- **Wishlist Item**: A reference to a product saved by an authenticated user
  for future purchase consideration.
- **Notification**: An in-app message with title, body, timestamp, read/unread
  status, and optional deep-link target.
- **Category / Collection**: A Shopify collection used to group and filter
  products on the Home and browse screens.
- **Offer / Promotion**: A promotional banner displayed on the Home screen,
  potentially linked to a collection or discount.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users complete the splash → onboarding → Home flow in under
  5 seconds on a mid-range device.
- **SC-002**: Product catalog loads and renders on the Home screen within
  2 seconds of reaching the Home screen on a stable connection.
- **SC-003**: Users can add a product to cart and proceed to checkout in under
  4 taps from the Home screen.
- **SC-004**: 95% of checkout attempts initiated from the app result in
  the Shopify checkout page loading successfully.
- **SC-005**: Order Confirmation screen displays within 3 seconds of checkout
  completion.
- **SC-006**: The app functions in offline/degraded-network mode for browsing
  previously loaded products without crashing.
- **SC-007**: 90% of first-time users complete or skip onboarding without
  abandoning the app.
- **SC-008**: Theme toggle applies UI changes across all screens within
  500 milliseconds.
- **SC-009**: Language change applies to all visible strings without requiring
  app restart.
- **SC-010**: Wishlist operations (add/remove) reflect in the UI within
  1 second.

## Assumptions

- Users have a stable internet connection for initial product data loading;
  offline mode is limited to previously cached content.
- Shopify Storefront API provides all required data for products, collections,
  customers, and orders — no secondary backend is needed.
- Checkout is handled via Shopify's web checkout (Custom Storefronts approach)
  rather than a fully native in-app checkout, as the Storefront API does not
  expose payment processing directly.
- n8n webhook automation is configured on the Shopify side (order-created /
  order-paid webhooks); the app's responsibility ends at completing the
  checkout successfully so Shopify can fire those hooks.
- Wishlist data is persisted locally on-device (Room database) rather than via
  a custom Shopify metafield or external service, as the Storefront API does
  not natively support wishlists.
- Notification content is generated from local events (order status polling)
  and promotional data from Shopify, not from push notifications in this
  version (no Firebase Cloud Messaging).
- The app supports a single currency and storefront locale at launch;
  multi-currency support is deferred.
- The "Midnight Cyber-Chic" dark theme is the default; the alternate light
  theme is secondary and follows Material 3 light token conventions.

## UI Design Reference

> **Source of truth**: All screen layouts below are derived from the Lovable
> design screenshots stored in `docs/`. The Implementer Agent MUST replicate
> these layouts faithfully using Jetpack Compose. Each screen section references
> its source file for visual verification.

---

### Screen 01 — Splash / Onboarding (`docs/splash.png`)

**Layout (top → bottom, vertically centered content)**:

1. **Background**: True black `#09090B` with a subtle magenta-to-violet
   radial glow emanating from behind the logo area (upper-center) and a
   secondary glow at the bottom behind the CTA button.
2. **Logo block** (centered, ~40% from top):
   - Rounded-square icon (~64dp) with magenta-to-violet gradient fill
     containing a white sparkle/star glyph.
   - Below: "Shop" in white bold + "Flow" in magenta/gradient bold, large
     display typography (~32sp).
3. **Tagline** (centered, below logo):
   - Line 1: "Where style meets speed." — magenta/gradient text, ~14sp.
   - Line 2: "Premium shopping, reimagined." — muted zinc `#A1A1AA`, ~14sp.
4. **Page indicator** (centered): Horizontal dot row — active dot is a
   short magenta pill, inactive dots are small zinc circles.
5. **CTA button** (bottom, full-width with horizontal padding ~24dp):
   - "Get Started →" — pill-shaped button, magenta-to-violet horizontal
     gradient fill, white bold text ~16sp, height ~56dp, corner radius full
     (28dp).
6. **Secondary link** (centered, below CTA):
   - "Already have an account? **Sign in**" — zinc text with "Sign in" in
     white bold, ~13sp.

**Key interactions**: Swipe horizontally through onboarding slides (dot
indicator updates). "Get Started" navigates to Home. "Sign in" navigates to
Login screen.

---

### Screen 02 — Home (`docs/home.png`)

**Layout (scrollable column)**:

1. **Top bar** (no AppBar chrome — custom layout):
   - Left: Greeting "Hello, Alex" in magenta ~12sp + "Find your style ✨"
     in white bold ~22sp, stacked vertically.
   - Right: Notification bell icon (with magenta ring border if unread) +
     circular avatar with initials "AL" and a magenta ring border (~40dp).
2. **Search bar** (full-width, horizontal padding ~16dp):
   - Glassmorphism pill: dark semi-transparent surface `#FFFFFF0A`, rounded
     corners (~28dp), leading search icon (zinc), placeholder text
     "Search luxury, sneakers, audio..." in zinc ~14sp, trailing microphone
     icon in magenta circular badge.
3. **Hero banner** (full-width card, ~180dp height, rounded 20dp):
   - Left-aligned content: "DROP 03" label in small caps magenta ~10sp,
     "Midnight Collection" in white bold ~24sp, "Shop Now" pill button
     (magenta fill, white text, ~12sp, ~32dp height).
   - Right side: Product silhouette image over a moody purple/magenta
     photographic background.
   - Bottom-right: Horizontal dot pagination indicator (white active,
     zinc inactive).
4. **Category row** (horizontal scroll, ~80dp height per item):
   - Each category: circular icon container (~52dp) with neon magenta ring
     border (active) or dark surface border (inactive), category icon
     inside. Label below in white ~11sp.
   - Categories shown: All (active, magenta ring + gradient fill), Shoes,
     Audio, Watches, Wear.
5. **Featured section**:
   - Header row: "Featured" in white bold ~18sp (left) + "See all" in
     magenta ~13sp (right).
   - 2-column grid of product cards, each card:
     - Dark glassmorphism surface, rounded 16dp, neon magenta/violet
       subtle border glow.
     - Product image (~120dp height) centered.
     - Heart/wishlist icon (top-right of image area, zinc outline).
     - "Premium" label in magenta ~10sp.
     - Product name in white bold ~14sp.
     - Price in magenta bold ~16sp (left) + circular magenta "+" add-to-cart
       button (right, ~32dp).
6. **Bottom navigation bar** (fixed, glassmorphism surface):
   - 5 tabs: Home (magenta circle highlight when active), Search, Wishlist
     (heart), Cart (bag icon), Profile (person icon).
   - Active tab: magenta filled circle behind icon. Inactive: zinc icons.

---

### Screen 03 — Product Details (`docs/product.png`)

**Layout (single screen, not scrollable list — full product view)**:

1. **Top bar** (overlaid on image, transparent background):
   - Left: back arrow in white inside a circular dark surface (~36dp).
   - Right: share icon + heart/wishlist icon, each in circular dark
     surfaces (~36dp), spaced ~8dp apart.
2. **Product image area** (~45% of screen height):
   - Large hero product image on true black background with subtle neon
     glow/reflection beneath the product.
   - Right edge: small circular thumbnail (~40dp) with magenta ring border
     showing an alternate angle.
3. **Product info section** (below image, padded ~20dp horizontal):
   - Brand · Category breadcrumb: "Lunaris · Sneakers" in zinc ~12sp.
   - Product title: "Neon Air Max 03" in white bold ~24sp.
   - Price badge: "$249" in a magenta/gradient pill badge (~36dp height),
     positioned at trailing edge of the title row.
   - Rating row: 5 yellow star icons + "4.9 (1.2k)" in zinc ~12sp.
4. **Size selector** (labeled "SIZE · US"):
   - Horizontal row of rounded-square chips (~44dp): "7", "8", "9", "10",
     "11". Selected chip has magenta fill + white text. Unselected: dark
     surface + zinc text. Corner radius ~12dp.
5. **Color selector** (labeled "COLOR"):
   - Horizontal row of circular swatches (~32dp): magenta/pink (selected —
     magenta ring), dark gray, charcoal, blue. Selected swatch has an outer
     magenta ring.
6. **Description** (below selectors):
   - Body text in zinc ~13sp, 2–3 lines, truncated with "..." if overflow.
7. **CTA button** (bottom, full-width with horizontal padding ~20dp):
   - "🛒 Add to Cart — $249" — pill-shaped, magenta-to-violet gradient,
     white bold ~16sp, height ~56dp, full-round corners.

---

### Screen 04 — Cart & Checkout (`docs/cart-checkout.png`)

**Layout (single scrollable screen)**:

1. **Top bar**:
   - Left: back arrow (white, circular dark surface).
   - Center: "Checkout" in white bold ~20sp.
   - Right: "3 items" in zinc ~13sp.
2. **Step indicator** (horizontal, below top bar):
   - 3 steps connected by a line: (1) Address ✓ (magenta check circle),
     (2) Payment (magenta filled circle with "2"), (3) Confirm (zinc "3").
   - Active step label is white bold; inactive is zinc.
   - Connecting line: completed = magenta, pending = zinc.
3. **Cart items list** (vertical stack, each item is a card):
   - Card: dark glassmorphism surface, rounded 16dp, ~80dp height.
   - Left: small product thumbnail (~48dp, rounded 12dp).
   - Center: product name in white bold ~14sp, variant info
     ("Size 9 · Magenta") in zinc ~12sp, price in magenta ~13sp.
   - Right: quantity stepper — minus (zinc circle), count in white, plus
     (magenta filled circle), each ~28dp.
4. **Delivery address card** (rounded 16dp, dark surface):
   - Location pin icon (magenta) + "Deliver to" label in zinc ~11sp.
   - Address "221B Neon St, Tokyo" in white ~14sp.
   - "Change" link in magenta ~13sp at trailing edge.
5. **Payment section** (labeled "PAYMENT" in zinc ~11sp):
   - Credit card visual: rounded 20dp card with magenta-to-violet gradient
     fill, showing card icon (leading), "VISA" label (trailing, white),
     masked number "•••• •••• •••• 4827" in white ~16sp, cardholder name
     "Alex Lunaris" (white ~12sp, leading), expiry "09/28" (white ~12sp,
     trailing).
6. **Order summary** (bottom area, above CTA):
   - Three rows: "Subtotal $627", "Tax $50", "**Total $677**" — labels in
     zinc, amounts in white (total in magenta bold).
7. **CTA button** (bottom, full-width):
   - "Proceed to Checkout" — magenta-to-violet gradient pill, white bold
     ~16sp, ~56dp height.

---

### Screen 05 — Order Confirmation (`docs/confimation.png`)

**Layout (centered, celebratory)**:

1. **Background**: True black with animated particle/confetti dots
   scattered across the screen in magenta, violet, and pink — small circles
   at random positions with subtle float animation.
2. **Top bar**: back arrow only (left, circular dark surface).
3. **Success icon** (centered, ~30% from top):
   - Large checkmark inside a circular magenta-ringed container (~80dp).
   - Concentric glowing rings radiating outward (magenta outer ring,
     violet inner rings) creating a pulsing halo effect.
4. **Title block** (centered):
   - "Order" in white bold + "Confirmed" in magenta bold, ~28sp.
   - Subtitle: "Your order #SF-08247 has been placed." in zinc ~14sp.
   - "Estimated delivery: 2–4 days." in zinc ~13sp.
5. **Tracking card** (full-width, dark glassmorphism surface, rounded 16dp):
   - Leading: package/box icon in a magenta circular badge (~36dp).
   - "Tracking" label in zinc ~11sp + tracking ID "SF-08247-NEON" in white
     bold ~14sp.
   - Trailing: "Active" status badge in green ~12sp.
6. **Primary CTA** (full-width):
   - "Track Order" — magenta-to-violet gradient pill, white bold ~16sp,
     ~52dp height.
7. **Secondary CTA** (full-width, below primary):
   - "Continue Shopping" — outlined/bordered pill (zinc border, no fill),
     white bold ~16sp, ~52dp height.

---

### Screen 06 — Login (`docs/login.png`)

**Layout (top-aligned form)**:

1. **Background**: True black with magenta/violet glow in upper-left and
   lower-center areas.
2. **Logo** (top-left, ~16dp from top): Small ShopFlow icon (~36dp),
   magenta gradient fill.
3. **Title block** (left-aligned, below logo):
   - "Welcome" + "back" — two lines, white bold, ~32sp.
   - Subtitle: "Sign in to continue your shopping journey." in zinc ~14sp.
4. **Form fields** (vertical stack, full-width, ~16dp spacing):
   - **Email field**: "EMAIL" label in zinc uppercase ~10sp above.
     Dark surface input (~52dp height), rounded 14dp, leading mail icon
     (zinc), placeholder/value text in white ~15sp.
   - **Password field**: "PASSWORD" label in zinc uppercase ~10sp above.
     Dark surface input (~52dp height), rounded 14dp, leading lock icon
     (zinc), dot-masked text, trailing eye/toggle icon (zinc).
   - "Forgot Password?" link: right-aligned, magenta ~13sp.
5. **Primary CTA**:
   - "Sign In →" — magenta-to-violet gradient pill, white bold ~16sp,
     ~56dp height, full-width.
6. **Divider**: "OR" centered between two horizontal zinc lines.
7. **Social login buttons** (horizontal row, 2 buttons, equal width):
   - "Google" — dark surface pill with Google icon (left) + white text.
   - "Apple" — dark surface pill with Apple icon (left) + white text.
   - Both: rounded 14dp, ~48dp height.
8. **Footer link** (centered, bottom):
   - "Don't have an account? **Sign up**" — zinc text, "Sign up" in
     magenta bold.

---

### Screen 07 — Sign Up / Register (`docs/signin.png`)

**Layout (top-aligned form, scrollable)**:

1. **Background**: True black with magenta glow upper-left.
2. **Logo**: Same small ShopFlow icon top-left (~36dp).
3. **Title block** (left-aligned):
   - "Create" + "account" — two lines, white bold, ~32sp.
   - Subtitle: "Join ShopFlow and unlock premium drops." in zinc ~14sp.
4. **Form fields** (vertical stack, full-width):
   - **Full Name**: "FULL NAME" label, dark surface input, leading person
     icon, "Jordan Avery" placeholder in white ~15sp.
   - **Email**: "EMAIL" label, dark surface input, leading mail icon,
     email placeholder.
   - **Password**: "PASSWORD" label, dark surface input, leading lock icon,
     dot-masked text, trailing **password strength badge** — "STRONG" in
     a green pill (~24dp height, ~10sp bold).
   - **Terms checkbox**: Magenta filled checkbox (checked) + "I agree to
     the **Terms** & **Privacy**" — zinc text with bold white linked terms.
5. **Primary CTA**:
   - "Create Account →" — magenta-to-violet gradient pill, white bold,
     ~56dp height, full-width.
6. **Divider**: "OR CONTINUE WITH" centered in zinc uppercase ~10sp.
7. **Social buttons** (vertical stack, full-width):
   - "Continue with Google" — dark surface pill, Google icon + white text.
   - "Continue with Apple" — dark surface pill, Apple icon + white text.
   - Both: rounded 14dp, ~48dp height.
8. **Footer link** (centered):
   - "Have an account? **Sign in**" — zinc text, "Sign in" in magenta bold.

---

### Screen 08 — Wishlist + Filters (`docs/wishlist-filters.png`)

**Layout (list + bottom sheet)**:

1. **Top bar**:
   - Left: back arrow (white, circular dark surface).
   - Center: "Wishlist" in white bold ~20sp.
   - Right: "3 items" in zinc ~13sp.
2. **Wishlist items** (vertical list of cards):
   - Each card: dark glassmorphism surface, rounded 16dp, ~80dp height.
   - Left: product thumbnail (~56dp, rounded 12dp).
   - Center column: category label in zinc ~10sp (e.g., "Sneakers"),
     product name in white bold ~15sp, price in magenta ~14sp.
   - Right: circular magenta "+" (add to cart) button (~32dp).
3. **Filter bottom sheet** (modal overlay, slides up from bottom):
   - Drag handle bar (centered, zinc, ~40dp wide, 4dp height).
   - Close "×" button (top-right, zinc).
   - **"Filters"** title in white bold ~20sp.
   - **Price range slider**: "PRICE" label in zinc uppercase ~10sp.
     Dual-thumb range slider with magenta track (active range) and zinc
     track (inactive). Current range displayed as "$80 — $420" in zinc
     ~13sp (right-aligned). Thumb handles: magenta circles (~20dp).
   - **Brand chips**: "BRAND" label in zinc uppercase ~10sp.
     Horizontal wrap of pill chips: selected = magenta fill + white text,
     unselected = dark surface + zinc text. Chips: Lunaris (selected), Nyx,
     Pulse, Vortex, Aether. Corner radius full pill.
   - **Sort by chips**: "SORT BY" label in zinc uppercase ~10sp.
     Horizontal row: Popular, Newest (magenta fill), Price ↑, Price ↓.
   - **Apply CTA** (bottom, full-width):
     "Apply Filters" — magenta-to-violet gradient pill, white bold ~16sp,
     ~52dp height.

---

### Screen 09 — Profile (`docs/profile.png`)

**Layout (scrollable column)**:

1. **Top bar**:
   - Left: "Profile" in white bold ~22sp.
   - Right: settings gear icon in a circular dark surface (~36dp).
2. **Avatar section** (centered):
   - Large circular avatar (~96dp) with magenta ring border. Initials "JA"
     in white bold ~28sp on dark surface background.
   - Name: "Jordan Avery" in white bold ~22sp (centered below avatar).
   - Email: "jordan@shopflow.io" in zinc ~14sp (centered).
   - Membership badge: "✨ PREMIUM MEMBER" — magenta gradient pill (~28dp
     height), white bold uppercase ~10sp, sparkle icon leading.
3. **Stats row** (3 equal-width items, horizontal):
   - Each stat: value in white bold ~22sp + label in zinc uppercase ~10sp.
   - Items: "12 ORDERS", "24 WISHLIST", "1.2K POINTS".
   - Contained in rounded dark surface cards (~72dp height), separated by
     ~8dp gaps.
4. **Menu items** (vertical stack of navigation rows):
   - Each row: dark glassmorphism surface card, rounded 16dp, ~64dp height.
   - Left: item title in white bold ~15sp + subtitle in zinc ~12sp.
   - Right: chevron "›" in zinc.
   - Rows: "My Orders" (12 orders), "Saved Addresses" (3 saved),
     "Payment Methods" (Visa •• 4821), "Wishlist" (24 items),
     "Rewards" (1,240 pts).

---

### Screen 10 — Order History (`docs/order_history.png`)

**Layout (filterable list)**:

1. **Top bar**:
   - Left: back arrow (white, circular dark surface).
   - Center: "My Orders" in white bold ~20sp.
   - Right: search/magnifier icon (white, circular dark surface).
2. **Filter tabs** (horizontal row, below top bar):
   - Pill-shaped tabs: "All" (magenta fill, white text — active), "Active"
     (dark surface, zinc text), "Delivered", "Cancelled".
   - Rounded full pill, ~32dp height, ~8dp spacing.
3. **Order cards** (vertical scrollable list):
   - Each card: dark glassmorphism surface, rounded 16dp, ~140dp height,
     vertical padding ~16dp.
   - **Top row**: circular product thumbnail placeholder (~36dp) +
     order number "#SF-2891" in white bold ~15sp + date/item count
     "Apr 18, 2026 · 3 items" in zinc ~12sp.
   - **Status badge** (trailing on top row): pill with status text.
     Color-coded: "DELIVERED" = green fill, "PROCESSING" = orange/amber
     fill, "SHIPPED" = cyan fill. Uppercase ~10sp bold.
   - **Bottom row**: "TOTAL" label in zinc ~10sp uppercase + amount in
     magenta bold ~18sp (left). "Track Order" outlined pill button (zinc
     border, white text ~12sp, ~32dp height) at trailing edge.
   - Vertical separator between cards: ~12dp spacing.

---

### Screen 11 — Settings (`docs/settings.png`)

**Layout (grouped list)**:

1. **Top bar**:
   - Left: back arrow (white, circular dark surface).
   - Center: "Settings" in white bold ~20sp.
2. **Section: PREFERENCES** (section label in zinc uppercase ~10sp):
   - Vertical stack of toggle rows inside a grouped dark surface card
     (rounded 16dp):
     - "Push Notifications" — subtitle "Drops, deals & order updates"
       in zinc ~12sp. Trailing: magenta toggle switch (ON).
     - "Dark Theme" — subtitle "Midnight Cyber-Chic active" in zinc.
       Trailing: magenta toggle switch (ON).
     - "Biometric Login" — subtitle "Face ID enabled" in zinc.
       Trailing: magenta toggle switch (ON).
     - "Email Marketing" — subtitle "Weekly digest" in zinc, leading
       bell icon. Trailing: zinc/gray toggle switch (OFF).
   - Each row: ~60dp height, divider lines between rows (subtle zinc/dark).
3. **Section: REGION** (section label in zinc uppercase ~10sp):
   - "Language" row: dark surface card, rounded 16dp. "Language" in
     white ~15sp (left), "English (US)" in magenta ~13sp + chevron (right).
   - Below: horizontal wrap of language chips — "English" (magenta fill,
     selected), "Français", "Deutsch", "日本語", "한국어" (dark surface,
     zinc text). Pill-shaped, ~32dp height.
4. **Section: ACCOUNT & SECURITY** (section label in zinc uppercase ~10sp):
   - Vertical stack of navigation rows inside a grouped dark surface card:
     - "Change Password" — trailing chevron.
     - "Privacy & Data" — trailing chevron.
     - "Help Center" — trailing chevron.
   - Each row: white text ~15sp, ~56dp height.

---

### Bottom Navigation Bar (global, all authenticated screens)

- Fixed at bottom, glassmorphism dark surface with subtle top border.
- 5 equally-spaced tab items:
  1. **Home** — house icon.
  2. **Search** — magnifier icon.
  3. **Wishlist** — heart icon.
  4. **Cart** — shopping bag icon.
  5. **Profile** — person icon.
- **Active state**: Icon sits inside a filled magenta circle (~40dp).
- **Inactive state**: Zinc/gray icon, no background.
- No text labels — icon-only navigation.

---

### Global Design Tokens (derived from screenshots)

| Token                    | Value                                                           |
|--------------------------|-----------------------------------------------------------------|
| Screen background        | `#09090B` (true black)                                          |
| Card / surface           | `#FFFFFF0A` to `#FFFFFF14` (glassmorphism, semi-transparent)     |
| Primary gradient         | `#FF2D78` (Neon Magenta) → `#7B2FFF` (Electric Violet)         |
| Text primary             | `#F4F4F5` (off-white)                                           |
| Text secondary           | `#A1A1AA` (muted zinc)                                          |
| Accent (prices, links)   | `#FF2D78` (magenta)                                             |
| Success / Delivered      | `#22C55E` (green)                                               |
| Processing / Warning     | `#F59E0B` (amber)                                               |
| Shipped / Info           | `#06B6D4` (cyan)                                                |
| CTA button height        | ~56dp (primary), ~52dp (secondary)                              |
| Card corner radius       | 16dp (standard), 20dp (hero/banner), 28dp (full pill buttons)   |
| Input field height       | ~52dp                                                           |
| Icon container size      | ~36dp (top bar actions), ~32dp (inline add-to-cart)             |
| Avatar ring border       | 2dp magenta stroke                                              |
| Typography (display)     | ~32sp bold (titles)                                             |
| Typography (heading)     | ~20–24sp bold (screen titles, section heads)                    |
| Typography (body)        | ~14–15sp regular (descriptions, form values)                    |
| Typography (caption)     | ~10–12sp (labels, badges, subtitles)                            |
| Horizontal page padding  | ~16–24dp                                                        |
| Card internal padding    | ~16dp                                                           |
| Spacing between cards    | ~12dp                                                           |
