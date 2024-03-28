package com.davidm1a2.afraidofthedark.common.capabilities.chunk.ward

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.core.BlockPos
import net.minecraft.nbt.ByteArrayTag
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.LongArrayTag
import org.apache.logging.log4j.LogManager

class WardedBlockMapCapabilitySerializer(instance: IWardedBlockMap = WardedBlockMap()) : AOTDCapabilitySerializer<IWardedBlockMap, CompoundTag>(instance) {
    override fun getCapability() = ModCapabilities.WARDED_BLOCK_MAP

    override fun serializeNBT(): CompoundTag {
        val compound = CompoundTag()

        val wardedBlocks = instance.getWardedBlocks()
            .filter { instance.getWardStrength(it) != null }
            .associateWith { instance.getWardStrength(it)!! }
        val positions = LongArrayTag(wardedBlocks.keys.map { it.asLong() })
        val strengths = ByteArrayTag(wardedBlocks.values.map { it.toByte() }.toByteArray())

        compound.put(NBT_POSITIONS, positions)
        compound.put(NBT_STRENGTHS, strengths)

        return compound
    }

    override fun deserializeNBTSafe(nbt: CompoundTag) {
        val positions = nbt.getLongArray(NBT_POSITIONS)
        val strengths = nbt.getByteArray(NBT_STRENGTHS)
        if (positions.size == strengths.size) {
            for (i in positions.indices) {
                instance.wardBlock(BlockPos.of(positions[i]), strengths[i].toInt())
            }
        } else {
            LOG.error("Found an invalid warded block map storage. It had ${positions.size} positions and ${strengths.size} strengths.")
        }
    }

    companion object {
        private const val NBT_POSITIONS = "positions"
        private const val NBT_STRENGTHS = "strengths"

        private val LOG = LogManager.getLogger()
    }
}