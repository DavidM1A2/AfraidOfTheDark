package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 * Advanced control that displays an itemstack in a GUI
 *
 * @param x                   The X location of the top left corner
 * @param y                   The Y location of the top left corner
 * @param width               The width of the component
 * @param height              The height of the component
 * @param backgroundHighlight If the background should be highlit when hovered
 * @param itemStack           The itemstack to draw
 * @property highlight The image that is shown when the itemstack is hovered over
 */
class AOTDGuiItemStack(
        prefSize: Dimensions,
        offset: Position,
        backgroundHighlight: Boolean = false,
        var itemStack: ItemStack = ItemStack.EMPTY) :
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
    override fun draw() {
        // Ensure the control is visible
        if (this.isVisible) {
            // Test if we need to toggle the visibility of the highlit background
            if (this.highlight != null) {
                // Show the highlit background if hovered and hide it if not
                highlight.isVisible = this.isHovered && !this.itemStack.isEmpty
            }

            super.draw()

            // Enable item lighting
            RenderHelper.enableGUIStandardItemLighting()
            // Push a matrix before rendering the item, this code is taken from the inventory class
            GL11.glPushMatrix()

            // Translate to the center of the stack
            //GL11.glTranslated(x.toDouble(), y.toDouble(), 1.0)
            //GL11.glTranslated((3 - x).toDouble(), (3 - x).toDouble(), 1.0)

            // Grab the render item to draw items
            val renderItem = Minecraft.getInstance().itemRenderer
            // Set Z level to 100 like in default MC code
            renderItem.zLevel = 100.0f
            // Ensure we have an itemstack to draw
            if (!itemStack.isEmpty) {
                // Grab the font renderer for the item
                val font = itemStack.item.getFontRenderer(itemStack) ?: Minecraft.getInstance().fontRenderer
                // Render the itemstack into the GUI
                renderItem.renderItemAndEffectIntoGUI(itemStack, x, y)
                // Render the itemstack count overlay into the GUI
                renderItem.renderItemOverlayIntoGUI(font, itemStack, x, y, null)
                // Set Z level to 0 like in default MC code
                renderItem.zLevel = 0.0f
            }

            // Pop the matrix and disable the item lighting
            GL11.glPopMatrix()
            RenderHelper.disableStandardItemLighting()
        }
    }

    /**
     * Draws the overlay that displays the highlit background and item name
     */
    override fun drawOverlay() {
        // Ensure the control is visible and we have an overlay to draw
        if (this.isVisible && highlight != null) {
            // Ensure the stack is hovered and the interior items are not null
            if (this.isHovered && !this.itemStack.isEmpty) {
                // Show the item name and count
                fontRenderer.drawStringWithShadow(
                        "${itemStack.displayName.formattedText} x${itemStack.count}",
                        x.toFloat(),
                        (y - 5).toFloat(),
                        -0x1
                )
            }
        }
    }
}
