/**
 * Author & Developer: Jean Bodenberg
 * GIT: https://github.com/bodenberg/appdimens-ssps.git
 *
 * Compact remember keys for custom scaled-entry resolution in Compose.
 */
package com.appdimens.ssps.compose

import android.content.res.Configuration
import androidx.compose.ui.unit.Density

/** Mixes [densityDpi] into a packed layout long without overlapping SW/W/H bit fields. */
private fun mixDpi(packedLayout: Long, densityDpi: Int): Long {
    val dpi = densityDpi.toLong() and 0xFFFFL
    return packedLayout xor (dpi * 0x0001000100010001L)
}

/** Packs orientation + SW + W + H into non-overlapping bit fields (4+20+20+20). */
private fun packLayoutFields(configuration: Configuration): Long {
    val sw = configuration.smallestScreenWidthDp.toLong() and 0xFFFFFL
    val w = configuration.screenWidthDp.toLong() and 0xFFFFFL
    val h = configuration.screenHeightDp.toLong() and 0xFFFFFL
    val o = configuration.orientation.toLong() and 0xFL
    return (o shl 60) or (sw shl 40) or (w shl 20) or h
}

/** Layout stamp: orientation, SW, W, H, and densityDpi. */
internal fun layoutRememberStamp(configuration: Configuration): Long =
    mixDpi(packLayoutFields(configuration), configuration.densityDpi)

/** Sp stamp: layout fields xor physical density xor font scale. */
internal fun spRememberStamp(layoutStamp: Long, density: Density): Long {
    val d = density.density.toRawBits().toLong() and 0xFFFFFFFFL
    val f = density.fontScale.toRawBits().toLong() and 0xFFFFFFFFL
    return layoutStamp xor (d shl 32) xor f
}

/**
 * Stamp for custom scaled-entry matching: SW/W/H/orientation + uiMode ordinal.
 * Omits densityDpi (aspect ratio is derived from width/height).
 */
internal fun scaledEntryRememberStamp(
    uiModeOrdinal: Int,
    configuration: Configuration,
): Long {
    val packed = packLayoutFields(configuration)
    return packed xor uiModeOrdinal.toLong()
}
