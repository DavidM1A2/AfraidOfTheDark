package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.potion.PotionSleeping;
import net.minecraft.potion.Potion;

/**
 * Class containing all potions in AOTD
 */
public class ModPotions
{
    public static final Potion SLEEPING_POTION = new PotionSleeping();

    public static final Potion[] POTION_LIST = new Potion[]
    {
        SLEEPING_POTION
    };
}
