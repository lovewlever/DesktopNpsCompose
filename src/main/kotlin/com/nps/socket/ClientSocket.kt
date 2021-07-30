package com.nps.socket

import com.nps.common.ThreadPoolCommon
import java.io.FileOutputStream
import java.io.PrintWriter
import java.net.Socket

/**
 * Socket客户端
 */
object ClientSocket {

    private lateinit var socket: Socket
    private lateinit var printWriter: PrintWriter

    fun connect(errorCallback: (String) -> Unit = {}) {
        ThreadPoolCommon.scheduled.execute {
            try {
                socket = Socket("127.0.0.1", 2000)
            } catch (e: Exception) {
                errorCallback("${e.message}")
            }
        }
    }

    fun sentMsg(filePath: String, savePath: String) {
        if (this::socket.isInitialized) {
            printWriter = PrintWriter(socket.getOutputStream(), true)
            printWriter.println(filePath/*"D:\\Documents\\AndroidProjects\\Navigation\\app\\release\\app-release.apk"*/)
            saveFile(savePath)
        }
    }

    private fun saveFile(path: String) {
        ThreadPoolCommon.scheduled.execute {
            val buf = socket.getInputStream().buffered()
            val bos = FileOutputStream(path/*"C:\\Users\\AOC\\Desktop\\release.apk"*/).buffered()
            var len: Int
            while (buf.read().also { len = it } != -1) {
                bos.write(len)
            }
            bos.flush()
            bos.close()
        }

    }
}