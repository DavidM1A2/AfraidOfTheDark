package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.AOESpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.ISpellDeliveryEffectApplicator
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.utility.getNormal
import net.minecraft.block.Blocks
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Registers any spell effect overrides
 */
class SpellEffectOverrideRegister {
    /**
     * Register all of our mod spell effect overrides
     */
    @SubscribeEvent
    fun commonSetupEvent(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            registerAoeFixes()
            registerAoeTeleportFix()
            registerAoeFreezeFix()
        }
    }

    /**
     * Fixes the insane lag when using AOE+aoe effects, the effect is randomly placed in the AOE instead of at every possible spot
     */
    private fun registerAoeFixes() {
        val customAOEApplicator = object : ISpellDeliveryEffectApplicator {
            override fun procEffect(
                state: DeliveryTransitionState,
                effect: SpellComponentInstance<SpellEffect>
            ): Boolean {
                val deliveryInstance = state.getCurrentStage().deliveryInstance!!
                val deliveryMethod = deliveryInstance.component

                // Should always be true, we're overriding AOE's custom applicator
                if (deliveryMethod is AOESpellDeliveryMethod) {
                    val radius = deliveryMethod.getRadius(deliveryInstance)
                    // Custom logic for block AOE
                    // Don't apply big AOE effects to every spot in the AOE, otherwise we lag hard. Pick sqrt(radius) random points inside the AOE
                    val maxExplosions = ceil(sqrt(radius)).toInt().coerceIn(1, Int.MAX_VALUE)
                    val basePos = BlockPos(state.position)

                    var explosionCount = 0
                    while (explosionCount < maxExplosions) {
                        val randomX = Random.nextDouble(radius * 2) - radius
                        val randomY = Random.nextDouble(radius * 2) - radius
                        val randomZ = Random.nextDouble(radius * 2) - radius

                        // Grab the blockpos
                        val aoePos = basePos.offset(randomX, randomY, randomZ)

                        // Test to see if the block is within the radius
                        if (aoePos.distSqr(basePos) < radius * radius) {
                            // Apply the effect at the position
                            val position = Vector3d.atCenterOf(aoePos)
                            val direction = position.subtract(state.position).normalize()
                            var normal = direction.getNormal()
                            // Straight up means we can't know our normal. Just use 1, 0, 0
                            if (normal == Vector3d.ZERO) {
                                normal = Vector3d(1.0, 0.0, 0.0)
                            }
                            effect.component.procEffect(
                                DeliveryTransitionState(
                                    spell = state.spell,
                                    stageIndex = state.stageIndex,
                                    world = state.world,
                                    position = position,
                                    blockPosition = aoePos,
                                    direction = direction,
                                    normal = normal,
                                    casterEntity = state.casterEntity
                                ),
                                effect,
                                reducedParticles = true
                            )
                            explosionCount++
                        }
                    }
                }
                return true
            }
        }
        ModSpellDeliveryMethods.AOE.addCustomEffectApplicator(ModSpellEffects.EXPLOSION, customAOEApplicator)
    }

    /**
     * Fixes the crazy teleport spam when using AOE+teleport causing a bunch of teleports
     */
    private fun registerAoeTeleportFix() {
        val customAOEApplicator = object : ISpellDeliveryEffectApplicator {
            override fun procEffect(
                state: DeliveryTransitionState,
                effect: SpellComponentInstance<SpellEffect>
            ): Boolean {
                val world = state.world

                val spellCaster = state.casterEntity
                if (spellCaster != null) {
                    // Get the delivery method instance
                    val deliveryInstance = state.getCurrentStage().deliveryInstance!!

                    // Get the radius
                    val radius = (deliveryInstance.component as AOESpellDeliveryMethod).getRadius(deliveryInstance)
                    // The center point
                    val center = state.position

                    // Pick a random spot in the AOE to teleport to, try 20 times to find an air space
                    for (i in 0..19) {
                        val teleportPos = center.add(
                            Random.nextDouble(radius * 2) - radius,
                            Random.nextDouble(radius * 2) - radius,
                            Random.nextDouble(radius * 2) - radius
                        )
                        val blockPos = BlockPos(teleportPos)
                        val blockState = world.getBlockState(blockPos)

                        if (blockState.isAir(world, blockPos)) {
                            // Create particles at the pre and post teleport position
                            // Play sound at the pre and post teleport position
                            AOTDSpellEffect.createParticlesAt(1, 3, teleportPos, spellCaster.level.dimension(), ModParticles.ENDER)
                            world.playSound(
                                null,
                                teleportPos.x,
                                teleportPos.y,
                                teleportPos.z,
                                SoundEvents.ENDERMAN_TELEPORT,
                                SoundCategory.PLAYERS,
                                2.5f,
                                1.0f
                            )

                            spellCaster.moveTo(teleportPos.x, teleportPos.y, teleportPos.z)

                            AOTDSpellEffect.createParticlesAt(1, 3, teleportPos, spellCaster.level.dimension(), ModParticles.ENDER)
                            world.playSound(
                                null,
                                teleportPos.x,
                                teleportPos.y,
                                teleportPos.z,
                                SoundEvents.ENDERMAN_TELEPORT,
                                SoundCategory.PLAYERS,
                                2.5f,
                                1.0f
                            )
                            break
                        }
                    }
                }
                return true
            }
        }
        ModSpellDeliveryMethods.AOE.addCustomEffectApplicator(ModSpellEffects.TELEPORT, customAOEApplicator)
    }

    /**
     * Makes it so AOE+freeze creates a giant ice sphere at the position
     */
    private fun registerAoeFreezeFix() {
        val customAOEApplicator = object : ISpellDeliveryEffectApplicator {
            override fun procEffect(
                state: DeliveryTransitionState,
                effect: SpellComponentInstance<SpellEffect>
            ): Boolean {
                val world = state.world
                val centerPosition = state.position
                val centerBlockPosition = state.blockPosition
                val deliveryInstance = state.getCurrentStage().deliveryInstance!!
                val deliveryMethodAOE = deliveryInstance.component as AOESpellDeliveryMethod

                // Grab the radius from the AOE spell delivery method
                val radius = deliveryMethodAOE.getRadius(deliveryInstance)
                val blockRadius = ceil(radius).toInt()

                // The threshold lets us define the thickness of the sphere
                val threshhold = 0.5

                // Iterate over all the blocks in the sphere
                for (x in -blockRadius until blockRadius + 1) {
                    for (y in -blockRadius until blockRadius + 1) {
                        for (z in -blockRadius until blockRadius + 1) {
                            // Grab the block position at the xyz position
                            val blockLocation = centerBlockPosition.offset(x, y, z)
                            val location = centerPosition.add(x.toDouble(), y.toDouble(), z.toDouble())

                            // Get the distance from the center to the location
                            val distance = centerPosition.distanceTo(location)
                            // Test if the block is within the threshold
                            if (distance < radius + threshhold && distance > radius - threshhold) {
                                // If the block is air replace it with ice
                                val blockState = world.getBlockState(blockLocation)
                                if (blockState.isAir) {
                                    world.setBlock(
                                        blockLocation,
                                        Blocks.PACKED_ICE.defaultBlockState(),
                                        2 or 16
                                    )
                                    AOTDSpellEffect.createParticlesAround(
                                        0,
                                        1,
                                        location,
                                        state.world.dimension(),
                                        ModParticles.FREEZE,
                                        0.5
                                    )
                                }
                            }
                        }
                    }
                }
                return true
            }
        }
        ModSpellDeliveryMethods.AOE.addCustomEffectApplicator(ModSpellEffects.FREEZE, customAOEApplicator)
    }
}