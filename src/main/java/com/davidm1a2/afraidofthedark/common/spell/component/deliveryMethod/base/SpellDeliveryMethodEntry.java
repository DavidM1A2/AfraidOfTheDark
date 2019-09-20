package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base;

import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentEntry;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Entry used to store a reference to a delivery method
 */
public class SpellDeliveryMethodEntry extends SpellComponentEntry<SpellDeliveryMethodEntry, SpellDeliveryMethod>
{
    // A map of previous delivery entry type to transitioner to fire to move from that delivery method to this one
    private final Map<SpellDeliveryMethodEntry, ISpellDeliveryTransitioner> deliveryCustomTransitioners = new HashMap<>();
    // A map of effect entries to custom effect applicators, used to specify how effects are applied
    private final Map<SpellEffectEntry, ISpellDeliveryEffectApplicator> deliveryEffectCustomApplicators = new HashMap<>();

    /**
     * Constructor just passes on the id and factory
     *
     * @param id      The ID of this delivery method entry
     * @param factory The factory that makes new instances of this delivery method
     */
    public SpellDeliveryMethodEntry(ResourceLocation id, Supplier<SpellDeliveryMethod> factory)
    {
        super(id, new ResourceLocation(id.getResourceDomain(), "textures/gui/spell_component/delivery_methods/" + id.getResourcePath() + ".png"), factory);
    }

    /**
     * @return Gets the unlocalized name of the component
     */
    public String getUnlocalizedName()
    {
        return "delivery_method." + this.getRegistryName().toString();
    }

    /**
     * Adds a custom transitioner to this delivery method
     *
     * @param previous     The previous delivery method that causes this custom transitioner
     * @param transitioner The transitioner to be used if the previous delivery method fired
     */
    public void addCustomTransitioner(SpellDeliveryMethodEntry previous, ISpellDeliveryTransitioner transitioner)
    {
        this.deliveryCustomTransitioners.put(previous, transitioner);
    }

    /**
     * Adds a custom applicator to this effect for the current delivery method
     *
     * @param effectEntry         The effect to modify application of
     * @param applicator          The applicator to modify the application with
     */
    public void addCustomEffectApplicator(SpellEffectEntry effectEntry, ISpellDeliveryEffectApplicator applicator)
    {
        this.deliveryEffectCustomApplicators.put(effectEntry, applicator);
    }

    /**
     * Gets the transitioner to use for going to this delivery method from a given previous delivery method
     *
     * @param previous The previous delivery method type
     * @return The transitioner to use to go from the previous delivery method to this one
     */
    public ISpellDeliveryTransitioner getTransitioner(SpellDeliveryMethodEntry previous)
    {
        return this.deliveryCustomTransitioners.get(previous);
    }

    /**
     * Gets the applicator to use for this effect or the default if no custom one was specified
     *
     * @param effectEntry The effect entry to apply
     * @return The applicator to use to apply this effect
     */
    public ISpellDeliveryEffectApplicator getApplicator(SpellEffectEntry effectEntry)
    {
        return this.deliveryEffectCustomApplicators.get(effectEntry);
    }
}
