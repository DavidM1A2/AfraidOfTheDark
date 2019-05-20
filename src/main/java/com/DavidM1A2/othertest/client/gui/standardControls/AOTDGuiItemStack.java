package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

/**
 * Advanced control that displays an itemstack in a GUI
 */
public class AOTDGuiItemStack extends AOTDGuiContainer
{
    // The itemstack to draw
    private ItemStack itemStack;
    // The image that is shown when the itemstack is hovered over
    private AOTDGuiImage highlight;

    /**
     * Control that shows an itemstack in the GUI
     *
     * @param x                   The X location of the top left corner
     * @param y                   The Y location of the top left corner
     * @param width               The width of the component
     * @param height              The height of the component
     * @param itemStack           The itemstack to draw
     * @param backgroundHighlight If the background should be highlit when hovered
     */
    public AOTDGuiItemStack(int x, int y, int width, int height, ItemStack itemStack, boolean backgroundHighlight)
    {
        super(x, y, width, height);
        this.itemStack = itemStack;
        // if we should highlight the background then add a highlit background image
        if (backgroundHighlight)
        {
            highlight = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/journal_page/slot_highlight.png");
            // Start the highlight off
            highlight.setVisible(false);
            this.add(highlight);
        }
    }

    /**
     * Render the itemstack
     */
    @Override
    public void draw()
    {
        // Ensure the control is visible
        if (this.isVisible())
        {
            // Test if we need to toggle the visibility of the highlit background
            if (this.highlight != null)
            {
                // Show the highlit background if hovered and hide it if not
                if (this.isHovered() && this.itemStack != null)
                {
                    highlight.setVisible(true);
                }
                else
                {
                    this.highlight.setVisible(false);
                }
            }

            super.draw();

            // Enable item lighting
            RenderHelper.enableGUIStandardItemLighting();
            // Push a matrix before rendering the item, this code is taken from the inventory class
            GL11.glPushMatrix();

            // Translate to the center of the stack
            GL11.glTranslated(this.getXScaled(), this.getYScaled(), 1.0);
            GL11.glScaled(this.getScaleX(), this.getScaleY(), 1.0D);
            GL11.glTranslated(3 - this.getXScaled(), 3 - this.getYScaled(), 1.0);

            // Grab the render item to draw items
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            // Set Z level to 100 like in default MC code
            renderItem.zLevel = 100.0F;
            FontRenderer font;
            // Ensure we have an itemstack to draw
            if (itemStack != null)
            {
                // Grab the font renderer for the item
                font = itemStack.getItem().getFontRenderer(itemStack);
                if (font == null)
                {
                    font = Minecraft.getMinecraft().fontRenderer;
                }
                // Render the itemstack into the GUI
                renderItem.renderItemAndEffectIntoGUI(itemStack, this.getXScaled(), this.getYScaled());
                // Render the itemstack count overlay into the GUI
                renderItem.renderItemOverlayIntoGUI(font, itemStack, this.getXScaled(), this.getYScaled(), null);
                // Set Z level to 0 like in default MC code
                renderItem.zLevel = 0.0F;
            }

            // Pop the matrix and disable the item lighting
            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();
        }
    }

    /**
     * Draws the overlay that displays the highlit background and item name
     */
    @Override
    public void drawOverlay()
    {
        // Ensure the control is visible and we have an overlay to draw
        if (this.isVisible() && highlight != null)
        // Ensure the stack is hovered and the interior items are not null
        {
            if (this.isHovered() && itemStack != null)
            // Show the item name and count
            {
                fontRenderer.drawStringWithShadow(itemStack.getDisplayName() + " x" + itemStack.getCount(), this.getXScaled(), this.getYScaled() - 5, 0xFFFFFFFF);
            }
        }
    }

    /**
     * @return The itemstack to draw
     */
    public ItemStack getItemStack()
    {
        return this.itemStack;
    }

    /**
     * Sets the itemstack to draw
     *
     * @param itemStack The itemstack to draw
     */
    public void setItemStack(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }
}
