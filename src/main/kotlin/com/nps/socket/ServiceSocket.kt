package com.nps.socket

/**
 * Socket服务端
 */
object ServiceSocket {

    private val serverSocketConnect by lazy {
        ServerSocketConnect()
    }

    fun startServer() {
        serverSocketConnect.startServer()
    }

    fun closeServer() {
        serverSocketConnect.closeServer()
    }
}