package com.davidm1a2.afraidofthedark.common.loot.loottable

import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.entity.ChestBlockEntity
import java.util.*

/**
 * Class representing a loot table that can be used by schematics to generate loot
 *
 * @constructor initializes the loot table
 * @param itemProviderToLootTable The loot table entries that make up this loot table
 */
class LootTable(val name: String, itemProviderToLootTable: Map<ItemLike?, ResourceLocation>) {
    private val itemToLootTable = itemProviderToLootTable.mapKeys { it.key?.asItem() }

    /**
     * Generates the appropriate loot table for the chest tile entity based on what's inside
     *
     * @param chest The chest to generate loot in
     */
    fun generate(chest: ChestBlockEntity, chestNBT: CompoundTag, random: Random) {
        // Can't use chest.getStackInSlot() since it requires a valid world object
        val items = NonNullList.withSize(chest.containerSize, ItemStack.EMPTY)
        ContainerHelper.loadAllItems(chestNBT, items)
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