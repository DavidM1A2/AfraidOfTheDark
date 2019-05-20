package com.DavidM1A2.afraidofthedark.common.block;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDBlockTileEntity;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import com.DavidM1A2.afraidofthedark.common.tileEntity.TileEntityEnariaSpawner;
import com.DavidM1A2.afraidofthedark.common.tileEntity.TileEntityGhastlyEnariaSpawner;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Class representing the block that spawns enaria in the nightmare realm
 */
public class BlockEnariaSpawner extends AOTDBlockTileEntity
{
    /**
     * Constructor makes the block hard to break and sets the block's name
     */
    public BlockEnariaSpawner()
    {
        super("enaria_spawner", Material.ROCK);
        this.setHardness(10.0F);
        this.setResistance(50.0F);
        this.setHarvestLevel("pickaxe", 3);
    }

    /**
     * Creates a tile entity for the block. If this block is in the overworld it will spawn a standard enaria, otherwise
     * it will spawn a ghastly enaria for the nightmare realm
     *
     * @param worldIn The world the block was created in
     * @param meta    The block's metadata
     * @return A tile entity representing this block
     */
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        int dimensionId = worldIn.provider.getDimension();
        // In the overworld we spawn a regular enaria, in the nightmare we spawn a ghastly enaria
        if (dimensionId == 0)
        {
            return new TileEntityEnariaSpawner();
        }
        else if (dimensionId == ModDimensions.NIGHTMARE.getId())
        {
            return new TileEntityGhastlyEnariaSpawner();
        }
        else
        {
            return null;
        }
    }

    /**
     * This block renders as a standard block, not a 3d tile entity model
     *
     * @param state The block state to render
     * @return MODEL meaning it's not a tile entity
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * @return False, we don't want this to show up in creative
     */
    @Override
    protected boolean displayInCreative()
    {
        return false;
    }

    /**
     * This block doesn't drop anything
     *
     * @param random ignored
     * @return 0
     */
    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
}
