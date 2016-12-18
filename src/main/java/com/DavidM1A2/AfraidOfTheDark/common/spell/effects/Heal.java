/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellHitInfo;
import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

public class Heal extends Effect
{
	private int healAmount = 8;

	@Override
	public int getCost()
	{
		return 8;
	}

	@Override
	public void performEffect(SpellHitInfo hitInfo)
	{
		if (hitInfo.getEntityHit() == null)
		{
			int blockRadius = (int) Math.floor(hitInfo.getRadius());
			if (blockRadius < 0)
				blockRadius = 0;
			for (EntityLivingBase entityLivingBase : hitInfo.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(hitInfo.getLocation().add(-blockRadius, -blockRadius, -blockRadius), hitInfo.getLocation().add(blockRadius, blockRadius, blockRadius))))
			{
				if (!(entityLivingBase instanceof EntityArmorStand))
				{
					entityLivingBase.heal(this.healAmount);
					VitaeUtils.vitaeReleasedFX(hitInfo.getWorld(), entityLivingBase.getPosition(), 1, 5);
				}
			}
		}
		else
		{
			if (hitInfo.getEntityHit() instanceof EntityLivingBase && !(hitInfo.getEntityHit() instanceof EntityArmorStand))
			{
				((EntityLivingBase) hitInfo.getEntityHit()).heal(this.healAmount);
				VitaeUtils.vitaeReleasedFX(hitInfo.getWorld(), hitInfo.getLocation(), 1, 5);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		this.setHealAmount(compound.getInteger("healAmount"));
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("healAmount", this.getHealAmount());
	}

	public void setHealAmount(int healAmount)
	{
		this.healAmount = healAmount;
	}

	public int getHealAmount()
	{
		return this.healAmount;
	}

	@Override
	public Effects getType()
	{
		return Effects.Heal;
	}

}
