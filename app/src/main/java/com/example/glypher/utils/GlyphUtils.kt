package com.example.glypher.utils

import android.os.Build
import java.util.Locale

/**
 * Nothing Phone Model Detector
 */
object GyphUtils {

    private val model = Build.MODEL.uppercase(Locale.ROOT)
    private val device = Build.DEVICE.uppercase(Locale.ROOT)
    private val product = Build.PRODUCT.uppercase(Locale.ROOT)

    private fun containsAny(vararg keys: String): Boolean {
        return keys.any { key ->
            model.contains(key, ignoreCase = true) ||
                    device.contains(key, ignoreCase = true) ||
                    product.contains(key, ignoreCase = true)
        }
    }

    /**
     * Phone (1)
     * Model: A063
     */
    fun is20111(): Boolean = containsAny("A063")

    /**
     * Phone (2)
     * Model: A065
     */
    fun is22111(): Boolean = containsAny("A065")

    /**
     * Phone (2a)
     * Model: A142
     */
    fun is23111(): Boolean = containsAny("A142")

    /**
     * Phone (2a) Plus
     * Model: A143
     */
    fun is23113(): Boolean = containsAny("A143")

    /**
     * Phone (3a) / Phone (3a) Pro
     * Models: A159 / A160 (future-safe)
     */
    fun is24111(): Boolean = containsAny("A159", "A160")
}
