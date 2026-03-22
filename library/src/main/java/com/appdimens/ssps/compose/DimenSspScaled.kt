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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.DpQualifierEntry
import com.appdimens.ssps.common.Inverter
import com.appdimens.ssps.common.Orientation
import com.appdimens.ssps.common.UiModeType

/**
 * EN
 * Represents a custom Sp entry with qualifiers and priority, for the Compose Sp builder.
 *
 * PT
 * Representa uma entrada de Sp customizada com qualificadores e prioridade, para o builder Sp do Compose.
 *
 * @param uiModeType The UI mode (CAR, TELEVISION, WATCH, NORMAL). Null for any mode.
 * @param dpQualifierEntry The Dp qualifier entry (type and value). Null if only UI mode is used.
 * @param orientation The screen orientation (LANDSCAPE, PORTRAIT, DEFAULT).
 * @param customValue The base Int Sp value to be used if the condition is met.
 * @param finalQualifierResolver Optional override for the scaling qualifier at resolution time.
 * @param priority The resolution priority. 1 is most specific (UI + Qualifier), 4 is least specific.
 * @param inverter The inverter type to adapt scaling on rotation changes.
 * @param fontScale Whether to respect the system font scale (default true).
 */
data class CustomSpEntry(
    val uiModeType: UiModeType? = null,
    val dpQualifierEntry: DpQualifierEntry? = null,
    val orientation: Orientation = Orientation.DEFAULT,
    val customValue: Int,
    val finalQualifierResolver: DpQualifier? = null,
    val priority: Int,
    val inverter: Inverter = Inverter.DEFAULT,
    val fontScale: Boolean = true
)

// EN Methods for creating the ScaledSp class.
// PT Métodos de criação da classe ScaledSp.

/**
 * EN Starts the build chain for ScaledSp from a base TextUnit (Sp).
 * Usage example: `16.sp.scaledSp().screen(...)`.
 *
 * PT Inicia a cadeia de construção para ScaledSp a partir de um TextUnit (Sp) base.
 * Exemplo de uso: `16.sp.scaledSp().screen(...)`.
 */
@Composable
fun TextUnit.scaledSp(): ScaledSp = ScaledSp(this.value.toInt())

/**
 * EN Starts the build chain for ScaledSp from a base Int (treated as sp).
 * Usage example: `16.scaledSp().screen(...)`.
 *
 * PT Inicia a cadeia de construção para ScaledSp a partir de um Int base (tratado como sp).
 * Exemplo de uso: `16.scaledSp().screen(...)`.
 */
@Composable
fun Int.scaledSp(): ScaledSp = ScaledSp(this)

// EN Helps extract the activity from context wrapper (ScaledSp version)
// PT Ajuda a extrair a activity de um context wrapper (versão ScaledSp)
private tailrec fun android.content.Context.findActivityScaledSp(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivityScaledSp()
    else -> null
}

/**
 * EN
 * A Stable Compose class that allows defining custom Sp text dimensions
 * based on screen qualifiers (UiModeType, Width, Height, Smallest Width).
 *
 * The TextUnit is resolved at composition and uses the base value or a custom value,
 * applying dynamic scaling via the existing XML DP resources.
 *
 * PT
 * Classe Stable do Compose que permite definir dimensões de texto Sp customizadas
 * baseadas em qualificadores de tela (UiModeType, Largura, Altura, Smallest Width).
 *
 * O TextUnit é resolvido na composição e usa o valor base ou um valor customizado,
 * aplicando o escalonamento dinâmico via os recursos XML de DP existentes.
 */
@Stable
class ScaledSp private constructor(
    private val initialBaseValue: Int,
    private val defaultFontScale: Boolean = true,
    private val sortedCustomEntries: List<CustomSpEntry> = emptyList()
) {
    constructor(initialBaseValue: Int) : this(initialBaseValue, true, emptyList())

    private fun reorderEntries(newEntry: CustomSpEntry): List<CustomSpEntry> {
        return (sortedCustomEntries + newEntry).sortedWith(
            compareBy<CustomSpEntry> { it.priority }
                .thenByDescending { it.dpQualifierEntry?.value ?: 0 }
        )
    }

    // EN Fluent methods for construction.
    // PT Métodos fluentes para construção.

    /**
     * EN Priority 1: Most specific qualifier - Combines UiModeType AND Dp Qualifier (sw, h, w).
     * PT Prioridade 1: Qualificador mais específico - Combina UiModeType E Qualificador de Dp (sw, h, w).
     */
    fun screen(
        uiModeType: UiModeType,
        qualifierType: DpQualifier,
        qualifierValue: Int,
        customValue: Int,
        finalQualifierResolver: DpQualifier? = null,
        orientation: Orientation = Orientation.DEFAULT,
        inverter: Inverter = Inverter.DEFAULT,
        fontScale: Boolean = defaultFontScale
    ): ScaledSp {
        val entry = CustomSpEntry(
            uiModeType = uiModeType,
            dpQualifierEntry = DpQualifierEntry(qualifierType, qualifierValue),
            orientation = orientation,
            customValue = customValue,
            finalQualifierResolver = finalQualifierResolver,
            priority = 1,
            inverter = inverter,
            fontScale = fontScale
        )
        return ScaledSp(initialBaseValue, defaultFontScale, reorderEntries(entry))
    }

    /**
     * EN Priority 2: UiModeType qualifier (e.g., TELEVISION, WATCH).
     * PT Prioridade 2: Qualificador de UiModeType (e.g., TELEVISION, WATCH).
     */
    fun screen(
        type: UiModeType,
        customValue: Int,
        finalQualifierResolver: DpQualifier? = null,
        orientation: Orientation = Orientation.DEFAULT,
        inverter: Inverter = Inverter.DEFAULT,
        fontScale: Boolean = defaultFontScale
    ): ScaledSp {
        val entry = CustomSpEntry(
            uiModeType = type,
            orientation = orientation,
            customValue = customValue,
            finalQualifierResolver = finalQualifierResolver,
            priority = 2,
            inverter = inverter,
            fontScale = fontScale
        )
        return ScaledSp(initialBaseValue, defaultFontScale, reorderEntries(entry))
    }

    /**
     * EN Priority 3: Dp qualifier (sw, h, w) without UiModeType restriction.
     * PT Prioridade 3: Qualificador de Dp (sw, h, w) sem restrição de UiModeType.
     */
    fun screen(
        type: DpQualifier,
        value: Int,
        customValue: Int,
        finalQualifierResolver: DpQualifier? = null,
        orientation: Orientation = Orientation.DEFAULT,
        inverter: Inverter = Inverter.DEFAULT,
        fontScale: Boolean = defaultFontScale
    ): ScaledSp {
        val entry = CustomSpEntry(
            dpQualifierEntry = DpQualifierEntry(type, value),
            orientation = orientation,
            customValue = customValue,
            finalQualifierResolver = finalQualifierResolver,
            priority = 3,
            inverter = inverter,
            fontScale = fontScale
        )
        return ScaledSp(initialBaseValue, defaultFontScale, reorderEntries(entry))
    }

    /**
     * EN Priority 4: Orientation only.
     * PT Prioridade 4: Apenas Orientação.
     */
    fun screen(
        orientation: Orientation = Orientation.DEFAULT,
        customValue: Int,
        finalQualifierResolver: DpQualifier? = null,
        inverter: Inverter = Inverter.DEFAULT,
        fontScale: Boolean = defaultFontScale
    ): ScaledSp {
        val entry = CustomSpEntry(
            orientation = orientation,
            customValue = customValue,
            finalQualifierResolver = finalQualifierResolver,
            priority = 4,
            inverter = inverter,
            fontScale = fontScale
        )
        return ScaledSp(initialBaseValue, defaultFontScale, reorderEntries(entry))
    }

    // EN Resolution logic.
    // PT Lógica de resolução.

    @SuppressLint("ConfigurationScreenWidthHeight")
    @Composable
    private fun resolve(qualifier: DpQualifier): TextUnit {
        val context = LocalContext.current
        val configuration = LocalConfiguration.current

        val activity = context.findActivityScaledSp()
        val windowLayoutInfo = remember(activity) {
            activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
        }?.collectAsState(initial = null)

        val foldingFeature = windowLayoutInfo?.value?.displayFeatures
            ?.filterIsInstance<FoldingFeature>()
            ?.firstOrNull()

        val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)

        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        val foundEntry = sortedCustomEntries.firstOrNull { entry ->
            val qualifierEntry = entry.dpQualifierEntry
            val uiModeMatch = entry.uiModeType == null || entry.uiModeType == currentUiModeType

            val orientationMatch = when (entry.orientation) {
                Orientation.LANDSCAPE -> isLandscape
                Orientation.PORTRAIT -> isPortrait
                else -> true
            }

            if (qualifierEntry != null) {
                val qualifierMatch = getQualifierValue(qualifierEntry.type, configuration) >= qualifierEntry.value
                if (entry.priority == 1 && uiModeMatch && qualifierMatch && orientationMatch) return@firstOrNull true
                if (entry.priority == 3 && qualifierMatch && orientationMatch) return@firstOrNull true
                return@firstOrNull false
            } else {
                if (entry.priority == 2 && uiModeMatch && orientationMatch) return@firstOrNull true
                if (entry.priority == 4 && orientationMatch) return@firstOrNull true
                return@firstOrNull false
            }
        }

        val valueToUse = foundEntry?.customValue ?: initialBaseValue
        val finalQualifier = foundEntry?.finalQualifierResolver ?: qualifier
        val finalFontScale = foundEntry?.fontScale ?: defaultFontScale

        return valueToUse.toDynamicScaledSp(finalQualifier, finalFontScale, foundEntry?.inverter ?: Inverter.DEFAULT)
    }

    @SuppressLint("ConfigurationScreenWidthHeight")
    @Composable
    private fun resolveNoFontScale(qualifier: DpQualifier): TextUnit {
        val context = LocalContext.current
        val configuration = LocalConfiguration.current

        val activity = context.findActivityScaledSp()
        val windowLayoutInfo = remember(activity) {
            activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) }
        }?.collectAsState(initial = null)

        val foldingFeature = windowLayoutInfo?.value?.displayFeatures
            ?.filterIsInstance<FoldingFeature>()
            ?.firstOrNull()

        val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)

        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        val foundEntry = sortedCustomEntries.firstOrNull { entry ->
            val qualifierEntry = entry.dpQualifierEntry
            val uiModeMatch = entry.uiModeType == null || entry.uiModeType == currentUiModeType

            val orientationMatch = when (entry.orientation) {
                Orientation.LANDSCAPE -> isLandscape
                Orientation.PORTRAIT -> isPortrait
                else -> true
            }

            if (qualifierEntry != null) {
                val qualifierMatch = getQualifierValue(qualifierEntry.type, configuration) >= qualifierEntry.value
                if (entry.priority == 1 && uiModeMatch && qualifierMatch && orientationMatch) return@firstOrNull true
                if (entry.priority == 3 && qualifierMatch && orientationMatch) return@firstOrNull true
                return@firstOrNull false
            } else {
                if (entry.priority == 2 && uiModeMatch && orientationMatch) return@firstOrNull true
                if (entry.priority == 4 && orientationMatch) return@firstOrNull true
                return@firstOrNull false
            }
        }

        val valueToUse = foundEntry?.customValue ?: initialBaseValue
        val finalQualifier = foundEntry?.finalQualifierResolver ?: qualifier

        return valueToUse.toDynamicScaledSp(finalQualifier, fontScale = false, foundEntry?.inverter ?: Inverter.DEFAULT)
    }

    /**
     * EN The final TextUnit (Sp) value resolved using Smallest Width (WITH font scale).
     * PT O valor final TextUnit (Sp) resolvido usando Smallest Width (COM escala de fonte).
     */
    @get:Composable
    val ssp: TextUnit get() = resolve(DpQualifier.SMALL_WIDTH)

    /**
     * EN
     * Pixel version of ssp.
     *
     * PT
     * Versão em pixel de ssp.
     */
    @get:Composable
    val sspPx: Float get() = LocalDensity.current.run { ssp.toPx() }

    /**
     * EN The final TextUnit (Sp) value resolved using Screen Height (WITH font scale).
     * PT O valor final TextUnit (Sp) resolvido usando Altura da Tela (COM escala de fonte).
     */
    @get:Composable
    val hsp: TextUnit get() = resolve(DpQualifier.HEIGHT)

    /**
     * EN
     * Pixel version of hsp.
     *
     * PT
     * Versão em pixel de hsp.
     */
    @get:Composable
    val hspPx: Float get() = LocalDensity.current.run { hsp.toPx() }

    /**
     * EN The final TextUnit (Sp) value resolved using Screen Width (WITH font scale).
     * PT O valor final TextUnit (Sp) resolvido usando Largura da Tela (COM escala de fonte).
     */
    @get:Composable
    val wsp: TextUnit get() = resolve(DpQualifier.WIDTH)

    /**
     * EN
     * Pixel version of wsp.
     *
     * PT
     * Versão em pixel de wsp.
     */
    @get:Composable
    val wspPx: Float get() = LocalDensity.current.run { wsp.toPx() }

    /**
     * EN The final TextUnit (Sp) value resolved using Smallest Width (WITHOUT FONT SCALE).
     * The fontScale flag in each entry is ignored; scale is always stripped.
     * PT O valor final TextUnit (Sp) resolvido usando Smallest Width (SEM ESCALA DE FONTE).
     * A flag fontScale de cada entrada é ignorada; a escala é sempre removida.
     */
    @get:Composable
    val sem: TextUnit get() = resolveNoFontScale(DpQualifier.SMALL_WIDTH)

    /**
     * EN
     * Pixel version of sem.
     *
     * PT
     * Versão em pixel de sem.
     */
    @get:Composable
    val semPx: Float get() = LocalDensity.current.run { sem.toPx() }

    /**
     * EN The final TextUnit (Sp) value resolved using Screen Height (WITHOUT FONT SCALE).
     * PT O valor final TextUnit (Sp) resolvido usando Altura da Tela (SEM ESCALA DE FONTE).
     */
    @get:Composable
    val hem: TextUnit get() = resolveNoFontScale(DpQualifier.HEIGHT)

    /**
     * EN
     * Pixel version of hem.
     *
     * PT
     * Versão em pixel de hem.
     */
    @get:Composable
    val hemPx: Float get() = LocalDensity.current.run { hem.toPx() }

    /**
     * EN The final TextUnit (Sp) value resolved using Screen Width (WITHOUT FONT SCALE).
     * PT O valor final TextUnit (Sp) resolvido usando Largura da Tela (SEM ESCALA DE FONTE).
     */
    @get:Composable
    val wem: TextUnit get() = resolveNoFontScale(DpQualifier.WIDTH)

    /**
     * EN
     * Pixel version of wem.
     *
     * PT
     * Versão em pixel de wem.
     */
    @get:Composable
    val wemPx: Float get() = LocalDensity.current.run { wem.toPx() }
}
