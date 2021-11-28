package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ItemStackPane
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.crafting.IShapedRecipe

/**
 * Advanced control that displays an entire crafting recipe
 */
class RecipePane(prefSize: Dimensions, offset: Position = Position(0.0, 0.0), recipe: IRecipe<*>? = null) :
    AOTDPane(offset, prefSize) {
    private val craftingGrid: ImagePane =
        ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_page/crafting_grid.png"), ImagePane.DispMode.FIT_TO_PARENT)
    private val guiItemStacks: Array<ItemStackPane>
    private val output: ItemStackPane

    init {
        // Setup the crafting grid background image
        this.add(this.craftingGrid)

        // Create an array of 9 stacks for each of the 9 slots and initialize each of the 9 stacks
        this.guiItemStacks = Array(9)
        {
            ItemStackPane(
                Dimensions(0.2, 0.33),
                Position((it % 3) * 0.22 + 0.055, (it / 3) * 0.3 + 0.045),
                true
            )
        }

        // Initialize the output stack
        output = ItemStackPane(Dimensions(0.2, 0.33), Position(0.78, 0.35), true)

        // Add each stack to the pane to be drawn
        for (guiItemStack in this.guiItemStacks) {
            craftingGrid.add(guiItemStack)
        }
        craftingGrid.add(output)

        // Set the recipe to draw
        this.setRecipe(recipe)
    }

    /**
     * Called to draw the control, just draws all of its children
     */
    override fun draw(matrixStack: MatrixStack) {
        // If we have no output itemstack we can't draw the recipe
        if (this.isVisible && !this.output.itemStack.isEmpty) {
            super.draw(matrixStack)
        }
    }

    /**
     * Sets the recipe to be drawn
     *
     * @param recipe The recipe to draw
     */
    fun setRecipe(recipe: IRecipe<*>?) {
        // If the recipe is invalid just return
        if (recipe == null) {
            this.isVisible = false
            return
        }

        // Show the recipe
        this.isVisible = true

        // Set all the output stacks to null for now
        for (guiItemStack in this.guiItemStacks) {
            guiItemStack.itemStack = ItemStack.EMPTY
        }

        // Update each gui stack with the new ingredient
        // Shaped recipes are rendered differently than shapeless
        if (recipe is IShapedRecipe) {
            // Shaped recipe has width and height
            val width = recipe.recipeWidth
            val height = recipe.recipeHeight
            // Iterate over the width and height and set the stack in each slot
            for (i in 0 until width) {
                for (j in 0 until height) {
                    // Grab the ingredient in that slot
                    val ingredient = recipe.ingredients[i + j * width]
                    // If the ingredient is non-empty show it
                    if (ingredient !== Ingredient.EMPTY) {
                        this.guiItemStacks[i + j * 3].itemStack = ingredient.items[0]
                    }
                }
            }
        } else {
            // Shapeless recipes have no shape so just go from the beginning to the end and render each ingredient
            for (i in 0 until recipe.ingredients.size) {
                // Grab the ingredient in that slot
                val ingredient = recipe.ingredients[i]
                // If the ingredient is non-empty show it
                if (ingredient != Ingredient.EMPTY) {
                    this.guiItemStacks[i].itemStack = ingredient.items[0]
                }
            }
        }

        // Update the output itemstack
        this.output.itemStack = recipe.resultItem
    }
}
