package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.ProcResult
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.fml.network.PacketDistributor

/**
 * Entry used to store a reference to a delivery method
 *
 * @constructor just passes on the id and factory
 * @param id The ID of this delivery method entry
 * @param prerequisiteResearch The research required to use this component, or null if none is requiredry entry type to transitioner to fire to move from that delivery method to this one
 */
abstract class SpellDeliveryMethod(id: ResourceLocation, prerequisiteResearch: Research?) : SpellComponent<SpellDeliveryMethod>(
    id,
    ResourceLocation(id.namespace, "textures/gui/spell_component/delivery_methods/${id.path}.png"),
    prerequisiteResearch
) {
    /**
     * Called to begin delivering the effects to the target by whatever means necessary
     *
     * @param state The state of the spell at the current delivery method
     */
    abstract fun executeDelivery(state: DeliveryTransitionState)

    /**
     * Applies the effects that are a part of this delivery method to the targets
     *
     * @param state The state of the spell at the current delivery method
     * @param fizzleOnFailure Create fizzle particles when the proc fails
     * @return A successful result if at least one effect procs successfully, or there are no effects
     */
    fun procEffects(state: DeliveryTransitionState, fizzleOnFailure: Boolean = true): ProcResult {
        var hasOneEffect = false
        var oneEffectProcdSuccessfully = false
        for (effect in state.getCurrentStage().effects) {
            if (effect != null) {
                hasOneEffect = true
                val result = effect.component.procEffect(state, effect)
                if (result.isSuccess) {
                    oneEffectProcdSuccessfully = true
                }
            }
        }

        val isSuccess = !hasOneEffect || oneEffectProcdSuccessfully
        if (fizzleOnFailure && !isSuccess) {
            AfraidOfTheDark.packetHandler.sendToAllAround(
                ParticlePacket.builder()
                    .particle(ModParticles.FIZZLE)
                    .position(state.position)
                    .speed(Vector3d(0.0, 0.1, 0.0))
                    .build(),
                PacketDistributor.TargetPoint(state.position.x, state.position.y, state.position.z, 100.0, state.world.dimension())
            )
        }

        return ProcResult(isSuccess)
    }

    /**
     * Called after the delivery finishes and we transition from this state into the next
     *
     * @param state The state of the spell to transition FROM
     */
    fun transitionFrom(state: DeliveryTransitionState) {
        val spell = state.spell
        val nextStageIndex = state.stageIndex + 1
        // Test if we can transition to the next stage
        if (spell.hasStage(nextStageIndex)) {
            // Perform the transition between the next delivery method and the current delivery method
            spell.getStage(nextStageIndex)!!
                .deliveryInstance!!
                .component
                .executeDelivery(state.copy(stageIndex = nextStageIndex, deliveryEntity = null))
        }
    }

    /**
     * Gets the cost of specifically delivering the effects to the target
     *
     * @return The cost of delivering the effects
     */
    abstract fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double

    /**
     * Gets the number of times this spell will proc effects and the next delivery method
     *
     * @return The delivery method effect & delivery multiplicity
     */
    abstract fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double

    /**
     * @return Gets the unlocalized name of the component
     */
    override fun getUnlocalizedBaseName(): String {
        return "delivery_method.${registryName!!.namespace}.${registryName!!.path}"
    }
}