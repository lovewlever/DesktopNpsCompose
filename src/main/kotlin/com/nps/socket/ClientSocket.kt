package com.nps.socket

import com.nps.common.ThreadPoolCommon
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.PrintWriter
import java.net.Socket

/**
 * Socket客户端
 */
class ClientSocket {

    private lateinit var socket: Socket
    private lateinit var bos: BufferedOutputStream

    fun connect() {
        ThreadPoolCommon.scheduledThreadPoolExecutor.execute {
            socket = Socket("127.0.0.1", 8025)
            bos = BufferedOutputStream(this.socket.getOutputStream())
            println("客户端发消息给服务端：$2")
            bos.write(1)
            bos.flush()
            ClientThread(socket)
        }
    }

    fun sentMsg(msg: String) {
        if (this::socket.isInitialized) {
            println("客户端发消息给服务端：$msg")
            bos.write(1)
            bos.flush()
        }
    }
}

internal class ClientThread(private val socket: Socket): Runnable {
    override fun run() {
        val br = socket.getInputStream().buffered()
        var len = 0
        while(br.read().apply { len = this }!=-1){
            println("111111" + len.toString())
        }
    }
}