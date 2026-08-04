/**
 * Author & Developer: Jean Bodenberg
 * GIT: https://github.com/bodenberg/appdimens-ssps.git
 *
 * Compose remember stamps for SSPS — avoids unnecessary Sp recomputation when only
 * unrelated Configuration fields change (locale, keyboard, …).
 * Aligned with appdimens-dynamic ComposeRememberStamps (non-modularization fixes).
 */
package com.appdimens.ssps.compose

import android.content.res.Configuration
import androidx.compose.ui.unit.Density

/**
 * EN Mix [densityDpi] into a layout-only packed long without overlapping SW/W/H bit fields.
 * A plain `or (dpi shl 4)` collided with the low bits of height and could produce false hits.
 *
 * PT Mistura densityDpi sem sobrepor os campos de SW/W/H.
 */
private fun mixDpi(packedLayout: Long, densityDpi: Int): Long {
    val dpi = densityDpi.toLong() and 0xFFFFL
    return packedLayout xor (dpi * 0x0001000100010001L)
}

/**
 * EN Packs orientation + SW + W + H into non-overlapping bit fields (4+20+20+20 = 64).
 * PT Empacota orientação + SW + W + H em campos sem sobreposição.
 */
private fun packLayoutFields(configuration: Configuration): Long {
    val sw = configuration.smallestScreenWidthDp.toLong() and 0xFFFFFL
    val w = configuration.screenWidthDp.toLong() and 0xFFFFFL
    val h = configuration.screenHeightDp.toLong() and 0xFFFFFL
    val o = configuration.orientation.toLong() and 0xFL
    return (o shl 60) or (sw shl 40) or (w shl 20) or h
}

/**
 * EN Layout stamp for [remember] keys — orientation, SW, W, H, densityDpi only.
 * Deliberately **excludes** [Configuration.hashCode] so locale / keyboard changes
 * do not force every `.ssp` to recompute.
 *
 * PT Carimbo de layout — sem hashCode completo.
 */
internal fun layoutRememberStamp(configuration: Configuration): Long =
    mixDpi(packLayoutFields(configuration), configuration.densityDpi)

/**
 * EN Stamp for Sp [remember] paths: layout xor density xor fontScale.
 * PT Carimbo Sp: inclui fontScale.
 */
internal fun spRememberStamp(layoutStamp: Long, density: Density): Long {
    val d = density.density.toRawBits().toLong() and 0xFFFFFFFFL
    val f = density.fontScale.toRawBits().toLong() and 0xFFFFFFFFL
    return layoutStamp xor (d shl 32) xor f
}

/**
 * EN Stamp for custom scaled-entry resolution — matcher inputs only
 * (SW/W/H/orientation + uiMode). Omits densityDpi (AR derived from W/H).
 *
 * PT Carimbo para resolução de entradas customizadas — só inputs do matcher.
 */
internal fun scaledEntryRememberStamp(
    uiModeOrdinal: Int,
    configuration: Configuration,
): Long {
    val packed = packLayoutFields(configuration)
    return packed xor uiModeOrdinal.toLong()
}
