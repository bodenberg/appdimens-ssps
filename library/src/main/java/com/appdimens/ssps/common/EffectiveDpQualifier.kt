/**
 * Maps (orientation / Configuration, qualifier, inverter) to the effective SSP / hsp / wsp resource axis.
 *
 * Mirrors the inverter branching in [com.appdimens.ssps.code.DimenSsp.getResourceId].
 * Parallel to appdimens-sdps [effectiveDpQualifier], scoped to SSPS typography resources.
 */
package com.appdimens.ssps.common

import android.content.res.Configuration

internal fun effectiveDpQualifier(
    configuration: Configuration,
    dpQualifier: DpQualifier,
    inverter: Inverter,
): DpQualifier = effectiveDpQualifier(configuration.orientation, dpQualifier, inverter)

/**
 * Orientation-only overload so Compose can subscribe to
 * [androidx.compose.ui.platform.LocalConfiguration]`.orientation` without depending on
 * unrelated configuration fields in remember keys.
 */
internal fun effectiveDpQualifier(
    orientation: Int,
    dpQualifier: DpQualifier,
    inverter: Inverter,
): DpQualifier {
    if (inverter == Inverter.DEFAULT) return dpQualifier

    val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
    val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT
    var actual = dpQualifier
    when (inverter) {
        Inverter.PH_TO_LW ->
            if (isLandscape && dpQualifier == DpQualifier.HEIGHT) actual = DpQualifier.WIDTH
        Inverter.PW_TO_LH ->
            if (isLandscape && dpQualifier == DpQualifier.WIDTH) actual = DpQualifier.HEIGHT
        Inverter.LH_TO_PW ->
            if (isPortrait && dpQualifier == DpQualifier.HEIGHT) actual = DpQualifier.WIDTH
        Inverter.LW_TO_PH ->
            if (isPortrait && dpQualifier == DpQualifier.WIDTH) actual = DpQualifier.HEIGHT
        Inverter.SW_TO_LH ->
            if (isLandscape && dpQualifier == DpQualifier.SMALL_WIDTH) actual = DpQualifier.HEIGHT
        Inverter.SW_TO_LW ->
            if (isLandscape && dpQualifier == DpQualifier.SMALL_WIDTH) actual = DpQualifier.WIDTH
        Inverter.SW_TO_PH ->
            if (isPortrait && dpQualifier == DpQualifier.SMALL_WIDTH) actual = DpQualifier.HEIGHT
        Inverter.SW_TO_PW ->
            if (isPortrait && dpQualifier == DpQualifier.SMALL_WIDTH) actual = DpQualifier.WIDTH
        Inverter.DEFAULT -> Unit
    }
    return actual
}
