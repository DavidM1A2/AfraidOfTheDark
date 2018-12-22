package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * All capabilities that AOTD adds
 */
public class ModCapabilities
{
	// Capability that all players get which allows them to have AOTD basics
	@CapabilityInject(IAOTDPlayerBasics.class)
	public static final Capability<IAOTDPlayerBasics> PLAYER_BASICS = null;
}
