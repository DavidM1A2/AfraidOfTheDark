package com.davidm1a2.afraidofthedark.common.event.register

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
import com.davidm1a2.afraidofthedark.common.capabilities.world.spellState.AOTDWorldSpellStatesImpl
import com.davidm1a2.afraidofthedark.common.capabilities.world.spellState.AOTDWorldSpellStatesStorage
import com.davidm1a2.afraidofthedark.common.capabilities.world.spellState.IAOTDWorldSpellStates
import net.minecraftforge.common.capabilities.CapabilityManager

object CapabilityRegister {
    private var isInitialized = false

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
            CapabilityManager.INSTANCE.register(
                IAOTDWorldSpellStates::class.java,
                AOTDWorldSpellStatesStorage()
            ) { AOTDWorldSpellStatesImpl() }
        }
        isInitialized = true
    }
}