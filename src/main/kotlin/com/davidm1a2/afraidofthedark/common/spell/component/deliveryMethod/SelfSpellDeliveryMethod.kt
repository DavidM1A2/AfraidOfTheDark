package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.particle.SelfParticleData
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import net.minecraftforge.fmllegacy.network.PacketDistributor

/**
 * Self delivery method delivers the spell to the caster
 *
 * @constructor does not initialize anything
 */
class SelfSpellDeliveryMethod : AOTDSpellDeliveryMethod("self", ModResearches.THE_JOURNEY_BEGINS) {
    /**
     * Called to begin delivering the effects to the target by whatever means necessary
     *
     * @param state The state of the spell to deliver
     */
    override fun executeDelivery(state: DeliveryTransitionState) {
        // Self just procs the effects and transitions at the target entity
        val entity = state.entity
        val position = state.position
        if (entity != null) {
            this.procEffects(state)
            this.transitionFrom(state)

            val numParticles = 10
            AfraidOfTheDark.packetHandler.sendToAllAround(
                ParticlePacket.builder()
                    .particles(List(numParticles) {
                        SelfParticleData(entity.id, it.toFloat() / numParticles * 360)
                    })
                    .position(entity.position().add(0.0, entity.bbHeight / 2.0, 0.0))
                    .build(),
                PacketDistributor.TargetPoint(position.x, position.y, position.z, 100.0, state.world.dimension())
            )
        } else {
            AfraidOfTheDark.packetHandler.sendToAllAround(
                ParticlePacket.builder()
                    .particle(ModParticles.SELF_FIZZLE)
                    .position(position)
                    .build(),
                PacketDistributor.TargetPoint(position.x, position.y, position.z, 100.0, state.world.dimension())
            )
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 0.1
    }

    /**
     * Gets the multiplier that this delivery method will apply to the stage it's in
     *
     * @return The spell stage multiplier for cost
     */
    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 1.0
    }
}