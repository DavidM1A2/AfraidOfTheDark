package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources.causePlasmaBallDamage
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
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceContext
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
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
    private val animHandler = AnimationHandler(SpingChannel("Sping", 100.0f, 100, ChannelMode.LINEAR))
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
        setLocationAndAngles(
            shootingEntity.posX,
            shootingEntity.posY + shootingEntity.eyeHeight,
            shootingEntity.posZ,
            shootingEntity.rotationYaw,
            shootingEntity.rotationPitch
        )

        val velocityMagnitude = sqrt(xVelocity * xVelocity + yVelocity * yVelocity + zVelocity * zVelocity)
        // Update the acceleration vector by normalizing it and multiplying by speed
        motion = Vec3d(
            xVelocity / velocityMagnitude * PROJECTILE_SPEED,
            yVelocity / velocityMagnitude * PROJECTILE_SPEED,
            zVelocity / velocityMagnitude * PROJECTILE_SPEED
        )
    }

    /**
     * Register any entity data. Anything registered here is automatically synced from Server -> Client
     */
    override fun registerData() {
    }

    /**
     * Called every tick to update the entity's logic
     */
    override fun tick() {
        super.tick()

        // Animations only update client side
        if (world.isRemote) {
            animHandler.update()
        }

        // Update logic server side
        if (!world.isRemote) {
            // Ensure the shooting entity is null or not dead, and that the area the projectile is in is loaded
            @Suppress("DEPRECATION")
            if ((shootingEntity == null || shootingEntity!!.isAlive) && world.isBlockLoaded(BlockPos(this))) {
                // We are in the air, so increment our counter
                this.ticksInAir = this.ticksInAir + 1

                // Perform a ray cast to test if we've hit something. We can only hit the entity that fired the projectile after 25 ticks
                val rayTraceResult = ProjectileHelper.rayTrace(this, true, ticksInAir >= 25, shootingEntity, RayTraceContext.BlockMode.COLLIDER)

                if (rayTraceResult.type != RayTraceResult.Type.MISS) {
                    onImpact(rayTraceResult)
                }

                // Continue flying in the direction of motion, update the position
                setPosition(posX + motion.x, posY + motion.y, posZ + motion.z)
            } else {
                remove()
            }
        }
    }

    /**
     * Called from onUpdate to update entity specific logic
     */
    override fun baseTick() {
        super.baseTick()

        // If we're client side and no animation is active play the sping animation
        if (world.isRemote) {
            if (!animHandler.isAnimationActive("Sping")) {
                animHandler.playAnimation("Sping")
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
        if (!world.isRemote) {
            // Only do something if we hit an entity
            if (result is EntityRayTraceResult) {
                // Cause a slight amount of damage
                if (result.entity.attackEntityFrom(causePlasmaBallDamage(this, shootingEntity), 1.0f)) {
                    // If we hit a player slow them
                    if (result.entity is PlayerEntity) {
                        // The player that was hit, add slowness
                        val entityPlayer = result.entity as PlayerEntity
                        entityPlayer.addPotionEffect(EffectInstance(Effects.SLOWNESS, 60, 2, false, false))
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

    /**
     * Gets the collision bounding box which allows the collision box to be bigger than the entity box
     *
     * @return The size of the entity, 0.4
     */
    override fun getCollisionBorderSize(): Float {
        return 0.4f
    }

    /**
     * Called when the entity is attacked.
     *
     * @param source The damage source that hit the projectile
     * @param amount The amount of damage inflicted
     * @return False, this projectile cannot be attacked
     */
    override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean {
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
    override fun canBeRidden(entityIn: Entity): Boolean {
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

    override fun createSpawnPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun writeAdditional(compound: CompoundNBT) {
        this.ticksInAir = compound.getInt("ticks_in_air")
    }

    override fun readAdditional(compound: CompoundNBT) {
        compound.putInt("ticks_in_air", this.ticksInAir)
    }

    companion object {
        // The speed of the projectile
        private const val PROJECTILE_SPEED = 0.5
    }
}