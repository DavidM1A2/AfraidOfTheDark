package com.davidm1a2.afraidofthedark.common.capabilities

import com.davidm1a2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerVoidChestData
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.IAOTDPlayerSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellCharmData
import com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellFreezeData
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.entity.player.EntityPlayer

// Extension functions to access these player capabilities more easily

fun EntityPlayer.getBasics(): IAOTDPlayerBasics
{
    return this.getCapability(ModCapabilities.PLAYER_BASICS, null)!!
}

fun EntityPlayer.getResearch(): IAOTDPlayerResearch
{
    return this.getCapability(ModCapabilities.PLAYER_RESEARCH, null)!!
}

fun EntityPlayer.getVoidChestData(): IAOTDPlayerVoidChestData
{
    return this.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null)!!
}

fun EntityPlayer.getNightmareData(): IAOTDPlayerNightmareData
{
    return this.getCapability(ModCapabilities.PLAYER_NIGHTMARE_DATA, null)!!
}

fun EntityPlayer.getSpellManager(): IAOTDPlayerSpellManager
{
    return this.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null)!!
}

fun EntityPlayer.getSpellFreezeData(): IAOTDPlayerSpellFreezeData
{
    return this.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null)!!
}

fun EntityPlayer.getSpellCharmData(): IAOTDPlayerSpellCharmData
{
    return this.getCapability(ModCapabilities.PLAYER_SPELL_CHARM_DATA, null)!!
}