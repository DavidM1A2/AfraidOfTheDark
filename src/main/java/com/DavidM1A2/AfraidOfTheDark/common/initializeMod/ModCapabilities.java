package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDDefaultPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerDataStorage;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.IAOTDPlayerData;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities
{
	@CapabilityInject(IAOTDPlayerData.class)
	public static final Capability<IAOTDPlayerData> PLAYER_DATA = null;

	public static void initialize()
	{
		CapabilityManager.INSTANCE.<IAOTDPlayerData> register(IAOTDPlayerData.class, new AOTDPlayerDataStorage(), AOTDDefaultPlayerData.class);
	}
}
