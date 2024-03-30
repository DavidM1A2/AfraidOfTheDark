package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class DroppedJournalTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDTileEntity(ModTileEntities.DROPPED_JOURNAL, blockPos, blockState) {
    var journalItem: ItemStack = ItemStack(ModItems.ARCANE_JOURNAL)
        set(value) {
            setChanged()
            field = value
        }

    override fun save(nbt: CompoundTag): CompoundTag {
        super.save(nbt)
        nbt.put("arcane_journal", journalItem.serializeNBT())
        return nbt
    }

    override fun load(nbt: CompoundTag) {
        super.load(nbt)
        if (nbt.contains("arcane_journal")) {
            journalItem = ItemStack.of(nbt.getCompound("arcane_journal"))
        }
    }
}