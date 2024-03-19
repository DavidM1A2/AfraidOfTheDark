package com.davidm1a2.afraidofthedark.common.crafting

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.inventory.CraftingMenu
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraftforge.fml.util.ObfuscationReflectionHelper
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.logging.log4j.LogManager

/**
 * Base class representing a recipe that requires a research before it can be crafted
 *
 * @constructor just takes a recipe as base and a pre-requisite recipe to research
 * @param baseRecipe   The base shapeless ore recipe to be automatically created using existing minecraft code
 * @param preRequisite The pre-requisite research to be required to craft this recipe
 */
abstract class ResearchRequiredRecipeBase<T : Recipe<CraftingContainer>>(val baseRecipe: T, internal val preRequisite: Research) :
    Recipe<CraftingContainer> by baseRecipe, CraftingRecipe {
    /**
     * Used to check if a recipe matches current crafting inventory. Also checks if the player has the correct research
     *
     * @param inv The current inventory state
     * @param world The current world the object is being crafted in. Can be null even though it never is in vanilla. See: CoFH core
     * @return True if the crafting recipe matches, false if it does not
     */
    override fun matches(inv: CraftingContainer, world: Level?): Boolean {
        // Compute if the recipe matches first. IntelliJ claims "world" can't be null, but it 100% can be
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val matches = baseRecipe.matches(inv, world)

        // Grab the player who did the crafting
        val craftingPlayer = findPlayer(inv) ?: return false

        // If the player does not have the research return false
        if (!craftingPlayer.getResearch().isResearched(preRequisite)) {
            // Before returning false notify the player why the crafting failed if the recipe matched
            if (matches && !craftingPlayer.level.isClientSide) {
                craftingPlayer.sendMessage(TranslatableComponent("message.afraidofthedark.crafting.missing_research"))
            }
            return false
        }

        // The player has the research, so return true if the recipe matches or false otherwise
        return matches
    }

    override fun isSpecial(): Boolean {
        return true
    }

    // Default methods can't be delgated, see: https://kotlinlang.org/docs/reference/java-to-kotlin-interop.html#using-in-delegates
    override fun getRemainingItems(inventory: CraftingContainer): NonNullList<ItemStack> {
        return baseRecipe.getRemainingItems(inventory)
    }

    override fun getIngredients(): NonNullList<Ingredient> {
        return baseRecipe.ingredients
    }

    override fun getGroup(): String {
        return baseRecipe.group
    }

    companion object {
        private val LOG = LogManager.getLogger()

        /**
         * Finds the player that is crafting the recipe from the InventoryCrafting class using reflection
         *
         * @param inventory The crafting container to break into
         * @return The player who is crafting the recipe or null otherwise
         */
        private fun findPlayer(inventory: CraftingContainer): Player? {
            // TOOD: Confirm reflection field SRG mappings
            try {
                // Attempt to grab the container the player is crafting in
                val container: AbstractContainerMenu? = ObfuscationReflectionHelper.getPrivateValue(CraftingContainer::class.java, inventory, "field_70465_c")

                // Test if the container is a 2x2 grid or 'ContainerPlayer', if so return the player from the 'player' field
                if (container is InventoryMenu) {
                    return ObfuscationReflectionHelper.getPrivateValue(InventoryMenu::class.java, container, "field_82862_h")
                } else if (container is CraftingMenu) {
                    return ObfuscationReflectionHelper.getPrivateValue(CraftingMenu::class.java, container, "field_192390_i")
                }
            }
            // If something goes wrong catch the exception and log it, then return null
            catch (e: ObfuscationReflectionHelper.UnableToFindFieldException) {
                LOG.error("Could not find the player crafting the recipe, error was: ${ExceptionUtils.getStackTrace(e)}")
            }
            return null
        }
    }

    override fun getType(): RecipeType<*> {
        return super.getType()
    }
}