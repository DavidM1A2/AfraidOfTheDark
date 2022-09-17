package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.fml.network.PacketDistributor

/**
 * Creates a smoke screen at a given effect location
 *
 * @constructor adds the editable prop
 */
class SmokeScreenSpellEffect : AOTDSpellEffect("smoke_screen", ModResearches.POCKET_DIMENSION) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val position = state.position

        // Create smoke particle
        AfraidOfTheDark.packetHandler.sendToAllAround(
            ParticlePacket.builder()
                .particle(ModParticles.SMOKE_SCREEN)
                .position(position.add(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5))
                .speed(Vector3d.ZERO)
                .build(),
            PacketDistributor.TargetPoint(position.x, position.y, position.z, 128.0, state.world.dimension())
        )
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 0.2
    }
}