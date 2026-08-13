/**
 * @author Bodenberg
 *
 * EN CPU-bound microbenchmark runner for AppDimens SSPS dimension resolution calls.
 *    Runs entirely OFF the main thread (Dispatchers.Default).
 *    Uses warmup + measurement phases with an accumulator to prevent dead-code elimination.
 *    Each call type is timed INDIVIDUALLY; a dedicated block measures the latency of a
 *    SINGLE 1sp call (ssp / hsp / wsp / sspa / sem).
 *
 * PT Runner de microbenchmark vinculado à CPU para chamadas de resolução de dimensão AppDimens SSPS.
 *    Executa completamente FORA da thread principal (Dispatchers.Default).
 *    Usa fases de aquecimento + medição com acumulador para prevenir eliminação de código morto.
 *    Cada tipo de chamada é cronometrado INDIVIDUALMENTE; um bloco dedicado mede a latência
 *    de UMA única chamada de 1sp (ssp / hsp / wsp / sspa / sem).
 */
package com.example.app.compose.benchmark

import android.content.Context
import android.os.Process
import android.util.Log
import com.appdimens.ssps.code.DimenSsp
import com.appdimens.ssps.code.ssp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.system.measureNanoTime

private const val TAG = "APPDIMENS_MICRO"

/** EN Warmup iterations — results are discarded. Primes the JIT compiler.
 *  PT Iterações de aquecimento — resultados são descartados. Aquece o compilador JIT. */
private const val WARMUP_ITERATIONS = 10_000

/** EN Measurement iterations per call block. PT Iterações de medição por bloco de chamada. */
private const val MEASURE_ITERATIONS = 100_000

/** EN Value used for the single-value with/without aspect-ratio comparison.
 *  PT Valor usado na comparação de valor único com/sem aspect ratio. */
private const val SINGLE_VALUE = 64f

/** EN Discards the call-site warmup transients before a timed block.
 *  PT Descarta os transientes de aquecimento do call-site antes de um bloco cronometrado. */
private const val BLOCK_WARMUP_ITERATIONS = 10_000

/**
 * EN Discards the call-site warmup transients before a timed block.
 * PT Descarta os transientes de aquecimento do call-site antes de um bloco cronometrado.
 */
private fun warmCallSite(call: () -> Float) {
    var acc = 0f
    repeat(BLOCK_WARMUP_ITERATIONS) { acc += call() }
    Log.v(TAG, "Call-site warmup done (acc=$acc)")
}

/**
 * EN Forces the CPU governor to ramp the current core to its peak frequency BEFORE any
 *    measurement, collapsing first-benchmark-family artifacts and run-to-run spread.
 * PT Força o governor da CPU a subir o núcleo atual à frequência de pico ANTES de qualquer
 *    medição, colapsando artefatos da primeira família e a dispersão entre execuções.
 */
private fun thermalRamp(millis: Long = 1_500L) {
    var acc = 0f
    val deadline = System.nanoTime() + millis * 1_000_000L
    var i = 0
    do {
        val x = (i++ and 0xFF) + 1
        acc += kotlin.math.sqrt(x.toDouble()).toFloat()
    } while (System.nanoTime() < deadline)
    Log.v(TAG, "Thermal ramp done (acc=$acc, ${millis}ms)")
}

/**
 * EN Runs the full microbenchmark suite off the main thread and returns structured results.
 *    Sequence: warmup (discarded) → ssp → hsp → wsp → sspa → SINGLE 1sp (ssp/hsp/wsp/sspa/sem)
 *    → single value with/without AR → ext-vs-api probes.
 *
 * PT Executa a suíte completa de microbenchmark fora da thread principal e retorna resultados estruturados.
 *    Sequência: aquecimento (descartado) → ssp → hsp → wsp → sspa → 1sp ÚNICO (ssp/hsp/wsp/sspa/sem)
 *    → valor único com/sem AR → probes ext-vs-api.
 *
 * @param context EN Android context needed for dimension resolution. PT Contexto Android para resolução de dimensão.
 * @param onPhaseChange EN Callback invoked when phase transitions occur. PT Callback invocado nas transições de fase.
 */
suspend fun runMicroBenchmark(
    context: Context,
    onPhaseChange: (BenchmarkPhase) -> Unit
): MicroBenchmarkResult = withContext(Dispatchers.Default) {

    // ── WARMUP PHASE ──────────────────────────────────────────────────────────
    onPhaseChange(BenchmarkPhase.MICRO_WARMUP)

    var warmupAcc = 0f
    repeat(WARMUP_ITERATIONS) {
        warmupAcc += DimenSsp.ssp(context, 100)
        warmupAcc += DimenSsp.hsp(context, 50)
        warmupAcc += DimenSsp.wsp(context, 30)
        warmupAcc += DimenSsp.sspa(context, 40)
    }
    Log.v(TAG, "Warmup complete (acc=$warmupAcc, ${WARMUP_ITERATIONS} iters discarded)")

    // ── MEASUREMENT PHASE ─────────────────────────────────────────────────────
    onPhaseChange(BenchmarkPhase.MICRO_RUN)
    // EN Hold real-time-ish priority for the whole measurement window.
    // PT Mantém prioridade quase-real para toda a janela de medição.
    try {
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
    } catch (_: SecurityException) {
    }

    thermalRamp()

    val startWall = System.currentTimeMillis()

    // ── ssp (XML resource path) ──────────────────────────────────────────────
    var sspAcc = 0f
    warmCallSite { DimenSsp.ssp(context, 100) }
    val sspNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { sspAcc += DimenSsp.ssp(context, 100) }
    }
    val sspAvgNs = sspNs / MEASURE_ITERATIONS

    // ── hsp ──────────────────────────────────────────────────────────────────
    var hspAcc = 0f
    warmCallSite { DimenSsp.hsp(context, 50) }
    val hspNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { hspAcc += DimenSsp.hsp(context, 50) }
    }
    val hspAvgNs = hspNs / MEASURE_ITERATIONS

    // ── wsp ──────────────────────────────────────────────────────────────────
    var wspAcc = 0f
    warmCallSite { DimenSsp.wsp(context, 30) }
    val wspNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { wspAcc += DimenSsp.wsp(context, 30) }
    }
    val wspAvgNs = wspNs / MEASURE_ITERATIONS

    // ── sspa (aspect-ratio path) ─────────────────────────────────────────────
    var sspaAcc = 0f
    warmCallSite { DimenSsp.sspa(context, 40) }
    val sspaNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { sspaAcc += DimenSsp.sspa(context, 40) }
    }
    val sspaAvgNs = sspaNs / MEASURE_ITERATIONS

    // ── SINGLE 1sp — the per-call cost of ONE sp ──────────────────────────────
    // EN Back-to-back timing of value=1 through every path. This is the number a
    //    developer sees per single .ssp / .hsp / .wsp / .sspa / .sem call.
    // PT Cronometragem consecutiva do valor=1 em todos os caminhos. É o número que
    //    um desenvolvedor vê por chamada única .ssp / .hsp / .wsp / .sspa / .sem.
    var singleSpAcc = 0f

    warmCallSite { DimenSsp.ssp(context, 1) }
    val singleSpSspNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { singleSpAcc += DimenSsp.ssp(context, 1) }
    }
    val singleSpSspAvgNs = singleSpSspNs / MEASURE_ITERATIONS

    warmCallSite { DimenSsp.hsp(context, 1) }
    val singleSpHspNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { singleSpAcc += DimenSsp.hsp(context, 1) }
    }
    val singleSpHspAvgNs = singleSpHspNs / MEASURE_ITERATIONS

    warmCallSite { DimenSsp.wsp(context, 1) }
    val singleSpWspNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { singleSpAcc += DimenSsp.wsp(context, 1) }
    }
    val singleSpWspAvgNs = singleSpWspNs / MEASURE_ITERATIONS

    warmCallSite { DimenSsp.sspa(context, 1) }
    val singleSpSspaNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { singleSpAcc += DimenSsp.sspa(context, 1) }
    }
    val singleSpSspaAvgNs = singleSpSspaNs / MEASURE_ITERATIONS

    warmCallSite { DimenSsp.sem(context, 1) }
    val singleSpSemNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { singleSpAcc += DimenSsp.sem(context, 1) }
    }
    val singleSpSemAvgNs = singleSpSemNs / MEASURE_ITERATIONS

    // ── Single value: same value, with vs without AR ─────────────────────────
    var singleNoArAcc = 0f
    warmCallSite { DimenSsp.ssp(context, SINGLE_VALUE.toInt()) }
    val singleNoArNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { singleNoArAcc += DimenSsp.ssp(context, SINGLE_VALUE.toInt()) }
    }
    val singleNoArAvgNs = singleNoArNs / MEASURE_ITERATIONS

    var singleWithArAcc = 0f
    warmCallSite { DimenSsp.sspa(context, SINGLE_VALUE.toInt()) }
    val singleWithArNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { singleWithArAcc += DimenSsp.sspa(context, SINGLE_VALUE.toInt()) }
    }
    val singleWithArAvgNs = singleWithArNs / MEASURE_ITERATIONS

    // ── Direct-call probes ────────────────────────────────────────────────────
    // EN Isolates the wrapper overhead: extension `100.ssp(ctx)` vs the public
    //    API `DimenSsp.ssp(ctx, 100)`.
    // PT Isola o overhead do wrapper: extensão `100.ssp(ctx)` vs a API pública
    //    `DimenSsp.ssp(ctx, 100)`.
    var extSspAcc = 0f
    var apiSspAcc = 0f

    warmCallSite { 100.ssp(context) }
    val extSspNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { extSspAcc += 100.ssp(context) }
    }
    val extSspAvgNs = extSspNs / MEASURE_ITERATIONS

    warmCallSite { DimenSsp.ssp(context, 100) }
    val apiSspNs = measureNanoTime {
        repeat(MEASURE_ITERATIONS) { apiSspAcc += DimenSsp.ssp(context, 100) }
    }
    val apiSspAvgNs = apiSspNs / MEASURE_ITERATIONS

    val endWall = System.currentTimeMillis()
    val totalWallMs = endWall - startWall

    try {
        Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT)
    } catch (_: SecurityException) {
    }

    // ── Combined average across the 6 core blocks ────────────────────────────
    val totalOps = MEASURE_ITERATIONS * 6
    val combinedNs = sspNs + hspNs + wspNs + sspaNs + singleNoArNs + singleWithArNs
    val combinedAvgNs = combinedNs / totalOps

    // ── Anti-dead-code accumulator checksum ──────────────────────────────────
    val checksum = sspAcc + hspAcc + wspAcc + sspaAcc + singleSpAcc +
        singleNoArAcc + singleWithArAcc + extSspAcc + apiSspAcc

    val singleSsp = SingleSspBenchmarkResult(
        sspAvgNs  = singleSpSspAvgNs,
        hspAvgNs  = singleSpHspAvgNs,
        wspAvgNs  = singleSpWspAvgNs,
        sspaAvgNs = singleSpSspaAvgNs,
        semAvgNs  = singleSpSemAvgNs,
        valuePx   = DimenSsp.ssp(context, 1),
        density   = context.resources.displayMetrics.density,
    )

    // ── Logcat export ─────────────────────────────────────────────────────────
    Log.i(TAG, "╔══════════════════ MICRO BENCHMARK RESULT ══════════════════╗")
    Log.i(TAG, "║ Combined avg: ${combinedAvgNs.formatNs()}/op · Total ops: $totalOps")
    Log.i(TAG, "║ ssp  (XML)   : ${sspAvgNs.formatNs()}/op")
    Log.i(TAG, "║ hsp  (XML)   : ${hspAvgNs.formatNs()}/op")
    Log.i(TAG, "║ wsp  (XML)   : ${wspAvgNs.formatNs()}/op")
    Log.i(TAG, "║ sspa (+AR)   : ${sspaAvgNs.formatNs()}/op")
    Log.i(TAG, "║ SINGLE 1sp ssp : ${singleSpSspAvgNs.formatNs()}/call (value=${"%.2f".format(singleSsp.valuePx)}px)")
    Log.i(TAG, "║ SINGLE 1sp hsp : ${singleSpHspAvgNs.formatNs()}/call")
    Log.i(TAG, "║ SINGLE 1sp wsp : ${singleSpWspAvgNs.formatNs()}/call")
    Log.i(TAG, "║ SINGLE 1sp sspa: ${singleSpSspaAvgNs.formatNs()}/call")
    Log.i(TAG, "║ SINGLE 1sp sem : ${singleSpSemAvgNs.formatNs()}/call")
    Log.i(TAG, "║ single $SINGLE_VALUE no-AR: ${singleNoArAvgNs.formatNs()}/op")
    Log.i(TAG, "║ single $SINGLE_VALUE +AR  : ${singleWithArAvgNs.formatNs()}/op")
    Log.i(TAG, "║ direct ext 100.ssp(ctx) : ${extSspAvgNs.formatNs()}/op")
    Log.i(TAG, "║ direct api DimenSsp.ssp : ${apiSspAvgNs.formatNs()}/op")
    Log.i(TAG, "║ Total wall time: ${totalWallMs}ms")
    Log.i(TAG, "║ Accumulator checksum: $checksum")
    Log.i(TAG, "╚════════════════════════════════════════════════════════════╝")

    MicroBenchmarkResult(
        avgNsPerOp          = combinedAvgNs,
        totalOps            = totalOps,
        totalTimeMs         = totalWallMs,
        sspBypassAvgNs      = sspAvgNs,
        hspBypassAvgNs      = hspAvgNs,
        wspBypassAvgNs      = wspAvgNs,
        sspaCacheAvgNs      = sspaAvgNs,
        singleNoArAvgNs     = singleNoArAvgNs,
        singleWithArAvgNs   = singleWithArAvgNs,
        singleValue         = SINGLE_VALUE,
        extSspAvgNs         = extSspAvgNs,
        apiSspAvgNs         = apiSspAvgNs,
        accumulatorChecksum = checksum,
        singleSsp           = singleSsp,
    )
}