/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

public class EntityWoodenBolt extends EntityBolt
{
	public EntityWoodenBolt(final World world)
	{
		super(world);
	}

	public EntityWoodenBolt(final World world, final EntityLivingBase entityLivingBase)
	{
		super(world, entityLivingBase);
	}

	public EntityWoodenBolt(final World world, final double x, final double y, final double z)
	{
		super(world, x, y, z);
	}

	// Set the properties of the bolt
	public void setProperties()
	{
		this.setDamage(2);
		this.setMyType(ModItems.woodenBolt);
		this.setChanceToDropHitEntity(.4);
		this.setChanceToDropHitGround(.8);
	}
}
