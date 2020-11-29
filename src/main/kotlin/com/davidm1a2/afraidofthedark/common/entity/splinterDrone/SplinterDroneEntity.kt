package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation.ActivateActivate
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation.ChargeChannel
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation.IdleChannel
import com.davidm1a2.afraidofthedark.common.network.packets.animationPackets.AnimationPacket
import net.minecraft.entity.EntityType
import net.minecraft.entity.FlyingEntity
import net.minecraft.entity.Pose
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.LookRandomlyGoal
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import net.minecraftforge.fml.network.PacketDistributor

/**
 * The splinter drone entity in gnomish cities
 *
 * @constructor sets mob's size and other properties
 * @param world The world that the splinter drone is in
 * @property animHandler The animation handler used to manage animations
 */
class SplinterDroneEntity(entityType: EntityType<out SplinterDroneEntity>, world: World) : FlyingEntity(entityType, world), IMob, IMCAnimatedModel {
    private val animHandler = AnimationHandler(
        ActivateActivate("Activate", 25.0f, 100, ChannelMode.LINEAR),
        ChargeChannel("Charge", 100.0f, 100, ChannelMode.LINEAR),
        IdleChannel("Idle", 25.0f, 100, ChannelMode.LINEAR)
    )

    init {
        // Set the EXP value to 7
        experienceValue = 7
        // Update our move helper to fly like a ghast
        moveController = SplinterDroneMovementController(this)
        this.dataManager[PLAYED_SPAWN_ANIMATION] = false
    }

    override fun registerData() {
        super.registerData()
        this.dataManager.register(PLAYED_SPAWN_ANIMATION, false)
    }

    /**
     * Sets up the entity's AI tasks
     */
    override fun registerGoals() {
        // Task one is always to face the nearest player
        goalSelector.addGoal(1, LookAtGoal(this, PlayerEntity::class.java, AGRO_RANGE.toFloat()))
        // Task two is to hover over the ground and fly around
        goalSelector.addGoal(2, SplinterDroneHoverGoal(this))
        // Task three is to look idle and look around
        goalSelector.addGoal(3, LookRandomlyGoal(this))
        // Set target tasks for shooting the player
        targetSelector.addGoal(1, SplinterDroneAttackGoal(this))
        // Find the nearest player to target and hit
        targetSelector.addGoal(2, NearestAttackableTargetGoal(this, PlayerEntity::class.java, true))
    }

    /**
     * Sets entity attributes such as max health and movespeed
     */
    override fun registerAttributes() {
        super.registerAttributes()
        // Make sure to register the attack damage attribute for this entity
        attributes.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = MAX_HEALTH
        getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = FOLLOW_RANGE
        getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).baseValue = KNOCKBACK_RESISTANCE
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = MOVE_SPEED
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = ATTACK_DAMAGE
    }

    /**
     * Update animations for this entity when update is called, also kill the entity if it's peaceful
     */
    override fun tick() {
        super.tick()

        // If we're on peaceful and server side kill the entity
        if (!world.isRemote && world.difficulty == Difficulty.PEACEFUL) {
            remove()
        }

        // Animations only update client side
        if (world.isRemote) {
            animHandler.update()
        }
    }

    /**
     * Called every game tick for the entity
     */
    override fun baseTick() {
        // Update any base logic
        super.baseTick()

        // Server side test if the entity has played the spawn animation
        if (!world.isRemote) {
            if (!this.dataManager[PLAYED_SPAWN_ANIMATION]) {
                // If it hasn't played the spawn animation play it to all nearby players
                AfraidOfTheDark.packetHandler.sendToAllAround(
                    AnimationPacket(this, "Activate"),
                    PacketDistributor.TargetPoint(posX, posY, posZ, 50.0, dimension)
                )
                this.dataManager[PLAYED_SPAWN_ANIMATION] = true
            }
        }

        // If we're client side and no animation is active play the idle animation
        if (world.isRemote) {
            if (!animHandler.isAnimationActive("Activate") && !animHandler.isAnimationActive("Charge") && !animHandler.isAnimationActive(
                    "Idle"
                )
            ) {
                animHandler.playAnimation("Idle")
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
            if (cause.trueSource is PlayerEntity) {
                val entityPlayer = cause.trueSource as PlayerEntity
                // If we can unlock the gnomish city research do so
                val playerResearch = entityPlayer.getResearch()
                if (playerResearch.canResearch(ModResearches.GNOMISH_CITY)) {
                    playerResearch.setResearch(ModResearches.GNOMISH_CITY, true)
                    playerResearch.sync(entityPlayer, true)
                }
            }
        }
    }

    override fun spawnDrops(damageSource: DamageSource) {
        // Drop 1 energy core 20% of the time (+20% per looting level)
        if (rand.nextDouble() < 0.2) { // TODO: lootingModifier * 0.2
            entityDropItem(ModItems.POWER_CORE, 1)
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
    override fun getEyeHeight(pose: Pose): Float {
        return 1.5f
    }

    companion object {
        private val PLAYED_SPAWN_ANIMATION = EntityDataManager.createKey(SplinterDroneEntity::class.java, DataSerializers.BOOLEAN)

        // Constants used to define splinter drone properties
        private const val MOVE_SPEED = 0.05
        private const val AGRO_RANGE = 30.0
        private const val FOLLOW_RANGE = 30.0
        private const val MAX_HEALTH = 20.0
        private const val ATTACK_DAMAGE = 2.0
        private const val KNOCKBACK_RESISTANCE = 0.5
    }
}