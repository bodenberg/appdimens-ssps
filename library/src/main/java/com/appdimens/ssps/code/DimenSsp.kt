/**
 * Author & Developer: Jean Bodenberg
 * GIT: https://github.com/bodenberg/appdimens.git
 * Date: 2025-10-04
 *
 * Library: AppDimens
 *
 * Description:
 * The AppDimens library is a dimension management system that automatically
 * adjusts Sp, Sp, and Px values in a responsive and mathematically refined way,
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
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.DpQualifierEntry
import com.appdimens.ssps.common.Inverter
import com.appdimens.ssps.common.Orientation
import com.appdimens.ssps.common.UiModeType

/**
 * EN
 * Utility object for handling SSP (Scalable Sp) dimensions.
 *
 * PT
 * Objeto utilitário para manipulação de dimensões SSP (Scalable Sp).
 */
object DimenSsp {
    private const val MIN_VALUE =
        1 // EN Minimum allowed SSP value. / PT Valor mínimo permitido para SSP.
    private const val MAX_VALUE =
        600 // EN Maximum allowed SSP value. / PT Valor máximo permitido para SSP.
    private const val DIMEN_TYPE =
        "dimen" // EN The resource type for dimensions. / PT O tipo de recurso para dimensões.

    /**
     * EN
     * Gets the dimension in pixels from an SSP value.
     *
     * PT
     * Obtém a dimensão em pixels a partir de um valor SSP.
     *
     * @param context The application context.
     * @param dpQualifier DpQualifier.
     * @param value The SSP value (1 to 600).
     * @param fontScale A boolean that indicates if the font scale is enabled or not
     * @param inverter The inverter type to dynamically adapt scaling (default is Inverter.DEFAULT).
     * @return The dimension in pixels, or 0f if not found.
     */
    @JvmStatic
    @JvmOverloads
    fun getDimensionInPx(context: Context, dpQualifier: DpQualifier, value: Int, fontScale: Boolean = true, inverter: Inverter = Inverter.DEFAULT): Float {
        if (value == 0) return 0f
        val resourceId = getResourceId(context, dpQualifier, value, inverter)
        return if (resourceId != 0) {
            if (fontScale) context.resources.getDimension(resourceId)
            else context.resources.getDimension(resourceId) / context.resources.configuration.fontScale
        } else 0f
    }

    /**
     * EN
     * Gets the resource ID for an SSP value.
     *
     * PT
     * Obtém o ID do recurso para um valor SSP.
     *
     * @param context The application context.
     * @param dpQualifier DpQualifier.
     * @param value The SSP value (1 to 600).
     * @param inverter The inverter type to dynamically adapt scaling (default is Inverter.DEFAULT).
     * @return The resource ID, or 0 if not found.
     */
    @JvmStatic
    @JvmOverloads
    @SuppressLint("DiscouragedApi")
    fun getResourceId(context: Context, dpQualifier: DpQualifier, value: Int, inverter: Inverter = Inverter.DEFAULT): Int {
        if (value == 0) return 0

        val configuration = context.resources.configuration
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        var actualQualifier = dpQualifier

        when (inverter) {
            Inverter.PH_TO_LW -> if (isLandscape && dpQualifier == DpQualifier.HEIGHT) actualQualifier = DpQualifier.WIDTH
            Inverter.PW_TO_LH -> if (isLandscape && dpQualifier == DpQualifier.WIDTH) actualQualifier = DpQualifier.HEIGHT
            Inverter.LH_TO_PW -> if (isPortrait && dpQualifier == DpQualifier.HEIGHT) actualQualifier = DpQualifier.WIDTH
            Inverter.LW_TO_PH -> if (isPortrait && dpQualifier == DpQualifier.WIDTH) actualQualifier = DpQualifier.HEIGHT
            Inverter.DEFAULT -> {}
        }

        val safeValue = value.coerceIn(MIN_VALUE, MAX_VALUE)
        val sspSuffix = when (actualQualifier) {
            DpQualifier.SMALL_WIDTH -> "ssp"
            DpQualifier.HEIGHT -> "hsp"
            DpQualifier.WIDTH -> "wsp"
        }
        val dimenName = buildResourceName(safeValue, sspSuffix)

        return context.resources.getIdentifier(dimenName, DIMEN_TYPE, context.packageName)
    }

    // EN Extensions style functions similar to the compose equivalents for quick resolution.
    // PT Funções estilo extensão similares aos equivalentes do compose para resolução rápida.

    /**
     * EN
     * Quick resolution for Smallest Width (ssp).
     * Usage example: `DimenSsp.ssp(context, 16)`.
     *
     * PT
     * Resolução rápida para Smallest Width (ssp).
     * Exemplo de uso: `DimenSsp.ssp(context, 16)`.
     */
    @JvmStatic
    @JvmOverloads
    fun ssp(context: Context, value: Int, fontScale: Boolean = true): Float = getDimensionInPx(context, DpQualifier.SMALL_WIDTH, value, fontScale)

    /**
     * EN
     * Quick resolution for Screen Height (hsp).
     * Usage example: `DimenSsp.hsp(context, 32)`.
     *
     * PT
     * Resolução rápida para Altura da Tela (hsp).
     * Exemplo de uso: `DimenSsp.hsp(context, 32)`.
     */
    @JvmStatic
    @JvmOverloads
    fun hsp(context: Context, value: Int, fontScale: Boolean = true): Float = getDimensionInPx(context, DpQualifier.HEIGHT, value, fontScale)

    /**
     * EN
     * Quick resolution for Screen Height (hsp), but
     * in landscape orientation it acts as Screen Width (wsp).
     * Usage example: `DimenSsp.hsp_lw(context, 32)`.
     *
     * PT
     * Resolução rápida para Altura da Tela (hsp), mas
     * na orientação paisagem atua como Largura da Tela (wsp).
     * Exemplo de uso: `DimenSsp.hsp_lw(context, 32)`.
     */
    @JvmStatic
    @JvmOverloads
    fun hsp_lw(context: Context, value: Int, fontScale: Boolean = true): Float = getDimensionInPx(context, DpQualifier.HEIGHT, value, fontScale, Inverter.PH_TO_LW)

    /**
     * EN
     * Quick resolution for Screen Height (hsp), but
     * in portrait orientation it acts as Screen Width (wsp).
     * Usage example: `DimenSsp.hsp_pw(context, 32)`.
     *
     * PT
     * Resolução rápida para Altura da Tela (hsp), mas
     * na orientação retrato atua como Largura da Tela (wsp).
     * Exemplo de uso: `DimenSsp.hsp_pw(context, 32)`.
     */
    @JvmStatic
    @JvmOverloads
    fun hsp_pw(context: Context, value: Int, fontScale: Boolean = true): Float = getDimensionInPx(context, DpQualifier.HEIGHT, value, fontScale, Inverter.LH_TO_PW)

    /**
     * EN
     * Quick resolution for Screen Width (wsp).
     * Usage example: `DimenSsp.wsp(context, 100)`.
     *
     * PT
     * Resolução rápida para Largura da Tela (wsp).
     * Exemplo de uso: `DimenSsp.wsp(context, 100)`.
     */
    @JvmStatic
    @JvmOverloads
    fun wsp(context: Context, value: Int, fontScale: Boolean = true): Float = getDimensionInPx(context, DpQualifier.WIDTH, value, fontScale)

    /**
     * EN
     * Quick resolution for Screen Width (wsp), but
     * in landscape orientation it acts as Screen Height (hsp).
     * Usage example: `DimenSsp.wsp_lh(context, 100)`.
     *
     * PT
     * Resolução rápida para Largura da Tela (wsp), mas
     * na orientação paisagem atua como Altura da Tela (hsp).
     * Exemplo de uso: `DimenSsp.wsp_lh(context, 100)`.
     */
    @JvmStatic
    @JvmOverloads
    fun wsp_lh(context: Context, value: Int, fontScale: Boolean = true): Float = getDimensionInPx(context, DpQualifier.WIDTH, value, fontScale, Inverter.PW_TO_LH)

    /**
     * EN
     * Quick resolution for Screen Width (wsp), but
     * in portrait orientation it acts as Screen Height (hsp).
     * Usage example: `DimenSsp.wsp_ph(context, 100)`.
     *
     * PT
     * Resolução rápida para Largura da Tela (wsp), mas
     * na orientação retrato atua como Altura da Tela (hsp).
     * Exemplo de uso: `DimenSsp.wsp_ph(context, 100)`.
     */
    @JvmStatic
    @JvmOverloads
    fun wsp_ph(context: Context, value: Int, fontScale: Boolean = true): Float = getDimensionInPx(context, DpQualifier.WIDTH, value, fontScale, Inverter.LW_TO_PH)

    // EN Resource ID variants of the above extensions.
    // PT Variantes que retornam o ID de recurso das extensões acima.

    /**
     * EN
     * Quick resolution for Smallest Width resource ID (sspRes).
     * Usage example: `DimenSsp.sspRes(context, 16)`.
     *
     * PT
     * Resolução rápida para ID de recurso Smallest Width (sspRes).
     * Exemplo de uso: `DimenSsp.sspRes(context, 16)`.
     */
    @JvmStatic
    fun sspRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.SMALL_WIDTH, value)

    /**
     * EN
     * Quick resolution for Screen Height resource ID (hspRes).
     * Usage example: `DimenSsp.hspRes(context, 32)`.
     *
     * PT
     * Resolução rápida para ID de recurso Altura da Tela (hspRes).
     * Exemplo de uso: `DimenSsp.hspRes(context, 32)`.
     */
    @JvmStatic
    fun hspRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.HEIGHT, value)

    /**
     * EN
     * Quick resolution for Screen Height resource ID (hspRes), but
     * in landscape orientation it acts as Screen Width resource ID (wspRes).
     * Usage example: `DimenSsp.hsp_lwRes(context, 32)`.
     *
     * PT
     * Resolução rápida para ID de recurso Altura da Tela (hspRes), mas
     * na orientação paisagem atua como Largura da Tela (wspRes).
     * Exemplo de uso: `DimenSsp.hsp_lwRes(context, 32)`.
     */
    @JvmStatic
    fun hsp_lwRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.HEIGHT, value, Inverter.PH_TO_LW)

    /**
     * EN
     * Quick resolution for Screen Height resource ID (hspRes), but
     * in portrait orientation it acts as Screen Width resource ID (wspRes).
     * Usage example: `DimenSsp.hsp_pwRes(context, 32)`.
     *
     * PT
     * Resolução rápida para ID de recurso Altura da Tela (hspRes), mas
     * na orientação retrato atua como Largura da Tela (wspRes).
     * Exemplo de uso: `DimenSsp.hsp_pwRes(context, 32)`.
     */
    @JvmStatic
    fun hsp_pwRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.HEIGHT, value, Inverter.LH_TO_PW)

    /**
     * EN
     * Quick resolution for Screen Width resource ID (wspRes).
     * Usage example: `DimenSsp.wspRes(context, 100)`.
     *
     * PT
     * Resolução rápida para ID de recurso Largura da Tela (wspRes).
     * Exemplo de uso: `DimenSsp.wspRes(context, 100)`.
     */
    @JvmStatic
    fun wspRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.WIDTH, value)

    /**
     * EN
     * Quick resolution for Screen Width resource ID (wspRes), but
     * in landscape orientation it acts as Screen Height resource ID (hspRes).
     * Usage example: `DimenSsp.wsp_lhRes(context, 100)`.
     *
     * PT
     * Resolução rápida para ID de recurso Largura da Tela (wspRes), mas
     * na orientação paisagem atua como Altura da Tela (hspRes).
     * Exemplo de uso: `DimenSsp.wsp_lhRes(context, 100)`.
     */
    @JvmStatic
    fun wsp_lhRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.WIDTH, value, Inverter.PW_TO_LH)

    /**
     * EN
     * Quick resolution for Screen Width resource ID (wspRes), but
     * in portrait orientation it acts as Screen Height resource ID (hspRes).
     * Usage example: `DimenSsp.wsp_phRes(context, 100)`.
     *
     * PT
     * Resolução rápida para ID de recurso Largura da Tela (wspRes), mas
     * na orientação retrato atua como Altura da Tela (hspRes).
     * Exemplo de uso: `DimenSsp.wsp_phRes(context, 100)`.
     */
    @JvmStatic
    fun wsp_phRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.WIDTH, value, Inverter.LW_TO_PH)

    /**
     * EN
     * Builds the resource name for a given SSP value.
     * For negative values, it uses the "_minus" prefix (e.g., _minus10ssp) - Note: SSP only supports positive sizes.
     * For positive values, it uses a "_" prefix (e.g., _10ssp).
     *
     * PT
     * Constrói o nome do recurso para um determinado valor SSP.
     * Para valores negativos, usa o prefixo "_minus" (ex: _minus10ssp) - Nota: SSP só suporta tamanhos positivos.
     * Para valores positivos, usa o prefixo "_" (ex: _10ssp).
     *
     * @param value The integer value.
     * @return The formatted resource name.
     */
    private fun buildResourceName(value: Int, sspSuffix: String): String {
        return "_${value}$sspSuffix"
    }

    /**
     * EN
     * Starts the build chain for the custom dimension Scaled from a base Int.
     * Usage example: `DimenSsp.scaled(100).screen(...)`.
     *
     * PT
     * Inicia a cadeia de construção para a dimensão customizada Scaled a partir de um Int base.
     * Exemplo de uso: `DimenSsp.scaled(100).screen(...)`.
     */
    @JvmStatic
    fun scaled(initialBaseValue: Int): Scaled = Scaled(initialBaseValue)
}

/**
 * EN Starts the build chain for the custom dimension Scaled from a base Int.
 * PT Inicia a cadeia de construção para a dimensão customizada Scaled a partir de um Int base.
 */
fun Int.scaledSsp(): Scaled = Scaled(this)

/**
 * EN
 * Represents a custom dimension entry with qualifiers and priority for code.
 *
 * PT
 * Representa uma entrada de dimensão customizada com qualificadores e prioridade para código.
 *
 * @param uiModeType The UI mode (CAR, TELEVISION, WATCH, NORMAL). Null for any mode.
 * @param dpQualifierEntry The Dp qualifier entry (type and value, e.g., SMALL_WIDTH > 600). Null if only UI mode is used.
 * @param orientation The screen orientation (LANDSCAPE, PORTRAIT, DEFAULT).
 * @param customValue The int value to be used if the condition is met.
 * @param finalQualifierResolver Optional dimension qualifier (e.g., HEIGHT) to be applied at resolution time.
 * @param fontScale Optional enable/disable font scale.
 * @param priority The resolution priority. 1 is more specific (UI + Qualifier), 3 is less specific (Qualifier only).
 * @param inverter The inverter type to adapt scaling width/height on rotation changes (default is Inverter.DEFAULT).
 */
data class CustomSspEntry(
    val uiModeType: UiModeType? = null,
    val dpQualifierEntry: DpQualifierEntry? = null,
    val orientation: Orientation = Orientation.DEFAULT,
    val customValue: Int,
    val finalQualifierResolver: DpQualifier? = null,
    val fontScale: Boolean? = true,
    val priority: Int,
    val inverter: Inverter = Inverter.DEFAULT
)

/**
 * EN
 * A class that allows defining custom dimensions based on screen qualifiers.
 * Resolves to the final dimension in pixels.
 *
 * PT
 * Classe que permite definir dimensões customizadas baseadas em qualificadores de tela.
 * Resolve para a dimensão final em pixels.
 */
class Scaled internal constructor(
    private val initialBaseValue: Int,
    private val sortedCustomEntries: List<CustomSspEntry> = emptyList()
) {

    constructor(initialBaseValue: Int) : this(initialBaseValue, emptyList())

    private fun reorderEntries(newEntry: CustomSspEntry): List<CustomSspEntry> {
        return (sortedCustomEntries + newEntry).sortedWith(
            compareBy<CustomSspEntry> { it.priority }
                .thenByDescending { it.dpQualifierEntry?.value ?: 0 }
        )
    }

    /**
     * EN Priority 1: Most specific qualifier - Combines UiModeType AND Dp Qualifier.
     * PT Prioridade 1: Qualificador mais específico - Combina UiModeType E Qualificador de Dp.
     */
    @JvmOverloads
    fun screen(
        uiModeType: UiModeType,
        qualifierType: DpQualifier,
        qualifierValue: Int,
        customValue: Int,
        finalQualifierResolver: DpQualifier? = null,
        orientation: Orientation = Orientation.DEFAULT,
        fontScale: Boolean? = true,
        inverter: Inverter = Inverter.DEFAULT
    ): Scaled {
        val entry = CustomSspEntry(
            uiModeType = uiModeType,
            dpQualifierEntry = DpQualifierEntry(qualifierType, qualifierValue),
            orientation = orientation,
            customValue = customValue,
            finalQualifierResolver = finalQualifierResolver,
            fontScale = fontScale,
            priority = 1,
            inverter = inverter
        )
        return Scaled(initialBaseValue, reorderEntries(entry))
    }

    /**
     * EN Priority 2: UiModeType qualifier.
     * PT Prioridade 2: Qualificador de UiModeType.
     */
    @JvmOverloads
    fun screen(
        type: UiModeType, 
        customValue: Int, 
        finalQualifierResolver: DpQualifier? = null,
        orientation: Orientation = Orientation.DEFAULT,
        fontScale: Boolean? = true,
        inverter: Inverter = Inverter.DEFAULT
    ): Scaled {
        val entry = CustomSspEntry(
            uiModeType = type,
            orientation = orientation,
            customValue = customValue,
            finalQualifierResolver = finalQualifierResolver,
            fontScale = fontScale,
            priority = 2,
            inverter = inverter
        )
        return Scaled(initialBaseValue, reorderEntries(entry))
    }

    /**
     * EN Priority 3: Dp qualifier.
     * PT Prioridade 3: Qualificador de Dp.
     */
    @JvmOverloads
    fun screen(
        type: DpQualifier, 
        value: Int, 
        customValue: Int, 
        finalQualifierResolver: DpQualifier? = null,
        orientation: Orientation = Orientation.DEFAULT,
        fontScale: Boolean? = true,
        inverter: Inverter = Inverter.DEFAULT
    ): Scaled {
        val entry = CustomSspEntry(
            dpQualifierEntry = DpQualifierEntry(type, value),
            orientation = orientation,
            customValue = customValue,
            finalQualifierResolver = finalQualifierResolver,
            fontScale = fontScale,
            priority = 3,
            inverter = inverter
        )
        return Scaled(initialBaseValue, reorderEntries(entry))
    }

    /**
     * EN Priority 4: Orientation.
     * PT Prioridade 4: Orientação.
     */
    @JvmOverloads
    fun screen(
        orientation: Orientation = Orientation.DEFAULT,
        customValue: Int,
        finalQualifierResolver: DpQualifier? = null,
        fontScale: Boolean? = true,
        inverter: Inverter = Inverter.DEFAULT
    ): Scaled {
        val entry = CustomSspEntry(
            orientation = orientation,
            customValue = customValue,
            finalQualifierResolver = finalQualifierResolver,
            fontScale = fontScale,
            priority = 4,
            inverter = inverter
        )
        return Scaled(initialBaseValue, reorderEntries(entry))
    }

    private fun findMatchingEntry(context: Context, foldingFeature: androidx.window.layout.FoldingFeature? = null): CustomSspEntry? {
        val configuration = context.resources.configuration
        val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)

        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        return sortedCustomEntries.firstOrNull { entry ->
            val qualifierEntry = entry.dpQualifierEntry
            val uiModeMatch = entry.uiModeType == null || entry.uiModeType == currentUiModeType
            
            val orientationMatch = when (entry.orientation) {
                Orientation.DEFAULT -> true
                Orientation.LANDSCAPE -> isLandscape
                Orientation.PORTRAIT -> isPortrait
            }

            if (qualifierEntry != null) {
                val screenQualifierValue = when (qualifierEntry.type) {
                    DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp.toFloat()
                    DpQualifier.HEIGHT -> configuration.screenHeightDp.toFloat()
                    DpQualifier.WIDTH -> configuration.screenWidthDp.toFloat()
                }
                val qualifierMatch = screenQualifierValue >= qualifierEntry.value

                if (entry.priority == 1 && uiModeMatch && qualifierMatch && orientationMatch) return@firstOrNull true
                if (entry.priority == 3 && qualifierMatch && orientationMatch) return@firstOrNull true

                return@firstOrNull false
            } else {
                // EN Priority 2: Must match only uiModeMatch AND orientationMatch (without Dp qualifier).
                // PT Prioridade 2: Deve casar apenas uiModeMatch E orientationMatch (sem qualificador de Dp).
                if (entry.priority == 2 && uiModeMatch && orientationMatch) return@firstOrNull true

                // EN Priority 4: Must match only orientationMatch (without Dp qualifier).
                // PT Prioridade 4: Deve casar apenas orientationMatch (sem qualificador de Dp).
                if (entry.priority == 4 && orientationMatch) return@firstOrNull true

                return@firstOrNull false
            }
        }
    }

    private fun resolve(context: Context, qualifier: DpQualifier, fontScale: Boolean, foldingFeature: androidx.window.layout.FoldingFeature?): Float {
        val foundEntry = findMatchingEntry(context, foldingFeature)
        val valueToUse = foundEntry?.customValue ?: initialBaseValue
        val actualQualifier = foundEntry?.finalQualifierResolver ?: qualifier
        val finalFontScale = foundEntry?.fontScale ?: fontScale
        return DimenSsp.getDimensionInPx(context, actualQualifier, valueToUse, finalFontScale, foundEntry?.inverter ?: Inverter.DEFAULT)
    }

    private fun resolveRes(context: Context, qualifier: DpQualifier, foldingFeature: androidx.window.layout.FoldingFeature?): Int {
        val foundEntry = findMatchingEntry(context, foldingFeature)
        val valueToUse = foundEntry?.customValue ?: initialBaseValue
        val actualQualifier = foundEntry?.finalQualifierResolver ?: qualifier
        return DimenSsp.getResourceId(context, actualQualifier, valueToUse, foundEntry?.inverter ?: Inverter.DEFAULT)
    }

    /**
     * EN Final dimension value resolved in pixels.
     * PT Valor da dimensão final resolvida em pixels.
     *
     * @param context Application context
     * @param fontScale Whether to respect the user's font scaling settings.
     * @param foldingFeature Optional Jetpack WindowManager FoldingFeature to accurately detect foldable states.
     */
    @JvmOverloads
    fun ssp(context: Context, fontScale: Boolean = true, foldingFeature: androidx.window.layout.FoldingFeature? = null): Float = resolve(context, DpQualifier.SMALL_WIDTH, fontScale, foldingFeature)
    
    /**
     * EN Final dimension value resolved in pixels.
     * PT Valor da dimensão final resolvida em pixels.
     *
     * @param context Application context
     * @param fontScale Whether to respect the user's font scaling settings.
     * @param foldingFeature Optional Jetpack WindowManager FoldingFeature to accurately detect foldable states.
     */
    @JvmOverloads
    fun hsp(context: Context, fontScale: Boolean = true, foldingFeature: androidx.window.layout.FoldingFeature? = null): Float = resolve(context, DpQualifier.HEIGHT, fontScale, foldingFeature)
    
    /**
     * EN Final dimension value resolved in pixels.
     * PT Valor da dimensão final resolvida em pixels.
     *
     * @param context Application context
     * @param fontScale Whether to respect the user's font scaling settings.
     * @param foldingFeature Optional Jetpack WindowManager FoldingFeature to accurately detect foldable states.
     */
    @JvmOverloads
    fun wsp(context: Context, fontScale: Boolean = true, foldingFeature: androidx.window.layout.FoldingFeature? = null): Float = resolve(context, DpQualifier.WIDTH, fontScale, foldingFeature)
    
    /**
     * EN Final dimension value resolved as resource ID.
     * PT Valor da dimensão final resolvida como ID do recurso.
     *
     * @param context Application context
     * @param foldingFeature Optional Jetpack WindowManager FoldingFeature to accurately detect foldable states.
     */
    @JvmOverloads
    fun sspRes(context: Context, foldingFeature: androidx.window.layout.FoldingFeature? = null): Int = resolveRes(context, DpQualifier.SMALL_WIDTH, foldingFeature)
    
    /**
     * EN Final dimension value resolved as resource ID.
     * PT Valor da dimensão final resolvida como ID do recurso.
     *
     * @param context Application context
     * @param foldingFeature Optional Jetpack WindowManager FoldingFeature to accurately detect foldable states.
     */
    @JvmOverloads
    fun hspRes(context: Context, foldingFeature: androidx.window.layout.FoldingFeature? = null): Int = resolveRes(context, DpQualifier.HEIGHT, foldingFeature)
    
    /**
     * EN Final dimension value resolved as resource ID.
     * PT Valor da dimensão final resolvida como ID do recurso.
     *
     * @param context Application context
     * @param foldingFeature Optional Jetpack WindowManager FoldingFeature to accurately detect foldable states.
     */
    @JvmOverloads
    fun wspRes(context: Context, foldingFeature: androidx.window.layout.FoldingFeature? = null): Int = resolveRes(context, DpQualifier.WIDTH, foldingFeature)

    /**
     * EN Final dimension value resolved in pixels.
     * PT Valor da dimensão final resolvida em pixels.
     *
     * @param context Application context
     * @param foldingFeature Optional Jetpack WindowManager FoldingFeature to accurately detect foldable states.
     */
    @JvmOverloads
    fun sem(context: Context, foldingFeature: androidx.window.layout.FoldingFeature? = null): Float = resolve(context, DpQualifier.SMALL_WIDTH, false, foldingFeature)
    
    /**
     * EN Final dimension value resolved in pixels.
     * PT Valor da dimensão final resolvida em pixels.
     *
     * @param context Application context
     * @param foldingFeature Optional Jetpack WindowManager FoldingFeature to accurately detect foldable states.
     */
    @JvmOverloads
    fun hem(context: Context, foldingFeature: androidx.window.layout.FoldingFeature? = null): Float = resolve(context, DpQualifier.HEIGHT, false, foldingFeature)
    
    /**
     * EN Final dimension value resolved in pixels.
     * PT Valor da dimensão final resolvida em pixels.
     *
     * @param context Application context
     * @param foldingFeature Optional Jetpack WindowManager FoldingFeature to accurately detect foldable states.
     */
    @JvmOverloads
    fun wem(context: Context, foldingFeature: androidx.window.layout.FoldingFeature? = null): Float = resolve(context, DpQualifier.WIDTH, false, foldingFeature)
}
