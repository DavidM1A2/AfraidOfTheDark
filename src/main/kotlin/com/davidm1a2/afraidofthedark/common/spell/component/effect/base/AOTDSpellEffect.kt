package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.fmllegacy.network.PacketDistributor

abstract class AOTDSpellEffect(name: String, prerequisiteResearch: Research? = null) : SpellEffect(ResourceLocation(Constants.MOD_ID, name), prerequisiteResearch) {
    companion object {
        fun createParticlesAt(state: DeliveryTransitionState, particlePacket: ParticlePacket) {
            AfraidOfTheDark.packetHandler.sendToAllAround(particlePacket, PacketDistributor.TargetPoint(state.position.x, state.position.y, state.position.z, 100.0, state.world.dimension()))
        }
    }
}