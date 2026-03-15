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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.Inverter
import kotlin.math.abs

// EN Composable extensions for quick dynamic text scaling (Sp) using the DP XML resources.
// PT Extensões Composable para escalonamento dinâmico rápido de texto (Sp) usando os recursos XML de DP.

/**
 * EN
 * Extension for TextUnit (Sp) with dynamic scaling based on the **Smallest Width (swDP)**.
 * Reads the pre-calculated DP resource (e.g., `_16sdp`) and converts it to Sp, respecting
 * the user's font scale setting.
 * Usage example: `16.ssp`.
 *
 * PT
 * Extensão para TextUnit (Sp) com dimensionamento dinâmico baseado na **Smallest Width (swDP)**.
 * Lê o recurso DP pré-calculado (ex: `_16sdp`) e converte para Sp, respeitando
 * a configuração de escala de fonte do usuário.
 * Exemplo de uso: `16.ssp`.
 */
@get:Composable
val Int.ssp: TextUnit get() = this.toDynamicScaledSp(DpQualifier.SMALL_WIDTH, fontScale = true)

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

// EN Dynamic scaling function for Sp (Resource-based, reuses DP XML resources).
// PT Função de dimensionamento dinâmico para Sp (baseada em recursos, reutiliza os recursos XML de DP).

/**
 * EN
 * Converts an Int (the base Sp value) into a dynamically scaled TextUnit (Sp).
 * This function reuses the existing DP XML resources (`_Nsdp`, `_Nhdp`, `_Nwdp`) as the
 * dimension values, then converts them to Sp. This means the scaling system is the same as
 * the DP system — the raw dp value from the resource is used directly as an sp number.
 *
 * 1. Constructs the resource name based on the value and the qualifier (e.g., `_16sdp`).
 * 2. Loads the dimension value in dp from that resource.
 * 3. Converts it to Sp, optionally stripping the system font scale.
 *
 * PT
 * Converte um Int (o valor Sp base) em um TextUnit (Sp) escalado dinamicamente.
 * Esta função reutiliza os recursos XML de DP existentes (`_Nsdp`, `_Nhdp`, `_Nwdp`) como
 * valores de dimensão, convertendo-os para Sp. O sistema de escalonamento é o mesmo do DP —
 * o valor dp bruto do recurso é usado diretamente como número sp.
 *
 * 1. Constrói o nome do recurso baseado no valor e no qualificador (ex: `_16sdp`).
 * 2. Carrega o valor de dimensão em dp daquele recurso.
 * 3. Converte para Sp, opcionalmente removendo a escala de fonte do sistema.
 *
 * @param qualifier The screen qualifier used to determine the resource name (sdp, hdp, wdp).
 * @param fontScale Whether to respect the user's font scale setting.
 * @param inverter Inverter to swap qualifier when orientation changes.
 * @return The TextUnit (Sp) value loaded from the resource, or the base sp value as fallback.
 */
@SuppressLint("LocalContextResourcesRead", "DiscouragedApi")
@Composable
fun Int.toDynamicScaledSp(
    qualifier: DpQualifier,
    fontScale: Boolean,
    inverter: Inverter = Inverter.DEFAULT
): TextUnit {
    require(this in 1..600) {
        "Value must be between 1 and 600 to use the dynamic scaling dimension logic. Current value: $this"
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    var actualQualifier = qualifier

    when (inverter) {
        Inverter.PH_TO_LW -> if (isLandscape && qualifier == DpQualifier.HEIGHT) actualQualifier = DpQualifier.WIDTH
        Inverter.PW_TO_LH -> if (isLandscape && qualifier == DpQualifier.WIDTH) actualQualifier = DpQualifier.HEIGHT
        Inverter.LH_TO_PW -> if (isPortrait && qualifier == DpQualifier.HEIGHT) actualQualifier = DpQualifier.WIDTH
        Inverter.LW_TO_PH -> if (isPortrait && qualifier == DpQualifier.WIDTH) actualQualifier = DpQualifier.HEIGHT
        Inverter.SW_TO_LH -> if (isLandscape && qualifier == DpQualifier.SMALL_WIDTH) actualQualifier = DpQualifier.HEIGHT
        Inverter.SW_TO_LW -> if (isLandscape && qualifier == DpQualifier.SMALL_WIDTH) actualQualifier = DpQualifier.WIDTH
        Inverter.SW_TO_PH -> if (isPortrait && qualifier == DpQualifier.SMALL_WIDTH) actualQualifier = DpQualifier.HEIGHT
        Inverter.SW_TO_PW -> if (isPortrait && qualifier == DpQualifier.SMALL_WIDTH) actualQualifier = DpQualifier.WIDTH
        Inverter.DEFAULT -> {}
    }

    // EN Reuses the existing DP XML resource naming convention: _Nsdp, _Nhdp, _Nwdp.
    // PT Reutiliza a convenção de nomenclatura dos recursos XML de DP: _Nsdp, _Nhdp, _Nwdp.
    val suffix = when (actualQualifier) {
        DpQualifier.HEIGHT -> "hsp"
        DpQualifier.WIDTH -> "wsp"
        else -> "ssp"
    }

    val resourceName = "_${abs(this)}$suffix"
    val dimenResourceId = findResourceIdByNameSsp(resourceName)

    // EN If the resource is found, loads the dp value and treats it as sp.
    // PT Se o recurso for encontrado, carrega o valor dp e o trata como sp.
    return if (dimenResourceId != 0 && dimenResourceId != -1) {
        val dpValue = dimensionResource(id = dimenResourceId).value
        if (fontScale) dpValue.sp
        else (dpValue / LocalDensity.current.fontScale).sp
    } else {
        // EN Fallback: returns the base sp value (no scaling).
        // PT Fallback: retorna o valor sp base (sem escalonamento).
        if (fontScale) this.sp
        else (this.toFloat() / LocalDensity.current.fontScale).sp
    }
}

/**
 * EN
 * Returns the DP configuration value (as a float) for a specific DpQualifier.
 *
 * PT
 * Retorna o valor de configuração de DP (em float) para um DpQualifier específico.
 *
 * @param qualifier The type of qualifier (SMALL_WIDTH, HEIGHT, WIDTH).
 * @param configuration The current resource configuration.
 * @return The corresponding DP value in the configuration.
 */
internal fun getQualifierValue(qualifier: DpQualifier, configuration: Configuration): Float {
    return when (qualifier) {
        DpQualifier.SMALL_WIDTH -> configuration.smallestScreenWidthDp.toFloat()
        DpQualifier.HEIGHT -> configuration.screenHeightDp.toFloat()
        DpQualifier.WIDTH -> configuration.screenWidthDp.toFloat()
    }
}

/**
 * EN
 * Finds the dimension resource ID (`dimen`) by name.
 * Private to this file to avoid conflicts with the DP equivalent in DimenSdp.kt.
 *
 * PT
 * Encontra o ID de recurso de dimensão (`dimen`) pelo nome.
 * Privado neste arquivo para evitar conflitos com o equivalente DP em DimenSdp.kt.
 */
@SuppressLint("LocalContextResourcesRead", "DiscouragedApi")
@Composable
private fun findResourceIdByNameSsp(resourceName: String): Int {
    val context = androidx.compose.ui.platform.LocalContext.current
    return context.resources.getIdentifier(resourceName, "dimen", context.packageName)
}
