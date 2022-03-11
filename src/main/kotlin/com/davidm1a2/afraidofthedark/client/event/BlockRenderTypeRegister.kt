package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

class BlockRenderTypeRegister {
    @SubscribeEvent
    fun fmlClientSetupEvent(event: FMLClientSetupEvent) {
        event.enqueueWork {
            RenderTypeLookup.setRenderLayer(ModBlocks.AMORPHOUS_ELDRITCH_METAL, RenderType.translucent())
            RenderTypeLookup.setRenderLayer(ModBlocks.VOID_CHEST_PORTAL, RenderType.translucent())
            RenderTypeLookup.setRenderLayer(ModBlocks.IMBUED_CACTUS, RenderType.cutout())
            RenderTypeLookup.setRenderLayer(ModBlocks.IMBUED_CACTUS_BLOSSOM, RenderType.cutout())
            RenderTypeLookup.setRenderLayer(ModBlocks.GRAVEWOOD_SAPLING, RenderType.cutout())
            RenderTypeLookup.setRenderLayer(ModBlocks.MANGROVE_SAPLING, RenderType.cutout())
            RenderTypeLookup.setRenderLayer(ModBlocks.SACRED_MANGROVE_SAPLING, RenderType.cutout())
            RenderTypeLookup.setRenderLayer(ModBlocks.GRAVEWOOD_DOOR, RenderType.cutout())
            RenderTypeLookup.setRenderLayer(ModBlocks.MANGROVE_DOOR, RenderType.cutout())
        }
    }
}