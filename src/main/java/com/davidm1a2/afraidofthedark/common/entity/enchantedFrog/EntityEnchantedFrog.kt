package com.davidm1a2.afraidofthedark.common.entity.enchantedFrog

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.animation.AnimationHandlerEnchantedFrog
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.packets.animationPackets.SyncAnimation
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectInstance
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceInstance
import net.minecraft.block.Block
import net.minecraft.entity.EntityCreature
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAILookIdle
import net.minecraft.entity.ai.EntityAISwimming
import net.minecraft.entity.ai.EntityAIWander
import net.minecraft.entity.ai.EntityAIWatchClosest
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.random.Random

/**
 * Class representing an enchanted frog entity
 *
 * @constructor initializes the frog based on the world
 * @param world The world the frog is spawning into
 * @property animHandler The animation handler used to manage animations
 */
class EntityEnchantedFrog(world: World) : EntityCreature(world),
    IMCAnimatedModel {
    // We don't need to write this to NBT data, it's not important to persist
    private var ticksUntilNextCastAttempt = MAX_TICKS_BETWEEN_CASTS
    private val animHandler = AnimationHandlerEnchantedFrog()
    var frogsSpell: Spell

    init {
        // Set the size of the frog's hitbox
        setSize(0.7f, 0.4f)
        // Set how much XP the frog is worth
        experienceValue = 8
        // Set the spell to a random one
        frogsSpell = createRandomSpell()
    }

    /**
     * Creates a random spell for the frog to cast
     *
     * @return The spell that this frog will cast
     */
    private fun createRandomSpell(): Spell {
        frogsSpell = Spell()
        frogsSpell.name = "Froggie's magic spell"
        frogsSpell.powerSource = SpellPowerSourceInstance(ModSpellPowerSources.CREATIVE).apply { setDefaults() }
        frogsSpell.spellStages.add(createRandomSpellStage())
        // A max of 20 spell stages, but odds are few will be added
        for (ignored in 0..20) {
            // 25% chance to add another spell stage
            if (rand.nextDouble() < 0.25) {
                frogsSpell.spellStages.add(createRandomSpellStage())
            } else {
                break
            }
        }
        return frogsSpell
    }

    /**
     * Creates a random spell stage for the frog to cast
     *
     * @return The spell stage that the frog will cast
     */
    private fun createRandomSpellStage(): SpellStage {
        return SpellStage().apply {
            // Use a random delivery method
            deliveryInstance =
                SpellDeliveryMethodInstance(ModRegistries.SPELL_DELIVERY_METHODS.entries.random().value).apply { setDefaults() }
            // Add a random effect
            effects[0] = SpellEffectInstance(ModRegistries.SPELL_EFFECTS.entries.random().value).apply { setDefaults() }
            // 1/2 for an extra effect, 1/4 for 2 extra effects, 1/8 for 3 extra effects
            for (i in 1..3) {
                // 50% chance to add another effect
                if (rand.nextDouble() < 0.5) {
                    effects[i] =
                        SpellEffectInstance(ModRegistries.SPELL_EFFECTS.entries.random().value).apply { setDefaults() }
                } else {
                    break
                }
            }
        }
    }

    /**
     * Creates the AI used by hostile or passive entities
     */
    override fun initEntityAI() {
        // Tasks should have a list of AI tasks with a priority associated with them. Lower priority is executed first
        // If the entity can swim and it's in water it must do that otherwise it will skin
        tasks.addTask(1, EntityAISwimming(this))
        // If the entity isn't attacking then try to walk around
        tasks.addTask(2, EntityAIWander(this, 1.0, 20))
        // If the entity isn't wandering then try to watch whatever entity is nearby
        tasks.addTask(3, EntityAIWatchClosest(this, EntityPlayer::class.java, FOLLOW_RANGE))
        // If the entity isn't walking, attacking, or watching anything look idle
        tasks.addTask(4, EntityAILookIdle(this))
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

        // If we're on client side test if we need to show walking animations
        if (world.isRemote) {
            // If the entity is moving show the walking animation
            if (motionX > 0.05 || motionZ > 0.05 || motionX < -0.05 || motionZ < -0.05) {
                if (!animHandler.isAnimationActive("hop") && !animHandler.isAnimationActive("cast")) {
                    animHandler.playAnimation("hop")
                }
            }
        }

        // Cast a spell server side
        if (!world.isRemote) {
            ticksUntilNextCastAttempt--
            if (ticksUntilNextCastAttempt <= 0) {
                // Find the nearest player to cast at
                val nearestPlayer = world.getClosestPlayer(
                    this.posX, this.posY, this.posZ, FOLLOW_RANGE.toDouble()
                ) { it is EntityPlayer }
                // Cast at the player, and show the cast animation
                nearestPlayer?.let {
                    frogsSpell.attemptToCast(this, it.getPositionEyes(1.0f).subtract(this.positionVector).normalize())
                    AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(SyncAnimation("cast", this), this, 50.0)
                }

                ticksUntilNextCastAttempt = Random.nextInt(MIN_TICKS_BETWEEN_CASTS, MAX_TICKS_BETWEEN_CASTS)
            }
        }
    }

    /**
     * Called to drop items on the ground after the frog dies
     *
     * @param wasRecentlyHit  If the frog was recently hit
     * @param lootingModifier If looting was present, and what level of looting was present
     */
    override fun dropFewItems(wasRecentlyHit: Boolean, lootingModifier: Int) {
        val chance = rand.nextDouble()

        // 5% chance to drop 2, 40% chance for one
        val numberToDrop = when {
            chance < 0.05 -> 2
            chance < 0.4 -> 1
            else -> 0
        }

        if (numberToDrop > 0) {
            dropItem(this.dropItem, numberToDrop)
        }
    }

    /**
     * Frogs drop magic essence!
     *
     * @return The magic [frog bacon]
     */
    override fun getDropItem(): Item {
        return ModItems.MAGIC_ESSENCE
    }

    /**
     * Don't play a step sound for the frog
     *
     * @param pos The position
     */
    override fun playStepSound(pos: BlockPos, blockIn: Block) {
    }

    /**
     * Gets the ambient sound of the entity
     *
     * @return The croak sound
     */
    override fun getAmbientSound(): SoundEvent {
        return ModSounds.ENCHANTED_FROG_CROAK
    }

    /**
     * Gets the hurt sound of the entity
     *
     * @param damageSourceIn The source of the hurt damage
     * @return The croak sound
     */
    override fun getHurtSound(damageSourceIn: DamageSource): SoundEvent {
        return ModSounds.ENCHANTED_FROG_HURT
    }

    /**
     * Gets the death sound of the entity
     *
     * @return The croak sound
     */
    override fun getDeathSound(): SoundEvent {
        return ModSounds.ENCHANTED_FROG_DEATH
    }

    /**
     * @return The handler for all animations for this entity
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    /**
     * @return The eye height of the frog which is used in path finding and looking around
     */
    override fun getEyeHeight(): Float {
        return 0.2f
    }

    /**
     * Writes the entity to NBT
     *
     * @param compound The compound to write to
     */
    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)
        compound.setTag(NBT_SPELL, frogsSpell.serializeNBT())
    }

    /**
     * Reads the entity in from nbt
     *
     * @param compound The compound to read from
     */
    override fun readEntityFromNBT(compound: NBTTagCompound) {
        super.readEntityFromNBT(compound)
        frogsSpell = Spell(compound.getCompoundTag(NBT_SPELL))
    }

    companion object {
        private const val MIN_TICKS_BETWEEN_CASTS = 15 * 20
        private const val MAX_TICKS_BETWEEN_CASTS = 30 * 20

        // Constants defining frog parameters
        private const val MOVE_SPEED = 0.25f
        private const val FOLLOW_RANGE = 32.0f
        private const val MAX_HEALTH = 7.0f
        private const val KNOCKBACK_RESISTANCE = 0.5f

        // NBT properties
        private const val NBT_SPELL = "spell"
    }
}