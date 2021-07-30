package com.nps.socket

import com.nps.common.ServiceInfoLog
import com.nps.common.ThreadPoolCommon
import java.io.FileInputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

/**
 * Socket服务端
 */
object ServiceSocket {

    fun connect(
        callback: (ServiceInfoLog, String) -> Unit = { _,_ -> }
    ) {
        ThreadPoolCommon.scheduled.execute {
            try {
                val ss = ServerSocket(8025)
                callback(ServiceInfoLog.LogInfo, "启动成功，等待连接")
                while (true) {
                    val accept = ss.accept()
                    callback(ServiceInfoLog.LogInfo, "客户端连接：${accept.localAddress.address}")
                    ServerStream(accept) { serviceInfoLog, string ->
                        callback(serviceInfoLog, string)
                    }
                }
            } catch (e: Exception) {
                callback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }
}

internal class ServerStream(socket: Socket, callback: (ServiceInfoLog, String) -> Unit = { _,_ -> }) {
    private val br = socket.getInputStream().bufferedReader()
    private val bw = socket.getOutputStream().buffered()

    init  {
        try {
            var str: String
            while (br.readLine().apply { str = this } != null) {
                callback(ServiceInfoLog.LogInfo, "收到消息：${str}")
                if (str == "-1") {
                    break
                } else {
                    readFileSent(str)
                }
            }
        } catch (e: IOException) {
            callback(ServiceInfoLog.LogError, "${e.message}")
            e.printStackTrace()
        } finally {
            try {
                br.close()
            } catch (e: IOException) {
                callback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
            try {
                bw.close()
            } catch (e: IOException) {
                callback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }


    private fun readFileSent(path: String) {
        FileInputStream(path).buffered().use { bs ->
            var len: Int
            while (bs.read().also { len = it } != -1) {
                bw.write(len)
            }
            bw.flush()
            bw.close()
        }
    }

}