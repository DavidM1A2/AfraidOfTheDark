package com.davidm1a2.afraidofthedark.common.capabilities

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IPlayerNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IPlayerVoidChestData
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IPlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IPlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellCharmData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IPlayerSpellFreezeData
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.IWorldIslandVisitors
import com.davidm1a2.afraidofthedark.common.capabilities.world.spellStates.IWorldSpellStates
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap.IWorldStructureCollisionMap
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureMissCounter.IWorldStructureMissCounter
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

// Extension functions to access these player capabilities more easily

/**
 * @return The player's 'PLAYER_BASICS' capability
 */
fun PlayerEntity.getBasics(): IAOTDPlayerBasics {
    return this.getCapability(ModCapabilities.PLAYER_BASICS).orElseThrow {
        IllegalStateException("Could not get player's basics")
    }
}

/**
 * @return The player's 'PLAYER_RESEARCH' capability
 */
fun PlayerEntity.getResearch(): IPlayerResearch {
    return this.getCapability(ModCapabilities.PLAYER_RESEARCH).orElseThrow {
        IllegalStateException("Could not get player's research")
    }
}

/**
 * @return The player's 'PLAYER_VOID_CHEST_DATA' capability
 */
fun PlayerEntity.getVoidChestData(): IPlayerVoidChestData {
    return this.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA).orElseThrow {
        IllegalStateException("Could not get player's void chest data")
    }
}

/**
 * @return The player's 'PLAYER_NIGHTMARE_DATA' capability
 */
fun PlayerEntity.getNightmareData(): IPlayerNightmareData {
    return this.getCapability(ModCapabilities.PLAYER_NIGHTMARE_DATA).orElseThrow {
        IllegalStateException("Could not get player's nightmare data")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_MANAGER' capability
 */
fun PlayerEntity.getSpellManager(): IPlayerSpellManager {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER).orElseThrow {
        IllegalStateException("Could not get player's spell manager")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_FREEZE_DATA' capability
 */
fun PlayerEntity.getSpellFreezeData(): IPlayerSpellFreezeData {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA).orElseThrow {
        IllegalStateException("Could not get player's freeze data")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_CHARM_DATA' capability
 */
fun PlayerEntity.getSpellCharmData(): IPlayerSpellCharmData {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_CHARM_DATA).orElseThrow {
        IllegalStateException("Could not get player's charm data")
    }
}

/**
 * @return The world's 'WORLD_SPELL_STATES' capability
 */
fun World.getSpellStates(): IWorldSpellStates {
    return this.getCapability(ModCapabilities.WORLD_SPELL_STATES).orElseThrow {
        IllegalStateException("Could not get world's spell state data")
    }
}

/**
 * @return The world's 'WORLD_ISLAND_VISITORS' capability
 */
fun World.getIslandVisitors(): IWorldIslandVisitors {
    return this.getCapability(ModCapabilities.WORLD_ISLAND_VISITORS).orElseThrow {
        IllegalStateException("Could not get world's island visitors data")
    }
}

/**
 * @return The world's 'WORLD_STRUCTURE_COLLISION_MAP' capability
 */
fun World.getStructureCollisionMap(): IWorldStructureCollisionMap {
    return this.getCapability(ModCapabilities.WORLD_STRUCTURE_COLLISION_MAP).orElseThrow {
        IllegalStateException("Could not get world's structure collision map")
    }
}

/**
 * @return The world's 'WORLD_STRUCTURE_COLLISION_MAP' capability
 */
fun World.getStructureMissCounter(): IWorldStructureMissCounter {
    return this.getCapability(ModCapabilities.WORLD_STRUCTURE_MISS_COUNTER).orElseThrow {
        IllegalStateException("Could not get world's structure miss counter")
    }
}