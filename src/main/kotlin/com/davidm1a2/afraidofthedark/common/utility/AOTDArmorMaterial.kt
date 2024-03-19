package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient

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
) : ArmorMaterial {
    override fun getName() = name
    override fun getDurabilityForSlot(slot: EquipmentSlot) = MAX_DAMAGE_ARRAY[slot.index] * durabilityMultiplier
    override fun getDefenseForSlot(slot: EquipmentSlot) = damageReductionAmountArray[slot.index]
    override fun getEnchantmentValue() = enchantability
    override fun getEquipSound() = soundEvent
    override fun getToughness() = toughness
    override fun getKnockbackResistance() = knockbackResistance
    override fun getRepairIngredient() = repairMaterial

    companion object {
        private val MAX_DAMAGE_ARRAY = intArrayOf(13, 15, 16, 11)
    }
}