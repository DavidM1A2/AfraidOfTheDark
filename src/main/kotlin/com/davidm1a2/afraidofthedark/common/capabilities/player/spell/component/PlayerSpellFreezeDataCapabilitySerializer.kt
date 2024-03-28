package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.Vec3

class PlayerSpellFreezeDataCapabilitySerializer(instance: IPlayerSpellFreezeData = PlayerSpellFreezeData()) : AOTDCapabilitySerializer<IPlayerSpellFreezeData, CompoundTag>(instance) {
    override fun getCapability() = ModCapabilities.PLAYER_SPELL_FREEZE_DATA

    override fun serializeNBT(): CompoundTag {
        // Create a compound to write
        val nbt = CompoundTag()
        nbt.putInt(NBT_FREEZE_TICKS, instance.freezeTicks)
        instance.freezePosition?.let {
            nbt.putDouble(NBT_POSITION + "_x", it.x)
            nbt.putDouble(NBT_POSITION + "_y", it.y)
            nbt.putDouble(NBT_POSITION + "_z", it.z)
        }
        nbt.putFloat(NBT_DIRECTION_YAW, instance.freezeYaw)
        nbt.putFloat(NBT_DIRECTION_PITCH, instance.freezePitch)
        return nbt
    }

    override fun deserializeNBTSafe(nbt: CompoundTag) {
        instance.freezeTicks = nbt.getInt(NBT_FREEZE_TICKS)

        if (nbt.contains(NBT_POSITION + "_x") &&
            nbt.contains(NBT_POSITION + "_y") &&
            nbt.contains(NBT_POSITION + "_z")
        ) {
            instance.freezePosition = Vec3(
                nbt.getDouble(NBT_POSITION + "_x"),
                nbt.getDouble(NBT_POSITION + "_y"),
                nbt.getDouble(NBT_POSITION + "_z")
            )
        }

        instance.freezeYaw = nbt.getFloat(NBT_DIRECTION_YAW)
        instance.freezePitch = nbt.getFloat(NBT_DIRECTION_PITCH)
    }

    companion object {
        // NBT constants used for serialization
        private const val NBT_FREEZE_TICKS = "freeze_ticks"
        private const val NBT_POSITION = "position"
        private const val NBT_DIRECTION_YAW = "direction_yaw"
        private const val NBT_DIRECTION_PITCH = "direction_pitch"
    }
}