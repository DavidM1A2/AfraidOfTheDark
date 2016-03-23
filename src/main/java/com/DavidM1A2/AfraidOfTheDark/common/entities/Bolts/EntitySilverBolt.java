/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.bolts;

import com.DavidM1A2.AfraidOfTheDark.common.entities.ICanTakeSilverDamage;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDDamageSources;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySilverBolt extends EntityBolt
{
	public EntitySilverBolt(final World world)
	{
		super(world);
	}

	public EntitySilverBolt(final World world, final EntityLivingBase entityLivingBase)
	{
		super(world, entityLivingBase);
	}

	public EntitySilverBolt(final World world, final double x, final double y, final double z)
	{
		super(world, x, y, z);
	}

	// Set the properties of the bolt
	@Override
	public void setProperties()
	{
		this.setDamage(14);
		this.setMyType(ModItems.silverBolt);
		this.setChanceToDropHitEntity(.4);
		this.setChanceToDropHitGround(.8);
	}

	// Silver bolts have special on hit properties
	@Override
	protected void onImpact(final MovingObjectPosition movingObjectPosition)
	{
		final Entity entityHit = movingObjectPosition.entityHit;

		if (!(entityHit instanceof ICanTakeSilverDamage))
		{
			super.onImpact(movingObjectPosition);
		}
		else
		{
			if ((this.myDamageSource != null) && (this.myDamageSource instanceof EntityPlayer))
			{
				if (((EntityPlayer) this.myDamageSource).getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD())
				{
					entityHit.attackEntityFrom(AOTDDamageSources.causeSilverDamage(this.myDamageSource), this.getDamage() * 2.0F);
				}
			}
		}
	}
}
