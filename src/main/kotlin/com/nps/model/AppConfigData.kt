package com.nps.model

import androidx.compose.runtime.Stable

@Stable
class AppConfigData {
    var clientConfig: ClientConfig = ClientConfig()
    var serverConfig: ServerConfig = ServerConfig()
}

@Stable
class ClientConfig {
    var ipAddress: String = ""
    var portAddress: String = ""
    var npcParam: String = ""
    var fileDownloadSavePath: String = ""
    var remoteRootPath: String = ""
}

@Stable
class ServerConfig {
    var portAddress: String = ""
}