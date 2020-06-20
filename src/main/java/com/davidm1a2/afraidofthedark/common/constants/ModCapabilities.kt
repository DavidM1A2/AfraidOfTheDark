package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsImpl
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.*
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.AOTDPlayerResearchImpl
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.AOTDPlayerResearchStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.AOTDPlayerSpellManagerImpl
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.AOTDPlayerSpellManagerStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.*
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager

/**
 * All capabilities that AOTD adds
 */
object ModCapabilities {
    private var isInitialized = false

    // Capability that all players get which allows them to have AOTD basics
    @JvmStatic
    @CapabilityInject(IAOTDPlayerBasics::class)
    lateinit var PLAYER_BASICS: Capability<IAOTDPlayerBasics>

    // Capability that all players get which allows them to have research
    @JvmStatic
    @CapabilityInject(IAOTDPlayerResearch::class)
    lateinit var PLAYER_RESEARCH: Capability<IAOTDPlayerResearch>

    // Capability that all players get which allows them to go to and from the void chest dimension
    @JvmStatic
    @CapabilityInject(IAOTDPlayerVoidChestData::class)
    lateinit var PLAYER_VOID_CHEST_DATA: Capability<IAOTDPlayerVoidChestData>

    // Capability that all players get which allows them to go to and from the nightmare dimension
    @JvmStatic
    @CapabilityInject(IAOTDPlayerNightmareData::class)
    lateinit var PLAYER_NIGHTMARE_DATA: Capability<IAOTDPlayerNightmareData>

    // Capability that all players get which allows them to store spells inside their data
    @JvmStatic
    @CapabilityInject(IAOTDPlayerSpellManager::class)
    lateinit var PLAYER_SPELL_MANAGER: Capability<IAOTDPlayerSpellManager>

    // Capability that all players get which allows them to be frozen
    @JvmStatic
    @CapabilityInject(IAOTDPlayerSpellFreezeData::class)
    lateinit var PLAYER_SPELL_FREEZE_DATA: Capability<IAOTDPlayerSpellFreezeData>

    // Capability that all players get which allows them to be charmed
    @JvmStatic
    @CapabilityInject(IAOTDPlayerSpellCharmData::class)
    lateinit var PLAYER_SPELL_CHARM_DATA: Capability<IAOTDPlayerSpellCharmData>

    /**
     * Called to register our mod's capabilities
     */
    fun register() {
        if (!isInitialized) {
            CapabilityManager.INSTANCE.register(
                IAOTDPlayerBasics::class.java,
                AOTDPlayerBasicsStorage()
            ) { AOTDPlayerBasicsImpl() }
            CapabilityManager.INSTANCE.register(
                IAOTDPlayerResearch::class.java,
                AOTDPlayerResearchStorage()
            ) { AOTDPlayerResearchImpl() }
            CapabilityManager.INSTANCE.register(
                IAOTDPlayerVoidChestData::class.java,
                AOTDPlayerVoidChestDataStorage()
            ) { AOTDPlayerVoidChestDataImpl() }
            CapabilityManager.INSTANCE.register(
                IAOTDPlayerNightmareData::class.java,
                AOTDPlayerNightmareDataStorage()
            ) { AOTDPlayerNightmareImpl() }
            CapabilityManager.INSTANCE.register(
                IAOTDPlayerSpellManager::class.java,
                AOTDPlayerSpellManagerStorage()
            ) { AOTDPlayerSpellManagerImpl() }
            CapabilityManager.INSTANCE.register(
                IAOTDPlayerSpellFreezeData::class.java,
                AOTDPlayerSpellFreezeDataStorage()
            ) { AOTDPlayerSpellFreezeDataImpl() }
            CapabilityManager.INSTANCE.register(
                IAOTDPlayerSpellCharmData::class.java,
                AOTDPlayerSpellCharmDataStorage()
            ) { AOTDPlayerSpellCharmDataImpl() }
        }
        isInitialized = true
    }
}