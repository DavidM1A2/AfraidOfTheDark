package com.davidm1a2.afraidofthedark.common.capabilities.player.basics

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation

class AOTDPlayerBasicsCapabilitySerializer(instance: IAOTDPlayerBasics = AOTDPlayerBasics()) : AOTDCapabilitySerializer<IAOTDPlayerBasics, CompoundTag>(instance) {
    override fun getCapability() = ModCapabilities.PLAYER_BASICS

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

    override fun deserializeNBTSafe(nbt: CompoundTag) {
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
    }

    companion object {
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