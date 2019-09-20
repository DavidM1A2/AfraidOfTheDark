package com.davidm1a2.afraidofthedark.common.block;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity;
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityDarkForest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Dark forest block used to monitor the state of the dark forest dungeon
 */
public class BlockDarkForest extends AOTDBlockTileEntity
{
    /**
     * Constructor sets the block name and properties
     */
    public BlockDarkForest()
    {
        // Set the block name and material
        super("dark_forest", Material.ROCK);
        // Make this block hard to destroy with explosives and only harvestable with a diamond pick
        this.setHardness(10.0F);
        this.setResistance(50.0F);
        this.setHarvestLevel("pickaxe", 3);
    }

    /**
     * @return False, hide this block from creative
     */
    @Override
    protected boolean displayInCreative()
    {
        return false;
    }

    /**
     * This tile entity just renders as a normal block
     *
     * @param state The block state to render
     * @return MODEL, since it's not an animated tile entity
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * Creates a tile entity that monitors for nearby players
     *
     * @param worldIn The world the block is in
     * @param meta    The block's metadata value
     * @return The dark forest tile entity instance
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityDarkForest();
    }

    /**
     * The dark forest block cannot be dropped and picked up
     */
    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
}
