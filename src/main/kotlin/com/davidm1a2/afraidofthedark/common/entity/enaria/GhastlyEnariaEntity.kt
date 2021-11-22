package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.DanceChannel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.FlyingEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import java.util.UUID

/**
 * Class representing the ghastly enaria entity
 *
 * @constructor sets the ghastly enaria entity properties
 * @property animHandler The animation handler used to manage animations
 */
class GhastlyEnariaEntity(entityType: EntityType<out GhastlyEnariaEntity>, world: World) : FlyingEntity(entityType, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(DanceChannel("dance", 30.0f, 300, ChannelMode.LINEAR))

    private var touchedPlayer: UUID? = null

    init {
        // The name of the entity, will be bold and red
        this.customName = StringTextComponent("§c§lGhastly Enaria")
        // Enable noclip so enaria can go through walls
        noPhysics = true
        // Add a custom move helper that moves her through walls
        moveControl = GhastlyEnariaMovementController(this)
    }

    /**
     * Initialize dataManager. Anything registered here is automatically synced from Server -> Client
     */
    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(IS_BENIGN, true)
    }

    /**
     * Initializes the entity AI
     */
    override fun registerGoals() {
        // Add a player chase task that makes her follow the player
        goalSelector.addGoal(1, GhastlyEnariaPlayerChaseGoal(this))
    }

    /**
     * Called every game tick for the entity
     */
    override fun baseTick() {
        super.baseTick()

        // Check == 20 instead of == 0 so the player can spawn and that way we don't accidentally check before a player joins
        // the world
        if (tickCount % PLAYER_BENIGN_CHECK_FREQUENCY == 20.0) {
            // Grab the distance between the nightamre islands
            val distanceBetweenIslands = Constants.DISTANCE_BETWEEN_ISLANDS
            // Grab the closest player
            val closestPlayer = level.getNearestPlayer(x, y, z, distanceBetweenIslands / 2.toDouble(), false)
            // If the closest player is null enaria will be benign
            if (closestPlayer == null) {
                setBenign(true)
            } else {
                setBenign(!closestPlayer.getResearch().isResearched(ModResearches.ARCH_SORCERESS))
            }
        }

        // If dance is not active play the animation client side
        if (level.isClientSide) {
            if (isBenign()) {
                if (!this.animHandler.isAnimationActive("dance")) {
                    this.animHandler.playAnimation("dance")
                }
            }
        }

        // If a player gets within 3 blocks of enaria send them back to the overworld
        if (!level.isClientSide) {
            if (tickCount % PLAYER_DISTANCE_CHECK_FREQUENCY == 0.0) {
                // Grab the closest player within 3 blocks
                val entityPlayer = level.getNearestPlayer(this, 3.0)
                // Make sure the player is valid and not dead
                if (entityPlayer != null && entityPlayer.isAlive) {
                    // Don't TP The player from here or we get an exception. Let the GhastlyEnariaTileEntity do that for us
                    this.touchedPlayer = entityPlayer.uuid
                }
            }
        }
    }

    /**
     * Enaria can't be damaged unless the source is falling out of the world
     *
     * @param damageSource The damage source that hurt enaria
     * @param damage       The amount of damage done
     * @return True to let the attack go through, false otherwise
     */
    override fun hurt(damageSource: DamageSource, damage: Float): Boolean {
        return if (damageSource == DamageSource.OUT_OF_WORLD) {
            super.hurt(damageSource, damage)
        } else false
    }

    /**
     * Gets the name of the entity to show above it
     *
     * @return Red and bold nametag
     */
    override fun getDisplayName(): ITextComponent {
        return this.customName!!
    }

    override fun checkDespawn() {
        // Can't despawn
    }

    /**
     * @return False, enaria can't ride any entities
     */
    override fun canRide(entityIn: Entity): Boolean {
        return false
    }

    /**
     * Gets the benign flag, if true enaria will dance, if false she will chase
     *
     * @return The benign boolean flag
     */
    fun isBenign(): Boolean {
        return entityData[IS_BENIGN]
    }

    /**
     * Sets the benign flag, if true enaria will dance, if false she will chase
     *
     * @param benign The benign boolean flag
     */
    fun setBenign(benign: Boolean) {
        entityData[IS_BENIGN] = benign
        // If we're client side stop playing the dance animation
        if (level.isClientSide && !benign) {
            animHandler.stopAnimation("dance")
        }
    }

    fun getTouchedPlayer(): UUID? {
        return this.touchedPlayer
    }

    fun clearTouchedPlayer() {
        this.touchedPlayer = null
    }

    /**
     * Ghastly Enaria can't be pushed by fluid
     *
     * @return false
     */
    override fun isPushedByFluid(): Boolean {
        return false
    }

    /**
     * The animation handler for the entity
     *
     * @return The GhastlyEnaria animation handler
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    override fun getAddEntityPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        this.entityData[IS_BENIGN] = compound.getBoolean("is_benign")
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.putBoolean("is_benign", this.entityData[IS_BENIGN])
    }

    companion object {
        private val IS_BENIGN = EntityDataManager.defineId(GhastlyEnariaEntity::class.java, DataSerializers.BOOLEAN)

        // Constants defining enaria parameters
        private const val MOVE_SPEED = 0.005
        private const val FOLLOW_RANGE = 300.0
        private const val MAX_HEALTH = 9001.0
        private const val ATTACK_DAMAGE = 900.0
        private const val KNOCKBACK_RESISTANCE = 1.0
        private const val PLAYER_DISTANCE_CHECK_FREQUENCY = 10.0
        private const val PLAYER_BENIGN_CHECK_FREQUENCY = 200.0

        /**
         * Sets entity attributes such as max health and movespeed
         */
        fun buildAttributeModifiers(): AttributeModifierMap.MutableAttribute {
            return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, MOVE_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
        }
    }
}