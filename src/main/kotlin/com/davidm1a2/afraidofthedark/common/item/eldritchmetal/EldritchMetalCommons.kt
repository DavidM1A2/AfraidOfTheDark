package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.world.World
import java.time.Duration

object EldritchMetalCommons {
    private const val CHECK_FREQUENCY = 100
    private const val MAX_MULTIPLIER = 10
    private val TIME_BEFORE_EFFECTS = Duration.ofMinutes(5).toMillis()

    fun processItem(itemStack: ItemStack, world: World, entity: Entity) {
        if (!world.isClientSide) {
            if (entity.tickCount % CHECK_FREQUENCY == 0) {
                if (entity is LivingEntity) {
                    val item = itemStack.item
                    if (item !is IEldritchItem) return

                    val lastKillTime = item.getLastKillTime(itemStack)
                    val currentTime = System.currentTimeMillis()
                    val timeSinceKill = (currentTime - lastKillTime).coerceAtLeast(0)
                    if (timeSinceKill > TIME_BEFORE_EFFECTS) {
                        val multipleOver = ((timeSinceKill - TIME_BEFORE_EFFECTS) / TIME_BEFORE_EFFECTS.toDouble()).toInt().coerceAtMost(MAX_MULTIPLIER - 1)
                        // Add blindness for 2 seconds upon getting hungry for the first time
                        if (!entity.hasEffect(Effects.HUNGER) || !entity.hasEffect(Effects.MOVEMENT_SLOWDOWN)) {
                            entity.addEffect(EffectInstance(Effects.BLINDNESS, 40, 0, false, true))
                        }
                        // Up to hunger 10
                        entity.addEffect(EffectInstance(Effects.HUNGER, CHECK_FREQUENCY + 20, multipleOver, false, true))
                        // Up to slowness 5
                        entity.addEffect(EffectInstance(Effects.MOVEMENT_SLOWDOWN, CHECK_FREQUENCY + 20, multipleOver / 2, false, true))
                    }
                }
            }
        }
    }
}