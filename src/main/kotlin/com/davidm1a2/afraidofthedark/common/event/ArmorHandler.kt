package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemArmor
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Contains logic for handling armor damage. Replaces ISpecialArmor functionality
 */
class ArmorHandler {
    /**
     * Called when an entity takes damage. If it's an entity wearing Afraid of the Dark armor, perform damage reduction calculations
     *
     * @param event The event to process
     */
    @SubscribeEvent
    fun onLivingHurtEvent(event: LivingHurtEvent) {
        // Server side processing only
        if (!event.entity.world.isRemote) {
            val armor = event.entityLiving.armorInventoryList.toList()
            // Only process armor with 4 pieces (helm, chest, legging, boot)
            if (armor.size == 4) {
                val percentDamageBlocked = armor.sumByDouble {
                    val armorItem = it.item
                    if (armorItem is AOTDItemArmor) {
                        armorItem.processDamage(event.entityLiving, it, event.source, event.amount, armorItem.equipmentSlot)
                    } else {
                        0.0
                    }
                }
                event.amount = (event.amount * (1.0f - percentDamageBlocked.coerceIn(0.0, 1.0))).toFloat()
            }
        }
    }
}