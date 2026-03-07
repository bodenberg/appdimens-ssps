# AppDimens SSP, HSP, WSP

![AppDimens Banner](IMAGES/banner_top.png)

Welcome to the official documentation for the **AppDimens SSPS** library.

## 📖 What is this library?

**AppDimens SSP, HSP, WSP** is a modern dimension management system exclusively for typography and fonts (`Sp`) on Android. It expands the classic SSP (Scaled Size Pixels) standard by introducing scaling by Height (HSP) and Width (WSP). The library automates the process of adjusting text sizes (`TextUnit`), ensuring that typography remains perfectly scaled and legible on any device format in a mathematically precise way.

## ⚙️ What does it do?

It provides thousands of pre-calculated `@dimen` resources (from `1` to `600`) ready to use, saving the developer the trouble of calculating font sizes for each Android screen variant.

* **SSP (Smallest Width SP):** Scales the font based on the device's smallest width available. Perfect for maintaining text proportions in most scenarios (e.g., `@dimen/_16ssp` or `16.ssp`).
* **WSP (Width SP):** Scales text specifically based on the device's exact horizontal width in the current orientation (e.g., `@dimen/_16wsp` or `16.wsp`).
* **HSP (Height SP):** Scales text specifically based on the device's exact vertical height (e.g., `@dimen/_16hsp` or `16.hsp`).
* **SEM, WEM, HEM (Ignore Font Scale):** The `.sem`, `.wem`, `.hem` variants work the same way as the standard SSP/WSP/HSP resources but **do not follow the system's accessibility font scale settings**. They are useful for texts that shouldn't break strict component designs regardless of user accessibility preferences.
* **Dynamic Conditionals (Compose):** Facilitates adapting the font based on the device type (Car, TV, Watch) through the `.scaledSp()` instruction.

<br/>
<p align="center">
  <img src="IMAGES/screenshot.png" alt="Layout example" width="25%" />
</p>
<br/>

## 🚀 Advantages

1. **Accelerated Development:** Eliminates the need to create massive manual `dimens.xml` files for various screen categories (like `values-sw320dp`, `values-sw600dp`). Everything comes unified.
2. **Direct Hybrid Integration:** Works incredibly well both in traditional **XML** (`View System`) through predefined dimensions, and in the modern era of **Jetpack Compose**.
3. **Flexible Scaling:** Allows customizing typography by controlling whether Android's user accessibility scales should affect certain texts or not, through `.ssp` (which respects it) vs `.sem` (which ignores user scaling).
4. **Precision for TV, Wear OS, and Auto:** Handles advanced font rules without complexity using `UiModeType` combined with qualifiers.

## ⚡ Performance

The implementation ensures zero or virtually zero impact on performance:
* **In XML:** All tags like `@dimen/_16ssp` are processed statically at build time and resolved natively and parallel to the Android Framework resources.
* **In Compose:** Access to `.ssp`, `.hsp`, and `.wsp` uses optimized functions that extract dimensions via native context caching (`LocalConfiguration`, `LocalDensity`, and injected IDs). Avoiding unnecessary processing, it respects conventional UI steps without forcing useless recompositions.

## 🛠️ Support and Installation

The library has broad support in the Android ecosystem and is constantly updated for the most recently launched paradigms.

* **Min SDK:** 24
* **Compile SDK:** 36
* **Languages:** Kotlin and Java.
* **Paradigm:** XML and Jetpack Compose.

To install, simply add it to your `build.gradle` (dependency):

```kotlin
dependencies {
    implementation("io.github.bodenberg:appdimens-ssps:3.0.0")
}
```

### Quick Example in Compose:
```kotlin
Text(
    text = "Responsive Sizing",
    fontSize = 24.ssp, // Scales font based on Smallest Width and respects system font scale
    lineHeight = 28.ssp
)

Text(
    text = "Restricted Text Size",
    fontSize = 16.sem // Scales based on Smallest Width, but is NOT affected by vision accessibility preference
)
```

### Advanced Conditional Example:
```kotlin
val dynamicFontSize = 16.sp.scaledSp()
    .screen(UiModeType.TELEVISION, customValue = 32.ssp)
    .ssp // Result: 32.ssp on TV, 16.ssp on other mobile devices
```

![Extra demonstration](IMAGES/image.png)

---
*Created with the best practices for responsive and accessible layouts for the Android ecosystem.*
