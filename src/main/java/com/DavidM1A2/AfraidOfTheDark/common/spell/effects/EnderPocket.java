package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EnderPocket extends Effect
{
	@Override
	public int getCost()
	{
		return 6;
	}

	@Override
	public void performEffect(BlockPos location, World world, double radius)
	{
		if (world.getTileEntity(location) == null)
		{
			world.setBlockState(location.up(), Blocks.ender_chest.getDefaultState());
			VitaeUtils.vitaeReleasedFX(world, location.up(), 1, 10);
		}
	}

	@Override
	public void performEffect(Entity entity)
	{
		if (entity instanceof EntityPlayer)
		{
			InventoryEnderChest enderChest = ((EntityPlayer) entity).getInventoryEnderChest();
			if (enderChest != null)
				((EntityPlayer) entity).displayGUIChest(enderChest);
			VitaeUtils.vitaeReleasedFX(entity.worldObj, entity.getPosition(), 1, 10);
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
