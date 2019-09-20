package com.davidm1a2.afraidofthedark.common.block;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock;
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModItems;
import com.davidm1a2.afraidofthedark.common.constants.ModResearches;
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
 * Class representing star metal ore found in meteors
 */
public class BlockStarMetalOre extends AOTDBlock
{
    /**
     * Constructor initializes the block's name and properties
     */
    public BlockStarMetalOre()
    {
        super("star_metal_ore", Material.ROCK);
        this.setLightLevel(0.4f);
        this.setHardness(10.0F);
        this.setResistance(50.0F);
        this.setHarvestLevel("pickaxe", 2);
    }

    /**
     * Called to set the item that gets dropped when the ore gets dropped
     *
     * @param state The block state that was broken
     * @param rand The random to be used to determine the number of drops
     * @param fortune The fortune level that the ore was mined with
     * @return A star metal fragment to craft with
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ModItems.STAR_METAL_FRAGMENT;
    }

    /**
     * Called when the block is broken, here we check if the player can unlock the star metal research, if so unlock it
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
        // If the player can research star metal let them
        if (playerResearch.canResearch(ModResearches.STAR_METAL))
        {
            playerResearch.setResearch(ModResearches.STAR_METAL, true);
            playerResearch.sync(player, true);
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
}
