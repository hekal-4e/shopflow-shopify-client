# Apollo Kotlin
-keep class com.apollographql.** { *; }
-keep class **.databinding.*Binding { *; }
-dontwarn com.apollographql.**

# Dagger Hilt
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponentManager { *; }
-keep class * extends dagger.hilt.internal.processedrootsentinel.ProcessedRootSentinel { *; }
-dontwarn dagger.hilt.**

# Room
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-dontwarn androidx.room.**

# Kotlin serialization/runtime metadata
-keepclassmembers class kotlinx.serialization.** { *; }
-keep class kotlinx.serialization.** { *; }
-keep class kotlin.Metadata { *; }
