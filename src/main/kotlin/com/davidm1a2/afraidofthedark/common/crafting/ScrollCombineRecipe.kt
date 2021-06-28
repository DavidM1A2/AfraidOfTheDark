package com.davidm1a2.afraidofthedark.common.crafting

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModRecipeSerializers
import com.davidm1a2.afraidofthedark.common.item.ResearchScrollItem
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ICraftingRecipe
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

/**
 * Class that registers a dynamic recipe allowing us to combine scroll pieces
 *
 * @constructor sets the registry name
 */
class ScrollCombineRecipe : ICraftingRecipe {
    /**
     * True if the recipe is matched, false otherwise
     *
     * @param inv The inventory used to craft the item
     * @param world The current world the object is being crafted in. Can be null even though it never is in vanilla. See: CoFH core
     * @return True if the recipe works, and false otherwise
     */
    override fun matches(inv: CraftingInventory, world: World?): Boolean {
        // Grab a list of non-empty stacks in the crafting grid
        val stacks = mutableListOf<ItemStack>()
        for (i in 0 until inv.sizeInventory) {
            val itemStack = inv.getStackInSlot(i)
            if (!itemStack.isEmpty) {
                stacks.add(itemStack)
            }
        }

        // Make sure we have at least one itemstack
        if (stacks.isNotEmpty()) {
            // Grab a random itemstack (pick the first)
            val firstItemStack = stacks[0]

            // Check if it's a research scroll and if it's a part-based scroll
            if (firstItemStack.item is ResearchScrollItem && RESEARCH_SCROLL.isPart(firstItemStack)) {
                // Grab the number of parts required to fufill the scroll
                val numberParts = RESEARCH_SCROLL.getNumberParts(firstItemStack)
                // Grab the research of the first scroll
                val research = RESEARCH_SCROLL.getScrollResearch(firstItemStack)

                // Make sure num parts and research are valid
                if (research != null) {
                    // Go through all itemstacks and make sure they match the first one
                    val partNumbersPresent = stacks.asSequence()
                        // Make sure they're all research scrolls
                        .filter { it.item is ResearchScrollItem }
                        // Make sure they all have the same research
                        .filter { RESEARCH_SCROLL.getScrollResearch(it) == research }
                        // Make sure they are all parts
                        .filter { RESEARCH_SCROLL.isPart(it) }
                        // Make sure they all have the same max number of parts
                        .filter { RESEARCH_SCROLL.getNumberParts(it) == numberParts }
                        // Map the itemstack to the part number and collect it into a set
                        .map { RESEARCH_SCROLL.getPartNumber(it) }
                        .toList()

                    // Check if the number of resulting part numbers is the same as the number of stacks meaning we have enough scrolls to complete the scroll
                    if (partNumbersPresent.size == stacks.size) {
                        // Ensure we have part numbers 1 through n, then It's valid!
                        return partNumbersPresent.containsAll((1..numberParts).toList())
                    }
                }
            }
        }
        return false
    }

    /**
     * Returns the result of the crafting recipe
     *
     * @param inv The inventory containing items to put together
     * @return The resulting item
     */
    override fun getCraftingResult(inv: CraftingInventory): ItemStack {
        // Grab a list of non-empty stacks in the crafting grid
        val stacks = mutableListOf<ItemStack>()
        for (i in 0 until inv.sizeInventory) {
            val itemStack = inv.getStackInSlot(i)
            if (!itemStack.isEmpty) {
                stacks.add(itemStack)
            }
        }

        // We know the stacks are valid from matches()
        val itemStack = stacks[0]

        // Grab the scroll research
        val scrollResearch = RESEARCH_SCROLL.getScrollResearch(itemStack)!!

        // Output a finished scroll research
        val toReturn = ItemStack(RESEARCH_SCROLL, 1)
        RESEARCH_SCROLL.setScrollResearch(toReturn, scrollResearch)
        return toReturn
    }

    /**
     * We need at least 2 slots to finish a scroll
     *
     * @param width  The width of the crafting inventory
     * @param height The height of the crafting inventory
     * @return True if wxh is bigger than 2
     */
    override fun canFit(width: Int, height: Int): Boolean {
        return width * height >= 2
    }

    /**
     * This recipe does not do anything without any input
     *
     * @return An empty itemstack
     */
    override fun getRecipeOutput(): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getId(): ResourceLocation {
        return ResourceLocation(Constants.MOD_ID, "scroll_combine")
    }

    override fun getSerializer(): IRecipeSerializer<*> {
        return ModRecipeSerializers.SCROLL_COMBINE_RECIPE
    }

    /**
     * This recipe changes based on NBT so it's dynamic
     *
     * @return True, the recipe is dynamic
     */
    override fun isDynamic(): Boolean {
        return true
    }

    companion object {
        // A reference to the research scroll item
        private val RESEARCH_SCROLL = ModItems.RESEARCH_SCROLL
    }
}