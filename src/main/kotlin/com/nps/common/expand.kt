package com.nps.common

import com.nps.model.InteractiveData

inline fun String.toInteractiveData(): InteractiveData? {
    return if (GsonCommon.isJsonObj(this)) {
        GsonCommon.gson.fromJson(this, InteractiveData::class.java)
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