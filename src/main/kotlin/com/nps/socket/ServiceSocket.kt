package com.nps.socket

import com.nps.common.ThreadPoolCommon
import java.io.FileInputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

/**
 * Socket服务端
 */
class ServiceSocket {

    fun connect() {
        ThreadPoolCommon.scheduled.execute {
            val ss = ServerSocket(8025)
            while (true) {
                val accept = ss.accept()
                println(accept.localAddress.address.toString())
                ServerStream(accept)
            }
        }
    }
}

internal class ServerStream(socket: Socket) {
    private val br = socket.getInputStream().bufferedReader()
    private val bw = socket.getOutputStream().buffered()

    init  {
        try {
            var str: String
            while (br.readLine().apply { str = this } != null) {
                if (str == "-1") {
                    break
                } else {
                    readFileSent(str)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                br.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                bw.close()
            } catch (e: IOException) {
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