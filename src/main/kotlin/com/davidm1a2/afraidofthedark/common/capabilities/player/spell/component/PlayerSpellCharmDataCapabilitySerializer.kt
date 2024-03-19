package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.common.capabilities.INullableCapabilitySerializable
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import org.apache.logging.log4j.LogManager

class PlayerSpellCharmDataCapabilitySerializer : INullableCapabilitySerializable<CompoundTag> {
    private val instance: IPlayerSpellCharmData = PlayerSpellCharmData()

    override fun <V> getCapability(capability: Capability<V>?, side: Direction?): LazyOptional<V> {
        return if (capability == ModCapabilities.PLAYER_SPELL_CHARM_DATA) LazyOptional.of { instance }.cast() else LazyOptional.empty()
    }

    override fun serializeNBT(): CompoundTag {
        // Create a compound to write
        val nbt = CompoundTag()
        nbt.putInt(NBT_CHARM_TICKS, instance.charmTicks)
        instance.charmingEntityId?.let { nbt.put(NBT_CHARMING_ENTITY, NbtUtils.createUUID(it)) }
        return nbt
    }

    override fun deserializeNBT(nbt: CompoundTag?) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt != null) {
            instance.charmTicks = nbt.getInt(NBT_CHARM_TICKS)

            if (nbt.contains(NBT_CHARMING_ENTITY)) {
                instance.charmingEntityId = NbtUtils.loadUUID(nbt.get(NBT_CHARMING_ENTITY)!!)
            } else {
                instance.charmingEntityId = null
            }
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was null!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()

        // NBT constants used for serialization
        private const val NBT_CHARM_TICKS = "charm_ticks"
        private const val NBT_CHARMING_ENTITY = "charming_entity"
    }
}