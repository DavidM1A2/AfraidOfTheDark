package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Dig extends Effect
{
	@Override
	public int getCost()
	{
		return 14;
	}

	@Override
	public void performEffect(BlockPos location, World world, double radius)
	{
		int blockRadius = (int) Math.floor(radius) / 2;
		if (blockRadius < 0)
			blockRadius = 0;
		for (int x = -blockRadius; x < blockRadius + 1; x++)
			for (int y = -blockRadius; y < blockRadius + 1; y++)
				for (int z = -blockRadius; z < blockRadius + 1; z++)
					if (world.getTileEntity(location.add(x, y, z)) == null)
					{
						if (world.destroyBlock(location.add(x, y, z), true))
							VitaeUtils.vitaeReleasedFX(world, location.add(x, y, z), .2, 1);
					}
	}

	@Override
	public void performEffect(Entity entity)
	{
		if (entity.worldObj.destroyBlock(entity.getPosition().down(), true))
			VitaeUtils.vitaeReleasedFX(entity.worldObj, entity.getPosition().down(), .2, 1);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	public Effects getType()
	{
		return Effects.Dig;
	}

}
