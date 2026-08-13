package com.example.app.compose

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

/**
 * EN Automated test for BenchmarkActivity that triggers the full benchmark suite.
 * PT Teste automatizado para BenchmarkActivity que dispara a suíte completa de benchmark.
 */
class BenchmarkActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<BenchmarkActivity>()

    @Test
    fun runFullBenchmarkSuite() {
        // EN Wait for the dashboard to appear
        // PT Aguarda o dashboard aparecer
        composeTestRule.onNodeWithText("AppDimens SSPS Benchmark").assertIsDisplayed()

        // EN Locate the "Full Run" button and trigger it
        // PT Localiza o botão "Full Run" e o aciona
        composeTestRule.onNodeWithText("Full Run", substring = true)
            .assertIsEnabled()
            .performClick()

        // EN Wait for the benchmark to transition through phases.
        //    Micro + Cooldown + Macro (1000 items scroll) takes ~20s.
        //    We wait for the success message.
        // PT Aguarda o benchmark passar pelas fases.
        //    Micro + Cooldown + Macro (scroll de 1000 itens) leva ~20s.
        //    Aguardamos a mensagem de sucesso.
        composeTestRule.waitUntil(timeoutMillis = 40_000) {
            composeTestRule
                .onAllNodesWithText("Benchmark complete", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        // EN Assert final state
        // PT Garante o estado final
        composeTestRule.onNodeWithText("Benchmark complete", substring = true).assertIsDisplayed()
        
        // EN Log a message to signal the end of the automated session
        // PT Loga uma mensagem para sinalizar o fim da sessão automatizada
        println("APPDIMENS_TEST_FINISHED: BenchmarkActivity automation completed successfully.")
    }
}