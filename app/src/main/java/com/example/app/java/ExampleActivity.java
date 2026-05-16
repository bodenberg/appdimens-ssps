/**
 * @author Bodenberg
 * GIT: https://github.com/bodenberg/appdimens-sdps.git
 */
package com.example.app.java;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.appdimens.ssps.code.DimenSsp;
import com.appdimens.ssps.code.ScaledSp;
import com.appdimens.ssps.common.DpQualifier;
import com.appdimens.ssps.common.UiModeType;
import com.example.app.databinding.ActivitySspBinding;

/**
 * EN Main activity demonstrating various features of the AppDimens library in Java.
 *
 * PT Atividade principal que demonstra vários recursos da biblioteca AppDimens em Java.
 */
public class ExampleActivity extends AppCompatActivity {

    private ActivitySspBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySspBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DimenSsp.warmupSspsFactors(this);

        // EN Direct resolution (Static methods for Java)
        // PT Resolução direta (Métodos estáticos para Java)
        float sspValue = DimenSsp.ssp(this, 16);
        float hspValue = DimenSsp.hsp(this, 18);
        float wspValue = DimenSsp.wsp(this, 18);

        // EN Inverter Shortcuts
        // PT Atalhos com Inversão
        float sspPhValue = DimenSsp.sspPh(this, 16);
        float sspLwValue = DimenSsp.sspLw(this, 16);

        // EN Without Font Scale
        // PT Sem Escala de Fonte
        float semValue = DimenSsp.sem(this, 16);

        float compareSspPx = DimenSsp.ssp(this, 32);
        float compareSspaPx = DimenSsp.sspa(this, 32);
        float compareHspPx = DimenSsp.hsp(this, 22);
        float compareHspaPx = DimenSsp.hspa(this, 22);
        Log.d("AppDimensSspJava", "AR compare 32.ssp(px)=" + compareSspPx + " 32.sspa(px)=" + compareSspaPx
                + " 22.hsp(px)=" + compareHspPx + " 22.hspa(px)=" + compareHspaPx);

        // EN Facilitators
        // PT Facilitadores
        float rotateValue = DimenSsp.sspRotate(this, 16, 24); // Defaults to Landscape
        float modeValue = DimenSsp.sspMode(this, 16, 30, UiModeType.TELEVISION);

        // EN ScaledSp Builder (Fluent API)
        // PT Builder ScaledSp (API Fluente)
        float scaledValue = DimenSsp.scaled(10)
                .screen(UiModeType.TELEVISION, DpQualifier.SMALL_WIDTH, 720, 24)
                .screen(DpQualifier.SMALL_WIDTH, 600, 18)
                .ssp(this);

        // EN Resource ID resolution
        // PT Resolução de ID de recurso
        int sspResId = DimenSsp.sspRes(this, 25);

        Log.d("AppDimensSspJava", "ssp: " + sspValue + ", scaled: " + scaledValue + ", resId: " + sspResId);
    }
}