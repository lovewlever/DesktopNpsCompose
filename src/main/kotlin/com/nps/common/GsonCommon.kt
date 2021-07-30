package com.nps.common

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser

object GsonCommon {

    val gson by lazy { Gson() }

    fun jsonParser(string: String): JsonElement =
        JsonParser.parseString(string)

    fun isJsonObj(string: String):Boolean {
        return jsonParser(string).isJsonObject
    }
}