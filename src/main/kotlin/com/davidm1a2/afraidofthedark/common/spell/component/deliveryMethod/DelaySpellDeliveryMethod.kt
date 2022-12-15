package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellStates
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.fml.network.PacketDistributor
import java.time.Duration

/**
 * Self delivery method delivers the spell after a delay
 *
 * @constructor initializes the editable properties
 */
class DelaySpellDeliveryMethod : AOTDSpellDeliveryMethod("delay", ModResearches.APPRENTICE_ASCENDED) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("delay"))
                .withSetter(this::setDelay)
                .withGetter(this::getDelay)
                .withDefaultValue(1.0)
                .withMinValue(0.0)
                .withMaxValue(Duration.ofMinutes(20L).seconds.toDouble())
                .build()
        )
    }

    /**
     * Called to deliver the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState) {
        // Delayed adds this spell to the queue to wait
        val world = state.world
        world.getSpellStates().addDelayedDelivery(state)
        val position = state.position
        AfraidOfTheDark.packetHandler.sendToAllAround(
            ParticlePacket.builder()
                .particle(ModParticles.DELAY)
                .position(position)
                // Speed's x coordinate is actually the scale of the particle
                .speed(Vector3d(state.entity?.bbWidth?.times(1.5) ?: 1.2, 1.0, 1.0))
                .build(),
            PacketDistributor.TargetPoint(position.x, position.y, position.z, 100.0, world.dimension())
        )
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The spell delivery method instance
     * @return The cost of the delivery method
     */
    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        // Each second of delay costs 0.25 vitae, and a 1 vitae base cost
        return 1.0 + getDelay(instance) * 0.25
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @param instance The spell delivery method instance
     * @return The spell stage multiplier for cost
     */
    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.0
    }

    fun setDelay(instance: SpellComponentInstance<*>, delay: Double) {
        instance.data.putDouble(NBT_DELAY, delay)
    }

    fun getDelay(instance: SpellComponentInstance<*>): Double {
        return instance.data.getDouble(NBT_DELAY)
    }

    companion object {
        // The NBT keys
        private const val NBT_DELAY = "delay"
    }
}