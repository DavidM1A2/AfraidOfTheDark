package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.NbtUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level

data class RespawnPosition(
    val respawnPosition: BlockPos?,
    val respawnDimension: ResourceKey<Level>,
    val respawnAngle: Float,
    val respawnForced: Boolean
) {
    fun serializeNBT(): CompoundTag {
        val nbt = CompoundTag()

        respawnPosition?.let { nbt.put(NBT_RESPAWN_POSITION, NbtUtils.writeBlockPos(it)) }
        ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, respawnDimension.location()).result().ifPresent { nbt.put(NBT_RESPAWN_DIMENSION, it) }
        nbt.putFloat(NBT_RESPAWN_ANGLE, respawnAngle)
        nbt.putBoolean(NBT_RESPAWN_FORCED, respawnForced)

        return nbt
    }

    companion object {
        private const val NBT_RESPAWN_POSITION = "respawn_position"
        private const val NBT_RESPAWN_DIMENSION = "respawn_dimension"
        private const val NBT_RESPAWN_ANGLE = "respawn_angle"
        private const val NBT_RESPAWN_FORCED = "respawn_forced"

        fun from(nbt: CompoundTag): RespawnPosition {
            val respawnPosition = if (nbt.contains(NBT_RESPAWN_POSITION)) {
                NbtUtils.readBlockPos(nbt.getCompound(NBT_RESPAWN_POSITION))
            } else {
                null
            }
            val respawnDimension = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get(NBT_RESPAWN_DIMENSION)).result().orElse(Level.OVERWORLD)
            val respawnAngle = nbt.getFloat(NBT_RESPAWN_ANGLE)
            val respawnForced = nbt.getBoolean(NBT_RESPAWN_FORCED)

            return RespawnPosition(respawnPosition, respawnDimension, respawnAngle, respawnForced)
        }
    }
}