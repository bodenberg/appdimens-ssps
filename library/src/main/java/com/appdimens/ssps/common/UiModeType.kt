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

import android.content.Context
import android.content.res.Configuration
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.window.layout.WindowMetricsCalculator

/**
 * EN Defines the Android UI Mode Types for dimension customization,
 * based on Configuration.uiMode.
 *
 * PT Define os tipos de modo de interface do usuário (UI Mode Type) do Android
 * para customização de dimensões, com base em Configuration.uiMode.
 */
enum class UiModeType(val configValue: Int) {
    /**
     * EN Default Phone/Tablet.
     *
     * PT Telefone/Tablet Padrão.
     */
    NORMAL(Configuration.UI_MODE_TYPE_NORMAL),

    /**
     * EN Television.
     *
     * PT Televisão.
     */
    TELEVISION(Configuration.UI_MODE_TYPE_TELEVISION),

    /**
     * EN Car.
     *
     * PT Carro.
     */
    CAR(Configuration.UI_MODE_TYPE_CAR),

    /**
     * EN Watch (Wear OS).
     *
     * PT Relógio (Wear OS).
     */
    WATCH(Configuration.UI_MODE_TYPE_WATCH),

    /**
     * EN Desk Device (Docked).
     *
     * PT Dispositivo de Mesa (Docked).
     */
    DESK(Configuration.UI_MODE_TYPE_DESK),

    /**
     * EN Projection Device (e.g., Android Auto, Cast).
     *
     * PT Dispositivo de Projeção (e.g., Android Auto, Cast).
     */
    APPLIANCE(Configuration.UI_MODE_TYPE_APPLIANCE),

    /**
     * EN Virtual Reality (VR) Device.
     *
     * PT Dispositivo de Realidade Virtual (VR).
     */
    @RequiresApi(Build.VERSION_CODES.O)
    VR_HEADSET(Configuration.UI_MODE_TYPE_VR_HEADSET),

    /**
     * EN Any unspecified/other UI mode.
     *
     * PT Qualquer modo de UI não especificado/outros.
     */
    UNDEFINED(Configuration.UI_MODE_TYPE_UNDEFINED),

    /**
     * EN Foldable Device (Open state).
     * PT Dispositivo Dobrável tipo Fold (Estado aberto).
     */
    FOLD_OPEN(-101),

    /**
     * EN Foldable Device (Closed state).
     * PT Dispositivo Dobrável tipo Fold (Estado fechado).
     */
    FOLD_CLOSED(-102),

    /**
     * EN Flip Device (Open state).
     * PT Dispositivo Dobrável tipo Flip (Estado aberto).
     */
    FLIP_OPEN(-103),

    /**
     * EN Flip Device (Closed state).
     * PT Dispositivo Dobrável tipo Flip (Estado fechado).
     */
    FLIP_CLOSED(-104),

    /**
     * EN Foldable Device (Half-opened state).
     * PT Dispositivo Dobrável tipo Fold (Estado semiaberto).
     */
    FOLD_HALF_OPENED(-105),

    /**
     * EN Flip Device (Half-opened state).
     * PT Dispositivo Dobrável tipo Flip (Estado semiaberto).
     */
    FLIP_HALF_OPENED(-106);

    companion object {
        /**
         * EN Returns the UiModeType corresponding to the Configuration.uiMode value,
         * taking into account physical foldable features using Jetpack WindowManager.
         *
         * PT Retorna o UiModeType correspondente ao valor de Configuration.uiMode,
         * levando em conta características físicas de dispositivos dobráveis usando Jetpack WindowManager.
         *
         * @param context Application context.
         * @param foldingFeature Optional FoldingFeature obtained from WindowInfoTracker to dynamically adapt.
         */
        fun fromConfiguration(context: Context, foldingFeature: androidx.window.layout.FoldingFeature? = null): UiModeType {
            val config = context.resources.configuration

            // EN 1. Try to use Jetpack WindowManager FoldingFeature if provided
            // PT 1. Tenta usar o FoldingFeature do Jetpack WindowManager se fornecido
            if (foldingFeature != null) {
                // If it's a folding feature, we decide if it's Fold or Flip based on orientation,
                // and if it's open, closed or half_opened based on state.

                val isFold = foldingFeature.orientation == androidx.window.layout.FoldingFeature.Orientation.VERTICAL

                return if (isFold) {
                    when {
                        foldingFeature.state == androidx.window.layout.FoldingFeature.State.FLAT -> FOLD_OPEN
                        foldingFeature.state == androidx.window.layout.FoldingFeature.State.HALF_OPENED -> FOLD_HALF_OPENED
                        else -> FOLD_CLOSED
                    }
                } else {
                    // Usually horizontal fold is a Flip device.
                    when {
                        foldingFeature.state == androidx.window.layout.FoldingFeature.State.FLAT -> FLIP_OPEN
                        foldingFeature.state == androidx.window.layout.FoldingFeature.State.HALF_OPENED -> FLIP_HALF_OPENED
                        else -> FLIP_CLOSED
                    }
                }
            }

            // EN 2. Fallback: Check for hinge sensor to identify foldable
            // PT 2. Fallback: Verifica sensor de dobradiça para identificar dispositivo dobrável
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            // Sensor.TYPE_HINGE_ANGLE is 36
            val hingeSensor = sensorManager?.getDefaultSensor(36)
            val isFoldable = hingeSensor != null

            if (isFoldable) {
                // EN We use Jetpack WindowManager to get the device's maximum window size
                // PT Usamos Jetpack WindowManager para obter o tamanho máximo da janela do dispositivo
                val maxMetrics = WindowMetricsCalculator.getOrCreate().computeMaximumWindowMetrics(context)
                val maxBounds = maxMetrics.bounds
                val density = context.resources.displayMetrics.density

                val maxSwPx = kotlin.math.min(maxBounds.width(), maxBounds.height())
                val maxSwDp = maxSwPx / density

                val isFold = maxSwDp >= 600f
                val currentSwDp = config.smallestScreenWidthDp

                return if (isFold) {
                    if (currentSwDp >= 600) FOLD_OPEN else FOLD_CLOSED
                } else {
                    // EN Flips when open have normal sizes, but when closed they are tiny
                    // PT Flips quando abertos têm tamanhos normais, mas quando fechados são minúsculos
                    if (currentSwDp < 400 && config.screenHeightDp < 400) FLIP_CLOSED else FLIP_OPEN
                }
            }

            // EN The mask is used to extract only the UI Mode TYPE, ignoring night/other flags.
            // PT A máscara é usada para extrair apenas o TIPO do UI Mode, ignorando flags noturnas/outras.
            val type = config.uiMode and Configuration.UI_MODE_TYPE_MASK

            if (type == Configuration.UI_MODE_TYPE_TELEVISION ||
                context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)) {
                return TELEVISION
            }

            return entries.firstOrNull { it.configValue == type } ?: NORMAL // Returns NORMAL as default
        }
    }
}