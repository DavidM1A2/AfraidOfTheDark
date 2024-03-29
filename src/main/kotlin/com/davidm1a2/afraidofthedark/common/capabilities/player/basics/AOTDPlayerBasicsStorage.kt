package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import org.apache.logging.log4j.LogManager

/**
 * Default storage implementation for AOTD player basics
 */
class AOTDPlayerBasicsStorage : IStorage<IAOTDPlayerBasics> {
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(
        capability: Capability<IAOTDPlayerBasics>,
        instance: IAOTDPlayerBasics,
        side: Direction?
    ): INBT {
        // Create a compound to write
        val compound = CompoundNBT()

        val watchedMeteor = instance.watchedMeteor
        if (watchedMeteor != null) {
            compound.putString(WATCHED_METEOR, watchedMeteor.meteor.registryName.toString())
            compound.putInt(WATCHED_METEOR_ACCURACY, watchedMeteor.accuracy)
            compound.putInt(WATCHED_METEOR_DROP_ANGLE, watchedMeteor.dropAngle)
            compound.putInt(WATCHED_METEOR_LATITUDE, watchedMeteor.latitude)
            compound.putInt(WATCHED_METEOR_LONGITUDE, watchedMeteor.longitude)
        }

        compound.putString(SELECTED_SPELL_POWER_SOURCE, instance.selectedPowerSource.registryName.toString())

        val multiplicities = CompoundNBT()
        instance.listMultiplicities().forEach {
            multiplicities.putInt(it.toString(), instance.getMultiplicity(it))
        }
        compound.put(MULTIPLICITIES, multiplicities)

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
        capability: Capability<IAOTDPlayerBasics>,
        instance: IAOTDPlayerBasics,
        side: Direction?,
        nbt: INBT
    ) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is CompoundNBT) {
            // The compound to read from
            if (nbt.contains(WATCHED_METEOR)) {
                instance.watchedMeteor = WatchedMeteor(
                    ModRegistries.METEORS.getValue(ResourceLocation(nbt.getString(WATCHED_METEOR)))!!,
                    nbt.getInt(WATCHED_METEOR_ACCURACY),
                    nbt.getInt(WATCHED_METEOR_DROP_ANGLE),
                    nbt.getInt(WATCHED_METEOR_LATITUDE),
                    nbt.getInt(WATCHED_METEOR_LONGITUDE)
                )
            } else {
                instance.watchedMeteor = null
            }

            if (nbt.contains(SELECTED_SPELL_POWER_SOURCE)) {
                instance.selectedPowerSource = ModRegistries.SPELL_POWER_SOURCES.getValue(ResourceLocation(nbt.getString(SELECTED_SPELL_POWER_SOURCE)))!!
            }

            val multiplicities = nbt.getCompound(MULTIPLICITIES)
            multiplicities.allKeys.forEach {
                instance.setMultiplicity(ResourceLocation(it), multiplicities.getInt(it))
            }
        } else {
            logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // Constant IDs used in NBT
        private const val WATCHED_METEOR = "watchedMeteor"
        private const val WATCHED_METEOR_ACCURACY = "watchedMeteorAccuracy"
        private const val WATCHED_METEOR_DROP_ANGLE = "watchedMeteorDropAngle"
        private const val WATCHED_METEOR_LATITUDE = "watchedMeteorLatitude"
        private const val WATCHED_METEOR_LONGITUDE = "watchedMeteorLongitude"
        private const val SELECTED_SPELL_POWER_SOURCE = "selectedSpellPowerSource"
        private const val MULTIPLICITIES = "multiplicities"
    }
}