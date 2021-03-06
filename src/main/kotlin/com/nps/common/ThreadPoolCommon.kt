package com.nps.common

import java.util.concurrent.ScheduledThreadPoolExecutor

object ThreadPoolCommon {
    val scheduled by lazy {
        ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors())
    }
}