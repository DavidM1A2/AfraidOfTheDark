package com.davidm1a2.afraidofthedark.client.sound

import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound.AttenuationType
import net.minecraft.client.audio.MovingSound
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Utility class for making a sound follow the player
 *
 * @constructor sets the sound event to play
 * @param soundEvent The sound event to play
 * @param soundCategory The sound category (effects which sound slider modifies this sound volume)
 */
@SideOnly(Side.CLIENT)
abstract class PlayerFollowingSound internal constructor(soundEvent: SoundEvent, soundCategory: SoundCategory) : MovingSound(soundEvent, soundCategory)
{
    init
    {
        // The sound is ambient
        attenuationType = AttenuationType.NONE
    }

    /**
     * The sound will follow the player's position
     */
    override fun update()
    {
        val entityPlayer: EntityPlayer = Minecraft.getMinecraft().player
        xPosF = entityPlayer.posX.toFloat()
        yPosF = entityPlayer.posY.toFloat()
        zPosF = entityPlayer.posZ.toFloat()
    }
}