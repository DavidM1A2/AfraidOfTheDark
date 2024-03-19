package com.davidm1a2.afraidofthedark.common.constants

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
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken


/**
 * All capabilities that AOTD adds
 */
object ModCapabilities {
    // Capability that all players get which allows them to have AOTD basics
    val PLAYER_BASICS: Capability<IAOTDPlayerBasics> = CapabilityManager.get(object : CapabilityToken<IAOTDPlayerBasics>() {})

    // Capability that all players get which allows them to have research
    val PLAYER_RESEARCH: Capability<IPlayerResearch> = CapabilityManager.get(object : CapabilityToken<IPlayerResearch>() {})

    // Capability that all players get which allows them to go to and from the void chest dimension
    val PLAYER_VOID_CHEST_DATA: Capability<IPlayerVoidChestData> = CapabilityManager.get(object : CapabilityToken<IPlayerVoidChestData>() {})

    // Capability that all players get which allows them to go to and from the nightmare dimension
    val PLAYER_NIGHTMARE_DATA: Capability<IPlayerNightmareData> = CapabilityManager.get(object : CapabilityToken<IPlayerNightmareData>() {})

    // Capability that all players get which allows them to store spells inside their data
    val PLAYER_SPELL_MANAGER: Capability<IPlayerSpellManager> = CapabilityManager.get(object : CapabilityToken<IPlayerSpellManager>() {})

    // Capability that all players get which allows them to be frozen
    val PLAYER_SPELL_FREEZE_DATA: Capability<IPlayerSpellFreezeData> = CapabilityManager.get(object : CapabilityToken<IPlayerSpellFreezeData>() {})

    // Capability that all players get which allows them to be charmed
    val PLAYER_SPELL_CHARM_DATA: Capability<IPlayerSpellCharmData> = CapabilityManager.get(object : CapabilityToken<IPlayerSpellCharmData>() {})

    // Capability that all players get which allows them to have "lunar" vitae
    val PLAYER_SPELL_LUNAR_DATA: Capability<IPlayerSpellLunarData> = CapabilityManager.get(object : CapabilityToken<IPlayerSpellLunarData>() {})

    // Capability that all players get which allows them to have "solar" vitae
    val PLAYER_SPELL_SOLAR_DATA: Capability<IPlayerSpellSolarData> = CapabilityManager.get(object : CapabilityToken<IPlayerSpellSolarData>() {})

    // Capability that all players get which allows them to have "thermal" vitae
    val PLAYER_SPELL_THERMAL_DATA: Capability<IPlayerSpellThermalData> = CapabilityManager.get(object : CapabilityToken<IPlayerSpellThermalData>() {})

    // Capability that all players get which allows them to have "innate" vitae
    val PLAYER_SPELL_INNATE_DATA: Capability<IPlayerSpellInnateData> = CapabilityManager.get(object : CapabilityToken<IPlayerSpellInnateData>() {})

    // Capability that all worlds get which allows them to store spell state data
    val WORLD_SPELL_STATES: Capability<IWorldSpellStates> = CapabilityManager.get(object : CapabilityToken<IWorldSpellStates>() {})

    // Capability that the VoidChest and Nightmare worlds get which allows them to store island visitors
    val WORLD_ISLAND_VISITORS: Capability<IWorldIslandVisitors> = CapabilityManager.get(object : CapabilityToken<IWorldIslandVisitors>() {})

    // Capability that the overwold gets containing the world's structure map
    val WORLD_STRUCTURE_MAPPER: Capability<IWorldStructureMapper> = CapabilityManager.get(object : CapabilityToken<IWorldStructureMapper>() {})

    // Capability that a chunk gets containing the chunk's warded block map
    val WARDED_BLOCK_MAP: Capability<IWardedBlockMap> = CapabilityManager.get(object : CapabilityToken<IWardedBlockMap>() {})
}