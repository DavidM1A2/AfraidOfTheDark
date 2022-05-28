package com.davidm1a2.afraidofthedark.common.entity.enchantedFrog

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.animation.CastChannel
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.animation.HopChannel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacket
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectInstance
import net.minecraft.block.BlockState
import net.minecraft.entity.CreatureEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.Pose
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.LookAtGoal
import net.minecraft.entity.ai.goal.LookRandomlyGoal
import net.minecraft.entity.ai.goal.RandomWalkingGoal
import net.minecraft.entity.ai.goal.SwimGoal
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
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
class EnchantedFrogEntity(entityType: EntityType<out EnchantedFrogEntity>, world: World) : CreatureEntity(entityType, world), IMCAnimatedModel {
    // We don't need to write this to NBT data, it's not important to persist
    private var ticksUntilNextCastAttempt = MAX_TICKS_BETWEEN_CASTS
    private val animHandler = AnimationHandler(HOP_CHANNEL, CAST_CHANNEL)
    var spell = createRandomSpell()
        private set

    init {
        // Set how much XP the frog is worth
        xpReward = 8
    }

    /**
     * Creates the AI used by hostile or passive entities
     */
    override fun registerGoals() {
        // Tasks should have a list of AI tasks with a priority associated with them. Lower priority is executed first
        // If the entity can swim and it's in water it must do that otherwise it will sink
        goalSelector.addGoal(1, SwimGoal(this))
        // If the entity isn't attacking then try to walk around
        goalSelector.addGoal(2, RandomWalkingGoal(this, 1.0, 20))
        // If the entity isn't wandering then try to watch whatever entity is nearby
        goalSelector.addGoal(3, LookAtGoal(this, PlayerEntity::class.java, FOLLOW_RANGE.toFloat()))
        // If the entity isn't walking, attacking, or watching anything look idle
        goalSelector.addGoal(4, LookRandomlyGoal(this))
    }

    /**
     * Creates a random spell for the frog to cast
     *
     * @return The spell that this frog will cast
     */
    private fun createRandomSpell(): Spell {
        val frogsSpell = Spell()
        frogsSpell.name = "Froggie's magic spell"
        frogsSpell.spellStages.add(createRandomSpellStage())
        // A max of 20 spell stages, but odds are few will be added
        for (ignored in 0..20) {
            // 50% chance to add another spell stage
            if (random.nextDouble() < 0.50) {
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
                if (random.nextDouble() < 0.5) {
                    effects[i] =
                        SpellEffectInstance(ModRegistries.SPELL_EFFECTS.entries.random().value).apply { setDefaults() }
                } else {
                    break
                }
            }
        }
    }

    /**
     * Called every game tick for the entity
     */
    override fun baseTick() {
        // Don't forget to call super!
        super.baseTick()

        // If we're on client side test if we need to show walking animations
        if (level.isClientSide) {
            // If the entity is moving show the walking animation
            if (deltaMovement.x > 0.05 || deltaMovement.z > 0.05 || deltaMovement.x < -0.05 || deltaMovement.z < -0.05) {
                if (!animHandler.isAnimationActive("hop") && !animHandler.isAnimationActive("cast")) {
                    animHandler.playAnimation("hop")
                }
            }
        }

        // Cast a spell server side
        if (!level.isClientSide) {
            ticksUntilNextCastAttempt--
            if (ticksUntilNextCastAttempt <= 0) {
                // Find the nearest player to cast at
                val nearestPlayer = level.getNearestPlayer(this, FOLLOW_RANGE)
                // Cast at the player, and show the cast animation
                nearestPlayer?.let {
                    spell.attemptToCast(this, it.getEyePosition(1.0f).subtract(this.position()).normalize())
                    AfraidOfTheDark.packetHandler.sendToAllAround(AnimationPacket(this, "cast"), this, 50.0)
                }

                ticksUntilNextCastAttempt = Random.nextInt(MIN_TICKS_BETWEEN_CASTS, MAX_TICKS_BETWEEN_CASTS)
            }
        }
    }

    /**
     * Don't play a step sound for the frog
     *
     * @param pos The position
     * @param state The block's state
     */
    override fun playStepSound(pos: BlockPos, state: BlockState) {
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
    override fun getEyeHeight(pose: Pose): Float {
        return 0.2f
    }

    override fun readAdditionalSaveData(compound: CompoundNBT) {
        super.readAdditionalSaveData(compound)
        if (compound.contains("spell")) {
            this.spell = Spell(compound.getCompound("spell"))
        }
    }

    override fun addAdditionalSaveData(compound: CompoundNBT) {
        super.addAdditionalSaveData(compound)
        compound.put("spell", this.spell.serializeNBT())
    }

    companion object {
        private const val MIN_TICKS_BETWEEN_CASTS = 15 * 20
        private const val MAX_TICKS_BETWEEN_CASTS = 30 * 20

        // Constants defining frog parameters
        private const val MOVE_SPEED = 0.25
        private const val FOLLOW_RANGE = 32.0
        private const val MAX_HEALTH = 7.0
        private const val KNOCKBACK_RESISTANCE = 0.5

        private val HOP_CHANNEL = HopChannel("hop", 120.0f, 80, ChannelMode.LINEAR)
        private val CAST_CHANNEL = CastChannel("cast", 120.0f, 60, ChannelMode.LINEAR)

        /**
         * Gives the enchanted frog its entity attributes like movespeed
         */
        fun buildAttributeModifiers(): AttributeModifierMap.MutableAttribute {
            return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE)
                .add(Attributes.MOVEMENT_SPEED, MOVE_SPEED)
        }
    }
}