# Quickstart: ShopFlow E-Commerce App

**Feature**: `001-shopflow-ecommerce-app`
**Date**: 2026-04-22

---

## Prerequisites

1. **Android Studio** — Latest stable (Ladybug or later)
2. **JDK 17+** — Required for Gradle and Kotlin compilation
3. **Android SDK** — API 26 (min) and latest stable (target)
4. **Shopify Partner Account** — With a development store configured
5. **Shopify Storefront Access Token** — From your Shopify store admin
   under Settings → Apps and sales channels → Develop apps →
   Storefront API access

## Environment Setup

1. Clone the repository and switch to the feature branch:
   ```bash
   git clone <repo-url> ShopFlow
   cd ShopFlow
   git checkout 001-shopflow-ecommerce-app
   ```

2. Create a `local.properties` file in the project root (if not present):
   ```properties
   sdk.dir=/path/to/your/Android/Sdk
   ```

3. Create a `secrets.properties` file in the project root (**never commit**):
   ```properties
   SHOPIFY_STORE_DOMAIN=your-store.myshopify.com
   SHOPIFY_STOREFRONT_ACCESS_TOKEN=your-storefront-access-token
   ```

4. Sync Gradle and build:
   ```bash
   ./gradlew assembleDebug
   ```

## Running the App

```bash
./gradlew installDebug
adb shell am start -n com.shopflow.app/.MainActivity
```

Or simply click **Run** (▶) in Android Studio.

## Project Structure Overview

```text
app/
├── src/main/
│   ├── java/com/shopflow/app/
│   │   ├── ShopFlowApp.kt              # @HiltAndroidApp Application class
│   │   ├── MainActivity.kt             # Single Activity, Compose host
│   │   ├── data/                        # Data layer
│   │   │   ├── remote/                  # Apollo GraphQL data sources
│   │   │   ├── local/                   # Room DAOs, DataStore
│   │   │   ├── repository/             # Repository implementations
│   │   │   ├── mapper/                  # DTO → Domain mappers
│   │   │   └── model/                   # DTOs (Apollo-generated + Room entities)
│   │   ├── domain/                      # Domain layer (zero Android imports)
│   │   │   ├── model/                   # Domain models (data classes)
│   │   │   ├── repository/             # Repository interfaces
│   │   │   └── usecase/                 # Use cases
│   │   ├── presentation/               # Presentation layer
│   │   │   ├── navigation/             # Nav graph, routes
│   │   │   ├── theme/                   # ShopFlowTheme, colors, typography
│   │   │   ├── components/             # Reusable composables
│   │   │   └── screens/                # Feature screens
│   │   │       ├── splash/
│   │   │       ├── onboarding/
│   │   │       ├── auth/
│   │   │       ├── home/
│   │   │       ├── product/
│   │   │       ├── cart/
│   │   │       ├── checkout/
│   │   │       ├── confirmation/
│   │   │       ├── profile/
│   │   │       ├── orders/
│   │   │       ├── wishlist/
│   │   │       ├── notifications/
│   │   │       └── settings/
│   │   └── di/                          # Hilt modules
│   │       ├── NetworkModule.kt
│   │       ├── RepositoryModule.kt
│   │       ├── DatabaseModule.kt
│   │       └── UseCaseModule.kt
│   ├── graphql/                         # .graphql query/mutation files
│   └── res/
│       ├── values/strings.xml
│       └── font/                        # Inter / Outfit font files
└── build.gradle.kts
```

## Key Commands

| Action                  | Command                              |
|-------------------------|--------------------------------------|
| Build debug APK         | `./gradlew assembleDebug`            |
| Run unit tests          | `./gradlew testDebugUnitTest`        |
| Run instrumented tests  | `./gradlew connectedDebugAndroidTest`|
| Lint check              | `./gradlew lintDebug`                |
| Generate Apollo models  | `./gradlew generateApolloSources`    |
| Clean build             | `./gradlew clean assembleDebug`      |

## Verifying the Setup

After first build, verify:

1. **App launches** → Splash screen with ShopFlow logo appears
2. **Apollo codegen** → `build/generated/source/apollo/` contains
   generated Kotlin models
3. **Hilt injection** → No runtime `UninitializedPropertyAccessException`
4. **Shopify connection** → Home screen loads products (requires valid
   access token in `secrets.properties`)

## Troubleshooting

| Problem                           | Solution                                    |
|-----------------------------------|---------------------------------------------|
| Apollo codegen fails              | Verify `.graphql` files are in `src/main/graphql/` and schema is downloaded |
| Hilt "missing binding" error      | Ensure all `@Module` classes are annotated with `@InstallIn` |
| "SDK not found" build error       | Verify `local.properties` has correct `sdk.dir` path |
| Products don't load               | Check `secrets.properties` token is valid; check logcat for network errors |
| Room schema conflict              | Run `./gradlew clean` then rebuild          |
