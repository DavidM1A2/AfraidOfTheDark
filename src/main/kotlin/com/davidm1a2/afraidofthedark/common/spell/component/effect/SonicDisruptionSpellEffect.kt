package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.registries.ForgeRegistries

/**
 * Effect that creates a sound
 */
class SonicDisruptionSpellEffect : AOTDSpellEffect("sonic_disruption", ModResearches.INSANITY) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.registryEntryProperty<SoundEvent>()
                .withBaseName(getUnlocalizedPropertyBaseName("sound"))
                .withSetter(this::setSound)
                .withGetter(this::getSound)
                .withDefaultValue(SoundEvents.CREEPER_PRIMED)
                .withRegistry(ForgeRegistries.SOUND_EVENTS)
                .withFilter(this::isAllowedSound)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.floatProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("volume"))
                .withSetter(this::setVolume)
                .withGetter(this::getVolume)
                .withDefaultValue(1f)
                .withMinValue(0f)
                .withMaxValue(1f)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.floatProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("pitch"))
                .withSetter(this::setPitch)
                .withGetter(this::getPitch)
                .withDefaultValue(1f)
                .withMinValue(0.5f)
                .withMaxValue(2f)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val world = state.world
        val volume = getVolume(instance)
        val pitch = getPitch(instance)
        world.playSound(null, state.position.x, state.position.y, state.position.z, getSound(instance), SoundCategory.PLAYERS, volume, pitch)
        createParticlesAt(
            state, ParticlePacket.builder()
                .position(state.position)
                // Particle scale is passed as the X coordinate of the "speed"
                .speed(Vector3d(volume.toDouble(), 0.0, 0.0))
                .particle(ModParticles.SONIC_DISRUPTION)
                .build()
        )
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        // 75 - 100 vitae
        return 75.0 + 25.0 * getVolume(instance)
    }

    private fun setSound(instance: SpellComponentInstance<*>, soundEvent: SoundEvent) {
        instance.data.putString(NBT_SOUND, soundEvent.registryName!!.toString())
    }

    private fun getSound(instance: SpellComponentInstance<*>): SoundEvent {
        return ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation(instance.data.getString(NBT_SOUND)))!!
    }

    private fun isAllowedSound(soundEvent: SoundEvent): Boolean {
        val name = soundEvent.registryName!!.path
        // No music.*, music_disc.*, or ui.* sounds
        return !name.startsWith("music.") && !name.startsWith("music_disc.") && !name.startsWith("ui.")
    }

    private fun setVolume(instance: SpellComponentInstance<*>, volume: Float) {
        instance.data.putFloat(NBT_VOLUME, volume)
    }

    private fun getVolume(instance: SpellComponentInstance<*>): Float {
        return instance.data.getFloat(NBT_VOLUME)
    }

    private fun setPitch(instance: SpellComponentInstance<*>, pitch: Float) {
        instance.data.putFloat(NBT_PITCH, pitch)
    }

    private fun getPitch(instance: SpellComponentInstance<*>): Float {
        return instance.data.getFloat(NBT_PITCH)
    }

    companion object {
        // NBT constants
        private const val NBT_SOUND = "sound"
        private const val NBT_VOLUME = "volume"
        private const val NBT_PITCH = "pitch"
    }
}