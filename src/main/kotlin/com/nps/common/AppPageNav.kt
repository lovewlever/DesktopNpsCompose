package com.nps.common

sealed class AppPageNav {
    object TypeServer: AppPageNav()
    object TypeClient: AppPageNav()
    object TypeAppConfigSetting: AppPageNav()
}

sealed class AppLogType {
    object LogError: AppLogType()
    object LogInfo: AppLogType()
}
