package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources.getPlasmaBallDamage
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation.SpingChannel
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.DamageSource
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import kotlin.math.sqrt

/**
 * Class representing the splinter drone projectile which gets fired by the splinter drone
 *
 * @constructor creates the entity without motion in the world
 * @param world The world the entity was created in
 * @property shootingEntity The entity that fired the projectile
 * @property animHandler The animation handler used to manage animations
 */
class SplinterDroneProjectileEntity(entityType: EntityType<out SplinterDroneProjectileEntity>, world: World) : Entity(entityType, world),
    IMCAnimatedModel {
    private var shootingEntity: SplinterDroneEntity? = null
    private val animHandler = AnimationHandler(SPING_CHANNEL)
    private var ticksInAir = 0

    /**
     * Recommended constructor that sets the projectile in motion given a velocity vector and shooting entity
     *
     * @param world          The world the entity is being spawned in
     * @param shootingEntity The entity that fired this projectile
     * @param xVelocity      The x component of velocity of the projectile
     * @param yVelocity      The y component of velocity of the projectile
     * @param zVelocity      The z component of velocity of the projectile
     */
    constructor(
        world: World,
        shootingEntity: SplinterDroneEntity,
        xVelocity: Double,
        yVelocity: Double,
        zVelocity: Double
    ) : this(ModEntities.SPLINTER_DRONE_PROJECTILE, world) {
        // Update the entity that fired this projectile
        this.shootingEntity = shootingEntity

        // Position the entity at the center of the drone
        moveTo(shootingEntity.x, shootingEntity.y + shootingEntity.eyeHeight, shootingEntity.z, shootingEntity.yRot, shootingEntity.xRot)

        val velocityMagnitude = sqrt(xVelocity * xVelocity + yVelocity * yVelocity + zVelocity * zVelocity)
        // Update the acceleration vector by normalizing it and multiplying by speed
        deltaMovement = Vector3d(
            xVelocity / velocityMagnitude * PROJECTILE_SPEED,
            yVelocity / velocityMagnitude * PROJECTILE_SPEED,
            zVelocity / velocityMagnitude * PROJECTILE_SPEED
        )
    }

    /**
     * Register any entity data. Anything registered here is automatically synced from Server -> Client
     */
    override fun defineSynchedData() {
    }

    override fun onAddedToWorld() {
        super.onAddedToWorld()
        if (level.isClientSide) {
            animHandler.playAnimation(SPING_CHANNEL.name)
        }
    }

    /**
     * Called every tick to update the entity's logic
     */
    override fun tick() {
        super.tick()

        // Animations only update client side
        if (level.isClientSide) {
            animHandler.update()
        }

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

    /**
     * Called when this Entity hits a block or entity.
     *
     * @param result The result of the ray hitting an object
     */
    private fun onImpact(result: RayTraceResult) {
        // Only process server side
        if (!level.isClientSide) {
            // Only do something if we hit an entity
            if (result is EntityRayTraceResult) {
                // Cause a slight amount of damage
                if (result.entity.hurt(getPlasmaBallDamage(this, shootingEntity), 1.0f)) {
                    // If we hit a player slow them
                    if (result.entity is PlayerEntity) {
                        // The player that was hit, add slowness
                        val entityPlayer = result.entity as PlayerEntity
                        entityPlayer.addEffect(EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 2, false, false))
                    }
                }
            }

            // Kill the projectile
            remove()
        }
    }

    /**
     * @return True, the projectile can hit other entities
     */
    override fun canBeCollidedWith(): Boolean {
        return true
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

    /**
     * Called when the entity is attacked.
     *
     * @param source The damage source that hit the projectile
     * @param amount The amount of damage inflicted
     * @return False, this projectile cannot be attacked
     */
    override fun hurt(source: DamageSource, amount: Float): Boolean {
        if (source == DamageSource.OUT_OF_WORLD) {
            return super.hurt(source, amount)
        }
        return false
    }

    /**
     * Gets how bright this entity is.
     *
     * @return 1.0, max brightness so the projectile isn't too dar
     */
    override fun getBrightness(): Float {
        return 1.0f
    }

    /**
     * The splinter drone projectile can't ride anything
     *
     * @param entityIn The entity to test
     * @return False
     */
    override fun canRide(entityIn: Entity): Boolean {
        return false
    }

    /**
     * Gets the animation handler which makes the projectile spin
     *
     * @return The animation handler for the projectile
     */
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
        private const val PROJECTILE_SPEED = 0.5

        private val SPING_CHANNEL = SpingChannel("Sping", 100.0f, 100, ChannelMode.LOOP)
    }
}