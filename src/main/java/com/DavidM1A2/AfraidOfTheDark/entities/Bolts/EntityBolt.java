/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.entities.Bolts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;

// The bolt is a throwable entity and does Generic damage.
public abstract class EntityBolt extends EntityThrowable
{
	private DamageSource myDamageType = DamageSource.generic;
	private int damageAmount = 6;
	private Item myDrop = ModItems.ironBolt;
	private double myChanceToDropHitEntity = .4;
	private double myChanceToDropHitGround = .8;

	public EntityBolt(World world)
	{
		super(world);
	}

	public EntityBolt(World world, EntityLivingBase entityLivingBase)
	{
		super(world, entityLivingBase);
	}

	public EntityBolt(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}

	// The bolt may drop depending on if the chance to drop is great enough
	@Override
	protected void onImpact(MovingObjectPosition movingObjectPosition)
	{
		Entity entityHit = movingObjectPosition.entityHit;

		if (!this.worldObj.isRemote)
		{
			if (entityHit != null)
			{
				entityHit.attackEntityFrom(myDamageType, damageAmount);

				if (Math.random() < myChanceToDropHitEntity)
				{
					entityHit.dropItem(myDrop, 1);
				}
			}
			else
			{
				if (Math.random() < myChanceToDropHitGround)
				{
					this.dropItem(myDrop, 1);
				}
			}
		}

		this.setDead();
	}

	// Set the damage of this bolt
	public void setDamage(int damage)
	{
		this.damageAmount = damage;
	}

	// Set the damage type of this bolt
	public void setDamageType(DamageSource damage)
	{
		this.myDamageType = damage;
	}

	// Set the type of bolt
	public void setMyType(Item item)
	{
		myDrop = item;
	}

	// Set the chance to drop if the bolt hit the ground
	public void setChanceToDropHitGround(double chance)
	{
		myChanceToDropHitGround = chance;
	}

	// Set the chance to drop if the bolt hit an entity
	public void setChanceToDropHitEntity(double chance)
	{
		myChanceToDropHitEntity = chance;
	}

	// Various getters
	public DamageSource getDamageType()
	{
		return this.myDamageType;
	}

	public float getDamage()
	{
		return this.damageAmount;
	}

	public double getMyChanceToDropHitEntity()
	{
		return this.myChanceToDropHitEntity;
	}

	public double getMyChanceToDropHitGround()
	{
		return this.myChanceToDropHitGround;
	}

	public Item getMyType()
	{
		return this.myDrop;
	}
}
