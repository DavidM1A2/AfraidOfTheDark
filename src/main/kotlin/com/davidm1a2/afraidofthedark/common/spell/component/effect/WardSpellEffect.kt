package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.ResourceLocation

class WardSpellEffect : AOTDDurationSpellEffect(ResourceLocation(Constants.MOD_ID, "ward"), null, 0, 0) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("strength"))
                .withSetter(this::setStrength)
                .withGetter(this::getStrength)
                .withMinValue(1)
                .withMaxValue(10)
                .withDefaultValue(1)
                .build()
        )
    }

    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val entityHit = state.entity
        if (entityHit is LivingEntity) {
            createParticlesAt(2, 6, state.position, state.world.dimension(), ModParticles.SPELL_HIT)
            entityHit.addEffect(EffectInstance(Effects.DAMAGE_RESISTANCE, getDuration(instance), getStrength(instance) - 1))
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return getStrength(instance) * 7.0 + getDuration(instance) * 0.1
    }

    fun setStrength(instance: SpellComponentInstance<*>, amount: Int) {
        instance.data.putInt(NBT_STRENGTH, amount)
    }

    fun getStrength(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_STRENGTH)
    }

    companion object {
        // NBT constants for strength
        private const val NBT_STRENGTH = "strength"
    }
}