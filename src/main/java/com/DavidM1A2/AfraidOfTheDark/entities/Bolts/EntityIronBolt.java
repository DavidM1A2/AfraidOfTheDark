/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.entities.Bolts;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;

public class EntityIronBolt extends EntityBolt
{
	public EntityIronBolt(final World world)
	{
		super(world);
		this.setProperties();
	}

	public EntityIronBolt(final World world, final EntityLivingBase entityLivingBase)
	{
		super(world, entityLivingBase);
		this.setProperties();
	}

	public EntityIronBolt(final World world, final double x, final double y, final double z)
	{
		super(world, x, y, z);
		this.setProperties();
	}

	// Set the properties of the bolt
	public void setProperties()
	{
		this.setDamage(8);
		this.setMyType(ModItems.ironBolt);
		this.setChanceToDropHitEntity(.4);
		this.setChanceToDropHitGround(.8);
	}

}
