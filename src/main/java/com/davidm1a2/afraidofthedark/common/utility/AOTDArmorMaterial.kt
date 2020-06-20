package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.IArmorMaterial
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.SoundEvent

/**
 * Utility simple class implementing IArmorMaterial
 */
class AOTDArmorMaterial(
    private val name: String,
    private val maxDamageFactor: Int,
    private val damageReductionAmountArray: Array<Int>,
    private val enchantability: Int,
    private val soundEvent: SoundEvent,
    private val toughness: Float,
    private val repairMaterial: Ingredient
) : IArmorMaterial {
    override fun getName() = name
    override fun getDurability(slot: EntityEquipmentSlot) = MAX_DAMAGE_ARRAY[slot.index] * maxDamageFactor
    override fun getDamageReductionAmount(slot: EntityEquipmentSlot) = damageReductionAmountArray[slot.index]
    override fun getEnchantability() = enchantability
    override fun getSoundEvent() = soundEvent
    override fun getToughness() = toughness
    override fun getRepairMaterial() = repairMaterial

    companion object {
        private val MAX_DAMAGE_ARRAY = intArrayOf(13, 15, 16, 11)
    }
}