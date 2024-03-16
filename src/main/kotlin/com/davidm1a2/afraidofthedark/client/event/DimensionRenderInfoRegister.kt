package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.client.dimension.NightmareRenderInfo
import com.davidm1a2.afraidofthedark.client.dimension.VoidChestRenderInfo
import com.davidm1a2.afraidofthedark.common.constants.Constants
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import net.minecraft.client.renderer.DimensionSpecialEffects
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.util.ObfuscationReflectionHelper

class DimensionRenderInfoRegister {
    @SubscribeEvent
    fun fmlClientSetupEvent(event: FMLClientSetupEvent) {
        event.enqueueWork {
            val effects = ObfuscationReflectionHelper.getPrivateValue<Object2ObjectMap<ResourceLocation, DimensionSpecialEffects>, DimensionSpecialEffects>(
                DimensionSpecialEffects::class.java,
                null,
                "field_239208_a_"
            ) ?: throw IllegalStateException("Field 'EFFECTS' could not be reflected out of DimensionRenderInfo")
            effects[ResourceLocation(Constants.MOD_ID, "nightmare_effects")] = NightmareRenderInfo()
            effects[ResourceLocation(Constants.MOD_ID, "void_chest_effects")] = VoidChestRenderInfo()
        }
    }
}