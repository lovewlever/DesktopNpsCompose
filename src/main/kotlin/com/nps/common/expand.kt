package com.nps.common

import com.google.gson.reflect.TypeToken
import com.nps.model.SocketInteractiveData

inline fun String.toInteractiveData(): SocketInteractiveData<*>? {
    return if (GsonCommon.isJsonObj(this)) {
        GsonCommon.gson.fromJson(this, object :TypeToken<SocketInteractiveData<*>>(){}.type)
    } else {
        null
    }
}

/**
 * 获取字符串后缀
 */
fun String.suffixText(delimiter: String): String {
    return this.substring(this.lastIndexOf(delimiter), this.length)
}