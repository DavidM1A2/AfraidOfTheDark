package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.common.capabilities.INullableCapabilitySerializable
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import org.apache.logging.log4j.LogManager

class PlayerSpellThermalDataCapabilitySerializer : INullableCapabilitySerializable<CompoundTag> {
    private val instance: IPlayerSpellThermalData = PlayerSpellThermalData()

    override fun <V> getCapability(capability: Capability<V>?, side: Direction?): LazyOptional<V> {
        return if (capability == ModCapabilities.PLAYER_SPELL_THERMAL_DATA) LazyOptional.of { instance }.cast() else LazyOptional.empty()
    }

    override fun serializeNBT(): CompoundTag {
        val compound = CompoundTag()

        compound.putDouble("vitae", instance.vitae)
        compound.putDouble("heat", instance.heat)

        return compound
    }

    override fun deserializeNBT(nbt: CompoundTag?) {
        if (nbt != null) {
            instance.vitae = nbt.getDouble("vitae")
            instance.heat = nbt.getDouble("heat")
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was null!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}