package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

/**
 * The cleanse spell effect clears your spell effects
 */
public class SpellEffectCleanse extends AOTDSpellEffect
{
    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return 20;
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        if (state.getEntity() != null)
        {
            Entity entity = state.getEntity();
            entity.extinguish();
            if (entity instanceof EntityLivingBase)
            {
                ((EntityLivingBase) entity).clearActivePotions();
            }
            createParticlesAt(1, 2, new Vec3d(entity.posX, entity.posY, entity.posZ), entity.dimension);
        }
    }

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public SpellEffectEntry getEntryRegistryType()
    {
        return ModSpellEffects.CLEANSE;
    }
}
