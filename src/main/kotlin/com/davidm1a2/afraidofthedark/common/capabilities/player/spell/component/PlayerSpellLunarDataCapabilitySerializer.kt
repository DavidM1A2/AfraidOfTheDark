package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.DoubleTag

class PlayerSpellLunarDataCapabilitySerializer(instance: IPlayerSpellLunarData = PlayerSpellLunarData()) : AOTDCapabilitySerializer<IPlayerSpellLunarData, DoubleTag>(instance) {
    override fun getCapability() = ModCapabilities.PLAYER_SPELL_LUNAR_DATA

    override fun serializeNBT(): DoubleTag {
        return DoubleTag.valueOf(instance.vitae)
    }

    override fun deserializeNBTSafe(nbt: DoubleTag) {
        instance.vitae = nbt.asDouble
    }
}