package com.DavidM1A2.afraidofthedark.common.recipe;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import com.DavidM1A2.afraidofthedark.common.item.ItemResearchScroll;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class that registers a dynamic recipe allowing us to combine scroll pieces
 */
public class ScrollCombineRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    // A reference to the research scroll item
    private final ItemResearchScroll RESEARCH_SCROLL = (ItemResearchScroll) ModItems.RESEARCH_SCROLL;

    /**
     * Constructor sets the registry name
     */
    public ScrollCombineRecipe()
    {
        this.setRegistryName(new ResourceLocation(Constants.MOD_ID, "scroll_combine"));
    }

    /**
     * True if the recipe is matched, false otherwise
     *
     * @param inv     The inventory used to craft the item
     * @param worldIn The world the recipe was crafted in
     * @return True if the recipe works, and false otherwise
     */
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        // Grab a list of non-empty stacks in the crafting grid
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (!itemStack.isEmpty())
                stacks.add(itemStack);
        }

        // Make sure we have at least one itemstack
        if (!stacks.isEmpty())
        {
            // Grab a random itemstack (pick the first)
            ItemStack firstItemStack = stacks.get(0);
            // Check if it's a research scroll and if it's a part-based scroll
            if (firstItemStack.getItem() instanceof ItemResearchScroll && RESEARCH_SCROLL.isPart(firstItemStack))
            {
                // Grab the number of parts required to fufill the scroll
                Integer numberParts = RESEARCH_SCROLL.getNumberParts(firstItemStack);
                // Grab the research of the first scroll
                Research research = RESEARCH_SCROLL.getScrollResearch(firstItemStack);
                // Make sure num parts and research are valid
                if (numberParts != null && research != null)
                {
                    // Go through all itemstacks and make sure they match the first one
                    Set<Integer> partNumbersPresent = stacks.stream()
                            // Make sure they're all research scrolls
                            .filter(itemStack -> itemStack.getItem() instanceof ItemResearchScroll)
                            // Make sure they all have the same research
                            .filter(itemStack -> RESEARCH_SCROLL.getScrollResearch(itemStack) == research)
                            // Make sure they are all parts
                            .filter(RESEARCH_SCROLL::isPart)
                            // Make sure they all have the same max number of parts
                            .filter(itemStack -> RESEARCH_SCROLL.getNumberParts(itemStack).equals(numberParts))
                            // Map the itemstack to the part number and collect it into a set
                            .map(RESEARCH_SCROLL::getPartNumber)
                            .collect(Collectors.toSet());

                    // Check if the number of resulting part numbers is the same as the number of stacks meaning we have enough scrolls to complete the scroll
                    if (partNumbersPresent.size() == stacks.size())
                    {
                        // Ensure we have part numbers 1 through n
                        if (partNumbersPresent.containsAll(IntStream.range(1, numberParts + 1).boxed().collect(Collectors.toList())))
                        {
                            // It's valid!
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns the result of the crafting recipe
     *
     * @param inv The inventory containing items to put together
     * @return The resulting item
     */
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        // Grab a list of non-empty stacks in the crafting grid
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (!itemStack.isEmpty())
                stacks.add(itemStack);
        }

        // We know the stacks are valid from matches()
        ItemStack itemStack = stacks.get(0);
        // Grab the scroll research
        Research scrollResearch = RESEARCH_SCROLL.getScrollResearch(itemStack);

        // Output a finished scroll research
        ItemStack toReturn = new ItemStack(RESEARCH_SCROLL, 1);
        RESEARCH_SCROLL.setScrollResearch(toReturn, scrollResearch);
        return toReturn;
    }

    /**
     * We need at least 2 slots to finish a scroll
     *
     * @param width  The width of the crafting inventory
     * @param height The height of the crafting inventory
     * @return True if wxh is bigger than 2
     */
    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }

    /**
     * This recipe does not do anything without any input
     *
     * @return An empty itemstack
     */
    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    /**
     * This recipe changes based on NBT so it's dynamic
     *
     * @return True, the recipe is dynamic
     */
    @Override
    public boolean isDynamic()
    {
        return true;
    }
}
