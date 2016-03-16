package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Explosion implements IEffect
{
	@Override
	public int getCost()
	{
		return 0;
	}

	@Override
	public void performEffect(BlockPos location, World world)
	{
		world.createExplosion(null, location.getX(), location.getY(), location.getZ(), 3.0f, true);
	}

	@Override
	public void performEffect(Entity entity)
	{
		entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 3.0f, true);
	}
}
