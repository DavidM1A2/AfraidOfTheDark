/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.entities.Bolts;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;

public class EntityIronBolt extends EntityBolt
{
	public EntityIronBolt(World world)
	{
		super(world);
		setProperties();
	}

	public EntityIronBolt(World world, EntityLivingBase entityLivingBase)
	{
		super(world, entityLivingBase);
		setProperties();
	}

	public EntityIronBolt(World world, double x, double y, double z)
	{
		super(world, x, y, z);
		setProperties();
	}

	// Set the properties of the bolt
	public void setProperties()
	{
		this.setDamage(8);
		this.setDamageType(DamageSource.generic.setProjectile());
		this.setMyType(ModItems.ironBolt);
		this.setChanceToDropHitEntity(.4);
		this.setChanceToDropHitGround(.8);
	}

}
