/**
 * @author Bodenberg
 * GIT: https://github.com/bodenberg/appdimens.git
 */
package com.example.app.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.Orientation
import com.appdimens.ssps.common.UiModeType
import com.appdimens.ssps.code.DimenSsp
import com.appdimens.ssps.compose.*

/**
 * [EN] Example Activity to demonstrate the full usage
 * of the AppDimensSsp library.
 *
 * Shows both direct and conditional usage of the functions:
 * - .ssp, .hsp, .wsp
 * - .scaledSp() with various rule combinations
 *
 * [PT] Atividade de exemplo para demonstrar o uso completo
 * da biblioteca AppDimensSsp.
 *
 * Mostra o uso direto e condicional das funções:
 * - .ssp, .hsp, .wsp
 * - .scaledSp() com várias combinações de regras
 */
class ExampleActivity : ComponentActivity() {
    /**
     * [EN] Called when the activity is first created.
     *
     * [PT] Chamado quando a atividade é criada pela primeira vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DimenSsp.warmupSspsFactors(applicationContext)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppDimensSspExampleScreen()
                }
            }
        }
    }
}

/**
 * [EN] A composable function that displays the AppDimens SSP example screen.
 *
 * [PT] Uma função de composição que exibe a tela de exemplo do AppDimens SSP.
 */
@Composable
fun AppDimensSspExampleScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        // [EN] HEADER
        // [PT] CABEÇALHO
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        item {
            Text(
                text = "Demo - AppDimensSsp",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Dynamic text scaling (Sp) for Compose",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        // [EN] BENCHMARK BUTTON
        // [PT] BOTÃO DE BENCHMARK
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        item {
            val context = LocalContext.current
            Button(
                onClick = {
                    context.startActivity(Intent(context, BenchmarkActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Speed, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("⚡ Benchmark — Medir velocidade", fontWeight = FontWeight.Bold)
            }
        }

        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        // [EN] SECTION 1 — Direct Scaling (ssp, hsp, wsp)
        // [PT] SEÇÃO 1 — Escala Direta (ssp, hsp, wsp)
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        item {
            DemoCard(
                title = "Direct Scaling",
                icon = Icons.Default.FormatSize,
                description = "Direct usage of .ssp, .hsp, and .wsp extensions " +
                        "to automatically adjust text size according to actual screen dimensions."
            ) {
                Text("16.ssp → based on smallest width (sw)", fontSize = 10.ssp)
                Text("18.hsp → based on height", fontSize = 10.hsp)
                Text("18.wsp → based on width", fontSize = 10.wsp)
            }
        }

        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        // [EN] SECTION 2 — Inverter Shortcuts
        // [PT] SEÇÃO 2 — Atalhos com Inversão
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        item {
            DemoCard(
                title = "Inverter Shortcuts",
                icon = Icons.Default.FormatSize,
                description = "Quick shortcuts to resolve dimensions based on orientation swaps (e.g., sspPh uses Height in Portrait)."
            ) {
                Text("16.sspPh → ${16.sspPh.value}sp", fontSize = 16.sspPh)
                Text("16.sspLw → ${16.sspLw.value}sp", fontSize = 16.sspLw)
                Text("18.hspLw → ${18.hspLw.value}sp", fontSize = 18.hspLw)
            }
        }

        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        // [EN] SECTION 3 — No Font Scale (sem, hem, wem)
        // [PT] SEÇÃO 3 — Sem Escala de Fonte (sem, hem, wem)
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        item {
            DemoCard(
                title = "No Font Scale (sem)",
                icon = Icons.Default.FormatSize,
                description = "Dimensions that ignore the system font scale setting."
            ) {
                Text("16.sem → Ignore font scale (sw)", fontSize = 16.sem)
                Text("16.hem → Ignore font scale (h)", fontSize = 16.hem)
                Text("16.wem → Ignore font scale (w)", fontSize = 16.wem)
            }
        }

        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        // [EN] SECTION 4 — Aspect ratio (*.ssp vs *a)
        // [PT] SEÇÃO 4 — Proporção (* sem *a vs *a)
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        item {
            DemoCard(
                title = "Aspect Ratio (with vs without)",
                icon = Icons.Filled.AspectRatio,
                description = "Variants ending in «a» (sspa, hspa, wspa, sema) apply an aspect-ratio " +
                    "adjustment on top of the XML-resolved size. Compared here at the same nominal size. " +
                    "On ratios near 16:9 the values can look identical."
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Without AR",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "32.ssp → ${32.ssp.value}sp",
                            fontSize = 32.ssp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "22.hsp → ${22.hsp.value}sp",
                            fontSize = 22.hsp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "22.wsp → ${22.wsp.value}sp",
                            fontSize = 22.wsp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "16.sem → ${16.sem.value}sp",
                            fontSize = 16.sem,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "With AR (*a)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "32.sspa → ${32.sspa.value}sp",
                            fontSize = 32.sspa,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "22.hspa → ${22.hspa.value}sp",
                            fontSize = 22.hspa,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "22.wspa → ${22.wspa.value}sp",
                            fontSize = 22.wspa,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "16.sema → ${16.sema.value}sp",
                            fontSize = 16.sema,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        // [EN] SECTION 5 — Facilitators (Rotate, Mode, Qualifier)
        // [PT] SEÇÃO 5 — Facilitadores (Rotação, Modo, Qualificador)
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        item {
            DemoCard(
                title = "Facilitators",
                icon = Icons.Default.DisplaySettings,
                description = "Easy to use functions for common conditional scaling scenarios."
            ) {
                Text("Rotate: 16.sspRotate(24) (24 in Landscape)", fontSize = 16.sspRotate(24))
                // [EN] Plain with two TextUnits — no extra resource resolution on this call.
                // [PT] Plain com dois TextUnits — sem nova resolução de recurso nesta chamada.
                Text(
                    "RotatePlain (2× TextUnit): 16.ssp.sspRotatePlain(22.ssp)",
                    fontSize = 16.ssp.sspRotatePlain(22.ssp)
                )
                Text(
                    "RotatePlain + Orientation: 14.ssp.sspRotatePlain(20.ssp, PORTRAIT)",
                    fontSize = 14.ssp.sspRotatePlain(20.ssp, Orientation.PORTRAIT)
                )
                // [EN] Inner sspRotatePlain runs first, then outer uses that result as rotation TextUnit.
                // [PT] O sspRotatePlain interior avalia primeiro; o exterior usa esse resultado como TextUnit de rotação.
                Text(
                    "Nested Plain: 16.ssp.sspRotatePlain(20.ssp.sspRotatePlain(14.ssp))",
                    fontSize = 16.ssp.sspRotatePlain(20.ssp.sspRotatePlain(14.ssp))
                )
                Text("Mode: 16.sspMode(30, UiModeType.TELEVISION)", fontSize = 16.sspMode(30, UiModeType.TELEVISION))
                Text("Qualifier: 16.sspQualifier(20, DpQualifier.SMALL_WIDTH, 600)", fontSize = 16.sspQualifier(20, DpQualifier.SMALL_WIDTH, 600))
            }
        }

        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        // [EN] SECTION 6 — Conditional Scaling (ScaledSp)
        // [PT] SEÇÃO 6 — Escala Condicional (ScaledSp)
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        item {
            val scaledExample = 10.scaledSp()
                .screen(UiModeType.TELEVISION, DpQualifier.SMALL_WIDTH, 720, 24)
                .screen(UiModeType.CAR, 11)
                .screen(DpQualifier.SMALL_WIDTH, 300, 15)
                .screen(DpQualifier.HEIGHT, 400, 13)
                .screen(DpQualifier.WIDTH, 400, 18)

            DemoCard(
                title = "Conditional Scaling (ScaledSp)",
                icon = Icons.Default.DisplaySettings,
                description = "Define custom rules based on UI mode (TV, Car, etc.) " +
                        "and screen qualifiers (width, height, or smallest width)."
            ) {
                Text("Dynamically scaled text:", fontSize = scaledExample.ssp)
                Text("Based on height →", fontSize = scaledExample.hsp)
                Text("Based on width →", fontSize = scaledExample.wsp)
            }
        }

        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        // [EN] SECTION 7 — Visual Comparison
        // [PT] SEÇÃO 7 — Comparação Visual
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        item {
            DemoCard(
                title = "Scale Comparison",
                icon = Icons.Default.Devices,
                description = "Shows the difference between base value and automatically " +
                        "scaled values according to rules and screen dimensions."
            ) {
                Text(
                    text = "Base 14sp → ${14.ssp.value}sp",
                    fontSize = 14.ssp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                val dynamicScaledText = 14.scaledSp()
                    .screen(UiModeType.TELEVISION, DpQualifier.SMALL_WIDTH, 720, 24)
                Text(
                    text = "TV Mode (sw ≥ 720dp) → dynamicScaledText.ssp",
                    fontSize = dynamicScaledText.ssp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Powered by AppDimens Library",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * [EN] Demo card with title, icon, description, and content.
 *
 * [PT] Cartão de demonstração com título, ícone, descrição e conteúdo.
 */
@Composable
fun DemoCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )

            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(lineHeight = 1.3.em)
            ) {
                content()
            }
        }
    }
}

/**
 * [EN] A preview for the AppDimens SSP example screen.
 *
 * [PT] Uma visualização para a tela de exemplo do AppDimens SSP.
 */
@Preview(showBackground = true, device = "id:Nexus One")
@Composable
fun PreviewAppDimensSspExample() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            AppDimensSspExampleScreen()
        }
    }
}
