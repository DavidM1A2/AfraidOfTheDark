package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.core.Registry
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

class PlayerVoidChestDataCapabilitySerializer(instance: IPlayerVoidChestData = PlayerVoidChestData()) : AOTDCapabilitySerializer<IPlayerVoidChestData, CompoundTag>(instance) {
    override fun getCapability() = ModCapabilities.PLAYER_VOID_CHEST_DATA

    override fun serializeNBT(): CompoundTag {
        // Create a compound to write
        val compound = CompoundTag()
        compound.putInt(NBT_POSITIONAL_INDEX, instance.positionalIndex)
        compound.putInt(NBT_FRIENDS_INDEX, instance.friendsIndex)
        instance.preTeleportPosition?.let { compound.put(NBT_PRE_TELEPORT_POSITION, NbtUtils.writeBlockPos(it)) }
        instance.preTeleportDimension?.let { compound.putString(NBT_PRE_TELEPORT_DIMENSION, it.location().toString()) }
        instance.preTeleportRespawnPosition?.let { compound.put(NBT_PRE_TELEPORT_RESPAWN_POSITION, it.serializeNBT()) }
        return compound
    }

    override fun deserializeNBTSafe(nbt: CompoundTag) {
        // The compound to read from
        instance.positionalIndex = nbt.getInt(NBT_POSITIONAL_INDEX)
        instance.friendsIndex = nbt.getInt(NBT_FRIENDS_INDEX)

        if (nbt.contains(NBT_PRE_TELEPORT_POSITION)) {
            instance.preTeleportPosition =
                NbtUtils.readBlockPos(nbt.getCompound(NBT_PRE_TELEPORT_POSITION))
        } else {
            instance.preTeleportPosition = null
        }

        if (nbt.contains(NBT_PRE_TELEPORT_DIMENSION)) {
            instance.preTeleportDimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, ResourceLocation(nbt.getString(NBT_PRE_TELEPORT_DIMENSION)))
        } else {
            instance.preTeleportDimension = null
        }

        if (nbt.contains(NBT_PRE_TELEPORT_RESPAWN_POSITION)) {
            instance.preTeleportRespawnPosition = RespawnPosition.from(nbt.getCompound(NBT_PRE_TELEPORT_RESPAWN_POSITION))
        } else {
            instance.preTeleportRespawnPosition = null
        }
    }

    companion object {
        // Constant IDs used in NBT
        private const val NBT_POSITIONAL_INDEX = "positional_index"
        private const val NBT_FRIENDS_INDEX = "friends_index"
        private const val NBT_PRE_TELEPORT_POSITION = "pre_teleport_position"
        private const val NBT_PRE_TELEPORT_DIMENSION = "pre_teleport_dimension"
        private const val NBT_PRE_TELEPORT_RESPAWN_POSITION = "pre_teleport_respawn_position"
    }
}