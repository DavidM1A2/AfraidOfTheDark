package com.DavidM1A2.afraidofthedark.common.worldGeneration;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * Class representing a loot table that can be used by schematics to generate loot
 */
public class LootTable
{
    // Array of loot table entries that this loot table will reference
    private final Map<Item, ResourceLocation> itemToLootTable;

    /**
     * Constructor initializes the loot table
     *
     * @param itemToLootTable The loot table entries that make up this loot table
     */
    public LootTable(Map<Item, ResourceLocation> itemToLootTable)
    {
        this.itemToLootTable = itemToLootTable;
    }

    /**
     * Generates the appropriate loot table for the chest tile entity based on what's inside
     *
     * @param chest The chest to generate loot in
     */
    public void generate(TileEntityChest chest)
    {
        // Iterate over the chest's inventory
        for (int i = 0; i < chest.getSizeInventory(); i++)
        {
            ItemStack stackInSlot = chest.getStackInSlot(i);
            // If we find a non-empty slot test if we know what loot table that corresponds to
            if (!stackInSlot.isEmpty() && this.itemToLootTable.containsKey(stackInSlot.getItem()))
            {
                // Clear the chest's inventory and update the loot table. Then return since we're done
                chest.clear();
                chest.setLootTable(this.itemToLootTable.get(stackInSlot.getItem()), chest.getWorld().rand.nextLong());
                return;
            }
        }
        // No item matched the loot table, so attempt to use the default loot table with key = null
        if (this.itemToLootTable.containsKey(null))
        {
            chest.clear();
            chest.setLootTable(this.itemToLootTable.get(null), chest.getWorld().rand.nextLong());
        }
    }
}
