package com.davidm1a2.afraidofthedark.client.sound;

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions;
import com.davidm1a2.afraidofthedark.common.constants.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

/**
 * Class representing the erie echo that plays when entering the nightamre realm
 */
public class ErieEcho extends PlayerFollowingSound
{
    /**
     * Constructor sets the sound event to play and category
     */
    public ErieEcho()
    {
        super(ModSounds.ERIE_ECHOS, SoundCategory.AMBIENT);
    }

    /**
     * Ensure that this only players in the nightmare when the player is not dead
     */
    @Override
    public void update()
    {
        super.update();
        EntityPlayer entityPlayer = Minecraft.getMinecraft().player;
        if (entityPlayer.isDead || entityPlayer.dimension != ModDimensions.NIGHTMARE.getId())
        {
            this.donePlaying = true;
        }
    }
}
