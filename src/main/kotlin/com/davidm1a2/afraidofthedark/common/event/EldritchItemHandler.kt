package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.item.eldritchmetal.IEldritchItem
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.EntityDamageSource
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class EldritchItemHandler {
    @SubscribeEvent
    fun onLivingDeathEvent(event: LivingDeathEvent) {
        val source = event.source
        if (source is EntityDamageSource) {
            val entity = source.entity
            if (entity is PlayerEntity) {
                val now = System.currentTimeMillis()
                for (itemStack in entity.inventory.items + entity.inventory.armor + entity.inventory.offhand) {
                    if (!itemStack.isEmpty) {
                        val item = itemStack.item
                        if (item is IEldritchItem) {
                            item.setLastKillTime(itemStack, now)
                        }
                    }
                }
            }
        }
    }
}