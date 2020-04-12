package com.davidm1a2.afraidofthedark.common.entity.werewolf

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.werewolf.animation.AnimationHandlerWerewolf
import com.davidm1a2.afraidofthedark.common.packets.animationPackets.SyncAnimation
import net.minecraft.entity.Entity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.*
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.SoundEvent
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint

/**
 * Class representing a werewolf entity
 *
 * @constructor initializes the werewolf properties
 * @param world The world the werewolf is a part of
 * @property animHandler Animation handler used by the werewolf
 * @property attacksAnyone Flag telling the werewolf if it is allowed to attack anyone or just players that have started AOTD
 */
class EntityWerewolf(world: World) : EntityMob(world),
    IMCAnimatedModel {
    private val animHandler = AnimationHandlerWerewolf()
    private var attacksAnyone: Boolean

    init {
        // Set the hitbox size
        setSize(1.1f, 1.4f)

        // Ensure this werewolf does not attack anyone yet
        attacksAnyone = false

        // This werewolf is worth 10xp
        experienceValue = 10
    }

    override fun initEntityAI() {
        // First priority is to swim and not drown
        tasks.addTask(1, EntityAISwimming(this))
        // Second priority is to attack melee if possible
        tasks.addTask(2, EntityAIAttackMelee(this, 1.0, false))
        // If we can't attack or swim just wander around
        tasks.addTask(3, EntityAIWander(this, MOVE_SPEED * 2))
        // If we don't wander just look at the closest entity
        tasks.addTask(4, EntityAIWatchClosest(this, EntityPlayer::class.java, AGRO_RANGE.toFloat()))
        // If nothing else triggers look idle
        tasks.addTask(5, EntityAILookIdle(this))
        // For our target tasks we first test if we were hurt, and if so target what hurt us
        targetTasks.addTask(1, EntityAIHurtByTarget(this, true))
        // Then introduce our custom werewolf target locator
        targetTasks.addTask(2, CustomWerewolfTargetLocator(this, false, 2))
    }

    /**
     * Sets entity attributes such as max health and movespeed
     */
    override fun applyEntityAttributes() {
        super.applyEntityAttributes()
        attributeMap.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).baseValue = MAX_HEALTH
        attributeMap.getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = FOLLOW_RANGE
        attributeMap.getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).baseValue = KNOCKBACK_RESISTANCE
        attributeMap.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = MOVE_SPEED
        attributeMap.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = ATTACK_DAMAGE
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
     * Called every tick to update the entities state
     */
    override fun onEntityUpdate() {
        // Call super
        super.onEntityUpdate()

        // Show the walking animation if the entity is walking and not biting
        if (world.isRemote) {
            if (motionX > 0.05 || motionX < -0.05 || motionZ > 0.05 || motionZ < -0.05) {
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
                if (cause.getTrueSource() is EntityPlayer) {
                    val killer = cause.getTrueSource() as EntityPlayer
                    val playerResearch = killer.getResearch()

                    // If the player can research the slaying of the wolves achievement do so
                    if (playerResearch.canResearch(ModResearches.SLAYING_OF_THE_WOLVES)) {
                        playerResearch.setResearch(ModResearches.SLAYING_OF_THE_WOLVES, true)
                        playerResearch.sync(killer, true)
                    }

                    // If the player has the slaying of the wolves achievement then test if the player has glass bottles to fill with werewolf blood
                    if (playerResearch.isResearched(ModResearches.SLAYING_OF_THE_WOLVES)) {
                        // If the player is in creative mode or we can clear a glass bottle do so and add 1 werewolf blood
                        if (killer.isCreative || killer.inventory.clearMatchingItems(
                                Items.GLASS_BOTTLE,
                                0,
                                1,
                                null
                            ) == 1
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
        return if (damageSource.damageType == ModDamageSources.SILVER_DAMAGE) {
            super.attackEntityFrom(damageSource, damage)
        } else {
            super.attackEntityFrom(DamageSource.GENERIC, 1f)
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
            if (entity is EntityPlayer) {
                // Show all players within 50 blocks the bite animation
                AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(
                    SyncAnimation("Bite", this, "Bite"),
                    TargetPoint(dimension, posX, posY, posZ, 50.0)
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
     * Reads this entity from an NBT compound
     *
     * @param nbtTagCompound The compound to read from
     */
    override fun readEntityFromNBT(nbtTagCompound: NBTTagCompound) {
        attacksAnyone = nbtTagCompound.getBoolean(NBT_CAN_ATTACK_ANYONE)
        super.readEntityFromNBT(nbtTagCompound)
    }

    /**
     * Writes this entity to an NBT compound
     *
     * @param nbtTagCompound The compound to write to
     */
    override fun writeEntityToNBT(nbtTagCompound: NBTTagCompound) {
        nbtTagCompound.setBoolean(NBT_CAN_ATTACK_ANYONE, attacksAnyone)
        super.writeEntityToNBT(nbtTagCompound)
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
    override fun getEyeHeight(): Float {
        return 1.3f
    }

    /**
     * Sets the flag telling us if the werewolf can attack anyone or just AOTD players
     *
     * @param attacksAnyone THe flag
     */
    fun setCanAttackAnyone(attacksAnyone: Boolean) {
        this.attacksAnyone = attacksAnyone
    }

    /**
     * @return True if the werewolf can attack anyone or false if it can just attack AOTD players
     */
    fun canAttackAnyone(): Boolean {
        return attacksAnyone
    }

    companion object {
        // Constants defining werewolf parameters
        private const val MOVE_SPEED = .43
        private const val AGRO_RANGE = 16.0
        private const val FOLLOW_RANGE = 32.0
        private const val MAX_HEALTH = 20.0
        private const val KNOCKBACK_RESISTANCE = 0.5
        private const val ATTACK_DAMAGE = 20.0

        // NBT tag for if the werewolf can attack anyone or just players that have started AOTD
        private const val NBT_CAN_ATTACK_ANYONE = "can_attack_anyone"
    }
}