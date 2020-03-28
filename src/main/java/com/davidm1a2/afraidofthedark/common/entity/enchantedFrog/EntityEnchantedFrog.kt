package com.davidm1a2.afraidofthedark.common.entity.enchantedFrog

import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.animation.AnimationHandlerEnchantedFrog
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import net.minecraft.entity.EntityCreature
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityAILookIdle
import net.minecraft.entity.ai.EntityAISwimming
import net.minecraft.entity.ai.EntityAIWander
import net.minecraft.entity.ai.EntityAIWatchClosest
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.world.World

/**
 * Class representing an enchanted frog entity
 *
 * @constructor initializes the frog based on the world
 * @param world The world the skeleton is spawning into
 * @property animHandler The animation handler used to manage animations
 */
class EntityEnchantedFrog(world: World) : EntityCreature(world), IMCAnimatedEntity {
    private val animHandler = AnimationHandlerEnchantedFrog(this)

    init {
        // Set the size of the frog's hitbox
        setSize(0.7f, 0.4f)
        // Set how much XP the frog is worth
        experienceValue = 8
    }

    /**
     * Creates the AI used by hostile or passive entities
     */
    override fun initEntityAI() {
        // Tasks should have a list of AI tasks with a priority associated with them. Lower priority is executed first
        // If the entity can swim and it's in water it must do that otherwise it will skin
        tasks.addTask(1, EntityAISwimming(this))
        // If the entity isn't attacking then try to walk around
        tasks.addTask(2, EntityAIWander(this, (MOVE_SPEED / 4).toDouble()))
        // If the entity isn't wandering then try to watch whatever entity is nearby
        tasks.addTask(4, EntityAIWatchClosest(this, EntityPlayer::class.java, AGRO_RANGE))
        // If the entity isn't walking, attacking, or watching anything look idle
        tasks.addTask(5, EntityAILookIdle(this))
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
            animHandler.animationsUpdate()
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
                if (!animHandler.isAnimationActive("cast")) {
                    animHandler.activateAnimation("cast", 0f)
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
     * @return The handler for all animations for this entity
     */
    override fun getAnimationHandler(): AnimationHandler {
        return animHandler
    }

    /**
     * @return The eye height of the skeleton which is used in path finding and looking around
     */
    override fun getEyeHeight(): Float {
        return 0.2f
    }

    companion object {
        // Constants defining skeleton parameters
        private const val MOVE_SPEED = 1.0f
        private const val AGRO_RANGE = 16.0f
        private const val FOLLOW_RANGE = 32.0f
        private const val MAX_HEALTH = 7.0f
        private const val KNOCKBACK_RESISTANCE = 0.5f
    }
}