package com.nps.socket

import com.nps.common.ServiceInfoLog
import com.nps.common.SocketInteractiveKey
import com.nps.common.ThreadPoolCommon
import java.io.FileInputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

/**
 * Socket服务端
 */
object ServiceSocket {

    var logCallback: (ServiceInfoLog, String) -> Unit = { _, _ -> }

    private val serverSocketConnect by lazy {
        ServerSocketConnect().also { ssc ->
            ssc.logCallback = logCallback
        }
    }

    fun startServer() {
        serverSocketConnect.startServer()
    }
}