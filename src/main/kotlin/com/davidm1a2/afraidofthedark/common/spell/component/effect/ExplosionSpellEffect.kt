package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.Explosion
import kotlin.math.floor

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
                .withMaxValue(MAX_RADIUS)
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
        val numParticles = floor(getRadius(instance) * getRadius(instance)).toInt().coerceAtLeast(10)
        createParticlesAt(
            state, ParticlePacket.builder()
                .particle(ModParticles.EXPLOSION)
                .position(position)
                // Particle's 'speed' isn't actually speed. It's the explosion's radius. We'll re-use the speed's x coordinate though since it's unused
                .speed(Vector3d(getRadius(instance).toDouble(), 0.0, 0.0))
                .iterations(numParticles)
                .build()
        )
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

        const val MAX_RADIUS = 40.0f
    }
}