/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

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
		return 0;
	}

	@Override
	public void performEffect(BlockPos location, World world)
	{
		for (int x = -2; x < 3; x++)
			for (int y = -2; y < 1; y++)
				for (int z = -2; z < 3; z++)
					if (world.getBlockState(location.add(x, y, z)).getBlock() instanceof BlockAir)
						world.setBlockState(location.add(x, y, z), Blocks.fire.getDefaultState());
	}

	@Override
	public void performEffect(Entity entity)
	{
		entity.setFire(burnTimeSeconds);
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
