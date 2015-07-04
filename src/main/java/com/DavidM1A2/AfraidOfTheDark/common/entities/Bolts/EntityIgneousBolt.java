package com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.entities.POOPER123.EntityPOOPER123;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

public class EntityIgneousBolt extends EntityBolt
{
	public EntityIgneousBolt(final World world)
	{
		super(world);
	}

	public EntityIgneousBolt(final World world, final EntityLivingBase entityLivingBase)
	{
		super(world, entityLivingBase);
	}

	public EntityIgneousBolt(final World world, final double x, final double y, final double z)
	{
		super(world, x, y, z);
	}

	// Set the properties of the bolt
	public void setProperties()
	{
		this.setDamage(11);
		this.setMyType(ModItems.igneousBolt);
		this.setChanceToDropHitEntity(.4);
		this.setChanceToDropHitGround(.8);
	}

	// Silver bolts have special on hit properties
	@Override
	protected void onImpact(final MovingObjectPosition movingObjectPosition)
	{
		final Entity entityHit = movingObjectPosition.entityHit;

		if (entityHit != null)
		{
			entityHit.setFire(10);
		}
		if (!(entityHit instanceof EntityPOOPER123))
		{
			super.onImpact(movingObjectPosition);
		}
		else
		{
			if ((this.myDamageSource != null) && (this.myDamageSource instanceof EntityPlayer))
			{
				if (HasStartedAOTD.get((EntityPlayer) this.myDamageSource))
				{
					entityHit.attackEntityFrom(Constants.AOTDDamageSources.silverDamage, this.getDamage() * 2.0F);
				}
			}
		}

		this.setDead();
	}
}
