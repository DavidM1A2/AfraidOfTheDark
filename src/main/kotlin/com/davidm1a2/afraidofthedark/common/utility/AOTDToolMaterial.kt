package com.davidm1a2.afraidofthedark.common.utility

import net.minecraft.item.IItemTier
import net.minecraft.item.crafting.Ingredient

class AOTDToolMaterial(
    private val harvestLevel: Int,
    private val maxUses: Int,
    private val efficiency: Float,
    private val attackDamage: Float,
    private val enchantability: Int,
    private val repairMaterial: Ingredient
) : IItemTier {
    override fun getHarvestLevel() = harvestLevel
    override fun getMaxUses() = maxUses
    override fun getEfficiency() = efficiency
    override fun getAttackDamage() = attackDamage
    override fun getEnchantability() = enchantability
    override fun getRepairMaterial() = repairMaterial
}