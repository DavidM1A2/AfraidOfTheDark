package com.davidm1a2.afraidofthedark.common.capabilities

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerVoidChestData
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellCharmData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellFreezeData
import com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors.IAOTDWorldIslandVisitors
import com.davidm1a2.afraidofthedark.common.capabilities.world.spellStates.IAOTDWorldSpellStates
import com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap.IAOTDWorldStructureCollisionMap
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
fun PlayerEntity.getResearch(): IAOTDPlayerResearch {
    return this.getCapability(ModCapabilities.PLAYER_RESEARCH).orElseThrow {
        IllegalStateException("Could not get player's research")
    }
}

/**
 * @return The player's 'PLAYER_VOID_CHEST_DATA' capability
 */
fun PlayerEntity.getVoidChestData(): IAOTDPlayerVoidChestData {
    return this.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA).orElseThrow {
        IllegalStateException("Could not get player's void chest data")
    }
}

/**
 * @return The player's 'PLAYER_NIGHTMARE_DATA' capability
 */
fun PlayerEntity.getNightmareData(): IAOTDPlayerNightmareData {
    return this.getCapability(ModCapabilities.PLAYER_NIGHTMARE_DATA).orElseThrow {
        IllegalStateException("Could not get player's nightmare data")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_MANAGER' capability
 */
fun PlayerEntity.getSpellManager(): IAOTDPlayerSpellManager {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER).orElseThrow {
        IllegalStateException("Could not get player's spell manager")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_FREEZE_DATA' capability
 */
fun PlayerEntity.getSpellFreezeData(): IAOTDPlayerSpellFreezeData {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA).orElseThrow {
        IllegalStateException("Could not get player's freeze data")
    }
}

/**
 * @return The player's 'PLAYER_SPELL_CHARM_DATA' capability
 */
fun PlayerEntity.getSpellCharmData(): IAOTDPlayerSpellCharmData {
    return this.getCapability(ModCapabilities.PLAYER_SPELL_CHARM_DATA).orElseThrow {
        IllegalStateException("Could not get player's charm data")
    }
}

/**
 * @return The world's 'WORLD_SPELL_STATES' capability
 */
fun World.getSpellStates(): IAOTDWorldSpellStates {
    return this.getCapability(ModCapabilities.WORLD_SPELL_STATES).orElseThrow {
        IllegalStateException("Could not get world's spell state data")
    }
}

/**
 * @return The world's 'WORLD_ISLAND_VISITORS' capability
 */
fun World.getIslandVisitors(): IAOTDWorldIslandVisitors {
    return this.getCapability(ModCapabilities.WORLD_ISLAND_VISITORS).orElseThrow {
        IllegalStateException("Could not get world's island visitors data")
    }
}

/**
 * @return The world's 'WORLD_STRUCTURE_COLLISION_MAP' capability
 */
fun World.getStructureCollisionMap(): IAOTDWorldStructureCollisionMap {
    return this.getCapability(ModCapabilities.WORLD_STRUCTURE_COLLISION_MAP).orElseThrow {
        IllegalStateException("Could not get world's structure collision map")
    }
}