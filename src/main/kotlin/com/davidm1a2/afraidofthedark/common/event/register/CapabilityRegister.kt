package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IPlayerNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IPlayerVoidChestData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.PlayerNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.PlayerNightmareDataStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.PlayerVoidChestData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.PlayerVoidChestDataStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IPlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.PlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.PlayerResearchStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IPlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.PlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.PlayerSpellManagerStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellCharmData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellFreezeData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.PlayerSpellCharmData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.PlayerSpellCharmDataStorage
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.PlayerSpellFreezeData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.PlayerSpellFreezeDataStorage
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.IWorldIslandVisitors
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.WorldIslandVisitors
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.WorldIslandVisitorsStorage
import com.davidm1a2.afraidofthedark.common.capabilities.world.spell.IWorldSpellStates
import com.davidm1a2.afraidofthedark.common.capabilities.world.spell.WorldSpellStates
import com.davidm1a2.afraidofthedark.common.capabilities.world.spell.WorldSpellStatesStorage
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap.IWorldStructureCollisionMap
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap.WorldStructureCollisionMap
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap.WorldStructureCollisionMapStorage
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureMissCounter.IWorldStructureMissCounter
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureMissCounter.WorldStructureMissCounter
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureMissCounter.WorldStructureMissCounterStorage
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
            ) { AOTDPlayerBasics() }
            CapabilityManager.INSTANCE.register(
                IPlayerResearch::class.java,
                PlayerResearchStorage()
            ) { PlayerResearch() }
            CapabilityManager.INSTANCE.register(
                IPlayerVoidChestData::class.java,
                PlayerVoidChestDataStorage()
            ) { PlayerVoidChestData() }
            CapabilityManager.INSTANCE.register(
                IPlayerNightmareData::class.java,
                PlayerNightmareDataStorage()
            ) { PlayerNightmareData() }
            CapabilityManager.INSTANCE.register(
                IPlayerSpellManager::class.java,
                PlayerSpellManagerStorage()
            ) { PlayerSpellManager() }
            CapabilityManager.INSTANCE.register(
                IPlayerSpellFreezeData::class.java,
                PlayerSpellFreezeDataStorage()
            ) { PlayerSpellFreezeData() }
            CapabilityManager.INSTANCE.register(
                IPlayerSpellCharmData::class.java,
                PlayerSpellCharmDataStorage()
            ) { PlayerSpellCharmData() }
            CapabilityManager.INSTANCE.register(
                IWorldSpellStates::class.java,
                WorldSpellStatesStorage()
            ) { WorldSpellStates() }
            CapabilityManager.INSTANCE.register(
                IWorldIslandVisitors::class.java,
                WorldIslandVisitorsStorage()
            ) { WorldIslandVisitors() }
            CapabilityManager.INSTANCE.register(
                IWorldStructureCollisionMap::class.java,
                WorldStructureCollisionMapStorage()
            ) { WorldStructureCollisionMap() }
            CapabilityManager.INSTANCE.register(
                IWorldStructureMissCounter::class.java,
                WorldStructureMissCounterStorage()
            ) { WorldStructureMissCounter() }
        }
        isInitialized = true
    }
}