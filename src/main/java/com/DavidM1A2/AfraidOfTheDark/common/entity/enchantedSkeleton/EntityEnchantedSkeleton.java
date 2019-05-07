/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton;


import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.animation.AnimationHandlerEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.DavidM1A2.afraidofthedark.common.item.ItemBladeOfExhumation;
import com.DavidM1A2.afraidofthedark.common.packets.animationPackets.SyncAnimation;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Class representing an enchanted skeleton entity
 */
public class EntityEnchantedSkeleton extends EntityMob implements IMCAnimatedEntity
{
    // Constants defining skeleton parameters
    private static final float MOVE_SPEED = 1.0f;
    private static final float AGRO_RANGE = 16.0f;
    private static final float FOLLOW_RANGE = 32.0f;
    private static final float MAX_HEALTH = 10.0f;
    private static final float ATTACK_DAMAGE = 4.0f;
    private static final float KNOCKBACK_RESISTANCE = 0.5f;
    // NBT tag for if the skeleton has done the spawn animation yet or not
    private static final String NBT_PLAYED_SPAWN_ANIMATION = "played_spawn_animation";
    // The animation handler used to manage animations
    private final AnimationHandler animHandler = new AnimationHandlerEnchantedSkeleton(this);
    // Flag telling us if we have played the spawn animation yet or not
    private boolean hasPlayedSpawnAnimation = false;

    /**
     * Constructor initializes the skeleton based on the world
     *
     * @param world The world the skeleton is spawning into
     */
    public EntityEnchantedSkeleton(World world)
    {
        // Call the base constructor
        super(world);
        // Set the size of the skeleton's hitbox
        this.setSize(0.8F, 2.0F);
        // Set how much XP the skeleton is worth
        this.experienceValue = 4;
    }

    /**
     * Creates the AI used by hostile or passive entities
     */
    @Override
    protected void initEntityAI()
    {
        // Tasks should have a list of AI tasks with a priority associated with them. Lower priority is executed first

        // If the entity can swim and it's in water it must do that otherwise it will skin
        this.tasks.addTask(1, new EntityAISwimming(this));
        // If it's not swimming, test if we can engage in combat, if so do that
        this.tasks.addTask(2, new EntityAIAttackMelee(this, MOVE_SPEED / 4, false));
        // If the entity isn't attacking then try to walk around
        this.tasks.addTask(3, new EntityAIWander(this, MOVE_SPEED / 4));
        // If the entity isn't wandering then try to watch whatever entity is nearby
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, AGRO_RANGE));
        // If the entity isn't walking, attacking, or watching anything look idle
        this.tasks.addTask(5, new EntityAILookIdle(this));

        // Targeting tasks get executed when the entity wants to attack and select a target

        // If the entity is hurt by a player target that player
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        // If the entity is not hurt find the nearest player to attack
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    /**
     * Sets entity attributes such as max health and movespeed
     */
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EntityEnchantedSkeleton.MAX_HEALTH);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntityEnchantedSkeleton.FOLLOW_RANGE);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(EntityEnchantedSkeleton.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntityEnchantedSkeleton.MOVE_SPEED);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EntityEnchantedSkeleton.ATTACK_DAMAGE);
    }

    /**
     * Update animations for this entity when update is called
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
        // Don't forget to call super!
        super.onEntityUpdate();

        // If we're server side
        if (!this.world.isRemote)
        {
            // If we haven't played the spawn animation yet, play it now
            if (!hasPlayedSpawnAnimation)
            {
                // Tell clients to show the spawn animation
                AfraidOfTheDark.INSTANCE.getPacketHandler().sendToAllAround(new SyncAnimation("Spawn", this), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 50));
                // Give the skeleton slowness and weakness for 3 seconds with level 100 so it can't do anything while spawning
                this.addPotionEffect(new PotionEffect(Potion.getPotionById(18), 60, 100));
                this.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, 100));
                // Set our flag
                this.hasPlayedSpawnAnimation = true;
            }
        }

        // If we're on client side test if we need to show walking animations
        if (this.world.isRemote)
        {
            // If spawn and attack are not active test if we can show walking animations
            if (!animHandler.isAnimationActive("Spawn") && !animHandler.isAnimationActive("Attack"))
            {
                // If the entity is moving show the walking animation
                if (this.motionX > 0.05 || this.motionZ > 0.05 || this.motionX < -0.05 || this.motionZ < -0.05)
                {
                    if (!animHandler.isAnimationActive("Walk"))
                    {
                        animHandler.activateAnimation("Walk", 0);
                    }
                }
                // If the entity is not moving show the idle animation
                else
                {
                    if (!animHandler.isAnimationActive("Idle"))
                    {
                        animHandler.activateAnimation("Idle", 0);
                    }
                }
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     *
     * @param damageSource The damage source that killed the skeleton
     */
    @Override
    public void onDeath(DamageSource damageSource)
    {
        // Call super
        super.onDeath(damageSource);

        // Server side processing only
        if (!this.world.isRemote)
        {
            // Test if a player killed the skeleton
            if (damageSource.getTrueSource() instanceof EntityPlayer)
            {
                // Grab a reference to the player
                EntityPlayer killer = (EntityPlayer) damageSource.getTrueSource();

                // Grab the player's research
                IAOTDPlayerResearch playerResearch = killer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);

                // If the player can research the blade of exhumation research give him the research
                if (playerResearch.canResearch(ModResearches.BLADE_OF_EXHUMATION))
                {
                    // Unlock the research for the player
                    playerResearch.setResearch(ModResearches.BLADE_OF_EXHUMATION, true);
                    playerResearch.sync(killer, true);
                }

                // If the blade of exhumation research is researched and the player is using a blade of exhumation drop one extra enchanted skeleton bone
                if (playerResearch.isResearched(ModResearches.BLADE_OF_EXHUMATION))
                {
                    if (killer.getHeldItemMainhand().getItem() instanceof ItemBladeOfExhumation)
                    {
                        this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY + 1, this.posZ, new ItemStack(ModItems.ENCHANTED_SKELETON_BONE, 1, 0)));
                    }
                }
            }
        }
    }

    /**
     * Called to drop items on the ground after the skeleton dies
     *
     * @param wasRecentlyHit  If the skeleton was recently hit
     * @param lootingModifier If looting was present, and what level of looting was present
     */
    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        // Drop exactly 3 bones, because 4 bones cause another skeleton to spawn
        this.dropItem(this.getDropItem(), 3);
    }

    /**
     * Skeletons drop enchanted skeleton bones!
     *
     * @return The enchanted skeleton bone
     */
    @Override
    protected Item getDropItem()
    {
        return ModItems.ENCHANTED_SKELETON_BONE;
    }

    /**
     * @return The handler for all animations for this entity
     */
    @Override
    public AnimationHandler getAnimationHandler()
    {
        return animHandler;
    }

    /**
     * Writes the entity to NBT, must also save our flag in NBT
     *
     * @param tagCompound The compound to write to
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setBoolean(NBT_PLAYED_SPAWN_ANIMATION, hasPlayedSpawnAnimation);
        super.writeEntityToNBT(tagCompound);
    }

    /**
     * Reads the entity to NBT, must also read our flag in NBT
     *
     * @param tagCompound The compound to read from
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        this.hasPlayedSpawnAnimation = tagCompound.getBoolean(NBT_PLAYED_SPAWN_ANIMATION);
        super.readEntityFromNBT(tagCompound);
    }

    /**
     * @param damageSourceIn The damage source that the entity was hit by
     * @return the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    /**
     * @return Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    /**
     * Plays the sound the entity makes when moving
     *
     * @param pos     The position the entity is at
     * @param blockIn The block the entity is over
     */
    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SKELETON_STEP, 0.15F, 1.0F);
    }

    /**
     * Called when the skeleton attacks an entity
     *
     * @param entity The entity attacked
     * @return True if the attack goes through, false otherwise
     */
    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        // Server side processing the hit only
        if (!entity.world.isRemote)
        {
            // Test if the entity is a player or not
            if (entity instanceof EntityPlayer)
            {
                // Cast the entity to a player
                EntityPlayer entityPlayer = (EntityPlayer) entity;
                // Add slowness and weakness\ to the player if hit by a skeleton
                entityPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 80, 0, false, true));
                entityPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(18), 80, 0, false, true));
            }
            // Send a packet to the other players telling them the skeleton attacked
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendToAllAround(new SyncAnimation("Attack", this, "Attack", "Spawn"), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 15));

        }
        return super.attackEntityAsMob(entity);
    }

    /**
     * @return The eye height of the skeleton which is used in path finding and looking around
     */
    @Override
    public float getEyeHeight()
    {
        return 1.9f;
    }
}