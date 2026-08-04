/**
 * Aspect-ratio adjustment factors for XML-pre-scaled SSP resources.
 *
 * Infers the active resource bucket from `_1ssp` / `_1wsp` / `_1hsp` and recomputes when
 * `(smallestScreenWidthDp, screenWidthDp, screenHeightDp, densityDpi)` changes.
 */
package com.appdimens.ssps.core

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.VisibleForTesting
import com.appdimens.ssps.common.DpQualifier
import java.util.Objects
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min

object AppDimensSspsFactors {
    internal const val REFERENCE_ASPECT_RATIO = 1.78f
    internal const val DESIGN_BASE_DP = 300f
    internal const val ADJUSTMENT_SCALE = 0.10f / 30f
    internal const val SENSITIVITY_DEFAULT = 0.08f / 30f

    private val updateLock = Any()

    @JvmField @Volatile var arAdjustmentSw: Float = 1f
    @JvmField @Volatile var arAdjustmentW: Float = 1f
    @JvmField @Volatile var arAdjustmentH: Float = 1f

    @Volatile private var lastConfigSignature: Int = Int.MIN_VALUE

    fun adjustmentForQualifier(qualifier: DpQualifier): Float =
        when (qualifier) {
            DpQualifier.SMALL_WIDTH -> arAdjustmentSw
            DpQualifier.WIDTH -> arAdjustmentW
            DpQualifier.HEIGHT -> arAdjustmentH
        }

    fun ensureUpToDate(context: Context) {
        val cfg = context.resources.configuration
        val sig = computeSignature(cfg)
        if (sig == lastConfigSignature) return
        synchronized(updateLock) {
            val innerCfg = context.resources.configuration
            val innerSig = computeSignature(innerCfg)
            if (innerSig == lastConfigSignature) return
            rebuild(context, innerCfg)
            lastConfigSignature = innerSig
        }
    }

    fun warmup(context: Context) {
        ensureUpToDate(context)
    }

    @VisibleForTesting
    internal fun resetAdjustmentCacheForTestsOnly() {
        synchronized(updateLock) {
            lastConfigSignature = Int.MIN_VALUE
            arAdjustmentSw = 1f
            arAdjustmentW = 1f
            arAdjustmentH = 1f
        }
    }

    private fun computeSignature(c: Configuration): Int =
        Objects.hash(
            c.smallestScreenWidthDp,
            c.screenWidthDp,
            c.screenHeightDp,
            c.densityDpi,
        )

    private fun rebuild(context: Context, config: Configuration) {
        val wDp = config.screenWidthDp.toFloat().coerceAtLeast(0f)
        val hDp = config.screenHeightDp.toFloat().coerceAtLeast(0f)
        val minDim = min(wDp, hDp)
        val maxDim = max(wDp, hDp)
        val rawAr = if (minDim > 0f) maxDim / minDim else 1f
        val normalizedAr = rawAr / REFERENCE_ASPECT_RATIO
        val logNormalizedAr = ln(normalizedAr.toDouble()).toFloat()

        val pkg = context.packageName
        val density = context.resources.displayMetrics.density

        arAdjustmentSw = computeAxisAdjustment(
            context.resources,
            config.smallestScreenWidthDp.toFloat(),
            density,
            "_1ssp",
            pkg,
            logNormalizedAr,
        )
        arAdjustmentW = computeAxisAdjustment(
            context.resources,
            config.screenWidthDp.toFloat(),
            density,
            "_1wsp",
            pkg,
            logNormalizedAr,
        )
        arAdjustmentH = computeAxisAdjustment(
            context.resources,
            config.screenHeightDp.toFloat(),
            density,
            "_1hsp",
            pkg,
            logNormalizedAr,
        )
    }

    private fun computeAxisAdjustment(
        res: android.content.res.Resources,
        dimDp: Float,
        density: Float,
        dimenOneName: String,
        pkg: String,
        logNormalizedAr: Float,
    ): Float {
        if (density <= 0f) return 1f
        val id = DimenResourceIdCache.getOrResolve(res, pkg, dimenOneName)
        if (id == 0) return 1f
        val oneUnitPx = kotlin.runCatching { res.getDimension(id) }.getOrElse { return 1f }
        val bucketDp = oneUnitPx / density * DESIGN_BASE_DP
        if (bucketDp <= 0f) return 1f
        val arMultiplier = 1f + (dimDp - DESIGN_BASE_DP) * (ADJUSTMENT_SCALE + SENSITIVITY_DEFAULT * logNormalizedAr)
        return arMultiplier * DESIGN_BASE_DP / bucketDp
    }
}
