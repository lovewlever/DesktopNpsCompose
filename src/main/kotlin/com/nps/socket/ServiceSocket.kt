package com.nps.socket

import java.io.BufferedInputStream
import java.net.ServerSocket
import java.net.Socket

class ServiceSocket {

    fun connect() {
        ServerSocket(8025).use { ss: ServerSocket ->
            while (true) {
                ss.accept().getInputStream().buffered().use { bis: BufferedInputStream ->

                }
            }
        }
    }
}

internal class ServerThread(private val socket: Socket): Thread() {

    override fun run() {
        super.run()
        socket.use { s: Socket ->
            val br = s.getInputStream().bufferedReader()
        }
    }
}