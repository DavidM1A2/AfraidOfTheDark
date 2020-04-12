/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.animation.AnimationHandlerEnchantedSkeleton
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.item.ItemBladeOfExhumation
import com.davidm1a2.afraidofthedark.common.packets.animationPackets.SyncAnimation
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint

/**
 * Class representing an enchanted skeleton entity
 *
 * @constructor initializes the skeleton based on the world
 * @param world The world the skeleton is spawning into
 * @property animHandler The animation handler used to manage animations
 * @property hasPlayedSpawnAnimation Flag telling us if we have played the spawn animation yet or not
 */
class EntityEnchantedSkeleton(world: World) : EntityMob(world),
    IMCAnimatedModel {
    private val animHandler = AnimationHandlerEnchantedSkeleton()
    private var hasPlayedSpawnAnimation = false

    init {
        // Set the size of the skeleton's hitbox
        setSize(0.8f, 2.0f)
        // Set how much XP the skeleton is worth
        experienceValue = 4
    }

    /**
     * Creates the AI used by hostile or passive entities
     */
    override fun initEntityAI() {
        // Tasks should have a list of AI tasks with a priority associated with them. Lower priority is executed first
        // If the entity can swim and it's in water it must do that otherwise it will skin
        tasks.addTask(1, EntityAISwimming(this))
        // If it's not swimming, test if we can engage in combat, if so do that
        tasks.addTask(2, EntityAIAttackMelee(this, (MOVE_SPEED / 4).toDouble(), false))
        // If the entity isn't attacking then try to walk around
        tasks.addTask(3, EntityAIWander(this, (MOVE_SPEED / 4).toDouble()))
        // If the entity isn't wandering then try to watch whatever entity is nearby
        tasks.addTask(4, EntityAIWatchClosest(this, EntityPlayer::class.java, AGRO_RANGE))
        // If the entity isn't walking, attacking, or watching anything look idle
        tasks.addTask(5, EntityAILookIdle(this))
        // Targeting tasks get executed when the entity wants to attack and select a target
        // If the entity is hurt by a player target that player
        targetTasks.addTask(1, EntityAIHurtByTarget(this, true))
        // If the entity is not hurt find the nearest player to attack
        targetTasks.addTask(2, EntityAINearestAttackableTarget(this, EntityPlayer::class.java, true))
    }

    /**
     * Sets entity attributes such as max health and movespeed
     */
    override fun applyEntityAttributes() {
        super.applyEntityAttributes()
        attributeMap.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).baseValue = MAX_HEALTH.toDouble()
        attributeMap.getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = FOLLOW_RANGE.toDouble()
        attributeMap.getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).baseValue =
            KNOCKBACK_RESISTANCE.toDouble()
        attributeMap.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = MOVE_SPEED.toDouble()
        attributeMap.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = ATTACK_DAMAGE.toDouble()
    }

    /**
     * Update animations for this entity when update is called
     */
    override fun onUpdate() {
        super.onUpdate()
        // Animations only update client side
        if (world.isRemote) {
            animHandler.update()
        }
    }

    /**
     * Called every game tick for the entity
     */
    override fun onEntityUpdate() {
        // Don't forget to call super!
        super.onEntityUpdate()
        // If we're server side
        if (!world.isRemote) {
            // If we haven't played the spawn animation yet, play it now
            if (!hasPlayedSpawnAnimation) {
                // Tell clients to show the spawn animation
                AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(
                    SyncAnimation("Spawn", this),
                    TargetPoint(dimension, posX, posY, posZ, 50.0)
                )

                // Give the skeleton slowness and weakness for 3 seconds with level 100 so it can't do anything while spawning
                addPotionEffect(PotionEffect(Potion.getPotionById(18)!!, 60, 100))
                addPotionEffect(PotionEffect(Potion.getPotionById(2)!!, 60, 100))

                // Set our flag
                hasPlayedSpawnAnimation = true
            }
        }

        // If we're on client side test if we need to show walking animations
        if (world.isRemote) {
            // If spawn and attack are not active test if we can show walking animations
            if (!animHandler.isAnimationActive("Spawn") && !animHandler.isAnimationActive("Attack")) {
                // If the entity is moving show the walking animation
                if (motionX > 0.05 || motionZ > 0.05 || motionX < -0.05 || motionZ < -0.05) {
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
            if (damageSource.trueSource is EntityPlayer) {
                // Grab a reference to the player
                val killer = damageSource.trueSource as EntityPlayer
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
                    if (killer.heldItemMainhand.item is ItemBladeOfExhumation) {
                        world.spawnEntity(
                            EntityItem(
                                world,
                                posX,
                                posY + 1,
                                posZ,
                                ItemStack(ModItems.ENCHANTED_SKELETON_BONE)
                            )
                        )
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
    override fun dropFewItems(wasRecentlyHit: Boolean, lootingModifier: Int) {
        // Drop exactly 3 bones, because 4 bones cause another skeleton to spawn
        dropItem(this.dropItem, 3)

        // Have a 5% chance to drop a heart, increased by 5% per looting level
        if (rand.nextDouble() < 0.05 + 0.05 * lootingModifier) {
            dropItem(ModItems.CURSED_HEART, 1)
        }
    }

    /**
     * Skeletons drop enchanted skeleton bones!
     *
     * @return The enchanted skeleton bone
     */
    override fun getDropItem(): Item {
        return ModItems.ENCHANTED_SKELETON_BONE
    }

    /**
     * @return The handler for all animations for this entity
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
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
     * @param blockIn The block the entity is over
     */
    override fun playStepSound(pos: BlockPos, blockIn: Block) {
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
            this.getActivePotionEffect(Potion.getPotionById(2)!!)?.let {
                if (it.amplifier == 100) {
                    return false
                }
            }

            // Test if the entity is a player or not
            if (entity is EntityPlayer) {
                // Add slowness and weakness 1 to the player if hit by a skeleton
                entity.addPotionEffect(PotionEffect(Potion.getPotionById(2)!!, 80, 0, false, true))
                entity.addPotionEffect(PotionEffect(Potion.getPotionById(18)!!, 80, 0, false, true))
            }

            // Send a packet to the other players telling them the skeleton attacked
            AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(
                SyncAnimation("Attack", this, "Attack", "Spawn"),
                TargetPoint(dimension, posX, posY, posZ, 15.0)
            )
        }
        return super.attackEntityAsMob(entity)
    }

    /**
     * @return The eye height of the skeleton which is used in path finding and looking around
     */
    override fun getEyeHeight(): Float {
        return 1.9f
    }

    companion object {
        // Constants defining skeleton parameters
        private const val MOVE_SPEED = 1.0f
        private const val AGRO_RANGE = 16.0f
        private const val FOLLOW_RANGE = 32.0f
        private const val MAX_HEALTH = 7.0f
        private const val ATTACK_DAMAGE = 4.0f
        private const val KNOCKBACK_RESISTANCE = 0.5f

        // NBT tag for if the skeleton has done the spawn animation yet or not
        private const val NBT_PLAYED_SPAWN_ANIMATION = "played_spawn_animation"
    }
}