/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Burn extends Effect
{
	private int burnTimeSeconds = 14;

	@Override
	public int getCost()
	{
		return 5;
	}

	@Override
	public void performEffect(BlockPos location, World world, double radius)
	{
		int blockRadius = (int) Math.floor(radius);
		for (int x = -blockRadius; x < blockRadius + 1; x++)
			for (int y = -1; y < 2; y++)
				for (int z = -blockRadius; z < blockRadius + 1; z++)
					if (world.getBlockState(location.add(x, y, z)).getBlock() instanceof BlockAir)
					{
						world.setBlockState(location.add(x, y, z), Blocks.fire.getDefaultState());
						VitaeUtils.vitaeReleasedFX(world, location.add(x, y, z), 1, 1);
					}
	}

	@Override
	public void performEffect(Entity entity)
	{
		entity.setFire(burnTimeSeconds);
		VitaeUtils.vitaeReleasedFX(entity.worldObj, entity.getPosition(), 1, 5);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		this.setBurnTimeSeconds(compound.getInteger("burnTime"));
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("burnTime", this.getBurnTimeSeconds());
	}

	public void setBurnTimeSeconds(int burnTimeSeconds)
	{
		this.burnTimeSeconds = burnTimeSeconds;
	}

	public int getBurnTimeSeconds()
	{
		return this.burnTimeSeconds;
	}

	@Override
	public Effects getType()
	{
		return Effects.Burn;
	}
}
