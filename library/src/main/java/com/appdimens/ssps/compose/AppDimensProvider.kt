/**
 * Optional CompositionLocal provider for [UiModeType].
 * Wrap the UI root to reuse fold / WindowLayoutInfo resolution across facilitators.
 */
package com.appdimens.ssps.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.appdimens.ssps.common.UiModeType

/** CompositionLocal for the current [UiModeType]. */
val LocalUiModeType = compositionLocalOf { UiModeType.UNDEFINED }

/** Provides [UiModeType] (including foldables) to the composition subtree. */
@Composable
fun AppDimensProvider(content: @Composable () -> Unit) {
    val uiModeType = rememberCurrentUiModeType()
    CompositionLocalProvider(LocalUiModeType provides uiModeType) {
        content()
    }
}

/** Returns [LocalUiModeType] when set; otherwise resolves via [rememberCurrentUiModeType]. */
@Composable
internal fun resolveCurrentUiModeType(): UiModeType {
    val provided = LocalUiModeType.current
    if (provided != UiModeType.UNDEFINED) return provided
    return rememberCurrentUiModeType()
}
