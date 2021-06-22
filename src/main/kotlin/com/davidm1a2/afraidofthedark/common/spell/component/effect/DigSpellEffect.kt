package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.fml.network.PacketDistributor
import kotlin.random.Random

/**
 * Dig effect digs a block
 */
class DigSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "dig")) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val world = state.world
        val entity = state.getEntity()
        val min = if (reducedParticles) 0 else 5
        val max = if (reducedParticles) 1 else 10
        if (entity != null) {
            // Digs the block under the entity
            val blockPos = entity.position.down()
            if (canBlockBeDestroyed(world, blockPos)) {
                createParticlesAt(min, max, entity.positionVector, entity.dimension, ModParticles.DIG)
                world.destroyBlock(blockPos, true)
            }
        } else {
            // Digs the block at the position
            val position = state.blockPosition
            if (canBlockBeDestroyed(world, position)) {
                if (!reducedParticles) {
                    createParticlesAt(min, max, state.position, world.dimension.type, ModParticles.DIG)
                } else if (Random.nextDouble() > 0.6) {
                    createParticlesAt(min, max, state.position, world.dimension.type, ModParticles.DIG)
                }
                world.destroyBlock(position, true)
            }
        }
    }

    /**
     * Tests if a given block can be broken with a dig spell
     *
     * @param world    The world the block is in
     * @param blockPos The pos the block is at
     * @return True if the block can be destroyed, false otherwise
     */
    private fun canBlockBeDestroyed(world: World, blockPos: BlockPos): Boolean {
        val blockState = world.getBlockState(blockPos)
        @Suppress("DEPRECATION")
        return blockState.block.getBlockHardness(blockState, world, blockPos) != -1f
    }

    /**
     * Gets the cost of the effect given the state of the effect
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of dig is 14
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 14.0
    }
}