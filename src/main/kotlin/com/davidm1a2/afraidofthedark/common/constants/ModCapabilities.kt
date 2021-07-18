package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IPlayerNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IPlayerVoidChestData
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IPlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IPlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellCharmData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellFreezeData
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.IWorldIslandVisitors
import com.davidm1a2.afraidofthedark.common.capabilities.world.spell.IWorldSpellStates
import com.davidm1a2.afraidofthedark.common.capabilities.world.structure.IWorldMasterChunkMap
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject

/**
 * All capabilities that AOTD adds
 */
object ModCapabilities {
    // Capability that all players get which allows them to have AOTD basics
    @JvmStatic
    @CapabilityInject(IAOTDPlayerBasics::class)
    lateinit var PLAYER_BASICS: Capability<IAOTDPlayerBasics>

    // Capability that all players get which allows them to have research
    @JvmStatic
    @CapabilityInject(IPlayerResearch::class)
    lateinit var PLAYER_RESEARCH: Capability<IPlayerResearch>

    // Capability that all players get which allows them to go to and from the void chest dimension
    @JvmStatic
    @CapabilityInject(IPlayerVoidChestData::class)
    lateinit var PLAYER_VOID_CHEST_DATA: Capability<IPlayerVoidChestData>

    // Capability that all players get which allows them to go to and from the nightmare dimension
    @JvmStatic
    @CapabilityInject(IPlayerNightmareData::class)
    lateinit var PLAYER_NIGHTMARE_DATA: Capability<IPlayerNightmareData>

    // Capability that all players get which allows them to store spells inside their data
    @JvmStatic
    @CapabilityInject(IPlayerSpellManager::class)
    lateinit var PLAYER_SPELL_MANAGER: Capability<IPlayerSpellManager>

    // Capability that all players get which allows them to be frozen
    @JvmStatic
    @CapabilityInject(IPlayerSpellFreezeData::class)
    lateinit var PLAYER_SPELL_FREEZE_DATA: Capability<IPlayerSpellFreezeData>

    // Capability that all players get which allows them to be charmed
    @JvmStatic
    @CapabilityInject(IPlayerSpellCharmData::class)
    lateinit var PLAYER_SPELL_CHARM_DATA: Capability<IPlayerSpellCharmData>

    // Capability that all worlds get which allows them to store spell state data
    @JvmStatic
    @CapabilityInject(IWorldSpellStates::class)
    lateinit var WORLD_SPELL_STATES: Capability<IWorldSpellStates>

    // Capability that the VoidChest and Nightmare worlds get which allows them to store island visitors
    @JvmStatic
    @CapabilityInject(IWorldIslandVisitors::class)
    lateinit var WORLD_ISLAND_VISITORS: Capability<IWorldIslandVisitors>

    // Capability that the overwold gets containing a list of master chunks
    @JvmStatic
    @CapabilityInject(IWorldMasterChunkMap::class)
    lateinit var WORLD_MASTER_CHUNK_MAP: Capability<IWorldMasterChunkMap>
}