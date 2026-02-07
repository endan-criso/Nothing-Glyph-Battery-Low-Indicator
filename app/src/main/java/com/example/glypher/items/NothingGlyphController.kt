package com.example.glypher.items

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nothing.ketchum.GlyphManager
import com.nothing.ketchum.Glyph
import kotlinx.coroutines.*

class NothingGlyphController(private val context: Context) {

    private val glyphManager: GlyphManager = GlyphManager.getInstance(context)
    private var isConnected = false

    fun init(onReady: (() -> Unit)? = null) {
        glyphManager.init(object : GlyphManager.Callback {
            override fun onServiceConnected(name: ComponentName) {
                try {
                    // "23111" is the ID for Phone (2a)
                    glyphManager.register(Glyph.DEVICE_23111)
                    glyphManager.openSession()
                    isConnected = true
                    onReady?.invoke()
                } catch (e: Exception) {
                    Log.e("GlyphController", "Init failed: ${e.message}")
                }
            }

            override fun onServiceDisconnected(name: ComponentName) {
                isConnected = false
            }
        })
    }

    fun animateGlyph(vararg glyphs: GlyphID, period: Int = 500, cycles: Int = 3) {
        if (!isConnected) return
        try {
            val builder = glyphManager.getGlyphFrameBuilder()
            glyphs.forEach { builder.buildChannel(it.index) }
            glyphManager.animate(builder.buildPeriod(period).buildCycles(cycles).build())
        } catch (e: Exception) {
            Log.e("GlyphController", "Animate error: ${e.message}")
        }
    }

    fun startSnakeAnimation(scope: CoroutineScope) {
        if (!isConnected) return
        scope.launch {

            // Circle segments are 0 to 23
            for (i in 0..23) {
                val builder = glyphManager.getGlyphFrameBuilder()
                builder.buildChannel(i)
                glyphManager.toggle(builder.build())
                delay(100) // 100ms is a good "snake" speed
            }

            for (i in 23 downTo  0) {
                val builder = glyphManager.getGlyphFrameBuilder()
                builder.buildChannel(i)
                glyphManager.toggle(builder.build())
                delay(100) // 100ms is a good "snake" speed
            }


            glyphManager.closeSession()
        }
    }

    fun turnOffGlyphs() {
        if (isConnected) {
            glyphManager.turnOff()
        }
    }

    fun release() {
        try {
            glyphManager.closeSession()
            glyphManager.unInit()
        } catch (e: Exception) {}
        isConnected = false
    }
}

/**
 * Battery Receiver
 */
class BatteryLowGlyphReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BATTERY_LOW) {
            val controller = NothingGlyphController(context.applicationContext)
            controller.init {
                controller.animateGlyph(GlyphID.C_1, GlyphID.C_12, period = 1000, cycles = 3)
            }
        }
    }
}