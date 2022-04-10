package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.ProjectileFlyChannel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import net.minecraftforge.fml.network.PacketDistributor
import kotlin.math.ceil
import kotlin.math.sqrt
import kotlin.random.Random

class FrostPhoenixProjectileEntity(entityType: EntityType<out FrostPhoenixProjectileEntity>, world: World) : Entity(entityType, world), IMCAnimatedModel {
    private var shootingEntity: FrostPhoenixEntity? = null
    private val animHandler = AnimationHandler(FLY_CHANNEL)
    private var ticksInAir = 0

    constructor(
        shootingEntity: FrostPhoenixEntity,
        xDirection: Double,
        yDirection: Double,
        zDirection: Double
    ) : this(ModEntities.FROST_PHOENIX_PROJECTILE, shootingEntity.level) {
        // Update the entity that fired this projectile
        this.shootingEntity = shootingEntity

        // Position the entity at the center of the phoenix
        moveTo(shootingEntity.x, shootingEntity.y + shootingEntity.eyeHeight, shootingEntity.z, shootingEntity.yRot, shootingEntity.xRot)

        val directionMagnitude = sqrt(xDirection * xDirection + yDirection * yDirection + zDirection * zDirection)
        // Update the acceleration vector by normalizing it and multiplying by speed
        deltaMovement = Vector3d(
            xDirection / directionMagnitude * PROJECTILE_SPEED,
            yDirection / directionMagnitude * PROJECTILE_SPEED,
            zDirection / directionMagnitude * PROJECTILE_SPEED
        )
    }

    override fun defineSynchedData() {
    }

    override fun onAddedToWorld() {
        super.onAddedToWorld()
        if (level.isClientSide) {
            animHandler.playAnimation(FLY_CHANNEL.name)
        }
    }

    override fun tick() {
        super.tick()

        // Update logic server side
        if (!level.isClientSide) {
            // Ensure the shooting entity is null or not dead, and that the area the projectile is in is loaded
            @Suppress("DEPRECATION")
            if ((shootingEntity == null || shootingEntity!!.isAlive) && level.isLoaded(this.blockPosition())) {
                // We are in the air, so increment our counter
                this.ticksInAir = this.ticksInAir + 1

                // Perform a ray cast to test if we've hit something. We can only hit the entity that fired the projectile after 25 ticks
                val rayTraceResult = ProjectileHelper.getHitResult(this) {
                    if (ticksInAir > 25) {
                        true
                    } else {
                        it != shootingEntity
                    }
                }

                if (rayTraceResult.type != RayTraceResult.Type.MISS) {
                    onImpact(rayTraceResult)
                }

                // Continue flying in the direction of motion, update the position
                moveTo(x + deltaMovement.x, y + deltaMovement.y, z + deltaMovement.z)
            } else {
                remove()
            }
        }
    }

    private fun onImpact(result: RayTraceResult) {
        // Only process server side
        if (!level.isClientSide) {
            val nearbyEntities = level.getEntitiesOfClass(LivingEntity::class.java, boundingBox.inflate(HIT_RADIUS)) { it.distanceTo(this) < HIT_RADIUS }

            // Spawn freeze particles
            val centerPosition = result.location
            val positions = List(40) {
                centerPosition.add(
                    Random.nextDouble(-HIT_RADIUS, HIT_RADIUS),
                    Random.nextDouble(-HIT_RADIUS, HIT_RADIUS),
                    Random.nextDouble(-HIT_RADIUS, HIT_RADIUS)
                )
            }

            // Send the particle packet
            AfraidOfTheDark.packetHandler.sendToAllAround(
                ParticlePacket(
                    ModParticles.FREEZE,
                    positions,
                    List(positions.size) { Vector3d.ZERO }),
                PacketDistributor.TargetPoint(centerPosition.x, centerPosition.y, centerPosition.z, 100.0, level.dimension())
            )

            // Slow all impacted entities
            for (nearbyEntity in nearbyEntities) {
                if (nearbyEntity.hurt(ModDamageSources.getFrostPhoenixProjectileDamage(this, shootingEntity), 8f)) {
                    nearbyEntity.addEffect(EffectInstance(Effects.MOVEMENT_SLOWDOWN, 120, 2, false, false))
                }
            }

            // Freeze nearby snow/ice blocks
            val intRadius = ceil(HIT_RADIUS).toInt()
            val centerBlockPos = BlockPos(centerPosition)
            for (x in -intRadius..intRadius) {
                for (y in -intRadius..intRadius) {
                    for (z in -intRadius..intRadius) {
                        if (x * x + y * y + z * z < HIT_RADIUS_SQUARED) {
                            val blockPos = centerBlockPos.offset(x, y, z)
                            when (level.getBlockState(blockPos).block) {
                                Blocks.SNOW_BLOCK -> level.setBlockAndUpdate(blockPos, Blocks.ICE.defaultBlockState())
                                Blocks.ICE -> level.setBlockAndUpdate(blockPos, Blocks.PACKED_ICE.defaultBlockState())
                                Blocks.PACKED_ICE -> level.setBlockAndUpdate(blockPos, Blocks.BLUE_ICE.defaultBlockState())
                            }
                        }
                    }
                }
            }

            // Kill the projectile
            remove()
        }
    }

    override fun isPushedByFluid(): Boolean {
        return false;
    }

    override fun isPickable(): Boolean {
        return true
    }

    override fun getPickRadius(): Float {
        return 0.4f
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        if (source == DamageSource.OUT_OF_WORLD) {
            return super.hurt(source, amount)
        }
        return false
    }

    override fun getBrightness(): Float {
        return 1.0f
    }

    override fun canRide(entityIn: Entity): Boolean {
        return false
    }

    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        this.ticksInAir = compound.getInt("ticks_in_air")
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        compound.putInt("ticks_in_air", this.ticksInAir)
    }

    companion object {
        // The speed of the projectile
        private const val PROJECTILE_SPEED = 0.9
        private const val HIT_RADIUS = 4.0
        private const val HIT_RADIUS_SQUARED = HIT_RADIUS * HIT_RADIUS

        private val FLY_CHANNEL = ProjectileFlyChannel("Fly", 20.0f, 31, ChannelMode.LOOP)
    }
}