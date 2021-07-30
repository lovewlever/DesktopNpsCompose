package com.nps.socket

import com.nps.common.*
import com.nps.model.InteractiveData
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.*

class ServerSocketConnect {
    protected var serverSocket: ServerSocket? = null

    fun startServer() {
        if (serverSocket != null && serverSocket?.isClosed == false) return
        ThreadPoolCommon.scheduled.execute {
            try {
                serverSocket = ServerSocket(8025)
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogInfo, "启动成功，等待连接")
                while (true) {
                    val accept = serverSocket?.accept()
                    AppLogCallbackCommon.logCallback(ServiceInfoLog.LogInfo, "客户端连接：${accept?.localAddress?.address}")
                    accept?.let { DataProgressServerStream(accept).start() }
                }
            } catch (e: Exception) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun restartServer() {
        serverSocket?.close()
        serverSocket = null
        System.gc()
        startServer()
    }

}

internal class DataProgressServerStream(
    socket: Socket
): ServerStream(socket) {

    override fun interactiveProgress(interactiveData: InteractiveData, bw: OutputStream) {
        when(interactiveData.key) {
            SocketInteractiveKey.GetDirectory ->
                sentLocalDirectoryList(interactiveData.value, PrintWriter(bw, true))
        }
    }

    /**
     * 发送本地目录到客户端
     */
    private fun sentLocalDirectoryList(localPath: String, bw: PrintWriter) {
        val file = File(localPath)
        if (file.isFile) {
            bw.println(
                GsonCommon.gson.toJson(
                    InteractiveData(
                        key = SocketInteractiveKey.GetDirectory,
                        fileName = file.name,
                        filePath = file.absolutePath
                    )
                )
            )
        } else if (file.isDirectory) {
            file.listFiles()?.map { f ->
                InteractiveData(
                    key = SocketInteractiveKey.GetDirectory,
                    fileName = Base64Common.encodeToString(f.name),
                    filePath = Base64Common.encodeToString(f.absolutePath),
                    isDirectory = f.isDirectory
                )
            }?.let { list: List<InteractiveData> ->
                val toJson = GsonCommon.gson.toJson(list)
                bw.println(Base64Common.encodeToString(toJson))
            }
        }
    }
}

abstract class ServerStream(
    socket: Socket
): Thread() {
    private val br = socket.getInputStream().bufferedReader()
    private val bw = socket.getOutputStream()

    override fun run() {
        super.run()
        try {
            var str: String
            while (br.readLine().apply { str = this } != null) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogInfo, "收到消息：${str}")
                val tId = str.toInteractiveData() ?: break
                if (tId.key == SocketInteractiveKey.CloseSocket) {
                    break
                } else {
                    interactiveProgress(tId, bw)
                }
            }
        } catch (e: IOException) {
            AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
            e.printStackTrace()
        } finally {
            try {
                br.close()
            } catch (e: IOException) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
            try {
                bw.close()
            } catch (e: IOException) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }


    abstract fun interactiveProgress(interactiveData: InteractiveData, bw: OutputStream)
}