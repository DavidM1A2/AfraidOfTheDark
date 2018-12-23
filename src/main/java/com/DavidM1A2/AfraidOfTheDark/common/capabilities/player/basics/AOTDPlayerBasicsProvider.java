package com.DavidM1A2.afraidofthedark.common.capabilities.player.basics;

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
public class AOTDPlayerBasicsProvider implements ICapabilitySerializable<NBTBase>
{
	// The instance of the player basics capability
	private IAOTDPlayerBasics instance = ModCapabilities.PLAYER_BASICS.getDefaultInstance();

	/**
	 * Tests if the given capability is the player basics capability
	 *
	 * @param capability The capability to test
	 * @param facing ignored
	 * @return True if the capability is a player basics capability, false otherwise
	 */
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == ModCapabilities.PLAYER_BASICS;
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
		return capability == ModCapabilities.PLAYER_BASICS ? ModCapabilities.PLAYER_BASICS.cast(this.instance) : null;
	}

	/**
	 * Serializes this capability to NBT using the storage's write to NBT method
	 *
	 * @return The NBTTagCompound representing this capability
	 */
	@Override
	public NBTBase serializeNBT()
	{
		return ModCapabilities.PLAYER_BASICS.getStorage().writeNBT(ModCapabilities.PLAYER_BASICS, this.instance, null);
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
		ModCapabilities.PLAYER_BASICS.getStorage().readNBT(ModCapabilities.PLAYER_BASICS, this.instance, null, nbt);
	}
}
