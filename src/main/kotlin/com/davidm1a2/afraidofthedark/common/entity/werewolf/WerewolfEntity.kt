package com.davidm1a2.afraidofthedark.common.entity.werewolf

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.hasStartedAOTD
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.entity.werewolf.animation.BiteChannel
import com.davidm1a2.afraidofthedark.common.entity.werewolf.animation.RunChannel
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacket
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.Pose
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.HurtByTargetGoal
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.LookRandomlyGoal
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.ai.goal.SwimGoal
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.SoundEvent
import net.minecraft.world.World
import net.minecraftforge.fml.network.PacketDistributor

/**
 * Class representing a werewolf entity
 *
 * @constructor initializes the werewolf properties
 * @param world The world the werewolf is a part of
 * @property animHandler Animation handler used by the werewolf
 */
class WerewolfEntity(entityType: EntityType<out WerewolfEntity>, world: World) : MonsterEntity(entityType, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(
        BiteChannel("Bite", 50.0f, 21, ChannelMode.LINEAR),
        RunChannel("Run", 60.0f, 32, ChannelMode.LINEAR)
    )

    var canAttackAnyone: Boolean = false

    constructor(world: World) : this(ModEntities.WEREWOLF, world)

    init {
        // This werewolf is worth 10xp
        xpReward = 10
    }

    override fun registerGoals() {
        // First priority is to swim and not drown
        goalSelector.addGoal(1, SwimGoal(this))
        // Second priority is to attack melee if possible
        goalSelector.addGoal(2, MeleeAttackGoal(this, 1.0, false))
        // If we can't attack or swim just wander around
        goalSelector.addGoal(3, WaterAvoidingRandomWalkingGoal(this, MOVE_SPEED * 2))
        // If we don't wander just look at the closest entity
        goalSelector.addGoal(4, LookAtGoal(this, PlayerEntity::class.java, AGRO_RANGE.toFloat()))
        // If nothing else triggers look idle
        goalSelector.addGoal(5, LookRandomlyGoal(this))
        // For our target tasks we first test if we were hurt, and if so target what hurt us
        targetSelector.addGoal(1, HurtByTargetGoal(this))
        // Then introduce our custom werewolf target locator
        targetSelector.addGoal(2, WerewolfTargetLocatorGoal(this, false, 2))
    }

    /**
     * Called every tick to update the entities state
     */
    override fun baseTick() {
        // Call super
        super.baseTick()

        // Show the walking animation if the entity is walking and not biting
        if (level.isClientSide) {
            if (deltaMovement.x > 0.05 || deltaMovement.x < -0.05 || deltaMovement.z > 0.05 || deltaMovement.z < -0.05) {
                if (!animHandler.isAnimationActive("Bite") && !animHandler.isAnimationActive("Run")) {
                    animHandler.playAnimation("Run")
                }
            }
        }
    }

    /**
     * Called when the werewolf dies
     *
     * @param cause The damage source that killed the werewolf
     */
    override fun die(cause: DamageSource) {
        super.die(cause)

        // Server side processing only
        if (!level.isClientSide) {
            // If the cause was from a player we perform further processing
            if (cause is EntityDamageSource) {
                // Test if the killer was a player
                if (cause.entity is PlayerEntity) {
                    val killer = cause.entity as PlayerEntity
                    val playerResearch = killer.getResearch()

                    // If the player has the slaying of the wolves achievement then test if the player has glass bottles to fill with werewolf blood
                    if (playerResearch.isResearched(ModResearches.WEREWOLF_BLOOD)) {
                        // If the player is in creative mode or we can clear a glass bottle do so and add 1 werewolf blood
                        if (killer.isCreative || ItemStackHelper.clearOrCountMatchingItems(
                                killer.inventory,
                                { it.item == Items.GLASS_BOTTLE },
                                1,
                                false
                            ) == 1
                        ) {
                            killer.inventory.add(ItemStack(ModItems.WEREWOLF_BLOOD, 1))
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when the entity is attacked from a damage source
     *
     * @param damageSource The damage source used
     * @param damage       The damage inflicted
     * @return True if the attack went through, false otherwise
     */
    override fun hurt(damageSource: DamageSource, damage: Float): Boolean {
        // If the werewolf takes damage from a player that has not started the mod, agro them
        if (damageSource is EntityDamageSource) {
            val player = damageSource.entity
            if (player is PlayerEntity) {
                if (!player.hasStartedAOTD()) {
                    canAttackAnyone = true
                }
            }
        }

        // If the damage was 'silver_damage' then we can apply it, otherwise we just do 1 'generic' damage. Out of world damage is caused by /kill
        return when (damageSource.msgId) {
            ModDamageSources.SILVER_DAMAGE, DamageSource.OUT_OF_WORLD.msgId -> super.hurt(damageSource, damage)
            else -> super.hurt(DamageSource.GENERIC, 1f)
        }
    }

    /**
     * Called when this mob attacks an entity
     *
     * @param entity The entity that was attacked
     * @return True to let the interaction go through, false otherwise
     */
    override fun doHurtTarget(entity: Entity): Boolean {
        // Perform the attack first, then process the aftermath
        val attackResult = super.doHurtTarget(entity)

        // Server side processing only
        if (!level.isClientSide) {
            if (entity is PlayerEntity) {
                // Show all players within 50 blocks the bite animation
                AfraidOfTheDark.packetHandler.sendToAllAround(
                    AnimationPacket(this, "Bite", "Bite"),
                    PacketDistributor.TargetPoint(x, y, z, 50.0, level.dimension())
                )
            }
        }
        return attackResult
    }

    /**
     * @return the sound this mob makes on death.
     */
    override fun getDeathSound(): SoundEvent {
        return ModSounds.WEREWOLF_DEATH
    }

    /**
     * @return the sound this mob makes when it is hurt.
     */
    override fun getHurtSound(damageSourceIn: DamageSource): SoundEvent {
        return ModSounds.WEREWOLF_HURT
    }

    /**
     * @return The ambient sound of the werewolf, if the werewolf is attacking it is the agro sound otherwise it's the idle sound
     */
    override fun getAmbientSound(): SoundEvent {
        return if (target == null) {
            ModSounds.WEREWOLF_IDLE
        } else {
            ModSounds.WEREWOLF_AGRO
        }
    }

    /**
     * @return the werewolf's volume
     */
    override fun getSoundVolume(): Float {
        return 0.5f
    }

    /**
     * @return Returns the animation handler which manages our animation state
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    /**
     * @return Get the AI movespeed
     */
    override fun getSpeed(): Float {
        return MOVE_SPEED.toFloat()
    }

    /**
     * @return The eye height of the werewolf that it uses to look around
     */
    override fun getEyeHeight(pose: Pose): Float {
        return 1.3f
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        this.canAttackAnyone = compound.getBoolean("can_attack_anyone")
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putBoolean("can_attack_anyone", this.canAttackAnyone)
    }

    companion object {
        // Constants defining werewolf parameters
        private const val MOVE_SPEED = .43
        private const val AGRO_RANGE = 16.0
        private const val FOLLOW_RANGE = 32.0
        private const val MAX_HEALTH = 20.0
        private const val KNOCKBACK_RESISTANCE = 0.5
        private const val ATTACK_DAMAGE = 20.0
        private const val ATTACK_KNOCKBACK = 1.5

        /**
         * Sets entity attributes such as max health and movespeed
         */
        fun buildAttributeModifiers(): AttributeModifierMap.MutableAttribute {
            return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, MOVE_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK, ATTACK_KNOCKBACK)
        }
    }
}