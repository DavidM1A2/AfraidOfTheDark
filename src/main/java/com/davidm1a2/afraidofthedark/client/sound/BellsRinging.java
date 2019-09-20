package com.davidm1a2.afraidofthedark.client.sound;

import com.davidm1a2.afraidofthedark.common.constants.ModDimensions;
import com.davidm1a2.afraidofthedark.common.constants.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

/**
 * Class representing the bell ringing sound in the nightmare
 */
public class BellsRinging extends PlayerFollowingSound
{
    /**
     * Constructor sets the sound event to play and sound category
     */
    public BellsRinging()
    {
        super(ModSounds.BELLS, SoundCategory.AMBIENT);
        // This sound loops
        this.repeat = true;
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

    /**
     * Gets the repeat delay for the sound, this will be somewhat random
     *
     * @return The delay between sound plays
     */
    @Override
    public int getRepeatDelay()
    {
        // Wait a minute (60 seconds * 20 ticks / second)
        return 60 * 20;
    }
}
