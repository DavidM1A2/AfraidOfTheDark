package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItem;
import com.DavidM1A2.afraidofthedark.common.utility.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Class representing the blood stained journal item
 */
public class ItemJournal extends AOTDItem
{
	// Two constant tag names, one that tells us who the journal owner is, and one that tells us if the journal is a cheatsheet
	private static final String OWNER_TAG = "owner";
	private static final String CHEAT_SHEET_TAG = "cheatsheet";

	/**
	 * Constructor sets up item properties
	 */
	public ItemJournal()
	{
		super("journal");
		this.setMaxStackSize(1);
	}

	/**
	 * Called when the user right clicks with the journal. We show the research UI if they have started the mod
	 *
	 * @param worldIn The world that the item was right clicked in
	 * @param playerIn The player that right clicked the item
	 * @param handIn The hand that the item is in
	 * @return An action result that determines if the right click was.
	 *         Success = The call has succeeded in doing what was needed and should stop here.
	 *         Pass    = The call succeeded, but more calls can be made farther down the call stack.
	 *         Fail    = The call has failed to do what was intended and should stop here.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack heldItemStack = playerIn.getHeldItem(handIn);
		// If the journal does not have an owner yet...
		if (!NBTHelper.hasTag(heldItemStack, OWNER_TAG))
		{

		}
		// If the journal does have an owner, check if that owner is us
		else if (playerIn.getDisplayName().getUnformattedText().equals(NBTHelper.getString(heldItemStack, OWNER_TAG)))
		{

		}
		// If the owner is someone else, just tell that player that they cannot understand the journal
		else
		{
			// Send chat messages on server side only
			if (!worldIn.isRemote)
				playerIn.sendMessage(new TextComponentString("I cannot comprehend this journal..."));
		}

		// Return success because the journal processed the right click successfully
		return ActionResult.newResult(EnumActionResult.SUCCESS, heldItemStack);
	}

	/**
	 * Returns a list of sub-items that this item has. In our case there's 2 journal types, one is a cheat sheet and one is not
	 *
	 * @param tab The creative tab that we can add items to if we want, we don't use this
	 * @param items A list of items (one cheatsheet, and one regular journal)
	 */
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		// Two item stacks one standard and one cheatsheet journal
		ItemStack standardJournal = new ItemStack(this);
		ItemStack cheatsheetJournal = new ItemStack(this);
		// The cheat sheet journal will have the cheat sheet tag set to true
		NBTHelper.setBoolean(cheatsheetJournal, CHEAT_SHEET_TAG, true);
		// Add the two journals to the item list
		items.add(standardJournal);
		items.add(cheatsheetJournal);
	}

	/**
	 * Called to add a tooltip to the journal. If the journal has an owner, that owner is shown. If the journal does not have
	 * an owner, that is also shown. If the journal is a cheat sheet, show that.
	 *
	 * @param stack The itemstack to add information about
	 * @param worldIn The world that the item was hovered over in
	 * @param tooltip The tooltip that we need to fill out
	 * @param flagIn The flag telling us if we should show advanced or normal tooltips
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		// If the stack has an owner tag, show who owns the stack, otherwise show that the journal is not bound
		if (NBTHelper.hasTag(stack, OWNER_TAG))
			tooltip.add("Item soulbound to " + NBTHelper.getString(stack, OWNER_TAG));
		else
			tooltip.add("Item not bound");

		// If the journal is a cheat sheet, show that
		if (NBTHelper.hasTag(stack, CHEAT_SHEET_TAG))
			tooltip.add("Cheatsheet");
	}
}
