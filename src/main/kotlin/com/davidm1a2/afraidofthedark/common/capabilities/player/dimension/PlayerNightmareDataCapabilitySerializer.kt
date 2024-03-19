package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import com.davidm1a2.afraidofthedark.common.capabilities.INullableCapabilitySerializable
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import org.apache.logging.log4j.LogManager

class PlayerNightmareDataCapabilitySerializer : INullableCapabilitySerializable<CompoundTag> {
    private val instance: IPlayerNightmareData = PlayerNightmareData()

    override fun <V> getCapability(capability: Capability<V>?, side: Direction?): LazyOptional<V> {
        return if (capability == ModCapabilities.PLAYER_NIGHTMARE_DATA) LazyOptional.of { instance }.cast() else LazyOptional.empty()
    }

    override fun serializeNBT(): CompoundTag {
        // Create a compound to write
        val compound = CompoundTag()
        compound.putInt(NBT_POSITIONAL_INDEX, instance.positionalIndex)
        instance.preTeleportPlayerInventory?.let { compound.put(NBT_PRE_TELEPORT_INVENTORY, it) }
        instance.preTeleportPosition?.let { compound.put(NBT_PRE_TELEPORT_POSITION, NbtUtils.writeBlockPos(it)) }
        instance.preTeleportDimension?.let { compound.putString(NBT_PRE_TELEPORT_DIMENSION, it.location().toString()) }
        instance.preTeleportRespawnPosition?.let { compound.put(NBT_PRE_TELEPORT_RESPAWN_POSITION, it.serializeNBT()) }
        return compound
    }

    override fun deserializeNBT(nbt: CompoundTag?) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt != null) {
            // The compound to read from
            instance.positionalIndex = nbt.getInt(NBT_POSITIONAL_INDEX)
            if (nbt.contains(NBT_PRE_TELEPORT_INVENTORY)) {
                instance.preTeleportPlayerInventory = nbt.getList(
                    NBT_PRE_TELEPORT_INVENTORY,
                    Tag.TAG_COMPOUND.toInt()
                )
            } else {
                instance.preTeleportPlayerInventory = null
            }

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
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was null!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()

        // Constant IDs used in NBT
        private const val NBT_POSITIONAL_INDEX = "positional_index"
        private const val NBT_PRE_TELEPORT_INVENTORY = "pre_teleport_inventory"
        private const val NBT_PRE_TELEPORT_POSITION = "pre_teleport_position"
        private const val NBT_PRE_TELEPORT_DIMENSION = "pre_teleport_dimension"
        private const val NBT_PRE_TELEPORT_RESPAWN_POSITION = "pre_teleport_respawn_position"
    }
}