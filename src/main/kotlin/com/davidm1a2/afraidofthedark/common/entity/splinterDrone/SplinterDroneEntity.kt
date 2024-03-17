package com.davidm1a2.afraidofthedark.common.entity.splinterDrone

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation.ActivateChannel
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation.ChargeChannel
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.animation.IdleChannel
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacket
import net.minecraft.entity.EntityType
import net.minecraft.entity.FlyingEntity
import net.minecraft.entity.MobEntity
import net.minecraft.entity.Pose
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.LookRandomlyGoal
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.world.World
import net.minecraft.world.entity.FlyingMob
import net.minecraftforge.fml.network.PacketDistributor

/**
 * The splinter drone entity in forbidden cities
 *
 * @constructor sets mob's size and other properties
 * @param world The world that the splinter drone is in
 * @property animHandler The animation handler used to manage animations
 */
class SplinterDroneEntity(entityType: EntityType<out SplinterDroneEntity>, world: World) : FlyingMob(entityType, world), IMob, IMCAnimatedModel {
    private val animHandler = AnimationHandler(ACTIVATE_CHANNEL, CHARGE_CHANNEL, IDLE_CHANNEL)
    private var playedSpawnAnimation = false

    constructor(world: World) : this(ModEntities.SPLINTER_DRONE, world)

    init {
        // Set the EXP value to 7
        xpReward = 7
        // Update our move helper to fly like a ghast
        moveControl = SplinterDroneMovementController(this)
    }

    /**
     * Sets up the entity's AI tasks
     */
    override fun registerGoals() {
        // First task is shooting the target if possible
        goalSelector.addGoal(0, SplinterDroneAttackGoal(this))
        // Task one is always to face the nearest player
        goalSelector.addGoal(1, LookAtGoal(this, PlayerEntity::class.java, AGRO_RANGE.toFloat()))
        // Task two is to hover over the ground and fly around
        goalSelector.addGoal(2, SplinterDroneHoverGoal(this))
        // Task three is to look idle and look around
        goalSelector.addGoal(3, LookRandomlyGoal(this))
        // Find the nearest player to target
        targetSelector.addGoal(0, NearestAttackableTargetGoal(this, PlayerEntity::class.java, true))
    }

    /**
     * Called every game tick for the entity
     */
    override fun baseTick() {
        // Update any base logic
        super.baseTick()

        // Server side test if the entity has played the spawn animation
        if (!level.isClientSide) {
            if (!this.playedSpawnAnimation) {
                // If it hasn't played the spawn animation play it to all nearby players
                AfraidOfTheDark.packetHandler.sendToAllAround(
                    AnimationPacket(this, "Activate"),
                    PacketDistributor.TargetPoint(x, y, z, 50.0, level.dimension())
                )
                this.playedSpawnAnimation = true
            }
        }

        // If we're client side and no animation is active play the idle animation
        if (level.isClientSide) {
            if (!animHandler.isAnimationActive("Activate") && !animHandler.isAnimationActive("Charge") && !animHandler.isAnimationActive("Idle")) {
                animHandler.playAnimation("Idle")
            }
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

    override fun shouldDespawnInPeaceful(): Boolean {
        return true
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        this.playedSpawnAnimation = if (compound.contains("played_spawn_animation")) {
            compound.getBoolean("played_spawn_animation")
        } else {
            false
        }
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putBoolean("played_spawn_animation", this.playedSpawnAnimation)
    }

    companion object {
        // Constants used to define splinter drone properties
        private const val MOVE_SPEED = 0.05
        private const val AGRO_RANGE = 30.0
        private const val FOLLOW_RANGE = 30.0
        private const val MAX_HEALTH = 20.0
        private const val ATTACK_DAMAGE = 2.0
        private const val KNOCKBACK_RESISTANCE = 0.5
        private const val ATTACK_KNOCKBACK = 0.0

        private val ACTIVATE_CHANNEL = ActivateChannel("Activate", 25.0f, 100, ChannelMode.LINEAR)
        private val CHARGE_CHANNEL = ChargeChannel("Charge", 100.0f, 100, ChannelMode.LINEAR)
        private val IDLE_CHANNEL = IdleChannel("Idle", 25.0f, 100, ChannelMode.LINEAR)

        /**
         * Sets entity attributes such as max health and movespeed
         */
        fun buildAttributeModifiers(): AttributeModifierMap.MutableAttribute {
            // Make sure to register the attack damage attribute for this entity
            return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, MOVE_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK, ATTACK_KNOCKBACK)
        }
    }
}