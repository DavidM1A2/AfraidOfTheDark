package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.DoubleTag

class PlayerSpellSolarDataCapabilitySerializer(instance: IPlayerSpellSolarData = PlayerSpellSolarData()) : AOTDCapabilitySerializer<IPlayerSpellSolarData, DoubleTag>(instance) {
    override fun getCapability() = ModCapabilities.PLAYER_SPELL_SOLAR_DATA

    override fun serializeNBT(): DoubleTag {
        return DoubleTag.valueOf(instance.vitae)
    }

    override fun deserializeNBTSafe(nbt: DoubleTag) {
        instance.vitae = nbt.asDouble
    }
}