package com.nps.common

object AppLogCallbackCommon {
    var logCallback: (ServiceInfoLog, String) -> Unit = { _, _ -> }
}