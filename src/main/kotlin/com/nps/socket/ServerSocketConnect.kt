package com.nps.socket

import com.nps.common.*
import com.nps.model.SocketFileData
import com.nps.model.SocketInteractiveData
import java.io.*
import java.net.ServerSocket
import java.net.Socket

class ServerSocketConnect {
    protected var serverSocket: ServerSocket? = null

    fun startServer(port: Int) {
        if (serverSocket != null && serverSocket?.isClosed == false) return
        ThreadPoolCommon.scheduled.execute {
            try {
                serverSocket = ServerSocket(port)
                CallbackCommon.logCallback(AppLogType.LogInfo, "启动成功，等待连接")
                while (true) {
                    val accept = serverSocket?.accept()
                    CallbackCommon.logCallback(AppLogType.LogInfo, "客户端连接：${accept?.localAddress?.address}")
                    accept?.let { DataProgressServerStream(accept).start() }
                }
            } catch (e: Exception) {
                CallbackCommon.logCallback(AppLogType.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun closeServer() {
        serverSocket?.close()
        System.gc()
    }

}

internal class DataProgressServerStream(
    val socket: Socket
): ServerStream(socket) {

    override fun interactiveProgress(interactiveData: SocketInteractiveData<*>, bw: OutputStream) {
        when(interactiveData.key) {
            SocketInteractiveKey.GetDirectory ->
                sentLocalDirectoryList(interactiveData.value, bw.buffered())
            SocketInteractiveKey.DownloadFile ->
                sentFileStream(localPath = interactiveData.value, BufferedOutputStream(bw))
        }
    }

    /**
     * 发送文件给客户端
     */
    private fun sentFileStream(localPath: String, bos: BufferedOutputStream) {
        FileInputStream(localPath).use { fis ->
            var len : Int
            bos.write(SocketStreamType.ByteStream.toByteArray())
            val array = ByteArray(8192)
            while (fis.read(array).also { len = it } != -1) {
                bos.write(array, 0, len)
            }
            bos.write(SocketStreamType.StreamDone.toByteArray())
            bos.flush()
            //socket.shutdownOutput()
        }
    }

    /**
     * 发送本地目录到客户端
     */
    private fun sentLocalDirectoryList(localPath: String, bw: BufferedOutputStream) {
        val file = File(localPath)
        if (file.isFile) {
            bw.write(SocketStreamType.CharacterStream.toByteArray())
            bw.write(
                GsonCommon.gson.toJson(
                    SocketInteractiveData(
                        key = SocketInteractiveKey.GetDirectory,
                        data = mutableListOf(SocketFileData(
                            fileName = file.name,
                            filePath = file.absolutePath,
                            fileSize = if (file.isFile) file.length() else 0L,
                            isDirectory = false
                        ))
                    )

                ).encodeToByteArray()
            )
            bw.write(SocketStreamType.StreamDone.toByteArray())
            bw.flush()
        } else if (file.isDirectory) {
            file.listFiles()?.map { f ->
                SocketFileData(
                    fileName = f.name,
                    filePath = f.absolutePath,
                    fileSize = if (f.isFile) f.length() else 0L,
                    isDirectory = f.isDirectory
                )
            }?.let { list: List<SocketFileData> ->
                bw.write(SocketStreamType.CharacterStream.toByteArray())
                bw.write(
                    GsonCommon.gson.toJson(
                        SocketInteractiveData(
                        key = SocketInteractiveKey.GetDirectory,
                        data = list
                    )).encodeToByteArray()
                )
                bw.write(SocketStreamType.StreamDone.toByteArray())
                bw.flush()
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
                CallbackCommon.logCallback(AppLogType.LogInfo, "收到消息：${str}")
                val tId = str.toInteractiveData() ?: break
                if (tId.key == SocketInteractiveKey.CloseSocket) {
                    break
                } else {
                    interactiveProgress(tId, bw)
                }
            }
        } catch (e: IOException) {
            CallbackCommon.logCallback(AppLogType.LogError, "${e.message}")
            e.printStackTrace()
        } finally {
            try {
                br.close()
            } catch (e: IOException) {
                CallbackCommon.logCallback(AppLogType.LogError, "${e.message}")
                e.printStackTrace()
            }
            try {
                bw.close()
            } catch (e: IOException) {
                CallbackCommon.logCallback(AppLogType.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }


    abstract fun interactiveProgress(interactiveData: SocketInteractiveData<*>, bw: OutputStream)
}