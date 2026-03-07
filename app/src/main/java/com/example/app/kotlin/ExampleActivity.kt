package com.example.app.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appdimens.ssps.code.DimenSsp
import com.appdimens.ssps.common.DpQualifier
import com.example.app.databinding.ActivitySspBinding

/**
 * @author Bodenberg
 * GIT: https://github.com/bodenberg/appdimens-sdps.git
 */
class ExampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySspBinding

    /**
     * EN Called when the activity is first created.
     *
     * PT Chamado quando a atividade é criada pela primeira vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // EN Data Binding Setup
        // PT Configuração do Data Binding
        binding = ActivitySspBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example by code
        DimenSsp.getResourceId(this, DpQualifier.SMALL_WIDTH, 25);
        DimenSsp.getResourceId(this, DpQualifier.WIDTH, 25);
        DimenSsp.getResourceId(this, DpQualifier.HEIGHT, 25);
    }

}