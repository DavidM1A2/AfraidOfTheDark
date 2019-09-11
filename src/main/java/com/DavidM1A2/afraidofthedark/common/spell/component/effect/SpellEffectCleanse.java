package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellCharmData;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component.IAOTDPlayerSpellFreezeData;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
            // Extinguish the entity
            entity.extinguish();
            // Clear potion effects
            if (entity instanceof EntityLivingBase)
            {
                ((EntityLivingBase) entity).clearActivePotions();
            }
            // Unfreeze and uncharm the player
            if (entity instanceof EntityPlayer)
            {
                EntityPlayer entityPlayer = (EntityPlayer) entity;
                IAOTDPlayerSpellFreezeData freezeData = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null);
                freezeData.setFreezeTicks(0);
                freezeData.sync(entityPlayer);
                IAOTDPlayerSpellCharmData charmData = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_CHARM_DATA, null);
                charmData.setCharmTicks(0);
                freezeData.sync(entityPlayer);
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
