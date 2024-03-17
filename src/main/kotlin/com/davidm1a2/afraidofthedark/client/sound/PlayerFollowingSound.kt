package com.davidm1a2.afraidofthedark.client.sound

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource

/**
 * Utility class for making a sound follow the player
 *
 * @constructor sets the sound event to play
 * @param soundEvent The sound event to play
 * @param soundCategory The sound category (effects which sound slider modifies this sound volume)
 */
abstract class PlayerFollowingSound internal constructor(soundEvent: SoundEvent, soundCategory: SoundSource) : AbstractTickableSoundInstance(soundEvent, soundCategory) {
    init {
        // The sound is ambient
        attenuation = SoundInstance.Attenuation.NONE
    }

    override fun canStartSilent(): Boolean {
        return true
    }

    /**
     * The sound will follow the player's position
     */
    override fun tick() {
        val entityPlayer = Minecraft.getInstance().player!!
        x = entityPlayer.x
        y = entityPlayer.y
        z = entityPlayer.z
    }
}