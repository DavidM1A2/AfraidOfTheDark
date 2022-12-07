package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.ProcResult
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.command.arguments.EntityAnchorArgument
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.LightningBoltEntity

class LightningSpellEffect : AOTDSpellEffect("lightning", ModResearches.INSANITY) {
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>): ProcResult {
        val position = state.position
        val lightningBolt = LightningBoltEntity(EntityType.LIGHTNING_BOLT, state.world)
        lightningBolt.setPos(position.x, position.y, position.z)
        lightningBolt.lookAt(EntityAnchorArgument.Type.FEET, state.direction)
        state.world.addFreshEntity(lightningBolt)
        createParticlesAt(
            state, ParticlePacket.builder()
                .particle(ModParticles.LIGHTNING)
                .position(state.position)
                .iterations(12)
                .build()
        )
        return ProcResult.success()
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 7.0
    }
}