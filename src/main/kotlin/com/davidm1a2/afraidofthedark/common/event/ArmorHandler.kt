package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.item.core.AOTDArmorItem
import net.minecraft.entity.MobEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ArmorItem
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
            val armorSlots = entity.armorSlots.toList()

            var damageMultiplierTotal = 1.0

            for (armorSlot in armorSlots) {
                val armorItem = armorSlot.item
                if (armorItem is AOTDArmorItem) {
                    val equipmentSlot = MobEntity.getEquipmentSlotForItem(armorSlot)
                    val ratio = getRatio(armorItem, equipmentSlot)
                    damageMultiplierTotal -= ratio * (1 - armorItem.getDamageMultiplier(event.entityLiving, armorSlot, event.source, equipmentSlot))
                }
            }

            event.amount = event.amount * damageMultiplierTotal.toFloat()
        }
    }

    /**
     * Returns the ratio of protection each pieces gives
     *
     * @param slot The slot the armor is in
     * @return The ratio of protection of each piece reduced by the percent damage blocked
     */
    private fun getRatio(armorItem: ArmorItem, slot: EquipmentSlotType): Double {
        val material = armorItem.material
        // Total protection of each piece
        val totalProtection = material.getDefenseForSlot(EquipmentSlotType.HEAD) +
                material.getDefenseForSlot(EquipmentSlotType.CHEST) +
                material.getDefenseForSlot(EquipmentSlotType.LEGS) +
                material.getDefenseForSlot(EquipmentSlotType.FEET)
        // Ratio of protection of this pieces to the rest
        return material.getDefenseForSlot(slot).toDouble() / totalProtection
    }
}