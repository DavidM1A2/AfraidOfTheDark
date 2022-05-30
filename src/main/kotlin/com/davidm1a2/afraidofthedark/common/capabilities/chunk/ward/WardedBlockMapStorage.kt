package com.davidm1a2.afraidofthedark.common.capabilities.chunk.ward

import net.minecraft.nbt.ByteArrayNBT
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.LongArrayNBT
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import org.apache.logging.log4j.LogManager

/**
 * Default storage implementation for warded block map storage
 */
class WardedBlockMapStorage : Capability.IStorage<IWardedBlockMap> {
    override fun writeNBT(
        capability: Capability<IWardedBlockMap>,
        instance: IWardedBlockMap,
        side: Direction?
    ): INBT {
        val compound = CompoundNBT()

        val wardedBlocks = instance.getWardedBlocks()
            .filter { instance.getWardStrength(it) != null }
            .associateWith { instance.getWardStrength(it)!! }
        val positions = LongArrayNBT(wardedBlocks.keys.map { it.asLong() })
        val strengths = ByteArrayNBT(wardedBlocks.values.map { it.toByte() }.toByteArray())

        compound.put(NBT_POSITIONS, positions)
        compound.put(NBT_STRENGTHS, strengths)

        return compound
    }

    override fun readNBT(
        capability: Capability<IWardedBlockMap>,
        instance: IWardedBlockMap,
        side: Direction?,
        nbt: INBT
    ) {
        if (nbt is CompoundNBT) {
            val positions = nbt.getLongArray(NBT_POSITIONS)
            val strengths = nbt.getByteArray(NBT_STRENGTHS)
            if (positions.size == strengths.size) {
                for (i in positions.indices) {
                    instance.wardBlock(BlockPos.of(positions[i]), strengths[i].toInt())
                }
            } else {
                LOG.error("Found an invalid warded block map storage. It had ${positions.size} positions and ${strengths.size} strengths.")
            }
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private const val NBT_POSITIONS = "positions"
        private const val NBT_STRENGTHS = "strengths"

        private val LOG = LogManager.getLogger()
    }
}