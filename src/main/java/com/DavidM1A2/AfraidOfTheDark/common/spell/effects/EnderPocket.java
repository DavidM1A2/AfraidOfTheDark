package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellHitInfo;
import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.nbt.NBTTagCompound;

public class EnderPocket extends Effect
{
	@Override
	public int getCost()
	{
		return 6;
	}

	@Override
	public void performEffect(SpellHitInfo hitInfo)
	{
		if (hitInfo.getEntityHit() == null)
		{
			if (hitInfo.getWorld().getTileEntity(hitInfo.getLocation()) == null)
			{
				hitInfo.getWorld().setBlockState(hitInfo.getLocation().up(), Blocks.ENDER_CHEST.getDefaultState());
				VitaeUtils.vitaeReleasedFX(hitInfo.getWorld(), hitInfo.getLocation().up(), 1, 10);
			}
		}
		else if (hitInfo.getEntityHit() instanceof EntityPlayer)
		{
			InventoryEnderChest enderChest = ((EntityPlayer) hitInfo.getEntityHit()).getInventoryEnderChest();
			if (enderChest != null)
				((EntityPlayer) hitInfo.getEntityHit()).displayGUIChest(enderChest);
			VitaeUtils.vitaeReleasedFX(hitInfo.getWorld(), hitInfo.getLocation(), 1, 10);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	public Effects getType()
	{
		return Effects.EnderPocket;
	}

}
