package com.DavidM1A2.AfraidOfTheDark.item.researchScrolls;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.item.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;

public abstract class ItemResearchScroll extends AOTDItem
{
	protected ResearchTypes myType;

	public ItemResearchScroll()
	{
		super();
		this.setMyType();
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (!world.isRemote)
		{
			if (LoadResearchData.canResearch(entityPlayer, this.myType))
			{
				itemStack.stackSize = itemStack.stackSize - 1;
				LoadResearchData.unlockResearchSynced(entityPlayer, this.myType, Side.SERVER, true);
			}
		}

		return super.onItemRightClick(itemStack, world, entityPlayer);
	}

	public abstract void setMyType();
}
