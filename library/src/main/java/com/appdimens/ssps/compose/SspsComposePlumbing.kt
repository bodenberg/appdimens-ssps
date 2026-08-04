/**
 * Author & Developer: Jean Bodenberg
 * GIT: https://github.com/bodenberg/appdimens-ssps.git
 *
 * Compose helpers for Activity resolution, WindowLayoutInfo collection, and UiMode caching.
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
 * Process-local [Context] → [Activity] cache. The wrapper chain is stable for a given
 * [Context] instance; [WeakHashMap] avoids retaining contexts after GC.
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

    @androidx.annotation.VisibleForTesting
    internal fun clearForTestsOnly() {
        activityByContext.clear()
    }
}

/**
 * Returns a non-null [Flow] of [WindowLayoutInfo] so [collectAsState] can be called
 * unconditionally (uses [emptyFlow] when no [Activity] is available).
 */
internal fun windowLayoutInfoFlowOrEmpty(activity: Activity?): Flow<WindowLayoutInfo> =
    activity?.let { WindowInfoTracker.getOrCreate(it).windowLayoutInfo(it) } ?: emptyFlow()

/** Remembers the current [FoldingFeature], if any. */
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
 * Remembers [UiModeType], keyed by fold semantics (`state`, `orientation`, `isSeparating`)
 * rather than the [FoldingFeature] instance identity.
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
