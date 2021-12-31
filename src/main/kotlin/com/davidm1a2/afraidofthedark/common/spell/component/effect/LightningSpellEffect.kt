package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.command.arguments.EntityAnchorArgument
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.LightningBoltEntity
import net.minecraft.util.ResourceLocation

class LightningSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "lightning"), null) {
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val position = state.position
        createParticlesAt(2, 6, position, state.world.dimension(), ModParticles.SPELL_HIT)
        val lightningBolt = LightningBoltEntity(EntityType.LIGHTNING_BOLT, state.world)
        lightningBolt.setPos(position.x, position.y, position.z)
        lightningBolt.lookAt(EntityAnchorArgument.Type.FEET, state.direction)
        state.world.addFreshEntity(lightningBolt)
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 5.0
    }
}