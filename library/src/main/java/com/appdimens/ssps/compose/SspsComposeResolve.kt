/**
 * Shared Compose resolve helpers for SSPS — parallel to appdimens-sdps `SdpsComposeResolve`,
 * but resolves `_Nssp` / `_Nhsp` / `_Nwsp` (typography) instead of SDP layout buckets.
 *
 * - DEFAULT inverter + no aspect-ratio: no [androidx.compose.ui.platform.LocalConfiguration] read
 *   ( [androidx.compose.ui.res.dimensionResource] already tracks resource configuration ).
 * - Inverters: subscribe only to `LocalConfiguration.orientation`.
 * - Aspect-ratio: subscribe only to sw/w/h/dpi fields used by [com.appdimens.ssps.core.AppDimensSspsFactors].
 */
package com.appdimens.ssps.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.Inverter
import com.appdimens.ssps.common.effectiveDpQualifier
import com.appdimens.ssps.core.AppDimensSspsFactors
import com.appdimens.ssps.core.DimenResourceIdCache
import kotlin.math.abs

@Composable
internal fun rememberEffectiveQualifier(
    qualifier: DpQualifier,
    inverter: Inverter,
): DpQualifier {
    if (inverter == Inverter.DEFAULT) return qualifier
    val orientation = LocalConfiguration.current.orientation
    return remember(orientation, qualifier, inverter) {
        effectiveDpQualifier(orientation, qualifier, inverter)
    }
}

@Composable
internal fun rememberSspResourceId(
    actualQualifier: DpQualifier,
    value: Int,
): Int {
    val context = LocalContext.current
    return remember(actualQualifier, value, context.packageName) {
        resolveSspResourceId(context, actualQualifier, value)
    }
}

/** Builds `_Nssp` / `_Nhsp` / `_Nwsp` and resolves via [DimenResourceIdCache]. */
internal fun resolveSspResourceId(
    context: Context,
    actualQualifier: DpQualifier,
    value: Int,
): Int {
    if (value == 0) return 0
    val axis = when (actualQualifier) {
        DpQualifier.HEIGHT -> "hsp"
        DpQualifier.WIDTH -> "wsp"
        DpQualifier.SMALL_WIDTH -> "ssp"
    }
    val dimenName = "_${abs(value)}$axis"
    return DimenResourceIdCache.getOrResolve(
        context.resources,
        context.packageName,
        dimenName,
    )
}

/**
 * Reads only AR-relevant configuration fields so remember keys ignore locale/uiMode noise
 * in the remembered adjustment (still requires a LocalConfiguration read to invalidate on size/dpi).
 */
@Composable
internal fun rememberAspectRatioAdjustment(actualQualifier: DpQualifier): Float {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val sw = configuration.smallestScreenWidthDp
    val widthDp = configuration.screenWidthDp
    val heightDp = configuration.screenHeightDp
    val densityDpi = configuration.densityDpi
    return remember(sw, widthDp, heightDp, densityDpi, actualQualifier, context) {
        AppDimensSspsFactors.ensureUpToDate(context)
        AppDimensSspsFactors.adjustmentForQualifier(actualQualifier)
    }
}
