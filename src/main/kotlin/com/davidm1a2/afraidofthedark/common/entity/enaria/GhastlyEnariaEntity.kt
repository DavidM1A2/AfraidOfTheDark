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
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import java.util.*

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
        noClip = true
        // Add a custom move helper that moves her through walls
        moveController = GhastlyEnariaMovementController(this)
    }

    /**
     * Initialize dataManager. Anything registered here is automatically synced from Server -> Client
     */
    override fun registerData() {
        super.registerData()
        this.dataManager.register(IS_BENIGN, true)
    }

    /**
     * Initializes the entity AI
     */
    override fun registerGoals() {
        // Add a player chase task that makes her follow the player
        goalSelector.addGoal(1, GhastlyEnariaPlayerChaseGoal(this))
    }

    /**
     * Sets entity attributes such as max health and movespeed
     */
    override fun registerAttributes() {
        super.registerAttributes()
        attributes.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = ATTACK_DAMAGE
        attributes.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH)?.baseValue = MAX_HEALTH
        attributes.getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE)?.baseValue = FOLLOW_RANGE
        attributes.getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE)?.baseValue = KNOCKBACK_RESISTANCE
        attributes.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED)?.baseValue = MOVE_SPEED
    }

    /**
     * Called every game tick for the entity
     */
    override fun baseTick() {
        super.baseTick()

        // Check == 20 instead of == 0 so the player can spawn and that way we don't accidentally check before a player joins
        // the world
        if (ticksExisted % PLAYER_BENIGN_CHECK_FREQUENCY == 20.0) {
            // Grab the distance between the nightamre islands
            val distanceBetweenIslands = Constants.DISTANCE_BETWEEN_ISLANDS
            // Grab the closest player
            val closestPlayer = world.getClosestPlayer(posX, posY, posZ, distanceBetweenIslands / 2.toDouble(), false)
            // If the closest player is null enaria will be benign
            if (closestPlayer == null) {
                setBenign(true)
            } else {
                setBenign(!closestPlayer.getResearch().isResearched(ModResearches.ENARIA))
            }
        }

        // If dance is not active play the animation client side
        if (world.isRemote) {
            if (isBenign()) {
                if (!this.animHandler.isAnimationActive("dance")) {
                    this.animHandler.playAnimation("dance")
                }
            }
        }

        // If a player gets within 3 blocks of enaria send them back to the overworld
        if (!world.isRemote) {
            if (ticksExisted % PLAYER_DISTANCE_CHECK_FREQUENCY == 0.0) {
                // Grab the closest player within 3 blocks
                val entityPlayer = world.getClosestPlayer(this, 3.0)
                // Make sure the player is valid and not dead
                if (entityPlayer != null && entityPlayer.isAlive) {
                    // Don't TP The player from here or we get an exception. Let the GhastlyEnariaTileEntity do that for us
                    this.touchedPlayer = entityPlayer.uniqueID
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
    override fun attackEntityFrom(damageSource: DamageSource, damage: Float): Boolean {
        return if (damageSource == DamageSource.OUT_OF_WORLD) {
            super.attackEntityFrom(damageSource, damage)
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

    /**
     * @return False, enaria can't despawn
     */
    override fun canDespawn(distanceToClosestPlayer: Double): Boolean {
        return false
    }

    /**
     * @return False, enaria can't ride any entities
     */
    override fun canBeRidden(entityIn: Entity): Boolean {
        return false
    }

    /**
     * Gets the benign flag, if true enaria will dance, if false she will chase
     *
     * @return The benign boolean flag
     */
    fun isBenign(): Boolean {
        return dataManager[IS_BENIGN]
    }

    /**
     * Sets the benign flag, if true enaria will dance, if false she will chase
     *
     * @param benign The benign boolean flag
     */
    fun setBenign(benign: Boolean) {
        dataManager[IS_BENIGN] = benign
        // If we're client side stop playing the dance animation
        if (world.isRemote && !benign) {
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
     * Ghastly Enaria can't be pushed by water
     *
     * @return false
     */
    override fun isPushedByWater(): Boolean {
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

    override fun createSpawnPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun readAdditional(compound: CompoundNBT) {
        super.readAdditional(compound)
        this.dataManager[IS_BENIGN] = compound.getBoolean("is_benign")
    }

    override fun writeAdditional(compound: CompoundNBT) {
        super.writeAdditional(compound)
        compound.putBoolean("is_benign", this.dataManager[IS_BENIGN])
    }

    companion object {
        private val IS_BENIGN = EntityDataManager.createKey(GhastlyEnariaEntity::class.java, DataSerializers.BOOLEAN)

        // Constants defining enaria parameters
        private const val MOVE_SPEED = 0.005
        private const val FOLLOW_RANGE = 300.0
        private const val MAX_HEALTH = 9001.0
        private const val ATTACK_DAMAGE = 900.0
        private const val KNOCKBACK_RESISTANCE = 1.0
        private const val PLAYER_DISTANCE_CHECK_FREQUENCY = 10.0
        private const val PLAYER_BENIGN_CHECK_FREQUENCY = 200.0
    }
}