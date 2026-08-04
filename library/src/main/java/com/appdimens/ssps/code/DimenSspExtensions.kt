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

import android.content.Context
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.Orientation
import com.appdimens.ssps.common.UiModeType

// EN Extension functions for quick dynamic text scaling (Sp) from code (non-Compose).
// PT Funções de extensão para escalonamento dinâmico rápido de texto (Sp) a partir de código (não-Compose).

/**
 * EN Extension for Smallest Width (ssp).
 * PT Extensão para Smallest Width (ssp).
 * @see DimenSsp.ssp
 */
fun Int.ssp(context: Context): Float = DimenSsp.ssp(context, this)

/**
 * EN Extension for ssp, portrait -> Height.
 * PT Extensão para ssp, retrato -> Altura.
 * @see DimenSsp.sspPh
 */
fun Int.sspPh(context: Context): Float = DimenSsp.sspPh(context, this)

/**
 * EN Extension for ssp, landscape -> Height.
 * PT Extensão para ssp, paisagem -> Altura.
 * @see DimenSsp.sspLh
 */
fun Int.sspLh(context: Context): Float = DimenSsp.sspLh(context, this)

/**
 * EN Extension for ssp, portrait -> Width.
 * PT Extensão para ssp, retrato -> Largura.
 * @see DimenSsp.sspPw
 */
fun Int.sspPw(context: Context): Float = DimenSsp.sspPw(context, this)

/**
 * EN Extension for ssp, landscape -> Width.
 * PT Extensão para ssp, paisagem -> Largura.
 * @see DimenSsp.sspLw
 */
fun Int.sspLw(context: Context): Float = DimenSsp.sspLw(context, this)

/**
 * EN Extension for Screen Height (hsp).
 * PT Extensão para Altura da Tela (hsp).
 * @see DimenSsp.hsp
 */
fun Int.hsp(context: Context): Float = DimenSsp.hsp(context, this)

/**
 * EN Extension for hsp, landscape -> Width.
 * PT Extensão para hsp, paisagem -> Largura.
 * @see DimenSsp.hspLw
 */
fun Int.hspLw(context: Context): Float = DimenSsp.hspLw(context, this)

/**
 * EN Extension for hsp, portrait -> Width.
 * PT Extensão para hsp, retrato -> Largura.
 * @see DimenSsp.hspPw
 */
fun Int.hspPw(context: Context): Float = DimenSsp.hspPw(context, this)

/**
 * EN Extension for Screen Width (wsp).
 * PT Extensão para Largura da Tela (wsp).
 * @see DimenSsp.wsp
 */
fun Int.wsp(context: Context): Float = DimenSsp.wsp(context, this)

/**
 * EN Extension for wsp, landscape -> Height.
 * PT Extensão para wsp, paisagem -> Altura.
 * @see DimenSsp.wspLh
 */
fun Int.wspLh(context: Context): Float = DimenSsp.wspLh(context, this)

/**
 * EN Extension for wsp, portrait -> Height.
 * PT Extensão para wsp, retrato -> Altura.
 * @see DimenSsp.wspPh
 */
fun Int.wspPh(context: Context): Float = DimenSsp.wspPh(context, this)

// Aspect-ratio-aware variants (*a / *ia).

private fun Number.toSspIndex(): Int =
    kotlin.math.round(this.toDouble()).toInt().coerceIn(1, 600)

fun Number.sspa(context: Context): Float = this.toSspIndex().let { DimenSsp.sspa(context, it) }
fun Number.sspia(context: Context): Float = sspa(context)

fun Int.sspa(context: Context): Float = DimenSsp.sspa(context, this.coerceIn(1, 600))
fun Int.sspia(context: Context): Float = DimenSsp.sspia(context, this.coerceIn(1, 600))
fun Float.sspa(context: Context): Float = this.toSspIndex().let { DimenSsp.sspa(context, it) }
fun Float.sspia(context: Context): Float = sspa(context)

fun Number.sspPha(context: Context): Float = this.toSspIndex().let { DimenSsp.sspPha(context, it) }
fun Number.sspPhia(context: Context): Float = sspPha(context)
fun Number.sspLha(context: Context): Float = this.toSspIndex().let { DimenSsp.sspLha(context, it) }
fun Number.sspLhia(context: Context): Float = sspLha(context)
fun Number.sspPwa(context: Context): Float = this.toSspIndex().let { DimenSsp.sspPwa(context, it) }
fun Number.sspPwia(context: Context): Float = sspPwa(context)
fun Number.sspLwa(context: Context): Float = this.toSspIndex().let { DimenSsp.sspLwa(context, it) }
fun Number.sspLwia(context: Context): Float = sspLwa(context)

fun Number.hspa(context: Context): Float = this.toSspIndex().let { DimenSsp.hspa(context, it) }
fun Number.hspia(context: Context): Float = hspa(context)
fun Int.hspa(context: Context): Float = DimenSsp.hspa(context, this.coerceIn(1, 600))
fun Int.hspia(context: Context): Float = DimenSsp.hspia(context, this.coerceIn(1, 600))
fun Float.hspa(context: Context): Float = this.toSspIndex().let { DimenSsp.hspa(context, it) }
fun Float.hspia(context: Context): Float = hspa(context)
fun Number.hspLwa(context: Context): Float = this.toSspIndex().let { DimenSsp.hspLwa(context, it) }
fun Number.hspLwia(context: Context): Float = hspLwa(context)
fun Number.hspPwa(context: Context): Float = this.toSspIndex().let { DimenSsp.hspPwa(context, it) }
fun Number.hspPwia(context: Context): Float = hspPwa(context)

fun Number.wspa(context: Context): Float = this.toSspIndex().let { DimenSsp.wspa(context, it) }
fun Number.wspia(context: Context): Float = wspa(context)
fun Int.wspa(context: Context): Float = DimenSsp.wspa(context, this.coerceIn(1, 600))
fun Int.wspia(context: Context): Float = DimenSsp.wspia(context, this.coerceIn(1, 600))
fun Float.wspa(context: Context): Float = this.toSspIndex().let { DimenSsp.wspa(context, it) }
fun Float.wspia(context: Context): Float = wspa(context)
fun Number.wspLha(context: Context): Float = this.toSspIndex().let { DimenSsp.wspLha(context, it) }
fun Number.wspLhia(context: Context): Float = wspLha(context)
fun Number.wspPha(context: Context): Float = this.toSspIndex().let { DimenSsp.wspPha(context, it) }
fun Number.wspPhia(context: Context): Float = wspPha(context)

fun Int.sspPha(context: Context): Float = DimenSsp.sspPha(context, coerceIn(1, 600))
fun Int.sspPhia(context: Context): Float = sspPha(context)
fun Int.sspLha(context: Context): Float = DimenSsp.sspLha(context, coerceIn(1, 600))
fun Int.sspLhia(context: Context): Float = sspLha(context)
fun Int.sspPwa(context: Context): Float = DimenSsp.sspPwa(context, coerceIn(1, 600))
fun Int.sspPwia(context: Context): Float = sspPwa(context)
fun Int.sspLwa(context: Context): Float = DimenSsp.sspLwa(context, coerceIn(1, 600))
fun Int.sspLwia(context: Context): Float = sspLwa(context)
fun Int.hspLwa(context: Context): Float = DimenSsp.hspLwa(context, coerceIn(1, 600))
fun Int.hspLwia(context: Context): Float = hspLwa(context)
fun Int.hspPwa(context: Context): Float = DimenSsp.hspPwa(context, coerceIn(1, 600))
fun Int.hspPwia(context: Context): Float = hspPwa(context)
fun Int.wspLha(context: Context): Float = DimenSsp.wspLha(context, coerceIn(1, 600))
fun Int.wspLhia(context: Context): Float = wspLha(context)
fun Int.wspPha(context: Context): Float = DimenSsp.wspPha(context, coerceIn(1, 600))
fun Int.wspPhia(context: Context): Float = wspPha(context)

// EN WITHOUT FONT SCALE variants
// PT Variantes SEM ESCALA DE FONTE

/**
 * EN Extension for ssp without font scale.
 * PT Extensão para ssp sem escala de fonte.
 * @see DimenSsp.sem
 */
fun Int.sem(context: Context): Float = DimenSsp.sem(context, this)

/**
 * EN Extension for ssp without font scale, portrait -> Height.
 * PT Extensão para ssp sem escala de fonte, retrato -> Altura.
 * @see DimenSsp.semPh
 */
fun Int.semPh(context: Context): Float = DimenSsp.semPh(context, this)

/**
 * EN Extension for ssp without font scale, landscape -> Height.
 * PT Extensão para ssp sem escala de fonte, paisagem -> Altura.
 * @see DimenSsp.semLh
 */
fun Int.semLh(context: Context): Float = DimenSsp.semLh(context, this)

/**
 * EN Extension for ssp without font scale, portrait -> Width.
 * PT Extensão para ssp sem escala de fonte, retrato -> Largura.
 * @see DimenSsp.semPw
 */
fun Int.semPw(context: Context): Float = DimenSsp.semPw(context, this)

/**
 * EN Extension for ssp without font scale, landscape -> Width.
 * PT Extensão para ssp sem escala de fonte, paisagem -> Largura.
 * @see DimenSsp.semLw
 */
fun Int.semLw(context: Context): Float = DimenSsp.semLw(context, this)

/**
 * EN Extension for hsp without font scale.
 * PT Extensão para hsp sem escala de fonte.
 * @see DimenSsp.hem
 */
fun Int.hem(context: Context): Float = DimenSsp.hem(context, this)

/**
 * EN Extension for hsp without font scale, landscape -> Width.
 * PT Extensão para hsp sem escala de fonte, paisagem -> Largura.
 * @see DimenSsp.hemLw
 */
fun Int.hemLw(context: Context): Float = DimenSsp.hemLw(context, this)

/**
 * EN Extension for hsp without font scale, portrait -> Width.
 * PT Extensão para hsp sem escala de fonte, retrato -> Largura.
 * @see DimenSsp.hemPw
 */
fun Int.hemPw(context: Context): Float = DimenSsp.hemPw(context, this)

/**
 * EN Extension for wsp without font scale.
 * PT Extensão para wsp sem escala de fonte.
 * @see DimenSsp.wem
 */
fun Int.wem(context: Context): Float = DimenSsp.wem(context, this)

/**
 * EN Extension for wsp without font scale, landscape -> Height.
 * PT Extensão para wsp sem escala de fonte, paisagem -> Altura.
 * @see DimenSsp.wemLh
 */
fun Int.wemLh(context: Context): Float = DimenSsp.wemLh(context, this)

/**
 * EN Extension for wsp without font scale, portrait -> Height.
 * PT Extensão para wsp sem escala de fonte, retrato -> Altura.
 * @see DimenSsp.wemPh
 */
fun Int.wemPh(context: Context): Float = DimenSsp.wemPh(context, this)

// Aspect-ratio + sem/hem/wem (no system font scale in DimenSsp path).

fun Number.sema(context: Context): Float = this.toSspIndex().let { DimenSsp.sema(context, it) }
fun Number.semia(context: Context): Float = sema(context)
fun Int.sema(context: Context): Float = DimenSsp.sema(context, this.coerceIn(1, 600))
fun Int.semia(context: Context): Float = DimenSsp.semia(context, this.coerceIn(1, 600))
fun Float.sema(context: Context): Float = this.toSspIndex().let { DimenSsp.sema(context, it) }
fun Float.semia(context: Context): Float = sema(context)
fun Number.semPha(context: Context): Float = this.toSspIndex().let { DimenSsp.semPha(context, it) }
fun Number.semPhia(context: Context): Float = semPha(context)
fun Number.semLha(context: Context): Float = this.toSspIndex().let { DimenSsp.semLha(context, it) }
fun Number.semLhia(context: Context): Float = semLha(context)
fun Number.semPwa(context: Context): Float = this.toSspIndex().let { DimenSsp.semPwa(context, it) }
fun Number.semPwia(context: Context): Float = semPwa(context)
fun Number.semLwa(context: Context): Float = this.toSspIndex().let { DimenSsp.semLwa(context, it) }
fun Number.semLwia(context: Context): Float = semLwa(context)

fun Number.hema(context: Context): Float = this.toSspIndex().let { DimenSsp.hema(context, it) }
fun Number.hemia(context: Context): Float = hema(context)
fun Int.hema(context: Context): Float = DimenSsp.hema(context, this.coerceIn(1, 600))
fun Int.hemia(context: Context): Float = DimenSsp.hemia(context, this.coerceIn(1, 600))
fun Float.hema(context: Context): Float = this.toSspIndex().let { DimenSsp.hema(context, it) }
fun Float.hemia(context: Context): Float = hema(context)
fun Number.hemLwa(context: Context): Float = this.toSspIndex().let { DimenSsp.hemLwa(context, it) }
fun Number.hemLwia(context: Context): Float = hemLwa(context)
fun Number.hemPwa(context: Context): Float = this.toSspIndex().let { DimenSsp.hemPwa(context, it) }
fun Number.hemPwia(context: Context): Float = hemPwa(context)

fun Number.wema(context: Context): Float = this.toSspIndex().let { DimenSsp.wema(context, it) }
fun Number.wemia(context: Context): Float = wema(context)
fun Int.wema(context: Context): Float = DimenSsp.wema(context, this.coerceIn(1, 600))
fun Int.wemia(context: Context): Float = DimenSsp.wemia(context, this.coerceIn(1, 600))
fun Float.wema(context: Context): Float = this.toSspIndex().let { DimenSsp.wema(context, it) }
fun Float.wemia(context: Context): Float = wema(context)
fun Number.wemLha(context: Context): Float = this.toSspIndex().let { DimenSsp.wemLha(context, it) }
fun Number.wemLhia(context: Context): Float = wemLha(context)
fun Number.wemPha(context: Context): Float = this.toSspIndex().let { DimenSsp.wemPha(context, it) }
fun Number.wemPhia(context: Context): Float = wemPha(context)

fun Int.semPha(context: Context): Float = DimenSsp.semPha(context, coerceIn(1, 600))
fun Int.semPhia(context: Context): Float = semPha(context)
fun Int.semLha(context: Context): Float = DimenSsp.semLha(context, coerceIn(1, 600))
fun Int.semLhia(context: Context): Float = semLha(context)
fun Int.semPwa(context: Context): Float = DimenSsp.semPwa(context, coerceIn(1, 600))
fun Int.semPwia(context: Context): Float = semPwa(context)
fun Int.semLwa(context: Context): Float = DimenSsp.semLwa(context, coerceIn(1, 600))
fun Int.semLwia(context: Context): Float = semLwa(context)
fun Int.hemLwa(context: Context): Float = DimenSsp.hemLwa(context, coerceIn(1, 600))
fun Int.hemLwia(context: Context): Float = hemLwa(context)
fun Int.hemPwa(context: Context): Float = DimenSsp.hemPwa(context, coerceIn(1, 600))
fun Int.hemPwia(context: Context): Float = hemPwa(context)
fun Int.wemLha(context: Context): Float = DimenSsp.wemLha(context, coerceIn(1, 600))
fun Int.wemLhia(context: Context): Float = wemLha(context)
fun Int.wemPha(context: Context): Float = DimenSsp.wemPha(context, coerceIn(1, 600))
fun Int.wemPhia(context: Context): Float = wemPha(context)

// EN Resource ID variants
// PT Variantes que retornam o ID de recurso

/**
 * EN Extension to get ssp resource ID.
 * PT Extensão para obter o ID de recurso do ssp.
 * @see DimenSsp.sspRes
 */
fun Int.sspRes(context: Context): Int = DimenSsp.sspRes(context, this)

/**
 * EN Extension to get ssp resource ID, portrait -> Height.
 * PT Extensão para obter o ID de recurso ssp, retrato -> Altura.
 * @see DimenSsp.sspPhRes
 */
fun Int.sspPhRes(context: Context): Int = DimenSsp.sspPhRes(context, this)

/**
 * EN Extension to get ssp resource ID, landscape -> Height.
 * PT Extensão para obter o ID de recurso ssp, paisagem -> Altura.
 * @see DimenSsp.sspLhRes
 */
fun Int.sspLhRes(context: Context): Int = DimenSsp.sspLhRes(context, this)

/**
 * EN Extension to get ssp resource ID, portrait -> Width.
 * PT Extensão para obter o ID de recurso ssp, retrato -> Largura.
 * @see DimenSsp.sspPwRes
 */
fun Int.sspPwRes(context: Context): Int = DimenSsp.sspPwRes(context, this)

/**
 * EN Extension to get ssp resource ID, landscape -> Width.
 * PT Extensão para obter o ID de recurso ssp, paisagem -> Largura.
 * @see DimenSsp.sspLwRes
 */
fun Int.sspLwRes(context: Context): Int = DimenSsp.sspLwRes(context, this)

/**
 * EN Extension to get hsp resource ID.
 * PT Extensão para obter o ID de recurso do hsp.
 * @see DimenSsp.hspRes
 */
fun Int.hspRes(context: Context): Int = DimenSsp.hspRes(context, this)

/**
 * EN Extension to get hsp resource ID, landscape -> Width.
 * PT Extensão para obter o ID de recurso hsp, paisagem -> Largura.
 * @see DimenSsp.hspLwRes
 */
fun Int.hspLwRes(context: Context): Int = DimenSsp.hspLwRes(context, this)

/**
 * EN Extension to get hsp resource ID, portrait -> Width.
 * PT Extensão para obter o ID de recurso hsp, retrato -> Largura.
 * @see DimenSsp.hspPwRes
 */
fun Int.hspPwRes(context: Context): Int = DimenSsp.hspPwRes(context, this)

/**
 * EN Extension to get wsp resource ID.
 * PT Extensão para obter o ID de recurso do wsp.
 * @see DimenSsp.wspRes
 */
fun Int.wspRes(context: Context): Int = DimenSsp.wspRes(context, this)

/**
 * EN Extension to get wsp resource ID, landscape -> Height.
 * PT Extensão para obter o ID de recurso wsp, paisagem -> Altura.
 * @see DimenSsp.wspLhRes
 */
fun Int.wspLhRes(context: Context): Int = DimenSsp.wspLhRes(context, this)

/**
 * EN Extension to get wsp resource ID, portrait -> Height.
 * PT Extensão para obter o ID de recurso wsp, retrato -> Altura.
 * @see DimenSsp.wspPhRes
 */
fun Int.wspPhRes(context: Context): Int = DimenSsp.wspPhRes(context, this)


// EN Rotation facilitator extensions for Sp from code.
// PT Extensões facilitadoras de rotação para Sp de código.

/**
 * EN Extension for ssp with rotation override.
 * PT Extensão para ssp com substituição por rotação.
 * @see DimenSsp.sspRotate
 */
fun Int.sspRotate(
    context: Context,
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.SMALL_WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float = DimenSsp.sspRotate(context, this, rotationValue, finalQualifierResolver, orientation, fontScale)

/**
 * EN Extension for hsp with rotation override.
 * PT Extensão para hsp com substituição por rotação.
 * @see DimenSsp.hspRotate
 */
fun Int.hspRotate(
    context: Context,
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float = DimenSsp.hspRotate(context, this, rotationValue, finalQualifierResolver, orientation, fontScale)

/**
 * EN Extension for wsp with rotation override.
 * PT Extensão para wsp com substituição por rotação.
 * @see DimenSsp.wspRotate
 */
fun Int.wspRotate(
    context: Context,
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float = DimenSsp.wspRotate(context, this, rotationValue, finalQualifierResolver, orientation, fontScale)

// EN UiModeType facilitator extensions for Sp from code.
// PT Extensões facilitadoras de UiModeType para Sp de código.

/**
 * EN Extension for ssp with UiModeType override.
 * PT Extensão para ssp com substituição por UiModeType.
 * @see DimenSsp.sspMode
 */
fun Int.sspMode(
    context: Context,
    modeValue: Int,
    uiModeType: UiModeType,
    foldingFeature: androidx.window.layout.FoldingFeature? = null,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.sspMode(context, this, modeValue, uiModeType, foldingFeature, finalQualifierResolver, fontScale)

/**
 * EN Extension for hsp with UiModeType override.
 * PT Extensão para hsp com substituição por UiModeType.
 * @see DimenSsp.hspMode
 */
fun Int.hspMode(
    context: Context,
    modeValue: Int,
    uiModeType: UiModeType,
    foldingFeature: androidx.window.layout.FoldingFeature? = null,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.hspMode(context, this, modeValue, uiModeType, foldingFeature, finalQualifierResolver, fontScale)

/**
 * EN Extension for wsp with UiModeType override.
 * PT Extensão para wsp com substituição por UiModeType.
 * @see DimenSsp.wspMode
 */
fun Int.wspMode(
    context: Context,
    modeValue: Int,
    uiModeType: UiModeType,
    foldingFeature: androidx.window.layout.FoldingFeature? = null,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.wspMode(context, this, modeValue, uiModeType, foldingFeature, finalQualifierResolver, fontScale)

// EN DpQualifier facilitator extensions for Sp from code.
// PT Funções facilitadoras de DpQualifier para Sp de código.

/**
 * EN Extension for ssp with DpQualifier override.
 * PT Extensão para ssp com substituição por DpQualifier.
 * @see DimenSsp.sspQualifier
 */
fun Int.sspQualifier(
    context: Context,
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.sspQualifier(context, this, qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale)

/**
 * EN Extension for hsp with DpQualifier override.
 * PT Extensão para hsp com substituição por DpQualifier.
 * @see DimenSsp.hspQualifier
 */
fun Int.hspQualifier(
    context: Context,
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.hspQualifier(context, this, qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale)

/**
 * EN Extension for wsp with DpQualifier override.
 * PT Extensão para wsp com substituição por DpQualifier.
 * @see DimenSsp.wspQualifier
 */
fun Int.wspQualifier(
    context: Context,
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.wspQualifier(context, this, qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale)

// EN UiModeType + DpQualifier combined facilitator extensions for Sp from code.
// PT Extensões facilitadoras combinadas UiModeType + DpQualifier para Sp de código.

/**
 * EN Extension for ssp with combined UiModeType + DpQualifier override.
 * PT Extensão para ssp com substituição combinada UiModeType + DpQualifier.
 * @see DimenSsp.sspScreen
 */
fun Int.sspScreen(
    context: Context,
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    foldingFeature: androidx.window.layout.FoldingFeature? = null,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.sspScreen(context, this, screenValue, uiModeType, qualifierType, qualifierValue, foldingFeature, finalQualifierResolver, fontScale)

/**
 * EN Extension for hsp with combined UiModeType + DpQualifier override.
 * PT Extensão para hsp com substituição combinada UiModeType + DpQualifier.
 * @see DimenSsp.hspScreen
 */
fun Int.hspScreen(
    context: Context,
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    foldingFeature: androidx.window.layout.FoldingFeature? = null,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.hspScreen(context, this, screenValue, uiModeType, qualifierType, qualifierValue, foldingFeature, finalQualifierResolver, fontScale)

/**
 * EN Extension for wsp with combined UiModeType + DpQualifier override.
 * PT Extensão para wsp com substituição combinada UiModeType + DpQualifier.
 * @see DimenSsp.wspScreen
 */
fun Int.wspScreen(
    context: Context,
    screenValue: Int,
    uiModeType: UiModeType,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    foldingFeature: androidx.window.layout.FoldingFeature? = null,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.wspScreen(context, this, screenValue, uiModeType, qualifierType, qualifierValue, foldingFeature, finalQualifierResolver, fontScale)
