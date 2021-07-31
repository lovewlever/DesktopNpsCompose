package com.nps.common

import com.google.gson.Gson
import com.nps.model.AppConfigData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.StringBuilder

object AppConfigCommon {

    suspend fun getConfigData(): AppConfigData? = withContext(Dispatchers.IO) {
        val sb = StringBuilder()
        return@withContext BufferedReader(FileReader(getConfigFilePath())).use { br ->
            var str: String?
            while (br.readLine().also { str = it } != null) {
                sb.append(str)
            }
            GsonCommon.gson.fromJson(sb.toString(), AppConfigData::class.java)
        }
    }

    suspend fun putConfig(appConfigData: AppConfigData) = withContext(Dispatchers.IO) {
        PrintWriter(FileWriter(getConfigFilePath())).use { pw: PrintWriter ->
            pw.println(GsonCommon.gson.toJson(appConfigData))
        }
    }

    private fun getConfigFilePath(): String =
        "${System.getProperty("user.dir")}\\src\\main\\resources\\config.json"
}