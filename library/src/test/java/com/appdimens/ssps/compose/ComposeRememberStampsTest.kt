package com.appdimens.ssps.compose

import android.content.res.Configuration
import androidx.compose.ui.unit.Density
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/** Unit tests for Compose remember stamp packing. */
class ComposeRememberStampsTest {

    private fun config(
        sw: Int,
        w: Int,
        h: Int,
        dpi: Int,
        orientation: Int = Configuration.ORIENTATION_PORTRAIT,
    ): Configuration {
        val c = Configuration()
        c.smallestScreenWidthDp = sw
        c.screenWidthDp = w
        c.screenHeightDp = h
        c.densityDpi = dpi
        c.orientation = orientation
        return c
    }

    @Test
    fun layoutStamp_stableWhenOnlyNonLayoutFieldsChange() {
        val a = config(411, 411, 890, 420)
        val stamp1 = layoutRememberStamp(a)
        // Mutate a field that must NOT affect layout stamp packing (SW/W/H/orientation/dpi).
        a.keyboardHidden = Configuration.KEYBOARDHIDDEN_YES
        a.fontScale = 1.5f
        val stamp2 = layoutRememberStamp(a)
        assertEquals(stamp1, stamp2)
    }

    @Test
    fun layoutStamp_changesWhenHeightChanges() {
        val a = layoutRememberStamp(config(411, 411, 890, 420))
        val b = layoutRememberStamp(config(411, 411, 900, 420))
        assertNotEquals(a, b)
    }

    @Test
    fun layoutStamp_noFalseCollision_betweenHeightLowBits_andDpi() {
        // Regression: old `or (dpi shl 4)` overlapped height low bits.
        // Two configs where height low bits ≈ dpi shift must not collide.
        val stampA = layoutRememberStamp(config(400, 400, 0x10, 0x10))
        val stampB = layoutRememberStamp(config(400, 400, 0x00, 0x01))
        // Different layout fields → different stamps (mixDpi must not make them equal by chance for this pair)
        assertNotEquals(stampA, stampB)

        // Same layout geometry, different dpi → different stamps
        val s1 = layoutRememberStamp(config(411, 411, 890, 320))
        val s2 = layoutRememberStamp(config(411, 411, 890, 480))
        assertNotEquals(s1, s2)
    }

    @Test
    fun spRememberStamp_includesFontScale() {
        val layout = layoutRememberStamp(config(411, 411, 890, 420))
        val d1 = Density(density = 2.75f, fontScale = 1.0f)
        val d2 = Density(density = 2.75f, fontScale = 1.3f)
        assertNotEquals(spRememberStamp(layout, d1), spRememberStamp(layout, d2))
    }

    @Test
    fun spRememberStamp_includesDensity() {
        val layout = layoutRememberStamp(config(411, 411, 890, 420))
        val d1 = Density(density = 2.0f, fontScale = 1.0f)
        val d2 = Density(density = 3.0f, fontScale = 1.0f)
        assertNotEquals(spRememberStamp(layout, d1), spRememberStamp(layout, d2))
    }

    @Test
    fun scaledEntryStamp_ignoresDpi() {
        val a = scaledEntryRememberStamp(0, config(411, 411, 890, 320))
        val b = scaledEntryRememberStamp(0, config(411, 411, 890, 480))
        assertEquals(a, b)
    }

    @Test
    fun scaledEntryStamp_changesWithUiModeOrdinal() {
        val cfg = config(411, 411, 890, 420)
        assertNotEquals(
            scaledEntryRememberStamp(0, cfg),
            scaledEntryRememberStamp(1, cfg),
        )
    }
}
