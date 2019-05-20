package com.DavidM1A2.afraidofthedark.common.potion;

import com.DavidM1A2.afraidofthedark.common.potion.base.AOTDPotion;

import java.awt.*;

/**
 * Sleeping potion makes players 'drowsy'
 */
public class PotionSleeping extends AOTDPotion
{
    /**
     * Constructor just sets item properties
     */
    public PotionSleeping()
    {
        super("Drowsiness", "sleeping_potion", 0, 0, false, new Color(255, 255, 255));
    }
}
