/**
 * @author Bodenberg
 * GIT: https://github.com/bodenberg/appdimens.git
 */
package com.example.app.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appdimens.ssps.common.DpQualifier
import com.appdimens.ssps.common.UiModeType
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
        // [EN] SECTION 4 — Facilitators (Rotate, Mode, Qualifier)
        // [PT] SEÇÃO 4 — Facilitadores (Rotação, Modo, Qualificador)
        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        item {
            DemoCard(
                title = "Facilitators",
                icon = Icons.Default.DisplaySettings,
                description = "Easy to use functions for common conditional scaling scenarios."
            ) {
                Text("Rotate: 16.sspRotate(24) (24 in Landscape)", fontSize = 16.sspRotate(24))
                Text("Mode: 16.sspMode(30, UiModeType.TELEVISION)", fontSize = 16.sspMode(30, UiModeType.TELEVISION))
                Text("Qualifier: 16.sspQualifier(20, DpQualifier.SMALL_WIDTH, 600)", fontSize = 16.sspQualifier(20, DpQualifier.SMALL_WIDTH, 600))
            }
        }

        // [EN] ---------------------------------------------------------------
        // [PT] ---------------------------------------------------------------
        // [EN] SECTION 5 — Conditional Scaling (ScaledSp)
        // [PT] SEÇÃO 5 — Escala Condicional (ScaledSp)
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
        // [EN] SECTION 3 — Visual Comparison
        // [PT] SEÇÃO 3 — Comparação Visual
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
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

            content()
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
