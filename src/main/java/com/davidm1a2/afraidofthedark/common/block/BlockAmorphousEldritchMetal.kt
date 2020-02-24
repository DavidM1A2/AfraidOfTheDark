package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class representing amorphous eldritch metal, this can be walked through
 *
 * @constructor initializes the block's properties
 */
class BlockAmorphousEldritchMetal : AOTDBlock("amorphous_eldritch_metal", Material.PORTAL) {
    init {
        setHardness(10.0f)
        setResistance(50.0f)
        this.setHarvestLevel("pickaxe", 2)
        setLightLevel(1.0f)
    }

    /**
     * Called to get the collision/hitbox of this block. We can walk through this block so return the null AABB
     *
     * @param blockState The block state of this block
     * @param worldIn    The world the block is in
     * @param pos        The position the block is at
     * @return The hitbox/bounding box of this block
     */
    override fun getCollisionBoundingBox(
        blockState: IBlockState,
        worldIn: IBlockAccess,
        pos: BlockPos
    ): AxisAlignedBB? {
        return NULL_AABB
    }

    /**
     * This block is not opaque since we can see through it
     *
     * @param state The block state
     * @return False since the block is not opaque
     */
    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }

    /**
     * @return The layer that this block is in, it's translucent since we can see through it
     */
    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.TRANSLUCENT
    }
}