/**
 * @author Bodenberg
 *
 * EN UI-bound macrobenchmark runner for AppDimens SSPS rendering performance validation.
 *    Measures real scroll performance over a 1000-item LazyColumn using wall-clock timing.
 *    DOES NOT use measureNanoTime — uses currentTimeMillis start/end deltas.
 *    Must be called from the main thread (inside a LaunchedEffect or Main coroutine scope).
 *
 * PT Runner de macrobenchmark vinculado à UI para validação de performance de renderização.
 *    Mede a performance real de rolagem em um LazyColumn de 1000 itens com tempo de relógio.
 *    NÃO usa measureNanoTime — usa deltas start/end com currentTimeMillis.
 *    Deve ser chamado da thread principal (dentro de LaunchedEffect ou escopo Main).
 */
package com.example.app.compose.benchmark

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val TAG = "APPDIMENS_MACRO"

/** EN Total items in the LazyColumn stress list. PT Total de itens na lista de estresse LazyColumn. */
const val MACRO_ITEM_COUNT = 1_000

/** EN Assumed frame duration at 60fps in milliseconds. PT Duração de frame assumida a 60fps em ms. */
private const val FRAME_DURATION_MS = 16.67f

/**
 * EN Runs the macrobenchmark on the main thread: programmatically animates a scroll
 *    through all 1,000 items and measures the total elapsed wall-clock time.
 *    The LazyListState must already be attached to a rendered LazyColumn.
 *
 * PT Executa o macrobenchmark na thread principal: anima programaticamente uma rolagem
 *    por todos os 1.000 itens e mede o tempo decorrido total no relógio.
 *    O LazyListState deve estar vinculado a um LazyColumn já renderizado.
 *
 * @param listState     EN LazyListState attached to the benchmark list. PT Estado da lista de benchmark.
 * @param onPhaseChange EN Callback for phase transitions. PT Callback para transições de fase.
 */
suspend fun runMacroBenchmark(
    listState: LazyListState,
    onPhaseChange: (BenchmarkPhase) -> Unit
): MacroBenchmarkResult = withContext(Dispatchers.Main) {

    onPhaseChange(BenchmarkPhase.MACRO_RUN)

    // EN Ensure we start from the top before measuring
    // PT Garantir que começamos do topo antes de medir
    listState.scrollToItem(0)
    delay(100) // EN Brief settle delay PT Pausa breve para estabilizar

    // ── SCROLL PASS ───────────────────────────────────────────────────────────
    // EN Wall-clock timing of a full animated scroll from item 0 to item (MACRO_ITEM_COUNT - 1).
    // PT Cronometragem de relógio de uma rolagem animada completa do item 0 ao item (MACRO_ITEM_COUNT - 1).
    val startMs = System.currentTimeMillis()

    listState.animateScrollToItem(MACRO_ITEM_COUNT - 1)

    val endMs = System.currentTimeMillis()
    val scrollDurationMs = endMs - startMs

    // EN Scroll back to top (not counted in measurement)
    // PT Rolagem de volta ao topo (não contada na medição)
    listState.scrollToItem(0)

    // ── DERIVED METRICS ───────────────────────────────────────────────────────
    val estimatedFrames = (scrollDurationMs / FRAME_DURATION_MS).toInt().coerceAtLeast(1)
    val estimatedCostPerItemUs = (scrollDurationMs * 1_000f) / MACRO_ITEM_COUNT

    val notes = buildString {
        append("animateScrollToItem(${MACRO_ITEM_COUNT - 1}) · ")
        append("wall-clock · ")
        append("~${estimatedFrames} frames @ 60fps estimate")
    }

    // ── Logcat export ─────────────────────────────────────────────────────────
    Log.i(TAG, "╔══════════════════ MACRO BENCHMARK RESULT ══════════════════╗")
    Log.i(TAG, "║ Scroll duration: ${scrollDurationMs}ms")
    Log.i(TAG, "║ Items rendered: $MACRO_ITEM_COUNT")
    Log.i(TAG, "║ Est. cost/item: ${estimatedCostPerItemUs.formatUs()}")
    Log.i(TAG, "║ Est. frames @ 60fps: $estimatedFrames")
    Log.i(TAG, "║ Notes: $notes")
    Log.i(TAG, "╚════════════════════════════════════════════════════════════╝")

    MacroBenchmarkResult(
        scrollDurationMs       = scrollDurationMs,
        itemsRendered          = MACRO_ITEM_COUNT,
        estimatedCostPerItemUs = estimatedCostPerItemUs,
        estimatedFrames        = estimatedFrames,
        notes                  = notes,
    )
}