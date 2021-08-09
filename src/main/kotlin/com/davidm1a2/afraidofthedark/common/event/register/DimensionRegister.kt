package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.dimension.NightmareChunkGenerator
import com.davidm1a2.afraidofthedark.common.dimension.VoidChestChunkGenerator
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Class that registers all AOTD dimensions into the game
 */
object DimensionRegister {
    @OnlyIn(Dist.CLIENT)
    fun registerRenderInfos() {
    }

    fun registerChunkGenerators() {
        Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(Constants.MOD_ID, "nightmare_generator"), NightmareChunkGenerator.CODEC)
        Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(Constants.MOD_ID, "void_chest_generator"), VoidChestChunkGenerator.CODEC)
    }
}