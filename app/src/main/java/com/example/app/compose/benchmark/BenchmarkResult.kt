/**
 * @author Bodenberg
 *
 * EN Data models for benchmark results of the AppDimens SSPS library.
 *    MicroBenchmarkResult: per-call-type averages from CPU-bound measurement.
 *    SingleSspBenchmarkResult: latency of a SINGLE 1sp call (user-facing metric).
 *    MacroBenchmarkResult: scroll timing and cost estimates from UI-bound measurement.
 * PT Modelos de dados para resultados de benchmark da biblioteca AppDimens SSPS.
 *    MicroBenchmarkResult: médias por tipo de chamada da medição vinculada à CPU.
 *    SingleSspBenchmarkResult: latência de UMA única chamada de 1sp (métrica de face ao usuário).
 *    MacroBenchmarkResult: tempo de rolagem e estimativas de custo da medição vinculada à UI.
 */
package com.example.app.compose.benchmark

/**
 * EN Latency of a SINGLE 1sp call through each resolution path.
 * PT Latência de UMA única chamada de 1sp em cada caminho de resolução.
 *
 * @param sspAvgNs  EN Avg ns per single `DimenSsp.ssp(ctx, 1)` call. PT Média ns por chamada única `DimenSsp.ssp(ctx, 1)`.
 * @param hspAvgNs  EN Avg ns per single `DimenSsp.hsp(ctx, 1)` call. PT Média ns por chamada única `DimenSsp.hsp(ctx, 1)`.
 * @param wspAvgNs  EN Avg ns per single `DimenSsp.wsp(ctx, 1)` call. PT Média ns por chamada única `DimenSsp.wsp(ctx, 1)`.
 * @param sspaAvgNs EN Avg ns per single `DimenSsp.sspa(ctx, 1)` call (with AR). PT Média ns por chamada única `DimenSsp.sspa(ctx, 1)` (com AR).
 * @param semAvgNs  EN Avg ns per single `DimenSsp.sem(ctx, 1)` call (no font scale). PT Média ns por chamada única `DimenSsp.sem(ctx, 1)` (sem escala de fonte).
 * @param valuePx   EN Resolved pixel value of 1.ssp on this device. PT Valor em pixel resolvido de 1.ssp neste device.
 * @param density   EN Display density at measurement time. PT Densidade do display na medição.
 */
data class SingleSspBenchmarkResult(
    val sspAvgNs: Long,
    val hspAvgNs: Long,
    val wspAvgNs: Long,
    val sspaAvgNs: Long,
    val semAvgNs: Long,
    val valuePx: Float,
    val density: Float,
)

/**
 * EN Results from the Microbenchmark runner.
 *    Each call type (ssp, hsp, wsp = XML bypass path; sspa = aspect-ratio path) is timed
 *    individually to expose the performance of each resolution route.
 * PT Resultados do runner de Microbenchmark.
 *    Cada tipo de chamada (ssp, hsp, wsp = caminho XML bypass; sspa = caminho aspect ratio)
 *    é cronometrado individualmente para expor o desempenho de cada rota de resolução.
 *
 * @param avgNsPerOp    EN Combined average ns per operation across the 6 core blocks.
 *                      PT Média combinada ns/op nos 6 blocos centrais.
 * @param totalOps      EN Total core operations measured. PT Total de operações centrais medidas.
 * @param totalTimeMs   EN Total elapsed measurement time in ms. PT Tempo total de medição em ms.
 * @param sspBypassAvgNs EN Avg ns per ssp() call (XML resource path). PT Média ns por chamada ssp() (recurso XML).
 * @param hspBypassAvgNs EN Avg ns per hsp() call. PT Média ns por chamada hsp().
 * @param wspBypassAvgNs EN Avg ns per wsp() call. PT Média ns por chamada wsp().
 * @param sspaCacheAvgNs EN Avg ns per sspa() call (aspect-ratio path). PT Média ns por chamada sspa() (aspect ratio).
 * @param singleNoArAvgNs EN Avg ns per single-value resolution WITHOUT aspect ratio. PT Média ns por resolução de um único valor SEM aspect ratio.
 * @param singleWithArAvgNs EN Avg ns per single-value resolution WITH aspect ratio. PT Média ns por resolução de um único valor COM aspect ratio.
 * @param singleValue EN The value used for the with/without AR comparison. PT Valor usado na comparação com/sem AR.
 * @param extSspAvgNs EN SCALED-only: avg ns/op for the direct extension call `100.ssp(ctx)`.
 *                     PT Apenas SCALED: média ns/op da chamada de extensão direta `100.ssp(ctx)`.
 * @param apiSspAvgNs EN SCALED-only: avg ns/op for the public API call `DimenSsp.ssp(ctx, 100)`.
 *                     PT Apenas SCALED: média ns/op da chamada de API pública `DimenSsp.ssp(ctx, 100)`.
 * @param accumulatorChecksum EN Accumulator value to prove results were consumed (anti-dead-code). PT Valor acumulador.
 * @param singleSsp EN Single-1sp latency measurement. PT Medição de latência de um único 1sp.
 */
data class MicroBenchmarkResult(
    val avgNsPerOp: Long,
    val totalOps: Int,
    val totalTimeMs: Long,
    val sspBypassAvgNs: Long,
    val hspBypassAvgNs: Long,
    val wspBypassAvgNs: Long,
    val sspaCacheAvgNs: Long,
    val singleNoArAvgNs: Long,
    val singleWithArAvgNs: Long,
    val singleValue: Float,
    val extSspAvgNs: Long,
    val apiSspAvgNs: Long,
    val accumulatorChecksum: Float,
    val singleSsp: SingleSspBenchmarkResult,
)

/**
 * EN Results from the Calculation Benchmark runner.
 *    Measures the average latency of mixed dimension resolutions (ssp, hsp, wsp, sspa)
 *    in a single tight loop to simulate real-world usage patterns.
 * PT Resultados do runner de Benchmark de Cálculo.
 *    Mede a latência média de resoluções de dimensão mistas (ssp, hsp, wsp, sspa)
 *    em um único loop para simular padrões de uso do mundo real.
 *
 * @param avgNsPerRes EN Average nanoseconds per resolution call. PT Média de nanossegundos por chamada.
 * @param totalOps    EN Total operations (calls) measured. PT Total de operações (chamadas) medidas.
 * @param throughput  EN Formatted string showing the call-type mix. PT String formatada mostrando o mix.
 */
data class CalculationBenchmarkResult(
    val avgNsPerRes: Long,
    val totalOps: Int,
    val throughput: String,
)

/**
 * EN Results from the Macrobenchmark runner.
 *    Measures real UI scroll performance across 1,000 items using wall-clock timing.
 *    Does NOT use measureNanoTime — uses currentTimeMillis start/end deltas.
 * PT Resultados do runner de Macrobenchmark.
 *    Mede a performance real de rolagem da UI em 1.000 itens usando tempo de relógio.
 *    NÃO usa measureNanoTime — usa deltas de start/end com currentTimeMillis.
 *
 * @param scrollDurationMs       EN Full scroll pass duration in ms. PT Duração da passagem de rolagem em ms.
 * @param itemsRendered          EN Number of items in the LazyColumn. PT Número de itens no LazyColumn.
 * @param estimatedCostPerItemUs EN Estimated rendering cost per item in µs. PT Custo estimado por item em µs.
 * @param estimatedFrames        EN Estimated frame count at 60fps. PT Contagem de frames estimada a 60fps.
 * @param notes                  EN Additional context or observations. PT Contexto ou observações adicionais.
 */
data class MacroBenchmarkResult(
    val scrollDurationMs: Long,
    val itemsRendered: Int,
    val estimatedCostPerItemUs: Float,
    val estimatedFrames: Int,
    val notes: String,
)

/**
 * EN Unified benchmark result container. All fields are nullable since the user may
 *    choose to run only Micro or only Macro.
 * PT Contêiner unificado de resultados de benchmark. Todos os campos são nulos pois o usuário
 *    pode optar por executar apenas Micro ou apenas Macro.
 *
 * @param calculation EN Calculation benchmark result, or null if not run. PT Resultado do benchmark de cálculo, ou null.
 * @param micro       EN Microbenchmark result, or null if not run. PT Resultado do microbenchmark, ou null.
 * @param macro       EN Macrobenchmark result, or null if not run. PT Resultado do macrobenchmark, ou null.
 */
data class BenchmarkResult(
    val calculation: CalculationBenchmarkResult? = null,
    val micro: MicroBenchmarkResult? = null,
    val macro: MacroBenchmarkResult? = null,
)

// ─── Formatting helpers ────────────────────────────────────────────────────────

/** EN Formats a nanosecond value into a readable string with appropriate unit. */
fun Long.formatNs(): String = when {
    this < 1_000L     -> "$this ns"
    this < 1_000_000L -> "${"%.1f".format(this / 1_000.0)} µs"
    else              -> "${"%.2f".format(this / 1_000_000.0)} ms"
}

/** EN Formats a float microsecond value with 2 decimal places. */
fun Float.formatUs(): String = "${"%.2f".format(this)} µs"