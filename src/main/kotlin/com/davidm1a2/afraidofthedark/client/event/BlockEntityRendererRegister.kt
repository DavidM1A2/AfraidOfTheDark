package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.client.tileEntity.TileEntityVoidChestRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar.TileEntityEnariasAltarRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

class BlockEntityRendererRegister {
    @SubscribeEvent
    fun fmlClientSetupEvent(event: FMLClientSetupEvent) {
        event.enqueueWork {
            // Tell MC to render our special tile entities with the special renderer
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.VOID_CHEST) { TileEntityVoidChestRenderer(it) }
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.ENARIAS_ALTAR) { TileEntityEnariasAltarRenderer(it) }
        }
    }
}
