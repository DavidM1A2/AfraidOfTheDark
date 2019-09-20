package com.davidm1a2.afraidofthedark.common.entity.spell.projectile;

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.animation.AnimationHandlerSpellProjectile;
import com.davidm1a2.afraidofthedark.common.spell.Spell;
import com.davidm1a2.afraidofthedark.common.spell.SpellStage;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder;
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.SpellDeliveryMethodProjectile;
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class representing the projectile delivery method
 */
public class EntitySpellProjectile extends Entity implements IMCAnimatedEntity
{
    // NBT compound constants
    private static final String NBT_TICKS_IN_AIR = "ticks_in_air";
    private static final String NBT_MOTION_DIRECTION = "motion_direction";
    private static final String NBT_SPELL = "spell";
    private static final String NBT_SPELL_INDEX = "spell_index";
    private static final String NBT_BLOCK_DISTANCE_REMAINING = "block_distance_remaining";

    // The number of ticks the projectile has been in the air
    private int ticksInAir;
    // The spell that this projectile is a part of
    private Spell spell;
    // The current spell stage index
    private int spellIndex;
    // The entity that fired the projectile, can be null
    private Entity shooter;
    // The amount of blocks left before this projectile expires
    private double blockDistanceRemaining;

    // The animation handler that this entity uses for all animations
    private AnimationHandler animHandler = new AnimationHandlerSpellProjectile(this);

    /**
     * Required constructor that sets the world
     *
     * @param worldIn The world the entity is in
     */
    public EntitySpellProjectile(World worldIn)
    {
        super(worldIn);
        this.setSize(0.4F, 0.4F);
    }

    /**
     * Required constructor that sets the world, pos, and velocity
     *
     * @param world      The world the entity is in
     * @param spell      The spell that this projectile is delivering
     * @param spellIndex The index of the current spell stage that is being executed
     * @param position   The position of the spell projectile
     * @param velocity   The velocity of the projectile
     */
    public EntitySpellProjectile(World world, Spell spell, int spellIndex, Vec3d position, Vec3d velocity)
    {
        this(world);
        this.spell = spell;
        this.spellIndex = spellIndex;
        SpellDeliveryMethodProjectile deliveryMethodProjectile = (SpellDeliveryMethodProjectile) spell.getStage(spellIndex).getDeliveryMethod();
        this.blockDistanceRemaining = deliveryMethodProjectile.getRange();
        // Grab the projectile speed from the delivery method
        double projectileSpeed = deliveryMethodProjectile.getSpeed();
        // Default velocity will just be random velocity
        if (velocity == null)
        {
            velocity = new Vec3d(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
        }
        // Update the acceleration vector by normalizing it and multiplying by speed
        Vec3d motion = velocity.normalize().scale(projectileSpeed);
        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;
        // Position the entity at the center of the shooter moved slightly in the dir of fire
        this.setPosition(position.x + this.motionX, position.y + this.motionY, position.z + this.motionZ);
        // Null shooter
        this.shooter = null;
    }

    /**
     * Constructor sets the projectile's properties based on a shooter entity
     *
     * @param world      The world the projectile is in
     * @param spell      The spell that was fired
     * @param spellIndex The index of the current spell stage that is being executed
     * @param entity     The entity that fired the projectile
     */
    public EntitySpellProjectile(World world, Spell spell, int spellIndex, Entity entity)
    {
        this(world, spell, spellIndex, entity.getPositionVector().addVector(0, entity.getEyeHeight(), 0), entity.getLookVec());
        this.setRotation(entity.rotationYaw, entity.rotationPitch);
        this.shooter = entity;
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
            if (this.world.isBlockLoaded(new BlockPos(this)))
            {
                // We are in the air, so increment our counter
                this.ticksInAir++;

                // Perform a ray case to test if we've hit something. We can only hit the entity that fired the projectile after 25 ticks
                RayTraceResult rayTraceResult = ProjectileHelper.forwardsRaycast(this, true, this.ticksInAir >= 25, this.shooter);

                // If the ray trace hit something, perform the hit effect
                // Intellij says this is always non-null, that is not the case....
                if (rayTraceResult != null)
                {
                    this.onImpact(rayTraceResult);
                }

                // Continue flying in the direction of motion, update the position
                this.setPosition(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

                // Update distance flown, and kill the entity if it went
                double distanceFlown = new Vec3d(this.motionX, this.motionY, this.motionZ).lengthVector();
                this.blockDistanceRemaining = this.blockDistanceRemaining - distanceFlown;

                // If we're out of distance deliver the spell and kill the projectile
                if (this.blockDistanceRemaining <= 0)
                {
                    DeliveryTransitionState state = new DeliveryTransitionStateBuilder()
                            .withSpell(spell)
                            .withStageIndex(spellIndex)
                            .withWorld(world)
                            .withPosition(this.getPositionVector())
                            .withBlockPosition(this.getPosition())
                            .withDirection(new Vec3d(this.motionX, this.motionY, this.motionZ))
                            .withDeliveryEntity(this)
                            .build();

                    // Proc the effects and transition
                    SpellDeliveryMethod currentDeliveryMethod = spell.getStage(spellIndex).getDeliveryMethod();
                    currentDeliveryMethod.procEffects(state);
                    currentDeliveryMethod.transitionFrom(state);

                    this.setDead();
                }
            }
            else
            {
                this.setDead();
            }
        }
    }

    /**
     * Called when this Entity hits a block or entity.
     *
     * @param result The result of the ray hitting an object
     */
    private void onImpact(RayTraceResult result)
    {
        // Only process server side
        if (!this.world.isRemote)
        {
            // Grab the current spell stage
            SpellStage currentStage = spell.getStage(spellIndex);
            // If the type was a miss don't do anything
            if (result.typeOfHit == RayTraceResult.Type.MISS)
            {
                // Don't do anything
            }
            // If we hit a block proceed with the effects
            else
            {
                SpellDeliveryMethod currentDeliveryMethod = currentStage.getDeliveryMethod();
                if (result.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    // Grab the hit position
                    BlockPos hitPos = new BlockPos(result.hitVec);
                    // If we hit an air block find the block to the side of the air, hit that instead
                    if (world.getBlockState(hitPos).getBlock() == Blocks.AIR)
                    {
                        hitPos = hitPos.offset(result.sideHit.getOpposite());
                    }

                    DeliveryTransitionState state = new DeliveryTransitionStateBuilder()
                            .withSpell(spell)
                            .withStageIndex(spellIndex)
                            .withWorld(world)
                            .withPosition(result.hitVec)
                            .withBlockPosition(hitPos)
                            .withDirection(new Vec3d(this.motionX, this.motionY, this.motionZ))
                            .withDeliveryEntity(this)
                            .build();

                    // Proc the effects and transition
                    currentDeliveryMethod.procEffects(state);
                    currentDeliveryMethod.transitionFrom(state);
                }
                // If we hit an entity apply effects to the entity
                else if (result.typeOfHit == RayTraceResult.Type.ENTITY)
                {
                    DeliveryTransitionState state = new DeliveryTransitionStateBuilder()
                            .withSpell(spell)
                            .withStageIndex(spellIndex)
                            .withEntity(result.entityHit)
                            .withDeliveryEntity(this)
                            .build();

                    // Proc the effects and transition
                    currentDeliveryMethod.procEffects(state);
                    currentDeliveryMethod.transitionFrom(state);
                }
            }
            // Kill the projectile
            this.setDead();
        }
    }

    /**
     * Called from onUpdate to update entity specific logic
     */
    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();

        // If we're client side and no animation is active play the idle animation
        if (this.world.isRemote)
        {
            if (!animHandler.isAnimationActive("Idle"))
            {
                animHandler.activateAnimation("Idle", 0);
            }
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
        compound.setTag(NBT_SPELL, this.spell.serializeNBT());
        compound.setInteger(NBT_SPELL_INDEX, this.spellIndex);
        compound.setDouble(NBT_BLOCK_DISTANCE_REMAINING, this.blockDistanceRemaining);
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
        this.spell = new Spell(compound.getCompoundTag(NBT_SPELL));
        this.spellIndex = compound.getInteger(NBT_SPELL_INDEX);
        this.blockDistanceRemaining = compound.getDouble(NBT_BLOCK_DISTANCE_REMAINING);
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
     * @return 1.0, max brightness so the projectile isn't too dark
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
        return animHandler;
    }
}
