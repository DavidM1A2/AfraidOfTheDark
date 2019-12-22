package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

/**
 * Effect that creates an explosion at the given position
 *
 * @constructor initializes the editable properties
 */
class SpellEffectExplosion : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "explosion"))
{
    init
    {
        addEditableProperty(
            SpellComponentPropertyFactory.floatProperty()
                .withName("Radius")
                .withDescription("The explosion's radius.")
                .withSetter { instance, newValue -> instance.data.setFloat(NBT_RADIUS, newValue) }
                .withGetter { it.data.getFloat(NBT_RADIUS) }
                .withDefaultValue(5.0f)
                .withMinValue(1.0f)
                .withMaxValue(150.0f)
                .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>)
    {
        val world: World = state.world
        val position = state.position
        createParticlesAt(1, 3, position, world.provider.dimension)
        world.createExplosion(null, position.x, position.y, position.z, getRadius(instance), true)
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double
    {
        val radius = getRadius(instance)
        return 10.0 + radius * radius
    }

    /**
     * Gets the radius the explosion should have
     *
     * @param instance The instance of the spell effect
     * @return The radius of the explosion
     */
    fun getRadius(instance: SpellComponentInstance<SpellEffect>): Float
    {
        return instance.data.getFloat(NBT_RADIUS)
    }

    companion object
    {
        // NBT constants
        private const val NBT_RADIUS = "radius"
    }
}