package com.DavidM1A2.afraidofthedark.client.gui.base;

import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.util.Color;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

import java.util.Arrays;
import java.util.Optional;

/**
 * Base class for all GUI components like labels, buttons, etc
 */
public abstract class AOTDGuiComponent
{
    // A reference to the EntityPlayer object that opened the GUI
    protected final EntityPlayerSP entityPlayer = Minecraft.getMinecraft().player;
    // A reference to the font renderer that is used to draw fonts
    protected final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    // The X scale of the component
    private Double scaleX = 1.0;
    // The Y scale of the component
    private Double scaleY = 1.0;
    // True if the element is hovered by the mouse, false otherwise
    private Boolean isHovered = false;
    // True if the element is visible, false otherwise
    private Boolean isVisible = true;
    // The raw bounding box of the gui component, this is never changed
    private Rectangle boundingBox;
    // Scaled bounding box of the gui component, this is changed if the scale changes
    private Rectangle scaledBoundingBox = new Rectangle(0, 0, 0, 0);
    // The tint that this gui component should be drawn with
    private Color tint = new Color(255, 255, 255, 255);
    // A list of strings to draw when the component is hovered
    private String[] hoverTexts = ArrayUtils.EMPTY_STRING_ARRAY;

    /**
     * Constructor initializes the bounding box
     *
     * @param x      The X location of the top left corner
     * @param y      The Y location of the top left corner
     * @param width  The width of the component
     * @param height The height of the component
     */
    public AOTDGuiComponent(Integer x, Integer y, Integer width, Integer height)
    {
        this.boundingBox = new Rectangle(x, y, width, height);
    }

    /**
     * Draw function that gets called every frame. This needs to be overridden to draw custom controls
     */
    public void draw()
    {
        // Draw the bounding box for debug purposes
        //this.drawBoundingBox();
    }

    /**
     * Draws the hover text that appears when we mouse over the control
     */
    public void drawOverlay()
    {
        // Make sure the control is visible and hovered
        if (this.isVisible && this.isHovered)
        {
            // Find the longest string in the hover texts array
            Optional<Integer> maxHoverTextLengthOpt = Arrays.stream(this.hoverTexts).map(fontRenderer::getStringWidth).max(Integer::compareTo);
            // If it exists, draw the text
            if (maxHoverTextLengthOpt.isPresent())
            {
                // Grab the maximum text length
                Integer maxHoverTextLength = maxHoverTextLengthOpt.get();
                // Grab the mouse X and Y coordinates to draw at
                Integer mouseX = AOTDGuiUtility.getInstance().getMouseXInMCCoord();
                Integer mouseY = AOTDGuiUtility.getInstance().getMouseYInMCCoord();
                // Draw a background rectangle
                Gui.drawRect(mouseX + 2, mouseY - 2, mouseX + maxHoverTextLength + 7, mouseY + fontRenderer.FONT_HEIGHT * this.hoverTexts.length, new Color(140, 0, 0, 0).hashCode());
                // For each hover text in the array draw one line at a time
                for (int i = 0; i < hoverTexts.length; i++)
                {
                    // Grab the hover text to draw
                    String hoverText = hoverTexts[i];
                    // Draw the string
                    fontRenderer.drawStringWithShadow(hoverText, mouseX + 5, mouseY + i * fontRenderer.FONT_HEIGHT, new Color(255, 255, 255).hashCode());
                }
            }
        }
    }

    /**
     * Utility function to draw the bounding box of the GUI component, useful for debug only
     */
    public void drawBoundingBox()
    {
        int whiteColor = new Color(255, 255, 255, 255).hashCode();
        Gui.drawRect(this.getXScaled(), this.getYScaled(), this.getXScaled() + this.getWidthScaled(), this.getYScaled() + 1, whiteColor);
        Gui.drawRect(this.getXScaled(), this.getYScaled(), this.getXScaled() + 1, this.getYScaled() + this.getHeightScaled(), whiteColor);
        Gui.drawRect(this.getXScaled() + this.getWidthScaled() - 1, this.getYScaled(), this.getXScaled() + this.getWidthScaled(), this.getYScaled() + this.getHeightScaled(), whiteColor);
        Gui.drawRect(this.getXScaled(), this.getYScaled() + this.getHeightScaled() - 1, this.getXScaled() + this.getWidthScaled(), this.getYScaled() + this.getHeightScaled(), whiteColor);
    }

    /**
     * Returns true if the current component intersects the other component, or false if not
     *
     * @param other The other gui component to test intersection of
     * @return True if the components intersect, false otherwise
     */
    public Boolean intersects(AOTDGuiComponent other)
    {
        return this.intersects(other.scaledBoundingBox);
    }

    /**
     * Returns true if the current component intersects the point, or false if not
     *
     * @param point The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    public Boolean intersects(Point point)
    {
        if (point == null)
        {
            return false;
        }
        return this.scaledBoundingBox.contains(point);
    }

    /**
     * Returns true if the current component intersects the rectangle, or false if not
     *
     * @param rectangle The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    public boolean intersects(Rectangle rectangle)
    {
        if (rectangle == null)
        {
            return false;
        }
        return this.scaledBoundingBox.intersects(rectangle);
    }

    /**
     * Setter for X and Y scale, also updates the scaled bounding box
     *
     * @param scale The new X and Y scale
     */
    public void setScaleXAndY(double scale)
    {
        this.scaleX = scale;
        this.scaleY = scale;
        this.updateScaledBounds();
    }

    /**
     * @return Getter for the X scale
     */
    public Double getScaleX()
    {
        return this.scaleX;
    }

    /**
     * Setter for X scale, also updates the scaled bounding box
     *
     * @param scaleX The new X scale to use
     */
    public void setScaleX(double scaleX)
    {
        this.scaleX = scaleX;
        this.updateScaledBounds();
    }

    /**
     * @return Getter for Y scale
     */
    public Double getScaleY()
    {
        return this.scaleY;
    }

    /**
     * Setter for Y scale, also updates the scaled bounding box
     *
     * @param scaleY The new Y scale to use
     */
    public void setScaleY(double scaleY)
    {
        this.scaleY = scaleY;
        this.updateScaledBounds();
    }

    /**
     * @return Getter for the top left corner's X value
     */
    public int getX()
    {
        return this.boundingBox.getX();
    }

    /**
     * Setter for bounding box X
     *
     * @param x The new x position of the component
     */
    public void setX(int x)
    {
        this.boundingBox.setX(x);
        this.updateScaledBounds();
    }

    /**
     * @return Getter for the scaled bounding box's top corner's X value
     */
    public Integer getXScaled()
    {
        return this.scaledBoundingBox.getX();
    }

    /**
     * @return Getter for the top left corner's Y value
     */
    public int getY()
    {
        return this.boundingBox.getY();
    }

    /**
     * Setter for bounding box Y
     *
     * @param y The new y position of the component
     */
    public void setY(int y)
    {
        this.boundingBox.setY(y);
        this.updateScaledBounds();
    }

    /**
     * @return Getter for the scaled bounding box's top corner's Y value
     */
    public Integer getYScaled()
    {
        return this.scaledBoundingBox.getY();
    }

    /**
     * @return Getter for the component's width
     */
    public int getWidth()
    {
        return this.boundingBox.getWidth();
    }

    /**
     * Setter for the width of the component
     *
     * @param width The new component's width
     */
    public void setWidth(int width)
    {
        this.boundingBox.setWidth(width);
        this.updateScaledBounds();
    }

    /**
     * @return Getter for the component's scaled width
     */
    public int getWidthScaled()
    {
        return this.scaledBoundingBox.getWidth();
    }

    /**
     * @return Getter for the height of the component
     */
    public int getHeight()
    {
        return this.boundingBox.getHeight();
    }

    /**
     * Setter for the component's height
     *
     * @param height The new height of the component
     */
    public void setHeight(int height)
    {
        this.boundingBox.setHeight(height);
        this.updateScaledBounds();
    }

    /**
     * @return Getter for the scaled height of the component
     */
    public int getHeightScaled()
    {
        return this.scaledBoundingBox.getHeight();
    }

    /**
     * Updates the scaled bounding box from the current X and Y scale
     */
    public void updateScaledBounds()
    {
        // Compute new X, Y, Width, and Height by scaling the original bounding box and saving it into the new bounding box
        int xNew = Math.toIntExact(Math.round(this.scaleX * this.boundingBox.getX()));
        int yNew = Math.toIntExact(Math.round(this.scaleY * this.boundingBox.getY()));
        int widthNew = Math.toIntExact(Math.round(this.scaleX * this.boundingBox.getWidth()));
        int heightNew = Math.toIntExact(Math.round(this.scaleY * this.boundingBox.getHeight()));
        this.scaledBoundingBox.setBounds(xNew, yNew, widthNew, heightNew);
    }

    /**
     * @return True if the component is visible, false otherwise
     */
    public boolean isVisible()
    {
        return isVisible;
    }

    /**
     * Setter for the visibility property of the component
     *
     * @param isVisible If the component should be visible or not
     */
    public void setVisible(boolean isVisible)
    {
        this.isVisible = isVisible;
    }

    /**
     * @return True if the component is hovered, false otherwise
     */
    public boolean isHovered()
    {
        return isHovered;
    }

    /**
     * Setter for the hovered property of the component
     *
     * @param isHovered If the component should be hovered or not
     */
    public void setHovered(boolean isHovered)
    {
        this.isHovered = isHovered;
    }

    /**
     * Setter for the tint color of this component
     *
     * @param r The r color that this component should be
     * @param g The g color that this component should be
     * @param b The b color that this component should be
     * @param a The a color that this component should be
     */
    public void setColor(int r, int g, int b, int a)
    {
        this.tint.set(r, g, b, a);
    }

    /**
     * @return Getter for tint color of this component
     */
    public Color getColor()
    {
        return this.tint;
    }

    /**
     * Setter for the tint color of this component
     *
     * @param tint The color that this component should be
     */
    public void setColor(Color tint)
    {
        this.tint = tint;
    }

    /**
     * @return Gets the current list of hover texts
     */
    public String[] getHoverTexts()
    {
        return this.hoverTexts;
    }

    /**
     * Raw setter for hover texts array
     *
     * @param hoverTexts The new text to show when highlighting the component
     */
    public void setHoverTexts(String... hoverTexts)
    {
        this.hoverTexts = hoverTexts;
    }

    /**
     * Second getter for hover text, this one concatenates the hover texts back together with '\n' characters
     *
     * @return The hover text concatenated together
     */
    public String getHoverText()
    {
        return String.join("\n", this.hoverTexts);
    }

    /**
     * Second setter for hover texts, this one assumes the parameter is a '\n' delimited string to be converted into an array
     *
     * @param hoverText The new text to be the hover text of this component
     */
    public void setHoverText(String hoverText)
    {
        this.hoverTexts = hoverText.split("\n");
    }

    /**
     * @return Override this so that our GUI components can be printed for easy debugging
     */
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " located at " + this.boundingBox + " with a scaled resolution of " + this.scaledBoundingBox;
    }
}
