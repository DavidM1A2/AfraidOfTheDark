package com.davidm1a2.afraidofthedark.common.recipe

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.*
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.ReflectionHelper
import net.minecraftforge.registries.IForgeRegistryEntry
import org.apache.commons.lang3.exception.ExceptionUtils
import java.lang.reflect.Field

/**
 * Base class representing a recipe that requires a research before it can be crafted
 *
 * @constructor just takes a recipe as base and a pre-requisite recipe to research
 * @param baseRecipe   The base shapeless ore recipe to be automatically created using existing minecraft code
 * @param preRequisite The pre-requisite research to be required to craft this recipe
 */
abstract class ResearchRequiredRecipeBase<T : IRecipe>(val baseRecipe: T, private val preRequisite: Research) : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory. Also checks if the player has the correct research
     *
     * @param inv     The current inventory state
     * @param world The current world the object is being crafted in
     * @return True if the crafting recipe matches, false if it does not
     */
    override fun matches(inv: InventoryCrafting, world: World): Boolean
    {
        // Compute if the recipe matches first
        val matches = baseRecipe.matches(inv, world)

        // Grab the player who did the crafting
        val craftingPlayer = findPlayer(inv)

        // Ensure the player is non-null
        if (craftingPlayer != null)
        {
            // If the player does not have the research return false
            if (!craftingPlayer.getResearch().isResearched(preRequisite))
            {
                // Before returning false notify the player why the crafting failed if the recipe matched
                if (matches && !world.isRemote)
                {
                    craftingPlayer.sendMessage(TextComponentTranslation("aotd.crafting.missing_research"))
                }
                return false
            }
        }

        // The player has the research, so return true if the recipe matches or false otherwise
        return matches
    }

    /**
     * Returns an Item that is the result of this recipe
     *
     * @param inv The inventory crafting to work with
     * @return The item that is returned when the recipe is correct
     */
    override fun getCraftingResult(inv: InventoryCrafting): ItemStack
    {
        return baseRecipe.getCraftingResult(inv)
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     *
     * @param width  The width of the crafting grid
     * @param height The height of the crafting rid
     * @return True if the recipe fits in the grid, false otherwise
     */
    override fun canFit(width: Int, height: Int): Boolean
    {
        return baseRecipe.canFit(width, height)
    }

    /**
     * @return The output of the recipe
     */
    override fun getRecipeOutput(): ItemStack
    {
        return baseRecipe.recipeOutput
    }

    /**
     * Returns the items that remain after the crafting is complete given the inventory crafting to start with
     *
     * @param inv The inventory to start with
     * @return A list of items to return to the player after the items are consumed from the crafting grid
     */
    override fun getRemainingItems(inv: InventoryCrafting): NonNullList<ItemStack>
    {
        return baseRecipe.getRemainingItems(inv)
    }

    /**
     * @return The ingredients required for the recipe
     */
    override fun getIngredients(): NonNullList<Ingredient>
    {
        return baseRecipe.ingredients
    }

    /**
     * @return True if the recipe is dynamic, false otherwise
     */
    override fun isDynamic(): Boolean
    {
        return baseRecipe.isDynamic
    }

    /**
     * @return The group the recipe belongs to
     */
    override fun getGroup(): String
    {
        return baseRecipe.group
    }

    companion object
    {
        // 3 reflection fields used in determining which players are actually crafting a given recipe
        private var eventHandlerField: Field
        private var containerPlayerPlayerField: Field
        private var slotCraftingPlayerField: Field

        init
        {
            // If we're in the development client the 3 fields will have de-obsfucated names, grab those here. If we're in the actual
            // client they will have obsfucated names so grab those instead
            try
            {
                eventHandlerField = ReflectionHelper.findField(InventoryCrafting::class.java, "eventHandler")
                containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer::class.java, "player")
                slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting::class.java, "player")
            } catch (_: Exception)
            {
                eventHandlerField = ReflectionHelper.findField(InventoryCrafting::class.java, "field_70465_c")
                containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer::class.java, "field_82862_h")
                slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting::class.java, "field_75238_b")
            }
        }

        /**
         * Finds the player that is crafting the recipe from the InventoryCrafting class using reflection
         *
         * @param inventoryCrafting The crafting container to break into
         * @return The player who is crafting the recipe or null otherwise
         */
        private fun findPlayer(inventoryCrafting: InventoryCrafting): EntityPlayer?
        {
            try
            {
                // Attempt to grab the container the player is crafting in
                val container = eventHandlerField[inventoryCrafting] as Container

                // Test if the container is a 2x2 grid or 'ContainerPlayer', if so return the player from the 'player' field
                if (container is ContainerPlayer)
                {
                    return containerPlayerPlayerField[container] as EntityPlayer
                }
                else if (container is ContainerWorkbench)
                {
                    return slotCraftingPlayerField[container.getSlot(0)] as EntityPlayer
                }
            }
            // If something goes wrong catch the exception and log it, then return null
            catch (e: IllegalAccessException)
            {
                AfraidOfTheDark.INSTANCE.logger.error("Could not find the player crafting the recipe, error was: " + ExceptionUtils.getStackTrace(e))
            }
            return null
        }
    }
}