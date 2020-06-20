package com.davidm1a2.afraidofthedark.common.worldGeneration

import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.ResourceLocation

/**
 * Class representing a loot table that can be used by schematics to generate loot
 *
 * @constructor initializes the loot table
 * @param itemToLootTable The loot table entries that make up this loot table
 */
class LootTable(private val itemToLootTable: Map<Item?, ResourceLocation>) {
    /**
     * Generates the appropriate loot table for the chest tile entity based on what's inside
     *
     * @param chest The chest to generate loot in
     */
    fun generate(chest: TileEntityChest) {
        // Iterate over the chest's inventory
        for (i in 0 until chest.sizeInventory) {
            val stackInSlot = chest.getStackInSlot(i)
            // If we find a non-empty slot test if we know what loot table that corresponds to
            if (!stackInSlot.isEmpty && itemToLootTable.containsKey(stackInSlot.item)) {
                // Clear the chest's inventory and update the loot table. Then return since we're done
                chest.clear()
                chest.setLootTable(itemToLootTable[stackInSlot.item]!!, chest.world!!.rand.nextLong())
                return
            }
        }

        // No item matched the loot table, so attempt to use the default loot table with key = null
        if (itemToLootTable.containsKey(null)) {
            chest.clear()
            chest.setLootTable(itemToLootTable[null]!!, chest.world!!.rand.nextLong())
        }
    }
}