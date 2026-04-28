# ShopFlow Mock App Flow & Launch Verification

This document describes expected runtime behavior from app launch through all major flows.

## 1) App Launch Decision Flow

Entry point:
- `MainActivity` -> `ShopFlowNavGraph(startDestination = splash)`

Splash routing:
1. If onboarding is not completed -> navigate to `Onboarding`.
2. If onboarding is completed and user is not authenticated -> navigate to `Login`.
3. If onboarding is completed and user is authenticated (non-expired token) -> navigate to `Home`.

## 2) Guest User Flow (First Install)

1. Launch app -> `Splash`.
2. Auto route to `Onboarding`.
3. Tap `Get Started` on onboarding final page.
4. Expected route: `Login` (not `Home`) when not authenticated.
5. From `Login`, user can:
- Sign in (valid account) -> `Home`.
- Navigate to `Register` -> create account -> `Home`.

## 3) Authenticated User Flow (Returning User)

1. Launch app with valid stored token.
2. Splash auto routes to `Home`.
3. Bottom navigation available: `Home`, `Search`, `Wishlist`, `Cart`, `Profile`.

## 4) Browse -> Product -> Cart Flow

1. Home shows collections, featured products, and search.
2. Tap product card -> `ProductDetail`.
3. Tap `Add to Cart` -> item added to cart state.
4. Navigate to `Cart` and verify line item appears.

Cart persistence behavior:
- After process kill / app restart, previously added cart items should still be present.
- Quantity changes and removals should persist.
- Clearing cart should remove persisted state.

## 5) Checkout Flow

1. In `Cart`, tap `Proceed to Checkout`.
2. If not authenticated -> route to `Login`.
3. If authenticated -> route to `Checkout`.
4. App creates Shopify checkout and opens checkout URL in browser.
5. If checkout creation fails, error text is shown in `CheckoutScreen`.

## 6) Account Flow

1. `Profile` -> route to `OrderHistory`, `Wishlist`, `Settings`.
2. `Notifications` accessible from Home bell icon.
3. `Settings` logout should clear auth and return to splash/login path.

## 7) Manual Runtime Verification Checklist

Run this checklist on emulator/device:

1. Fresh install:
- Verify splash -> onboarding -> login.

2. Auth:
- Login with valid credentials -> home.
- Relaunch app -> auto home.

3. Cart persistence:
- Add 2 products to cart.
- Force close app.
- Relaunch app.
- Verify cart still contains same items and quantities.

4. Checkout guard:
- Logout.
- Go to cart and tap checkout.
- Verify login screen opens.

5. Checkout launch:
- Login, open checkout.
- Verify browser opens checkout URL.

6. Wishlist:
- Add/remove wishlist items.
- Verify persistence and list rendering.

## 8) Current Validation Status in This Workspace

Validated via Gradle:
- `assembleDebug`: PASS
- `compileDebugKotlin`: PASS

Not fully validated in this environment:
- Device/emulator runtime launch (manual run required)
- Connected Android instrumentation tests
