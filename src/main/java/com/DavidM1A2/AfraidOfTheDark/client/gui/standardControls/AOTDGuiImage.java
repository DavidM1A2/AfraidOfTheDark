package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Class representing an image to be rendered on the GUI
 */
public class AOTDGuiImage extends AOTDGuiContainer
{
    // The resource location that contains the texture to draw
    private ResourceLocation imageTexture;
    // The U and V representing the starting top left position inside the image to begin drawing from
    private int u = 0;
    private int v = 0;
    // The image texture's width and height
    private int textureWidth;
    private int textureHeight;

    /**
     * Constructor initializes the bounding box and the image texture
     *
     * @param x             The X location of the top left corner
     * @param y             The Y location of the top left corner
     * @param width         The width of the component
     * @param height        The height of the component
     * @param textureWidth  The width of the image png texture
     * @param textureHeight The height of the image png texture
     * @param imageTexture  The texture of the image
     */
    public AOTDGuiImage(Integer x, Integer y, Integer width, Integer height, Integer textureWidth, Integer textureHeight, String imageTexture)
    {
        super(x, y, width, height);
        this.setImageTexture(imageTexture, textureWidth, textureHeight);
    }

    /**
     * Constructor initializes the bounding box and the image texture
     *
     * @param x            The X location of the top left corner
     * @param y            The Y location of the top left corner
     * @param width        The width of the component
     * @param height       The height of the component
     * @param imageTexture The texture of the image
     */
    public AOTDGuiImage(int x, int y, int width, int height, String imageTexture)
    {
        super(x, y, width, height);
        this.setImageTexture(imageTexture);
    }

    /**
     * Draws the GUI image given the width and height
     */
    @Override
    public void draw()
    {
        if (this.isVisible())
        {
            GlStateManager.pushMatrix();
            // Enable alpha blending
            GlStateManager.enableBlend();
            // Set the color
            GlStateManager.color(this.getColor().getRed() / 255f, this.getColor().getGreen() / 255f, this.getColor().getBlue() / 255f, this.getColor().getAlpha() / 255f);
            // Bind the texture to render
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.imageTexture);
            // If the texture width and height are both -1, then we assume the image's size is this control's size
            if (textureHeight == -1 || textureWidth == -1)
            {
                Gui.drawModalRectWithCustomSizedTexture(this.getXScaled(), this.getYScaled(), this.u, this.v, this.getWidthScaled(), this.getHeightScaled(), this.getWidthScaled(), this.getHeightScaled());
            }
            else
            {
                Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), this.u, this.v, this.getWidth(), this.getHeight(), this.getWidthScaled(), this.getHeightScaled(), this.textureWidth, this.textureHeight);
            }
            GlStateManager.popMatrix();

            // Draw the any children
            super.draw();
        }
    }

    /**
     * Sets the image texture to a given image without any width or height constraints
     *
     * @param imageTexture The texture to draw
     */
    public void setImageTexture(String imageTexture)
    {
        this.setImageTexture(imageTexture, -1, -1);
    }

    /**
     * Sets the image texture to a given image without any width or height constraints
     *
     * @param imageTexture The texture to draw
     */
    public void setImageTexture(ResourceLocation imageTexture)
    {
        this.setImageTexture(imageTexture, -1, -1);
    }

    /**
     * Sets the image texture to a given image with a width and height constraint
     *
     * @param imageTexture  The texture to draw
     * @param textureWidth  The width of the image
     * @param textureHeight The height of the image
     */
    public void setImageTexture(String imageTexture, Integer textureWidth, Integer textureHeight)
    {
        this.setImageTexture(new ResourceLocation(imageTexture), textureWidth, textureHeight);
    }

    /**
     * Sets the image texture to a given image with a width and height constraint
     *
     * @param imageTexture  The texture to draw
     * @param textureWidth  The width of the image
     * @param textureHeight The height of the image
     */
    public void setImageTexture(ResourceLocation imageTexture, Integer textureWidth, Integer textureHeight)
    {
        this.imageTexture = imageTexture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    /**
     * @return Gets the x value to start drawing from inside the texture
     */
    public int getU()
    {
        return this.u;
    }

    /**
     * @param u Sets the x value to start drawing from inside the texture
     */
    public void setU(int u)
    {
        this.u = u;
    }

    /**
     * @return Gets the y value to start drawing from inside the texture
     */
    public int getV()
    {
        return this.v;
    }

    /**
     * @param v Sets the y value to start drawing from inside the texture
     */
    public void setV(int v)
    {
        this.v = v;
    }

    /**
     * @return Getter for maximum texture width. If we don't have a texture width use this control's width as the image's width
     */
    public int getMaxTextureWidth()
    {
        if (textureWidth == -1)
        {
            return this.getWidth();
        }
        else
        {
            return this.textureWidth;
        }
    }

    /**
     * @return Getter for maximum texture height. If we don't have a texture height use this control's height as the image's height
     */
    public int getMaxTextureHeight()
    {
        if (textureHeight == -1)
        {
            return this.getHeight();
        }
        else
        {
            return this.textureHeight;
        }
    }
}
