package com.DavidM1A2.afraidofthedark.common.entity.bolt;

import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.function.Function;

/**
 * Class representing a bolt entity shot by crossbows
 */
public abstract class EntityBolt extends EntityThrowable
{
	// The damage that this bolt will do
	private int damage = 6;
	// The item that this bolt will drop
	private Item drop = null;
	// The chance that the bolt will drop its item after hitting an entity
	private double chanceToDropHitEntity = 0.4;
	// The chance that the bolt will drop its item after hitting the ground
	private double chanceToDropHitGround = 0.8;
	// The factory used to compute what type of damage to inflict
	private Function<EntityPlayer, DamageSource> damageSourceProducer;

	/**
	 * Creates the entity in the world with a shooter source
	 *
	 * @param worldIn The world to create the bolt in
	 */
	public EntityBolt(World worldIn)
	{
		super(worldIn);
		this.setupProperties();
	}

	/**
	 * Creates the entity in the world without a source at a position
	 *
	 * @param worldIn The world to create the bolt in
	 * @param x The x position of the bolt
	 * @param y The y position of the bolt
	 * @param z The z position of the bolt
	 */
	public EntityBolt(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		this.setupProperties();
	}

	/**
	 * Creates the entity in the world with a shooter source
	 *
	 * @param worldIn The world to create the bolt in
	 * @param throwerIn The shooter of the bolt
	 */
	public EntityBolt(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
		this.setupProperties();
	}

	/**
	 * Called to setup the various properties of the bolt such as its damage
	 */
	abstract void setupProperties();

	/**
	 * Called when the bolt hits something
	 *
	 * @param result The object containing hit information
	 */
	@Override
	protected void onImpact(RayTraceResult result)
	{
		// Server side processing only
		if (!this.world.isRemote)
		{
			Entity entityHit = result.entityHit;
			// Test if we hit an entity or the ground
			if (entityHit != null)
			{
				// Test if the shooter of the bolt is a player
				if (this.thrower instanceof EntityPlayer)
					entityHit.attackEntityFrom(this.getDamageSourceProducer().apply((EntityPlayer) thrower), this.getDamage());
				// If the random chance succeeds, drop the bolt item
				if (Math.random() < this.getChanceToDropHitEntity())
					entityHit.dropItem(this.getDrop(), 1);
			}
			else
			{
				// If the random chance succeeds, drop the bolt item
				if (Math.random() < this.getChanceToDropHitGround())
					this.dropItem(this.getDrop(), 1);
			}
		}
		// Kill the bolt on server and client side after impact
		this.setDead();
	}

	///
	/// Setters / Getters for all the bolt properties
	///

	void setDamage(int damage)
	{
		this.damage = damage;
	}

	int getDamage()
	{
		return damage;
	}

	void setDrop(Item drop)
	{
		this.drop = drop;
	}

	Item getDrop()
	{
		return drop;
	}

	void setChanceToDropHitEntity(double chanceToDropHitEntity)
	{
		this.chanceToDropHitEntity = chanceToDropHitEntity;
	}

	double getChanceToDropHitEntity()
	{
		return chanceToDropHitEntity;
	}

	void setChanceToDropHitGround(double chanceToDropHitGround)
	{
		this.chanceToDropHitGround = chanceToDropHitGround;
	}

	double getChanceToDropHitGround()
	{
		return chanceToDropHitGround;
	}

	public void setDamageSourceProducer(Function<EntityPlayer, DamageSource> damageSourceProducer)
	{
		this.damageSourceProducer = damageSourceProducer;
	}

	public Function<EntityPlayer, DamageSource> getDamageSourceProducer()
	{
		return damageSourceProducer;
	}
}
