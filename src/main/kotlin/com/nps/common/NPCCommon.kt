package com.nps.common

import java.io.File
import java.io.IOException

object NPCCommon {

    private var clientProcess: Process? = null
    private var serverProcess: Process? = null

    /**
     * 启动Npc
     *   -server=8.140.170.239:8024 -vkey=nilgerw43tew0c8n -type=tcp -password=remote -target=127.0.0.1:2120 127.0.0.1:2121
     */
    fun startNpcClient(param: String) {
        clientProcess?.destroy()
        clientProcess?.destroyForcibly()
        ThreadPoolCommon.scheduled.execute {
            try {
                val property = System.getProperty("user.dir")
                clientProcess = Runtime.getRuntime().exec("${property}${File.separator}src${File.separator}main${File.separator}resources${File.separator}npc${File.separator}npc.exe $param")
                clientProcess?.inputStream?.bufferedReader().use { br ->
                        var str: String?
                        while (br?.readLine().also { str = it } != null) {
                            CallbackCommon.execProcessCallback(AppLogType.LogInfo, str ?: "")
                        }
                    }
            } catch (e: IOException) {
                CallbackCommon.execProcessCallback(AppLogType.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun startNpcServer(param: String) {
        serverProcess?.destroy()
        serverProcess?.destroyForcibly()
        ThreadPoolCommon.scheduled.execute {
            try {
                val property = System.getProperty("user.dir")
                serverProcess = Runtime.getRuntime().exec("${property}${File.separator}src${File.separator}main${File.separator}resources${File.separator}npc${File.separator}npc.exe $param")
                serverProcess?.inputStream?.bufferedReader().use { br ->
                    var str: String?
                    while (br?.readLine().also { str = it } != null) {
                        CallbackCommon.execProcessCallback(AppLogType.LogInfo, str ?: "")
                    }
                }
            } catch (e: IOException) {
                CallbackCommon.execProcessCallback(AppLogType.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun destroyExec() {
        clientProcess?.destroy()
        clientProcess?.destroyForcibly()
        serverProcess?.destroy()
        serverProcess?.destroyForcibly()
        Runtime.getRuntime().exit(9)
    }

}