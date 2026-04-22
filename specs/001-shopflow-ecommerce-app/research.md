# Research: ShopFlow E-Commerce App

**Feature**: `001-shopflow-ecommerce-app`
**Date**: 2026-04-22
**Status**: Complete — all unknowns resolved

---

## R-001: Shopify Storefront API (GraphQL) — Client & Auth Strategy

**Decision**: Use Apollo Kotlin as the sole GraphQL client for Shopify
Storefront API communication.

**Rationale**:
- Apollo Kotlin is the official Kotlin-first GraphQL client with first-class
  coroutine support, codegen from `.graphql` files, and normalized caching.
- Constitution Principle V mandates Apollo Kotlin as the only GraphQL
  implementation.
- Storefront API uses a public access token (X-Shopify-Storefront-Access-Token
  header) for unauthenticated product browsing, and customer access tokens for
  authenticated operations (profile, orders).

**Alternatives considered**:
- **GraphQL Kotlin (expedia)**: Server-focused, not suitable for Android client.
- **Raw OkHttp + manual JSON**: Violates DRY, no type safety, no codegen.
- **Retrofit + JSON converters**: REST-oriented; Storefront API is GraphQL-only.

---

## R-002: Shopify Customer Authentication Flow

**Decision**: Use Shopify's `customerAccessTokenCreate` mutation for login and
`customerCreate` mutation for registration, both via Storefront API GraphQL.

**Rationale**:
- Storefront API exposes customer auth mutations directly — no OAuth redirect
  needed for basic email/password flows.
- Social login (Google/Apple) is handled via Android's native credential
  manager APIs, with the resulting identity token exchanged for a Shopify
  customer account via `customerCreate` or linked via multipass (if configured).
- Access tokens are stored in encrypted DataStore for session persistence.

**Alternatives considered**:
- **Shopify OAuth (Admin API)**: Requires a backend proxy; Storefront API
  customer tokens are simpler for mobile.
- **Firebase Auth**: Prohibited by constitution.

---

## R-003: Checkout Strategy — Shopify Web Checkout

**Decision**: Use Shopify's `checkoutCreate` / `checkoutLineItemsAdd` mutations
to build a checkout object, then open the `webUrl` in a Custom Tab (Chrome
Custom Tab / Android Custom Tab) for payment.

**Rationale**:
- Storefront API does not expose payment processing directly. The official
  pattern is to create a checkout via GraphQL, then redirect the user to
  Shopify's hosted checkout page.
- Chrome Custom Tabs provide an in-app feel while delegating PCI-compliant
  payment handling to Shopify.
- On completion, Shopify redirects back to the app via a deep link, enabling
  the app to display the Order Confirmation screen.
- The Shopify-side order creation automatically triggers any configured
  webhooks (n8n) — no app-side webhook logic needed.

**Alternatives considered**:
- **Shopify Checkout Sheet Kit (iOS-only at time of writing)**: No Android
  SDK available.
- **Custom in-app payment form**: PCI compliance burden, out of scope.

---

## R-004: Local Persistence Strategy

**Decision**: Room for wishlist + product cache; DataStore Preferences for
user settings, onboarding state, and encrypted auth tokens.

**Rationale**:
- Wishlist is local-only (Storefront API has no wishlist endpoint).
- Product cache enables offline browsing of previously loaded data.
- DataStore is lighter than Room for key-value preferences and integrates
  well with Kotlin coroutines.
- Constitution permits Room for offline caching and DataStore for prefs.

**Alternatives considered**:
- **SQLDelight**: Viable, but Room has broader Android ecosystem support and
  is more familiar to most Android developers.
- **SharedPreferences**: Deprecated pattern, DataStore is the successor.

---

## R-005: Image Loading

**Decision**: Coil (Compose integration) for all image loading.

**Rationale**:
- Constitution mandates Coil.
- Coil is Kotlin-first, coroutine-based, and has native Compose
  `AsyncImage` composable support.
- Supports memory + disk caching out of the box.

**Alternatives considered**:
- **Glide**: Java-centric, less idiomatic Kotlin/Compose integration.
- **Picasso**: Outdated, no Compose-native API.

---

## R-006: Navigation Architecture

**Decision**: Jetpack Navigation Compose with a sealed class route hierarchy.

**Rationale**:
- Constitution mandates Jetpack Navigation Compose.
- Type-safe routes via sealed classes/data objects align with Kotlin idioms.
- Bottom navigation integration is straightforward with
  `NavigationBarItem` + `NavBackStackEntry`.

**Alternatives considered**:
- **Voyager**: Third-party, not officially supported by Google.
- **Compose Destinations (Kiwi)**: Extra codegen layer, unnecessary complexity.

---

## R-007: Notification Strategy (In-App Only)

**Decision**: In-app notification system powered by local Room storage +
periodic Shopify order-status polling via WorkManager.

**Rationale**:
- Firebase is prohibited; push notifications are deferred.
- Polling order status from Shopify via `customer.orders` query on a periodic
  schedule (e.g., every 15 minutes when app is backgrounded) satisfies the
  order-update notification requirement.
- Promotional notifications are sourced from Shopify collections/metafields
  at app launch.

**Alternatives considered**:
- **Firebase Cloud Messaging**: Prohibited by constitution.
- **Custom WebSocket server**: Out of scope; no custom backend allowed.

---

## R-008: Social Login Integration

**Decision**: Use Android Credential Manager API for Google Sign-In and
Apple Sign In, then create/link Shopify customer accounts via
`customerCreate` mutation with the social identity email.

**Rationale**:
- Credential Manager is Google's recommended API replacing legacy
  Google Sign-In SDK.
- Apple Sign In on Android is handled via web-based OAuth redirect
  (Apple's JS SDK in a Custom Tab) since Apple doesn't provide a native
  Android SDK.
- After obtaining the social identity, the app creates or retrieves a
  Shopify customer account using the verified email.

**Alternatives considered**:
- **Firebase Auth (social providers)**: Prohibited.
- **OneSignal / Auth0**: Additional external dependency, overkill.

---

## R-009: Build System & Dependency Management

**Decision**: Gradle with Kotlin DSL, version catalog (`libs.versions.toml`),
convention plugins for shared build logic.

**Rationale**:
- Constitution mandates Kotlin DSL and version catalogs.
- Convention plugins reduce duplication across module `build.gradle.kts` files.
- Single-module architecture initially (Clean Architecture enforced by package
  structure, not Gradle modules) to minimize build complexity. Module
  extraction deferred per KISS principle.

**Alternatives considered**:
- **Multi-module from day one**: Premature for initial release; KISS principle.
- **Groovy DSL**: Prohibited by constitution (Kotlin DSL mandated).
