package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Dig effect digs a block
 */
class SpellEffectDig : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "dig"))
{
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>)
    {
        val world = state.world
        val entity = state.getEntity()
        if (entity != null)
        {
            // Digs the block under the entity
            val blockPos = entity.position.down()
            if (canBlockBeDestroyed(world, blockPos))
            {
                createParticlesAt(1, 3, entity.positionVector, entity.dimension)
                world.destroyBlock(blockPos, true)
            }
        }
        else
        {
            // Digs the block at the position
            val position = state.blockPosition
            if (canBlockBeDestroyed(world, position))
            {
                createParticlesAt(1, 3, state.position, world.provider.dimension)
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
    private fun canBlockBeDestroyed(world: World, blockPos: BlockPos): Boolean
    {
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
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double
    {
        return 14.0
    }
}