package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.SpellDeliveryMethodAOE
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.ISpellDeliveryEffectApplicator
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.helper.TargetType
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.worldGeneration.WorldGenFast
import net.minecraft.block.BlockAir
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Registers any spell effect overrides
 */
object SpellEffectOverrideRegister
{
    /**
     * Register all of our mod spell effect overrides
     */
    fun initialize()
    {
        registerAoeFixes()
        registerAoeTeleportFix()
        registerAoeFreezeFix()
    }

    /**
     * Fixes the insane lag when using AOE+aoe effects, the effect is randomly placed in the AOE instead of at every possible spot
     */
    private fun registerAoeFixes()
    {
        val customAOEApplicator = object : ISpellDeliveryEffectApplicator
        {
            override fun procEffect(state: DeliveryTransitionState, effect: SpellComponentInstance<SpellEffect>): Boolean
            {
                val deliveryInstance = state.getCurrentStage().deliveryInstance!!
                val deliveryMethod = deliveryInstance.component

                // Should always be true, we're overriding AOE's custom applicator
                if (deliveryMethod is SpellDeliveryMethodAOE)
                {
                    val radius = deliveryMethod.getRadius(deliveryInstance)
                    if (deliveryMethod.getTargetType(deliveryInstance) == TargetType.ENTITY)
                    {
                        // Fire default logic for entities, that's not a problem
                        return false
                    }
                    else
                    {
                        // Custom logic for block AOE
                        // Don't apply big AOE effects to every spot in the AOE, otherwise we lag hard. Pick sqrt(radius) random points inside the AOE
                        val maxExplosions = ceil(sqrt(radius)).toInt().coerceIn(1, Int.MAX_VALUE)
                        val basePos = BlockPos(state.position)
                        val transitionBuilder = DeliveryTransitionStateBuilder()
                            .withSpell(state.spell)
                            .withStageIndex(state.stageIndex)
                            .withWorld(state.world)

                        var explosionCount = 0
                        while (explosionCount < maxExplosions)
                        {
                            val randomX = Random.nextDouble(radius * 2) - radius
                            val randomY = Random.nextDouble(radius * 2) - radius
                            val randomZ = Random.nextDouble(radius * 2) - radius

                            // Grab the blockpos
                            val aoePos = basePos.add(randomX, randomY, randomZ)

                            // Test to see if the block is within the radius
                            if (aoePos.distanceSq(basePos) < radius * radius)
                            {
                                // Apply the effect at the position
                                effect.component.procEffect(
                                    transitionBuilder.withPosition(Vec3d(aoePos.x.toDouble(), aoePos.y.toDouble(), aoePos.z.toDouble()))
                                        .withBlockPosition(aoePos)
                                        // Random direction, AOE has no direction
                                        .withDirection(Vec3d(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize())
                                        .build(),
                                    effect
                                )
                                explosionCount++
                            }
                        }
                    }
                }
                return true
            }
        }
        ModSpellDeliveryMethods.AOE.addCustomEffectApplicator(ModSpellEffects.EXPLOSION, customAOEApplicator)
        ModSpellDeliveryMethods.AOE.addCustomEffectApplicator(ModSpellEffects.POTION_EFFECT, customAOEApplicator)
    }

    /**
     * Fixes the crazy teleport spam when using AOE+teleport causing a bunch of teleports
     */
    private fun registerAoeTeleportFix()
    {
        ModSpellDeliveryMethods.AOE.addCustomEffectApplicator(ModSpellEffects.TELEPORT, object : ISpellDeliveryEffectApplicator
        {
            override fun procEffect(state: DeliveryTransitionState, effect: SpellComponentInstance<SpellEffect>): Boolean
            {
                val world = state.world

                val spellCaster = state.spell.getOwner()
                if (spellCaster != null)
                {
                    // Get the delivery method instance
                    val deliveryInstance = state.getCurrentStage().deliveryInstance!!

                    // Get the radius
                    val radius = (deliveryInstance.component as SpellDeliveryMethodAOE).getRadius(deliveryInstance)
                    // The center point
                    val center = state.position

                    // Pick a random spot in the AOE to teleport to, try 20 times to find an air space
                    for (i in 0..19)
                    {
                        val teleportPos = center.addVector(
                            Random.nextDouble(radius * 2) - radius,
                            Random.nextDouble(radius * 2) - radius,
                            Random.nextDouble(radius * 2) - radius
                        )
                        val blockPos = BlockPos(teleportPos)

                        if (world.getBlockState(blockPos).block === Blocks.AIR)
                        {
                            // Create particles at the pre and post teleport position
                            // Play sound at the pre and post teleport position
                            AOTDSpellEffect.createParticlesAt(1, 3, teleportPos, spellCaster.dimension)
                            world.playSound(null, teleportPos.x, teleportPos.y, teleportPos.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5f, 1.0f)

                            (spellCaster as EntityPlayerMP).connection.setPlayerLocation(
                                teleportPos.x,
                                teleportPos.y,
                                teleportPos.z,
                                spellCaster.rotationYaw,
                                spellCaster.rotationPitch
                            )

                            AOTDSpellEffect.createParticlesAt(1, 3, teleportPos, spellCaster.dimension)
                            world.playSound(null, teleportPos.x, teleportPos.y, teleportPos.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5f, 1.0f)
                            break
                        }
                    }
                }
                return true
            }
        })
    }

    /**
     * Makes it so AOE+freeze creates a giant ice sphere at the position
     */
    private fun registerAoeFreezeFix()
    {
        ModSpellDeliveryMethods.AOE.addCustomEffectApplicator(ModSpellEffects.FREEZE, object : ISpellDeliveryEffectApplicator
        {
            override fun procEffect(state: DeliveryTransitionState, effect: SpellComponentInstance<SpellEffect>): Boolean
            {
                val world = state.world
                val centerPosition = state.position
                val centerBlockPosition = state.blockPosition
                val deliveryInstance = state.getCurrentStage().deliveryInstance!!
                val deliveryMethodAOE = deliveryInstance.component as SpellDeliveryMethodAOE

                // If we should target entities use default logic
                if (deliveryMethodAOE.getTargetType(deliveryInstance) == TargetType.ENTITY)
                {
                    return false
                }

                // Grab the radius from the AOE spell delivery method
                val radius = deliveryMethodAOE.getRadius(deliveryInstance)
                val blockRadius = ceil(radius).toInt()

                // The threshold lets us define the thickness of the sphere
                val threshhold = 0.5

                // Iterate over all the blocks in the sphere
                for (x in -blockRadius until blockRadius + 1)
                {
                    for (y in -blockRadius until blockRadius + 1)
                    {
                        for (z in -blockRadius until blockRadius + 1)
                        {
                            // Grab the block position at the xyz position
                            val blockLocation = centerBlockPosition.add(x, y, z)
                            val location = centerPosition.addVector(x.toDouble(), y.toDouble(), z.toDouble())

                            // Get the distance from the center to the location
                            val distance = centerPosition.distanceTo(location)
                            // Test if the block is within the threshold
                            if (distance < radius + threshhold && distance > radius - threshhold)
                            {
                                // If the block is air replace it with ice
                                if (world.getBlockState(blockLocation).block is BlockAir)
                                {
                                    WorldGenFast.setBlockStateFast(world, blockLocation, Blocks.ICE.defaultState, 2 or 16)
                                    AOTDSpellEffect.createParticlesAt(1, 3, location, state.world.provider.dimension)
                                }
                            }
                        }
                    }
                }
                return true
            }
        })
    }
}