# ============================================================================
# AppDimens SSPS — App module · R8 rules (Full Mode + aggressive)
# R8 full mode is enabled in gradle.properties (android.enableR8.fullMode=true).
# Goal: maximum optimization (shrink, obfuscate, optimize) with maximum
# compatibility across device/API levels.
# ============================================================================

# --- Stack traces -----------------------------------------------------------
# Keep line numbers for readable crash reports (negligible size cost).
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# --- Deeper inlining / access modification ----------------------------------
# Lets R8 change method/field access modifiers (e.g. private→public) to enable
# more inlining and member moving. Safe for app code (no external callers).
-allowaccessmodification

# --- ViewBinding / DataBinding generated classes ----------------------------
# Generated binding + mapper classes are looked up by the data binding runtime;
# keep them intact so inflated layouts keep working under full mode.
-keep class * extends androidx.databinding.ViewDataBinding { *; }
-keep class * extends androidx.databinding.DataBinderMapper { *; }
-keep class * extends androidx.databinding.DataBindingComponent { *; }
-keep class * implements androidx.databinding.DataBindingComponent { *; }
-keep class * extends androidx.databinding.BaseObservable { *; }

# --- Custom Views inflated from XML ------------------------------------------
# If you add custom View subclasses inflated via layout XML, uncomment:
#-keepclasseswithmembers class * { public <init>(android.content.Context); }
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}

# --- WebView JS interfaces ---------------------------------------------------
# If you expose a @JavascriptInterface class to a WebView, uncomment and adapt:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#    public *;
#}

# --- AppDimens SSPS library ---------------------------------------------------
# The public API of com.appdimens.ssps.** is kept automatically through the
# library's consumer-rules.pro (merged by AGP into this build).