package com.davidm1a2.afraidofthedark.common.entity.splinterDrone;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModItems;
import com.davidm1a2.afraidofthedark.common.constants.ModResearches;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation.AnimationHandlerSplinterDrone;
import com.davidm1a2.afraidofthedark.common.packets.animationPackets.SyncAnimation;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * The splinter drone entity in gnomish cities
 */
public class EntitySplinterDrone extends EntityFlying implements IMob, IMCAnimatedEntity
{
    // Constants used to define splinter drone properties
    private static final double MOVE_SPEED = 0.05D;
    private static final double AGRO_RANGE = 30.0D;
    private static final double FOLLOW_RANGE = 30.0D;
    private static final double MAX_HEALTH = 20.0D;
    private static final double ATTACK_DAMAGE = 2.0D;
    private static final double KNOCKBACK_RESISTANCE = 0.5D;
    // NBT tag for if the skeleton has done the spawn animation yet or not
    private static final String NBT_PLAYED_SPAWN_ANIMATION = "played_spawn_animation";
    // The animation handler used to manage animations
    private final AnimationHandler animHandler = new AnimationHandlerSplinterDrone(this);
    // Flag telling us if we have played the spawn animation yet or not
    private boolean hasPlayedSpawnAnimation = false;

    /**
     * Constructor sets mob's size and other properties
     *
     * @param world The world that the splinter drone is in
     */
    public EntitySplinterDrone(World world)
    {
        super(world);
        // The the entity hitbox
        this.setSize(0.8F, 3.0F);
        // Make the entity immune to fire
        this.isImmuneToFire = true;
        // Set the EXP value to 7
        this.experienceValue = 7;

        // Update our move helper to fly like a ghast
        this.moveHelper = new EntitySplinterDroneMoveHelper(this);
    }

    /**
     * Sets up the entity's AI tasks
     */
    @Override
    protected void initEntityAI()
    {
        // Task one is always to face the nearest player
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, (float) AGRO_RANGE));
        // Task two is to hover over the ground and fly around
        this.tasks.addTask(2, new EntityAIHoverSplinterDrone(this));
        // Task three is to look idle and look around
        this.tasks.addTask(3, new EntityAILookIdle(this));

        // Set target tasks for shooting the player
        // Shoot the current target
        this.targetTasks.addTask(1, new EntityAIAttackSplinterDrone(this));
        // Find the nearest player to target and hit
        this.targetTasks.addTask(2, new EntityAIFindEntityNearestPlayer(this));
    }

    /**
     * Sets entity attributes such as max health and movespeed
     */
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        // Make sure to register the attack damage attribute for this entity
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EntitySplinterDrone.MAX_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntitySplinterDrone.FOLLOW_RANGE);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(EntitySplinterDrone.KNOCKBACK_RESISTANCE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntitySplinterDrone.MOVE_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EntitySplinterDrone.ATTACK_DAMAGE);
    }

    /**
     * Update animations for this entity when update is called, also kill the entity if it's peaceful
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        // If we're on peaceful and server side kill the entity
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            this.setDead();
        }
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
        // Update any base logic
        super.onEntityUpdate();

        // Server side test if the entity has played the spawn animation
        if (!this.world.isRemote)
        {
            if (!hasPlayedSpawnAnimation)
            {
                // If it hasn't played the spawn animation play it to all nearby players
                AfraidOfTheDark.INSTANCE.getPacketHandler().sendToAllAround(new SyncAnimation("Activate", this), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 50));
                this.hasPlayedSpawnAnimation = true;
            }
        }

        // If we're client side and no animation is active play the idle animation
        if (this.world.isRemote)
        {
            if (!animHandler.isAnimationActive("Activate") && !animHandler.isAnimationActive("Charge") && !animHandler.isAnimationActive("Idle"))
            {
                animHandler.activateAnimation("Idle", 0);
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     *
     * @param cause The damage source that killed the skeleton
     */
    @Override
    public void onDeath(DamageSource cause)
    {
        // Kill the entity
        super.onDeath(cause);

        // Only process server side
        if (!world.isRemote)
        {
            // If a player killed the entity unlock the gnomish city research
            if (cause.getTrueSource() instanceof EntityPlayer)
            {
                EntityPlayer entityPlayer = (EntityPlayer) cause.getTrueSource();

                // If we can unlock the gnomish city research do so
                IAOTDPlayerResearch playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
                if (playerResearch.canResearch(ModResearches.GNOMISH_CITY))
                {
                    playerResearch.setResearch(ModResearches.GNOMISH_CITY, true);
                    playerResearch.sync(entityPlayer, true);
                }
            }
        }
    }

    /**
     * Drop gnomish metal ingots on death
     *
     * @param wasRecentlyHit  If the entity was recently hit by a player
     * @param lootingModifier The player's looting modifier
     */
    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        // Drop 3 - 7 ingots
        this.dropItem(ModItems.GNOMISH_METAL_INGOT, rand.nextInt(5) + 3);
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
     * @return The eye height of the splinter drone which is up towards the top
     */
    @Override
    public float getEyeHeight()
    {
        return 1.5f;
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
}
