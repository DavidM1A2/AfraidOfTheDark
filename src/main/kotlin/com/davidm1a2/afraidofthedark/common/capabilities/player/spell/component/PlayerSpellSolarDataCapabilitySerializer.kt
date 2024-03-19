package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.common.capabilities.INullableCapabilitySerializable
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.core.Direction
import net.minecraft.nbt.DoubleTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import org.apache.logging.log4j.LogManager

class PlayerSpellSolarDataCapabilitySerializer : INullableCapabilitySerializable<DoubleTag> {
    private val instance: IPlayerSpellSolarData = PlayerSpellSolarData()

    override fun <V> getCapability(capability: Capability<V>?, side: Direction?): LazyOptional<V> {
        return if (capability == ModCapabilities.PLAYER_SPELL_SOLAR_DATA) LazyOptional.of { instance }.cast() else LazyOptional.empty()
    }

    override fun serializeNBT(): DoubleTag {
        return DoubleTag.valueOf(instance.vitae)
    }

    override fun deserializeNBT(nbt: DoubleTag?) {
        if (nbt != null) {
            instance.vitae = nbt.asDouble
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was null!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}