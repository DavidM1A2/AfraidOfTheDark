package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.capabilities.chunk.ward.IWardedBlockMap
import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IPlayerNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IPlayerVoidChestData
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IPlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IPlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellCharmData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellFreezeData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellInnateData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellLunarData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellSolarData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellThermalData
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.IWorldIslandVisitors
import com.davidm1a2.afraidofthedark.common.capabilities.world.spell.IWorldSpellStates
import com.davidm1a2.afraidofthedark.common.capabilities.world.structure.IWorldStructureMapper
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

    // Capability that all players get which allows them to have "lunar" vitae
    @JvmStatic
    @CapabilityInject(IPlayerSpellLunarData::class)
    lateinit var PLAYER_SPELL_LUNAR_DATA: Capability<IPlayerSpellLunarData>

    // Capability that all players get which allows them to have "solar" vitae
    @JvmStatic
    @CapabilityInject(IPlayerSpellSolarData::class)
    lateinit var PLAYER_SPELL_SOLAR_DATA: Capability<IPlayerSpellSolarData>

    // Capability that all players get which allows them to have "thermal" vitae
    @JvmStatic
    @CapabilityInject(IPlayerSpellThermalData::class)
    lateinit var PLAYER_SPELL_THERMAL_DATA: Capability<IPlayerSpellThermalData>

    // Capability that all players get which allows them to have "innate" vitae
    @JvmStatic
    @CapabilityInject(IPlayerSpellInnateData::class)
    lateinit var PLAYER_SPELL_INNATE_DATA: Capability<IPlayerSpellInnateData>

    // Capability that all worlds get which allows them to store spell state data
    @JvmStatic
    @CapabilityInject(IWorldSpellStates::class)
    lateinit var WORLD_SPELL_STATES: Capability<IWorldSpellStates>

    // Capability that the VoidChest and Nightmare worlds get which allows them to store island visitors
    @JvmStatic
    @CapabilityInject(IWorldIslandVisitors::class)
    lateinit var WORLD_ISLAND_VISITORS: Capability<IWorldIslandVisitors>

    // Capability that the overwold gets containing the world's structure map
    @JvmStatic
    @CapabilityInject(IWorldStructureMapper::class)
    lateinit var WORLD_STRUCTURE_MAPPER: Capability<IWorldStructureMapper>

    // Capability that a chunk gets containing the chunk's warded block map
    @JvmStatic
    @CapabilityInject(IWardedBlockMap::class)
    lateinit var WARDED_BLOCK_MAP: Capability<IWardedBlockMap>
}