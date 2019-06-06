package com.DavidM1A2.afraidofthedark.common.entity.enaria;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModDamageSources;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.entity.enaria.animation.AnimationHandlerEnaria;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

/**
 * The enaria entity that is the overworld boss
 */
public class EntityEnaria extends EntityMob implements IMCAnimatedEntity
{
    // Constants for enaria's stats
    private static final double MOVE_SPEED = 0.6D;
    private static final double FOLLOW_RANGE = 64.0D;
    private static final double MAX_HEALTH = 400.0D;
    private static final double ATTACK_DAMAGE = 12.0D;
    private static final double KNOCKBACK_RESISTANCE = 0.5D;
    private static final double RESEARCH_UNLOCK_RANGE = 100D;

    // NBT tag compounds for if the entity is valid and the last time she attacked
    private static final String NBT_IS_VALID = "is_valid";
    private static final String NBT_LAST_HIT = "last_hit";

    // The maximum amount of damage done in a single shot
    private static final int MAX_DAMAGE_IN_1_HIT = 10;

    // Enaria's animation handler
    private final AnimationHandler animHandler = new AnimationHandlerEnaria(this);

    // Enaria's boss info that allows the displaying of the boss HP bar
    private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    // The hitbox that enaria cannot leave
    private AxisAlignedBB allowedRegion;

    // Enaria's attack object
    private EnariaAttacks enariaAttacks;

    /**
     * Required single arg constructor just sets the entity size and world
     *
     * @param world The world the entity spawned in
     */
    public EntityEnaria(World world)
    {
        super(world);
        this.setSize(0.8F, 1.8F);
        // Name of the entity, will be bold and red
        this.setCustomNameTag("Enaria");
        this.bossInfo.setName(this.getDisplayName());
        this.experienceValue = 300;
        this.isImmuneToFire = true;
        this.enariaAttacks = new EnariaAttacks(this, this.rand);
    }

    /**
     * Overloaded constructor that sets the world and the region enaria is allowed to be in
     *
     * @param world         The world to spawn enaria in
     * @param allowedRegion The region enaria is allowed to be inside of
     */
    public EntityEnaria(World world, AxisAlignedBB allowedRegion)
    {
        this(world);
        this.getEntityData().setBoolean(NBT_IS_VALID, true);
        this.allowedRegion = allowedRegion;
    }

    /**
     * Initializes enaria's AI
     */
    @Override
    protected void initEntityAI()
    {
        // First ensure we're not drowning and try to swim
        this.tasks.addTask(1, new EntityAISwimming(this));
        // Attack the target if we are not swimming
        this.tasks.addTask(2, new EntityAIAttackEnaria(this));
        // Follow the target if we can't attack
        this.tasks.addTask(3, new EntityAIFollowPlayer(this, 8.0D, 128.0D, 32.0));
        // Watch the nearest player if we can't follow them
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 20.0F));

        // Find the nearest attackable player and attack
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    /**
     * Called to update the entity every tick
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        // Animations only update client side
        if (world.isRemote)
        {
            this.animHandler.animationsUpdate();
        }
    }

    /**
     * Called every game tick for the entity
     */
    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();

        // Client side test if enaria is walking, if so play the animation
        if (this.world.isRemote)
        {
            // Motion >= 0.5 = walking
            if (this.motionX > 0.05 || this.motionZ > 0.05 || this.motionX < -0.05 || this.motionZ < -0.05)
            {
                // Ensure no other animation is currently active
                if (!this.animHandler.isAnimationActive("spell") && !animHandler.isAnimationActive("autoattack") && !animHandler.isAnimationActive("armthrow") && !animHandler.isAnimationActive("walk"))
                {
                    // Walk
                    animHandler.activateAnimation("walk", 0);
                }
            }
        }
    }

    /**
     * Called every tick the enaria entity is alive
     */
    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        // If we're on server side perform some checks
        if (!this.world.isRemote)
        {
            // Update the boss info HP bar
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
            // If enaria was spawned without the right tags she's invalid so kill her
            if (this.ticksExisted == 1)
            {
                if (!this.getEntityData().getBoolean(NBT_IS_VALID))
                {
                    this.setDead();
                }
            }
        }
    }

    /**
     * Gives enaria her entity attributes like damage and movespeed
     */
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EntityEnaria.MAX_HEALTH);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntityEnaria.FOLLOW_RANGE);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(EntityEnaria.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntityEnaria.MOVE_SPEED);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EntityEnaria.ATTACK_DAMAGE);
    }

    /**
     * Called when enaria gets attacked
     *
     * @param source The damage source that hit enaria
     * @param amount The amount of damage inflicted
     * @return True to allow the damage, false otherwise
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        // Server side processing only
        if (!world.isRemote)
        {
            // Compute the time between this hit and the last hit she received
            long timeBetweenHits = System.currentTimeMillis() - this.getEntityData().getLong(NBT_LAST_HIT);
            // Update the last hit time
            this.getEntityData().setLong(NBT_LAST_HIT, timeBetweenHits);

            // Kill the entity if damage received is FLOAT.MAX_VALUE
            if (amount == Float.MAX_VALUE)
            {
                return super.attackEntityFrom(source, amount);
            }
            // Can't hit enaria for more than the max damage in 1 hit
            else if (amount > MAX_DAMAGE_IN_1_HIT)
            {
                amount = MAX_DAMAGE_IN_1_HIT;
            }

            // If an entity damaged the entity check if it was with silver damage
            if (source instanceof EntityDamageSource)
            {
                // Grab the source of the damage
                Entity damageSource = source.getTrueSource();
                if (damageSource instanceof EntityPlayer)
                {
                    EntityPlayer playerSource = (EntityPlayer) damageSource;
                    // If a player hit enaria check if they have the right research
                    if (!playerSource.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.ENARIA.getPreRequisite()))
                    {
                        playerSource.sendMessage(new TextComponentTranslation("aotd.enaria.dont_understand"));
                        // Can't damage enaria without research
                        return false;
                    }
                }

                // If the damage source is silver damage inflict heavy damage
                if (source.damageType.equalsIgnoreCase(ModDamageSources.SILVER_DAMAGE))
                {
                    // If its been more than a second since the last attack do full damage, otherwise scale the damage
                    float amountModifier = Math.min(1.0f, timeBetweenHits / 1000.0f);
                    // If the amount of time is less than 250ms then the player is spam clicking and enaria should teleport 1/40 times
                    if (this.rand.nextInt(40) == 0)
                    {
                        this.enariaAttacks.randomTeleport();
                    }

                    return super.attackEntityFrom(source, amount * amountModifier);
                }
                // If the player is attacking with non-silver damage teleport 1/60 times
                else
                {
                    if (this.rand.nextInt(60) == 0)
                    {
                        this.enariaAttacks.randomTeleport();
                    }
                }
            }
            // If enaria takes fall damage teleport 1/2 times
            else if (source == DamageSource.FALL)
            {
                if (this.rand.nextBoolean())
                {
                    this.enariaAttacks.randomTeleport();
                }
            }
            // If a non-player source is hitting enaria teleport 1/100 times
            else
            {
                if (this.rand.nextInt(100) == 0)
                {
                    this.enariaAttacks.randomTeleport();
                }
            }
        }

        // Finally if nothing succeeds do 1 damage. Only do this if she has more than 1hp, this ensures she doesn't
        // die to a non-player source
        if (this.getHealth() >= 2.0)
        {
            return super.attackEntityFrom(DamageSource.GENERIC, 1);
        }
        else
        {
            return false;
        }
    }

    /**
     * When enaria dies by a player unlock the enaria research
     *
     * @param cause The damage source that killed enaria
     */
    @Override
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        // Server side processing only
        if (!world.isRemote)
        {
            // If enaria died to a player damage source continue processing...
            if (cause instanceof EntityDamageSource)
            {
                // If enaria died to a player continue processing...
                if (cause.getTrueSource() instanceof EntityPlayer)
                {
                    // Grab all entities around enaria and if they can research "ENARIA" unlock the research for them
                    for (EntityPlayer entityPlayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(RESEARCH_UNLOCK_RANGE)))
                    {
                        IAOTDPlayerResearch playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
                        // If we can research enaria unlock it
                        if (playerResearch.canResearch(ModResearches.ENARIA))
                        {
                            playerResearch.setResearch(ModResearches.ENARIA, true);
                            playerResearch.sync(entityPlayer, true);
                        }
                    }
                }
            }
        }
    }

    /**
     * @return Make enaria's name tag red and bold in chat
     */
    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString("§c§l" + this.getCustomNameTag());
    }

    /**
     * Can't ride enaria
     *
     * @param entityIn The entity to test
     * @return False, duh
     */
    @Override
    protected boolean canBeRidden(Entity entityIn)
    {
        return false;
    }

    /**
     * @return False, enaria spawn behavior is managed by the tile entity
     */
    @Override
    protected boolean canDespawn()
    {
        return false;
    }

    /**
     * @return The animation handler used to animate enaria
     */
    @Override
    public AnimationHandler getAnimationHandler()
    {
        return animHandler;
    }

    /**
     * Called whenever a player enters range of enaria to show the boss bar
     *
     * @param player The player to show enaria's boss bar to
     */
    @Override
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    /**
     * Called whenever a player leaves range of enaria to hide the boss bar
     *
     * @param player The player to remove enaria's boss bar from
     */
    @Override
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    /**
     * @return False, enaria is a boss mob
     */
    @Override
    public boolean isNonBoss()
    {
        return false;
    }

    /**
     * @return Enaria's attack object use to manage her attacks
     */
    public EnariaAttacks getEnariaAttacks()
    {
        return enariaAttacks;
    }

    /**
     * @return The allowed region enaria may be in
     */
    public AxisAlignedBB getAllowedRegion()
    {
        return allowedRegion;
    }

    /**
     * Writes enaria's state to the NBT compound
     *
     * @param compound The NBT compound to write to
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setDouble("minX", this.allowedRegion.minX);
        compound.setDouble("minY", this.allowedRegion.minY);
        compound.setDouble("minZ", this.allowedRegion.minZ);
        compound.setDouble("maxX", this.allowedRegion.maxX);
        compound.setDouble("maxY", this.allowedRegion.maxY);
        compound.setDouble("maxZ", this.allowedRegion.maxZ);
    }

    /**
     * Reads enaria's state in from the NBT compound
     *
     * @param compound The NBT compound to read from
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.allowedRegion = new AxisAlignedBB(
                compound.getDouble("minX"),
                compound.getDouble("minY"),
                compound.getDouble("minZ"),
                compound.getDouble("maxX"),
                compound.getDouble("maxY"),
                compound.getDouble("maxZ")
        );
    }
}
