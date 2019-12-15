package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d

/**
 * The cleanse spell effect clears your spell effects
 */
class SpellEffectCleanse : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "cleanse"))
{
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>)
    {
        val entity = state.getEntity();
        if (entity != null)
        {
            // Extinguish the entity
            entity.extinguish()

            // Clear potion effects
            if (entity is EntityLivingBase)
            {
                entity.clearActivePotions()
            }

            // Unfreeze and uncharm the player
            if (entity is EntityPlayer)
            {
                val freezeData = entity.getCapability(ModCapabilities.PLAYER_SPELL_FREEZE_DATA, null)
                freezeData!!.freezeTicks = 0
                freezeData.sync(entity)
                val charmData = entity.getCapability(ModCapabilities.PLAYER_SPELL_CHARM_DATA, null)
                charmData!!.charmTicks = 0
                freezeData.sync(entity)
            }

            createParticlesAt(1, 2, Vec3d(entity.posX, entity.posY, entity.posZ), entity.dimension)
        }
    }

    /**
     * Gets the cost of the effect
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the effect
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double
    {
        return 20.0
    }
}