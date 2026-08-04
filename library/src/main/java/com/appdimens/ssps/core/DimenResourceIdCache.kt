/**
 * Shared cache for [android.content.res.Resources.getIdentifier] lookups.
 *
 * Resource IDs for a given `(packageName, dimenName)` are stable for the process lifetime;
 * configuration only changes the *value* returned by [android.content.res.Resources.getDimension],
 * not the ID. Caching avoids the expensive name→id scan on every `.ssp` / Compose resolve.
 */
package com.appdimens.ssps.core

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.annotation.VisibleForTesting
import java.util.concurrent.ConcurrentHashMap

object DimenResourceIdCache {
    private const val DIMEN_TYPE = "dimen"

    /** Key: `"$packageName\u0000$dimenName"` → resource id (0 = missing). */
    private val idByPackageAndName = ConcurrentHashMap<String, Int>(256)

    @SuppressLint("DiscouragedApi")
    fun getOrResolve(resources: Resources, packageName: String, dimenName: String): Int {
        val key = buildKey(packageName, dimenName)
        idByPackageAndName[key]?.let { return it }
        val id = resources.getIdentifier(dimenName, DIMEN_TYPE, packageName)
        // EN Cache misses (0) too — avoids repeated failed scans for out-of-range / typos.
        // PT Também cacheia misses (0) — evita scans repetidos para valores ausentes.
        val raced = idByPackageAndName.putIfAbsent(key, id)
        return raced ?: id
    }

    @VisibleForTesting
    internal fun resetForTestsOnly() {
        idByPackageAndName.clear()
    }

    @VisibleForTesting
    internal fun cachedSizeForTestsOnly(): Int = idByPackageAndName.size

    private fun buildKey(packageName: String, dimenName: String): String =
        "$packageName\u0000$dimenName"
}
