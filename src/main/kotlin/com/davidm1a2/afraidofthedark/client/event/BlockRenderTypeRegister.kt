package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

class BlockRenderTypeRegister {
    @SubscribeEvent
    fun fmlClientSetupEvent(event: FMLClientSetupEvent) {
        event.enqueueWork {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.AMORPHOUS_ELDRITCH_METAL, RenderType.translucent())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.VOID_CHEST_PORTAL, RenderType.translucent())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.IMBUED_CACTUS, RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.IMBUED_CACTUS_BLOSSOM, RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRAVEWOOD_SAPLING, RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MANGROVE_SAPLING, RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SACRED_MANGROVE_SAPLING, RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRAVEWOOD_DOOR, RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MANGROVE_DOOR, RenderType.cutout())
        }
    }
}