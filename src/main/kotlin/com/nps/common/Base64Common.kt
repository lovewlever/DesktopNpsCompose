package com.nps.common

import java.util.*

object Base64Common {

    fun decodeToString(str: String): String {
        return Base64.getDecoder().decode(str).decodeToString()
    }

    fun encodeToString(str: String): String {
        return Base64.getEncoder().encodeToString(str.toByteArray())
    }
}