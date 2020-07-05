package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.common.capabilities.getNightmareData
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.*
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.ChannelDance
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityFlying
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World

/**
 * Class representing the ghastly enaria entity
 *
 * @constructor sets the ghastly enaria entity properties
 * @property animHandler The animation handler used to manage animations
 * @property benign Flag telling us if this enaria is benign or not, defaults to true. This will change her AI
 */
class EntityGhastlyEnaria(world: World) : EntityFlying(ModEntities.GHASTLY_ENARIA, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(ChannelDance("dance", 30.0f, 300, ChannelMode.LINEAR))
    private var benign = true

    init {
        // Sets the size of the hitbox of enaria
        setSize(0.8f, 1.8f)
        // The name of the entity, will be bold and red
        this.customName = TextComponentString("§c§lGhastly Enaria")
        // Enable noclip so enaria can go through walls
        noClip = true
        // Enaria is immune to fire
        isImmuneToFire = true
        // Add a custom move helper that moves her through walls
        moveHelper = GhastlyEnariaMoveHelper(this)
    }

    /**
     * Initializes the entity AI
     */
    override fun initEntityAI() {
        // Add a player chase task that makes her follow the player
        tasks.addTask(1, GhastlyEnariaPlayerChase(this))
    }

    /**
     * Sets entity attributes such as max health and movespeed
     */
    override fun registerAttributes() {
        super.registerAttributes()
        attributeMap.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
        attributeMap.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).baseValue = MAX_HEALTH
        attributeMap.getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = FOLLOW_RANGE
        attributeMap.getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).baseValue = KNOCKBACK_RESISTANCE
        attributeMap.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = MOVE_SPEED
        attributeMap.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = ATTACK_DAMAGE
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
        super.baseTick()

        // Check == 20 instead of == 0 so the player can spawn and that way we don't accidentally check before a player joins
        // the world
        if (ticksExisted % PLAYER_BENIGN_CHECK_FREQUENCY == 20.0) {
            // Grab the distance between the nightamre islands
            val distanceBetweenIslands = ModCommonConfiguration.blocksBetweenIslands
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
                val entityPlayer = world.getClosestPlayerToEntity(this, 3.0)
                // Make sure the player is valid and not dead
                if (entityPlayer != null && entityPlayer.isAlive) {
                    // Kill enaria, she's now unloaded (can't use .setDead()) or we get an index out of bounds exception?
                    onKillCommand()

                    // Dismount whatever we're in
                    entityPlayer.stopRiding()

                    // Give the player a nightmare stone
                    entityPlayer.inventory.addItemStackToInventory(ItemStack(ModItems.NIGHTMARE_STONE))

                    // Send them back to their original dimension
                    entityPlayer.changeDimension(
                        entityPlayer.getNightmareData().preTeleportDimension!!,
                        ModDimensions.NOOP_TELEPORTER
                    )
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
     * Writes the benign boolean to the entities NBT
     *
     * @param tagCompound The compound to write to
     */
    override fun writeAdditional(tagCompound: NBTTagCompound) {
        super.writeAdditional(tagCompound)
        tagCompound.setBoolean(NBT_BENIGN, benign)
    }

    /**
     * Reads the benign boolean from the entities NBT
     *
     * @param tagCompund The compound to read from
     */
    override fun readAdditional(tagCompund: NBTTagCompound) {
        super.readAdditional(tagCompund)
        benign = tagCompund.getBoolean(NBT_BENIGN)
    }

    /**
     * @return False, enaria can't despawn
     */
    override fun canDespawn(): Boolean {
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
        return benign
    }

    /**
     * Sets the benign flag, if true enaria will dance, if false she will chase
     *
     * @param benign The benign boolean flag
     */
    fun setBenign(benign: Boolean) {
        this.benign = benign
        // If we're client side stop playing the dance animation
        if (world.isRemote && !this.benign) {
            animHandler.stopAnimation("dance")
        }
    }

    /**
     * The animation handler for the entity
     *
     * @return The GhastlyEnaria animation handler
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    companion object {
        // Constants defining enaria parameters
        private const val MOVE_SPEED = 0.02
        private const val FOLLOW_RANGE = 300.0
        private const val MAX_HEALTH = 9001.0
        private const val ATTACK_DAMAGE = 900.0
        private const val KNOCKBACK_RESISTANCE = 1.0
        private const val PLAYER_DISTANCE_CHECK_FREQUENCY = 10.0
        private const val PLAYER_BENIGN_CHECK_FREQUENCY = 200.0

        // Constant for benign NBT field
        private const val NBT_BENIGN = "benign"
    }
}