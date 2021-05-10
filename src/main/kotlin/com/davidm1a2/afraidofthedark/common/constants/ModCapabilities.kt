package com.davidm1a2.afraidofthedark.common.constants

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
    @CapabilityInject(IAOTDPlayerResearch::class)
    lateinit var PLAYER_RESEARCH: Capability<IAOTDPlayerResearch>

    // Capability that all players get which allows them to go to and from the void chest dimension
    @JvmStatic
    @CapabilityInject(IAOTDPlayerVoidChestData::class)
    lateinit var PLAYER_VOID_CHEST_DATA: Capability<IAOTDPlayerVoidChestData>

    // Capability that all players get which allows them to go to and from the nightmare dimension
    @JvmStatic
    @CapabilityInject(IAOTDPlayerNightmareData::class)
    lateinit var PLAYER_NIGHTMARE_DATA: Capability<IAOTDPlayerNightmareData>

    // Capability that all players get which allows them to store spells inside their data
    @JvmStatic
    @CapabilityInject(IAOTDPlayerSpellManager::class)
    lateinit var PLAYER_SPELL_MANAGER: Capability<IAOTDPlayerSpellManager>

    // Capability that all players get which allows them to be frozen
    @JvmStatic
    @CapabilityInject(IAOTDPlayerSpellFreezeData::class)
    lateinit var PLAYER_SPELL_FREEZE_DATA: Capability<IAOTDPlayerSpellFreezeData>

    // Capability that all players get which allows them to be charmed
    @JvmStatic
    @CapabilityInject(IAOTDPlayerSpellCharmData::class)
    lateinit var PLAYER_SPELL_CHARM_DATA: Capability<IAOTDPlayerSpellCharmData>

    // Capability that all worlds get which allows them to store spell state data
    @JvmStatic
    @CapabilityInject(IAOTDWorldSpellStates::class)
    lateinit var WORLD_SPELL_STATES: Capability<IAOTDWorldSpellStates>

    // Capability that the VoidChest and Nightmare worlds get which allows them to store island visitors
    @JvmStatic
    @CapabilityInject(IAOTDWorldIslandVisitors::class)
    lateinit var WORLD_ISLAND_VISITORS: Capability<IAOTDWorldIslandVisitors>

    // Capability that worlds get which allows them to compute structure collisions
    @JvmStatic
    @CapabilityInject(IAOTDWorldStructureCollisionMap::class)
    lateinit var WORLD_STRUCTURE_COLLISION_MAP: Capability<IAOTDWorldStructureCollisionMap>
}