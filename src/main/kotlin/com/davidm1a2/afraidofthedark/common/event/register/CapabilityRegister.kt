package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.capabilities.chunk.ward.IWardedBlockMap
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IPlayerNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IPlayerVoidChestData
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IPlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IPlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.*
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.IWorldIslandVisitors
import com.davidm1a2.afraidofthedark.common.capabilities.world.spell.IWorldSpellStates
import com.davidm1a2.afraidofthedark.common.capabilities.world.structure.IWorldStructureMapper
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class CapabilityRegister {
    /**
     * Called to register our mod's capabilities
     */
    @SubscribeEvent
    fun registerCapabilitiesEvent(event: RegisterCapabilitiesEvent) {
        event.register(IAOTDPlayerBasics::class.java)
        event.register(IPlayerResearch::class.java)
        event.register(IPlayerVoidChestData::class.java)
        event.register(IPlayerNightmareData::class.java)
        event.register(IPlayerSpellManager::class.java)
        event.register(IPlayerSpellFreezeData::class.java)
        event.register(IPlayerSpellCharmData::class.java)
        event.register(IPlayerSpellLunarData::class.java)
        event.register(IPlayerSpellSolarData::class.java)
        event.register(IPlayerSpellThermalData::class.java)
        event.register(IPlayerSpellInnateData::class.java)
        event.register(IWorldSpellStates::class.java)
        event.register(IWorldIslandVisitors::class.java)
        event.register(IWorldStructureMapper::class.java)
        event.register(IWardedBlockMap::class.java)
    }
}