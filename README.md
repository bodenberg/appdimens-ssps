# AppDimens SSP, HSP, WSP

![AppDimens Banner](IMAGES/banner_top.png)

**AppDimens** is the most complete responsive typography and dimension library for Android. It provides thousands of pre-calculated `@dimen` resources ready to use — plus dynamic Compose extensions, code-level APIs, conditional builders, orientation-aware inverters, and accessibility-aware variants — all in a single, zero-configuration dependency.

---

## 🛠️ Installation

```kotlin
dependencies {
    implementation("io.github.bodenberg:appdimens-ssps:3.0.8")
}
```

**Requirements:** Min SDK 24 · Compile SDK 36 · Kotlin & Java · XML & Jetpack Compose

---

## 💻 Usage Examples

### 1. Jetpack Compose

**Basic — Auto-Scaling Extensions:**
```kotlin
import com.appdimens.ssps.compose.ssp
import com.appdimens.ssps.compose.hsp
import com.appdimens.ssps.compose.wsp
import com.appdimens.ssps.compose.sem

Text(
    text = "Smallest Width Scaling",
    fontSize = 16.ssp,      // Scales relative to the device's smallest width
    lineHeight = 20.ssp
)

Text(
    text = "Width Scaling",
    fontSize = 18.wsp       // Scales relative to the device's width
)

Text(
    text = "Height Scaling",
    fontSize = 20.hsp       // Scales relative to the device's height
)

Text(
    text = "Fixed Scale",
    fontSize = 14.sem       // Scales by Smallest Width but IGNORES system font scale
)
```

**Inverter Shortcuts — Orientation-Aware Scaling:**
```kotlin
import com.appdimens.ssps.compose.sspPh
import com.appdimens.ssps.compose.sspLw
import com.appdimens.ssps.compose.hspLw
import com.appdimens.ssps.compose.wspLh

// .sspPh → uses Smallest Width by default; in Portrait → switches to Height
val adaptiveVert = 32.sspPh

// .sspLw → uses Smallest Width by default; in Landscape → switches to Width
val adaptiveHorz = 32.sspLw

// .hspLw → uses Height by default; in Landscape → switches to Width
val heightToWidth = 50.hspLw

// .wspLh → uses Width by default; in Landscape → switches to Height
val widthToHeight = 50.wspLh
```

**Facilitators — Quick Conditional Overrides:**
```kotlin
import com.appdimens.ssps.compose.sspRotate
import com.appdimens.ssps.compose.sspMode
import com.appdimens.ssps.compose.sspQualifier
import com.appdimens.ssps.compose.sspScreen

// Rotate: 16.ssp default, 24.ssp in Landscape
Text("Scalable", fontSize = 16.sspRotate(24))

// Mode: 16.ssp default, 40.ssp on TV
val modeVal = 16.sspMode(40, UiModeType.TELEVISION)

// Qualifier: 16.ssp default, 24.ssp when sw ≥ 600dp
val qualVal = 16.sspQualifier(24, DpQualifier.SMALL_WIDTH, 600)

// Screen: 16.ssp default, 32.ssp on TV with sw ≥ 600dp
val scrVal = 16.sspScreen(32, UiModeType.TELEVISION, DpQualifier.SMALL_WIDTH, 600)
```

**DimenSspScaled Builder — Complex Multi-Condition Chains:**
```kotlin
import com.appdimens.ssps.compose.scaledSp

val dynamicText = 16.scaledSp()
    // Priority 1: TV + sw ≥ 600 → 40.ssp
    .screen(UiModeType.TELEVISION, DpQualifier.SMALL_WIDTH, 600, 40.ssp)
    // Priority 2: Any TV → 32.ssp
    .screen(UiModeType.TELEVISION, customValue = 32.ssp)
    // Priority 3: Landscape → 20.wsp
    .screen(orientation = Orientation.LANDSCAPE, customValue = 20.wsp)
    .ssp // Resolve with Smallest Width adaptation as fallback
```

### 2. XML Layouts

Use dimension resources directly — all values from `1` to `600` are pre-generated:

```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <!-- SSP: Scales based on smallest width -->
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="@dimen/_16ssp"
        android:text="Smallest Width Scaled" />

    <!-- WSP: Scales based on screen width -->
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="@dimen/_16wsp"
        android:text="Width Scaled" />

    <!-- HSP: Scales based on screen height -->
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="@dimen/_16hsp"
        android:text="Height Scaled" />

    <!-- SEM: Scales by Smallest Width but ignores system font scale -->
    <TextView
        android:textSize="@dimen/_16sem"
        android:text="No Accessibility Scale" />
</LinearLayout>
```

### 3. Kotlin (Code Level)

```kotlin
// Core — Pixel values (converted from Sp)
val fontSizePx = DimenSsp.ssp(context, 16)    // Smallest Width
val heightPx   = DimenSsp.hsp(context, 32)    // Height
val widthPx    = DimenSsp.wsp(context, 100)   // Width

// Accessibility — Ignore font scaling
val fixedSpPx  = DimenSsp.sem(context, 16)    // Without font scaling

// Kotlin Extensions
import com.appdimens.ssps.code.ssp
import com.appdimens.ssps.code.hsp
import com.appdimens.ssps.code.scaledSp

val size = 16.ssp(context)
val adaptiveFont = 16.hsp(context)

// DimenSspScaled builder
val builderSp = 16.scaledSp()
    .screen(UiModeType.TELEVISION, 40)
    .ssp(context)

// Resource IDs
val resId = DimenSsp.sspRes(context, 16)

// Inverter shortcuts
val adaptive = DimenSsp.hspLw(context, 20)    // Height → Width in Landscape

// Facilitators
val rotated = DimenSsp.sspRotate(context, 16, 24)
val modeVal = DimenSsp.sspMode(context, 16, 32, UiModeType.TELEVISION)
```

### 4. Java (Code Level)

```java
// Core
float fontSizePx = DimenSsp.ssp(context, 16);
int resId = DimenSsp.sspRes(context, 16);

// Accessibility
float fixedSpPx = DimenSsp.sem(context, 16);

// Inverter shortcuts
float adaptive = DimenSsp.hspLw(context, 20);

// DimenSspScaled builder (uses @JvmStatic + @JvmOverloads)
DimenSspScaled scaled = DimenSsp.scaled(16)
    .screen(UiModeType.TELEVISION, 32)
    .screen(DpQualifier.SMALL_WIDTH, 600, 24);

float result = scaled.ssp(context);
```

<br/>
<p align="center">
  <img src="IMAGES/screenshot.png" alt="Layout example" width="25%" />
</p>
<br/>

---

## ✨ What's New in Version 3.x

| Feature | Description |
|---------|-------------|
| **Triple Axis Scaling** | Full support for SSP (Smallest Width), HSP (Height), and WSP (Width) |
| **Accessibility Control** | SEM, HEM, WEM variants to ignore system font scale when necessary |
| **Code-Level API** | Full `DimenSsp` object for Java & Kotlin — resolve text sizes outside of XML and Compose |
| **Inverter Shortcuts** | `.sspPh`, `.sspLw`, `.hspPw`, `.wspLh`, etc. — orientation-aware switching |
| **Facilitators** | `sspRotate`, `sspMode`, `sspQualifier`, `sspScreen` — quick conditional overrides |
| **Advanced Builders** | `DimenSspScaled` for complex chaining with `UiModeType` and `DpQualifier` |
| **Foldable Detection** | `FoldingFeature` integration — detects Fold/Flip open/half-open states |
| **UiModeType** | `NORMAL`, `TELEVISION`, `CAR`, `WATCH`, `FOLD_OPEN`, `FOLD_HALF`, `FLIP_OPEN`, `FLIP_HALF` |

---

## 🧮 Why Pre-Calculated Scales?

Most responsive Android solutions use runtime calculations to convert dimensions — multiplying density, screen metrics, or ratios on every frame or measure pass. **AppDimens takes a fundamentally different approach:**

### The Problem with Runtime Calculations

```kotlin
// ❌ Runtime calculation approach (common in other libraries)
fun scaledSp(value: Int): Float {
    val screenWidth = resources.displayMetrics.widthPixels
    val baseWidth = 360f // arbitrary "design" base
    return value * (screenWidth / baseWidth) // calculated EVERY call
}
```

This has several issues:
- **CPU Cost** — calculated on every call, wasted cycles
- **Linear Scaling** — simple ratios produce values that are too large on tablets or too small on watches
- **No Qualifier Awareness** — ignores Android's built-in resource qualifier system (`values-sw600dp`, etc.)

### The AppDimens Solution: Pre-Calculated + Qualifier-Aware

AppDimens provides **thousands of `@dimen` resources** generated with **mathematically refined, non-linear scaling curves** tuned for each qualifier bucket:

```
res/
├── values/           → Base values (phones ~320-360dp)
├── values-sw360dp/   → Standard phones
├── values-sw600dp/   → 7" tablets
├── values-sw720dp/   → 10" tablets
├── values-h600dp/    → Height-based qualifiers
├── values-w600dp/    → Width-based qualifiers
└── ...               → 350+ qualifier directories
```

Each value is **pre-calculated** to produce dimensions tuned specifically for that screen category.

---

## ⚡ Performance

### XML: Zero Cost
All `@dimen/_16ssp` resources are **resolved statically at build time**. No runtime overhead.

### Compose: Near-Zero Cost
The `.ssp`, `.hsp`, `.wsp` extensions use:
- `LocalConfiguration.current` — cached by Compose
- `LocalContext.current.resources.getIdentifier()` — native lookup
- `dimensionResource()` — standard Compose resolution

No extra state or unnecessary recompositions.

---

## 📖 How It Works

### Three Scaling Axes

| Qualifier | Extension | Resource | Based On |
|-----------|-----------|----------|----------|
| **SSP** | `.ssp` | `@dimen/_16ssp` | `smallestScreenWidthDp` — independent of orientation |
| **HSP** | `.hsp` | `@dimen/_16hsp` | `screenHeightDp` — current screen height |
| **WSP** | `.wsp` | `@dimen/_16wsp` | `screenWidthDp` — current screen width |

### Resource Naming Convention

```
_{value}{qualifier}

Examples:
  _16ssp      →  16sp scaled by Smallest Width
  _20wsp      →  20sp scaled by Width
  _14sem      →  14sp (Smallest Width) ignoring font scale
```

Range: **1 to 600** for all qualifiers.

---

## 🏆 Why AppDimens is More Complete

| Feature | AppDimens SSPS | intuit-ssp |
|---------|----------------|------------|
| SSP (Smallest Width) | ✅ | ✅ |
| HSP (Height) | ✅ | ❌ |
| WSP (Width) | ✅ | ❌ |
| SEM (Ignore font scale) | ✅ | ❌ |
| Compose extensions | ✅ `.ssp`, `.hsp`, `.wsp` | ❌ |
| Code-level API | ✅ `DimenSsp` object | ❌ |
| Conditional builder | ✅ `DimenSspScaled` | ❌ |
| Folding support | ✅ Fold/Flip states | ❌ |
| Range | 1 to 600 | 1 to 600 |

---

## 🚀 Advantages

1. **Zero Configuration** — Works out of the box. No initialization.
2. **Typography Precision** — Triple axis scaling (SSP+HSP+WSP) for perfect layouts.
3. **Hybrid Integration** — Works identically in XML, Compose, and Kotlin/Java code.
4. **Device-Aware** — Built-in detection for TV, Car, Watch, and Foldables.
5. **Accessibility Friendly** — Choose between standard `.ssp` or restricted `.sem`.
6. **Zero Performance Impact** — Values resolved via Android's native resource system.

![Extra demonstration](IMAGES/image.png)

> [!IMPORTANT]
> **Recommended Distribution:** It is highly recommended to generate and distribute your application as an **AAB (Android App Bundle)**. This allows the Android system to deliver only the resource qualifiers (like `values-sw600dp`) that match the specific device downloading the app, significantly reducing the final APK size.

---

*Created with the best traditions of responsive and accessible typography for the Android ecosystem.*
