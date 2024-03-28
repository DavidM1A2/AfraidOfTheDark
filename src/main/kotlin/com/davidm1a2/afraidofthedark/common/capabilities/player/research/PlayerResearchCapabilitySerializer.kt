package com.davidm1a2.afraidofthedark.common.capabilities.player.research

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.nbt.CompoundTag
import java.time.Instant
import java.time.ZonedDateTime

class PlayerResearchCapabilitySerializer(instance: IPlayerResearch = PlayerResearch()) : AOTDCapabilitySerializer<IPlayerResearch, CompoundTag>(instance) {
    override fun getCapability() = ModCapabilities.PLAYER_RESEARCH

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

    override fun deserializeNBTSafe(nbt: CompoundTag) {
        // For each research if we have researched it unlock that research in our instance
        for (research in ModRegistries.RESEARCH) {
            val unlockTime = if (nbt.contains(research.registryName.toString())) {
                ZonedDateTime.ofInstant(Instant.ofEpochMilli(nbt.getLong(research.registryName.toString())), Constants.DEFAULT_TIME_ZONE)
            } else {
                null
            }
            instance.setResearch(research, unlockTime)
        }
    }
}