# Consumer R8/ProGuard rules — merged into apps that depend on this library
# (minify, shrinkResources, android.enableR8.fullMode=true).

# Kotlin enums in common: stable values() / valueOf for Java callers and reflective APIs.
-keepclassmembers enum com.appdimens.ssps.common.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Non-Compose API: DimenSsp @JvmStatic / @JvmOverloads entry points.
-keep class com.appdimens.ssps.code.DimenSsp {
    public static *** *(...);
}
