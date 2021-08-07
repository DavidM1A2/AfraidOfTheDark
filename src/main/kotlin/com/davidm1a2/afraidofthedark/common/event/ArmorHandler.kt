package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.item.core.AOTDArmorItem
import net.minecraft.entity.MobEntity
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
        if (!event.entity.level.isClientSide) {
            val entity = event.entityLiving
            val armor = entity.armorSlots.toList()
            // Only process armor with 4 pieces (helm, chest, legging, boot)
            if (armor.size == 4) {
                val percentDamageBlocked = armor.sumOf {
                    val armorItem = it.item
                    if (armorItem is AOTDArmorItem) {
                        armorItem.processDamage(event.entityLiving, it, event.source, event.amount, MobEntity.getEquipmentSlotForItem(it))
                    } else {
                        0.0
                    }
                }
                event.amount = (event.amount * (1.0f - percentDamageBlocked.coerceIn(0.0, 1.0))).toFloat()
            }
        }
    }
}