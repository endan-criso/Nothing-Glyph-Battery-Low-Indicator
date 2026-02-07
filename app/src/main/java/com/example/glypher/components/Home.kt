package com.example.glypher.components

import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.glypher.items.BatteryLowGlyphReceiver
import com.example.glypher.items.BuildInfoSection
import com.example.glypher.items.NothingGlyphController
import com.example.glypher.items.GlyphID
import com.example.glypher.utils.GetBuildInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Home() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val info = remember { GetBuildInfo() }

    // 1. Create and persist the controller
    val glyphController = remember { NothingGlyphController(context.applicationContext) }

    // 2. State management
    var batteryLowEnabled by remember { mutableStateOf(false) }
    var isSnakeRunning by remember { mutableStateOf(false) }

    // 3. Initialize Controller once
    LaunchedEffect(Unit) {
        glyphController.init()
    }

    // 4. Handle Battery Receiver Registration
    DisposableEffect(batteryLowEnabled) {
        // FIX: Remove 'glyphController' from the parentheses
        val receiver = BatteryLowGlyphReceiver()

        if (batteryLowEnabled) {
            context.registerReceiver(
                receiver,
                IntentFilter(Intent.ACTION_BATTERY_LOW)
            )
        }
        onDispose {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: Exception) {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        BuildInfoSection(info)

        Spacer(Modifier.height(24.dp))

        // Toggle for Battery Listener
        ToggleButtName(
            label = "Battery Low Monitor",
            isOn = batteryLowEnabled,
            onToggle = { batteryLowEnabled = it }
        )

        Spacer(Modifier.height(16.dp))

        // Simple Test Button (Better than a switch for animations)
        Button(
            onClick = {
                glyphController.animateGlyph(
                    GlyphID.A, GlyphID.B, GlyphID.C_1,
                    period = 500, cycles = 2
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Test Blink (A + B + C1)")
        }

        Spacer(Modifier.height(8.dp))

        // Snake Animation Button
        Button(
            onClick = {
                if (!isSnakeRunning) {
                    isSnakeRunning = true
                    glyphController.startSnakeAnimation(scope)
                    // Reset button state after animation time (~2 seconds)
                    scope.launch {
                        delay(2500)
                        isSnakeRunning = false
                    }
                }
            },
            enabled = !isSnakeRunning,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSnakeRunning) "Snake Running..." else "Start Snake Animation")
        }

        Spacer(Modifier.height(8.dp))

        // Emergency Stop
        Button(
            onClick = { glyphController.turnOffGlyphs() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Turn Off All Lights")
        }
    }

    // 5. Cleanup when the app screen is destroyed
    DisposableEffect(Unit) {
        onDispose {
            glyphController.release()
        }
    }
}

@Composable
fun ToggleButtName(
    label: String,
    isOn: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Switch(checked = isOn, onCheckedChange = onToggle)
    }
}