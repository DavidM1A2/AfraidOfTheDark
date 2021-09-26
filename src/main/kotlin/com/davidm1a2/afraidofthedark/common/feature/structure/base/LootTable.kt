package com.davidm1a2.afraidofthedark.common.feature.structure.base

import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.ChestTileEntity
import net.minecraft.util.IItemProvider
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import java.util.Random

/**
 * Class representing a loot table that can be used by schematics to generate loot
 *
 * @constructor initializes the loot table
 * @param itemProviderToLootTable The loot table entries that make up this loot table
 */
class LootTable(val name: String, itemProviderToLootTable: Map<IItemProvider?, ResourceLocation>) {
    private val itemToLootTable = itemProviderToLootTable.mapKeys { it.key?.asItem() }

    /**
     * Generates the appropriate loot table for the chest tile entity based on what's inside
     *
     * @param chest The chest to generate loot in
     */
    fun generate(chest: ChestTileEntity, chestNBT: CompoundNBT, random: Random) {
        // Can't use chest.getStackInSlot() since it requires a valid world object
        val items = NonNullList.withSize(chest.containerSize, ItemStack.EMPTY)
        ItemStackHelper.loadAllItems(chestNBT, items)
        // Iterate over the chest's inventory
        for (i in 0 until chest.containerSize) {
            val stackInSlot = items[i]
            // If we find a non-empty slot test if we know what loot table that corresponds to
            if (!stackInSlot.isEmpty && itemToLootTable.containsKey(stackInSlot.item)) {
                // Clear the chest's inventory and update the loot table. Then return since we're done
                chest.clearContent()
                chest.setLootTable(itemToLootTable[stackInSlot.item]!!, random.nextLong())
                return
            }
        }

        // No item matched the loot table, so attempt to use the default loot table with key = null
        if (itemToLootTable.containsKey(null)) {
            chest.clearContent()
            chest.setLootTable(itemToLootTable[null]!!, random.nextLong())
        }
    }
}