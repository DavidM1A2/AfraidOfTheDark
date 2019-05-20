package com.DavidM1A2.afraidofthedark.common.entity.splinterDrone;

import com.DavidM1A2.afraidofthedark.common.constants.ModDamageSources;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.animation.AnimationHandlerSplinterDroneProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class representing the splinter drone projectile which gets fired by the splinter drone
 */
public class EntitySplinterDroneProjectile extends Entity implements IMCAnimatedEntity
{
    // NBT compound constants
    private static final String NBT_TICKS_IN_AIR = "ticks_in_air";
    private static final String NBT_MOTION_DIRECTION = "motion_direction";

    // The speed of the projectile
    private static final double PROJECTILE_SPEED = 0.5;
    // The entity that fired the projectile
    private EntitySplinterDrone shootingEntity;
    // The number of ticks the projectile has been in the air
    private int ticksInAir;
    // The animation handler used to manage animations
    private final AnimationHandler animHandler = new AnimationHandlerSplinterDroneProjectile(this);

    /**
     * Constructor creates the entity without motion in the world
     *
     * @param world The world the entity was created in
     */
    public EntitySplinterDroneProjectile(World world)
    {
        super(world);
        this.setSize(0.4F, 0.4F);
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
    public EntitySplinterDroneProjectile(World world, EntitySplinterDrone shootingEntity, double xVelocity, double yVelocity, double zVelocity)
    {
        this(world);
        // Update the entity that fired this projectile
        this.shootingEntity = shootingEntity;
        // Position the entity at the center of the drone
        this.setLocationAndAngles(shootingEntity.posX, shootingEntity.posY + shootingEntity.getEyeHeight(), shootingEntity.posZ, shootingEntity.rotationYaw, shootingEntity.rotationPitch);
        double velocityMagnitude = MathHelper.sqrt(xVelocity * xVelocity + yVelocity * yVelocity + zVelocity * zVelocity);
        // Update the acceleration vector by normalizing it and multiplying by speed
        this.motionX = xVelocity / velocityMagnitude * PROJECTILE_SPEED;
        this.motionY = yVelocity / velocityMagnitude * PROJECTILE_SPEED;
        this.motionZ = zVelocity / velocityMagnitude * PROJECTILE_SPEED;
    }

    /**
     * Required init method, does nothing
     */
    @Override
    protected void entityInit()
    {
    }

    /**
     * Called every tick to update the entity's logic
     */
    public void onUpdate()
    {
        super.onUpdate();

        // Animations only update client side
        if (world.isRemote)
        {
            this.animHandler.animationsUpdate();
        }

        // Update logic server side
        if (!world.isRemote)
        {
            // Ensure the shooting entity is null or not dead, and that the area the projectile is in is loaded
            if ((this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this)))
            {
                // We are in the air, so increment our counter
                this.ticksInAir++;

                // Perform a ray case to test if we've hit something. We can only hit the entity that fired the projectile after 25 ticks
                RayTraceResult rayTraceResult = ProjectileHelper.forwardsRaycast(this, true, this.ticksInAir >= 25, this.shootingEntity);

                // If the ray trace hit something, perform the hit effect
                // Intellij says this is always non-null, that is not the case....
                if (rayTraceResult != null)
                {
                    this.onImpact(rayTraceResult);
                }

                // Continue flying in the direction of motion, update the position
                this.setPosition(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            }
            else
            {
                this.setDead();
            }
        }
    }

    /**
     * Called from onUpdate to update entity specific logic
     */
    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();

        // If we're client side and no animation is active play the sping animation
        if (this.world.isRemote)
        {
            if (!animHandler.isAnimationActive("Sping"))
            {
                animHandler.activateAnimation("Sping", 0);
            }
        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     *
     * @param result The result of the ray hitting an object
     */
    private void onImpact(RayTraceResult result)
    {
        // Only process server side
        if (!this.world.isRemote)
        {
            // Only do something if we hit an entity
            if (result.entityHit != null)
            {
                // Cause a slight amount of damage
                if (result.entityHit.attackEntityFrom(ModDamageSources.causePlasmaBallDamage(this, this.shootingEntity), 1.0F))
                {
                    // If we hit a player slow them
                    if (result.entityHit instanceof EntityPlayer)
                    {
                        // The player that was hit, add slowness
                        EntityPlayer entityPlayer = (EntityPlayer) result.entityHit;
                        entityPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, 2, false, false));
                    }
                }
            }

            // Kill the projectile
            this.setDead();
        }
    }

    /**
     * Writes the entity to the nbt compound
     *
     * @param compound The nbt compound to write to
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger(NBT_TICKS_IN_AIR, this.ticksInAir);
        compound.setTag(NBT_MOTION_DIRECTION, this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
    }

    /**
     * Reads the entity from the nbt compound into the entity object
     *
     * @param compound The nbt compound to read data from
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.ticksInAir = compound.getInteger(NBT_TICKS_IN_AIR);
        NBTTagList motionTagList = compound.getTagList(NBT_MOTION_DIRECTION, Constants.NBT.TAG_DOUBLE);
        this.motionX = motionTagList.getDoubleAt(0);
        this.motionY = motionTagList.getDoubleAt(1);
        this.motionZ = motionTagList.getDoubleAt(2);
    }

    /**
     * @return True, the projectile can hit other entities
     */
    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Gets the collision bounding box which allows the collision box to be bigger than the entity box
     *
     * @return The size of the entity, 0.4
     */
    @Override
    public float getCollisionBorderSize()
    {
        return 0.4f;
    }

    /**
     * Called when the entity is attacked.
     *
     * @param source The damage source that hit the projectile
     * @param amount The amount of damage inflicted
     * @return False, this projectile cannot be attacked
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }

    /**
     * Gets how bright this entity is.
     *
     * @return 1.0, max brightness so the projectile isn't too dar
     */
    @Override
    public float getBrightness()
    {
        return 1.0F;
    }

    /**
     * Not sure exactly what this does, but the fireball uses this code too so I copied the value over
     *
     * @return The same value as EntityFireball
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 15728880;
    }

    /**
     * Gets the animation handler which makes the projectile spin
     *
     * @return The animation handler for the projectile
     */
    @Override
    public AnimationHandler getAnimationHandler()
    {
        return this.animHandler;
    }
}
