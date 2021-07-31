package com.nps.common

object SocketStreamType {
    const val StreamDone = "0xfe"
    const val CharacterStream = "0xfb"
    const val ByteStream = "0xfc"
}


object SocketInteractiveKey {
    const val CloseSocket = 3
    const val GetDirectory = 2
    const val Download = 1
}