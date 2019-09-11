package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerNightmareData;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerVoidChestData;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellCharmData;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellFreezeData;
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

    // Capability that all players get which allows them to have research
    @CapabilityInject(IAOTDPlayerResearch.class)
    public static final Capability<IAOTDPlayerResearch> PLAYER_RESEARCH = null;

    // Capability that all players get which allows them to go to and from the void chest dimension
    @CapabilityInject(IAOTDPlayerVoidChestData.class)
    public static final Capability<IAOTDPlayerVoidChestData> PLAYER_VOID_CHEST_DATA = null;

    // Capability that all players get which allows them to go to and from the nightmare dimension
    @CapabilityInject(IAOTDPlayerNightmareData.class)
    public static final Capability<IAOTDPlayerNightmareData> PLAYER_NIGHTMARE_DATA = null;

    // Capability that all players get which allows them to store spells inside their data
    @CapabilityInject(IAOTDPlayerSpellManager.class)
    public static final Capability<IAOTDPlayerSpellManager> PLAYER_SPELL_MANAGER = null;

    // Capability that all players get which allows them to be frozen
    @CapabilityInject(IAOTDPlayerSpellFreezeData.class)
    public static final Capability<IAOTDPlayerSpellFreezeData> PLAYER_SPELL_FREEZE_DATA = null;

    // Capability that all players get which allows them to be charmed
    @CapabilityInject(IAOTDPlayerSpellCharmData.class)
    public static final Capability<IAOTDPlayerSpellCharmData> PLAYER_SPELL_CHARM_DATA = null;
}
