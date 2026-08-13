/**
 * @author Bodenberg
 *
 * EN Benchmark orchestration controller.
 *    Manages the BenchmarkPhase state machine, drives sequential execution,
 *    enforces isolation with cooldown delays, and exposes results as StateFlows.
 *
 * PT Controlador de orquestração de benchmark.
 *    Gerencia a máquina de estados BenchmarkPhase, conduz a execução sequencial,
 *    garante isolamento com delays de cooldown e expõe resultados como StateFlows.
 */
package com.example.app.compose.benchmark

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * EN Cooldown pause between phases, in milliseconds.
 *    Allows the GC to run and the JIT to reach a steady state before UI measurement begins.
 * PT Pausa de cooldown entre fases, em milissegundos.
 *    Permite que o GC execute e o JIT alcance estado estável antes da medição de UI começar.
 */
private const val COOLDOWN_MS = 500L

/**
 * EN Orchestrates the entire Micro + Macro benchmark pipeline.
 *
 * Usage:
 * ```
 * val controller = BenchmarkController(scope, context, listState)
 * controller.runFull()      // Calc → Micro → cooldown → Macro
 * controller.runMicroOnly() // Micro only
 * controller.runCalcOnly()  // Calculation only
 * controller.runMacroOnly() // Macro only
 * ```
 *
 * PT Orquestra todo o pipeline de benchmark Micro + Macro.
 *
 * @param scope     EN CoroutineScope (typically viewModelScope or rememberCoroutineScope). PT CoroutineScope.
 * @param context   EN Application context. PT Contexto da aplicação.
 * @param listState EN LazyListState attached to the 1000-item benchmark list. PT Estado da lista de benchmark.
 */
class BenchmarkController(
    private val scope: CoroutineScope,
    private val context: Context,
    private val listState: LazyListState
) {

    // ── Internal mutable state ─────────────────────────────────────────────

    private val _phase = MutableStateFlow(BenchmarkPhase.IDLE)
    private val _result = MutableStateFlow(BenchmarkResult())

    // ── Public observable state ────────────────────────────────────────────

    /** EN Current benchmark phase. PT Fase atual do benchmark. */
    val phase: StateFlow<BenchmarkPhase> = _phase.asStateFlow()

    /** EN Accumulated benchmark results. PT Resultados acumulados do benchmark. */
    val result: StateFlow<BenchmarkResult> = _result.asStateFlow()

    // ── Public API ─────────────────────────────────────────────────────────

    /**
     * EN Runs the full pipeline: Calculation → Micro → cooldown → Macro.
     * PT Executa o pipeline completo: Cálculo → Micro → cooldown → Macro.
     */
    fun runFull() {
        scope.launch {
            reset()

            // ── Phase 1: Calculation ──────────────────────────────────────
            val calc = runCalculationBenchmark(context) { _phase.value = it }
            _result.value = _result.value.copy(calculation = calc)

            delay(COOLDOWN_MS)

            // ── Phase 2: Micro ────────────────────────────────────────────
            val micro = runMicroBenchmark(context) { _phase.value = it }
            _result.value = _result.value.copy(micro = micro)

            // EN Cooldown between phases — reduces GC and JIT interference.
            // PT Cooldown entre fases — reduz interferência de GC e JIT.
            _phase.value = BenchmarkPhase.MACRO_IDLE
            delay(COOLDOWN_MS)

            // ── Phase 3: Macro ────────────────────────────────────────────
            val macro = runMacroBenchmark(listState) { _phase.value = it }
            _result.value = _result.value.copy(macro = macro)

            _phase.value = BenchmarkPhase.DONE
        }
    }

    /**
     * EN Runs only the Microbenchmark. Macro results will remain null.
     * PT Executa apenas o Microbenchmark. Resultados Macro permanecerão nulos.
     */
    fun runMicroOnly() {
        scope.launch {
            reset()
            val micro = runMicroBenchmark(context) { _phase.value = it }
            _result.value = _result.value.copy(micro = micro)
            _phase.value = BenchmarkPhase.DONE
        }
    }

    /**
     * EN Runs only the Calculation benchmark. Other results will remain null.
     * PT Executa apenas o benchmark de Cálculo. Outros resultados permanecerão nulos.
     */
    fun runCalcOnly() {
        scope.launch {
            reset()
            val calc = runCalculationBenchmark(context) { _phase.value = it }
            _result.value = _result.value.copy(calculation = calc)
            _phase.value = BenchmarkPhase.DONE
        }
    }

    /**
     * EN Runs only the Macrobenchmark. Micro results will remain null.
     *    Skips straight to MACRO_IDLE → MACRO_RUN.
     * PT Executa apenas o Macrobenchmark. Resultados Micro permanecerão nulos.
     *    Vai direto para MACRO_IDLE → MACRO_RUN.
     */
    fun runMacroOnly() {
        scope.launch {
            reset()
            _phase.value = BenchmarkPhase.MACRO_IDLE
            delay(200L) // EN Brief stabilization. PT Breve estabilização.
            val macro = runMacroBenchmark(listState) { _phase.value = it }
            _result.value = _result.value.copy(macro = macro)
            _phase.value = BenchmarkPhase.DONE
        }
    }

    /**
     * EN Returns true if a benchmark is currently in progress (not IDLE or DONE).
     * PT Retorna true se um benchmark está em andamento (não IDLE ou DONE).
     */
    val isRunning: Boolean
        get() = _phase.value != BenchmarkPhase.IDLE && _phase.value != BenchmarkPhase.DONE

    // ── Private helpers ────────────────────────────────────────────────────

    private fun reset() {
        _phase.value = BenchmarkPhase.IDLE
        _result.value = BenchmarkResult()
    }
}