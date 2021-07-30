package com.nps.common

sealed class ServiceType {

    object TypeServer: ServiceType()
    object TypeClient: ServiceType()
    object TypeChooseSC: ServiceType()
}

sealed class ServiceInfoLog {
    object LogError: ServiceInfoLog()
    object LogInfo: ServiceInfoLog()
}