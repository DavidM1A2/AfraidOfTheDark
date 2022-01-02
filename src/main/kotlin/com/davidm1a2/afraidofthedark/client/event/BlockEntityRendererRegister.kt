package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.client.tileEntity.VoidChestTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.droppedJournal.DroppedJournalTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar.EnariasAltarTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.magicCrystal.MagicCrystalTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.spellCraftingTable.SpellCraftingTableTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor.VitaeExtractorTileEntityRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

class BlockEntityRendererRegister {
    @SubscribeEvent
    fun fmlClientSetupEvent(event: FMLClientSetupEvent) {
        event.enqueueWork {
            // Tell MC to render our special tile entities with the special renderer
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.VOID_CHEST) { VoidChestTileEntityRenderer(it) }
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.ENARIAS_ALTAR) { EnariasAltarTileEntityRenderer(it) }
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.DROPPED_JOURNAL) { DroppedJournalTileEntityRenderer(it) }
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.VITAE_EXTRACTOR) { VitaeExtractorTileEntityRenderer(it) }
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.MAGIC_CRYSTAL) { MagicCrystalTileEntityRenderer(it) }
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.SPELL_CRAFTING_TABLE) { SpellCraftingTableTileEntityRenderer(it) }
        }
    }
}