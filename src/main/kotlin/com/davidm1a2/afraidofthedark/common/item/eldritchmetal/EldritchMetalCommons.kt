package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.time.Duration

object EldritchMetalCommons {
    private const val CHECK_FREQUENCY = 100
    private const val MAX_MULTIPLIER = 10
    val TIME_BEFORE_EFFECTS = Duration.ofMinutes(5).toMillis()

    fun processItem(itemStack: ItemStack, world: Level, entity: Entity) {
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
                        if (!entity.hasEffect(MobEffects.HUNGER) || !entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                            entity.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, true))
                        }
                        // Up to hunger 10
                        entity.addEffect(MobEffectInstance(MobEffects.HUNGER, CHECK_FREQUENCY + 20, multipleOver, false, true))
                        // Up to slowness 5
                        entity.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, CHECK_FREQUENCY + 20, multipleOver / 2, false, true))
                    }
                }
            }
        }
    }
}