package com.DavidM1A2.afraidofthedark.common.capabilities.player.research;

import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Class responsible for providing a capability to a player
 */
public class AOTDPlayerResearchProvider implements ICapabilitySerializable<NBTBase>
{
	// The instance of the player research capability
	private IAOTDPlayerResearch instance = ModCapabilities.PLAYER_RESEARCH.getDefaultInstance();

	/**
	 * Tests if the given capability is the player research capability
	 *
	 * @param capability The capability to test
	 * @param facing ignored
	 * @return True if the capability is a player research capability, false otherwise
	 */
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == ModCapabilities.PLAYER_RESEARCH;
	}

	/**
	 * Getter for a capability
	 *
	 * @param capability The capability to get
	 * @param facing ignored
	 * @param <T> The type of capability
	 * @return The capability or null if it was the wrong type
	 */
	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == ModCapabilities.PLAYER_RESEARCH ? ModCapabilities.PLAYER_RESEARCH.cast(this.instance) : null;
	}

	/**
	 * Serializes this capability to NBT using the storage's write to NBT method
	 *
	 * @return The NBTTagCompound representing this capability
	 */
	@Override
	public NBTBase serializeNBT()
	{
		return ModCapabilities.PLAYER_RESEARCH.getStorage().writeNBT(ModCapabilities.PLAYER_RESEARCH, this.instance, null);
	}

	/**
	 * Deserializes this capability into the default storage using the read from NBT method
	 *
	 * @param nbt The NBT tag compound to read from
	 */
	@Override
	@SuppressWarnings("NullPointerException")
	public void deserializeNBT(NBTBase nbt)
	{
		ModCapabilities.PLAYER_RESEARCH.getStorage().readNBT(ModCapabilities.PLAYER_RESEARCH, this.instance, null, nbt);
	}
}
