package com.nps.socket

import com.nps.common.ThreadPoolCommon
import java.io.*
import java.net.ServerSocket
import java.net.Socket

/**
 * Socket服务端
 */
class ServiceSocket {

    fun connect() {
        ThreadPoolCommon.scheduledThreadPoolExecutor.execute {
            val ss = ServerSocket(8025)
            while (true) {
                try {
                    val accept = ss.accept()
                    val br = BufferedInputStream(accept.getInputStream())
                    val pw = BufferedOutputStream(accept.getOutputStream())
                    var len = 0
                    while(br.read().apply { len = this }!=-1){
                        println(len.toString())
                    }

                    pw.write(1)
                    pw.flush()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }
}

internal class ServerThread(private val socket: Socket) : Runnable {
    override fun run() {
        val br = socket.getInputStream().bufferedReader()
        val pw = PrintWriter(socket.getOutputStream(), true)

        while (true) {
            val readLine = br.readLine()
            println("服务端接收到消息：$readLine")
            pw.write("服务端发出消息：${readLine}")
        }
        br.close()
        pw.close()
    }
}