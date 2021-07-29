package com.nps.common

import java.util.concurrent.ScheduledThreadPoolExecutor

object ThreadPoolCommon {

    val scheduledThreadPoolExecutor by lazy {
        ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors())
    }
}