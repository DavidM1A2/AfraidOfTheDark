package com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.animation.AttackChannel
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.animation.IdleChannel
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.animation.SpawnChannel
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.animation.WalkChannel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.item.BoneSwordItem
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacket
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.Pose
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.HurtByTargetGoal
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.LookRandomlyGoal
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal
import net.minecraft.entity.ai.goal.RandomWalkingGoal
import net.minecraft.entity.ai.goal.SwimGoal
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundEvent
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.entity.monster.Monster
import net.minecraftforge.fml.network.PacketDistributor

/**
 * Class representing an enchanted skeleton entity
 *
 * @constructor initializes the skeleton based on the world
 * @param world The world the skeleton is spawning into
 * @property animHandler The animation handler used to manage animations
 */
class EnchantedSkeletonEntity(entityType: EntityType<out EnchantedSkeletonEntity>, world: World) : Monster(entityType, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(WALK_CHANNEL, ATTACK_CHANNEL, SPAWN_CHANNEL, IDLE_CHANNEL)
    private var playedSpawnAnimation = false

    init {
        // Set how much XP the skeleton is worth
        xpReward = 4
    }

    /**
     * Creates the AI used by hostile or passive entities
     */
    override fun registerGoals() {
        // Tasks should have a list of AI tasks with a priority associated with them. Lower priority is executed first
        // If the entity can swim and it's in water it must do that otherwise it will skin
        goalSelector.addGoal(1, SwimGoal(this))
        // If it's not swimming, test if we can engage in combat, if so do that
        goalSelector.addGoal(2, MeleeAttackGoal(this, MOVE_SPEED / 4, false))
        // If the entity isn't attacking then try to walk around
        goalSelector.addGoal(3, RandomWalkingGoal(this, MOVE_SPEED / 4))
        // If the entity isn't wandering then try to watch whatever entity is nearby
        goalSelector.addGoal(4, LookAtGoal(this, PlayerEntity::class.java, AGRO_RANGE.toFloat()))
        // If the entity isn't walking, attacking, or watching anything look idle
        goalSelector.addGoal(5, LookRandomlyGoal(this))
        // Targeting tasks get executed when the entity wants to attack and select a target
        // If the entity is hurt by a player target that player
        targetSelector.addGoal(1, HurtByTargetGoal(this))
        // If the entity is not hurt find the nearest player to attack
        targetSelector.addGoal(2, NearestAttackableTargetGoal(this, PlayerEntity::class.java, true))
    }

    /**
     * Called every game tick for the entity
     */
    override fun baseTick() {
        // Don't forget to call super!
        super.baseTick()
        // If we're server side
        if (!level.isClientSide) {
            // If we haven't played the spawn animation yet, play it now
            if (!playedSpawnAnimation) {
                // Tell clients to show the spawn animation
                AfraidOfTheDark.packetHandler.sendToAllAround(
                    AnimationPacket(this, "Spawn"),
                    PacketDistributor.TargetPoint(x, y, z, 50.0, level.dimension())
                )

                // Give the skeleton slowness and weakness for 3 seconds with level 100 so it can't do anything while spawning
                addEffect(EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 100))
                addEffect(EffectInstance(Effects.WEAKNESS, 60, 100))

                // Set our flag
                playedSpawnAnimation = true
            }
        }

        // If we're on client side test if we need to show walking animations
        if (level.isClientSide) {
            // If spawn and attack are not active test if we can show walking animations
            if (!animHandler.isAnimationActive("Spawn") && !animHandler.isAnimationActive("Attack")) {
                // If the entity is moving show the walking animation
                if (deltaMovement.x > 0.05 || deltaMovement.z > 0.05 || deltaMovement.x < -0.05 || deltaMovement.z < -0.05) {
                    if (!animHandler.isAnimationActive("Walk")) {
                        animHandler.playAnimation("Walk")
                    }
                } else {
                    if (!animHandler.isAnimationActive("Idle")) {
                        animHandler.playAnimation("Idle")
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
    override fun die(damageSource: DamageSource) {
        // Call super
        super.die(damageSource)

        // Server side processing only
        if (!level.isClientSide) {
            // Test if a player killed the skeleton
            if (damageSource.entity is PlayerEntity) {
                // Grab a reference to the player
                val killer = damageSource.entity as PlayerEntity
                // Grab the player's research
                val playerResearch = killer.getResearch()

                // If the blade of exhumation research is researched and the player is using a blade of exhumation drop one extra enchanted skeleton bone
                if (playerResearch.isResearched(ModResearches.BONE_SWORD)) {
                    if (killer.mainHandItem.item is BoneSwordItem) {
                        level.addFreshEntity(ItemEntity(level, x, y, z, ItemStack(ModItems.ENCHANTED_SKELETON_BONE)))
                    }
                }
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
     * @param damageSourceIn The damage source that the entity was hit by
     * @return the sound this mob makes when it is hurt.
     */
    override fun getHurtSound(damageSourceIn: DamageSource): SoundEvent {
        return SoundEvents.SKELETON_HURT
    }

    /**
     * @return Returns the sound this mob makes on death.
     */
    override fun getDeathSound(): SoundEvent {
        return SoundEvents.SKELETON_DEATH
    }

    /**
     * Plays the sound the entity makes when moving
     *
     * @param pos     The position the entity is at
     * @param state The block the entity is over
     */
    override fun playStepSound(pos: BlockPos, state: BlockState) {
        playSound(SoundEvents.SKELETON_STEP, 0.15f, 1.0f)
    }

    /**
     * Called when the skeleton attacks an entity
     *
     * @param entity The entity attacked
     * @return True if the attack goes through, false otherwise
     */
    override fun doHurtTarget(entity: Entity): Boolean {
        // Server side processing the hit only
        if (!entity.level.isClientSide) {
            // If the entity has slowness 100, then it is still spawning so it can't attack
            this.getEffect(Effects.MOVEMENT_SLOWDOWN)?.let {
                if (it.amplifier == 100) {
                    return false
                }
            }

            // Test if the entity is a player or not
            if (entity is PlayerEntity) {
                // Add slowness and weakness 1 to the player if hit by a skeleton
                entity.addEffect(EffectInstance(Effects.MOVEMENT_SLOWDOWN, 80, 0, false, true))
                entity.addEffect(EffectInstance(Effects.WEAKNESS, 80, 0, false, true))
            }

            // Send a packet to the other players telling them the skeleton attacked
            AfraidOfTheDark.packetHandler.sendToAllAround(
                AnimationPacket(this, "Attack", "Attack", "Spawn"),
                PacketDistributor.TargetPoint(x, y, z, 15.0, level.dimension())
            )
        }
        return super.doHurtTarget(entity)
    }

    /**
     * @return The eye height of the skeleton which is used in path finding and looking around
     */
    override fun getEyeHeight(pose: Pose): Float {
        return 1.9f
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
        // Constants defining skeleton parameters
        private const val MOVE_SPEED = 1.0
        private const val AGRO_RANGE = 16.0
        private const val FOLLOW_RANGE = 32.0
        private const val MAX_HEALTH = 7.0
        private const val ATTACK_DAMAGE = 4.0
        private const val KNOCKBACK_RESISTANCE = 0.5
        private const val ATTACK_KNOCKBACK = 0.0

        private val WALK_CHANNEL = WalkChannel("Walk", 20.0f, 40, ChannelMode.LINEAR)
        private val ATTACK_CHANNEL = AttackChannel("Attack", 30.0f, 20, ChannelMode.LINEAR)
        private val SPAWN_CHANNEL = SpawnChannel("Spawn", 20.0f, 40, ChannelMode.LINEAR)
        private val IDLE_CHANNEL = IdleChannel("Idle", 10.0f, 20, ChannelMode.LOOP)

        /**
         * Gives the enchanted skeleton entity attributes like damage and movespeed
         */
        fun buildAttributeModifiers(): AttributeModifierMap.MutableAttribute {
            return MonsterEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, MOVE_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK, ATTACK_KNOCKBACK)
        }
    }
}