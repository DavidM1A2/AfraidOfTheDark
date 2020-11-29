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
import com.davidm1a2.afraidofthedark.common.item.BladeOfExhumationItem
import com.davidm1a2.afraidofthedark.common.network.packets.animationPackets.AnimationPacket
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.Pose
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundEvent
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.network.PacketDistributor

/**
 * Class representing an enchanted skeleton entity
 *
 * @constructor initializes the skeleton based on the world
 * @param world The world the skeleton is spawning into
 * @property animHandler The animation handler used to manage animations
 */
class EnchantedSkeletonEntity(entityType: EntityType<out EnchantedSkeletonEntity>, world: World) : MonsterEntity(entityType, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(
        WalkChannel("Walk", 20.0f, 40, ChannelMode.LINEAR),
        AttackChannel("Attack", 30.0f, 20, ChannelMode.LINEAR),
        SpawnChannel("Spawn", 20.0f, 40, ChannelMode.LINEAR),
        IdleChannel("Idle", 10.0f, 20, ChannelMode.LOOP)
    )

    init {
        // Set how much XP the skeleton is worth
        experienceValue = 4
    }

    /**
     * Initialize dataManager
     */
    override fun registerData() {
        super.registerData()
        // Set the spell to a random one
        this.dataManager.register(PLAYED_SPAWN_ANIMATION, false)
    }

    /**
     * Creates the AI used by hostile or passive entities
     */
    override fun registerGoals() {
        // Tasks should have a list of AI tasks with a priority associated with them. Lower priority is executed first
        // If the entity can swim and it's in water it must do that otherwise it will skin
        goalSelector.addGoal(1, SwimGoal(this))
        // If it's not swimming, test if we can engage in combat, if so do that
        goalSelector.addGoal(2, MeleeAttackGoal(this, (MOVE_SPEED / 4).toDouble(), false))
        // If the entity isn't attacking then try to walk around
        goalSelector.addGoal(3, RandomWalkingGoal(this, (MOVE_SPEED / 4).toDouble()))
        // If the entity isn't wandering then try to watch whatever entity is nearby
        goalSelector.addGoal(4, LookAtGoal(this, PlayerEntity::class.java, AGRO_RANGE))
        // If the entity isn't walking, attacking, or watching anything look idle
        goalSelector.addGoal(5, LookRandomlyGoal(this))
        // Targeting tasks get executed when the entity wants to attack and select a target
        // If the entity is hurt by a player target that player
        targetSelector.addGoal(1, HurtByTargetGoal(this))
        // If the entity is not hurt find the nearest player to attack
        targetSelector.addGoal(2, NearestAttackableTargetGoal(this, PlayerEntity::class.java, true))
    }

    /**
     * Sets entity attributes such as max health and movespeed
     */
    override fun registerAttributes() {
        super.registerAttributes()
        attributes.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH)?.baseValue = MAX_HEALTH.toDouble()
        attributes.getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE)?.baseValue = FOLLOW_RANGE.toDouble()
        attributes.getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE)?.baseValue = KNOCKBACK_RESISTANCE.toDouble()
        attributes.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED)?.baseValue = MOVE_SPEED.toDouble()
        attributes.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE)?.baseValue = ATTACK_DAMAGE.toDouble()
    }

    /**
     * Update animations for this entity when update is called
     */
    override fun tick() {
        super.tick()
        // Animations only update client side
        if (world.isRemote) {
            animHandler.update()
        }
    }

    /**
     * Called every game tick for the entity
     */
    override fun baseTick() {
        // Don't forget to call super!
        super.baseTick()
        // If we're server side
        if (!world.isRemote) {
            // If we haven't played the spawn animation yet, play it now
            if (!dataManager[PLAYED_SPAWN_ANIMATION]) {
                // Tell clients to show the spawn animation
                AfraidOfTheDark.packetHandler.sendToAllAround(
                    AnimationPacket(this, "Spawn"),
                    PacketDistributor.TargetPoint(posX, posY, posZ, 50.0, dimension)
                )

                // Give the skeleton slowness and weakness for 3 seconds with level 100 so it can't do anything while spawning
                addPotionEffect(EffectInstance(Effects.SLOWNESS, 60, 100))
                addPotionEffect(EffectInstance(Effects.WEAKNESS, 60, 100))

                // Set our flag
                dataManager[PLAYED_SPAWN_ANIMATION] = true
            }
        }

        // If we're on client side test if we need to show walking animations
        if (world.isRemote) {
            // If spawn and attack are not active test if we can show walking animations
            if (!animHandler.isAnimationActive("Spawn") && !animHandler.isAnimationActive("Attack")) {
                // If the entity is moving show the walking animation
                if (motion.x > 0.05 || motion.z > 0.05 || motion.x < -0.05 || motion.z < -0.05) {
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
    override fun onDeath(damageSource: DamageSource) {
        // Call super
        super.onDeath(damageSource)

        // Server side processing only
        if (!world.isRemote) {
            // Test if a player killed the skeleton
            if (damageSource.trueSource is PlayerEntity) {
                // Grab a reference to the player
                val killer = damageSource.trueSource as PlayerEntity
                // Grab the player's research
                val playerResearch = killer.getResearch()
                // If the player can research the blade of exhumation research give him the research
                if (playerResearch.canResearch(ModResearches.BLADE_OF_EXHUMATION)) {
                    // Unlock the research for the player
                    playerResearch.setResearch(ModResearches.BLADE_OF_EXHUMATION, true)
                    playerResearch.sync(killer, true)
                }

                // If the blade of exhumation research is researched and the player is using a blade of exhumation drop one extra enchanted skeleton bone
                if (playerResearch.isResearched(ModResearches.BLADE_OF_EXHUMATION)) {
                    if (killer.heldItemMainhand.item is BladeOfExhumationItem) {
                        entityDropItem(ItemStack(ModItems.ENCHANTED_SKELETON_BONE), 1f)
                    }
                }
            }
        }
    }

    /**
     * Called to drop items on the ground after the skeleton dies
     *
     * @param damageSource The damage source
     */
    override fun spawnDrops(damageSource: DamageSource) {
        // Drop exactly 3 bones, because 4 bones cause another skeleton to spawn
        entityDropItem(ItemStack(ModItems.ENCHANTED_SKELETON_BONE, 3), 1f)

        // Have a 5% chance to drop a heart, increased by 5% per looting level
        if (rand.nextDouble() < 0.05) { // TODO fix 0.05 * lootingModifier
            entityDropItem(ModItems.CURSED_HEART, 1)
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
        return SoundEvents.ENTITY_SKELETON_HURT
    }

    /**
     * @return Returns the sound this mob makes on death.
     */
    override fun getDeathSound(): SoundEvent {
        return SoundEvents.ENTITY_SKELETON_DEATH
    }

    /**
     * Plays the sound the entity makes when moving
     *
     * @param pos     The position the entity is at
     * @param state The block the entity is over
     */
    override fun playStepSound(pos: BlockPos, state: BlockState) {
        playSound(SoundEvents.ENTITY_SKELETON_STEP, 0.15f, 1.0f)
    }

    /**
     * Called when the skeleton attacks an entity
     *
     * @param entity The entity attacked
     * @return True if the attack goes through, false otherwise
     */
    override fun attackEntityAsMob(entity: Entity): Boolean {
        // Server side processing the hit only
        if (!entity.world.isRemote) {
            // If the entity has slowness 100, then it is still spawning so it can't attack
            this.getActivePotionEffect(Effects.SLOWNESS)?.let {
                if (it.amplifier == 100) {
                    return false
                }
            }

            // Test if the entity is a player or not
            if (entity is PlayerEntity) {
                // Add slowness and weakness 1 to the player if hit by a skeleton
                entity.addPotionEffect(EffectInstance(Effects.SLOWNESS, 80, 0, false, true))
                entity.addPotionEffect(EffectInstance(Effects.WEAKNESS, 80, 0, false, true))
            }

            // Send a packet to the other players telling them the skeleton attacked
            AfraidOfTheDark.packetHandler.sendToAllAround(
                AnimationPacket(this, "Attack", "Attack", "Spawn"),
                PacketDistributor.TargetPoint(posX, posY, posZ, 15.0, dimension)
            )
        }
        return super.attackEntityAsMob(entity)
    }

    /**
     * @return The eye height of the skeleton which is used in path finding and looking around
     */
    override fun getEyeHeight(pose: Pose): Float {
        return 1.9f
    }

    override fun readAdditional(compound: CompoundNBT) {
        super.readAdditional(compound)
        this.dataManager[PLAYED_SPAWN_ANIMATION] = compound.getBoolean("played_spawn_animation")
    }

    override fun writeAdditional(compound: CompoundNBT) {
        super.writeAdditional(compound)
        compound.putBoolean("played_spawn_animation", this.dataManager[PLAYED_SPAWN_ANIMATION])
    }

    companion object {
        private val PLAYED_SPAWN_ANIMATION = EntityDataManager.createKey(EnchantedSkeletonEntity::class.java, DataSerializers.BOOLEAN)

        // Constants defining skeleton parameters
        private const val MOVE_SPEED = 1.0f
        private const val AGRO_RANGE = 16.0f
        private const val FOLLOW_RANGE = 32.0f
        private const val MAX_HEALTH = 7.0f
        private const val ATTACK_DAMAGE = 4.0f
        private const val KNOCKBACK_RESISTANCE = 0.5f
    }
}