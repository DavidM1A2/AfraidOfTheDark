package com.davidm1a2.afraidofthedark.common.entity.werewolf

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
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
import com.davidm1a2.afraidofthedark.common.network.packets.animationPackets.AnimationPacket
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.Pose
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.goal.HurtByTargetGoal
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.LookRandomlyGoal
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.ai.goal.SwimGoal
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
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

    constructor(world: World) : this(ModEntities.WEREWOLF, world)

    init {
        // Ensure this werewolf does not attack anyone yet
        this.dataManager[CAN_ATTACK_ANYONE] = false

        // This werewolf is worth 10xp
        experienceValue = 10
    }

    override fun registerData() {
        super.registerData()
        this.dataManager.register(CAN_ATTACK_ANYONE, false)
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
     * Sets entity attributes such as max health and movespeed
     */
    override fun registerAttributes() {
        super.registerAttributes()
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = MAX_HEALTH
        getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = FOLLOW_RANGE
        getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).baseValue = KNOCKBACK_RESISTANCE
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = MOVE_SPEED
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = ATTACK_DAMAGE
    }

    /**
     * Called every tick to update the entities state
     */
    override fun baseTick() {
        // Call super
        super.baseTick()

        // Show the walking animation if the entity is walking and not biting
        if (world.isRemote) {
            if (motion.x > 0.05 || motion.x < -0.05 || motion.z > 0.05 || motion.z < -0.05) {
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
    override fun onDeath(cause: DamageSource) {
        super.onDeath(cause)

        // Server side processing only
        if (!world.isRemote) {
            // If the cause was from a player we perform further processing
            if (cause is EntityDamageSource) {
                // Test if the killer was a player
                if (cause.getTrueSource() is PlayerEntity) {
                    val killer = cause.getTrueSource() as PlayerEntity
                    val playerResearch = killer.getResearch()

                    // If the player can research the slaying of the wolves achievement do so
                    if (playerResearch.canResearch(ModResearches.SLAYING_OF_THE_WOLVES)) {
                        playerResearch.setResearch(ModResearches.SLAYING_OF_THE_WOLVES, true)
                        playerResearch.sync(killer, true)
                    }

                    // If the player has the slaying of the wolves achievement then test if the player has glass bottles to fill with werewolf blood
                    if (playerResearch.isResearched(ModResearches.SLAYING_OF_THE_WOLVES)) {
                        // If the player is in creative mode or we can clear a glass bottle do so and add 1 werewolf blood
                        if (killer.isCreative || killer.inventory.clearMatchingItems({ it.item == Items.GLASS_BOTTLE }, 1) == 1
                        ) {
                            killer.inventory.addItemStackToInventory(ItemStack(ModItems.WEREWOLF_BLOOD, 1))
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
    override fun attackEntityFrom(damageSource: DamageSource, damage: Float): Boolean {
        // If the damage was 'silver_damage' then we can apply it, otherwise we just do 1 'generic' damage
        return when (damageSource.damageType) {
            ModDamageSources.SILVER_DAMAGE -> {
                super.attackEntityFrom(damageSource, damage)
            }
            // Out of world damage is caused by /kill
            DamageSource.OUT_OF_WORLD.damageType -> {
                super.attackEntityFrom(damageSource, damage)
            }
            else -> {
                super.attackEntityFrom(DamageSource.GENERIC, 1f)
            }
        }
    }

    /**
     * Called when this mob attacks an entity
     *
     * @param entity The entity that was attacked
     * @return True to let the interaction go through, false otherwise
     */
    override fun attackEntityAsMob(entity: Entity): Boolean {
        // Perform the attack first, then process the aftermath
        val attackResult = super.attackEntityAsMob(entity)

        // Server side processing only
        if (!world.isRemote) {
            if (entity is PlayerEntity) {
                // Show all players within 50 blocks the bite animation
                AfraidOfTheDark.packetHandler.sendToAllAround(
                    AnimationPacket(this, "Bite", "Bite"),
                    PacketDistributor.TargetPoint(posX, posY, posZ, 50.0, dimension)
                )

                // If the thing that was attacked was a player test if that player was killed or not
                val playerResearch = entity.getResearch()

                // Don't check 'isDead' because that only gets updated next tick, instead check if HP > 0 for alive
                if (entity.health > 0) {
                    // The player was not killed by the attack unlock the werewolf research if possible
                    if (playerResearch.canResearch(ModResearches.WEREWOLF_EXAMINATION)) {
                        playerResearch.setResearch(ModResearches.WEREWOLF_EXAMINATION, true)
                        playerResearch.sync(entity, true)
                    }
                }
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
        return if (attackTarget == null) {
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
    override fun getAIMoveSpeed(): Float {
        return MOVE_SPEED.toFloat()
    }

    /**
     * @return The eye height of the werewolf that it uses to look around
     */
    override fun getEyeHeight(pose: Pose): Float {
        return 1.3f
    }

    /**
     * Sets the flag telling us if the werewolf can attack anyone or just AOTD players
     *
     * @param attacksAnyone THe flag
     */
    fun setCanAttackAnyone(attacksAnyone: Boolean) {
        this.dataManager[CAN_ATTACK_ANYONE] = attacksAnyone
    }

    /**
     * @return True if the werewolf can attack anyone or false if it can just attack AOTD players
     */
    fun canAttackAnyone(): Boolean {
        return this.dataManager[CAN_ATTACK_ANYONE]
    }

    override fun readAdditional(compound: CompoundNBT) {
        super.readAdditional(compound)
        this.dataManager[CAN_ATTACK_ANYONE] = compound.getBoolean("can_attack_anyone")
    }

    override fun writeAdditional(compound: CompoundNBT) {
        super.writeAdditional(compound)
        compound.putBoolean("can_attack_anyone", this.dataManager[CAN_ATTACK_ANYONE])
    }

    companion object {
        private val CAN_ATTACK_ANYONE = EntityDataManager.createKey(WerewolfEntity::class.java, DataSerializers.BOOLEAN)

        // Constants defining werewolf parameters
        private const val MOVE_SPEED = .43
        private const val AGRO_RANGE = 16.0
        private const val FOLLOW_RANGE = 32.0
        private const val MAX_HEALTH = 20.0
        private const val KNOCKBACK_RESISTANCE = 0.5
        private const val ATTACK_DAMAGE = 20.0
    }
}