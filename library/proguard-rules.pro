# ============================================================================
# AppDimens SSPS — Library module · self-build R8 rules
# Applied only if this AAR is compiled with minifyEnabled = true
# (release build of the library itself).
#
# Keeps the complete public API contract so consumers keep working;
# private/internal code stays fully shrinkable/optimizable.
# ============================================================================

# --- Public API surface ------------------------------------------------------
# Keep every public/protected member of all public types. Internal/private
# members are still removed and optimized by R8 full mode.
-keep class com.appdimens.ssps.** { public protected *; }

# --- Enums (Java interop: Enum.values / Enum.valueOf / constants) ------------
-keepclassmembers enum com.appdimens.ssps.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# --- Compose ------------------------------------------------------------------
# Preserve runtime-visible annotations (Kotlin @Metadata, @Composable, etc.)
# and all @Composable members so Compose-compiled consumers keep working.
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

# --- Dynamic resource lookup ---------------------------------------------------
# getIdentifier() lookups ("_1ssp", "_1wsp", "_1hsp", ...) are resource-level;
# names are preserved via res/raw/keep.xml (tools:keep) when the consuming
# app enables shrinkResources.