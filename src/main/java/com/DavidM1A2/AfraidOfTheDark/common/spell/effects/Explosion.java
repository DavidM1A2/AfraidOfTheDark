/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Explosion extends Effect
{
	@Override
	public double getCost()
	{
		return 0;
	}

	@Override
	public void performEffect(Entity entity)
	{
		entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 5, entity.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
	}

	@Override
	public void performEffect(World world, BlockPos location)
	{
		world.createExplosion(null, location.getX(), location.getY(), location.getZ(), 5, world.getGameRules().getGameRuleBooleanValue("mobGriefing"));
	}

}
