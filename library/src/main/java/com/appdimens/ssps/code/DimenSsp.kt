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
import android.util.TypedValue
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.effectiveDpQualifier
import com.appdimens.ssps.common.Inverter
import com.appdimens.ssps.common.Orientation
import com.appdimens.ssps.common.UiModeType
import com.appdimens.ssps.core.AppDimensSspsFactors
import com.appdimens.ssps.core.DimenResourceIdCache
import kotlin.math.abs

/**
 * EN
 * Utility object for handling SSP (Scalable Sp) dimensions from code (non-Compose).
 * Reuses the existing XML DP resources (`_Nssp`, `_Nhsp`, `_Nwsp`) and converts
 * the resulting Dp value to a Sp (pixels) value, respecting or ignoring the system font scale.
 *
 * PT
 * Objeto utilitário para manipulação de dimensões SSP (Sp escalável) a partir de código (não-Compose).
 * Reutiliza os recursos XML de DP existentes (`_Nssp`, `_Nhsp`, `_Nwsp`) e converte
 * o valor Dp resultante para um valor Sp (pixels), respeitando ou ignorando a escala de fonte.
 */
object DimenSsp {
    private const val MIN_VALUE = 1
    private const val MAX_VALUE = 600

    /**
     * EN
     * Gets the dimension in pixels (Sp) from an SSP value.
     * Reads the DP XML resource and converts it to Sp pixels, respecting or ignoring the system font scale.
     * Optionally applies the same aspect-ratio multiplier as Compose `sspa`.
     *
     * PT
     * Obtém a dimensão em pixels (Sp) a partir de um valor SSP.
     * Lê o recurso XML de DP e converte para pixels Sp, respeitando ou ignorando a escala de fonte.
     * Opcionalmente aplica o mesmo multiplicador de aspect ratio do Compose `sspa`.
     *
     * @param context The application context.
     * @param dpQualifier DpQualifier (SMALL_WIDTH, HEIGHT, WIDTH).
     * @param value The SSP value (1 to 600).
     * @param inverter The inverter type (default is Inverter.DEFAULT).
     * @param fontScale Whether to include the system font scale. Default true.
     * @param applyAspectRatio When true, multiply resolved Sp pixels by the per-axis AR adjustment.
     * @return The dimension in Sp pixels. If the XML resource is missing, falls back to an unscaled Sp value.
     */
    @JvmStatic
    @JvmOverloads
    fun getDimensionInSpPx(
        context: Context,
        dpQualifier: DpQualifier,
        value: Int,
        inverter: Inverter = Inverter.DEFAULT,
        fontScale: Boolean = true,
        applyAspectRatio: Boolean = false,
    ): Float {
        if (value == 0) return 0f
        require(value in MIN_VALUE..MAX_VALUE) {
            "Value must be between $MIN_VALUE and $MAX_VALUE. Current value: $value"
        }
        val configuration = context.resources.configuration
        val actualQualifier = effectiveDpQualifier(configuration, dpQualifier, inverter)
        // EN Resolve once with the already-computed qualifier (avoid double inverter + getIdentifier).
        // PT Resolve uma vez com o qualificador já calculado (evita inverter + getIdentifier duplicados).
        val resourceId = resolveResourceId(context, actualQualifier, value)
        val metrics = context.resources.displayMetrics

        // EN Missing resource → unscaled Sp (parity with Compose `toDynamicScaledSp` fallback).
        // PT Recurso ausente → Sp sem escala XML (paridade com o fallback Compose).
        val dpValue = if (resourceId != 0) {
            context.resources.getDimension(resourceId) / metrics.density
        } else {
            value.toFloat()
        }

        // EN Same order as appdimens-sdps `DimenSsp.getDimensionInSpPx`: Sp px first, then AR multiply.
        // PT Mesma ordem do SDPS: px Sp primeiro, depois multiplicação AR.
        val baseSpPx = if (fontScale) {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dpValue, metrics)
        } else {
            // EN Bypasses font scale using density directly.
            // PT Ignora a escala de fonte usando a densidade diretamente.
            dpValue * metrics.density
        }
        if (!applyAspectRatio) return baseSpPx
        AppDimensSspsFactors.ensureUpToDate(context)
        return baseSpPx * AppDimensSspsFactors.adjustmentForQualifier(actualQualifier)
    }

    /**
     * EN
     * Gets the resource ID for an SSP value (delegates to DP resource naming).
     * The SP system reuses the same DP XML resources.
     *
     * PT
     * Obtém o ID do recurso para um valor SSP (delega para a nomenclatura do recurso DP).
     * O sistema SP reutiliza os mesmos recursos XML de DP.
     *
     * @param context The application context.
     * @param dpQualifier DpQualifier.
     * @param value The SSP value (1 to 600).
     * @param inverter The inverter type (default is Inverter.DEFAULT).
     * @return The resource ID, or 0 if not found.
     */
    @JvmStatic
    @JvmOverloads
    fun getResourceId(
        context: Context,
        dpQualifier: DpQualifier,
        value: Int,
        inverter: Inverter = Inverter.DEFAULT
    ): Int {
        if (value == 0) return 0
        val actualQualifier =
            effectiveDpQualifier(context.resources.configuration, dpQualifier, inverter)
        return resolveResourceId(context, actualQualifier, value)
    }

    /** EN Builds `_Nssp` / `_Nhsp` / `_Nwsp` and resolves via [DimenResourceIdCache]. PT Monta o nome e resolve via cache. */
    private fun resolveResourceId(context: Context, actualQualifier: DpQualifier, value: Int): Int {
        val safeValue = value.coerceIn(MIN_VALUE, MAX_VALUE)
        val suffix = when (actualQualifier) {
            DpQualifier.SMALL_WIDTH -> "ssp"
            DpQualifier.HEIGHT -> "hsp"
            DpQualifier.WIDTH -> "wsp"
        }
        val dimenName = "_${abs(safeValue)}$suffix"
        return DimenResourceIdCache.getOrResolve(
            context.resources,
            context.packageName,
            dimenName,
        )
    }

    // EN Quick-resolution methods.
    // PT Métodos de resolução rápida.

    /**
     * EN Quick resolution for Smallest Width (ssp). Usage: `DimenSsp.ssp(context, 16)`.
     * PT Resolução rápida para Smallest Width (ssp). Uso: `DimenSsp.ssp(context, 16)`.
     */
    @JvmStatic
    fun ssp(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value)

    /**
     * EN Quick resolution for ssp, portrait → Height.
     * PT Resolução rápida para ssp, retrato → Altura.
     */
    @JvmStatic
    fun sspPh(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_PH)

    /**
     * EN Quick resolution for ssp, landscape → Height.
     * PT Resolução rápida para ssp, paisagem → Altura.
     */
    @JvmStatic
    fun sspLh(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_LH)

    /**
     * EN Quick resolution for ssp, portrait → Width.
     * PT Resolução rápida para ssp, retrato → Largura.
     */
    @JvmStatic
    fun sspPw(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_PW)

    /**
     * EN Quick resolution for ssp, landscape → Width.
     * PT Resolução rápida para ssp, paisagem → Largura.
     */
    @JvmStatic
    fun sspLw(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_LW)

    /**
     * EN Quick resolution for Screen Height (hsp). Usage: `DimenSsp.hsp(context, 32)`.
     * PT Resolução rápida para Altura da Tela (hsp). Uso: `DimenSsp.hsp(context, 32)`.
     */
    @JvmStatic
    fun hsp(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value)

    /**
     * EN Quick resolution for hsp, landscape → Width.
     * PT Resolução rápida para hsp, paisagem → Largura.
     */
    @JvmStatic
    fun hspLw(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, Inverter.PH_TO_LW)

    /**
     * EN Quick resolution for hsp, portrait → Width.
     * PT Resolução rápida para hsp, retrato → Largura.
     */
    @JvmStatic
    fun hspPw(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, Inverter.LH_TO_PW)

    /**
     * EN Quick resolution for Screen Width (wsp). Usage: `DimenSsp.wsp(context, 100)`.
     * PT Resolução rápida para Largura da Tela (wsp). Uso: `DimenSsp.wsp(context, 100)`.
     */
    @JvmStatic
    fun wsp(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value)

    /**
     * EN Quick resolution for wsp, landscape → Height.
     * PT Resolução rápida para wsp, paisagem → Altura.
     */
    @JvmStatic
    fun wspLh(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, Inverter.PW_TO_LH)

    /**
     * EN Quick resolution for wsp, portrait → Height.
     * PT Resolução rápida para wsp, retrato → Altura.
     */
    @JvmStatic
    fun wspPh(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, Inverter.LW_TO_PH)

    @JvmStatic
    fun warmupSspsFactors(context: Context): Unit = AppDimensSspsFactors.warmup(context)

    @JvmStatic
    fun sspa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, applyAspectRatio = true)

    @JvmStatic
    fun sspia(context: Context, value: Int): Float = sspa(context, value)

    @JvmStatic
    fun sspPha(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_PH, applyAspectRatio = true)

    @JvmStatic
    fun sspPhia(context: Context, value: Int): Float = sspPha(context, value)

    @JvmStatic
    fun sspLha(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_LH, applyAspectRatio = true)

    @JvmStatic
    fun sspLhia(context: Context, value: Int): Float = sspLha(context, value)

    @JvmStatic
    fun sspPwa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_PW, applyAspectRatio = true)

    @JvmStatic
    fun sspPwia(context: Context, value: Int): Float = sspPwa(context, value)

    @JvmStatic
    fun sspLwa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_LW, applyAspectRatio = true)

    @JvmStatic
    fun sspLwia(context: Context, value: Int): Float = sspLwa(context, value)

    @JvmStatic
    fun hspa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, applyAspectRatio = true)

    @JvmStatic
    fun hspia(context: Context, value: Int): Float = hspa(context, value)

    @JvmStatic
    fun hspLwa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, Inverter.PH_TO_LW, applyAspectRatio = true)

    @JvmStatic
    fun hspLwia(context: Context, value: Int): Float = hspLwa(context, value)

    @JvmStatic
    fun hspPwa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, Inverter.LH_TO_PW, applyAspectRatio = true)

    @JvmStatic
    fun hspPwia(context: Context, value: Int): Float = hspPwa(context, value)

    @JvmStatic
    fun wspa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, applyAspectRatio = true)

    @JvmStatic
    fun wspia(context: Context, value: Int): Float = wspa(context, value)

    @JvmStatic
    fun wspLha(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, Inverter.PW_TO_LH, applyAspectRatio = true)

    @JvmStatic
    fun wspLhia(context: Context, value: Int): Float = wspLha(context, value)

    @JvmStatic
    fun wspPha(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, Inverter.LW_TO_PH, applyAspectRatio = true)

    @JvmStatic
    fun wspPhia(context: Context, value: Int): Float = wspPha(context, value)

    // EN WITHOUT FONT SCALE variants (sem escala de fonte).
    // PT Variantes SEM ESCALA DE FONTE.

    /** EN ssp without font scale. PT ssp sem escala de fonte. */
    @JvmStatic
    fun sem(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, fontScale = false)

    /** EN ssp without font scale, portrait → Height. PT ssp sem escala de fonte, retrato → Altura. */
    @JvmStatic
    fun semPh(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_PH, fontScale = false)

    /** EN ssp without font scale, landscape → Height. PT ssp sem escala de fonte, paisagem → Altura. */
    @JvmStatic
    fun semLh(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_LH, fontScale = false)

    /** EN ssp without font scale, portrait → Width. PT ssp sem escala de fonte, retrato → Largura. */
    @JvmStatic
    fun semPw(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_PW, fontScale = false)

    /** EN ssp without font scale, landscape → Width. PT ssp sem escala de fonte, paisagem → Largura. */
    @JvmStatic
    fun semLw(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_LW, fontScale = false)

    /** EN hsp without font scale. PT hsp sem escala de fonte. */
    @JvmStatic
    fun hem(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, fontScale = false)

    /** EN hsp without font scale, landscape → Width. PT hsp sem escala de fonte, paisagem → Largura. */
    @JvmStatic
    fun hemLw(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, Inverter.PH_TO_LW, fontScale = false)

    /** EN hsp without font scale, portrait → Width. PT hsp sem escala de fonte, retrato → Largura. */
    @JvmStatic
    fun hemPw(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, Inverter.LH_TO_PW, fontScale = false)

    /** EN wsp without font scale. PT wsp sem escala de fonte. */
    @JvmStatic
    fun wem(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, fontScale = false)

    /** EN wsp without font scale, landscape → Height. PT wsp sem escala de fonte, paisagem → Altura. */
    @JvmStatic
    fun wemLh(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, Inverter.PW_TO_LH, fontScale = false)

    /** EN wsp without font scale, portrait → Height. PT wsp sem escala de fonte, retrato → Altura. */
    @JvmStatic
    fun wemPh(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, Inverter.LW_TO_PH, fontScale = false)

    @JvmStatic
    fun sema(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun semia(context: Context, value: Int): Float = sema(context, value)

    @JvmStatic
    fun semPha(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_PH, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun semPhia(context: Context, value: Int): Float = semPha(context, value)

    @JvmStatic
    fun semLha(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_LH, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun semLhia(context: Context, value: Int): Float = semLha(context, value)

    @JvmStatic
    fun semPwa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_PW, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun semPwia(context: Context, value: Int): Float = semPwa(context, value)

    @JvmStatic
    fun semLwa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_LW, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun semLwia(context: Context, value: Int): Float = semLwa(context, value)

    @JvmStatic
    fun hema(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun hemia(context: Context, value: Int): Float = hema(context, value)

    @JvmStatic
    fun hemLwa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, Inverter.PH_TO_LW, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun hemLwia(context: Context, value: Int): Float = hemLwa(context, value)

    @JvmStatic
    fun hemPwa(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.HEIGHT, value, Inverter.LH_TO_PW, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun hemPwia(context: Context, value: Int): Float = hemPwa(context, value)

    @JvmStatic
    fun wema(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun wemia(context: Context, value: Int): Float = wema(context, value)

    @JvmStatic
    fun wemLha(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, Inverter.PW_TO_LH, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun wemLhia(context: Context, value: Int): Float = wemLha(context, value)

    @JvmStatic
    fun wemPha(context: Context, value: Int): Float =
        getDimensionInSpPx(context, DpQualifier.WIDTH, value, Inverter.LW_TO_PH, fontScale = false, applyAspectRatio = true)

    @JvmStatic
    fun wemPhia(context: Context, value: Int): Float = wemPha(context, value)

    // EN Resource ID variants.
    // PT Variantes de ID de recurso.

    /** EN Gets the resources id of ssp. PT Obtém o id de recurso do ssp. */
    @JvmStatic
    fun sspRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.SMALL_WIDTH, value)

    /** EN resource ID variant for ssp portrait to height. PT Id de recurso ssp retrato para altura. */
    @JvmStatic
    fun sspPhRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_PH)

    /** EN resource ID variant for ssp landscape to height. PT Id de recurso ssp paisagem para altura. */
    @JvmStatic
    fun sspLhRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_LH)

    /** EN resource ID variant for ssp portrait to width. PT Id de recurso ssp retrato para largura. */
    @JvmStatic
    fun sspPwRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_PW)

    /** EN resource ID variant for ssp landscape to width. PT Id de recurso ssp paisagem para largura. */
    @JvmStatic
    fun sspLwRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.SMALL_WIDTH, value, Inverter.SW_TO_LW)

    /** EN resource ID variant for hsp. PT Id de recurso hsp. */
    @JvmStatic
    fun hspRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.HEIGHT, value)

    /** EN resource ID variant for hsp landscape to width. PT Id de recurso hsp paisagem para largura. */
    @JvmStatic
    fun hspLwRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.HEIGHT, value, Inverter.PH_TO_LW)

    /** EN resource ID variant for hsp portrait to width. PT Id de recurso hsp retrato para largura. */
    @JvmStatic
    fun hspPwRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.HEIGHT, value, Inverter.LH_TO_PW)

    /** EN resource ID variant for wsp. PT Id de recurso wsp. */
    @JvmStatic
    fun wspRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.WIDTH, value)

    /** EN resource ID variant for wsp landscape to height. PT Id de recurso wsp paisagem para altura. */
    @JvmStatic
    fun wspLhRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.WIDTH, value, Inverter.PW_TO_LH)

    /** EN resource ID variant for wsp portrait to height. PT Id de recurso wsp retrato para altura. */
    @JvmStatic
    fun wspPhRes(context: Context, value: Int): Int = getResourceId(context, DpQualifier.WIDTH, value, Inverter.LW_TO_PH)

    /**
     * EN
     * Starts the build chain for the custom dimension ScaledSp from a base Int.
     * Usage example: `DimenSsp.scaled(16).screen(...)`.
     *
     * PT
     * Inicia a cadeia de construção para a dimensão customizada ScaledSp a partir de um Int base.
     * Exemplo de uso: `DimenSsp.scaled(16).screen(...)`.
     */
    @JvmStatic
    fun scaled(initialBaseValue: Int): ScaledSp = ScaledSp(initialBaseValue)

    // EN Rotation facilitator functions for Sp.
    // PT Funções facilitadoras de rotação para Sp.

    /**
     * EN Facilitator for ssp with rotation override. Usage: `DimenSsp.sspRotate(context, 30, 45, DpQualifier.SMALL_WIDTH)`.
     * PT Facilitador para ssp com substituição por rotação.
     */
    @JvmStatic
    @JvmOverloads
    fun sspRotate(
        context: Context,
        value: Int,
        rotationValue: Int,
        finalQualifierResolver: DpQualifier = DpQualifier.SMALL_WIDTH,
        orientation: Orientation = Orientation.LANDSCAPE,
        fontScale: Boolean = true
    ): Float {
        val configuration = context.resources.configuration
        val isTarget = when (orientation) {
            Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            else -> false
        }
        return if (isTarget) getDimensionInSpPx(context, finalQualifierResolver, rotationValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, fontScale = fontScale)
    }

    /**
     * EN Facilitator for hsp with rotation override.
     * PT Facilitador para hsp com substituição por rotação.
     */
    @JvmStatic
    @JvmOverloads
    fun hspRotate(
        context: Context,
        value: Int,
        rotationValue: Int,
        finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
        orientation: Orientation = Orientation.LANDSCAPE,
        fontScale: Boolean = true
    ): Float {
        val configuration = context.resources.configuration
        val isTarget = when (orientation) {
            Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            else -> false
        }
        return if (isTarget) getDimensionInSpPx(context, finalQualifierResolver, rotationValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.HEIGHT, value, fontScale = fontScale)
    }

    /**
     * EN Facilitator for wsp with rotation override.
     * PT Facilitador para wsp com substituição por rotação.
     */
    @JvmStatic
    @JvmOverloads
    fun wspRotate(
        context: Context,
        value: Int,
        rotationValue: Int,
        finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
        orientation: Orientation = Orientation.LANDSCAPE,
        fontScale: Boolean = true
    ): Float {
        val configuration = context.resources.configuration
        val isTarget = when (orientation) {
            Orientation.LANDSCAPE -> configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            Orientation.PORTRAIT -> configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            else -> false
        }
        return if (isTarget) getDimensionInSpPx(context, finalQualifierResolver, rotationValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.WIDTH, value, fontScale = fontScale)
    }

    // EN UiModeType facilitator functions for Sp.
    // PT Funções facilitadoras de UiModeType para Sp.

    /**
     * EN Facilitator for ssp with UiModeType override. Usage: `DimenSsp.sspMode(context, 30, 50, UiModeType.TELEVISION)`.
     * PT Facilitador para ssp com substituição por UiModeType.
     */
    @JvmStatic
    @JvmOverloads
    fun sspMode(
        context: Context,
        value: Int,
        modeValue: Int,
        uiModeType: UiModeType,
        foldingFeature: androidx.window.layout.FoldingFeature? = null,
        finalQualifierResolver: DpQualifier? = null,
        fontScale: Boolean = true
    ): Float {
        val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
        return if (currentUiModeType == uiModeType)
            getDimensionInSpPx(context, finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, modeValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, fontScale = fontScale)
    }

    /**
     * EN Facilitator for hsp with UiModeType override.
     * PT Facilitador para hsp com substituição por UiModeType.
     */
    @JvmStatic
    @JvmOverloads
    fun hspMode(
        context: Context,
        value: Int,
        modeValue: Int,
        uiModeType: UiModeType,
        foldingFeature: androidx.window.layout.FoldingFeature? = null,
        finalQualifierResolver: DpQualifier? = null,
        fontScale: Boolean = true
    ): Float {
        val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
        return if (currentUiModeType == uiModeType)
            getDimensionInSpPx(context, finalQualifierResolver ?: DpQualifier.HEIGHT, modeValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.HEIGHT, value, fontScale = fontScale)
    }

    /**
     * EN Facilitator for wsp with UiModeType override.
     * PT Facilitador para wsp com substituição por UiModeType.
     */
    @JvmStatic
    @JvmOverloads
    fun wspMode(
        context: Context,
        value: Int,
        modeValue: Int,
        uiModeType: UiModeType,
        foldingFeature: androidx.window.layout.FoldingFeature? = null,
        finalQualifierResolver: DpQualifier? = null,
        fontScale: Boolean = true
    ): Float {
        val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
        return if (currentUiModeType == uiModeType)
            getDimensionInSpPx(context, finalQualifierResolver ?: DpQualifier.WIDTH, modeValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.WIDTH, value, fontScale = fontScale)
    }

    // EN DpQualifier facilitator functions for Sp.
    // PT Funções facilitadoras de DpQualifier para Sp.

    /**
     * EN Facilitator for ssp with DpQualifier override.
     * PT Facilitador para ssp com substituição por DpQualifier.
     */
    @JvmStatic
    @JvmOverloads
    @SuppressLint("ConfigurationScreenWidthHeight")
    fun sspQualifier(
        context: Context,
        value: Int,
        qualifiedValue: Int,
        qualifierType: DpQualifier,
        qualifierValue: Int,
        finalQualifierResolver: DpQualifier? = null,
        fontScale: Boolean = true
    ): Float {
        val configuration = context.resources.configuration
        val screenValue = when (qualifierType) {
            DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp.toFloat()
            DpQualifier.HEIGHT -> configuration.screenHeightDp.toFloat()
            DpQualifier.WIDTH -> configuration.screenWidthDp.toFloat()
        }
        return if (screenValue >= qualifierValue)
            getDimensionInSpPx(context, finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, qualifiedValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, fontScale = fontScale)
    }

    /**
     * EN Facilitator for hsp with DpQualifier override.
     * PT Facilitador para hsp com substituição por DpQualifier.
     */
    @JvmStatic
    @JvmOverloads
    @SuppressLint("ConfigurationScreenWidthHeight")
    fun hspQualifier(
        context: Context,
        value: Int,
        qualifiedValue: Int,
        qualifierType: DpQualifier,
        qualifierValue: Int,
        finalQualifierResolver: DpQualifier? = null,
        fontScale: Boolean = true
    ): Float {
        val configuration = context.resources.configuration
        val screenValue = when (qualifierType) {
            DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp.toFloat()
            DpQualifier.HEIGHT -> configuration.screenHeightDp.toFloat()
            DpQualifier.WIDTH -> configuration.screenWidthDp.toFloat()
        }
        return if (screenValue >= qualifierValue)
            getDimensionInSpPx(context, finalQualifierResolver ?: DpQualifier.HEIGHT, qualifiedValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.HEIGHT, value, fontScale = fontScale)
    }

    /**
     * EN Facilitator for wsp with DpQualifier override.
     * PT Facilitador para wsp com substituição por DpQualifier.
     */
    @JvmStatic
    @JvmOverloads
    @SuppressLint("ConfigurationScreenWidthHeight")
    fun wspQualifier(
        context: Context,
        value: Int,
        qualifiedValue: Int,
        qualifierType: DpQualifier,
        qualifierValue: Int,
        finalQualifierResolver: DpQualifier? = null,
        fontScale: Boolean = true
    ): Float {
        val configuration = context.resources.configuration
        val screenValue = when (qualifierType) {
            DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp.toFloat()
            DpQualifier.HEIGHT -> configuration.screenHeightDp.toFloat()
            DpQualifier.WIDTH -> configuration.screenWidthDp.toFloat()
        }
        return if (screenValue >= qualifierValue)
            getDimensionInSpPx(context, finalQualifierResolver ?: DpQualifier.WIDTH, qualifiedValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.WIDTH, value, fontScale = fontScale)
    }

    // EN UiModeType + DpQualifier combined facilitator functions for Sp.
    // PT Funções facilitadoras combinadas UiModeType + DpQualifier para Sp.

    /**
     * EN Facilitator for ssp with combined UiModeType + DpQualifier override.
     * PT Facilitador para ssp com substituição combinada UiModeType + DpQualifier.
     */
    @JvmStatic
    @JvmOverloads
    @SuppressLint("ConfigurationScreenWidthHeight")
    fun sspScreen(
        context: Context,
        value: Int,
        screenValue: Int,
        uiModeType: UiModeType,
        qualifierType: DpQualifier,
        qualifierValue: Int,
        foldingFeature: androidx.window.layout.FoldingFeature? = null,
        finalQualifierResolver: DpQualifier? = null,
        fontScale: Boolean = true
    ): Float {
        val configuration = context.resources.configuration
        val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
        val qualifierScreenValue = when (qualifierType) {
            DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp.toFloat()
            DpQualifier.HEIGHT -> configuration.screenHeightDp.toFloat()
            DpQualifier.WIDTH -> configuration.screenWidthDp.toFloat()
        }
        return if (currentUiModeType == uiModeType && qualifierScreenValue >= qualifierValue)
            getDimensionInSpPx(context, finalQualifierResolver ?: DpQualifier.SMALL_WIDTH, screenValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.SMALL_WIDTH, value, fontScale = fontScale)
    }

    /**
     * EN Facilitator for hsp with combined UiModeType + DpQualifier override.
     * PT Facilitador para hsp com substituição combinada UiModeType + DpQualifier.
     */
    @JvmStatic
    @JvmOverloads
    @SuppressLint("ConfigurationScreenWidthHeight")
    fun hspScreen(
        context: Context,
        value: Int,
        screenValue: Int,
        uiModeType: UiModeType,
        qualifierType: DpQualifier,
        qualifierValue: Int,
        foldingFeature: androidx.window.layout.FoldingFeature? = null,
        finalQualifierResolver: DpQualifier? = null,
        fontScale: Boolean = true
    ): Float {
        val configuration = context.resources.configuration
        val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
        val qualifierScreenValue = when (qualifierType) {
            DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp.toFloat()
            DpQualifier.HEIGHT -> configuration.screenHeightDp.toFloat()
            DpQualifier.WIDTH -> configuration.screenWidthDp.toFloat()
        }
        return if (currentUiModeType == uiModeType && qualifierScreenValue >= qualifierValue)
            getDimensionInSpPx(context, finalQualifierResolver ?: DpQualifier.HEIGHT, screenValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.HEIGHT, value, fontScale = fontScale)
    }

    /**
     * EN Facilitator for wsp with combined UiModeType + DpQualifier override.
     * PT Facilitador para wsp com substituição combinada UiModeType + DpQualifier.
     */
    @JvmStatic
    @JvmOverloads
    @SuppressLint("ConfigurationScreenWidthHeight")
    fun wspScreen(
        context: Context,
        value: Int,
        screenValue: Int,
        uiModeType: UiModeType,
        qualifierType: DpQualifier,
        qualifierValue: Int,
        foldingFeature: androidx.window.layout.FoldingFeature? = null,
        finalQualifierResolver: DpQualifier? = null,
        fontScale: Boolean = true
    ): Float {
        val configuration = context.resources.configuration
        val currentUiModeType = UiModeType.fromConfiguration(context, foldingFeature)
        val qualifierScreenValue = when (qualifierType) {
            DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp.toFloat()
            DpQualifier.HEIGHT -> configuration.screenHeightDp.toFloat()
            DpQualifier.WIDTH -> configuration.screenWidthDp.toFloat()
        }
        return if (currentUiModeType == uiModeType && qualifierScreenValue >= qualifierValue)
            getDimensionInSpPx(context, finalQualifierResolver ?: DpQualifier.WIDTH, screenValue, fontScale = fontScale)
        else getDimensionInSpPx(context, DpQualifier.WIDTH, value, fontScale = fontScale)
    }
}
