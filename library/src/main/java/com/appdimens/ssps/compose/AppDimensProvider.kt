/**
 * Author & Developer: Jean Bodenberg
 * GIT: https://github.com/bodenberg/appdimens-ssps.git
 *
 * Optional CompositionLocal provider for UiModeType — avoids re-resolving fold /
 * WindowLayoutInfo on every `*Mode` / `*Screen` call when wrapped at the root.
 */
package com.appdimens.ssps.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.appdimens.ssps.common.UiModeType

/**
 * EN CompositionLocal for the current UiModeType.
 * PT CompositionLocal para o UiModeType atual.
 */
val LocalUiModeType = compositionLocalOf { UiModeType.UNDEFINED }

/**
 * EN Provider that computes and provides [UiModeType] (including foldables) to children.
 * Recommended for performance when using many `*Mode` / `*Screen` facilitators.
 *
 * PT Provedor que computa e fornece o [UiModeType] (incl. dobráveis) aos filhos.
 */
@Composable
fun AppDimensProvider(content: @Composable () -> Unit) {
    val uiModeType = rememberCurrentUiModeType()
    CompositionLocalProvider(LocalUiModeType provides uiModeType) {
        content()
    }
}

/**
 * EN Uses [LocalUiModeType] when provided; otherwise resolves via [rememberCurrentUiModeType].
 * PT Usa [LocalUiModeType] se fornecido; caso contrário resolve via [rememberCurrentUiModeType].
 */
@Composable
internal fun resolveCurrentUiModeType(): UiModeType {
    val provided = LocalUiModeType.current
    if (provided != UiModeType.UNDEFINED) return provided
    return rememberCurrentUiModeType()
}
