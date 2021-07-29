package com.nps.socket

import java.net.Socket

class ClientSocket {

    fun connect() {
        Socket("127.0.0.1", 2000).use { socket ->
            socket.getInputStream().buffered().use { bis ->

            }
        }
    }
}