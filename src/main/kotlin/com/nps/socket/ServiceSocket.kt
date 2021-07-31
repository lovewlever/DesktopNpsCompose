package com.nps.socket

/**
 * Socket服务端
 */
object ServiceSocket {

    private val serverSocketConnect by lazy {
        ServerSocketConnect()
    }

    fun startServer(port: Int) {
        serverSocketConnect.startServer(port)
    }

    fun closeServer() {
        serverSocketConnect.closeServer()
    }
}