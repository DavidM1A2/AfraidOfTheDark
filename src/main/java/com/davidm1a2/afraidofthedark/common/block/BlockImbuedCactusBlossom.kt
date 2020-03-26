package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBush
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.EnumPlantType
import java.util.*

class BlockImbuedCactusBlossom : AOTDBush("imbued_cactus_blossom", Material.VINE)
{
    /**
     * Checks if this block can be placed exactly at the given position.
     *
     * @param worldIn The world the block is being placed at
     * @param pos The position the block is being placed
     * @return True if the block can fit here, or false otherwise
     */
    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean
    {
        val ground = worldIn.getBlockState(pos.down())
        return super.canPlaceBlockAt(worldIn, pos) && ground.block == ModBlocks.IMBUED_CACTUS
    }

    /**
     * Gets the bounding box of the cactus blossom
     *
     * @param state The block's state
     * @param source The world the block is in
     * @param pos The position of the block
     * @return The cactus's AABB
     */
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return IMBUED_CACTUS_BLOSSOM_AABB
    }

    /**
     * This block can only be placed on imbued cactus
     *
     * @param state The block's state
     * @return True if the block is a cactus, false otherwise
     */
    override fun canSustainBush(state: IBlockState): Boolean
    {
        return state.block == ModBlocks.IMBUED_CACTUS
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     *
     * @param worldIn The world block is in
     * @param pos The position the block is at
     * @return False since the block isn't replaceable
     */
    override fun isReplaceable(worldIn: IBlockAccess, pos: BlockPos): Boolean
    {
        return false
    }

    /**
     * Returns the quantity of items to drop on block destruction
     *
     * @param random The random object to use
     * @return 1-2 fruits
     */
    override fun quantityDropped(random: Random): Int
    {
        return random.nextInt(2) + 1
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param state The block's state
     * @param rand A random instance
     * @param fortune The fortune level of the tool
     * @return Desert fruit item
     */
    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item
    {
        return ModItems.DESERT_FRUIT
    }

    /**
     * Gets the plant type of this block, used to determine if it should "pop off" or not
     *
     * @param world The world the block is in
     * @param pos The position the block is in
     * @return Desert so that this block can sit on a cactus
     */
    override fun getPlantType(world: IBlockAccess, pos: BlockPos): EnumPlantType
    {
        return EnumPlantType.Desert
    }

    companion object
    {
        private val IMBUED_CACTUS_BLOSSOM_AABB = AxisAlignedBB(0.09999999403953552, 0.0, 0.09999999403953552, 0.8999999761581421, 0.800000011920929, 0.8999999761581421)
    }
}