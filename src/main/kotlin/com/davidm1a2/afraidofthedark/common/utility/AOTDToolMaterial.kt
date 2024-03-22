package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.world.item.Tier
import net.minecraft.world.item.crafting.Ingredient

class AOTDToolMaterial(
    private val harvestLevel: Int,
    private val maxUses: Int,
    private val efficiency: Float,
    private val attackDamage: Float,
    private val enchantability: Int,
    private val repairMaterial: Ingredient
) : Tier {
    override fun getLevel() = harvestLevel
    override fun getUses() = maxUses
    override fun getSpeed() = efficiency
    override fun getAttackDamageBonus() = attackDamage
    override fun getEnchantmentValue() = enchantability
    override fun getRepairIngredient() = repairMaterial
}