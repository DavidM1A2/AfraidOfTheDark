/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Heal extends Effect
{
	private int healAmount = 8;

	@Override
	public int getCost()
	{
		return 0;
	}

	@Override
	public void performEffect(BlockPos location, World world)
	{
		return;
	}

	@Override
	public void performEffect(Entity entity)
	{
		if (entity instanceof EntityLivingBase)
			((EntityLivingBase) entity).heal(this.healAmount);
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
