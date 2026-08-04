/**
 * Author & Developer: Jean Bodenberg
 * GIT: https://github.com/bodenberg/appdimens-ssps.git
 *
 * Shared Compose plumbing for SSPS — Activity lookup cache, unconditional
 * WindowLayoutInfo collection, and UiMode remember helpers.
 * Mirrors the non-modularization fixes from appdimens-dynamic (P8 / P9).
 */
package com.appdimens.ssps.compose

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import com.appdimens.ssps.common.UiModeType
import java.util.Collections
import java.util.WeakHashMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * EN Lightweight [Context] → [Activity] cache (P8). The wrapper chain is stable for the
 * lifetime of a given [Context] instance. [WeakHashMap] avoids retaining Contexts past GC.
 *
 * PT Cache leve Context→Activity; a cadeia de wrappers é estável por instância.
 */
internal object SspsActivityCache {
    private val activityByContext: MutableMap<Context, Activity?> =
        Collections.synchronizedMap(WeakHashMap())

    fun findActivity(context: Context): Activity? {
        synchronized(activityByContext) {
            if (activityByContext.containsKey(context)) {
                return activityByContext[context]
            }
            var ctx: Context = context
            var found: Activity? = null
            while (ctx is ContextWrapper) {
                if (ctx is Activity) {
                    found = ctx
                    break
                }
                ctx = ctx.baseContext
            }
            activityByContext[context] = found
            return found
        }
    }

    /** EN Clears the Context→Activity cache (tests). PT Limpa o cache Context→Activity. */
    @androidx.annotation.VisibleForTesting
    internal fun clearForTestsOnly() {
        activityByContext.clear()
    }
}

/**
 * EN Resolves the [WindowLayoutInfo] flow. Always returns a non-null [Flow] so
 * [collectAsState] can be called unconditionally (P9).
 *
 * PT Resolve o Flow de [WindowLayoutInfo]; sempre não-nulo para collectAsState incondicional.
 */
internal fun windowLayoutInfoFlowOrEmpty(activity: Activity?): Flow<WindowLayoutInfo> =
    activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) } ?: emptyFlow()

/**
 * EN Remembers the current [FoldingFeature], collecting [WindowLayoutInfo] unconditionally.
 * PT Lembra o [FoldingFeature] atual com collectAsState incondicional.
 */
@Composable
internal fun rememberFoldingFeature(): FoldingFeature? {
    val context = LocalContext.current
    val activity = remember(context) { SspsActivityCache.findActivity(context) }
    val flow = remember(activity) { windowLayoutInfoFlowOrEmpty(activity) }
    val windowLayoutInfo = flow.collectAsState(initial = null)
    return windowLayoutInfo.value?.displayFeatures
        ?.filterIsInstance<FoldingFeature>()
        ?.firstOrNull()
}

/**
 * EN Remembers [UiModeType], keyed on fold semantics (state / orientation / isSeparating)
 * rather than the [FoldingFeature] instance — WindowLayoutInfo often re-emits a new object
 * with identical semantics.
 *
 * PT Lembra [UiModeType] com chaves semânticas do fold, não a instância.
 */
@Composable
internal fun rememberCurrentUiModeType(): UiModeType {
    val context = LocalContext.current
    val foldingFeature = rememberFoldingFeature()
    return remember(
        context,
        foldingFeature?.state,
        foldingFeature?.orientation,
        foldingFeature?.isSeparating,
    ) {
        UiModeType.fromConfiguration(context, foldingFeature)
    }
}
