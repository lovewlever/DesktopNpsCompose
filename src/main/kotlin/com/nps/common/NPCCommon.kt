package com.nps.common

import java.io.IOException

object NPCCommon {

    var execProcessCallback: (ServiceInfoLog,String) -> Unit = { _,_ ->}

    /**
     * 启动Npc
     *   -server=8.140.170.239:8024 -vkey=nilgerw43tew0c8n -type=tcp -password=remote -target=127.0.0.1:2120 127.0.0.1:2121
     */
    fun startNpcClient(param: String) {
        ThreadPoolCommon.scheduled.execute {
            try {
                val property = System.getProperty("user.dir")
                Runtime.getRuntime().exec("${property}\\src\\main\\resources\\npc\\npc.exe $param")
                    .inputStream.bufferedReader().use { br ->
                        var str: String
                        while (br.readLine().also { str = it } != null) {
                            execProcessCallback(ServiceInfoLog.LogInfo, str)
                        }
                    }
            } catch (e: IOException) {
                execProcessCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
        }

    }
}