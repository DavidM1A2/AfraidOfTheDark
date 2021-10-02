package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTileEntity
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT

class DroppedJournalTileEntity : AOTDTileEntity(ModTileEntities.DROPPED_JOURNAL) {
    var journalItem: ItemStack = ItemStack(ModItems.ARCANE_JOURNAL)
        set(value) {
            setChanged()
            field = value
        }

    override fun save(nbt: CompoundNBT): CompoundNBT {
        super.save(nbt)
        nbt.put("arcane_journal", journalItem.serializeNBT())
        return nbt
    }

    override fun load(blockState: BlockState, nbt: CompoundNBT) {
        super.load(blockState, nbt)
        if (nbt.contains("arcane_journal")) {
            journalItem = ItemStack.of(nbt.getCompound("arcane_journal"))
        }
    }
}