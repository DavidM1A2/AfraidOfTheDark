package com.DavidM1A2.afraidofthedark.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Utility class for making a sound follow the player
 */
@SideOnly(Side.CLIENT)
public abstract class PlayerFollowingSound extends MovingSound
{
    /**
     * Constructor sets the sound event to play
     *
     * @param soundEvent The sound event to play
     * @param soundCategory The sound category (effects which sound slider modifies this sound volume)
     */
    PlayerFollowingSound(SoundEvent soundEvent, SoundCategory soundCategory)
    {
        // The sound is ambient
        super(soundEvent, soundCategory);
        // The sound is always at max volume
        this.attenuationType = AttenuationType.NONE;
    }

    /**
     * The sound will follow the player's position
     */
    @Override
    public void update()
    {
        EntityPlayer entityPlayer = Minecraft.getMinecraft().player;
        this.xPosF = (float) entityPlayer.posX;
        this.yPosF = (float) entityPlayer.posY;
        this.zPosF = (float) entityPlayer.posZ;
    }
}
