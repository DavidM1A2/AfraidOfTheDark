package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.block.BlockAir
import net.minecraft.init.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

/**
 * Effect that sets fire to the hit target
 */
class SpellEffectBurn : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "burn"))
{
    /**
     * Constructor adds the editable prop
     */
    init
    {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withName("Burn")
                .withDescription("The number of seconds to set fire to when hitting entities.")
                .withSetter { instance, newValue -> instance.data.setInteger(NBT_BURN_DURATION, newValue) }
                .withGetter { it.data.getInteger(NBT_BURN_DURATION) }
                .withDefaultValue(2)
                .withMinValue(1)
                .withMaxValue(60)
                .build()
        )
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>)
    {
        if (state.getEntity() != null)
        {
            val entity = state.getEntity()
            createParticlesAt(3, 5, Vec3d(entity!!.posX, entity.posY, entity.posZ), entity.dimension)
            entity.setFire(getBurnDuration(instance))
        }
        else
        {
            val world: World = state.world
            val position = state.blockPosition
            if (world.getBlockState(position.up()).block is BlockAir)
            {
                if (world.getBlockState(position).block !is BlockAir)
                {
                    createParticlesAt(1, 3, state.position, world.provider.dimension)
                    world.setBlockState(position.up(), Blocks.FIRE.defaultState)
                }
            }
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double
    {
        return 10.0 + getBurnDuration(instance) * 5.0
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    fun getBurnDuration(instance: SpellComponentInstance<SpellEffect>): Int
    {
        return instance.data.getInteger(NBT_BURN_DURATION)
    }

    companion object
    {
        // NBT constants for burn duration
        private const val NBT_BURN_DURATION = "burn_duration"
    }
}