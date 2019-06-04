package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.spell.component.effect.SpellEffectDig;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.SpellEffectExplosion;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.util.ResourceLocation;

/**
 * A static class containing all of our spell effect references for us
 */
public class ModSpellEffects
{
    public static final SpellEffectEntry DIG = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "dig"), SpellEffectDig::new);
    public static final SpellEffectEntry EXPLOSION = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "explosion"), SpellEffectExplosion::new);

    // An array containing a list of spell effects that AOTD adds
    public static final SpellEffectEntry[] SPELL_EFFECTS = new SpellEffectEntry[]
            {
                    DIG,
                    EXPLOSION
            };
}
