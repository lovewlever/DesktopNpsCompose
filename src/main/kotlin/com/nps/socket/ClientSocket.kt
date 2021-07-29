package com.nps.socket

import java.net.Socket

/**
 * Socket客户端
 */
class ClientSocket {

    private lateinit var socket: Socket

    fun connect() {
        Thread {
            socket = Socket("127.0.0.1", 8025)
            val br = this.socket.getInputStream().bufferedReader()
            val readLine = br.readLine()
            println("客户端收到消息：$readLine")
        }.start()
    }

    fun sentMsg(msg: String) {
        if (this::socket.isInitialized) {
            val bw = this.socket.getOutputStream().bufferedWriter()
            println("客户端发消息给服务端：$msg")
            bw.write(msg)
        }
    }
}