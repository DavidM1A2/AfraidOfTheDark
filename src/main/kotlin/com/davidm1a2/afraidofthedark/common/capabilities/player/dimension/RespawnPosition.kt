package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTDynamicOps
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.RegistryKey
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class RespawnPosition(
    val respawnPosition: BlockPos?,
    val respawnDimension: RegistryKey<World>,
    val respawnAngle: Float,
    val respawnForced: Boolean
) {
    fun serializeNBT(): CompoundNBT {
        val nbt = CompoundNBT()

        respawnPosition?.let { nbt.put(NBT_RESPAWN_POSITION, NBTUtil.writeBlockPos(it)) }
        ResourceLocation.CODEC.encodeStart(NBTDynamicOps.INSTANCE, respawnDimension.location()).result().ifPresent { nbt.put(NBT_RESPAWN_DIMENSION, it) }
        nbt.putFloat(NBT_RESPAWN_ANGLE, respawnAngle)
        nbt.putBoolean(NBT_RESPAWN_FORCED, respawnForced)

        return nbt
    }

    companion object {
        private const val NBT_RESPAWN_POSITION = "respawn_position"
        private const val NBT_RESPAWN_DIMENSION = "respawn_dimension"
        private const val NBT_RESPAWN_ANGLE = "respawn_angle"
        private const val NBT_RESPAWN_FORCED = "respawn_forced"

        fun from(nbt: CompoundNBT): RespawnPosition {
            val respawnPosition = if (nbt.contains(NBT_RESPAWN_POSITION)) {
                NBTUtil.readBlockPos(nbt.getCompound(NBT_RESPAWN_POSITION))
            } else {
                null
            }
            val respawnDimension = World.RESOURCE_KEY_CODEC.parse(NBTDynamicOps.INSTANCE, nbt.get(NBT_RESPAWN_DIMENSION)).result().orElse(World.OVERWORLD)
            val respawnAngle = nbt.getFloat(NBT_RESPAWN_ANGLE)
            val respawnForced = nbt.getBoolean(NBT_RESPAWN_FORCED)

            return RespawnPosition(respawnPosition, respawnDimension, respawnAngle, respawnForced)
        }
    }
}