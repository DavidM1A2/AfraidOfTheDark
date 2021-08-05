package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.client.dimension.NightmareRenderInfo
import com.davidm1a2.afraidofthedark.client.dimension.VoidChestRenderInfo
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.dimension.NightmareChunkGenerator
import com.davidm1a2.afraidofthedark.common.dimension.VoidChestChunkGenerator
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import net.minecraft.client.world.DimensionRenderInfo
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.common.ObfuscationReflectionHelper

/**
 * Class that registers all AOTD dimensions into the game
 */
object DimensionRegister {
    @OnlyIn(Dist.CLIENT)
    fun registerRenderInfos() {
        val effects = ObfuscationReflectionHelper.getPrivateValue<Object2ObjectMap<ResourceLocation, DimensionRenderInfo>, DimensionRenderInfo>(
            DimensionRenderInfo::class.java,
            null,
            "field_239208_a_"
        ) ?: throw IllegalStateException("Field 'EFFECTS' could not be reflected out of DimensionRenderInfo")
        effects[ResourceLocation(Constants.MOD_ID, "nightmare_effects")] = NightmareRenderInfo()
        effects[ResourceLocation(Constants.MOD_ID, "void_chest_effects")] = VoidChestRenderInfo()
    }

    fun registerChunkGenerators() {
        Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(Constants.MOD_ID, "nightmare_generator"), NightmareChunkGenerator.CODEC)
        Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(Constants.MOD_ID, "void_chest_generator"), VoidChestChunkGenerator.CODEC)
    }
}