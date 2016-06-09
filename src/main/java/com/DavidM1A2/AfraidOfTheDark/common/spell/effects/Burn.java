/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellHitInfo;
import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

public class Burn extends Effect
{
	private int burnTimeSeconds = 14;

	@Override
	public int getCost()
	{
		return 5;
	}

	@Override
	public void performEffect(SpellHitInfo hitInfo)
	{
		if (hitInfo.getEntityHit() == null)
		{
			int blockRadius = (int) Math.floor(hitInfo.getRadius());
			for (int x = -blockRadius; x < blockRadius + 1; x++)
				for (int y = -1; y < 2; y++)
					for (int z = -blockRadius; z < blockRadius + 1; z++)
						if (hitInfo.getWorld().getBlockState(hitInfo.getLocation().add(x, y, z)).getBlock() instanceof BlockAir)
						{
							hitInfo.getWorld().setBlockState(hitInfo.getLocation().add(x, y, z), Blocks.fire.getDefaultState());
							VitaeUtils.vitaeReleasedFX(hitInfo.getWorld(), hitInfo.getLocation().add(x, y, z), 1, 1);
						}
		}
		else
		{
			hitInfo.getEntityHit().setFire(burnTimeSeconds);
			VitaeUtils.vitaeReleasedFX(hitInfo.getWorld(), hitInfo.getLocation(), 1, 5);
		}
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
