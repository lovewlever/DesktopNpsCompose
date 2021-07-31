package com.nps.common

object SocketStreamType {
    const val StreamDone = "0xfe"
    const val CharacterStream = "0xfb"
    const val ByteStream = "0xfc"
}


object SocketInteractiveKey {
    const val CloseSocket = 3
    const val GetDirectory = 2
    const val DownloadFile = 1
    //const val GetDownloadFileSize = 4 // 获取要下载文件的大小
}