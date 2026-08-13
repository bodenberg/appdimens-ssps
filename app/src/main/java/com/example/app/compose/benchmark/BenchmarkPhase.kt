/**
 * @author Bodenberg
 *
 * EN State machine enum for the benchmark orchestration system.
 *    Enforces strict sequential isolation: Calc MUST complete before Micro starts.
 * PT Enum de máquina de estados para o sistema de orquestração de benchmarks.
 *    Garante isolamento sequencial estrito: Calc DEVE concluir antes do Micro iniciar.
 */
package com.example.app.compose.benchmark

/**
 * EN Represents the current phase of the benchmark execution pipeline.
 * PT Representa a fase atual do pipeline de execução do benchmark.
 */
enum class BenchmarkPhase {

    /** EN Initial state. No benchmark is running. PT Estado inicial. Nenhum benchmark em execução. */
    IDLE,

    /** EN Calculation benchmark warmup phase. PT Fase de aquecimento do benchmark de cálculo. */
    CALC_WARMUP,

    /** EN Calculation benchmark measurement phase. PT Fase de medição do benchmark de cálculo. */
    CALC_RUN,

    /** EN Microbenchmark warmup phase. JIT is priming, results are discarded.
     *  PT Fase de aquecimento do microbenchmark. JIT está aquecendo, resultados são descartados. */
    MICRO_WARMUP,

    /** EN Microbenchmark measurement phase. CPU-bound, off main thread.
     *  PT Fase de medição do microbenchmark. Vinculada à CPU, fora da thread principal. */
    MICRO_RUN,

    /** EN Cooldown pause between micro and macro. Allows GC, JIT stabilization.
     *  PT Pausa de cooldown entre micro e macro. Permite GC, estabilização do JIT. */
    MACRO_IDLE,

    /** EN Macrobenchmark measurement phase. UI-bound, runs on main thread.
     *  PT Fase de medição do macrobenchmark. Vinculada à UI, executa na thread principal. */
    MACRO_RUN,

    /** EN All phases complete. Results are ready for display and export.
     *  PT Todas as fases concluídas. Resultados prontos para exibição e exportação. */
    DONE
}

/**
 * EN Returns a human-readable display label for UI rendering.
 * PT Retorna um rótulo legível para renderização da UI.
 */
val BenchmarkPhase.displayLabel: String
    get() = when (this) {
        BenchmarkPhase.IDLE        -> "Idle — Ready to run"
        BenchmarkPhase.CALC_WARMUP -> "Calculation: Warming up JIT…"
        BenchmarkPhase.CALC_RUN    -> "Calculation: Measuring mixed path…"
        BenchmarkPhase.MICRO_WARMUP -> "Micro: Warming up JIT…"
        BenchmarkPhase.MICRO_RUN   -> "Micro: Measuring CPU performance…"
        BenchmarkPhase.MACRO_IDLE  -> "Cooldown: Stabilizing environment…"
        BenchmarkPhase.MACRO_RUN   -> "Macro: Measuring UI scroll performance…"
        BenchmarkPhase.DONE        -> "Done — Results ready"
    }

/**
 * EN Returns an approximate progress fraction [0.0, 1.0] for the progress indicator.
 * PT Retorna uma fração de progresso aproximada [0.0, 1.0] para o indicador de progresso.
 */
val BenchmarkPhase.progressFraction: Float
    get() = when (this) {
        BenchmarkPhase.IDLE         -> 0f
        BenchmarkPhase.CALC_WARMUP  -> 0.05f
        BenchmarkPhase.CALC_RUN     -> 0.15f
        BenchmarkPhase.MICRO_WARMUP -> 0.30f
        BenchmarkPhase.MICRO_RUN    -> 0.55f
        BenchmarkPhase.MACRO_IDLE   -> 0.70f
        BenchmarkPhase.MACRO_RUN    -> 0.90f
        BenchmarkPhase.DONE         -> 1f
    }