package com.DavidM1A2.afraidofthedark.common.capabilities.player.research;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.research.base.Research;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

/**
 * Default storage implementation for AOTD player research
 */
public class AOTDPlayerResearchStorage implements Capability.IStorage<IAOTDPlayerResearch>
{
	private final static String RESEARCH_DATA = "unlockedResearches";

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
	public NBTBase writeNBT(Capability<IAOTDPlayerResearch> capability, IAOTDPlayerResearch instance, EnumFacing side)
	{
		// Create a compound to write
		NBTTagCompound compound = new NBTTagCompound();

		// Write each researches name as a key with true/false as the value
		for (Research research : ModRegistries.RESEARCH)
			compound.setBoolean(research.getRegistryName().toString(), instance.isResearched(research));

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
	public void readNBT(Capability<IAOTDPlayerResearch> capability, IAOTDPlayerResearch instance, EnumFacing side, NBTBase nbt)
	{
		// Test if the nbt tag base is an NBT tag compound
		if (nbt instanceof NBTTagCompound)
		{
			// The compound to read from
			NBTTagCompound compound = (NBTTagCompound) nbt;

			// For each research if we have researched it unlock that research in our instance
			for (Research research : ModRegistries.RESEARCH)
				instance.setResearch(research, compound.getBoolean(research.getRegistryName().toString()));
		}
		// There's an error, this should not be possible
		else
		{
			AfraidOfTheDark.INSTANCE.getLogger().error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!");
		}
	}
}
