package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d

/**
 * Effect that creates an ender chest
 */
class SpellEffectEnderPocket : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "ender_pocket")) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        // If we hit a player open the ender chest GUI
        val entity = state.getEntity()
        if (entity is EntityPlayer) {
            createParticlesAt(3, 5, Vec3d(entity.posX, entity.posY, entity.posZ), entity.dimension)
            val enderChest = entity.inventoryEnderChest
            entity.displayGUIChest(enderChest)
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 45.0
    }
}