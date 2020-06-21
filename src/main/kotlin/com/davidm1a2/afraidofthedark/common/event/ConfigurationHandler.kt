package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.constants.ModConfigHolder
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.config.ModConfig

/**
 * Class used to maintain mod configurable options
 */
class ConfigurationHandler {
    /**
     * When we get a mod config event we update our config
     *
     * @param event The event that we will use to refresh our config
     */
    @SubscribeEvent
    fun onModConfigEvent(event: ModConfig.ModConfigEvent) {
        // Reload client or server config based on spec type
        when (event.config.spec) {
            ModConfigHolder.CLIENT_SPEC -> ModConfigHolder.reloadClientConfig()
            ModConfigHolder.SERVER_SPEC -> ModConfigHolder.reloadServerConfig()
        }
    }
}