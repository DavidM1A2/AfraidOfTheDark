package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.IArmorMaterial
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.SoundEvent

/**
 * Utility simple class implementing IArmorMaterial
 */
class AOTDArmorMaterial(
    private val name: String,
    private val durabilityMultiplier: Int,
    private val damageReductionAmountArray: Array<Int>,
    private val enchantability: Int,
    private val soundEvent: SoundEvent,
    private val toughness: Float,
    private val knockbackResistance: Float,
    private val repairMaterial: Ingredient
) : IArmorMaterial {
    override fun getName() = name
    override fun getDurabilityForSlot(slot: EquipmentSlotType) = MAX_DAMAGE_ARRAY[slot.index] * durabilityMultiplier
    override fun getDefenseForSlot(slot: EquipmentSlotType) = damageReductionAmountArray[slot.index]
    override fun getEnchantmentValue() = enchantability
    override fun getEquipSound() = soundEvent
    override fun getToughness() = toughness
    override fun getKnockbackResistance() = knockbackResistance
    override fun getRepairIngredient() = repairMaterial

    companion object {
        private val MAX_DAMAGE_ARRAY = intArrayOf(13, 15, 16, 11)
    }
}