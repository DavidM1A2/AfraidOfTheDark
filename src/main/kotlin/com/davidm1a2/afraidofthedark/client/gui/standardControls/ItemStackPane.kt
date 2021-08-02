package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

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
            highlight = ImagePane(ResourceLocation("afraidofthedark:textures/gui/journal_page/slot_highlight.png"), ImagePane.DispMode.FIT_TO_PARENT)
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

            // Enable item lighting
            RenderHelper.setupForFlatItems()
            // Push a matrix before rendering the item, this code is taken from the inventory class
            matrixStack.pushPose()

            // Grab the render item to draw items
            val renderItem = Minecraft.getInstance().itemRenderer
            // Ensure we have an itemstack to draw
            if (!itemStack.isEmpty) {
                // Grab the font renderer for the item
                val font = itemStack.item.getFontRenderer(itemStack) ?: Minecraft.getInstance().font
                // Attempt to at least center the item because we can't scale them
                val calcX = x + this.width / 2 - 10
                val calcY = y + this.height / 2 - 10
                // Render the itemstack into the GUI
                renderItem.renderGuiItem(itemStack, calcX, calcY)
            }

            // Pop the matrix and disable the item lighting
            matrixStack.popPose()
            RenderHelper.setupFor3DItems()
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
                // Show the item name and count
                fontRenderer.drawShadow(
                    matrixStack,
                    "${itemStack.displayName.string} x${itemStack.count}",
                    x.toFloat(),
                    (y - 5).toFloat(),
                    -0x1
                )
            }
        }
    }
}
