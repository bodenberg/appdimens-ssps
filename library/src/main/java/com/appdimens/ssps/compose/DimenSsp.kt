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
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.effectiveDpQualifier
import com.appdimens.ssps.common.Inverter
import com.appdimens.ssps.core.AppDimensSspsFactors
import com.appdimens.ssps.core.DimenResourceIdCache
import kotlin.math.abs

/**
 * EN
 * Gets the actual value from the Configuration for the given DpQualifier.
 *
 * PT
 * Obtém o valor real da configuração (Configuration) para o DpQualifier dado.
 *
 * @param qualifier The type of qualifier (SMALL_WIDTH, HEIGHT, WIDTH).
 * @param configuration The current resource configuration.
 * @return The numeric value (in Dp) of the screen metric.
 */
internal fun getQualifierValue(qualifier: DpQualifier, configuration: Configuration): Float {
    return when (qualifier) {
        DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp.toFloat()
        DpQualifier.HEIGHT -> configuration.screenHeightDp.toFloat()
        DpQualifier.WIDTH -> configuration.screenWidthDp.toFloat()
    }
}

// EN Composable extensions for quick dynamic text scaling (Sp) using the DP XML resources.
// PT Extensões Composable para escalonamento dinâmico rápido de texto (Sp) usando os recursos XML de DP.

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Smallest Width (swDP)**.
 * Usage example: `16.ssp`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Lê o recurso DP pré-calculado (ex: `_16ssp`) e converte para Sp, respeitando
 * a configuração de escala de fonte do usuário.
 * Exemplo de uso: `16.ssp`.
 */
@get:Composable
val Int.ssp: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true)

/**
 * EN
 * Pixel version of ssp.
 *
 * PT
 * Versão em pixel de ssp.
 */
@get:Composable
val Int.sspPx: Float get() = LocalDensity.current.run { ssp.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**, but
 * in portrait orientation it acts as **Screen Height (hDP)**.
 * Usage example: `32.sspPh`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**, mas
 * na orientação retrato atua como **Altura da Tela (hDP)**.
 * Exemplo de uso: `32.sspPh`.
 */
@get:Composable
val Int.sspPh: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true, inverter = Inverter.SW_TO_PH)

/**
 * EN
 * Pixel version of sspPh.
 *
 * PT
 * Versão em pixel de sspPh.
 */
@get:Composable
val Int.sspPhPx: Float get() = LocalDensity.current.run { sspPh.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**, but
 * in landscape orientation it acts as **Screen Height (hDP)**.
 * Usage example: `32.sspLh`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**, mas
 * na orientação paisagem atua como **Altura da Tela (hDP)**.
 * Exemplo de uso: `32.sspLh`.
 */
@get:Composable
val Int.sspLh: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true, inverter = Inverter.SW_TO_LH)

/**
 * EN
 * Pixel version of sspLh.
 *
 * PT
 * Versão em pixel de sspLh.
 */
@get:Composable
val Int.sspLhPx: Float get() = LocalDensity.current.run { sspLh.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**, but
 * in portrait orientation it acts as **Screen Width (wDP)**.
 * Usage example: `32.sspPw`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**, mas
 * na orientação retrato atua como **Largura da Tela (wDP)**.
 * Exemplo de uso: `32.sspPw`.
 */
@get:Composable
val Int.sspPw: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true, inverter = Inverter.SW_TO_PW)

/**
 * EN
 * Pixel version of sspPw.
 *
 * PT
 * Versão em pixel de sspPw.
 */
@get:Composable
val Int.sspPwPx: Float get() = LocalDensity.current.run { sspPw.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)**, but
 * in landscape orientation it acts as **Screen Width (wDP)**.
 * Usage example: `32.sspLw`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**, mas
 * na orientação paisagem atua como **Largura da Tela (wDP)**.
 * Exemplo de uso: `32.sspLw`.
 */
@get:Composable
val Int.sspLw: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true, inverter = Inverter.SW_TO_LW)

/**
 * EN
 * Pixel version of sspLw.
 *
 * PT
 * Versão em pixel de sspLw.
 */
@get:Composable
val Int.sspLwPx: Float get() = LocalDensity.current.run { sspLw.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Height (hDP)**.
 * Usage example: `32.hsp`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**.
 * Exemplo de uso: `32.hsp`.
 */
@get:Composable
val Int.hsp: TextUnit get() = this.toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = true)

/**
 * EN
 * Pixel version of hsp.
 *
 * PT
 * Versão em pixel de hsp.
 */
@get:Composable
val Int.hspPx: Float get() = LocalDensity.current.run { hsp.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Height (hDP)**, but
 * in landscape orientation it acts as **Screen Width (wDP)**.
 * Usage example: `32.hspLw`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**, mas
 * na orientação paisagem atua como **Largura da Tela (wDP)**.
 * Exemplo de uso: `32.hspLw`.
 */
@get:Composable
val Int.hspLw: TextUnit get() = this.toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = true, inverter = Inverter.PH_TO_LW)

/**
 * EN
 * Pixel version of hspLw.
 *
 * PT
 * Versão em pixel de hspLw.
 */
@get:Composable
val Int.hspLwPx: Float get() = LocalDensity.current.run { hspLw.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Height (hDP)**, but
 * in portrait orientation it acts as **Screen Width (wDP)**.
 * Usage example: `32.hspPw`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)**, mas
 * na orientação retrato atua como **Largura da Tela (wDP)**.
 * Exemplo de uso: `32.hspPw`.
 */
@get:Composable
val Int.hspPw: TextUnit get() = this.toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = true, inverter = Inverter.LH_TO_PW)

/**
 * EN
 * Pixel version of hspPw.
 *
 * PT
 * Versão em pixel de hspPw.
 */
@get:Composable
val Int.hspPwPx: Float get() = LocalDensity.current.run { hspPw.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Width (wDP)**.
 * Usage example: `100.wsp`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**.
 * Exemplo de uso: `100.wsp`.
 */
@get:Composable
val Int.wsp: TextUnit get() = this.toDynamicScaledSp(DpQualifier.WIDTH, fontScale = true)

/**
 * EN
 * Pixel version of wsp.
 *
 * PT
 * Versão em pixel de wsp.
 */
@get:Composable
val Int.wspPx: Float get() = LocalDensity.current.run { wsp.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Width (wDP)**, but
 * in landscape orientation it acts as **Screen Height (hDP)**.
 * Usage example: `100.wspLh`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**, mas
 * na orientação paisagem atua como **Altura da Tela (hDP)**.
 * Exemplo de uso: `100.wspLh`.
 */
@get:Composable
val Int.wspLh: TextUnit get() = this.toDynamicScaledSp(DpQualifier.WIDTH, fontScale = true, inverter = Inverter.PW_TO_LH)

/**
 * EN
 * Pixel version of wspLh.
 *
 * PT
 * Versão em pixel de wspLh.
 */
@get:Composable
val Int.wspLhPx: Float get() = LocalDensity.current.run { wspLh.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Width (wDP)**, but
 * in portrait orientation it acts as **Screen Height (hDP)**.
 * Usage example: `100.wspPh`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)**, mas
 * na orientação retrato atua como **Altura da Tela (hDP)**.
 * Exemplo de uso: `100.wspPh`.
 */
@get:Composable
val Int.wspPh: TextUnit get() = this.toDynamicScaledSp(DpQualifier.WIDTH, fontScale = true, inverter = Inverter.LW_TO_PH)

/**
 * EN
 * Pixel version of wspPh.
 *
 * PT
 * Versão em pixel de wspPh.
 */
@get:Composable
val Int.wspPhPx: Float get() = LocalDensity.current.run { wspPh.toPx() }

// Aspect-ratio-aware (font-scale on); parity with appdimens-sdps `sspa` / dynamic `sdpa`.

@get:Composable
val Int.sspa: TextUnit get() = toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true, applyAspectRatio = true)

@get:Composable
val Int.sspPxa: Float get() = LocalDensity.current.run { sspa.toPx() }

@get:Composable
val Int.sspia: TextUnit get() = sspa

@get:Composable
val Int.sspPxIa: Float get() = sspPxa

@get:Composable
val Int.sspPha: TextUnit get() = toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true, inverter = Inverter.SW_TO_PH, applyAspectRatio = true)

@get:Composable
val Int.sspPxPha: Float get() = LocalDensity.current.run { sspPha.toPx() }

@get:Composable
val Int.sspPhia: TextUnit get() = sspPha

@get:Composable
val Int.sspPxPhia: Float get() = sspPxPha

@get:Composable
val Int.sspLha: TextUnit get() = toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true, inverter = Inverter.SW_TO_LH, applyAspectRatio = true)

@get:Composable
val Int.sspPxLha: Float get() = LocalDensity.current.run { sspLha.toPx() }

@get:Composable
val Int.sspLhia: TextUnit get() = sspLha

@get:Composable
val Int.sspPxLhia: Float get() = sspPxLha

@get:Composable
val Int.sspPwa: TextUnit get() = toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true, inverter = Inverter.SW_TO_PW, applyAspectRatio = true)

@get:Composable
val Int.sspPxPwa: Float get() = LocalDensity.current.run { sspPwa.toPx() }

@get:Composable
val Int.sspPwia: TextUnit get() = sspPwa

@get:Composable
val Int.sspPxPwia: Float get() = sspPxPwa

@get:Composable
val Int.sspLwa: TextUnit get() = toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true, inverter = Inverter.SW_TO_LW, applyAspectRatio = true)

@get:Composable
val Int.sspPxLwa: Float get() = LocalDensity.current.run { sspLwa.toPx() }

@get:Composable
val Int.sspLwia: TextUnit get() = sspLwa

@get:Composable
val Int.sspPxLwia: Float get() = sspPxLwa

@get:Composable
val Int.hspa: TextUnit get() = toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = true, applyAspectRatio = true)

@get:Composable
val Int.hspPxA: Float get() = LocalDensity.current.run { hspa.toPx() }

@get:Composable
val Int.hspia: TextUnit get() = hspa

@get:Composable
val Int.hspPxIa: Float get() = hspPxA

@get:Composable
val Int.hspLwa: TextUnit get() = toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = true, inverter = Inverter.PH_TO_LW, applyAspectRatio = true)

@get:Composable
val Int.hspPxLwa: Float get() = LocalDensity.current.run { hspLwa.toPx() }

@get:Composable
val Int.hspLwia: TextUnit get() = hspLwa

@get:Composable
val Int.hspPxLwia: Float get() = hspPxLwa

@get:Composable
val Int.hspPwa: TextUnit get() = toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = true, inverter = Inverter.LH_TO_PW, applyAspectRatio = true)

@get:Composable
val Int.hspPxPwa: Float get() = LocalDensity.current.run { hspPwa.toPx() }

@get:Composable
val Int.hspPwia: TextUnit get() = hspPwa

@get:Composable
val Int.hspPxPwia: Float get() = hspPxPwa

@get:Composable
val Int.wspa: TextUnit get() = toDynamicScaledSp(DpQualifier.WIDTH, fontScale = true, applyAspectRatio = true)

@get:Composable
val Int.wspPxA: Float get() = LocalDensity.current.run { wspa.toPx() }

@get:Composable
val Int.wspia: TextUnit get() = wspa

@get:Composable
val Int.wspPxIa: Float get() = wspPxA

@get:Composable
val Int.wspLha: TextUnit get() = toDynamicScaledSp(DpQualifier.WIDTH, fontScale = true, inverter = Inverter.PW_TO_LH, applyAspectRatio = true)

@get:Composable
val Int.wspPxLha: Float get() = LocalDensity.current.run { wspLha.toPx() }

@get:Composable
val Int.wspLhia: TextUnit get() = wspLha

@get:Composable
val Int.wspPxLhia: Float get() = wspPxLha

@get:Composable
val Int.wspPha: TextUnit get() = toDynamicScaledSp(DpQualifier.WIDTH, fontScale = true, inverter = Inverter.LW_TO_PH, applyAspectRatio = true)

@get:Composable
val Int.wspPxPha: Float get() = LocalDensity.current.run { wspPha.toPx() }

@get:Composable
val Int.wspPhia: TextUnit get() = wspPha

@get:Composable
val Int.wspPxPhia: Float get() = wspPxPha

// EN WITHOUT FONT SCALE variants (sem escala de fonte)
// PT Variantes SEM ESCALA DE FONTE

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)** (WITHOUT FONT SCALE).
 * Usage example: `16.sem`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)** (SEM ESCALA DE FONTE).
 * Exemplo de uso: `16.sem`.
 */
@get:Composable
val Int.sem: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = false)

/**
 * EN
 * Pixel version of sem.
 *
 * PT
 * Versão em pixel de sem.
 */
@get:Composable
val Int.semPx: Float get() = LocalDensity.current.run { sem.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)** (WITHOUT FONT SCALE), but
 * in portrait orientation it acts as **Screen Height (hDP)**.
 * Usage example: `32.semPh`.
 *
 * PT
 * Extensão para TextUnit (Sp) (SEM ESCALA DE FONTE) com dimensionamento baseado na **Smallest Width**, mas
 * na orientação retrato atua como Altura da Tela.
 * Exemplo de uso: `32.semPh`.
 */
@get:Composable
val Int.semPh: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = false, inverter = Inverter.SW_TO_PH)

/**
 * EN
 * Pixel version of semPh.
 *
 * PT
 * Versão em pixel de semPh.
 */
@get:Composable
val Int.semPhPx: Float get() = LocalDensity.current.run { semPh.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)** (WITHOUT FONT SCALE), but
 * in landscape orientation it acts as **Screen Height (hDP)**.
 * Usage example: `32.semLh`.
 *
 * PT
 * Extensão para TextUnit (Sp) (SEM ESCALA DE FONTE) com dimensionamento baseado na **Smallest Width**, mas
 * na orientação paisagem atua como Altura da Tela.
 * Exemplo de uso: `32.semLh`.
 */
@get:Composable
val Int.semLh: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = false, inverter = Inverter.SW_TO_LH)

/**
 * EN
 * Pixel version of semLh.
 *
 * PT
 * Versão em pixel de semLh.
 */
@get:Composable
val Int.semLhPx: Float get() = LocalDensity.current.run { semLh.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)** (WITHOUT FONT SCALE), but
 * in portrait orientation it acts as **Screen Width (wDP)**.
 * Usage example: `32.semPw`.
 *
 * PT
 * Extensão para TextUnit (Sp) (SEM ESCALA DE FONTE) com dimensionamento baseado na **Smallest Width**, mas
 * na orientação retrato atua como Largura da Tela.
 * Exemplo de uso: `32.semPw`.
 */
@get:Composable
val Int.semPw: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = false, inverter = Inverter.SW_TO_PW)

/**
 * EN
 * Pixel version of semPw.
 *
 * PT
 * Versão em pixel de semPw.
 */
@get:Composable
val Int.semPwPx: Float get() = LocalDensity.current.run { semPw.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on **Smallest Width (swDP)** (WITHOUT FONT SCALE), but
 * in landscape orientation it acts as **Screen Width (wDP)**.
 * Usage example: `32.semLw`.
 *
 * PT
 * Extensão para TextUnit (Sp) (SEM ESCALA DE FONTE) com dimensionamento baseado na **Smallest Width**, mas
 * na orientação paisagem atua como Largura da Tela.
 * Exemplo de uso: `32.semLw`.
 */
@get:Composable
val Int.semLw: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = false, inverter = Inverter.SW_TO_LW)

/**
 * EN
 * Pixel version of semLw.
 *
 * PT
 * Versão em pixel de semLw.
 */
@get:Composable
val Int.semLwPx: Float get() = LocalDensity.current.run { semLw.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Height (hDP)** (WITHOUT FONT SCALE).
 * Usage example: `32.hem`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Altura da Tela (hDP)** (SEM ESCALA DE FONTE).
 * Exemplo de uso: `32.hem`.
 */
@get:Composable
val Int.hem: TextUnit get() = this.toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = false)

/**
 * EN
 * Pixel version of hem.
 *
 * PT
 * Versão em pixel de hem.
 */
@get:Composable
val Int.hemPx: Float get() = LocalDensity.current.run { hem.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Height (hDP)** (WITHOUT FONT SCALE), but
 * in landscape orientation it acts as **Screen Width (wDP)**.
 * Usage example: `32.hemLw`.
 *
 * PT
 * Extensão para TextUnit (Sp) (SEM ESCALA DE FONTE) baseado na Altura, mas na paisagem atua como Largura.
 * Exemplo de uso: `32.hemLw`.
 */
@get:Composable
val Int.hemLw: TextUnit get() = this.toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = false, inverter = Inverter.PH_TO_LW)

/**
 * EN
 * Pixel version of hemLw.
 *
 * PT
 * Versão em pixel de hemLw.
 */
@get:Composable
val Int.hemLwPx: Float get() = LocalDensity.current.run { hemLw.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Height (hDP)** (WITHOUT FONT SCALE), but
 * in portrait orientation it acts as **Screen Width (wDP)**.
 * Usage example: `32.hemPw`.
 *
 * PT
 * Extensão para TextUnit (Sp) (SEM ESCALA DE FONTE) baseado na Altura, mas no retrato atua como Largura.
 * Exemplo de uso: `32.hemPw`.
 */
@get:Composable
val Int.hemPw: TextUnit get() = this.toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = false, inverter = Inverter.LH_TO_PW)

/**
 * EN
 * Pixel version of hemPw.
 *
 * PT
 * Versão em pixel de hemPw.
 */
@get:Composable
val Int.hemPwPx: Float get() = LocalDensity.current.run { hemPw.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Width (wDP)** (WITHOUT FONT SCALE).
 * Usage example: `100.wem`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Largura da Tela (wDP)** (SEM ESCALA DE FONTE).
 * Exemplo de uso: `100.wem`.
 */
@get:Composable
val Int.wem: TextUnit get() = this.toDynamicScaledSp(DpQualifier.WIDTH, fontScale = false)

/**
 * EN
 * Pixel version of wem.
 *
 * PT
 * Versão em pixel de wem.
 */
@get:Composable
val Int.wemPx: Float get() = LocalDensity.current.run { wem.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Width (wDP)** (WITHOUT FONT SCALE), but
 * in landscape orientation it acts as **Screen Height (hDP)**.
 * Usage example: `100.wemLh`.
 *
 * PT
 * Extensão para TextUnit (Sp) (SEM ESCALA DE FONTE) baseado na Largura, mas na paisagem atua como Altura.
 * Exemplo de uso: `100.wemLh`.
 */
@get:Composable
val Int.wemLh: TextUnit get() = this.toDynamicScaledSp(DpQualifier.WIDTH, fontScale = false, inverter = Inverter.PW_TO_LH)

/**
 * EN
 * Pixel version of wemLh.
 *
 * PT
 * Versão em pixel de wemLh.
 */
@get:Composable
val Int.wemLhPx: Float get() = LocalDensity.current.run { wemLh.toPx() }

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Screen Width (wDP)** (WITHOUT FONT SCALE), but
 * in portrait orientation it acts as **Screen Height (hDP)**.
 * Usage example: `100.wemPh`.
 *
 * PT
 * Extensão para TextUnit (Sp) (SEM ESCALA DE FONTE) baseado na Largura, mas no retrato atua como Altura.
 * Exemplo de uso: `100.wemPh`.
 */
@get:Composable
val Int.wemPh: TextUnit get() = this.toDynamicScaledSp(DpQualifier.WIDTH, fontScale = false, inverter = Inverter.LW_TO_PH)

/**
 * EN
 * Pixel version of wemPh.
 *
 * PT
 * Versão em pixel de wemPh.
 */
@get:Composable
val Int.wemPhPx: Float get() = LocalDensity.current.run { wemPh.toPx() }

// Aspect-ratio-aware without Compose font multiplication (parity with sem/hem/wem + adjustment in code).

@get:Composable
val Int.sema: TextUnit get() = toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = false, applyAspectRatio = true)

@get:Composable
val Int.semPxA: Float get() = LocalDensity.current.run { sema.toPx() }

@get:Composable
val Int.semia: TextUnit get() = sema

@get:Composable
val Int.semPxIa: Float get() = semPxA

@get:Composable
val Int.semPha: TextUnit get() = toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = false, inverter = Inverter.SW_TO_PH, applyAspectRatio = true)

@get:Composable
val Int.semPxPha: Float get() = LocalDensity.current.run { semPha.toPx() }

@get:Composable
val Int.semPhia: TextUnit get() = semPha

@get:Composable
val Int.semPxPhia: Float get() = semPxPha

@get:Composable
val Int.semLha: TextUnit get() = toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = false, inverter = Inverter.SW_TO_LH, applyAspectRatio = true)

@get:Composable
val Int.semPxLha: Float get() = LocalDensity.current.run { semLha.toPx() }

@get:Composable
val Int.semLhia: TextUnit get() = semLha

@get:Composable
val Int.semPxLhia: Float get() = semPxLha

@get:Composable
val Int.semPwa: TextUnit get() = toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = false, inverter = Inverter.SW_TO_PW, applyAspectRatio = true)

@get:Composable
val Int.semPxPwa: Float get() = LocalDensity.current.run { semPwa.toPx() }

@get:Composable
val Int.semPwia: TextUnit get() = semPwa

@get:Composable
val Int.semPxPwia: Float get() = semPxPwa

@get:Composable
val Int.semLwa: TextUnit get() = toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = false, inverter = Inverter.SW_TO_LW, applyAspectRatio = true)

@get:Composable
val Int.semPxLwa: Float get() = LocalDensity.current.run { semLwa.toPx() }

@get:Composable
val Int.semLwia: TextUnit get() = semLwa

@get:Composable
val Int.semPxLwia: Float get() = semPxLwa

@get:Composable
val Int.hema: TextUnit get() = toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = false, applyAspectRatio = true)

@get:Composable
val Int.hemPxA: Float get() = LocalDensity.current.run { hema.toPx() }

@get:Composable
val Int.hemia: TextUnit get() = hema

@get:Composable
val Int.hemPxIa: Float get() = hemPxA

@get:Composable
val Int.hemLwa: TextUnit get() = toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = false, inverter = Inverter.PH_TO_LW, applyAspectRatio = true)

@get:Composable
val Int.hemPxLwa: Float get() = LocalDensity.current.run { hemLwa.toPx() }

@get:Composable
val Int.hemLwia: TextUnit get() = hemLwa

@get:Composable
val Int.hemPxLwia: Float get() = hemPxLwa

@get:Composable
val Int.hemPwa: TextUnit get() = toDynamicScaledSp(DpQualifier.HEIGHT, fontScale = false, inverter = Inverter.LH_TO_PW, applyAspectRatio = true)

@get:Composable
val Int.hemPxPwa: Float get() = LocalDensity.current.run { hemPwa.toPx() }

@get:Composable
val Int.hemPwia: TextUnit get() = hemPwa

@get:Composable
val Int.hemPxPwia: Float get() = hemPxPwa

@get:Composable
val Int.wema: TextUnit get() = toDynamicScaledSp(DpQualifier.WIDTH, fontScale = false, applyAspectRatio = true)

@get:Composable
val Int.wemPxA: Float get() = LocalDensity.current.run { wema.toPx() }

@get:Composable
val Int.wemia: TextUnit get() = wema

@get:Composable
val Int.wemPxIa: Float get() = wemPxA

@get:Composable
val Int.wemLha: TextUnit get() = toDynamicScaledSp(DpQualifier.WIDTH, fontScale = false, inverter = Inverter.PW_TO_LH, applyAspectRatio = true)

@get:Composable
val Int.wemPxLha: Float get() = LocalDensity.current.run { wemLha.toPx() }

@get:Composable
val Int.wemLhia: TextUnit get() = wemLha

@get:Composable
val Int.wemPxLhia: Float get() = wemPxLha

@get:Composable
val Int.wemPha: TextUnit get() = toDynamicScaledSp(DpQualifier.WIDTH, fontScale = false, inverter = Inverter.LW_TO_PH, applyAspectRatio = true)

@get:Composable
val Int.wemPxPha: Float get() = LocalDensity.current.run { wemPha.toPx() }

@get:Composable
val Int.wemPhia: TextUnit get() = wemPha

@get:Composable
val Int.wemPxPhia: Float get() = wemPxPha

// EN Dynamic scaling function for Sp (Resource-based, reuses DP XML resources).
// PT Função de dimensionamento dinâmico para Sp (baseada em recursos, reutiliza os recursos XML de DP).

/**
 * EN
 * Converts an Int (the base Sp value) into a dynamically scaled TextUnit (Sp).
 * 1. Constructs the resource name based on the value and the qualifier (e.g., `_16ssp`).
 * 2. Loads the dimension value in dp from that resource.
 * 3. Converts it to Sp, optionally stripping the system font scale.
 *
 * PT
 * Converte um Int (o valor Sp base) em um TextUnit (Sp) escalado dinamicamente.
 * Esta função reutiliza os recursos XML de DP existentes (`_Nsdp`, `_Nhdp`, `_Nwdp`) como
 * valores de dimensão, convertendo-os para Sp. O sistema de escalonamento é o mesmo do DP —
 * o valor dp bruto do recurso é usado diretamente como número sp.
 *
 * 1. Constrói o nome do recurso baseado no valor e no qualificador (ex: `_16ssp`).
 * 2. Carrega o valor de dimensão em dp daquele recurso.
 * 3. Converte para Sp, opcionalmente removendo a escala de fonte do sistema.
 *
 * @param qualifier The screen qualifier used to determine the resource name (sdp, hdp, wdp).
 * @param fontScale Whether to respect the user's font scale setting.
 * @param inverter Inverter to swap qualifier when orientation changes.
 * @return The TextUnit (Sp) value loaded from the resource, or the base sp value as fallback.
 */
@SuppressLint("LocalContextResourcesRead", "DiscouragedApi", "ConfigurationScreenWidthHeight")
@Composable
fun Int.toDynamicScaledSp(
    qualifier: DpQualifier,
    fontScale: Boolean,
    inverter: Inverter = Inverter.DEFAULT,
    applyAspectRatio: Boolean = false,
): TextUnit {
    require(this in 1..600) {
        "Value must be between 1 and 600 to use the dynamic scaling dimension logic. Current value: $this"
    }

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val density = LocalDensity.current
    val actualQualifier = effectiveDpQualifier(configuration, qualifier, inverter)

    // EN Reuses the existing DP XML resource naming convention: _Nssp, _Nhsp, _Nwsp.
    // PT Reutiliza a convenção de nomenclatura dos recursos XML de DP: _Nssp, _Nhsp, _Nwsp.
    val suffix =
        when (actualQualifier) {
            DpQualifier.HEIGHT -> "hsp"
            DpQualifier.WIDTH -> "wsp"
            else -> "ssp"
        }

    val resourceName = "_${abs(this)}$suffix"
    val layoutStamp = layoutRememberStamp(configuration)
    val stamp = spRememberStamp(layoutStamp, density)
    val base = this

    // EN Resolve resource id + Sp outside dimensionResource so remember can cache the result.
    // PT Resolve o id + Sp fora de dimensionResource para o remember poder cachear.
    val dimenResourceId = remember(resourceName, context.packageName) {
        DimenResourceIdCache.getOrResolve(
            context.resources,
            context.packageName,
            resourceName,
        )
    }

    return remember(
        base,
        actualQualifier,
        fontScale,
        applyAspectRatio,
        stamp,
        dimenResourceId,
    ) {
        if (dimenResourceId != 0) {
            val densityValue = density.density
            var dpValue = context.resources.getDimension(dimenResourceId) / densityValue
            if (applyAspectRatio) {
                AppDimensSspsFactors.ensureUpToDate(context)
                dpValue *= AppDimensSspsFactors.adjustmentForQualifier(actualQualifier)
            }
            if (fontScale) dpValue.sp
            else (dpValue / density.fontScale).sp
        } else {
            if (fontScale) base.sp
            else (base.toFloat() / density.fontScale).sp
        }
    }
}
