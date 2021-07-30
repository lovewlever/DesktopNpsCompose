package com.nps.common

import java.io.IOException

object NPCCommon {

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
                            println(str)
                        }
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }
}