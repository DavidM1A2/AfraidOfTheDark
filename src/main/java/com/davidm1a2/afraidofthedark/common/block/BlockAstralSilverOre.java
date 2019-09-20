package com.davidm1a2.afraidofthedark.common.block;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock;
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModResearches;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Class that represents an astral silver ore block
 */
public class BlockAstralSilverOre extends AOTDBlock
{
    /**
     * Constructor initializes the block's properties
     */
    public BlockAstralSilverOre()
    {
        super("astral_silver_ore", Material.ROCK);
        this.setHardness(10.0f);
        this.setResistance(50.0f);
        this.setHarvestLevel("pickaxe", 2);
    }

    /**
     * Called when the block gets harvested with a pickaxe
     *
     * @param worldIn The world the block is in
     * @param player  The player that broke the block
     * @param pos     The position of the block
     * @param state   The state of the block
     * @param te      The tile entity at the block pos
     * @param stack   The item stack used to break the block
     */
    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        // If the player can unlock the astral silver research unlock it and sync
        IAOTDPlayerResearch playerResearch = player.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
        if (playerResearch.canResearch(ModResearches.ASTRAL_SILVER))
        {
            playerResearch.setResearch(ModResearches.ASTRAL_SILVER, true);
            playerResearch.sync(player, true);
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
}
