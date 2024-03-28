package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils

class PlayerSpellCharmDataCapabilitySerializer(instance: IPlayerSpellCharmData = PlayerSpellCharmData()) : AOTDCapabilitySerializer<IPlayerSpellCharmData, CompoundTag>(instance) {
    override fun getCapability() = ModCapabilities.PLAYER_SPELL_CHARM_DATA

    override fun serializeNBT(): CompoundTag {
        // Create a compound to write
        val nbt = CompoundTag()
        nbt.putInt(NBT_CHARM_TICKS, instance.charmTicks)
        instance.charmingEntityId?.let { nbt.put(NBT_CHARMING_ENTITY, NbtUtils.createUUID(it)) }
        return nbt
    }

    override fun deserializeNBTSafe(nbt: CompoundTag) {
        instance.charmTicks = nbt.getInt(NBT_CHARM_TICKS)

        if (nbt.contains(NBT_CHARMING_ENTITY)) {
            instance.charmingEntityId = NbtUtils.loadUUID(nbt.get(NBT_CHARMING_ENTITY)!!)
        } else {
            instance.charmingEntityId = null
        }
    }

    companion object {
        // NBT constants used for serialization
        private const val NBT_CHARM_TICKS = "charm_ticks"
        private const val NBT_CHARMING_ENTITY = "charming_entity"
    }
}