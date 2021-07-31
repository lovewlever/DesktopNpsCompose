package com.nps.socket

import com.google.gson.reflect.TypeToken
import com.nps.common.*
import com.nps.model.SocketFileData
import com.nps.model.SocketInteractiveData
import com.nps.model.SocketInteractiveStatus
import java.io.*
import java.net.Socket
import kotlin.jvm.Throws

/**
 * Socket客户端
 */
object ClientSocket {

    private var socket: Socket? = null
    private var printWriter: PrintWriter? = null

    fun connect(initPath: String) {
        if (socket != null && socket?.isClosed == false) return
        ThreadPoolCommon.scheduled.execute {
            try {
                socket = Socket("127.0.0.1", 8025)
                printWriter = PrintWriter(socket?.getOutputStream()!!, true)
                CallbackCommon.logCallback(ServiceInfoLog.LogError, "连接成功")
                sentMsg(SocketInteractiveKey.GetDirectory, initPath, "")
            } catch (e: Exception) {
                CallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
            }
        }
    }

    fun sentMsg(key: Int, filePath: String, savePath: String = "", fileSize: Long = 0) {
        printWriter?.println(GsonCommon.gson.toJson(SocketInteractiveData<Any>(key = key, value = filePath)))
        inputStreamProgress(savePath, fileSize)
    }

    private fun inputStreamProgress(savePath: String, fileSize: Long = 0) {
        println(ThreadPoolCommon.scheduled.activeCount)
        ThreadPoolCommon.scheduled.execute {
            try {
                socket?.getInputStream()?.buffered()?.let { bis: BufferedInputStream ->
                    progressStream(bis, savePath, fileSize)
                }
            } catch (e: Exception) {
                CallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun progressStream(bis: BufferedInputStream, savePath: String, fileSize: Long) {
        val byteArrayOutputStream by lazy { ByteArrayOutputStream() }
        val bos: BufferedOutputStream? by lazy {
            if (savePath != "")
                FileOutputStream(savePath).buffered()
            else
                null
        }
        try {
            // 当前进度
            var curSchedule = 0L
            var startFlag = ""
            var len: Int
            var byteArray = ByteArray(8192)
            while (bis.read(byteArray).also { len = it } != -1) {
                curSchedule += len
                // 下载进度
                // 文件总大小
                CallbackCommon.fileDownloadScheduleCallback(fileSize, curSchedule)
                //
                val size = byteArray.size
                val sFlag = byteArray.filterIndexed { index, byte -> index < 4 }.toByteArray().decodeToString()
                val eFlag =
                    byteArray.copyOfRange(0, len).filterIndexed { index, byte -> index > len - 5 }.toByteArray()
                        .decodeToString()
                // 开始标识符
                when (sFlag) {
                    SocketStreamType.CharacterStream, SocketStreamType.ByteStream -> {
                        CallbackCommon.logCallback(ServiceInfoLog.LogInfo, "标识符：${startFlag}")
                        byteArray = byteArray.copyOfRange(4, size)
                        len -= 4
                        startFlag = sFlag
                    }
                }
                if (eFlag == SocketStreamType.StreamDone) {
                    len -= 4
                }
                when (startFlag) {
                    SocketStreamType.CharacterStream -> {
                        byteArrayOutputStream.write(byteArray, 0, len)
                    }
                    SocketStreamType.ByteStream -> {
                        bos?.write(byteArray, 0, len)
                    }
                }
                if (eFlag == SocketStreamType.StreamDone) {
                    CallbackCommon.logCallback(ServiceInfoLog.LogInfo, "标识符：${eFlag}")
                    break
                }
            }

            when (startFlag) {
                SocketStreamType.CharacterStream -> {
                    progressJson(byteArrayOutputStream)
                }
            }

        } catch (e: Exception) {
            CallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
            e.printStackTrace()
        } finally {
            try {
                byteArrayOutputStream.close()
            } catch (e: Exception) {
                CallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
            try {
                bos?.close()
            } catch (e: Exception) {
                CallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    @Throws
    private fun progressJson(baos: ByteArrayOutputStream) {
        val json = baos.toByteArray().decodeToString()
        if (GsonCommon.isJsonObj(json)) {
            val status = GsonCommon.gson.fromJson(json, SocketInteractiveStatus::class.java)
            when(status.key) {
                SocketInteractiveKey.GetDirectory -> { // 文件目录
                    GsonCommon.gson.fromJson<SocketInteractiveData<MutableList<SocketFileData>>>(
                        json, object : TypeToken<SocketInteractiveData<MutableList<SocketFileData>>>() {}.type
                    )?.let { itds: SocketInteractiveData<MutableList<SocketFileData>> ->
                        itds.data?.let { CallbackCommon.directoryListCallback(it) }
                    }
                }
            }

        }
    }
}