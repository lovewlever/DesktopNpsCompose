package com.nps.model

class SocketInteractiveData<T>(
    var key: Int = 0,
    var value: String = "",
    var data: T? = null
)

class SocketInteractiveStatus(
    var key: Int = 0,
    var value: String = ""
)