package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.dimension.NightmareChunkGenerator
import com.davidm1a2.afraidofthedark.common.dimension.VoidChestChunkGenerator
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

/**
 * Class that registers all AOTD chunk generators into the game
 */
class ChunkGeneratorRegister {
    @SubscribeEvent
    fun commonSetupEvent(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(Constants.MOD_ID, "nightmare_generator"), NightmareChunkGenerator.CODEC)
            Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(Constants.MOD_ID, "void_chest_generator"), VoidChestChunkGenerator.CODEC)
        }
    }
}