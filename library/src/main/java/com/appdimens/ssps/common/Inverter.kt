/**
 * Author & Developer: Jean Bodenberg
 * GIT: https://github.com/bodenberg/appdimens-sdps.git
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
package com.appdimens.ssps.common

/**
 * EN Defines the screen orientation inversion types of the device.
 * Used to specify how dimension qualifiers should swap their values based on screen rotation.
 *
 * PT Define os tipos de inversão de orientação de tela do dispositivo.
 * Usado para especificar como os qualificadores de dimensão devem trocar seus valores com base na rotação da tela.
 */
enum class Inverter {
    /**
     * EN Maps Portrait Height to Landscape Width.
     * PT Mapeia a Altura em Retrato para a Largura em Paisagem.
     */
    PH_TO_LW,

    /**
     * EN Maps Portrait Width to Landscape Height.
     * PT Mapeia a Largura em Retrato para a Altura em Paisagem.
     */
    PW_TO_LH,

    /**
     * EN Maps Landscape Height to Portrait Width.
     * PT Mapeia a Altura em Paisagem para a Largura em Retrato.
     */
    LH_TO_PW,

    /**
     * EN Maps Landscape Width to Portrait Height.
     * PT Mapeia a Largura em Paisagem para a Altura em Retrato.
     */
    LW_TO_PH,

    /**
     * EN Maps Smallest Width to Landscape Height.
     * PT Mapeia a Smallest Width para a Altura em Paisagem.
     */
    SW_TO_LH,

    /**
     * EN Maps Smallest Width to Landscape Width.
     * PT Mapeia a Smallest Width para a Largura em Paisagem.
     */
    SW_TO_LW,

    /**
     * EN Maps Smallest Width to Portrait Height.
     * PT Mapeia a Smallest Width para a Altura em Retrato.
     */
    SW_TO_PH,

    /**
     * EN Maps Smallest Width to Portrait Width.
     * PT Mapeia a Smallest Width para a Largura em Retrato.
     */
    SW_TO_PW,

    /**
     * EN Default behavior, no inversion is applied.
     * PT Comportamento padrão, nenhuma inversão é aplicada.
     */
    DEFAULT
}