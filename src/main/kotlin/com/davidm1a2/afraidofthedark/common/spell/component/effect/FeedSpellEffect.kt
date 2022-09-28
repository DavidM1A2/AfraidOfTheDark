package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.particle.FeedParticleData
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.player.PlayerEntity
import kotlin.math.PI
import kotlin.random.Random

/**
 * Effect that feeds a hit player
 *
 * @constructor adds the editable prop
 */
class FeedSpellEffect : AOTDSpellEffect("feed", ModResearches.APPRENTICE_ASCENDED) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("hunger_amount"))
                .withSetter(this::setHungerAmount)
                .withGetter(this::getHungerAmount)
                .withDefaultValue(2)
                .withMinValue(1)
                .withMaxValue(20)
                .build()
        )
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("saturation_amount"))
                .withSetter(this::setSaturationAmount)
                .withGetter(this::getSaturationAmount)
                .withDefaultValue(1)
                .withMinValue(0)
                .withMaxValue(20)
                .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val entity = state.entity
        if (entity is PlayerEntity) {
            val foodStats = entity.foodData
            foodStats.eat(getHungerAmount(instance), getSaturationAmount(instance).toFloat())
            val particleOffset = Random.nextFloat() * PI.toFloat() * 2
            createParticlesAt(
                state, ParticlePacket.builder()
                    .particles(List(3) {
                        FeedParticleData(
                            entity.id,
                            PI.toFloat() * 2 * it / 3 + particleOffset,
                            entity.bbWidth / 2
                        )
                    })
                    .position(entity.position().add(0.0, entity.bbHeight / 2.0, 0.0))
                    .build()
            )
        } else {
            createFizzleParticleAt(state)
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        // Each half-drumstick is 0.5 vitae
        val hungerCost = getHungerAmount(instance) * 0.5
        // Each saturation half-drumstick is 2 vitae
        val saturationCost = getSaturationAmount(instance) * 2.0
        return hungerCost + saturationCost
    }

    fun setHungerAmount(instance: SpellComponentInstance<*>, hungerAmount: Int) {
        instance.data.putInt(NBT_HUNGER_AMOUNT, hungerAmount)
    }

    fun getHungerAmount(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_HUNGER_AMOUNT)
    }

    fun setSaturationAmount(instance: SpellComponentInstance<*>, saturationAmount: Int) {
        instance.data.putInt(NBT_SATURATION_AMOUNT, saturationAmount)
    }

    fun getSaturationAmount(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_SATURATION_AMOUNT)
    }

    companion object {
        // NBT constants
        private const val NBT_HUNGER_AMOUNT = "hunger_amount"
        private const val NBT_SATURATION_AMOUNT = "saturation_amount"
    }
}