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
import net.minecraft.util.text.TextComponentString;
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
	 * @param worldIn The world that the telescope was right clicked in
	 * @param playerIn The player that right clicked the telescope
	 * @param handIn The hand the telescope is in
	 * @return The result of the right click
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		// Grab the itemstack the player is holding
		ItemStack itemStack = playerIn.getHeldItem(handIn);
		// Grab the player's research
		IAOTDPlayerResearch playerResearch = playerIn.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
		// First process server side only
		if (!worldIn.isRemote)
			// Test if the player can research astronomy 1
			if (playerResearch.canResearch(ModResearches.ASTRONOMY_1))
			{
				// The player must be above y=128 to see the clouds
				if (playerIn.getPosition().getY() <= 128)
					playerIn.sendMessage(new TextComponentString("I can't see anything through these thick clouds. Maybe I could move to a higher elevation."));
				else
				{
					playerResearch.setResearch(ModResearches.ASTRONOMY_1, true);
					playerResearch.sync(playerIn, true);
				}
			}
			else
			{
				playerIn.sendMessage(new TextComponentString("I can't understand what this thing does."));
			}
		// If we're on client side test if we have the proper research, if so show the GUI
		else
		{
			// Catch both cases where the research is finished and when the server sent us an unlock packet but we don't have it yet so we
			// check if we 'can research' it
			if (playerResearch.isResearched(ModResearches.ASTRONOMY_1) || playerResearch.canResearch(ModResearches.ASTRONOMY_1))
				playerIn.openGui(AfraidOfTheDark.INSTANCE, AOTDGuiHandler.TELESCOPE_ID, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
	}
}
