package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.client.tileEntity.FeyLightTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.VoidChestTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.droppedJournal.DroppedJournalTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar.EnariasAltarTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.magicCrystal.MagicCrystalTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.spellCraftingTable.SpellCraftingTableTileEntityRenderer
import com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor.VitaeExtractorTileEntityRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class BlockEntityRendererRegister {
    @SubscribeEvent
    fun registerRenderersEvent(event: EntityRenderersEvent.RegisterRenderers) {
        // Tell MC to render our special tile entities with the special renderer
        event.registerBlockEntityRenderer(ModTileEntities.VOID_CHEST, ::VoidChestTileEntityRenderer)
        event.registerBlockEntityRenderer(ModTileEntities.ENARIAS_ALTAR, ::EnariasAltarTileEntityRenderer)
        event.registerBlockEntityRenderer(ModTileEntities.DROPPED_JOURNAL, ::DroppedJournalTileEntityRenderer)
        event.registerBlockEntityRenderer(ModTileEntities.VITAE_EXTRACTOR, ::VitaeExtractorTileEntityRenderer)
        event.registerBlockEntityRenderer(ModTileEntities.MAGIC_CRYSTAL, ::MagicCrystalTileEntityRenderer)
        event.registerBlockEntityRenderer(ModTileEntities.SPELL_CRAFTING_TABLE, ::SpellCraftingTableTileEntityRenderer)
        event.registerBlockEntityRenderer(ModTileEntities.FEY_LIGHT, ::FeyLightTileEntityRenderer)
    }
}