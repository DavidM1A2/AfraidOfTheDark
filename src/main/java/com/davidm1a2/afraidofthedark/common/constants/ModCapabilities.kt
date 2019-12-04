package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerVoidChestData
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellCharmData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellFreezeData
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject

/**
 * All capabilities that AOTD adds
 */
object ModCapabilities
{
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

    // Extension functions to access these player capabilities more easily

    fun EntityPlayer.getBasics(): IAOTDPlayerBasics
    {
        return this.getCapability(PLAYER_BASICS, null)!!
    }

    fun EntityPlayer.getResearch(): IAOTDPlayerResearch
    {
        return this.getCapability(PLAYER_RESEARCH, null)!!
    }

    fun EntityPlayer.getVoidChestData(): IAOTDPlayerVoidChestData
    {
        return this.getCapability(PLAYER_VOID_CHEST_DATA, null)!!
    }

    fun EntityPlayer.getNightmareData(): IAOTDPlayerNightmareData
    {
        return this.getCapability(PLAYER_NIGHTMARE_DATA, null)!!
    }

    fun EntityPlayer.getSpellManager(): IAOTDPlayerSpellManager
    {
        return this.getCapability(PLAYER_SPELL_MANAGER, null)!!
    }

    fun EntityPlayer.getSpellFreezeData(): IAOTDPlayerSpellFreezeData
    {
        return this.getCapability(PLAYER_SPELL_FREEZE_DATA, null)!!
    }

    fun EntityPlayer.getSpellCharmData(): IAOTDPlayerSpellCharmData
    {
        return this.getCapability(PLAYER_SPELL_CHARM_DATA, null)!!
    }
}