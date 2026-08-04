# Consumer R8/ProGuard rules — merged into apps that depend on this library
# (minify, shrinkResources, android.enableR8.fullMode=true).

# Kotlin enums in common: stable values() / valueOf for Java callers and reflective APIs.
-keepclassmembers enum com.appdimens.ssps.common.** {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Non-Compose API: DimenSsp @JvmStatic / @JvmOverloads entry points.
-keep class com.appdimens.ssps.code.DimenSsp {
    public static *** *(...);
}

# --- Aspect-ratio factor cache + dimen ID cache (reachable from *a / resolve paths) ---
-keep class com.appdimens.ssps.core.AppDimensSspsFactors { *; }
-keep class com.appdimens.ssps.core.DimenResourceIdCache { *; }

# --- ScaledSp builders (code + Compose packages) ---
-keep class com.appdimens.ssps.code.ScaledSp { *; }
-keep class com.appdimens.ssps.compose.ScaledSp { *; }
