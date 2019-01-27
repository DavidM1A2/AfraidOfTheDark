package com.DavidM1A2.afraidofthedark.common.capabilities.player.basics;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Default storage implementation for AOTD player basics
 */
public class AOTDPlayerBasicsStorage implements Capability.IStorage<IAOTDPlayerBasics>
{
	private static final String STARTED_AOTD = "playerStartedAOTD";
	private static final String WRIST_CROSSBOW_BOLT_INDEX = "wristCrossbowBoltIndex";

	/**
	 * Called to write a capability to an NBT compound
	 *
	 * @param capability The capability that is being written
	 * @param instance The instance to of the capability to write
	 * @param side ignored
	 * @return An NBTTagCompound that contains all info about the capability
	 */
	@Nullable
	@Override
	public NBTBase writeNBT(Capability<IAOTDPlayerBasics> capability, IAOTDPlayerBasics instance, EnumFacing side)
	{
		// Create a compound to write
		NBTTagCompound compound = new NBTTagCompound();

		compound.setBoolean(STARTED_AOTD, instance.getStartedAOTD());
		compound.setInteger(WRIST_CROSSBOW_BOLT_INDEX, instance.getSelectedWristCrossbowBoltIndex());

		return compound;
	}

	/**
	 * Called to read the NBTTagCompound into a capability
	 *
	 * @param capability The capability that is being read
	 * @param instance The instance to of the capability to read
	 * @param side ignored
	 * @param nbt An NBTTagCompound that contains all info about the capability
	 */
	@Override
	public void readNBT(Capability<IAOTDPlayerBasics> capability, IAOTDPlayerBasics instance, EnumFacing side, NBTBase nbt)
	{
		// Test if the nbt tag base is an NBT tag compound
		if (nbt instanceof NBTTagCompound)
		{
			// The compound to read from
			NBTTagCompound compound = (NBTTagCompound) nbt;

			instance.setStartedAOTD(compound.getBoolean(STARTED_AOTD));
			instance.setSelectedWristCrossbowBoltIndex(compound.getInteger(WRIST_CROSSBOW_BOLT_INDEX));
		}
		// There's an error, this should not be possible
		else
		{
			AfraidOfTheDark.INSTANCE.getLogger().error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!");
		}
	}
}
