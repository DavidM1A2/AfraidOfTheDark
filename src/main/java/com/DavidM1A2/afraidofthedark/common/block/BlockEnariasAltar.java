package com.DavidM1A2.afraidofthedark.common.block;

import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.DavidM1A2.afraidofthedark.common.block.core.AOTDBlock;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Enaria's altar block used in the nightmare to being crafting spells
 */
public class BlockEnariasAltar extends AOTDBlock
{
    /**
     * Constructor sets the block's properties
     */
    public BlockEnariasAltar()
    {
        super("enarias_altar", Material.PORTAL);
        this.setLightLevel(1.0f);
        this.setResistance(Float.MAX_VALUE);
        this.setBlockUnbreakable();
    }

    /**
     * Called when the block is right clicked
     *
     * @param worldIn  The world the block is in
     * @param pos      The position the block is at
     * @param state    The state of the block that is right clicked
     * @param playerIn The player that right clicked the block
     * @param hand     The hand that was used to right click
     * @param facing   The side of the block that was right clicked
     * @param hitX     The X position of the block that was right clicked
     * @param hitY     The Y position of the block that was right clicked
     * @param hitZ     The Z position of the block that was right clicked
     * @return True to cancel processing
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        // Grab the player's research
        IAOTDPlayerResearch playerResearch = playerIn.getCapability(ModCapabilities.PLAYER_RESEARCH, null);

        // Server side processing research
        if (!worldIn.isRemote)
        {
            // If the player can research enaria's secret do so
            if (playerResearch.canResearch(ModResearches.ENARIAS_SECRET))
            {
                playerResearch.setResearch(ModResearches.ENARIAS_SECRET, true);
                playerResearch.sync(playerIn, true);
            }
        }
        // Client side gui opening
        else
        {
            // If the player has the right research show the gui
            if (playerResearch.isResearched(ModResearches.ENARIAS_SECRET))
            {
                playerIn.openGui(Constants.MOD_ID, AOTDGuiHandler.SPELL_LIST_ID, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            }
            // Otherwise tell the player the block doesn't do anything yet
            else
            {
                playerIn.sendMessage(new TextComponentTranslation("aotd.enarias_altar.no_research"));
            }
        }

        return true;
    }

    /**
     * This block is not a full cube, it has a special model
     *
     * @param state The state of the block
     * @return False, this is not a full cube
     */
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * False, this block lets light through
     *
     * @param state The block state to test
     * @return False since the block lets light through
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
