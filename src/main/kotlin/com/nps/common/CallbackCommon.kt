package com.nps.common

import com.nps.model.SocketFileData

object CallbackCommon {
    // 日志
    var logCallback: (AppLogType, String) -> Unit = { _, _ -> }
    // 目录列表
    var directoryListCallback: (MutableList<SocketFileData>) -> Unit = {}
    // 文件下载进度
    var fileDownloadScheduleCallback: (available: Long, curSchedule: Long) -> Unit = { _, _ -> }
}