package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import net.minecraftforge.oredict.OreDictionary

/**
 * Class that registers all of our mod's ore dictionary entries
 */
object OreDictionaryRegister {
    /**
     * Initializes the item <-> ore dictionary mapping
     */
    fun initialize() {
        OreDictionary.registerOre("logWood", ModBlocks.GRAVEWOOD)
        OreDictionary.registerOre("plankWood", ModBlocks.GRAVEWOOD_PLANKS)
        OreDictionary.registerOre("treeLeaves", ModBlocks.GRAVEWOOD_LEAVES)
        OreDictionary.registerOre("slabWood", ModBlocks.GRAVEWOOD_HALF_SLAB)
        OreDictionary.registerOre("slabWood", ModBlocks.GRAVEWOOD_DOUBLE_SLAB)
        OreDictionary.registerOre("treeSapling", ModBlocks.GRAVEWOOD_SAPLING)
        OreDictionary.registerOre("stairWood", ModBlocks.GRAVEWOOD_STAIRS)
        OreDictionary.registerOre("fenceWood", ModBlocks.GRAVEWOOD_FENCE)
        OreDictionary.registerOre("fenceGateWood", ModBlocks.GRAVEWOOD_FENCE_GATE)
        // Door blocks don't use default items, instead they have their own custom item
        OreDictionary.registerOre("doorWood", ModItems.GRAVEWOOD_DOOR)

        OreDictionary.registerOre("logWood", ModBlocks.MANGROVE)
        OreDictionary.registerOre("plankWood", ModBlocks.MANGROVE_PLANKS)
        OreDictionary.registerOre("treeLeaves", ModBlocks.MANGROVE_LEAVES)
        OreDictionary.registerOre("slabWood", ModBlocks.MANGROVE_HALF_SLAB)
        OreDictionary.registerOre("slabWood", ModBlocks.MANGROVE_DOUBLE_SLAB)
        OreDictionary.registerOre("treeSapling", ModBlocks.GRAVEWOOD_SAPLING)
        OreDictionary.registerOre("stairWood", ModBlocks.MANGROVE_STAIRS)
        OreDictionary.registerOre("fenceWood", ModBlocks.MANGROVE_FENCE)
        OreDictionary.registerOre("fenceGateWood", ModBlocks.MANGROVE_FENCE_GATE)
        // Door blocks don't use default items, instead they have their own custom item
        OreDictionary.registerOre("doorWood", ModItems.MANGROVE_DOOR)

        OreDictionary.registerOre("logWood", ModBlocks.SACRED_MANGROVE)
        OreDictionary.registerOre("plankWood", ModBlocks.SACRED_MANGROVE_PLANKS)
        OreDictionary.registerOre("treeLeaves", ModBlocks.SACRED_MANGROVE_LEAVES)
        OreDictionary.registerOre("slabWood", ModBlocks.SACRED_MANGROVE_HALF_SLAB)
        OreDictionary.registerOre("slabWood", ModBlocks.SACRED_MANGROVE_DOUBLE_SLAB)
        OreDictionary.registerOre("treeSapling", ModBlocks.GRAVEWOOD_SAPLING)
        OreDictionary.registerOre("stairWood", ModBlocks.SACRED_MANGROVE_STAIRS)
        OreDictionary.registerOre("fenceWood", ModBlocks.SACRED_MANGROVE_FENCE)
        OreDictionary.registerOre("fenceGateWood", ModBlocks.SACRED_MANGROVE_FENCE_GATE)
        // Door blocks don't use default items, instead they have their own custom item
        OreDictionary.registerOre("doorWood", ModItems.SACRED_MANGROVE_DOOR)
    }
}