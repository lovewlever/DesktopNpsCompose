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
                socket = Socket("127.0.0.1", 2000)
                printWriter = PrintWriter(socket?.getOutputStream()!!, true)
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "连接成功")
                sentMsg(SocketInteractiveKey.GetDirectory, initPath, "")
            } catch (e: Exception) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
            }
        }
    }

    fun sentMsg(key: String, filePath: String, savePath: String) {
        printWriter?.println(GsonCommon.gson.toJson(InteractiveData(key = key, value = filePath)))
        inputStreamProgress(key, savePath)
    }

    private fun inputStreamProgress(key: String, savePath: String) {
        println(ThreadPoolCommon.scheduled.activeCount)
        ThreadPoolCommon.scheduled.execute {
            try {
                socket?.getInputStream()?.let { iis: InputStream ->
                    println(iis::class.simpleName)
                    when (key) {
                        SocketInteractiveKey.GetDirectory ->
                            getDirectoryListStream(BufferedReader(iis.bufferedReader()))
                        SocketInteractiveKey.Download ->
                            saveFile(savePath, BufferedInputStream(iis))
                    }
                }
            } catch (e: Exception) {
                AppLogCallbackCommon.logCallback(ServiceInfoLog.LogError, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    @Throws
    private fun saveFile(savePath: String, buf: BufferedInputStream) {
        val bos = FileOutputStream(savePath).buffered()
        var len: Int
        while (buf.read().also { len = it } != -1) {
            bos.write(len)
        }
        bos.flush()
        bos.close()
    }

    @Throws
    private fun getDirectoryListStream(iis: BufferedReader) {
        var json: String
        while (iis.readLine().also { json = it } != null) {
            println(json)
            if (GsonCommon.isJsonArr(json)) {
                GsonCommon.gson.fromJson<MutableList<InteractiveData>>(
                    json, object : TypeToken<MutableList<InteractiveData>>() {}.type
                )?.let { itds: MutableList<InteractiveData> ->
                    directoryListCallback(itds)
                }
            }
        }
    }
}