package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Class representing the telescope item used to track meteors
 */
public class ItemTelescope extends AOTDItem
{
    /**
     * Constructor sets up item properties
     */
    public ItemTelescope()
    {
        super("telescope");
    }

    /**
     * Called when the player right clicks with the telescope
     *
     * @param worldIn  The world that the telescope was right clicked in
     * @param playerIn The player that right clicked the telescope
     * @param handIn   The hand the telescope is in
     * @return The result of the right click
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        // Grab the itemstack the player is holding
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        // Grab the player's research
        IAOTDPlayerResearch playerResearch = playerIn.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
        // Test if the player is high enough to use the telescope
        boolean highEnough = playerIn.getPosition().getY() > 128;

        // Start with server side processing
        if (!worldIn.isRemote)
        {
            // If the player can research the research research it
            if (playerResearch.canResearch(ModResearches.ASTRONOMY_1) && highEnough)
            {
                playerResearch.setResearch(ModResearches.ASTRONOMY_1, true);
                playerResearch.sync(playerIn, true);
            }

            // If the research is researched then test if the player is high enough
            if (playerResearch.isResearched(ModResearches.ASTRONOMY_1) || playerResearch.isResearched(ModResearches.ASTRONOMY_1.getPreRequisite()))
            {
                // Tell the player that they need to be higher to see through the clouds
                if (!highEnough)
                {
                    playerIn.sendMessage(new TextComponentTranslation("aotd.telescope.not_high_enough"));
                }
            }
            else
            {
                playerIn.sendMessage(new TextComponentTranslation("aotd.dont_understand"));
            }
        }

        // If we're on client side and have the proper research and the player is above y=128 to see the stars, show the GUI
        // Don't print anything out client side since the server side takes care of that for us
        if (worldIn.isRemote && highEnough)
        {
            if (playerResearch.isResearched(ModResearches.ASTRONOMY_1) || playerResearch.canResearch(ModResearches.ASTRONOMY_1))
            {
                playerIn.openGui(AfraidOfTheDark.INSTANCE, AOTDGuiHandler.TELESCOPE_ID, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
            }
        }


        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }
}
