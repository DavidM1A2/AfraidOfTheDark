package com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Default storage implementation for AOTD void chest data
 */
public class AOTDPlayerVoidChestDataStorage implements Capability.IStorage<IAOTDPlayerVoidChestData>
{
	// Constant IDs used in NBT
	private static final String POSITIONAL_INDEX = "positionalIndex";
	private static final String PRE_TELEPORT_POSITION = "preTeleportPosition";
	private static final String PRE_TELEPORT_DIMENSION_ID = "preTeleportDimensionID";

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
	public NBTBase writeNBT(Capability<IAOTDPlayerVoidChestData> capability, IAOTDPlayerVoidChestData instance, EnumFacing side)
	{
		// Create a compound to write
		NBTTagCompound compound = new NBTTagCompound();

		compound.setInteger(POSITIONAL_INDEX, instance.getPositionalIndex());
		compound.setTag(PRE_TELEPORT_POSITION, NBTUtil.createPosTag(instance.getPreTeleportPosition()));
		compound.setInteger(PRE_TELEPORT_DIMENSION_ID, instance.getPreTeleportDimensionID());

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
	public void readNBT(Capability<IAOTDPlayerVoidChestData> capability, IAOTDPlayerVoidChestData instance, EnumFacing side, NBTBase nbt)
	{
		// Test if the nbt tag base is an NBT tag compound
		if (nbt instanceof NBTTagCompound)
		{
			// The compound to read from
			NBTTagCompound compound = (NBTTagCompound) nbt;

			instance.setPositionalIndex(compound.getInteger(POSITIONAL_INDEX));
			instance.setPreTeleportPosition(NBTUtil.getPosFromTag((NBTTagCompound) compound.getTag(PRE_TELEPORT_POSITION)));
			instance.setPreTeleportDimensionID(compound.getInteger(PRE_TELEPORT_DIMENSION_ID));
		}
		// There's an error, this should not be possible
		else
		{
			AfraidOfTheDark.INSTANCE.getLogger().error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!");
		}
	}
}
