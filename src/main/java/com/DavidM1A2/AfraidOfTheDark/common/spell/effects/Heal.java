/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Heal extends Effect
{
	private int healAmount = 8;

	@Override
	public int getCost()
	{
		return 8;
	}

	@Override
	public void performEffect(BlockPos location, World world, double radius)
	{
		int blockRadius = (int) Math.floor(radius);
		if (blockRadius < 0)
			blockRadius = 0;
		for (EntityLivingBase entityLivingBase : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(location.add(-blockRadius, -blockRadius, -blockRadius), location.add(blockRadius, blockRadius, blockRadius))))
		{
			if (!(entityLivingBase instanceof EntityArmorStand))
			{
				entityLivingBase.heal(this.healAmount);
				VitaeUtils.vitaeReleasedFX(world, entityLivingBase.getPosition(), 1, 5);
			}
		}
	}

	@Override
	public void performEffect(Entity entity)
	{
		if (entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand))
		{
			((EntityLivingBase) entity).heal(this.healAmount);
			VitaeUtils.vitaeReleasedFX(entity.worldObj, entity.getPosition(), 1, 5);
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
