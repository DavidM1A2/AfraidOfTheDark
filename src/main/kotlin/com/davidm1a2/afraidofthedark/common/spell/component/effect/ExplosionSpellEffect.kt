package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.world.Explosion

/**
 * Effect that creates an explosion at the given position
 *
 * @constructor initializes the editable properties
 */
class ExplosionSpellEffect : AOTDSpellEffect("explosion", ModResearches.MAGIC_MASTERY) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.floatProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("radius"))
                .withSetter(this::setRadius)
                .withGetter(this::getRadius)
                .withDefaultValue(2.0f)
                .withMinValue(1.0f)
                .withMaxValue(40.0f)
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
        val world = state.world
        val position = state.position
        createParticlesAt(5, 10, position, world.dimension(), ModParticles.EXPLOSION)
        world.explode(null, position.x, position.y - 0.01f, position.z, getRadius(instance), Explosion.Mode.BREAK)
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        val radius = getRadius(instance)
        // Base cost to make an explosion
        val baseCost = 10.0
        // Cubic cost based on explosion radius
        val radiusCost = 1.0 * radius * radius * radius
        return baseCost + radiusCost
    }

    fun setRadius(instance: SpellComponentInstance<*>, radius: Float) {
        instance.data.putFloat(NBT_RADIUS, radius)
    }

    fun getRadius(instance: SpellComponentInstance<*>): Float {
        return instance.data.getFloat(NBT_RADIUS)
    }

    companion object {
        // NBT constants
        private const val NBT_RADIUS = "radius"
    }
}