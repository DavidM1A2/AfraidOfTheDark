package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.spell.component.effect.*;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.util.ResourceLocation;

/**
 * A static class containing all of our spell effect references for us
 */
public class ModSpellEffects
{
    public static final SpellEffectEntry DIG = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "dig"), SpellEffectDig::new);
    public static final SpellEffectEntry EXPLOSION = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "explosion"), SpellEffectExplosion::new);
    public static final SpellEffectEntry TELEPORT = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "teleport"), SpellEffectTeleport::new);
    public static final SpellEffectEntry GROW = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "grow"), SpellEffectGrow::new);
    public static final SpellEffectEntry HEAL = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "heal"), SpellEffectHeal::new);
    public static final SpellEffectEntry FEED = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "feed"), SpellEffectFeed::new);
    public static final SpellEffectEntry BURN = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "burn"), SpellEffectBurn::new);
    public static final SpellEffectEntry POTION_EFFECT = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "potion_effect"), SpellEffectPotionEffect::new);
    public static final SpellEffectEntry SMOKE_SCREEN = new SpellEffectEntry(new ResourceLocation(Constants.MOD_ID, "smoke_screen"), SpellEffectSmokeScreen::new);

    // An array containing a list of spell effects that AOTD adds
    public static final SpellEffectEntry[] SPELL_EFFECTS = new SpellEffectEntry[]
            {
                    DIG,
                    EXPLOSION,
                    TELEPORT,
                    GROW,
                    HEAL,
                    FEED,
                    BURN,
                    POTION_EFFECT,
                    SMOKE_SCREEN
            };
}
