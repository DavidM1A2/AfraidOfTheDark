package com.DavidM1A2.AfraidOfTheDark.common.spell;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public enum Effect 
{
	Explosion(0);
	
	private final double cost;
	
	private Effect(double cost)
	{
		this.cost = cost;
	}
	
	public double getCost()
	{
		return this.cost;
	}
	
	public static void performEffect(Effect effect, BlockPos location, World world)
	{
		switch (effect) 
		{
			case Explosion:
			{
				world.createExplosion(null, location.getX(), location.getY(), location.getZ(), 3.0f, true);
				break;
			}
		}
	}
	
	public static void performEffect(Effect effect, Entity entity)
	{
		switch (effect) 
		{
			case Explosion:
			{
				entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 3.0f, true);
				break;
			}
		}
	}
}
