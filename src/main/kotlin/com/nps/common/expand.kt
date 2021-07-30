package com.nps.common

import com.nps.model.InteractiveData

inline fun String.toInteractiveData(): InteractiveData? {
    return if (GsonCommon.isJsonObj(this)) {
        GsonCommon.gson.fromJson(this, InteractiveData::class.java)
    } else {
        null
    }
}