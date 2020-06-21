package com.davidm1a2.afraidofthedark.common.recipe

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.*
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.text.TextComponentTranslation
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
abstract class ResearchRequiredRecipeBase<T : IRecipe>(val baseRecipe: T, internal val preRequisite: Research) : IRecipe by baseRecipe {
    /**
     * Used to check if a recipe matches current crafting inventory. Also checks if the player has the correct research
     *
     * @param inv The current inventory state
     * @param world The current world the object is being crafted in. Can be null even though it never is in vanilla. See: CoFH core
     * @return True if the crafting recipe matches, false if it does not
     */
    override fun matches(inv: IInventory, world: World?): Boolean {
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
                    craftingPlayer.sendMessage(TextComponentTranslation(LocalizationConstants.Crafting.MISSING_RESEARCH))
                }
                return false
            }
        }

        // The player has the research, so return true if the recipe matches or false otherwise
        return matches
    }

    companion object {
        private val logger = LogManager.getLogger()

        /**
         * Finds the player that is crafting the recipe from the InventoryCrafting class using reflection
         *
         * @param inventory The crafting container to break into
         * @return The player who is crafting the recipe or null otherwise
         */
        private fun findPlayer(inventory: IInventory): EntityPlayer? {
            try {
                if (inventory is InventoryCrafting) {
                    // Attempt to grab the container the player is crafting in
                    val container: Container = ObfuscationReflectionHelper.getPrivateValue(InventoryCrafting::class.java, inventory, "eventHandler")

                    // Test if the container is a 2x2 grid or 'ContainerPlayer', if so return the player from the 'player' field
                    if (container is ContainerPlayer) {
                        return ObfuscationReflectionHelper.getPrivateValue(ContainerPlayer::class.java, container, "player")
                    } else if (container is ContainerWorkbench) {
                        return ObfuscationReflectionHelper.getPrivateValue(ContainerWorkbench::class.java, container, "player")
                    }
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
}