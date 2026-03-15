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

/** @see DimenSsp.ssp */
fun Int.ssp(context: Context): Float = DimenSsp.ssp(context, this)

/** @see DimenSsp.sspPh */
fun Int.sspPh(context: Context): Float = DimenSsp.sspPh(context, this)

/** @see DimenSsp.sspLh */
fun Int.sspLh(context: Context): Float = DimenSsp.sspLh(context, this)

/** @see DimenSsp.sspPw */
fun Int.sspPw(context: Context): Float = DimenSsp.sspPw(context, this)

/** @see DimenSsp.sspLw */
fun Int.sspLw(context: Context): Float = DimenSsp.sspLw(context, this)

/** @see DimenSsp.hsp */
fun Int.hsp(context: Context): Float = DimenSsp.hsp(context, this)

/** @see DimenSsp.hspLw */
fun Int.hspLw(context: Context): Float = DimenSsp.hspLw(context, this)

/** @see DimenSsp.hspPw */
fun Int.hspPw(context: Context): Float = DimenSsp.hspPw(context, this)

/** @see DimenSsp.wsp */
fun Int.wsp(context: Context): Float = DimenSsp.wsp(context, this)

/** @see DimenSsp.wspLh */
fun Int.wspLh(context: Context): Float = DimenSsp.wspLh(context, this)

/** @see DimenSsp.wspPh */
fun Int.wspPh(context: Context): Float = DimenSsp.wspPh(context, this)

// EN WITHOUT FONT SCALE variants
// PT Variantes SEM ESCALA DE FONTE

/** @see DimenSsp.sem */
fun Int.sem(context: Context): Float = DimenSsp.sem(context, this)

/** @see DimenSsp.semPh */
fun Int.semPh(context: Context): Float = DimenSsp.semPh(context, this)

/** @see DimenSsp.semLh */
fun Int.semLh(context: Context): Float = DimenSsp.semLh(context, this)

/** @see DimenSsp.semPw */
fun Int.semPw(context: Context): Float = DimenSsp.semPw(context, this)

/** @see DimenSsp.semLw */
fun Int.semLw(context: Context): Float = DimenSsp.semLw(context, this)

/** @see DimenSsp.hem */
fun Int.hem(context: Context): Float = DimenSsp.hem(context, this)

/** @see DimenSsp.hemLw */
fun Int.hemLw(context: Context): Float = DimenSsp.hemLw(context, this)

/** @see DimenSsp.hemPw */
fun Int.hemPw(context: Context): Float = DimenSsp.hemPw(context, this)

/** @see DimenSsp.wem */
fun Int.wem(context: Context): Float = DimenSsp.wem(context, this)

/** @see DimenSsp.wemLh */
fun Int.wemLh(context: Context): Float = DimenSsp.wemLh(context, this)

/** @see DimenSsp.wemPh */
fun Int.wemPh(context: Context): Float = DimenSsp.wemPh(context, this)

// EN Resource ID variants
// PT Variantes que retornam o ID de recurso

/** @see DimenSsp.sspRes */
fun Int.sspRes(context: Context): Int = DimenSsp.sspRes(context, this)

/** @see DimenSsp.sspPhRes */
fun Int.sspPhRes(context: Context): Int = DimenSsp.sspPhRes(context, this)

/** @see DimenSsp.sspLhRes */
fun Int.sspLhRes(context: Context): Int = DimenSsp.sspLhRes(context, this)

/** @see DimenSsp.sspPwRes */
fun Int.sspPwRes(context: Context): Int = DimenSsp.sspPwRes(context, this)

/** @see DimenSsp.sspLwRes */
fun Int.sspLwRes(context: Context): Int = DimenSsp.sspLwRes(context, this)

/** @see DimenSsp.hspRes */
fun Int.hspRes(context: Context): Int = DimenSsp.hspRes(context, this)

/** @see DimenSsp.hspLwRes */
fun Int.hspLwRes(context: Context): Int = DimenSsp.hspLwRes(context, this)

/** @see DimenSsp.hspPwRes */
fun Int.hspPwRes(context: Context): Int = DimenSsp.hspPwRes(context, this)

/** @see DimenSsp.wspRes */
fun Int.wspRes(context: Context): Int = DimenSsp.wspRes(context, this)

/** @see DimenSsp.wspLhRes */
fun Int.wspLhRes(context: Context): Int = DimenSsp.wspLhRes(context, this)

/** @see DimenSsp.wspPhRes */
fun Int.wspPhRes(context: Context): Int = DimenSsp.wspPhRes(context, this)


// EN Rotation facilitator extensions for Sp from code.
// PT Extensões facilitadoras de rotação para Sp de código.

/** @see DimenSsp.sspRotate */
fun Int.sspRotate(
    context: Context,
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.SMALL_WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float = DimenSsp.sspRotate(context, this, rotationValue, finalQualifierResolver, orientation, fontScale)

/** @see DimenSsp.hspRotate */
fun Int.hspRotate(
    context: Context,
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.HEIGHT,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float = DimenSsp.hspRotate(context, this, rotationValue, finalQualifierResolver, orientation, fontScale)

/** @see DimenSsp.wspRotate */
fun Int.wspRotate(
    context: Context,
    rotationValue: Int,
    finalQualifierResolver: DpQualifier = DpQualifier.WIDTH,
    orientation: Orientation = Orientation.LANDSCAPE,
    fontScale: Boolean = true
): Float = DimenSsp.wspRotate(context, this, rotationValue, finalQualifierResolver, orientation, fontScale)

// EN UiModeType facilitator extensions for Sp from code.
// PT Extensões facilitadoras de UiModeType para Sp de código.

/** @see DimenSsp.sspMode */
fun Int.sspMode(
    context: Context,
    modeValue: Int,
    uiModeType: UiModeType,
    foldingFeature: androidx.window.layout.FoldingFeature? = null,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.sspMode(context, this, modeValue, uiModeType, foldingFeature, finalQualifierResolver, fontScale)

/** @see DimenSsp.hspMode */
fun Int.hspMode(
    context: Context,
    modeValue: Int,
    uiModeType: UiModeType,
    foldingFeature: androidx.window.layout.FoldingFeature? = null,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.hspMode(context, this, modeValue, uiModeType, foldingFeature, finalQualifierResolver, fontScale)

/** @see DimenSsp.wspMode */
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

/** @see DimenSsp.sspQualifier */
fun Int.sspQualifier(
    context: Context,
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.sspQualifier(context, this, qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale)

/** @see DimenSsp.hspQualifier */
fun Int.hspQualifier(
    context: Context,
    qualifiedValue: Int,
    qualifierType: DpQualifier,
    qualifierValue: Int,
    finalQualifierResolver: DpQualifier? = null,
    fontScale: Boolean = true
): Float = DimenSsp.hspQualifier(context, this, qualifiedValue, qualifierType, qualifierValue, finalQualifierResolver, fontScale)

/** @see DimenSsp.wspQualifier */
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

/** @see DimenSsp.sspScreen */
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

/** @see DimenSsp.hspScreen */
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

/** @see DimenSsp.wspScreen */
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
