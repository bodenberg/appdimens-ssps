/**
 * @author Bodenberg
 *
 * EN Production-grade benchmark dashboard for the AppDimens SSPS library.
 *    Hosts a BenchmarkController that drives sequential Micro → Macro execution,
 *    and displays a structured four-section results panel with premium UI.
 *    Includes a dedicated section for the speed of a SINGLE 1sp call.
 *
 * PT Dashboard de benchmark de nível de produção para a biblioteca AppDimens SSPS.
 *    Hospeda um BenchmarkController que conduz a execução sequencial Micro → Macro,
 *    e exibe um painel de resultados estruturado em quatro seções com UI premium.
 *    Inclui uma seção dedicada à velocidade de UMA única chamada de 1sp.
 */
package com.example.app.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appdimens.ssps.compose.hsp
import com.appdimens.ssps.compose.ssp
import com.appdimens.ssps.compose.wsp
import com.example.app.compose.benchmark.*

// ═══════════════════════════════════════════════════════════════════════════════
// COLOUR PALETTE
// ═══════════════════════════════════════════════════════════════════════════════

private val DarkBg       = Color(0xFF0D0F14)
private val SurfaceCard  = Color(0xFF161B24)
private val SurfaceBorder= Color(0xFF252D3D)
private val AccentCyan   = Color(0xFF00E5FF)
private val AccentGreen  = Color(0xFF69FF47)
private val AccentAmber  = Color(0xFFFFD740)
private val AccentPurple = Color(0xFFB388FF)
private val TextPrimary  = Color(0xFFECF0F8)
private val TextSecondary= Color(0xFF8A95A8)
private val AccentRed    = Color(0xFFFF5252)

// ═══════════════════════════════════════════════════════════════════════════════
// ACTIVITY
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * EN Activity that hosts the full production-grade benchmark dashboard.
 * PT Atividade que hospeda o dashboard completo de benchmark de nível de produção.
 */
class BenchmarkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val autoStart = intent.getBooleanExtra("AUTO_START_FULL", false)
        val autoStartMicro = intent.getBooleanExtra("AUTO_START_MICRO", false)
        setContent {
            BenchmarkDashboardScreen(
                autoStart = autoStart,
                autoStartMicro = autoStartMicro
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// ROOT SCREEN
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * EN Root composable for the benchmark dashboard.
 *    Wires together the BenchmarkController, state observation, the results panel,
 *    and the 1000-item stress list.
 * PT Composable raiz do dashboard de benchmark.
 *    Conecta o BenchmarkController, observação de estado, o painel de resultados
 *    e a lista de estresse de 1000 itens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BenchmarkDashboardScreen(autoStart: Boolean = false, autoStartMicro: Boolean = false) {
    val context     = LocalContext.current
    val listState   = rememberLazyListState()
    val scope       = rememberCoroutineScope()
    val activity    = context as? BenchmarkActivity

    // EN Instantiate controller — stable across recompositions
    // PT Instanciar controlador — estável entre recomposições
    val controller = remember { BenchmarkController(scope, context, listState) }

    val phase  by controller.phase.collectAsState()
    val result by controller.result.collectAsState()
    val isRunning = phase != BenchmarkPhase.IDLE && phase != BenchmarkPhase.DONE

    // EN Automated start trigger for headless/automation runs
    // PT Gatilho de início automatizado para execuções headless/automação
    LaunchedEffect(Unit) {
        if (autoStartMicro) {
            android.util.Log.i("APPDIMENS_AUTO", "Auto-start MICRO triggered via Intent extra.")
            controller.runMicroOnly()
        } else if (autoStart) {
            android.util.Log.i("APPDIMENS_AUTO", "Auto-start triggered via Intent extra.")
            controller.runFull()
        }
    }

    MaterialTheme(
        colorScheme = darkColorScheme().copy(
            background = DarkBg,
            surface    = SurfaceCard,
            primary    = AccentCyan
        )
    ) {
        Scaffold(
            containerColor = DarkBg,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "AppDimens SSPS Benchmark",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { activity?.finish() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceCard)
                )
            }
        ) { innerPadding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // ── Control Panel ──────────────────────────────────────────
                item {
                    ControlPanel(
                        phase     = phase,
                        isRunning = isRunning,
                        onFull    = { controller.runFull() },
                        onCalc    = { controller.runCalcOnly() },
                        onMicro   = { controller.runMicroOnly() },
                        onMacro   = { controller.runMacroOnly() }
                    )
                }

                // ── Status Section ─────────────────────────────────────────
                item { StatusSection(phase = phase, isRunning = isRunning) }

                // ── Calculation Results Section ─────────────────────────────
                item { CalculationResultSection(result = result.calculation) }

                // ── Single 1sp Results Section (highlight) ─────────────────
                item { SingleSspResultSection(result = result.micro?.singleSsp) }

                // ── Micro Results Section ──────────────────────────────────
                item { MicroResultSection(result = result.micro) }

                // ── Macro Results Section ──────────────────────────────────
                item { MacroResultSection(result = result.macro) }

                // ── Divider before stress list ─────────────────────────────
                item {
                    DashboardSectionHeader(
                        icon  = "🗂️",
                        label = "UI Stress List — $MACRO_ITEM_COUNT Items (scaled ssp)",
                        color = TextSecondary
                    )
                }

                // ── 1000-item stress LazyColumn ───────────────────────────
                items((1..MACRO_ITEM_COUNT).toList(), key = { it }) { id ->
                    BenchmarkItem(id)
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONTROL PANEL
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun ControlPanel(
    phase: BenchmarkPhase,
    isRunning: Boolean,
    onFull: () -> Unit,
    onCalc: () -> Unit,
    onMicro: () -> Unit,
    onMacro: () -> Unit
) {
    DashboardCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            "🚀 Benchmark Controls",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BenchmarkButton(
                label     = "Full Run",
                subLabel  = "All tests",
                color     = AccentCyan,
                enabled   = !isRunning,
                modifier  = Modifier.weight(1f),
                onClick   = onFull
            )
            BenchmarkButton(
                label     = "Calc",
                subLabel  = "Mixed",
                color     = AccentGreen,
                enabled   = !isRunning,
                modifier  = Modifier.weight(1f),
                onClick   = onCalc
            )
            BenchmarkButton(
                label     = "Micro",
                subLabel  = "CPU ops",
                color     = AccentPurple,
                enabled   = !isRunning,
                modifier  = Modifier.weight(1f),
                onClick   = onMicro
            )
            BenchmarkButton(
                label     = "Macro",
                subLabel  = "UI scroll",
                color     = AccentAmber,
                enabled   = !isRunning,
                modifier  = Modifier.weight(1f),
                onClick   = onMacro
            )
        }
    }
}

@Composable
private fun BenchmarkButton(
    label: String,
    subLabel: String,
    color: Color,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick   = onClick,
        enabled   = enabled,
        modifier  = modifier.height(56.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = ButtonDefaults.buttonColors(
            containerColor         = color.copy(alpha = 0.18f),
            disabledContainerColor = color.copy(alpha = 0.06f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (enabled) color.copy(alpha = 0.6f) else color.copy(alpha = 0.2f)
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label,    color = if (enabled) color else color.copy(alpha = 0.4f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(subLabel, color = if (enabled) TextSecondary else TextSecondary.copy(alpha = 0.3f), fontSize = 10.sp)
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STATUS SECTION
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun StatusSection(phase: BenchmarkPhase, isRunning: Boolean) {
    val progress by animateFloatAsState(
        targetValue   = phase.progressFraction,
        animationSpec = tween(durationMillis = 600),
        label         = "progress"
    )

    DashboardSectionHeader(icon = "📊", label = "Status", color = AccentCyan)

    DashboardCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Phase indicator dot
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        when {
                            phase == BenchmarkPhase.DONE  -> AccentGreen
                            isRunning                     -> AccentCyan
                            else                          -> TextSecondary
                        }
                    )
            )
            Spacer(Modifier.width(10.dp))
            AnimatedContent(
                targetState    = phase.displayLabel,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
                label          = "phaseLabel"
            ) { label ->
                Text(label, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(Modifier.height(10.dp))

        LinearProgressIndicator(
            progress        = { progress },
            modifier        = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color           = when {
                phase == BenchmarkPhase.DONE   -> AccentGreen
                phase.name.startsWith("CALC")  -> AccentGreen
                phase.name.startsWith("MICRO") -> AccentPurple
                else                           -> AccentCyan
            },
            trackColor      = SurfaceBorder
        )

        if (phase == BenchmarkPhase.DONE) {
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, null, tint = AccentGreen, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text("Benchmark complete — results ready", color = AccentGreen, fontSize = 12.sp)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CALCULATION RESULTS SECTION
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun CalculationResultSection(result: CalculationBenchmarkResult?) {
    DashboardSectionHeader(
        icon  = "🧪",
        label = "Calculation Test — Mixed ssp/hsp/wsp (+AR)",
        color = AccentGreen
    )

    DashboardCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        AnimatedVisibility(visible = result == null) {
            Text(
                "Run Calculation or Full benchmark to see results.",
                color    = TextSecondary,
                fontSize = 12.sp
            )
        }
        AnimatedVisibility(visible = result != null) {
            result?.let { r ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    MetricRow("Avg resolution", r.avgNsPerRes.formatNs(), AccentGreen, isHighlight = true)
                    MetricRow("Total calls",    "${"%,d".format(r.totalOps)}",   AccentGreen)

                    Spacer(Modifier.height(4.dp))
                    HorizontalDivider(color = SurfaceBorder)
                    Text(
                        r.throughput,
                        color      = TextSecondary,
                        fontSize   = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier   = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SINGLE 1sp RESULTS SECTION (highlight — user-facing metric)
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * EN Dedicated section showing the speed of a SINGLE 1sp call through every path,
 *    plus the resolved pixel value and an estimated "calls per 60fps frame" figure.
 * PT Seção dedicada mostrando a velocidade de UMA única chamada de 1sp em cada
 *    caminho, além do valor resolvido em pixel e a estimativa de "chamadas por frame 60fps".
 */
@Composable
private fun SingleSspResultSection(result: SingleSspBenchmarkResult?) {
    DashboardSectionHeader(
        icon  = "⚡",
        label = "Single 1sp — Velocidade de uma única chamada",
        color = AccentAmber
    )

    DashboardCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        AnimatedVisibility(visible = result == null) {
            Text(
                "Run Micro or Full benchmark to measure the cost of a single 1sp call.",
                color    = TextSecondary,
                fontSize = 12.sp
            )
        }
        AnimatedVisibility(visible = result != null) {
            result?.let { r ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    MetricRow("1.ssp  (smallest width)", r.sspAvgNs.formatNs(),  AccentAmber, isHighlight = true)
                    MetricRow("1.hsp  (height)",         r.hspAvgNs.formatNs(),  AccentAmber)
                    MetricRow("1.wsp  (width)",          r.wspAvgNs.formatNs(),  AccentAmber)
                    MetricRow("1.sspa (+aspect ratio)",  r.sspaAvgNs.formatNs(), AccentAmber)
                    MetricRow("1.sem  (no font scale)",  r.semAvgNs.formatNs(),  AccentAmber)

                    Spacer(Modifier.height(4.dp))
                    HorizontalDivider(color = SurfaceBorder)

                    // EN Resolved value + derived per-frame budget
                    // PT Valor resolvido + orçamento derivado por frame
                    val callsPerFrame = if (r.sspAvgNs > 0) 16_670_000L / r.sspAvgNs else 0L
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(AccentAmber.copy(alpha = 0.08f))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "1.ssp = ${"%.2f".format(r.valuePx)}px @ ${"%.2f".format(r.density)} density",
                            color      = TextSecondary,
                            fontSize   = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            "≈ $callsPerFrame calls/frame @60fps",
                            color      = AccentAmber,
                            fontSize   = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// MICRO RESULTS SECTION
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun MicroResultSection(result: MicroBenchmarkResult?) {
    DashboardSectionHeader(
        icon  = "⚙️",
        label = "Microbenchmark — CPU ops",
        color = AccentPurple
    )

    DashboardCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        AnimatedVisibility(visible = result == null) {
            Text(
                "Run Micro or Full benchmark to see results.",
                color    = TextSecondary,
                fontSize = 12.sp
            )
        }
        AnimatedVisibility(visible = result != null) {
            result?.let { r ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    MetricRow("Combined avg",  r.avgNsPerOp.formatNs(), AccentPurple, isHighlight = true)
                    MetricRow("Total ops",     "${"%,d".format(r.totalOps)} ops", AccentPurple)
                    MetricRow("Wall time",     "${r.totalTimeMs} ms", AccentPurple)

                    Spacer(Modifier.height(4.dp))
                    Text("Call-type breakdown", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    HorizontalDivider(color = SurfaceBorder)

                    PathRow(label = "ssp   (XML)", avgNs = r.sspBypassAvgNs, color = AccentGreen)
                    PathRow(label = "hsp   (XML)", avgNs = r.hspBypassAvgNs, color = AccentGreen)
                    PathRow(label = "wsp   (XML)", avgNs = r.wspBypassAvgNs, color = AccentGreen)
                    PathRow(label = "sspa  (+AR)", avgNs = r.sspaCacheAvgNs, color = AccentAmber)

                    Spacer(Modifier.height(4.dp))
                    HorizontalDivider(color = SurfaceBorder)
                    Text(
                        "Single value ${r.singleValue.toInt()} — with vs without AR",
                        color      = TextSecondary,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier   = Modifier.padding(top = 4.dp)
                    )
                    PathRow(label = "no AR", avgNs = r.singleNoArAvgNs, color = AccentGreen)
                    PathRow(label = "with AR", avgNs = r.singleWithArAvgNs, color = AccentAmber)

                    Spacer(Modifier.height(4.dp))
                    HorizontalDivider(color = SurfaceBorder)
                    Text(
                        "Direct-call probes — wrapper overhead",
                        color      = TextSecondary,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier   = Modifier.padding(top = 4.dp)
                    )
                    PathRow(label = "ext 100.ssp(ctx)", avgNs = r.extSspAvgNs, color = AccentCyan)
                    PathRow(label = "api DimenSsp.ssp",  avgNs = r.apiSspAvgNs, color = AccentCyan)

                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Checksum: ${"%.0f".format(r.accumulatorChecksum)} (anti dead-code proof)",
                        color      = TextSecondary,
                        fontSize   = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// MACRO RESULTS SECTION
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun MacroResultSection(result: MacroBenchmarkResult?) {
    DashboardSectionHeader(icon = "🎮", label = "Macrobenchmark — UI Scroll Performance", color = AccentCyan)

    DashboardCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        AnimatedVisibility(visible = result == null) {
            Text(
                "Run Macro or Full benchmark to see results.",
                color    = TextSecondary,
                fontSize = 12.sp
            )
        }
        AnimatedVisibility(visible = result != null) {
            result?.let { r ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    MetricRow("Scroll duration",  "${r.scrollDurationMs} ms",         AccentCyan, isHighlight = true)
                    MetricRow("Items rendered",   "${"%,d".format(r.itemsRendered)}",  AccentCyan)
                    MetricRow("Cost per item",    r.estimatedCostPerItemUs.formatUs(), AccentAmber)
                    MetricRow("Est. frames @60fps","~${r.estimatedFrames} frames",     AccentGreen)

                    Spacer(Modifier.height(4.dp))
                    HorizontalDivider(color = SurfaceBorder)
                    Text(
                        r.notes,
                        color      = TextSecondary,
                        fontSize   = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier   = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SHARED DASHBOARD COMPONENTS
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun DashboardSectionHeader(icon: String, label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(icon, fontSize = 16.sp)
        Spacer(Modifier.width(8.dp))
        Text(
            label,
            color      = color,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DashboardCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content  = content
        )
    }
}

@Composable
private fun MetricRow(label: String, value: String, color: Color, isHighlight: Boolean = false) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            label,
            color      = if (isHighlight) TextPrimary else TextSecondary,
            fontSize   = if (isHighlight) 13.sp else 12.sp,
            fontWeight = if (isHighlight) FontWeight.SemiBold else FontWeight.Normal
        )
        Text(
            value,
            color      = if (isHighlight) color else color.copy(alpha = 0.85f),
            fontSize   = if (isHighlight) 14.sp else 12.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun PathRow(label: String, avgNs: Long, color: Color) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.07f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(label, color = TextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        Text(avgNs.formatNs(), color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STRESS LIST ITEM
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * EN A single stress-test list item resolving multiple AppDimens SSPS dimension types.
 *    Dimensions are resolved ONCE and cached to avoid redundant Composable calls.
 * PT Um único item da lista de teste de estresse resolvendo múltiplos tipos de dimensão AppDimens SSPS.
 *    As dimensões são resolvidas UMA VEZ e armazenadas para evitar chamadas Composable redundantes.
 *
 * @param id EN Item identifier. PT Identificador do item.
 */
@Composable
fun BenchmarkItem(id: Int) {
    val density = LocalDensity.current

    val pad4  = with(density) { 4.ssp.toDp() }
    val pad8  = with(density) { 8.ssp.toDp() }
    val box40 = with(density) { 40.ssp.toDp() }
    val sp12  = with(density) { 12.wsp.toDp() }
    val fs14  = 14.ssp
    val h80   = with(density) { 80.hsp.toDp() }

    val even = id % 2 == 0
    val cardColor = if (even) SurfaceCard else SurfaceCard.copy(alpha = 0.7f)
    val shape = remember { RoundedCornerShape(10.dp) }
    val boxShape = remember { RoundedCornerShape(8.dp) }
    val boxBrush = remember(even) {
        Brush.linearGradient(
            colors = if (even)
                listOf(AccentCyan.copy(alpha = 0.7f), AccentPurple.copy(alpha = 0.5f))
            else
                listOf(AccentAmber.copy(alpha = 0.7f), AccentRed.copy(alpha = 0.5f))
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = pad4)
            .clip(shape)
            .background(cardColor)
            .padding(pad8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(box40)
                .clip(boxShape)
                .background(boxBrush),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "${id % 100}",
                color      = Color.White,
                fontSize   = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(sp12))

        Column {
            Text(
                "Item #$id",
                color      = TextPrimary,
                fontSize   = fs14,
                fontWeight = FontWeight.Medium
            )
            Text(
                "h:${h80.value.toInt()}sp · w:${sp12.value.toInt()}sp · s:${box40.value.toInt()}sp",
                color      = TextSecondary,
                fontSize   = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}