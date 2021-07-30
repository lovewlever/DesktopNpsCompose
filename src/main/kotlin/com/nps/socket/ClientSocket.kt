package com.nps.socket

import com.google.gson.reflect.TypeToken
import com.nps.common.*
import com.nps.model.InteractiveData
import java.io.BufferedReader
import java.io.InputStream
import java.io.PrintWriter
import java.net.Socket
import kotlin.jvm.Throws

/**
 * Socket客户端
 */
object ClientSocket {


    var directoryListCallback: (MutableList<InteractiveData>) -> Unit = {}
    private var socket: Socket? = null
    private var printWriter: PrintWriter? = null

    fun connect() {
        if (socket != null && socket?.isClosed == false) return
        ThreadPoolCommon.scheduled.execute {
            try {
                socket = Socket("127.0.0.1", 2000)
                printWriter = PrintWriter(socket?.getOutputStream()!!, true)
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "连接成功")
                sentMsg(SocketInteractiveKey.GetDirectory, "C:\\", "")
                inputStreamProgress(SocketInteractiveKey.GetDirectory)
            } catch (e: Exception) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
            }
        }
    }

    fun sentMsg(key: String, filePath: String, savePath: String) {
        printWriter?.println(GsonCommon.gson.toJson(InteractiveData(key = key, value = filePath)))
    }

    private fun inputStreamProgress(key: String) {
        try {
            socket?.getInputStream()?.let { iis: InputStream ->
                when (key) {
                    SocketInteractiveKey.GetDirectory ->
                        getDirectoryListStream(iis.bufferedReader())
                }
            }
        } catch (e: Exception) {
            AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
            e.printStackTrace()
        }
    }

    @Throws
    private fun saveFile(path: String) {
        /*ThreadPoolCommon.scheduled.execute {
            val buf = socket?.getInputStream().buffered()
            val bos = FileOutputStream(path*//*"C:\\Users\\AOC\\Desktop\\release.apk"*//*).buffered()
            var len: Int
            while (buf.read().also { len = it } != -1) {
                bos.write(len)
            }
            bos.flush()
        }*/
    }

    @Throws
    private fun getDirectoryListStream(iis: BufferedReader) {
        var json: String?
        while (iis.readLine().also { json = it } != null) {
            if (GsonCommon.isJsonArr(json)) {
                GsonCommon.gson.fromJson<MutableList<InteractiveData>>(
                    json, object :TypeToken<MutableList<InteractiveData>>(){}.type)?.let { itds: MutableList<InteractiveData> ->
                    directoryListCallback(itds)
                }
            }
        }
    }
}