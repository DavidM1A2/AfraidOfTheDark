package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockBush
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.IItemProvider
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReaderBase
import net.minecraft.world.World
import net.minecraftforge.common.EnumPlantType
import net.minecraftforge.common.IShearable
import java.util.*

class BlockImbuedCactusBlossom : AOTDBlockBush(
    "imbued_cactus_blossom",
    Properties.create(Material.VINE, MaterialColor.WOOD)
        .doesNotBlockMovement()
        .hardnessAndResistance(0.0f)
        .sound(SoundType.PLANT)
), IShearable {
    override fun isValidPosition(state: IBlockState, world: IWorldReaderBase, blockPos: BlockPos): Boolean {
        val blockOn = world.getBlockState(blockPos.down())
        return super.isValidPosition(state, world, blockPos) && blockOn.block == ModBlocks.IMBUED_CACTUS
    }

    override fun isValidGround(state: IBlockState, world: IBlockReader, blockPos: BlockPos): Boolean {
        return state.block == ModBlocks.IMBUED_CACTUS
    }

    override fun getShape(state: IBlockState, world: IBlockReader, blockPos: BlockPos): VoxelShape {
        return IMBUED_CACTUS_BLOSSOM_SHAPE
    }

    override fun quantityDropped(state: IBlockState, random: Random): Int {
        return random.nextInt(2) + 1
    }

    override fun getItemDropped(state: IBlockState, world: World, blockPos: BlockPos, fortune: Int): IItemProvider {
        return ModItems.DESERT_FRUIT
    }

    override fun getPlantType(world: IBlockReader, pos: BlockPos): EnumPlantType {
        return EnumPlantType.Desert
    }

    override fun onSheared(item: ItemStack, world: IWorld, pos: BlockPos, fortune: Int): MutableList<ItemStack> {
        world.setBlockState(pos, Blocks.AIR.defaultState, 11)
        return mutableListOf(ItemStack(this))
    }

    companion object {
        private val IMBUED_CACTUS_BLOSSOM_SHAPE = makeCuboidShape(3.0, 0.0, 3.0, 12.0, 16.0, 12.0)
    }
}