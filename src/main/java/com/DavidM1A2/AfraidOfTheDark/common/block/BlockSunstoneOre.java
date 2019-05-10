package com.DavidM1A2.afraidofthedark.common.block;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDBlock;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Class representing sunstone ore found in meteors
 */
public class BlockSunstoneOre extends AOTDBlock
{
    /**
     * Constructor sets the block's properties like name
     */
    public BlockSunstoneOre()
    {
        super("sunstone_ore", Material.ROCK);
        this.setLightLevel(1.0f);
        this.setHardness(10.0F);
        this.setResistance(50.0F);
        this.setHarvestLevel("pickaxe", 2);
    }

    /**
     * Gets the item that should be dropped when the block is harvested
     *
     * @param state   The block that was broken
     * @param rand    The random to use if drops should be random
     * @param fortune The fortune level of the tool used to break the block
     * @return The item that should be dropped upon breaking the block
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ModItems.SUNSTONE_FRAGMENT;
    }

    /**
     * Called when the block is broken, here we check if the player can unlock the igneous research, if so unlock it
     *
     * @param worldIn The world that the block was broken in
     * @param player The player that broke the block
     * @param pos The position that the block was broken at
     * @param state The state of the block before being broken
     * @param te The tile entity inside the broken block
     * @param stack The item that was created as a result of breaking the block
     */
    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        IAOTDPlayerResearch playerResearch = player.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
        // If the player can research igneous let them
        if (playerResearch.canResearch(ModResearches.IGNEOUS))
        {
            playerResearch.setResearch(ModResearches.IGNEOUS, true);
            playerResearch.sync(player, true);
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
}
