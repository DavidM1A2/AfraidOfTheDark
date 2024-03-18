package com.davidm1a2.afraidofthedark.common.capabilities

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
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunk

// Extension functions to access these player capabilities more easily

fun Player.hasStartedAOTD(): Boolean {
    return getResearch().isResearched(ModResearches.THE_JOURNEY_BEGINS)
}

/**
 * @return The player's 'PLAYER_BASICS' capability
 */
fun Player.getBasics(): IAOTDPlayerBasics {
    return this.getCapability(ModCapabilities.PLAYER_BASICS).orElseThrow {
        IllegalStateException("Could not get player's basics")
    }
}

/**
 * @return The player's 'PLAYER_RESEARCH' capability
 */
fun Player.getResearch(): IPlayerResearch {
    return this.getCapability(ModCapabilities.PLAYER_RESEARCH).orElseThrow {
        IllegalStateException("Could not get player's research")
    }
}

/**
 * @return The player's 'PLAYER_VOID_CHEST_DATA' capability
 */
fun Player.getVoidChestData(): IPlayerVoidChestData {
    return this.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA).orElseThrow {
        IllegalStateException("Could not get player's void chest data")
    }
}

/**
 * @return The player's 'PLAYER_NIGHTMARE_DATA' capability
 */
fun Player.getNightmareData(): IPlayerNightmareData {
    return this.getCapability(ModCapabilities.PLAYER_NIGHTMARE_DATA).orElseThrow {
        IllegalStateException("Could not get player's nightmare data")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_MANAGER' capability
 */
fun Player.getSpellManager(): IPlayerSpellManager {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER).orElseThrow {
        IllegalStateException("Could not get player's spell manager")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_FREEZE_DATA' capability
 */
fun Player.getSpellFreezeData(): IPlayerSpellFreezeData {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA).orElseThrow {
        IllegalStateException("Could not get player's freeze data")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_CHARM_DATA' capability
 */
fun Player.getSpellCharmData(): IPlayerSpellCharmData {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_CHARM_DATA).orElseThrow {
        IllegalStateException("Could not get player's charm data")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_LUNAR_DATA' capability
 */
fun Player.getSpellLunarData(): IPlayerSpellLunarData {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_LUNAR_DATA).orElseThrow {
        IllegalStateException("Could not get player's lunar data")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_SOLAR_DATA' capability
 */
fun Player.getSpellSolarData(): IPlayerSpellSolarData {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_SOLAR_DATA).orElseThrow {
        IllegalStateException("Could not get player's solar data")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_THERMAL_DATA' capability
 */
fun Player.getSpellThermalData(): IPlayerSpellThermalData {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_THERMAL_DATA).orElseThrow {
        IllegalStateException("Could not get player's thermal data")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_INNATE_DATA' capability
 */
fun Player.getSpellInnateData(): IPlayerSpellInnateData {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_INNATE_DATA).orElseThrow {
        IllegalStateException("Could not get the player's innate data")
    }
}

/**
 * @return The world's 'WORLD_SPELL_STATES' capability
 */
fun Level.getSpellStates(): IWorldSpellStates {
    return this.getCapability(ModCapabilities.WORLD_SPELL_STATES).orElseThrow {
        IllegalStateException("Could not get world's spell state data")
    }
}

/**
 * @return The world's 'WORLD_ISLAND_VISITORS' capability
 */
fun Level.getIslandVisitors(): IWorldIslandVisitors {
    return this.getCapability(ModCapabilities.WORLD_ISLAND_VISITORS).orElseThrow {
        IllegalStateException("Could not get world's island visitors data")
    }
}

fun Level.getStructureMapper(): IWorldStructureMapper {
    return this.getCapability(ModCapabilities.WORLD_STRUCTURE_MAPPER).orElseThrow {
        IllegalStateException("Could not get world's structure mapper data")
    }
}

fun LevelChunk.getWardedBlockMap(): IWardedBlockMap {
    return this.getCapability(ModCapabilities.WARDED_BLOCK_MAP).orElseThrow {
        IllegalStateException("Could not get chunk's warded block map")
    }
}