package com.nps.socket

import com.google.gson.reflect.TypeToken
import com.nps.common.*
import com.nps.model.InteractiveData
import java.io.*
import java.net.Socket
import kotlin.jvm.Throws

/**
 * Socket客户端
 */
object ClientSocket {

    var directoryListCallback: (MutableList<InteractiveData>) -> Unit = {}
    private var socket: Socket? = null
    private var printWriter: PrintWriter? = null

    fun connect(initPath: String) {
        if (socket != null && socket?.isClosed == false) return
        ThreadPoolCommon.scheduled.execute {
            try {
                socket = Socket("127.0.0.1", 8025)
                printWriter = PrintWriter(socket?.getOutputStream()!!, true)
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "连接成功")
                sentMsg(SocketInteractiveKey.GetDirectory, initPath, "")
            } catch (e: Exception) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
            }
        }
    }

    fun sentMsg(key: Int, filePath: String, savePath: String) {
        printWriter?.println(GsonCommon.gson.toJson(InteractiveData(key = key, value = filePath)))
        inputStreamProgress(savePath)
    }

    private fun inputStreamProgress(savePath: String) {
        println(ThreadPoolCommon.scheduled.activeCount)
        ThreadPoolCommon.scheduled.execute {
            try {
                socket?.getInputStream()?.buffered()?.let { bis: BufferedInputStream ->
                    progressStream(bis, savePath)
                }
            } catch (e: Exception) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun progressStream(bis: BufferedInputStream, savePath: String) {
        val byteArrayOutputStream by lazy { ByteArrayOutputStream() }
        val bos: BufferedOutputStream? by lazy {
            if (savePath != "")
                FileOutputStream(savePath).buffered()
            else
                null
        }
        try {
            var startFlag = ""
            var len: Int
            var byteArray = ByteArray(8192)
            while (bis.read(byteArray).also { len = it } != -1) {
                val size = byteArray.size
                val sFlag = byteArray.filterIndexed { index, byte -> index < 4 }.toByteArray().decodeToString()
                val eFlag =
                    byteArray.copyOfRange(0, len).filterIndexed { index, byte -> index > len - 5 }.toByteArray()
                        .decodeToString()
                // 开始标识符
                when (sFlag) {
                    SocketStreamType.CharacterStream, SocketStreamType.ByteStream -> {
                        AppLogCallbackCommon.logCallback(ServiceInfoLog.LogInfo, "标识符：${startFlag}")
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
                    AppLogCallbackCommon.logCallback(ServiceInfoLog.LogInfo, "标识符：${eFlag}")
                    break
                }
            }

            when (startFlag) {
                SocketStreamType.CharacterStream -> {
                    progressJson(byteArrayOutputStream)
                }
            }

        } catch (e: Exception) {
            AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
            e.printStackTrace()
        } finally {
            try {
                byteArrayOutputStream.close()
            } catch (e: Exception) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
            try {
                bos?.close()
            } catch (e: Exception) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    @Throws
    private fun progressJson(baos: ByteArrayOutputStream) {
        val json = baos.toByteArray().decodeToString()
        if (GsonCommon.isJsonArr(json)) {
            GsonCommon.gson.fromJson<MutableList<InteractiveData>>(
                json, object : TypeToken<MutableList<InteractiveData>>() {}.type
            )?.let { itds: MutableList<InteractiveData> ->
                directoryListCallback(itds)
            }
        }
    }
}