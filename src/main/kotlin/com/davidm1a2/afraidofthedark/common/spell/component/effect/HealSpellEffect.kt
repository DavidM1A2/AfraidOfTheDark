package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.item.ArmorStandEntity
import net.minecraft.util.ResourceLocation

/**
 * Effect that heals a hit entity
 *
 * @constructor adds the editable prop
 */
class HealSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "heal")) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withName("Heal Amount")
                .withDescription("The amount of half hearts to restore.")
                .withSetter { instance, newValue -> instance.data.putInt(NBT_HEALING_AMOUNT, newValue) }
                .withGetter { it.data.getInt(NBT_HEALING_AMOUNT) }
                .withDefaultValue(2)
                .withMinValue(1)
                .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val entity = state.getEntity()
        if (entity is LivingEntity && entity !is ArmorStandEntity) {
            val healAmount = getHealAmount(instance)
            createParticlesAround(healAmount, 2 * healAmount, state.position, entity.level.dimension(), ModParticles.HEAL, 1.0)
            entity.heal(healAmount.toFloat())
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return getHealAmount(instance) * 5.0
    }

    fun setHealAmount(instance: SpellComponentInstance<SpellEffect>, amount: Int) {
        instance.data.putInt(NBT_HEALING_AMOUNT, amount)
    }

    /**
     * Gets the number of half hearts this spell effect heals
     *
     * @param instance The instance of this effect
     * @return The number of half hearts to heal
     */
    fun getHealAmount(instance: SpellComponentInstance<SpellEffect>): Int {
        return instance.data.getInt(NBT_HEALING_AMOUNT)
    }

    companion object {
        // NBT constants for healing amount
        private const val NBT_HEALING_AMOUNT = "healing_amount"
    }
}