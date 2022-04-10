package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.animation.ProjectileFlyChannel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.util.DamageSource
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import kotlin.math.sqrt

class FrostPhoenixProjectileEntity(entityType: EntityType<out FrostPhoenixProjectileEntity>, world: World) : Entity(entityType, world), IMCAnimatedModel {
    private var shootingEntity: FrostPhoenixEntity? = null
    private val animHandler = AnimationHandler(FLY_CHANNEL)
    private var ticksInAir = 0

    constructor(
        world: World,
        shootingEntity: FrostPhoenixEntity,
        xVelocity: Double,
        yVelocity: Double,
        zVelocity: Double
    ) : this(ModEntities.FROST_PHOENIX_PROJECTILE, world) {
        // Update the entity that fired this projectile
        this.shootingEntity = shootingEntity

        // Position the entity at the center of the phoenix
        moveTo(shootingEntity.x, shootingEntity.y + shootingEntity.eyeHeight, shootingEntity.z, shootingEntity.yRot, shootingEntity.xRot)

        val velocityMagnitude = sqrt(xVelocity * xVelocity + yVelocity * yVelocity + zVelocity * zVelocity)
        // Update the acceleration vector by normalizing it and multiplying by speed
        deltaMovement = Vector3d(
            xVelocity / velocityMagnitude * PROJECTILE_SPEED,
            yVelocity / velocityMagnitude * PROJECTILE_SPEED,
            zVelocity / velocityMagnitude * PROJECTILE_SPEED
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
            /*
            // Only do something if we hit an entity
            if (result is EntityRayTraceResult) {
                // Cause a slight amount of damage
                if (result.entity.hurt(ModDamageSources.getPlasmaBallDamage(this, shootingEntity), 1.0f)) {
                    // If we hit a player slow them
                    if (result.entity is PlayerEntity) {
                        // The player that was hit, add slowness
                        val entityPlayer = result.entity as PlayerEntity
                        entityPlayer.addEffect(EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 2, false, false))
                    }
                }
            }
             */

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
        private const val PROJECTILE_SPEED = 1.2

        private val FLY_CHANNEL = ProjectileFlyChannel("Fly", 20.0f, 31, ChannelMode.LOOP)
    }
}