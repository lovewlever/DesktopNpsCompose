package com.nps.socket

import java.net.ServerSocket
import java.net.Socket

/**
 * Socket服务端
 */
class ServiceSocket {

    fun connect() {
        Thread {
            val ss = ServerSocket(8025)
            ServerThread(ss).start()
        }.start()
    }
}

internal class ServerThread(private val ss: ServerSocket) : Thread() {

    override fun run() {
        super.run()
        while (true) {
            val accept = ss.accept()
            val br = accept.getInputStream().bufferedReader()
            var str = ""
            while (br.readLine() != null) {
                str += br.readLine()
            }
            println("服务端接收到消息：$str")
            val bw = accept.getOutputStream().bufferedWriter()
            bw.write("服务端接收到消息：${str}")
        }
    }
}