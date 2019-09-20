package com.davidm1a2.afraidofthedark.common.spell.component.effect.base;

import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentEntry;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

/**
 * Entry used to store a reference to an effect
 */
public class SpellEffectEntry extends SpellComponentEntry<SpellEffectEntry, SpellEffect>
{
    /**
     * Constructor just passes on the id and factory
     *
     * @param id The ID of this effect entry
     * @param factory The factory that makes new instances of this effect
     */
    public SpellEffectEntry(ResourceLocation id, Supplier<SpellEffect> factory)
    {
        super(id, new ResourceLocation(id.getResourceDomain(), "textures/gui/spell_component/effects/" + id.getResourcePath() + ".png"), factory);
    }

    /**
     * @return Gets the unlocalized name of the component
     */
    public String getUnlocalizedName()
    {
        return "effect." + this.getRegistryName().toString();
    }
}
