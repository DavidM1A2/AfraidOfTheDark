/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell;

import org.apache.commons.lang3.Validate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class SpellHitInfo
{
	private BlockPos hitLocation = null;
	private World hitWorld = null;
	private int hitRadius = -1;
	private Entity entityHit = null;
	private EntityPlayer spellCaster = null;

	public SpellHitInfo(EntityPlayer spellCaster, Entity entityHit)
	{
		this(spellCaster, entityHit.getPosition(), entityHit.worldObj, -1);
		Validate.notNull(entityHit);
		this.entityHit = entityHit;
	}

	public SpellHitInfo(EntityPlayer spellCaster, BlockPos location, World world, int radius)
	{
		Validate.notNull(spellCaster);
		this.spellCaster = spellCaster;
		Validate.notNull(location);
		this.hitLocation = location;
		Validate.notNull(world);
		this.hitWorld = world;
		this.hitRadius = radius;
	}

	/**
	 * 
	 * @return The location that the spell hit. Cannot be null
	 */
	public BlockPos getLocation()
	{
		return hitLocation;
	}

	/**
	 * 
	 * @return The world in which the spell hit. Cannot be null
	 */
	public World getWorld()
	{
		return hitWorld;
	}

	/**
	 * 
	 * @return The radius of the spell hit. -1 indicates no radius or an entity was hit
	 */
	public int getRadius()
	{
		return hitRadius;
	}

	/**
	 * 
	 * @return The entity that the spell hit. Can be null if no entity was hit
	 */
	public Entity getEntityHit()
	{
		return entityHit;
	}

	/**
	 * 
	 * @return The entity player that casted the spell. Cannot be null
	 */
	public EntityPlayer getSpellCaster()
	{
		return spellCaster;
	}
}
