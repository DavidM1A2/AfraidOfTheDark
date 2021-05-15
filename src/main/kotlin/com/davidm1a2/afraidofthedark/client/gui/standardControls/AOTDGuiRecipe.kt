package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.base.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.base.Position
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.crafting.IShapedRecipe

/**
 * Advanced control that displays an entire crafting recipe
 *
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @param recipe The recipe to draw
 * @property craftingGrid The background crafting grid texture
 * @property guiItemStacks The item stacks to draw
 * @property output The itemstack that gets created
 */
class AOTDGuiRecipe(width: Double, height: Double = width/367*267, xOffset: Double, yOffset: Double, recipe: IRecipe<*>? = null) :
    AOTDPane(Position(xOffset, yOffset), Dimensions(width, height)) {
    private val craftingGrid: ImagePane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/journal_page/crafting_grid.png"), ImagePane.DispMode.STRETCH)
    private val guiItemStacks: Array<AOTDGuiItemStack>
    private val output: AOTDGuiItemStack

    init {
        // Setup the crafting grid background image
        this.add(this.craftingGrid)

        // Create an array of 9 stacks for each of the 9 slots and initialize each of the 9 stacks
        this.guiItemStacks = Array(9)
        {
            AOTDGuiItemStack(
                width / 5.0,
                height / 4.0,
                5.0 + it % 3 * 24,    // TODO: Tune these offsets
                6.0 + 26 * (it / 3),
                true
            )
        }

        // Initialize the output stack
        output = AOTDGuiItemStack(24.0, 24.0, 83.0, 31.0, true)

        // Add each stack to the pane to be drawn
        for (guiItemStack in this.guiItemStacks) {
            this.add(guiItemStack)
        }
        this.add(output)

        // Set the recipe to draw
        this.setRecipe(recipe)
    }

    /**
     * Called to draw the control, just draws all of its children
     */
    override fun draw() {
        // If we have no output itemstack we can't draw the recipe
        if (this.isVisible && !this.output.itemStack.isEmpty) {
            super.draw()
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
                        this.guiItemStacks[i + j * 3].itemStack = ingredient.matchingStacks[0]
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
                    this.guiItemStacks[i].itemStack = ingredient.matchingStacks[0]
                }
            }
        }

        // Update the output itemstack
        this.output.itemStack = recipe.recipeOutput
    }
}
