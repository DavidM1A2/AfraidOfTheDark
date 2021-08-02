package com.davidm1a2.afraidofthedark.client.sound

import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound.AttenuationType
import net.minecraft.client.audio.TickableSound
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Utility class for making a sound follow the player
 *
 * @constructor sets the sound event to play
 * @param soundEvent The sound event to play
 * @param soundCategory The sound category (effects which sound slider modifies this sound volume)
 */
@OnlyIn(Dist.CLIENT)
abstract class PlayerFollowingSound internal constructor(soundEvent: SoundEvent, soundCategory: SoundCategory) : TickableSound(soundEvent, soundCategory) {
    init {
        // The sound is ambient
        attenuation = AttenuationType.NONE
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