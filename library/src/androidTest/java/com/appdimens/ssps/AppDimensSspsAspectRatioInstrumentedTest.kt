package com.appdimens.ssps

import android.content.Context
import android.content.res.Configuration
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.appdimens.ssps.code.DimenSsp
import com.appdimens.ssps.core.AppDimensSspsFactors
import com.appdimens.ssps.core.DimenResourceIdCache
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.abs

@RunWith(AndroidJUnit4::class)
class AppDimensSspsAspectRatioInstrumentedTest {

    private val epsilonSpPx = 0.06f

    @Before
    fun resetFactorsCache() {
        AppDimensSspsFactors.resetAdjustmentCacheForTestsOnly()
        DimenResourceIdCache.resetForTestsOnly()
    }

    private fun overlayContext(
        smallestWidthDp: Int,
        screenWidthDp: Int,
        screenHeightDp: Int,
        densityDpiOverride: Int? = null,
    ): Context {
        val base = InstrumentationRegistry.getInstrumentation().targetContext
        val cfg = Configuration(base.resources.configuration)
        cfg.smallestScreenWidthDp = smallestWidthDp
        cfg.screenWidthDp = screenWidthDp
        cfg.screenHeightDp = screenHeightDp
        densityDpiOverride?.let { cfg.densityDpi = it }
        return base.createConfigurationContext(cfg)
    }

    @Test
    fun ensureUpToDate_doesNotThrow_whenOneDimenResourcesExist() {
        val ctx = overlayContext(smallestWidthDp = 411, screenWidthDp = 411, screenHeightDp = 890)
        AppDimensSspsFactors.ensureUpToDate(ctx)
    }

    @Test
    fun sameConfigurationSignature_preservesVolatileAdjustments() {
        val ctx = overlayContext(420, 420, 915, 460)
        AppDimensSspsFactors.ensureUpToDate(ctx)
        val sw1 = AppDimensSspsFactors.arAdjustmentSw
        val w1 = AppDimensSspsFactors.arAdjustmentW
        AppDimensSspsFactors.ensureUpToDate(ctx)
        assertEquals(sw1, AppDimensSspsFactors.arAdjustmentSw, epsilonSpPx)
        assertEquals(w1, AppDimensSspsFactors.arAdjustmentW, epsilonSpPx)
    }

    @Test
    fun swap_screenWidthDp_and_screenHeightDp_swapsWandH_AdjustmentsLeavesSwTracked() {
        val dpi = InstrumentationRegistry.getInstrumentation().targetContext.resources.configuration.densityDpi
        val ctxA = overlayContext(411, 411, 900, dpi)
        AppDimensSspsFactors.ensureUpToDate(ctxA)
        val swA = AppDimensSspsFactors.arAdjustmentSw
        val wA = AppDimensSspsFactors.arAdjustmentW
        val hA = AppDimensSspsFactors.arAdjustmentH

        AppDimensSspsFactors.resetAdjustmentCacheForTestsOnly()

        val ctxB = overlayContext(411, 900, 411, dpi)
        AppDimensSspsFactors.ensureUpToDate(ctxB)
        assertEquals(swA, AppDimensSspsFactors.arAdjustmentSw, epsilonSpPx)
        assertEquals(wA, AppDimensSspsFactors.arAdjustmentH, 0.25f)
        assertEquals(hA, AppDimensSspsFactors.arAdjustmentW, 0.25f)
    }

    @Test
    fun referencePortrait300_ratio178_ssp_near_sspa() {
        val dpi = InstrumentationRegistry.getInstrumentation().targetContext.resources.configuration.densityDpi
        val ctx = overlayContext(smallestWidthDp = 300, screenWidthDp = 300, screenHeightDp = 534, dpi)
        AppDimensSspsFactors.ensureUpToDate(ctx)
        assertTrue(abs(AppDimensSspsFactors.arAdjustmentSw - 1f) < 0.035f)

        val pxBase = DimenSsp.ssp(ctx, 16)
        val pxAr = DimenSsp.sspa(ctx, 16)
        assertEquals(pxBase, pxAr, pxBase.coerceAtLeast(1f) * 0.02f + epsilonSpPx)
    }

    @Test
    fun aspectRatioProducesDifferentScaling_whenAwayFromUnity() {
        val dpi = InstrumentationRegistry.getInstrumentation().targetContext.resources.configuration.densityDpi
        val ctx = overlayContext(480, 480, 960, dpi)
        AppDimensSspsFactors.ensureUpToDate(ctx)
        val pxBase = DimenSsp.ssp(ctx, 32)
        val pxAr = DimenSsp.sspa(ctx, 32)
        assertTrue(kotlin.math.abs(pxBase - pxAr) > 0.5f)
    }

    @Test
    fun sspa_appliesSameAxisAdjustmentAsExpected() {
        val dpi = InstrumentationRegistry.getInstrumentation().targetContext.resources.configuration.densityDpi
        val ctx = overlayContext(480, 480, 960, dpi)
        AppDimensSspsFactors.ensureUpToDate(ctx)
        val sspBase = DimenSsp.ssp(ctx, 16)
        val sspAr = DimenSsp.sspa(ctx, 16)
        val expected = sspBase * AppDimensSspsFactors.arAdjustmentSw
        assertEquals(expected, sspAr, epsilonSpPx)
        assertTrue(kotlin.math.abs(sspBase - sspAr) > 0.1f)
    }

    @Test
    fun resourceIdCache_reusesIdentifierAcrossRepeatedSspLookups() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        DimenResourceIdCache.resetForTestsOnly()
        assertEquals(0, DimenResourceIdCache.cachedSizeForTestsOnly())

        val first = DimenSsp.ssp(ctx, 16)
        val sizeAfterFirst = DimenResourceIdCache.cachedSizeForTestsOnly()
        assertTrue(sizeAfterFirst >= 1)

        val second = DimenSsp.ssp(ctx, 16)
        assertEquals(first, second, epsilonSpPx)
        assertEquals(sizeAfterFirst, DimenResourceIdCache.cachedSizeForTestsOnly())

        // Same resource name - cache size must not grow.
        DimenSsp.sspa(ctx, 16)
        assertEquals(sizeAfterFirst, DimenResourceIdCache.cachedSizeForTestsOnly())
    }
}
