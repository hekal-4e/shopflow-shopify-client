# n8n Webhook Integration Review

## Status
**FAIL**

The implementation is not production-safe in its current form. It avoids an obvious user-facing crash on webhook failure, but it has a credential-leak risk, the business trigger is wired incorrectly for an "abandoned cart" event, and there are reliability issues in the repository implementation.

## Critical Findings

### 1. Shopify storefront token is leaked to the third-party n8n webhook
- **Files:** `app/src/main/java/com/shopflow/app/di/NetworkModule.kt:20-28`, `app/src/main/java/com/shopflow/app/data/remote/interceptor/ShopifyAuthInterceptor.kt:8-13`, `app/src/main/java/com/shopflow/app/data/repository/CartRepositoryImpl.kt:25-27`, `app/src/main/java/com/shopflow/app/data/repository/CartRepositoryImpl.kt:121-127`
- **Problem:** `CartRepositoryImpl` receives the app-wide `OkHttpClient`, and that client always adds `X-Shopify-Storefront-Access-Token` via `ShopifyAuthInterceptor`. The n8n request is sent to `https://hekl.app.n8n.cloud/...`, so the Shopify token will be attached to a third-party host.
- **Why this is critical:** This is a security boundary violation. A store credential intended only for Shopify is being exfiltrated to an external automation endpoint.
- **Architectural verdict:** DI is technically used, but the wrong dependency is injected. This should be a dedicated, qualified webhook client with no Shopify interceptor.

### 2. The feature is not actually "abandoned cart" behavior
- **Files:** `app/src/main/java/com/shopflow/app/presentation/screens/home/HomeViewModel.kt:137-150`
- **Problem:** The webhook is fired immediately inside `addToCart()`. That means every successful add-to-cart action is treated as abandonment, even if the user continues shopping or completes checkout seconds later.
- **Why this is critical:** This produces false positives, noisy automation, and potentially incorrect customer outreach. It is a functional bug, not just an implementation preference.
- **Architectural verdict:** The retention workflow should be triggered by an abandonment policy, not directly from the add-to-cart UI event.

### 3. HTTP responses are never closed
- **Files:** `app/src/main/java/com/shopflow/app/data/repository/CartRepositoryImpl.kt:127-133`
- **Problem:** `okHttpClient.newCall(request).execute()` is used without closing the `Response`.
- **Why this is critical:** Repeated calls can leak connections/resources and eventually degrade networking behavior under real usage.
- **Expected fix direction:** Wrap the response with `use { ... }`.

## Code Smells

### 1. Clean Architecture is bent by placing webhook workflow orchestration in the ViewModel
- **Files:** `app/src/main/java/com/shopflow/app/presentation/screens/home/HomeViewModel.kt:142-150`
- The ViewModel is coordinating a cross-cutting retention side effect by fetching auth state, loading the customer profile, extracting email, and calling the repository directly.
- Network code is not literally implemented in the ViewModel, but business workflow orchestration is leaking into presentation instead of being encapsulated in a dedicated use case such as `NotifyCartAbandonmentUseCase` or, better, a real abandonment scheduler/use case.

### 2. Domain layer contains a transport-shaped request model
- **Files:** `app/src/main/java/com/shopflow/app/domain/model/CartWebhookRequest.kt:1-3`
- `CartWebhookRequest` is pure Kotlin, so it does not violate Android dependency rules, but it is still the wrong abstraction for the domain layer.
- The snake_case field names (`user_email`, `product_name`) mirror wire-format JSON rather than domain language. That makes this object a remote DTO disguised as a domain model.
- It is also currently unused, which means the payload contract is duplicated manually in `JSONObject` construction.

### 3. Error handling is silent, but too broad
- **Files:** `app/src/main/java/com/shopflow/app/data/repository/CartRepositoryImpl.kt:111-136`, `app/src/main/java/com/shopflow/app/presentation/screens/home/HomeViewModel.kt:144-153`
- The app likely will not crash if the webhook is down or there is no internet, because exceptions are caught and the result is ignored.
- However, both layers catch `Exception`, which also risks swallowing `CancellationException` in coroutine code. That is not cancellation-safe and can keep cancelled work running longer than intended.
- Silent failure should be intentional and narrow: catch expected network failures, rethrow cancellation, and route diagnostics to logging/telemetry.

### 4. Hardcoded webhook URL in repository implementation
- **Files:** `app/src/main/java/com/shopflow/app/data/repository/CartRepositoryImpl.kt:121-123`
- The endpoint is embedded directly in the repository, making environment switching, rotation, feature flags, and testability harder than necessary.
- This should come from configuration or an injected service abstraction.

### 5. Result is ignored at the call site
- **Files:** `app/src/main/java/com/shopflow/app/presentation/screens/home/HomeViewModel.kt:149`
- `notifyCartAbandonment(...)` returns `Result<Unit>`, but the result is discarded completely.
- That is acceptable if failure must be fully silent, but then it should still be logged or traced somewhere for observability.

### 6. No automated coverage found for this integration path
- **Files reviewed:** `app/src/test`, `app/src/androidTest`
- I found no test references covering `notifyCartAbandonment`, `CartRepositoryImpl`, or the `HomeViewModel` integration path for this feature.
- This leaves the security/configuration behavior and failure handling unguarded.

## Suggestions for Polish

### 1. Move the integration behind a dedicated use case
- Create a domain use case that expresses intent, not transport, for example `ScheduleAbandonedCartNotificationUseCase`.
- The ViewModel should only invoke that use case after the add-to-cart action succeeds.

### 2. Use a dedicated webhook client or API abstraction
- Inject a qualified `OkHttpClient` for n8n, or better, a small `WebhookApi`/`CartAbandonmentNotifier` abstraction.
- Do not reuse the Shopify-authenticated client for third-party traffic.

### 3. Model abandonment explicitly
- Trigger after inactivity, app backgrounding, elapsed timeout, or a server-side abandonment decision.
- Cancel/suppress the signal when checkout completes or the cart is cleared.

### 4. Replace manual `JSONObject` building with typed serialization
- Either move `CartWebhookRequest` to a data/remote DTO package and serialize it with a proper serializer, or remove it entirely if manual JSON remains.
- This gives compile-time consistency and makes payload tests straightforward.

### 5. Make failure silent but observable
- Keep the user experience unaffected on failure.
- Re-throw `CancellationException`.
- Log network failures through Timber/Crashlytics/analytics so the team can detect webhook outages.

### 6. Close the response and verify status handling
- Use `execute().use { response -> ... }`.
- Consider treating non-2xx codes as a logged failure with structured diagnostics rather than `Exception("Webhook failed with code...")`.

## Checklist Verdict

### 1. Clean Architecture Violations
- **Needs revision**
- No raw network call is implemented in the ViewModel, but the ViewModel is orchestrating integration behavior that should live in a use case.
- `CartWebhookRequest` is not a true domain model; it is a transport concern placed in the domain layer.

### 2. Coroutines & Threading Safety
- **Partial pass**
- `withContext(Dispatchers.IO)` is used in the repository, which is correct for the blocking OkHttp call.
- `viewModelScope.launch` is used correctly at a high level, but broad `catch (Exception)` blocks are not cancellation-safe.

### 3. Error Handling (Silent Fail)
- **Partial pass / needs revision**
- The app is unlikely to crash when the webhook is unavailable because exceptions are caught.
- The current implementation is too silent, too broad, and not observable enough for production operations.

### 4. Dependency Injection
- **Needs revision**
- `OkHttpClient` is injected via Hilt, which is good.
- Reusing the Shopify-authenticated client for the n8n endpoint is a design flaw and a security issue.

### 5. JSON Serialization
- **Partial pass**
- The runtime payload shape is valid JSON.
- The implementation is weakly typed, duplicates the payload contract manually, and leaves the unused `CartWebhookRequest` model disconnected from actual serialization.
