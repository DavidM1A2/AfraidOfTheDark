package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityEnariaSpawner
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityGhastlyEnariaSpawner
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.world.World
import java.util.*

/**
 * Class representing the block that spawns enaria in the nightmare realm
 *
 * @constructor makes the block hard to break and sets the block's name
 */
class BlockEnariaSpawner : AOTDBlockTileEntity("enaria_spawner", Material.ROCK, false)
{
    init
    {
        setHardness(10.0f)
        setResistance(50.0f)
        this.setHarvestLevel("pickaxe", 3)
    }

    /**
     * Creates a tile entity for the block. If this block is in the overworld it will spawn a standard enaria, otherwise
     * it will spawn a ghastly enaria for the nightmare realm
     *
     * @param worldIn The world the block was created in
     * @param meta    The block's metadata
     * @return A tile entity representing this block
     */
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity?
    {
        // In the overworld we spawn a regular enaria, in the nightmare we spawn a ghastly enaria
        return when (worldIn.provider.dimension)
        {
            0 ->
            {
                TileEntityEnariaSpawner()
            }
            ModDimensions.NIGHTMARE.id ->
            {
                TileEntityGhastlyEnariaSpawner()
            }
            else ->
            {
                null
            }
        }
    }

    /**
     * This block renders as a standard block, not a 3d tile entity model
     *
     * @param state The block state to render
     * @return MODEL meaning it's not a tile entity
     */
    override fun getRenderType(state: IBlockState): EnumBlockRenderType
    {
        return EnumBlockRenderType.MODEL
    }

    /**
     * This block doesn't drop anything
     *
     * @param random ignored
     * @return 0
     */
    override fun quantityDropped(random: Random): Int
    {
        return 0
    }
}