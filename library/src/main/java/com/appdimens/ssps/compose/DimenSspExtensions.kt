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
    enableAdjust: Boolean = false,
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
        val qualifier = if (enableAdjust) finalQualifierResolver else DpQualifier.SMALL_WIDTH
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.sspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.SMALL_WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().sspRotate(rotationValue, finalQualifierResolver, orientation, enableAdjust, fontScale)
}

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
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Height (hDP)**.
 * Uses the base value by default, but when the device is in the specified [orientation],
 * it uses [rotationValue] scaled with the given [finalQualifierResolver].
 * Usage example: `30.hspRotate(45, DpQualifier.HEIGHT, Orientation.LANDSCAPE)`
 * → 30.hsp by default, 45 scaled by HEIGHT in landscape.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Usa o valor base por padrão, mas quando o dispositivo corresponde ao [orientation] especificado,
 * usa [rotationValue] escalado com o [finalQualifierResolver] dado.
 */
@Composable
fun Int.hspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
    orientation: Orientation = Orientation.LANDSCAPE,
    enableAdjust: Boolean = false,
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
        val qualifier = if (enableAdjust) finalQualifierResolver else DpQualifier.HEIGHT
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.hspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
    orientation: Orientation = Orientation.LANDSCAPE,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().hspRotate(rotationValue, finalQualifierResolver, orientation, enableAdjust, fontScale)
}

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
 * Extension for TextUnit (Sp) with dynamic scaling based on **Screen Width (wDP)**.
 * Uses the base value by default, but when the device is in the specified [orientation],
 * it uses [rotationValue] scaled with the given [finalQualifierResolver].
 * Usage example: `30.wspRotate(45, DpQualifier.WIDTH, Orientation.LANDSCAPE)`
 * → 30.wsp by default, 45 scaled by WIDTH in landscape.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Usa o valor base por padrão, mas quando o dispositivo corresponde ao [orientation] especificado,
 * usa [rotationValue] escalado com o [finalQualifierResolver] dado.
 */
@Composable
fun Int.wspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    enableAdjust: Boolean = false,
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
        val qualifier = if (enableAdjust) finalQualifierResolver else DpQualifier.WIDTH
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.wspRotate(
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().wspRotate(rotationValue, finalQualifierResolver, orientation, enableAdjust, fontScale)
}

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
    enableAdjust: Boolean = false,
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
        val qualifier = if (enableAdjust) (finalQualifierResolver ?: DpQualifier.SMALL_WIDTH) else DpQualifier.SMALL_WIDTH
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.sspMode(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().sspMode(modeValue, uiModeType, finalQualifierResolver, enableAdjust, fontScale)
}

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
    enableAdjust: Boolean = false,
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
        val qualifier = if (enableAdjust) (finalQualifierResolver ?: DpQualifier.HEIGHT) else DpQualifier.HEIGHT
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.hspMode(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().hspMode(modeValue, uiModeType, finalQualifierResolver, enableAdjust, fontScale)
}

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
    enableAdjust: Boolean = false,
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
        val qualifier = if (enableAdjust) (finalQualifierResolver ?: DpQualifier.WIDTH) else DpQualifier.WIDTH
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.wspMode(
    modeValue: Int,
    uiModeType: UiModeType,
    finalQualifierResolver: DpQualifier? = null,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().wspMode(modeValue, uiModeType, finalQualifierResolver, enableAdjust, fontScale)
}

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
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, fontScale)
    } else {
        val qualifier = if (enableAdjust) (finalQualifierResolver ?: DpQualifier.SMALL_WIDTH) else DpQualifier.SMALL_WIDTH
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.sspQualifier(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().sspQualifier(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, enableAdjust, fontScale)
}

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
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.HEIGHT, fontScale)
    } else {
        val qualifier = if (enableAdjust) (finalQualifierResolver ?: DpQualifier.HEIGHT) else DpQualifier.HEIGHT
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.hspQualifier(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().hspQualifier(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, enableAdjust, fontScale)
}

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
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    val configuration = LocalConfiguration.current
    val qualifierMatch = getQualifierValue(qualifierType, configuration) >= qualifierValue
    return if (qualifierMatch) {
        qualifiedValue.toDynamicScaledSp(finalQualifierResolver ?: DpQualifier.WIDTH, fontScale)
    } else {
        val qualifier = if (enableAdjust) (finalQualifierResolver ?: DpQualifier.WIDTH) else DpQualifier.WIDTH
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.wspQualifier(
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().wspQualifier(qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, enableAdjust, fontScale)
}

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
    enableAdjust: Boolean = false,
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
        val qualifier = if (enableAdjust) (finalQualifierResolver ?: DpQualifier.SMALL_WIDTH) else DpQualifier.SMALL_WIDTH
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.sspScreen(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().sspScreen(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, enableAdjust, fontScale)
}

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
    enableAdjust: Boolean = false,
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
        val qualifier = if (enableAdjust) (finalQualifierResolver ?: DpQualifier.HEIGHT) else DpQualifier.HEIGHT
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.hspScreen(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().hspScreen(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, enableAdjust, fontScale)
}

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
    enableAdjust: Boolean = false,
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
        val qualifier = if (enableAdjust) (finalQualifierResolver ?: DpQualifier.WIDTH) else DpQualifier.WIDTH
        this.toDynamicScaledSp(qualifier, fontScale)
    }
}

@Composable
fun TextUnit.wspScreen(
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    enableAdjust: Boolean = false,
    fontScale: Boolean = true
): TextUnit {
    return this.value.toInt().wspScreen(screenValue, uiModeType, qualifierType, qualifierValue, finalQualifierResolver, enableAdjust, fontScale)
}

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
