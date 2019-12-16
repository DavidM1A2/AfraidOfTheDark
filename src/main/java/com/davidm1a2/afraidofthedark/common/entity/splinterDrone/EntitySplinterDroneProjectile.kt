package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources.causePlasmaBallDamage
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation.AnimationHandlerSplinterDroneProjectile
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.sqrt

/**
 * Class representing the splinter drone projectile which gets fired by the splinter drone
 *
 * @constructor creates the entity without motion in the world
 * @param world The world the entity was created in
 * @property shootingEntity The entity that fired the projectile
 * @property ticksInAir The number of ticks the projectile has been in the air
 * @property animHandler The animation handler used to manage animations
 */
class EntitySplinterDroneProjectile(world: World) : Entity(world), IMCAnimatedEntity
{
    private var shootingEntity: EntitySplinterDrone? = null
    private var ticksInAir = 0
    private val animHandler = AnimationHandlerSplinterDroneProjectile(this)

    init
    {
        setSize(0.4f, 0.4f)
    }

    /**
     * Recommended constructor that sets the projectile in motion given a velocity vector and shooting entity
     *
     * @param world          The world the entity is being spawned in
     * @param shootingEntity The entity that fired this projectile
     * @param xVelocity      The x component of velocity of the projectile
     * @param yVelocity      The y component of velocity of the projectile
     * @param zVelocity      The z component of velocity of the projectile
     */
    constructor(world: World, shootingEntity: EntitySplinterDrone, xVelocity: Double, yVelocity: Double, zVelocity: Double) : this(world)
    {
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
        motionX = xVelocity / velocityMagnitude * PROJECTILE_SPEED
        motionY = yVelocity / velocityMagnitude * PROJECTILE_SPEED
        motionZ = zVelocity / velocityMagnitude * PROJECTILE_SPEED
    }

    /**
     * Required init method, does nothing
     */
    override fun entityInit()
    {
    }

    /**
     * Called every tick to update the entity's logic
     */
    override fun onUpdate()
    {
        super.onUpdate()
        // Animations only update client side
        if (world.isRemote)
        {
            animHandler.animationsUpdate()
        }
        // Update logic server side
        if (!world.isRemote)
        {
            // Ensure the shooting entity is null or not dead, and that the area the projectile is in is loaded
            if ((shootingEntity == null || !shootingEntity!!.isDead) && world.isBlockLoaded(BlockPos(this)))
            {
                // We are in the air, so increment our counter
                ticksInAir++
                // Perform a ray cast to test if we've hit something. We can only hit the entity that fired the projectile after 25 ticks
                val rayTraceResult: RayTraceResult? = ProjectileHelper.forwardsRaycast(this, true, ticksInAir >= 25, shootingEntity)
                // If the ray trace hit something, perform the hit effect
                // Intellij says this is always non-null, that is not the case....
                if (rayTraceResult != null)
                {
                    onImpact(rayTraceResult)
                }
                // Continue flying in the direction of motion, update the position
                setPosition(posX + motionX, posY + motionY, posZ + motionZ)
            }
            else
            {
                setDead()
            }
        }
    }

    /**
     * Called from onUpdate to update entity specific logic
     */
    override fun onEntityUpdate()
    {
        super.onEntityUpdate()
        // If we're client side and no animation is active play the sping animation
        if (world.isRemote)
        {
            if (!animHandler.isAnimationActive("Sping"))
            {
                animHandler.activateAnimation("Sping", 0f)
            }
        }
    }

    /**
     * Called when this Entity hits a block or entity.
     *
     * @param result The result of the ray hitting an object
     */
    private fun onImpact(result: RayTraceResult)
    {
        // Only process server side
        if (!world.isRemote)
        {
            // Only do something if we hit an entity
            if (result.entityHit != null)
            {
                // Cause a slight amount of damage
                if (result.entityHit.attackEntityFrom(causePlasmaBallDamage(this, shootingEntity), 1.0f))
                {
                    // If we hit a player slow them
                    if (result.entityHit is EntityPlayer)
                    {
                        // The player that was hit, add slowness
                        val entityPlayer = result.entityHit as EntityPlayer
                        entityPlayer.addPotionEffect(PotionEffect(Potion.getPotionById(2)!!, 60, 2, false, false))
                    }
                }
            }
            // Kill the projectile
            setDead()
        }
    }

    /**
     * Writes the entity to the nbt compound
     *
     * @param compound The nbt compound to write to
     */
    override fun writeEntityToNBT(compound: NBTTagCompound)
    {
        compound.setInteger(NBT_TICKS_IN_AIR, ticksInAir)
        compound.setTag(NBT_MOTION_DIRECTION, newDoubleNBTList(motionX, motionY, motionZ))
    }

    /**
     * Reads the entity from the nbt compound into the entity object
     *
     * @param compound The nbt compound to read data from
     */
    override fun readEntityFromNBT(compound: NBTTagCompound)
    {
        ticksInAir = compound.getInteger(NBT_TICKS_IN_AIR)
        val motionTagList = compound.getTagList(NBT_MOTION_DIRECTION, Constants.NBT.TAG_DOUBLE)
        motionX = motionTagList.getDoubleAt(0)
        motionY = motionTagList.getDoubleAt(1)
        motionZ = motionTagList.getDoubleAt(2)
    }

    /**
     * @return True, the projectile can hit other entities
     */
    override fun canBeCollidedWith(): Boolean
    {
        return true
    }

    /**
     * Gets the collision bounding box which allows the collision box to be bigger than the entity box
     *
     * @return The size of the entity, 0.4
     */
    override fun getCollisionBorderSize(): Float
    {
        return 0.4f
    }

    /**
     * Called when the entity is attacked.
     *
     * @param source The damage source that hit the projectile
     * @param amount The amount of damage inflicted
     * @return False, this projectile cannot be attacked
     */
    override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean
    {
        return false
    }

    /**
     * Gets how bright this entity is.
     *
     * @return 1.0, max brightness so the projectile isn't too dar
     */
    override fun getBrightness(): Float
    {
        return 1.0f
    }

    /**
     * Not sure exactly what this does, but the fireball uses this code too so I copied the value over
     *
     * @return The same value as EntityFireball
     */
    @SideOnly(Side.CLIENT)
    override fun getBrightnessForRender(): Int
    {
        return 15728880
    }

    /**
     * Gets the animation handler which makes the projectile spin
     *
     * @return The animation handler for the projectile
     */
    override fun getAnimationHandler(): AnimationHandler
    {
        return animHandler
    }

    companion object
    {
        // NBT compound constants
        private const val NBT_TICKS_IN_AIR = "ticks_in_air"
        private const val NBT_MOTION_DIRECTION = "motion_direction"
        // The speed of the projectile
        private const val PROJECTILE_SPEED = 0.5
    }
}