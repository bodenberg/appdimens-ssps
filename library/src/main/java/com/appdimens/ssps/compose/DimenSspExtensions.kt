/**
 * Author & Developer: Jean Bodenberg
 * GIT: https://github.com/bodenberg/appdimens-ssps.git
 * Date: 2025-10-04
 *
 * Library: AppDimens
 *
 * Description:
 * The AppDimens library is a dimension management system that automatically
 * adjusts Dp, Sp, and Px values in a responsive and mathematically refined way,
 * ensuring layout consistency across any screen size or ratio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appdimens.ssps.compose

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.Orientation
import com.appdimens.ssps.common.UiModeType

// EN Rotation facilitator extensions for Sp.
// PT Extensões facilitadoras para rotação (Sp).

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Uses the base value by default, but when the device is in the specified [orientation],
 * it uses [rotationValue] scaled with the given [finalQualifierResolver].
 * Usage example: `30.sspRotate(45, DpQualifier.SMALL_WIDTH, Orientation.LANDSCAPE)`
 * → 30.ssp by default, 45 scaled by SMALL_WIDTH in landscape.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Usa o valor base por padrão, mas quando o dispositivo está na [orientation] especificada,
 * usa [rotationValue] escalado com o [finalQualifierResolver] dado.
 * Exemplo de uso: `30.sspRotate(45, DpQualifier.SMALL_WIDTH, Orientation.LANDSCAPE)`
 * → 30.ssp por padrão, 45 escalado por SMALL_WIDTH no paisagem.
 */
@Composable
fun Int.sspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.SMALL_WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val isTargetOrientation = when (orientation) {
        Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        else -> false
    }
    return if (isTargetOrientation) {
        rotationValue.toDynamicScaledSp(finalQualifierResolver, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of sspRotate (Int).
 *
 * PT
 * Versão em pixel de sspRotate (Int).
 */
@Composable
fun Int.sspRotatePx(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.SMALL_WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspRotate(rotationValue, finalQualifierResolver, orientation, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the device is in the specified [orientation], it uses [rotationValue]
 * scaled with the given [finalQualifierResolver].
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando o dispositivo está na [orientation] especificada, usa [rotationValue]
 * escalado com o [finalQualifierResolver] dado.
 */
@Composable
fun TextUnit.sspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.SMALL_WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val isTargetOrientation = when (orientation) {
        Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        else -> false
    }
    return if (isTargetOrientation) {
        rotationValue.toDynamicScaledSp(finalQualifierResolver, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver, fontScale)
    }
}

/**
 * EN
 * Pixel version of sspRotate (TextUnit).
 *
 * PT
 * Versão em pixel de sspRotate (TextUnit).
 */
@Composable
fun TextUnit.sspRotatePx(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.SMALL_WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspRotate(rotationValue, finalQualifierResolver, orientation, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the device is in the specified [orientation], it uses [rotationValue]
 * scaled with the given [finalQualifierResolver].
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando o dispositivo está na [orientation] especificada, usa [rotationValue]
 * escalado com o [finalQualifierResolver] dado.
 */
@Composable
fun TextUnit.sspRotatePlain(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.SMALL_WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val isTargetOrientation = when (orientation) {
        Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        else -> false
    }
    return if (isTargetOrientation) {
        rotationValue.toDynamicScaledSp(finalQualifierResolver, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of sspRotatePlain.
 *
 * PT
 * Versão em pixel de sspRotatePlain.
 */
@Composable
fun TextUnit.sspRotatePlainPx(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.SMALL_WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspRotatePlain(rotationValue, finalQualifierResolver, orientation, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Uses the base value by default, but when the device is in the specified [orientation],
 * it uses [rotationValue] scaled with the given [finalQualifierResolver].
 * Usage example: `30.hspRotate(45, DpQualifier.HEIGHT, Orientation.LANDSCAPE)`
 * → 30.hsp by default, 45 scaled by HEIGHT in landscape.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Usa o valor base por padrão, mas quando o dispositivo está na [orientation] especificada,
 * usa [rotationValue] escalado com o [finalQualifierResolver] dado.
 */
@Composable
fun Int.hspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val isTargetOrientation = when (orientation) {
        Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        else -> false
    }
    return if (isTargetOrientation) {
        rotationValue.toDynamicScaledSp(finalQualifierResolver, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.HEIGHT, fontScale)
    }
}

/**
 * EN
 * Pixel version of hspRotate (Int).
 *
 * PT
 * Versão em pixel de hspRotate (Int).
 */
@Composable
fun Int.hspRotatePx(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspRotate(rotationValue, finalQualifierResolver, orientation, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the device is in the specified [orientation], it uses [rotationValue]
 * scaled with the given [finalQualifierResolver].
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando o dispositivo está na [orientation] especificada, usa [rotationValue]
 * escalado com o [finalQualifierResolver] dado.
 */
@Composable
fun TextUnit.hspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val isTargetOrientation = when (orientation) {
        Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        else -> false
    }
    return if (isTargetOrientation) {
        rotationValue.toDynamicScaledSp(finalQualifierResolver, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver, fontScale)
    }
}

/**
 * EN
 * Pixel version of hspRotate (TextUnit).
 *
 * PT
 * Versão em pixel de hspRotate (TextUnit).
 */
@Composable
fun TextUnit.hspRotatePx(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspRotate(rotationValue, finalQualifierResolver, orientation, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the device is in the specified [orientation], it uses [rotationValue]
 * scaled with the given [finalQualifierResolver].
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando o dispositivo está na [orientation] especificada, usa [rotationValue]
 * escalado com o [finalQualifierResolver] dado.
 */
@Composable
fun TextUnit.hspRotatePlain(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val isTargetOrientation = when (orientation) {
        Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        else -> false
    }
    return if (isTargetOrientation) {
        rotationValue.toDynamicScaledSp(finalQualifierResolver, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of hspRotatePlain.
 *
 * PT
 * Versão em pixel de hspRotatePlain.
 */
@Composable
fun TextUnit.hspRotatePlainPx(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspRotatePlain(rotationValue, finalQualifierResolver, orientation, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Uses the base value by default, but when the device is in the specified [orientation],
 * it uses [rotationValue] scaled with the given [finalQualifierResolver].
 * Usage example: `30.wspRotate(45, DpQualifier.WIDTH, Orientation.LANDSCAPE)`
 * → 30.wsp by default, 45 scaled by WIDTH in landscape.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Usa o valor base por padrão, mas quando o dispositivo está na [orientation] especificada,
 * usa [rotationValue] escalado com o [finalQualifierResolver] dado.
 */
@Composable
fun Int.wspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val isTargetOrientation = when (orientation) {
        Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        else -> false
    }
    return if (isTargetOrientation) {
        rotationValue.toDynamicScaledSp(finalQualifierResolver, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of wspRotate (Int).
 *
 * PT
 * Versão em pixel de wspRotate (Int).
 */
@Composable
fun Int.wspRotatePx(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspRotate(rotationValue, finalQualifierResolver, orientation, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the device is in the specified [orientation], it uses [rotationValue]
 * scaled with the given [finalQualifierResolver].
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando o dispositivo está na [orientation] especificada, usa [rotationValue]
 * escalado com o [finalQualifierResolver] dado.
 */
@Composable
fun TextUnit.wspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val isTargetOrientation = when (orientation) {
        Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        else -> false
    }
    return if (isTargetOrientation) {
        rotationValue.toDynamicScaledSp(finalQualifierResolver, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver, fontScale)
    }
}

/**
 * EN
 * Pixel version of wspRotate (TextUnit).
 *
 * PT
 * Versão em pixel de wspRotate (TextUnit).
 */
@Composable
fun TextUnit.wspRotatePx(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspRotate(rotationValue, finalQualifierResolver, orientation, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the device is in the specified [orientation], it uses [rotationValue]
 * scaled with the given [finalQualifierResolver].
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando o dispositivo está na [orientation] especificada, usa [rotationValue]
 * escalado com o [finalQualifierResolver] dado.
 */
@Composable
fun TextUnit.wspRotatePlain(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val isTargetOrientation = when (orientation) {
        Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        else -> false
    }
    return if (isTargetOrientation) {
        rotationValue.toDynamicScaledSp(finalQualifierResolver, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of wspRotatePlain.
 *
 * PT
 * Versão em pixel de wspRotatePlain.
 */
@Composable
fun TextUnit.wspRotatePlainPx(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspRotatePlain(rotationValue, finalQualifierResolver, orientation, fontScale).toPx() }
}

// EN Helps extract the activity from context wrapper (Sp version)
// PT Ajuda a extrair a activity de um context wrapper (versão Sp)
private tailrec fun android.content.Context.findActivitySp(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivitySp()
    else -> null
}

// EN UiModeType facilitator extensions for Sp.
// PT Extensões facilitadoras para UiModeType (Sp).

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Uses the base value by default, but when the device matches the specified [uiModeType],
 * it uses [modeValue] instead.
 * Usage example: `30.sspMode(50, UiModeType.TELEVISION)`
 * → 30.ssp by default, 50.ssp on television.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Usa o valor base por padrão, mas quando o dispositivo corresponde ao [uiModeType] especificado,
 * usa [modeValue] no lugar.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Int.sspMode(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    return if (currentUiModeType == uiModeType) {
        modeValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of sspMode (Int).
 *
 * PT
 * Versão em pixel de sspMode (Int).
 */
@Composable
fun Int.sspModePx(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspMode(modeValue, uiModeType, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the device matches the specified [uiModeType], it uses [modeValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] especificado, usa [modeValue] no lugar.
 */
@Composable
fun TextUnit.sspMode(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    return if (currentUiModeType == uiModeType) {
        modeValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of sspMode (TextUnit).
 *
 * PT
 * Versão em pixel de sspMode (TextUnit).
 */
@Composable
fun TextUnit.sspModePx(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspMode(modeValue, uiModeType, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the device matches the specified [uiModeType], it uses [modeValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] especificado, usa [modeValue] no lugar.
 */
@Composable
fun TextUnit.sspModePlain(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    return if (currentUiModeType == uiModeType) {
        modeValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of sspModePlain.
 *
 * PT
 * Versão em pixel de sspModePlain.
 */
@Composable
fun TextUnit.sspModePlainPx(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspModePlain(modeValue, uiModeType, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Uses the base value by default, but when the device matches the specified [uiModeType],
 * it uses [modeValue] instead.
 * Usage example: `30.hspMode(50, UiModeType.TELEVISION)`
 * → 30.hsp by default, 50.hsp on television.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Usa o valor base por padrão, mas quando o dispositivo corresponde ao [uiModeType] especificado,
 * usa [modeValue] no lugar.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Int.hspMode(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    return if (currentUiModeType == uiModeType) {
        modeValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.HEIGHT, fontScale)
    }
}

/**
 * EN
 * Pixel version of hspMode (Int).
 *
 * PT
 * Versão em pixel de hspMode (Int).
 */
@Composable
fun Int.hspModePx(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspMode(modeValue, uiModeType, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the device matches the specified [uiModeType], it uses [modeValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] especificado, usa [modeValue] no lugar.
 */
@Composable
fun TextUnit.hspMode(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    return if (currentUiModeType == uiModeType) {
        modeValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    }
}

/**
 * EN
 * Pixel version of hspMode (TextUnit).
 *
 * PT
 * Versão em pixel de hspMode (TextUnit).
 */
@Composable
fun TextUnit.hspModePx(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspMode(modeValue, uiModeType, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the device matches the specified [uiModeType], it uses [modeValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] especificado, usa [modeValue] no lugar.
 */
@Composable
fun TextUnit.hspModePlain(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    return if (currentUiModeType == uiModeType) {
        modeValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of hspModePlain.
 *
 * PT
 * Versão em pixel de hspModePlain.
 */
@Composable
fun TextUnit.hspModePlainPx(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspModePlain(modeValue, uiModeType, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Uses the base value by default, but when the device matches the specified [uiModeType],
 * it uses [modeValue] instead.
 * Usage example: `30.wspMode(50, UiModeType.TELEVISION)`
 * → 30.wsp by default, 50.wsp on television.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Usa o valor base por padrão, mas quando o dispositivo corresponde ao [uiModeType] especificado,
 * usa [modeValue] no lugar.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Int.wspMode(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    return if (currentUiModeType == uiModeType) {
        modeValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of wspMode (Int).
 *
 * PT
 * Versão em pixel de wspMode (Int).
 */
@Composable
fun Int.wspModePx(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspMode(modeValue, uiModeType, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the device matches the specified [uiModeType], it uses [modeValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] especificado, usa [modeValue] no lugar.
 */
@Composable
fun TextUnit.wspMode(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    return if (currentUiModeType == uiModeType) {
        modeValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of wspMode (TextUnit).
 *
 * PT
 * Versão em pixel de wspMode (TextUnit).
 */
@Composable
fun TextUnit.wspModePx(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspMode(modeValue, uiModeType, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the device matches the specified [uiModeType], it uses [modeValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] especificado, usa [modeValue] no lugar.
 */
@Composable
fun TextUnit.wspModePlain(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    return if (currentUiModeType == uiModeType) {
        modeValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of wspModePlain.
 *
 * PT
 * Versão em pixel de wspModePlain.
 */
@Composable
fun TextUnit.wspModePlainPx(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspModePlain(modeValue, uiModeType, finalQualifierResolver, fontScale).toPx() }
}

// EN DpQualifier facilitator extensions for Sp.
// PT Extensões facilitadoras para DpQualifier (Sp).

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Uses the base value by default, but when the screen metric for [qualifierType]
 * is >= [qualifierValue], it uses [qualifiedValue] instead.
 * Usage example: `30.sspQualifier(50, DpQualifier.SMALL_WIDTH, 600)`
 * → 30.ssp by default, 50.ssp when smallestScreenWidthDp >= 600.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Usa o valor base por padrão, mas quando a métrica de tela para [qualifierType]
 * é >= [qualifierValue], usa [qualifiedValue] no lugar.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Int.sspQualifier(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of sspQualifier (Int).
 *
 * PT
 * Versão em pixel de sspQualifier (Int).
 */
@Composable
fun Int.sspQualifierPx(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspQualifier(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the screen metric for [qualifierType] is >= [qualifierValue], it uses [qualifiedValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando a métrica de tela para [qualifierType] é >= [qualifierValue], usa [qualifiedValue] no lugar.
 */
@Composable
fun TextUnit.sspQualifier(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of sspQualifier (TextUnit).
 *
 * PT
 * Versão em pixel de sspQualifier (TextUnit).
 */
@Composable
fun TextUnit.sspQualifierPx(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspQualifier(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the screen metric for [qualifierType] is >= [qualifierValue], it uses [qualifiedValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando a métrica de tela para [qualifierType] é >= [qualifierValue], usa [qualifiedValue] no lugar.
 */
@Composable
fun TextUnit.sspQualifierPlain(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of sspQualifierPlain.
 *
 * PT
 * Versão em pixel de sspQualifierPlain.
 */
@Composable
fun TextUnit.sspQualifierPlainPx(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspQualifierPlain(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Uses the base value by default, but when the screen metric for [qualifierType]
 * is >= [qualifierValue], it uses [qualifiedValue] instead.
 * Usage example: `30.hspQualifier(50, DpQualifier.HEIGHT, 800)`
 * → 30.hsp by default, 50.hsp when screenHeightDp >= 800.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Int.hspQualifier(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.HEIGHT, fontScale)
    }
}

/**
 * EN
 * Pixel version of hspQualifier (Int).
 *
 * PT
 * Versão em pixel de hspQualifier (Int).
 */
@Composable
fun Int.hspQualifierPx(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspQualifier(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the screen metric for [qualifierType] is >= [qualifierValue], it uses [qualifiedValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando a métrica de tela para [qualifierType] é >= [qualifierValue], usa [qualifiedValue] no lugar.
 */
@Composable
fun TextUnit.hspQualifier(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    }
}

/**
 * EN
 * Pixel version of hspQualifier (TextUnit).
 *
 * PT
 * Versão em pixel de hspQualifier (TextUnit).
 */
@Composable
fun TextUnit.hspQualifierPx(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspQualifier(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the screen metric for [qualifierType] is >= [qualifierValue], it uses [qualifiedValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando a métrica de tela para [qualifierType] é >= [qualifierValue], usa [qualifiedValue] no lugar.
 */
@Composable
fun TextUnit.hspQualifierPlain(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of hspQualifierPlain.
 *
 * PT
 * Versão em pixel de hspQualifierPlain.
 */
@Composable
fun TextUnit.hspQualifierPlainPx(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspQualifierPlain(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Uses the base value by default, but when the screen metric for [qualifierType]
 * is >= [qualifierValue], it uses [qualifiedValue] instead.
 * Usage example: `30.wspQualifier(50, DpQualifier.WIDTH, 600)`
 * → 30.wsp by default, 50.wsp when screenWidthDp >= 600.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Int.wspQualifier(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of wspQualifier (Int).
 *
 * PT
 * Versão em pixel de wspQualifier (Int).
 */
@Composable
fun Int.wspQualifierPx(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspQualifier(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the screen metric for [qualifierType] is >= [qualifierValue], it uses [qualifiedValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando a métrica de tela para [qualifierType] é >= [qualifierValue], usa [qualifiedValue] no lugar.
 */
@Composable
fun TextUnit.wspQualifier(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of wspQualifier (TextUnit).
 *
 * PT
 * Versão em pixel de wspQualifier (TextUnit).
 */
@Composable
fun TextUnit.wspQualifierPx(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspQualifier(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the screen metric for [qualifierType] is >= [qualifierValue], it uses [qualifiedValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando a métrica de tela para [qualifierType] é >= [qualifierValue], usa [qualifiedValue] no lugar.
 */
@Composable
fun TextUnit.wspQualifierPlain(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of wspQualifierPlain.
 *
 * PT
 * Versão em pixel de wspQualifierPlain.
 */
@Composable
fun TextUnit.wspQualifierPlainPx(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspQualifierPlain(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

// EN UiModeType + DpQualifier combined facilitator extensions for Sp.
// PT Extensões facilitadoras combinadas UiModeType + DpQualifier para Sp.

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Uses the base value by default, but when the device matches [uiModeType] AND
 * the screen metric for [qualifierType] is >= [qualifierValue], it uses [screenValue] instead.
 * Usage example: `30.sspScreen(50, UiModeType.TELEVISION, DpQualifier.SMALL_WIDTH, 600)`
 * → 30.ssp by default, 50.ssp on television with sw >= 600.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Usa o valor base por padrão, mas quando o dispositivo corresponde ao [uiModeType] E
 * a métrica de tela para [qualifierType] é >= [qualifierValue], usa [screenValue] no lugar.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Int.sspScreen(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    val uiModeMatch = currentUiModeType == uiModeType
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (uiModeMatch && qualifierMatch) {
        screenValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of sspScreen (Int).
 *
 * PT
 * Versão em pixel de sspScreen (Int).
 */
@Composable
fun Int.sspScreenPx(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspScreen(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the device matches [uiModeType] AND the screen metric for [qualifierType]
 * is >= [qualifierValue], it uses [screenValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] E a métrica de tela para
 * [qualifierType] é >= [qualifierValue], usa [screenValue] no lugar.
 */
@Composable
fun TextUnit.sspScreen(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    val uiModeMatch = currentUiModeType == uiModeType
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (uiModeMatch && qualifierMatch) {
        screenValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of sspScreen (TextUnit).
 *
 * PT
 * Versão em pixel de sspScreen (TextUnit).
 */
@Composable
fun TextUnit.sspScreenPx(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspScreen(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the device matches [uiModeType] AND the screen metric for [qualifierType]
 * is >= [qualifierValue], it uses [screenValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] E a métrica de tela para
 * [qualifierType] é >= [qualifierValue], usa [screenValue] no lugar.
 */
@Composable
fun TextUnit.sspScreenPlain(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    val uiModeMatch = currentUiModeType == uiModeType
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (uiModeMatch && qualifierMatch) {
        screenValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of sspScreenPlain.
 *
 * PT
 * Versão em pixel de sspScreenPlain.
 */
@Composable
fun TextUnit.sspScreenPlainPx(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { sspScreenPlain(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Uses the base value by default, but when the device matches [uiModeType] AND
 * the screen metric for [qualifierType] is >= [qualifierValue], it uses [screenValue] instead.
 * Usage example: `30.hspScreen(50, UiModeType.TELEVISION, DpQualifier.HEIGHT, 800)`
 * → 30.hsp by default, 50.hsp on television with height >= 800.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Int.hspScreen(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    val uiModeMatch = currentUiModeType == uiModeType
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (uiModeMatch && qualifierMatch) {
        screenValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.HEIGHT, fontScale)
    }
}

/**
 * EN
 * Pixel version of hspScreen (Int).
 *
 * PT
 * Versão em pixel de hspScreen (Int).
 */
@Composable
fun Int.hspScreenPx(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspScreen(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the device matches [uiModeType] AND the screen metric for [qualifierType]
 * is >= [qualifierValue], it uses [screenValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] E a métrica de tela para
 * [qualifierType] é >= [qualifierValue], usa [screenValue] no lugar.
 */
@Composable
fun TextUnit.hspScreen(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    val uiModeMatch = currentUiModeType == uiModeType
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (uiModeMatch && qualifierMatch) {
        screenValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    }
}

/**
 * EN
 * Pixel version of hspScreen (TextUnit).
 *
 * PT
 * Versão em pixel de hspScreen (TextUnit).
 */
@Composable
fun TextUnit.hspScreenPx(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspScreen(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the device matches [uiModeType] AND the screen metric for [qualifierType]
 * is >= [qualifierValue], it uses [screenValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] E a métrica de tela para
 * [qualifierType] é >= [qualifierValue], usa [screenValue] no lugar.
 */
@Composable
fun TextUnit.hspScreenPlain(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    val uiModeMatch = currentUiModeType == uiModeType
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (uiModeMatch && qualifierMatch) {
        screenValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of hspScreenPlain.
 *
 * PT
 * Versão em pixel de hspScreenPlain.
 */
@Composable
fun TextUnit.hspScreenPlainPx(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { hspScreenPlain(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Uses the base value by default, but when the device matches [uiModeType] AND
 * the screen metric for [qualifierType] is >= [qualifierValue], it uses [screenValue] instead.
 * Usage example: `30.wspScreen(50, UiModeType.TELEVISION, DpQualifier.WIDTH, 600)`
 * → 30.wsp by default, 50.wsp on television with width >= 600.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Int.wspScreen(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    val uiModeMatch = currentUiModeType == uiModeType
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (uiModeMatch && qualifierMatch) {
        screenValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    } else {
        this.toDynamicScaledSp(DpQualifier.WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of wspScreen (Int).
 *
 * PT
 * Versão em pixel de wspScreen (Int).
 */
@Composable
fun Int.wspScreenPx(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspScreen(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Returns the original value **auto-scaled** using the specified qualifier if the condition is not met.
 * When the device matches [uiModeType] AND the screen metric for [qualifierType]
 * is >= [qualifierValue], it uses [screenValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Retorna o valor original **auto-escalonado** usando o qualificador especificado se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] E a métrica de tela para
 * [qualifierType] é >= [qualifierValue], usa [screenValue] no lugar.
 */
@Composable
fun TextUnit.wspScreen(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    val uiModeMatch = currentUiModeType == uiModeType
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (uiModeMatch && qualifierMatch) {
        screenValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    } else {
        this.value.toInt().toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    }
}

/**
 * EN
 * Pixel version of wspScreen (TextUnit).
 *
 * PT
 * Versão em pixel de wspScreen (TextUnit).
 */
@Composable
fun TextUnit.wspScreenPx(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspScreen(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Returns the original raw TextUnit value if the condition is not met.
 * When the device matches [uiModeType] AND the screen metric for [qualifierType]
 * is >= [qualifierValue], it uses [screenValue] instead.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Retorna o valor original de TextUnit bruto se a condição não for atendida.
 * Quando o dispositivo corresponde ao [uiModeType] E a métrica de tela para
 * [qualifierType] é >= [qualifierValue], usa [screenValue] no lugar.
 */
@Composable
fun TextUnit.wspScreenPlain(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): TextUnit {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val activity = context.findActivitySp()
    val windowLayoutInfo = remember(activity) {
        activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
    }?.collectAsState(initial = null)
    val foldingFeature = windowLayoutInfo?.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()?.firstOrNull()
    val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
    val uiModeMatch = currentUiModeType == uiModeType
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (uiModeMatch && qualifierMatch) {
        screenValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    } else {
        this
    }
}

/**
 * EN
 * Pixel version of wspScreenPlain.
 *
 * PT
 * Versão em pixel de wspScreenPlain.
 */
@Composable
fun TextUnit.wspScreenPlainPx(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float {
    return LocalDensity.current.run { wspScreenPlain(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, fontScale).toPx() }
}
