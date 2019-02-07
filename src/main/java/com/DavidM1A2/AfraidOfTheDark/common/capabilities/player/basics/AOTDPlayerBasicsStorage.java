package com.DavidM1A2.afraidofthedark.common.capabilities.player.basics;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.registry.meteor.MeteorEntry;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import org.codehaus.plexus.util.StringUtils;

import javax.annotation.Nullable;

/**
 * Default storage implementation for AOTD player basics
 */
public class AOTDPlayerBasicsStorage implements Capability.IStorage<IAOTDPlayerBasics>
{
	// Constant IDs used in NBT
	private static final String STARTED_AOTD = "playerStartedAOTD";
	private static final String WRIST_CROSSBOW_BOLT_INDEX = "wristCrossbowBoltIndex";
	private static final String WATCHED_METEOR = "watchedMeteor";
	private static final String WATCHED_METEOR_DROP_ANGLE = "watchedMeteorDropAngle";
	private static final String WATCHED_METEOR_LATITUDE = "watchedMeteorLatitude";
	private static final String WATCHED_METEOR_LONGITUDE = "watchedMeteorLongitude";

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
		compound.setString(WATCHED_METEOR, instance.getWatchedMeteor() == null ? "none" : instance.getWatchedMeteor().getRegistryName().toString());
		compound.setInteger(WATCHED_METEOR_DROP_ANGLE, instance.getWatchedMeteorDropAngle());
		compound.setInteger(WATCHED_METEOR_LATITUDE, instance.getWatchedMeteorLatitude());
		compound.setInteger(WATCHED_METEOR_LONGITUDE, instance.getWatchedMeteorLongitude());

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
			String watchedMeteorName = compound.getString(WATCHED_METEOR);
			MeteorEntry watchedMeteor = StringUtils.equals(watchedMeteorName, "none") ? null : ModRegistries.METEORS.getValue(new ResourceLocation(watchedMeteorName));
			int watchedMeteorDropAngle = compound.getInteger(WATCHED_METEOR_DROP_ANGLE);
			int watchedMeteorLatitude = compound.getInteger(WATCHED_METEOR_LATITUDE);
			int watchedMeteorLongitude = compound.getInteger(WATCHED_METEOR_LONGITUDE);
			instance.setWatchedMeteor(watchedMeteor, watchedMeteorDropAngle, watchedMeteorLatitude, watchedMeteorLongitude);
		}
		// There's an error, this should not be possible
		else
		{
			AfraidOfTheDark.INSTANCE.getLogger().error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!");
		}
	}
}
