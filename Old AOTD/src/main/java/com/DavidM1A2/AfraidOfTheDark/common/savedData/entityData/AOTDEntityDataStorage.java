/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.savedData.entityData;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class AOTDEntityDataStorage implements IStorage<IAOTDEntityData>
{
	private final static String VITAE_LEVEL = "vitaeLevel";

	@Override
	public NBTBase writeNBT(Capability<IAOTDEntityData> capability, IAOTDEntityData instance, EnumFacing side)
	{
		NBTTagCompound toReturn = new NBTTagCompound();

		toReturn.setInteger(VITAE_LEVEL, instance.getVitaeLevel());

		return toReturn;
	}

	@Override
	public void readNBT(Capability<IAOTDEntityData> capability, IAOTDEntityData instance, EnumFacing side, NBTBase nbt)
	{
		NBTTagCompound compound = (NBTTagCompound) nbt;

		instance.setVitaeLevel(compound.getInteger(VITAE_LEVEL));
	}
}
