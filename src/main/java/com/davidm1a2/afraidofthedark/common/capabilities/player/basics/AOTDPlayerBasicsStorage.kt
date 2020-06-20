package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.nbt.INBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
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
        side: EnumFacing?
    ): INBTBase {
        // Create a compound to write
        val compound = NBTTagCompound()
        compound.setBoolean(STARTED_AOTD, instance.startedAOTD)
        compound.setInt(WRIST_CROSSBOW_BOLT_INDEX, instance.selectedWristCrossbowBoltIndex)
        compound.setString(WATCHED_METEOR, instance.getWatchedMeteor()?.registryName?.toString() ?: "none")
        compound.setInt(WATCHED_METEOR_ACCURACY, instance.getWatchedMeteorAccuracy())
        compound.setInt(WATCHED_METEOR_DROP_ANGLE, instance.getWatchedMeteorDropAngle())
        compound.setInt(WATCHED_METEOR_LATITUDE, instance.getWatchedMeteorLatitude())
        compound.setInt(WATCHED_METEOR_LONGITUDE, instance.getWatchedMeteorLongitude())
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
        side: EnumFacing?,
        nbt: INBTBase
    ) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is NBTTagCompound) {
            // The compound to read from
            instance.startedAOTD = nbt.getBoolean(STARTED_AOTD)
            instance.selectedWristCrossbowBoltIndex = nbt.getInt(WRIST_CROSSBOW_BOLT_INDEX)
            val watchedMeteorName = nbt.getString(WATCHED_METEOR)
            val watchedMeteor = if (watchedMeteorName == "none") null else ModRegistries.METEORS.getValue(
                ResourceLocation(watchedMeteorName)
            )
            val watchedMeteorAccuracy = nbt.getInt(WATCHED_METEOR_ACCURACY)
            val watchedMeteorDropAngle = nbt.getInt(WATCHED_METEOR_DROP_ANGLE)
            val watchedMeteorLatitude = nbt.getInt(WATCHED_METEOR_LATITUDE)
            val watchedMeteorLongitude = nbt.getInt(WATCHED_METEOR_LONGITUDE)
            instance.setWatchedMeteor(
                watchedMeteor,
                watchedMeteorAccuracy,
                watchedMeteorDropAngle,
                watchedMeteorLatitude,
                watchedMeteorLongitude
            )
        } else {
            logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // Constant IDs used in NBT
        private const val STARTED_AOTD = "playerStartedAOTD"
        private const val WRIST_CROSSBOW_BOLT_INDEX = "wristCrossbowBoltIndex"
        private const val WATCHED_METEOR = "watchedMeteor"
        private const val WATCHED_METEOR_ACCURACY = "watchedMeteorAccuracy"
        private const val WATCHED_METEOR_DROP_ANGLE = "watchedMeteorDropAngle"
        private const val WATCHED_METEOR_LATITUDE = "watchedMeteorLatitude"
        private const val WATCHED_METEOR_LONGITUDE = "watchedMeteorLongitude"
    }
}