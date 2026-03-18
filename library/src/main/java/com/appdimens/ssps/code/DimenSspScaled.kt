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
package com.appdimens.ssps.code

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.window.layout.FoldingFeature
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.DpQualifierEntry
import com.appdimens.ssps.common.Inverter
import com.appdimens.ssps.common.Orientation
import com.appdimens.ssps.common.UiModeType

/**
 * EN
 * Represents a custom Sp entry with qualifiers and priority, for the Code Sp builder.
 *
 * PT
 * Representa uma entrada de Sp customizada com qualificadores e prioridade, para o builder Sp de código.
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

/**
 * EN
 * A class that allows defining custom Sp text dimensions
 * based on screen qualifiers (UiModeType, Width, Height, Smallest Width) from code.
 *
 * PT
 * Classe que permite definir dimensões de texto Sp customizadas baseadas em
 * qualificadores de tela (UiModeType, Largura, Altura, Smallest Width) via código.
 */
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
    @JvmOverloads
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
    @JvmOverloads
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
    @JvmOverloads
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
    @JvmOverloads
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

    /**
     * EN Internal resolution logic for ScaledSp (WITH font scale).
     * PT Lógica interna de resolução para ScaledSp (COM escala de fonte).
     */
    @SuppressLint("ConfigurationScreenWidthHeight")
    fun resolve(
        context: Context,
        qualifier: DpQualifier,
        foldingFeature: FoldingFeature? = null
    ): Float {
        val configuration = context.resources.configuration
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
                val screenValue = when (qualifierEntry.type) {
                    DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp
                    DpQualifier.HEIGHT -> configuration.screenHeightDp
                    DpQualifier.WIDTH -> configuration.screenWidthDp
                }
                val qualifierMatch = screenValue >= qualifierEntry.value
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

        return DimenSsp.getDimensionInSpPx(
            context,
            finalQualifier,
            valueToUse,
            foundEntry?.inverter ?: Inverter.DEFAULT,
            finalFontScale
        )
    }

    /**
     * EN Internal resolution logic for ScaledSp (WITHOUT font scale).
     * PT Lógica interna de resolução para ScaledSp (SEM escala de fonte).
     */
    fun resolveNoFontScale(
        context: Context,
        qualifier: DpQualifier,
        foldingFeature: FoldingFeature? = null
    ): Float {
        val configuration = context.resources.configuration
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
                val screenValue = when (qualifierEntry.type) {
                    DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp
                    DpQualifier.HEIGHT -> configuration.screenHeightDp
                    DpQualifier.WIDTH -> configuration.screenWidthDp
                }
                val qualifierMatch = screenValue >= qualifierEntry.value
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

        return DimenSsp.getDimensionInSpPx(
            context,
            finalQualifier,
            valueToUse,
            foundEntry?.inverter ?: Inverter.DEFAULT,
            fontScale = false
        )
    }

    // EN Context-aware resolution methods.
    // PT Métodos de resolução cientes do contexto.

    /**
     * EN The final dimension value resolved using Smallest Width (WITH font scale).
     * PT O valor de dimensão final resolvido usando Smallest Width (COM escala de fonte).
     */
    @JvmOverloads
    fun ssp(context: Context, foldingFeature: FoldingFeature? = null): Float =
        resolve(context, DpQualifier.SMALL_WIDTH, foldingFeature)

    /**
     * EN The final dimension value resolved using Screen Height (WITH font scale).
     * PT O valor de dimensão final resolvido usando Altura da Tela (COM escala de fonte).
     */
    @JvmOverloads
    fun hsp(context: Context, foldingFeature: FoldingFeature? = null): Float =
        resolve(context, DpQualifier.HEIGHT, foldingFeature)

    /**
     * EN The final dimension value resolved using Screen Width (WITH font scale).
     * PT O valor de dimensão final resolvido usando Largura da Tela (COM escala de fonte).
     */
    @JvmOverloads
    fun wsp(context: Context, foldingFeature: FoldingFeature? = null): Float =
        resolve(context, DpQualifier.WIDTH, foldingFeature)

    /**
     * EN The final dimension value resolved using Smallest Width (WITHOUT FONT SCALE).
     * PT O valor de dimensão final resolvido usando Smallest Width (SEM ESCALA DE FONTE).
     */
    @JvmOverloads
    fun sem(context: Context, foldingFeature: FoldingFeature? = null): Float =
        resolveNoFontScale(context, DpQualifier.SMALL_WIDTH, foldingFeature)

    /**
     * EN The final dimension value resolved using Screen Height (WITHOUT FONT SCALE).
     * PT O valor de dimensão final resolvido usando Altura da Tela (SEM ESCALA DE FONTE).
     */
    @JvmOverloads
    fun hem(context: Context, foldingFeature: FoldingFeature? = null): Float =
        resolveNoFontScale(context, DpQualifier.HEIGHT, foldingFeature)

    /**
     * EN The final dimension value resolved using Screen Width (WITHOUT FONT SCALE).
     * PT O valor de dimensão final resolvido usando Largura da Tela (SEM ESCALA DE FONTE).
     */
    @JvmOverloads
    fun wem(context: Context, foldingFeature: FoldingFeature? = null): Float =
        resolveNoFontScale(context, DpQualifier.WIDTH, foldingFeature)
}
