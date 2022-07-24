package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.fml.client.gui.GuiUtils

/**
 * Advanced control that displays an itemstack in a GUI
 */
open class ItemStackPane(
    prefSize: Dimensions,
    offset: Position,
    backgroundHighlight: Boolean = false,
    var itemStack: ItemStack = ItemStack.EMPTY
) :
    AOTDPane(offset, prefSize) {

    private val highlight: ImagePane?

    init {
        // if we should highlight the background then add a highlit background image
        if (backgroundHighlight) {
            highlight = ImagePane(ResourceLocation("afraidofthedark:textures/gui/arcane_journal_page/slot_highlight.png"), ImagePane.DispMode.FIT_TO_PARENT)
            // Start the highlight off
            highlight.isVisible = false
            this.add(highlight)
        } else {
            highlight = null
        }
    }

    /**
     * Render the itemstack
     */
    override fun draw(matrixStack: MatrixStack) {
        // Ensure the control is visible
        if (this.isVisible) {
            // Test if we need to toggle the visibility of the highlit background
            if (this.highlight != null) {
                // Show the highlit background if hovered and hide it if not
                highlight.isVisible = this.isHovered && !this.itemStack.isEmpty
            }

            super.draw(matrixStack)

            // Push a matrix before rendering the item, this code is taken from the inventory class
            matrixStack.pushPose()

            // Grab the render item to draw items
            val renderItem = Minecraft.getInstance().itemRenderer
            // Ensure we have an itemstack to draw
            if (!itemStack.isEmpty) {
                // Attempt to at least center the item because we can't scale them
                val calcX = x + this.width / 2 - 10
                val calcY = y + this.height / 2 - 10
                // Render the itemstack into the GUI
                renderItem.renderGuiItem(itemStack, calcX, calcY)
                renderItem.renderGuiItemDecorations(fontRenderer, itemStack, calcX, calcY)
                // Rendering items disables blend, re-enable it
                RenderSystem.enableBlend()
            }

            // Pop the matrix and disable the item lighting
            matrixStack.popPose()
        }
    }

    /**
     * Draws the overlay that displays the highlit background and item name
     */
    override fun drawOverlay(matrixStack: MatrixStack) {
        // Ensure the control is visible and we have an overlay to draw
        if (this.isVisible && highlight != null) {
            // Ensure the stack is hovered and the interior items are not null
            if (this.isHovered && !this.itemStack.isEmpty) {
                // Get the window width and calculate the distance to the edge of the screen
                val windowWidth = AOTDGuiUtility.getWindowWidthInMCCoords()
                val windowHeight = AOTDGuiUtility.getWindowHeightInMCCoords()
                GuiUtils.drawHoveringText(
                    itemStack,
                    matrixStack,
                    listOf(StringTextComponent("${itemStack.hoverName.string} x${itemStack.count}")),
                    x,
                    y,
                    windowWidth,
                    windowHeight,
                    200,
                    fontRenderer
                )
            }
        }
    }
}
