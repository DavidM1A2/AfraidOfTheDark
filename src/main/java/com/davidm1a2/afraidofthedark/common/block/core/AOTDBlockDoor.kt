package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockDoor
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

/**
 * Base class for any door blocks used by Afraid of the dark
 *
 * @constructor requires a material parameter that defines some block properties
 * @param baseName The name of the block to be used by the game registry
 * @param material The material of this block
 * @param displayInCreative True if the block should show up in creative, false otherwise
 */
abstract class AOTDBlockDoor(baseName: String, material: Material, val displayInCreative: Boolean = true) :
    BlockDoor(material) {
    init {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
        this.setHardness(3.0f)
        this.soundType = SoundType.WOOD
    }

    /**
     * The base door method returns OAK as default, so we need to override it
     *
     * @param world The world block being broken in
     * @param pos The positon of the block
     * @param state The state of the block being broken
     */
    override fun getItem(world: World, pos: BlockPos, state: IBlockState): ItemStack {
        return ItemStack(this.getItem())
    }

    /**
     * Get the Item that this Block should drop when harvested
     *
     * @param state The state of the block being broken
     * @param rand The random object to use to test if the item dropped or not
     * @param fortune The fortune level of the tool being used
     */
    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item {
        return if (state.getValue(HALF) == EnumDoorHalf.UPPER) Items.AIR else this.getItem()
    }

    /**
     * @return The item that places this door
     */
    internal abstract fun getItem(): Item
}