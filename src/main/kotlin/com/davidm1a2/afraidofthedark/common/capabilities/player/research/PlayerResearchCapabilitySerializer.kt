package com.davidm1a2.afraidofthedark.common.capabilities.player.research

import com.davidm1a2.afraidofthedark.common.capabilities.INullableCapabilitySerializable
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import org.apache.logging.log4j.LogManager
import java.time.Instant
import java.time.ZonedDateTime

class PlayerResearchCapabilitySerializer : INullableCapabilitySerializable<CompoundTag> {
    private val instance: IPlayerResearch = PlayerResearch()

    override fun <V> getCapability(capability: Capability<V>?, side: Direction?): LazyOptional<V> {
        return if (capability == ModCapabilities.PLAYER_RESEARCH) LazyOptional.of { instance }.cast() else LazyOptional.empty()
    }

    override fun serializeNBT(): CompoundTag {
        // Create a compound to write
        val compound = CompoundTag()

        // Write each researches name as a key with true/false as the value
        for (research in ModRegistries.RESEARCH) {
            val researchTime = instance.getResearchTime(research)
            researchTime?.let { compound.putLong(research.registryName.toString(), it.toInstant().toEpochMilli()) }
        }

        return compound
    }

    override fun deserializeNBT(nbt: CompoundTag?) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt != null) {
            // For each research if we have researched it unlock that research in our instance
            for (research in ModRegistries.RESEARCH) {
                val unlockTime = if (nbt.contains(research.registryName.toString())) {
                    ZonedDateTime.ofInstant(Instant.ofEpochMilli(nbt.getLong(research.registryName.toString())), Constants.DEFAULT_TIME_ZONE)
                } else {
                    null
                }
                instance.setResearch(research, unlockTime)
            }
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was null!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}