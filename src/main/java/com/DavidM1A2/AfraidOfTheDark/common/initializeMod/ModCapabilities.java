package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.savedData.entityData.AOTDDefaultEntityData;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.entityData.AOTDEntityDataStorage;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.entityData.IAOTDEntityData;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.playerData.AOTDDefaultPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.playerData.AOTDPlayerDataStorage;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.playerData.IAOTDPlayerData;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities
{
	@CapabilityInject(IAOTDPlayerData.class)
	public static final Capability<IAOTDPlayerData> PLAYER_DATA = null;
	@CapabilityInject(IAOTDEntityData.class)
	public static final Capability<IAOTDEntityData> ENTITY_DATA = null;

	public static void initialize()
	{
		CapabilityManager.INSTANCE.<IAOTDPlayerData> register(IAOTDPlayerData.class, new AOTDPlayerDataStorage(), AOTDDefaultPlayerData.class);
		CapabilityManager.INSTANCE.<IAOTDEntityData> register(IAOTDEntityData.class, new AOTDEntityDataStorage(), AOTDDefaultEntityData.class);
	}
}
