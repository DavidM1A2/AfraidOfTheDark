package com.davidm1a2.afraidofthedark.common.capabilities.player.research

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import org.apache.logging.log4j.LogManager
import java.time.Instant
import java.time.ZonedDateTime

/**
 * Default storage implementation for AOTD player research
 */
class PlayerResearchStorage : IStorage<IPlayerResearch> {
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(
        capability: Capability<IPlayerResearch>,
        instance: IPlayerResearch,
        side: Direction?
    ): INBT {
        // Create a compound to write
        val compound = CompoundNBT()

        // Write each researches name as a key with true/false as the value
        for (research in ModRegistries.RESEARCH) {
            val researchTime = instance.getResearchTime(research)
            researchTime?.let { compound.putLong(research.registryName.toString(), it.toInstant().toEpochMilli()) }
        }

        return compound
    }

    /**
     * Called to read the NBTTagCompound into a capability
     *
     * @param capability The capability that is being read
     * @param instance   The instance to of the capability to read
     * @param side       ignored
     * @param nbt        An NBTTagCompound that contains all info about the capability
     */
    override fun readNBT(
        capability: Capability<IPlayerResearch>,
        instance: IPlayerResearch,
        side: Direction?,
        nbt: INBT
    ) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is CompoundNBT) {
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
            logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}