/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.entities.Bolts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.entities.WereWolf.EntityWereWolf;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class EntitySilverBolt extends EntityBolt
{
	private final EntityLivingBase myDamageSource;

	public EntitySilverBolt(final World world)
	{
		super(world);
		this.setProperties();
		this.myDamageSource = null;
	}

	public EntitySilverBolt(final World world, final EntityLivingBase entityLivingBase)
	{
		super(world, entityLivingBase);
		this.setProperties();
		this.myDamageSource = entityLivingBase;
	}

	public EntitySilverBolt(final World world, final double x, final double y, final double z)
	{
		super(world, x, y, z);
		this.setProperties();
		this.myDamageSource = null;
	}

	// Set the properties of the bolt
	public void setProperties()
	{
		this.setDamage(10);
		this.setDamageType(Refrence.silverWeapon);
		this.setMyType(ModItems.silverBolt);
		this.setChanceToDropHitEntity(.4);
		this.setChanceToDropHitGround(.8);
	}

	// Silver bolts have special on hit properties
	@Override
	protected void onImpact(final MovingObjectPosition movingObjectPosition)
	{
		final Entity entityHit = movingObjectPosition.entityHit;

		if (!this.worldObj.isRemote)
		{
			if (entityHit != null)
			{
				if (!(entityHit instanceof EntityWereWolf))
				{
					entityHit.attackEntityFrom(this.getDamageType(), this.getDamage());
				}
				else
				{
					if ((this.myDamageSource != null) && (this.myDamageSource instanceof EntityPlayer))
					{
						if (HasStartedAOTD.get((EntityPlayer) this.myDamageSource))
						{
							entityHit.attackEntityFrom(this.getDamageType(), this.getDamage());
						}
					}
				}

				if (Math.random() < this.getMyChanceToDropHitEntity())
				{
					entityHit.dropItem(this.getMyType(), 1);
				}
			}
			else
			{
				if (Math.random() < this.getMyChanceToDropHitEntity())
				{
					this.dropItem(this.getMyType(), 1);
				}
			}
		}

		this.setDead();
	}
}
