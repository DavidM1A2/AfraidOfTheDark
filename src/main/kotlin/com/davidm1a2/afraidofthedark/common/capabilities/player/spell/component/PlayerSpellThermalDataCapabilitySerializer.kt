package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.CompoundTag

class PlayerSpellThermalDataCapabilitySerializer(instance: IPlayerSpellThermalData = PlayerSpellThermalData()) : AOTDCapabilitySerializer<IPlayerSpellThermalData, CompoundTag>(instance) {
    override fun getCapability() = ModCapabilities.PLAYER_SPELL_THERMAL_DATA

    override fun serializeNBT(): CompoundTag {
        val compound = CompoundTag()

        compound.putDouble("vitae", instance.vitae)
        compound.putDouble("heat", instance.heat)

        return compound
    }

    override fun deserializeNBTSafe(nbt: CompoundTag) {
        instance.vitae = nbt.getDouble("vitae")
        instance.heat = nbt.getDouble("heat")
    }
}