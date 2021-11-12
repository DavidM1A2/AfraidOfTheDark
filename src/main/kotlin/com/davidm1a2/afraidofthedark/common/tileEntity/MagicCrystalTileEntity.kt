package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.block.MagicCrystalBlock
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.AxisAlignedBB

class MagicCrystalTileEntity : AOTDTickingTileEntity(ModTileEntities.MAGIC_CRYSTAL) {
    override fun getRenderBoundingBox(): AxisAlignedBB {
        return super.getRenderBoundingBox().expandTowards(0.0, 16.0 * (MagicCrystalBlock.BLOCK_HEIGHT - 1), 0.0)
    }

    override fun save(compound: CompoundNBT): CompoundNBT {
        super.save(compound)
        return compound
    }

    override fun load(blockState: BlockState, compound: CompoundNBT) {
        super.load(blockState, compound)
    }
}