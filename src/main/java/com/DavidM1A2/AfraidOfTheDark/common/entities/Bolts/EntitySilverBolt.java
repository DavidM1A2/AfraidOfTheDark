/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWereWolf;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

public class EntitySilverBolt extends EntityBolt
{
	public EntitySilverBolt(final World world)
	{
		super(world);
		this.setProperties();
	}

	public EntitySilverBolt(final World world, final EntityLivingBase entityLivingBase)
	{
		super(world, entityLivingBase);
		this.setProperties();
	}

	public EntitySilverBolt(final World world, final double x, final double y, final double z)
	{
		super(world, x, y, z);
		this.setProperties();
	}

	// Set the properties of the bolt
	public void setProperties()
	{
		this.setDamage(10);
		this.setMyType(ModItems.silverBolt);
		this.setChanceToDropHitEntity(.4);
		this.setChanceToDropHitGround(.8);
	}

	// Silver bolts have special on hit properties
	@Override
	protected void onImpact(final MovingObjectPosition movingObjectPosition)
	{
		final Entity entityHit = movingObjectPosition.entityHit;

		if (!(entityHit instanceof EntityWereWolf))
		{
			super.onImpact(movingObjectPosition);
		}
		else
		{
			if ((this.myDamageSource != null) && (this.myDamageSource instanceof EntityPlayer))
			{
				if (HasStartedAOTD.get((EntityPlayer) this.myDamageSource))
				{
					entityHit.attackEntityFrom(Constants.AOTDDamageSources.silverWeapon, this.getDamage());
				}
			}
		}

		this.setDead();
	}
}
