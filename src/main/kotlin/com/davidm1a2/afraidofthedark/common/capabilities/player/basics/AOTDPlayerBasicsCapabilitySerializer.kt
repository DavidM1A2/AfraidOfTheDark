package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import com.davidm1a2.afraidofthedark.common.capabilities.INullableCapabilitySerializable
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import org.apache.logging.log4j.LogManager

class AOTDPlayerBasicsCapabilitySerializer : INullableCapabilitySerializable<CompoundTag> {
    private val instance: IAOTDPlayerBasics = AOTDPlayerBasics()

    override fun <V> getCapability(capability: Capability<V>?, side: Direction?): LazyOptional<V> {
        return if (capability == ModCapabilities.PLAYER_BASICS) LazyOptional.of { instance }.cast() else LazyOptional.empty()
    }

    override fun serializeNBT(): CompoundTag {
        // Create a compound to write
        val compound = CompoundTag()

        val watchedMeteor = instance.watchedMeteor
        if (watchedMeteor != null) {
            compound.putString(WATCHED_METEOR, watchedMeteor.meteor.registryName.toString())
            compound.putInt(WATCHED_METEOR_ACCURACY, watchedMeteor.accuracy)
            compound.putInt(WATCHED_METEOR_DROP_ANGLE, watchedMeteor.dropAngle)
            compound.putInt(WATCHED_METEOR_LATITUDE, watchedMeteor.latitude)
            compound.putInt(WATCHED_METEOR_LONGITUDE, watchedMeteor.longitude)
        }

        compound.putString(SELECTED_SPELL_POWER_SOURCE, instance.selectedPowerSource.registryName.toString())

        val multiplicities = CompoundTag()
        instance.listMultiplicities().forEach {
            multiplicities.putInt(it.toString(), instance.getMultiplicity(it))
        }
        compound.put(MULTIPLICITIES, multiplicities)

        return compound
    }

    override fun deserializeNBT(nbt: CompoundTag?) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt != null) {
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
            LOG.error("Attempted to deserialize an NBTBase that was null!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()

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