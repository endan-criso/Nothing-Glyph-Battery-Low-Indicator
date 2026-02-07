package com.example.glypher.utils

import android.os.Build
import java.io.File

class GetBuildInfo(){
    val manufacture: String = Build.MANUFACTURER
    val model: String = Build.MODEL
    val soc_model: String = Build.SOC_MODEL
    val isroot: Boolean = isRooted1() || isRooted2() || isRooted3()
}

fun isRooted1(): Boolean {
    val buildTag = Build.TAGS
    return buildTag.contains("test-keys") && buildTag != null
}

fun isRooted2(): Boolean{
    val paths = arrayOf("/system/app/Superuser.apk",
        "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su",
        "/data/local/bin/su", "/system/sd/xbin/su",
        "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su")

    for(x in paths)
    {
        val file = File(x + "su")
        if(file.exists()) return true;
        return false;
    }
    return true;
}

fun isRooted3(): Boolean{
    return try{
        val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
        val result = process.inputStream.bufferedReader().readText()
        result.isNotEmpty()
    }
    catch (e: Exception)
    {
        return false;
    }
}