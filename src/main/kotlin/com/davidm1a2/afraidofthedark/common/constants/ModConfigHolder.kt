package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.config.ClientConfig
import com.davidm1a2.afraidofthedark.common.config.ServerConfig
import net.minecraftforge.common.ForgeConfigSpec

/**
 * Holds the ForgeConfigSpec and Config instances
 */
object ModConfigHolder {
    val CLIENT_SPEC: ForgeConfigSpec
    val SERVER_SPEC: ForgeConfigSpec

    private val clientConfig: ClientConfig
    private val serverConfig: ServerConfig

    init {
        val (clientConfig, forgeClientConfigSpec) = ForgeConfigSpec.Builder().configure { ClientConfig(it) }
        this.clientConfig = clientConfig
        this.CLIENT_SPEC = forgeClientConfigSpec

        val (serverConfig, forgeServerConfigSpec) = ForgeConfigSpec.Builder().configure { ServerConfig(it) }
        this.serverConfig = serverConfig
        this.SERVER_SPEC = forgeServerConfigSpec
    }

    fun reloadClientConfig() {
        clientConfig.reload()
    }

    fun reloadServerConfig() {
        serverConfig.reload()
    }
}