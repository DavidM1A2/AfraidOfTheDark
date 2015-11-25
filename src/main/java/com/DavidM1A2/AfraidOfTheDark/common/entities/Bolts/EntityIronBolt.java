/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityIronBolt extends EntityBolt
{
	public EntityIronBolt(final World world)
	{
		super(world);
	}

	public EntityIronBolt(final World world, final EntityLivingBase entityLivingBase)
	{
		super(world, entityLivingBase);
	}

	public EntityIronBolt(final World world, final double x, final double y, final double z)
	{
		super(world, x, y, z);
	}

	// Set the properties of the bolt
	@Override
	public void setProperties()
	{
		this.setDamage(14);
		this.setMyType(ModItems.ironBolt);
		this.setChanceToDropHitEntity(.4);
		this.setChanceToDropHitGround(.8);
	}

}
