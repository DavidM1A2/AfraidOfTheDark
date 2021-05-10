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
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.AOTDWorldIslandVisitorsImpl
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.AOTDWorldIslandVisitorsStorage
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.IAOTDWorldIslandVisitors
import com.davidm1a2.afraidofthedark.common.capabilities.world.spellStates.AOTDWorldSpellStatesImpl
import com.davidm1a2.afraidofthedark.common.capabilities.world.spellStates.AOTDWorldSpellStatesStorage
import com.davidm1a2.afraidofthedark.common.capabilities.world.spellStates.IAOTDWorldSpellStates
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap.AOTDWorldStructureCollisionMapImpl
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap.AOTDWorldStructureCollisionMapStorage
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap.IAOTDWorldStructureCollisionMap
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
            CapabilityManager.INSTANCE.register(
                IAOTDWorldIslandVisitors::class.java,
                AOTDWorldIslandVisitorsStorage()
            ) { AOTDWorldIslandVisitorsImpl() }
            CapabilityManager.INSTANCE.register(
                IAOTDWorldStructureCollisionMap::class.java,
                AOTDWorldStructureCollisionMapStorage()
            ) { AOTDWorldStructureCollisionMapImpl() }
        }
        isInitialized = true
    }
}