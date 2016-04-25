package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponent;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IEffect extends ISpellComponent
{
	public abstract int getCost();

	public abstract void performEffect(BlockPos location, World world, double radius);

	public abstract void performEffect(Entity entity);

	/**
	 * 
	 * @param compound
	 *            The compound to write the current power source's data to
	 */
	public abstract void writeToNBT(NBTTagCompound compound);

	/**
	 * 
	 * @param compound
	 *            The compound to read the current power source's data from
	 */
	public abstract void readFromNBT(NBTTagCompound compound);

	/**
	 * 
	 * @return The ieffect enum constant representing this effect class
	 */
	@Override
	public abstract Effects getType();
}
