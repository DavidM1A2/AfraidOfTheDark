package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.ChannelArmthrow
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.ChannelAutoattack
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.ChannelSpell
import com.davidm1a2.afraidofthedark.common.entity.enaria.animation.ChannelWalk
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.PlayEnariasFightMusicPacket
import net.minecraft.entity.Entity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.ai.EntityAISwimming
import net.minecraft.entity.ai.EntityAIWatchClosest
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.BossInfo
import net.minecraft.world.BossInfoServer
import net.minecraft.world.World
import kotlin.math.min

/**
 * The enaria entity that is the overworld boss
 *
 * @constructor Required single arg constructor just sets the entity size and world
 * @param world The world enaria is in
 * @property animHandler Enaria's animation handler
 * @property bossInfo Enaria's boss info that allows the displaying of the boss HP bar
 * @property allowedRegion The hitbox that enaria cannot leave
 * @property enariaAttacks Enaria's attack object use to manage her attacks
 */
class EntityEnaria(world: World) : EntityMob(ModEntities.ENARIA, world), IMCAnimatedModel {
    private val animHandler = AnimationHandler(
        ChannelWalk("walk", 59.0f, 59, ChannelMode.LINEAR),
        ChannelArmthrow("armthrow", 61.0f, 61, ChannelMode.LINEAR),
        ChannelAutoattack("autoattack", 70.0f, 51, ChannelMode.LINEAR),
        ChannelSpell("spell", 90.0f, 121, ChannelMode.LINEAR)
    )
    private val bossInfo = BossInfoServer(
        this.displayName,
        BossInfo.Color.PURPLE,
        BossInfo.Overlay.PROGRESS
    ).setDarkenSky(true) as BossInfoServer
    lateinit var allowedRegion: AxisAlignedBB
    val enariaAttacks: EnariaAttacks

    init {
        setSize(0.8f, 1.8f)
        // Name of the entity, will be bold and red
        this.customName = TextComponentString("Enaria")
        bossInfo.name = this.displayName
        experienceValue = 300
        isImmuneToFire = true
        enariaAttacks = EnariaAttacks(this, rand)
    }

    /**
     * Overloaded constructor that sets the world and the region enaria is allowed to be in
     *
     * @param world The world to spawn enaria in
     * @param allowedRegion The region enaria is allowed to be inside of
     */
    constructor(world: World, allowedRegion: AxisAlignedBB) : this(world) {
        this.entityData.setBoolean(NBT_IS_VALID, true)
        this.allowedRegion = allowedRegion
    }

    /**
     * Initializes enaria's AI
     */
    override fun initEntityAI() {
        // First ensure we're not drowning and try to swim
        tasks.addTask(1, EntityAISwimming(this))
        // Attack the target if we are not swimming
        tasks.addTask(2, EntityAIAttackEnaria(this))
        // Follow the target if we can't attack
        tasks.addTask(3, EntityAIFollowPlayer(this, 8.0, 128.0, 32.0))
        // Watch the nearest player if we can't follow them
        tasks.addTask(4, EntityAIWatchClosest(this, EntityPlayer::class.java, 20.0f))
        // Find the nearest attackable player and attack
        targetTasks.addTask(0, EntityAINearestAttackableTarget(this, EntityPlayer::class.java, true))
    }

    /**
     * Called to update the entity every tick
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
        // Client side test if enaria is walking, if so play the animation
        if (world.isRemote) {
            // Motion >= 0.5 = walking
            if (motionX > 0.05 || motionZ > 0.05 || motionX < -0.05 || motionZ < -0.05) {
                // Ensure no other animation is currently active
                if (!animHandler.isAnimationActive("spell") &&
                    !animHandler.isAnimationActive("autoattack") &&
                    !animHandler.isAnimationActive("armthrow") &&
                    !animHandler.isAnimationActive("walk")
                ) {
                    // Walk
                    animHandler.playAnimation("walk")
                }
            }
        }
    }

    /**
     * Called every tick the enaria entity is alive
     */
    override fun livingTick() {
        super.livingTick()
        // If we're on server side perform some checks
        if (!world.isRemote) {
            // Update the boss info HP bar
            bossInfo.percent = this.health / this.maxHealth
            // If enaria was spawned without the right tags she's invalid so kill her
            if (ticksExisted == 1) {
                if (!this.entityData.getBoolean(NBT_IS_VALID)) {
                    remove()
                }
            }
        }
    }

    /**
     * Gives enaria her entity attributes like damage and movespeed
     */
    override fun registerAttributes() {
        super.registerAttributes()
        attributeMap.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).baseValue = MAX_HEALTH
        attributeMap.getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = FOLLOW_RANGE
        attributeMap.getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).baseValue = KNOCKBACK_RESISTANCE
        attributeMap.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = MOVE_SPEED
        attributeMap.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = ATTACK_DAMAGE
    }

    /**
     * Called when enaria gets attacked
     *
     * @param source The damage source that hit enaria
     * @param amount The amount of damage inflicted
     * @return True to allow the damage, false otherwise
     */
    override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean {
        // Server side processing only
        if (!world.isRemote) {
            // Compute the time between this hit and the last hit she received
            val timeBetweenHits = System.currentTimeMillis() - this.entityData.getLong(NBT_LAST_HIT)
            // Update the last hit time
            this.entityData.setLong(NBT_LAST_HIT, timeBetweenHits)

            // Make amount mutable
            @Suppress("NAME_SHADOWING")
            var amount = amount

            // Kill the entity if damage received is FLOAT.MAX_VALUE
            if (amount == Float.MAX_VALUE) {
                return super.attackEntityFrom(source, amount)
            } else if (amount > MAX_DAMAGE_IN_1_HIT) {
                amount = MAX_DAMAGE_IN_1_HIT.toFloat()
            }

            // If an entity damaged the entity check if it was with silver damage
            if (source is EntityDamageSource) {
                // Grab the source of the damage
                val damageSource = source.getTrueSource()
                if (damageSource is EntityPlayer) {
                    // If a player hit enaria check if they have the right research
                    if (!damageSource.getResearch().isResearched(ModResearches.ENARIA.preRequisite!!)) {
                        damageSource.sendMessage(TextComponentTranslation("message.afraidofthedark.enaria.dont_understand"))
                        // Can't damage enaria without research
                        return false
                    }
                }

                // If the damage source is silver damage inflict heavy damage
                if (source.damageType.equals(ModDamageSources.SILVER_DAMAGE, ignoreCase = true)) {
                    // If its been more than a second since the last attack do full damage, otherwise scale the damage
                    val amountModifier = min(1.0f, timeBetweenHits / 1000.0f)

                    // If the amount of time is less than 250ms then the player is spam clicking and enaria should teleport 1/40 times
                    if (rand.nextInt(40) == 0) {
                        enariaAttacks.randomTeleport()
                    }
                    return super.attackEntityFrom(source, amount * amountModifier)
                } else {
                    if (rand.nextInt(60) == 0) {
                        enariaAttacks.randomTeleport()
                    }
                }
            } else if (source == DamageSource.FALL) {
                if (rand.nextBoolean()) {
                    enariaAttacks.randomTeleport()
                }
            } else {
                if (rand.nextInt(100) == 0) {
                    enariaAttacks.randomTeleport()
                }
            }
        }

        // Finally if nothing succeeds do 1 damage. Only do this if she has more than 1hp, this ensures she doesn't
        // die to a non-player source
        return if (this.health >= 2.0) {
            super.attackEntityFrom(DamageSource.GENERIC, 1f)
        } else {
            false
        }
    }

    /**
     * When enaria dies by a player unlock the enaria research
     *
     * @param cause The damage source that killed enaria
     */
    override fun onDeath(cause: DamageSource) {
        super.onDeath(cause)
        // Server side processing only
        if (!world.isRemote) {
            // If enaria died to a player damage source continue processing...
            if (cause is EntityDamageSource) {
                // If enaria died to a player continue processing...
                if (cause.getTrueSource() is EntityPlayer) {
                    // Grab all entities around enaria and if they can research "ENARIA" unlock the research for them
                    for (entityPlayer in world.getEntitiesWithinAABB(
                        EntityPlayer::class.java,
                        this.boundingBox.grow(RESEARCH_UNLOCK_RANGE)
                    )) {
                        val playerResearch = entityPlayer.getResearch()
                        // If we can research enaria unlock it
                        if (playerResearch.canResearch(ModResearches.ENARIA)) {
                            playerResearch.setResearch(ModResearches.ENARIA, true)
                            playerResearch.sync(entityPlayer, true)
                        }
                    }
                }
            }
        }
    }

    /**
     * @return Make enaria's name tag red and bold in chat
     */
    override fun getDisplayName(): ITextComponent {
        return this.customName!!
    }

    /**
     * Enaria can't ride anything
     *
     * @param entityIn The entity to test
     * @return False, duh
     */
    override fun canBeRidden(entityIn: Entity): Boolean {
        return false
    }

    /**
     * @return False, enaria spawn behavior is managed by the tile entity
     */
    override fun canDespawn(): Boolean {
        return false
    }

    /**
     * @return The animation handler used to animate enaria
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    /**
     * Called whenever a player enters range of enaria to show the boss bar
     *
     * @param player The player to show enaria's boss bar to
     */
    override fun addTrackingPlayer(player: EntityPlayerMP) {
        super.addTrackingPlayer(player)
        // Tell the player to play the enaria combat music
        AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(this, true), player)
        bossInfo.addPlayer(player)
    }

    /**
     * Called whenever a player leaves range of enaria to hide the boss bar
     *
     * @param player The player to remove enaria's boss bar from
     */
    override fun removeTrackingPlayer(player: EntityPlayerMP) {
        super.removeTrackingPlayer(player)
        // Tell the player to stop playing the enaria combat music
        AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(this, false), player)
        bossInfo.removePlayer(player)
    }

    /**
     * @return False, enaria is a boss mob
     */
    override fun isNonBoss(): Boolean {
        return false
    }

    /**
     * Writes enaria's state to the NBT compound
     *
     * @param compound The NBT compound to write to
     */
    override fun writeAdditional(compound: NBTTagCompound) {
        super.writeAdditional(compound)
        compound.setDouble("minX", allowedRegion.minX)
        compound.setDouble("minY", allowedRegion.minY)
        compound.setDouble("minZ", allowedRegion.minZ)
        compound.setDouble("maxX", allowedRegion.maxX)
        compound.setDouble("maxY", allowedRegion.maxY)
        compound.setDouble("maxZ", allowedRegion.maxZ)
    }

    /**
     * Reads enaria's state in from the NBT compound
     *
     * @param compound The NBT compound to read from
     */
    override fun readAdditional(compound: NBTTagCompound) {
        super.readAdditional(compound)
        allowedRegion = AxisAlignedBB(
            compound.getDouble("minX"),
            compound.getDouble("minY"),
            compound.getDouble("minZ"),
            compound.getDouble("maxX"),
            compound.getDouble("maxY"),
            compound.getDouble("maxZ")
        )
    }

    companion object {
        // Constants for enaria's stats
        private const val MOVE_SPEED = 0.6
        private const val FOLLOW_RANGE = 64.0
        private const val MAX_HEALTH = 400.0
        private const val ATTACK_DAMAGE = 12.0
        private const val KNOCKBACK_RESISTANCE = 0.5
        private const val RESEARCH_UNLOCK_RANGE = 100.0

        // NBT tag compounds for if the entity is valid and the last time she attacked
        private const val NBT_IS_VALID = "is_valid"
        private const val NBT_LAST_HIT = "last_hit"

        // The maximum amount of damage done in a single shot
        private const val MAX_DAMAGE_IN_1_HIT = 10
    }
}