package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation.AnimationHandlerSplinterDrone
import com.davidm1a2.afraidofthedark.common.packets.animationPackets.SyncAnimation
import net.minecraft.entity.EntityFlying
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer
import net.minecraft.entity.ai.EntityAILookIdle
import net.minecraft.entity.ai.EntityAIWatchClosest
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraft.world.EnumDifficulty
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint

/**
 * The splinter drone entity in gnomish cities
 *
 * @constructor sets mob's size and other properties
 * @param world The world that the splinter drone is in
 * @property animHandler The animation handler used to manage animations
 * @property hasPlayedSpawnAnimation Flag telling us if we have played the spawn animation yet or not
 */
class EntitySplinterDrone(world: World) : EntityFlying(world), IMob, IMCAnimatedEntity {
    private val animHandler = AnimationHandlerSplinterDrone(this)
    private var hasPlayedSpawnAnimation = false

    init {
        // The the entity hitbox
        setSize(0.8f, 3.0f)
        // Make the entity immune to fire
        isImmuneToFire = true
        // Set the EXP value to 7
        experienceValue = 7
        // Update our move helper to fly like a ghast
        moveHelper = EntitySplinterDroneMoveHelper(this)
    }

    /**
     * Sets up the entity's AI tasks
     */
    override fun initEntityAI() {
        // Task one is always to face the nearest player
        tasks.addTask(1, EntityAIWatchClosest(this, EntityPlayer::class.java, AGRO_RANGE.toFloat()))
        // Task two is to hover over the ground and fly around
        tasks.addTask(2, EntityAIHoverSplinterDrone(this))
        // Task three is to look idle and look around
        tasks.addTask(3, EntityAILookIdle(this))
        // Set target tasks for shooting the player
        targetTasks.addTask(1, EntityAIAttackSplinterDrone(this))
        // Find the nearest player to target and hit
        targetTasks.addTask(2, EntityAIFindEntityNearestPlayer(this))
    }

    /**
     * Sets entity attributes such as max health and movespeed
     */
    override fun applyEntityAttributes() {
        super.applyEntityAttributes()
        // Make sure to register the attack damage attribute for this entity
        attributeMap.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = MAX_HEALTH
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = FOLLOW_RANGE
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).baseValue = KNOCKBACK_RESISTANCE
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = MOVE_SPEED
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = ATTACK_DAMAGE
    }

    /**
     * Update animations for this entity when update is called, also kill the entity if it's peaceful
     */
    override fun onUpdate() {
        super.onUpdate()

        // If we're on peaceful and server side kill the entity
        if (!world.isRemote && world.difficulty == EnumDifficulty.PEACEFUL) {
            setDead()
        }

        // Animations only update client side
        if (world.isRemote) {
            animHandler.animationsUpdate()
        }
    }

    /**
     * Called every game tick for the entity
     */
    override fun onEntityUpdate() {
        // Update any base logic
        super.onEntityUpdate()

        // Server side test if the entity has played the spawn animation
        if (!world.isRemote) {
            if (!hasPlayedSpawnAnimation) {
                // If it hasn't played the spawn animation play it to all nearby players
                AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(
                    SyncAnimation("Activate", this),
                    TargetPoint(dimension, posX, posY, posZ, 50.0)
                )
                hasPlayedSpawnAnimation = true
            }
        }

        // If we're client side and no animation is active play the idle animation
        if (world.isRemote) {
            if (!animHandler.isAnimationActive("Activate") && !animHandler.isAnimationActive("Charge") && !animHandler.isAnimationActive(
                    "Idle"
                )
            ) {
                animHandler.activateAnimation("Idle", 0f)
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     *
     * @param cause The damage source that killed the skeleton
     */
    override fun onDeath(cause: DamageSource) {
        // Kill the entity
        super.onDeath(cause)

        // Only process server side
        if (!world.isRemote) {
            // If a player killed the entity unlock the gnomish city research
            if (cause.trueSource is EntityPlayer) {
                val entityPlayer = cause.trueSource as EntityPlayer
                // If we can unlock the gnomish city research do so
                val playerResearch = entityPlayer.getResearch()
                if (playerResearch.canResearch(ModResearches.GNOMISH_CITY)) {
                    playerResearch.setResearch(ModResearches.GNOMISH_CITY, true)
                    playerResearch.sync(entityPlayer, true)
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
    override fun dropFewItems(wasRecentlyHit: Boolean, lootingModifier: Int) {
        // Drop 1 energy core 20% of the time (+20% per looting level)
        if (rand.nextDouble() < 0.2 + lootingModifier * 0.2)
        {
            dropItem(ModItems.POWER_CORE, 1)
        }
    }

    /**
     * @return The handler for all animations for this entity
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    /**
     * @return The eye height of the splinter drone which is up towards the top
     */
    override fun getEyeHeight(): Float {
        return 1.5f
    }

    /**
     * Writes the entity to NBT, must also save our flag in NBT
     *
     * @param tagCompound The compound to write to
     */
    override fun writeEntityToNBT(tagCompound: NBTTagCompound) {
        tagCompound.setBoolean(NBT_PLAYED_SPAWN_ANIMATION, hasPlayedSpawnAnimation)
        super.writeEntityToNBT(tagCompound)
    }

    /**
     * Reads the entity to NBT, must also read our flag in NBT
     *
     * @param tagCompound The compound to read from
     */
    override fun readEntityFromNBT(tagCompound: NBTTagCompound) {
        hasPlayedSpawnAnimation = tagCompound.getBoolean(NBT_PLAYED_SPAWN_ANIMATION)
        super.readEntityFromNBT(tagCompound)
    }

    companion object {
        // Constants used to define splinter drone properties
        private const val MOVE_SPEED = 0.05
        private const val AGRO_RANGE = 30.0
        private const val FOLLOW_RANGE = 30.0
        private const val MAX_HEALTH = 20.0
        private const val ATTACK_DAMAGE = 2.0
        private const val KNOCKBACK_RESISTANCE = 0.5

        // NBT tag for if the skeleton has done the spawn animation yet or not
        private const val NBT_PLAYED_SPAWN_ANIMATION = "played_spawn_animation"
    }
}