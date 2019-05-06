package com.DavidM1A2.afraidofthedark.common.entity.enaria;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.entity.enaria.animation.AnimationHandlerGhastlyEnaria;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Class representing the ghastly enaria entity
 */
public class EntityGhastlyEnaria extends EntityFlying implements IMCAnimatedEntity
{
    // The animation handler used to manage animations
    private final AnimationHandler animHandler = new AnimationHandlerGhastlyEnaria(this);

    // Constants defining enaria parameters
    private static final double MOVE_SPEED = 0.02D;
    private static final double FOLLOW_RANGE = 300.0D;
    private static final double MAX_HEALTH = 9001.0D;
    private static final double ATTACK_DAMAGE = 900.0D;
    private static final double KNOCKBACK_RESISTANCE = 1.0D;
    private static final double PLAYER_DISTANCE_CHECK_FREQUENCY = 10;
    private static final double PLAYER_BENIGN_CHECK_FREQUENCY = 200;

    // Constant for benign NBT field
    private static final String NBT_BENIGN = "benign";

    // Flag telling us if this enaria is benign or not, defaults to true. This will change her AI
    private boolean benign = true;

    /**
     * Constructor sets the ghastly enaria entity properties
     *
     * @param worldIn The world to put the entity in
     */
    public EntityGhastlyEnaria(World worldIn)
    {
        super(worldIn);
        // Sets the size of the hitbox of enaria
        this.setSize(0.8F, 1.8F);
        // The name of the entity
        this.setCustomNameTag(ChatFormatting.RED + "" + ChatFormatting.BOLD + "Ghastly Enaria");
        // Enable noclip so enaria can go through walls
        this.noClip = true;
        // Enaria is immune to fire
        this.isImmuneToFire = true;
        // Add a custom move helper that moves her through walls
        this.moveHelper = new GhastlyEnariaMoveHelper(this);
    }

    /**
     * Initializes the entity AI
     */
    @Override
    protected void initEntityAI()
    {
        // Add a player chase task that makes her follow the player
        this.tasks.addTask(1, new GhastlyEnariaPlayerChase(this));
    }

    /**
     * Sets entity attributes such as max health and movespeed
     */
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EntityGhastlyEnaria.MAX_HEALTH);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntityGhastlyEnaria.FOLLOW_RANGE);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(EntityGhastlyEnaria.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntityGhastlyEnaria.MOVE_SPEED);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EntityGhastlyEnaria.ATTACK_DAMAGE);
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
            this.animHandler.animationsUpdate();
    }

    /**
     * Called every game tick for the entity
     */
    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        // Check every 10 seconds if enaria should be benign or aggressive
        if (this.ticksExisted % PLAYER_BENIGN_CHECK_FREQUENCY == 0)
        {
            int distanceBetweenIslands = AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands();
            EntityPlayer closestPlayer = this.world.getClosestPlayer(this.posX, this.posY, this.posZ, distanceBetweenIslands / 2, false);

            this.setBenign(true);
            /*
            if (closestPlayer == null)
                this.setBenign(true);
            else
                this.setBenign(!closestPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.Enaria));
             */
        }

        // If dance is not active play the animation client side
        if (this.world.isRemote)
            if (this.isBenign())
                if (!this.getAnimationHandler().isAnimationActive("dance"))
                    this.getAnimationHandler().activateAnimation("dance", 0);

        // If a player gets within 3 blocks of enaria send them back to the overworld
        if (!this.world.isRemote)
            if (this.ticksExisted % PLAYER_DISTANCE_CHECK_FREQUENCY == 0)
            {
                EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 3);

                /*
                if (entityPlayer != null && !entityPlayer.isDead)
                    AOTDDimensions.Nightmare.fromDimensionTo(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerDimensionPreTeleport(), ((EntityPlayerMP) entityPlayer));
                 */
            }
    }

    /**
     * Enaria can't be damaged unless the source is falling out of the world
     *
     * @param damageSource The damage source that hurt enaria
     * @param damage The amount of damage done
     * @return True to let the attack go through, false otherwise
     */
    @Override
    public boolean attackEntityFrom(final DamageSource damageSource, float damage)
    {
        if (damageSource == DamageSource.OUT_OF_WORLD)
            return super.attackEntityFrom(damageSource, damage);
        return false;
    }

    /**
     * Writes the benign boolean to the entities NBT
     *
     * @param tagCompound The compound to write to
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean(NBT_BENIGN, this.benign);
    }

    /**
     * Reads the benign boolean from the entities NBT
     *
     * @param tagCompund The compound to read from
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.benign = tagCompund.getBoolean(NBT_BENIGN);
    }

    /**
     * @return False, enaria can't despawn
     */
    @Override
    protected boolean canDespawn()
    {
        return false;
    }

    /**
     * Sets the benign flag, if true enaria will dance, if false she will chase
     *
     * @param benign The benign boolean flag
     */
    public void setBenign(boolean benign)
    {
        this.benign = benign;
    }

    /**
     * Gets the benign flag, if true enaria will dance, if false she will chase
     *
     * @return The benign boolean flag
     */
    public boolean isBenign()
    {
        return this.benign;
    }

    /**
     * The animation handler for the entity
     *
     * @return The GhastlyEnaria animation handler
     */
    @Override
    public AnimationHandler getAnimationHandler()
    {
        return this.animHandler;
    }
}
