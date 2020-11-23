package com.davidm1a2.afraidofthedark.common.crafting

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.PlayerContainer
import net.minecraft.inventory.container.WorkbenchContainer
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ICraftingRecipe
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.logging.log4j.LogManager

/**
 * Base class representing a recipe that requires a research before it can be crafted
 *
 * @constructor just takes a recipe as base and a pre-requisite recipe to research
 * @param baseRecipe   The base shapeless ore recipe to be automatically created using existing minecraft code
 * @param preRequisite The pre-requisite research to be required to craft this recipe
 */
abstract class ResearchRequiredRecipeBase<T : IRecipe<CraftingInventory>>(val baseRecipe: T, internal val preRequisite: Research) :
    IRecipe<CraftingInventory> by baseRecipe, ICraftingRecipe {
    /**
     * Used to check if a recipe matches current crafting inventory. Also checks if the player has the correct research
     *
     * @param inv The current inventory state
     * @param world The current world the object is being crafted in. Can be null even though it never is in vanilla. See: CoFH core
     * @return True if the crafting recipe matches, false if it does not
     */
    override fun matches(inv: CraftingInventory, world: World?): Boolean {
        // Compute if the recipe matches first
        val matches = baseRecipe.matches(inv, world)

        // Grab the player who did the crafting
        val craftingPlayer = findPlayer(inv)

        // Ensure the player is non-null
        if (craftingPlayer != null) {
            // If the player does not have the research return false
            if (!craftingPlayer.getResearch().isResearched(preRequisite)) {
                // Before returning false notify the player why the crafting failed if the recipe matched
                if (matches && !craftingPlayer.world.isRemote) {
                    craftingPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.crafting.missing_research"))
                }
                return false
            }
        }

        // The player has the research, so return true if the recipe matches or false otherwise
        return matches
    }

    override fun isDynamic(): Boolean {
        return true
    }

    // Default methods can't be delgated, see: https://kotlinlang.org/docs/reference/java-to-kotlin-interop.html#using-in-delegates
    override fun getRemainingItems(inventory: CraftingInventory): NonNullList<ItemStack> {
        return baseRecipe.getRemainingItems(inventory)
    }

    override fun getIngredients(): NonNullList<Ingredient> {
        return baseRecipe.ingredients
    }

    override fun getGroup(): String {
        return baseRecipe.group
    }

    companion object {
        private val logger = LogManager.getLogger()

        /**
         * Finds the player that is crafting the recipe from the InventoryCrafting class using reflection
         *
         * @param inventory The crafting container to break into
         * @return The player who is crafting the recipe or null otherwise
         */
        private fun findPlayer(inventory: CraftingInventory): PlayerEntity? {
            try {
                // Attempt to grab the container the player is crafting in
                val container: Container = ObfuscationReflectionHelper.getPrivateValue(CraftingInventory::class.java, inventory, "field_70465_c")!!

                // Test if the container is a 2x2 grid or 'ContainerPlayer', if so return the player from the 'player' field
                if (container is PlayerContainer) {
                    return ObfuscationReflectionHelper.getPrivateValue(PlayerContainer::class.java, container, "field_82862_h")
                } else if (container is WorkbenchContainer) {
                    return ObfuscationReflectionHelper.getPrivateValue(WorkbenchContainer::class.java, container, "field_192390_i")
                }
            }
            // If something goes wrong catch the exception and log it, then return null
            catch (e: IllegalAccessException) {
                logger.error(
                    "Could not find the player crafting the recipe, error was: ${ExceptionUtils.getStackTrace(e)}"
                )
            }
            return null
        }
    }

    override fun getType(): IRecipeType<*> {
        return super.getType()
    }
}