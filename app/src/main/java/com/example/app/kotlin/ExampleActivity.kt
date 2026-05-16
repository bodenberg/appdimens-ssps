package com.example.app.kotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.appdimens.ssps.code.*
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.UiModeType
import com.example.app.databinding.ActivitySspBinding

/**
 * @author Bodenberg
 * GIT: https://github.com/bodenberg/appdimens-sdps.git
 */
class ExampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySspBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySspBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DimenSsp.warmupSspsFactors(applicationContext)

        // EN Direct usage by code (Extensions)
        // PT Uso direto via código (Extensões)
        val sspValue = 16.ssp(this)
        val hspValue = 18.hsp(this)
        val wspValue = 18.wsp(this)

        // EN Inverter Shortcuts
        // PT Atalhos com Inversão
        val sspPhValue = 16.sspPh(this)
        val sspLwValue = 16.sspLw(this)

        // EN Without Font Scale
        // PT Sem Escala de Fonte
        val semValue = 16.sem(this)

        // EN Aspect ratio vs base (pixels after resource + font scale; *a applies geometry multiplier)
        // PT Aspect ratio vs base (pixels após recurso + escala de fonte; *a aplica multiplicador geométrico)
        val compareSspPx = 32.ssp(this)
        val compareSspaPx = 32.sspa(this)
        Log.d(
            "AppDimensSsp",
            "AR compare 32.ssp(px)=$compareSspPx 32.sspa(px)=$compareSspaPx " +
                    "22.hsp(px)=${22.hsp(this)} 22.hspa(px)=${22.hspa(this)}",
        )

        // EN Facilitators
        // PT Facilitadores
        val rotateValue = 16.sspRotate(this, rotationValue = 24)
        val modeValue = 16.sspMode(this, modeValue = 30, uiModeType = UiModeType.TELEVISION)

        // EN ScaledSp Builder
        // PT Builder ScaledSp
        val scaledSp = DimenSsp.scaled(10)
            .screen(UiModeType.TELEVISION, DpQualifier.SMALL_WIDTH, 720, 24)
            .screen(DpQualifier.SMALL_WIDTH, 600, 18)
            .ssp(this)

        // EN Resource ID resolution
        // PT Resolução de ID de recurso
        val sspResId = 25.sspRes(this)

        Log.d("AppDimensSsp", "ssp: $sspValue, resId: $sspResId, scaled: $scaledSp")

        // EN Example: applying to a TextView
        // PT Exemplo: aplicando em um TextView
        // binding.textView.textSize = 16.ssp(this) // Note: context.resources.getDimension already returns pixels
    }
}