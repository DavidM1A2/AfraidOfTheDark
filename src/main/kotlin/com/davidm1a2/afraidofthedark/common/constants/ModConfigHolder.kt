package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.config.ClientConfig
import com.davidm1a2.afraidofthedark.common.config.CommonConfig
import net.minecraftforge.common.ForgeConfigSpec

/**
 * Holds the ForgeConfigSpec and Config instances
 */
object ModConfigHolder {
    val CLIENT_SPEC: ForgeConfigSpec
    val COMMON_SPEC: ForgeConfigSpec

    private val clientConfig: ClientConfig
    private val commonConfig: CommonConfig

    init {
        val (clientConfig, forgeClientConfigSpec) = ForgeConfigSpec.Builder().configure { ClientConfig(it) }
        this.clientConfig = clientConfig
        this.CLIENT_SPEC = forgeClientConfigSpec

        val (commonConfig, forgeCommonConfigSpec) = ForgeConfigSpec.Builder().configure { CommonConfig(it) }
        this.commonConfig = commonConfig
        this.COMMON_SPEC = forgeCommonConfigSpec
    }

    fun reloadClientConfig() {
        clientConfig.reload()
    }

    fun reloadCommonConfig() {
        commonConfig.reload()
    }
}