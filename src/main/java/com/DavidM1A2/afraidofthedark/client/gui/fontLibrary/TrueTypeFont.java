package com.DavidM1A2.afraidofthedark.client.gui.fontLibrary;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * TrueTyper: Open Source TTF implementation for Minecraft. Modified from Slick2D - under BSD Licensing - http://slick.ninjacave.com/license/
 * <p>
 * Copyright (c) 2013 - Slick2D
 * <p>
 * All rights reserved.
 *
 * Heavily modified the implementation for later versions of MC 1.12+
 */
public class TrueTypeFont
{
    // Flag on whether AntiAliasing is enabled or not
    private boolean antiAlias;
    // A reference to Java's AWT Font that we create our font texture from
    private Font font;
    // Array that holds necessary information about the font characters
    private CharacterGlyph[] asciiGlyphs = new CharacterGlyph[256];
    // Map of user defined font characters (Character <-> IntObject)
    private Map<Character, CharacterGlyph> additionalGlyphs = new HashMap<>();
    // Font's size
    private int fontSize;
    // Font's height
    private int fontHeight = 0;
    // Texture used to cache the font 0-255 characters
    private int fontTextureID;
    // Default font texture width
    private int textureWidth = 1024;
    // Default font texture height
    private int textureHeight = 1024;
    // The font metrics for our Java AWT font
    private FontMetrics fontMetrics;
    // Correction constants used to render letters closer together. These cause a big with getWidth() currently
    private static final int CORRECT_L = 0; // 2
    private static final int CORRECT_R = 0; // 1

    /**
     * Constructor initializes the font glyphs
     *
     * @param font The java font to render
     * @param antiAlias True if anti-alias is on, false otherwise
     * @param additionalChars Additional characters to draw
     */
    TrueTypeFont(Font font, boolean antiAlias, char[] additionalChars)
    {
        // Initialize all fields
        this.font = font;
        this.fontSize = font.getSize();
        this.antiAlias = antiAlias;
        // Render the characters into open GL format
        createSet(additionalChars);
    }

    /**
     * Initializes the font by rendering each character to an image
     *
     * @param customCharsArray The extra non-ascii characters to render
     */
    private void createSet(char[] customCharsArray)
    {
        // If there are custom chars then expand the font texture twice
        if (customCharsArray != null && customCharsArray.length > 0)
        {
            textureWidth = textureWidth * 2;
        }

        // In any case this should be done in other way. Texture with size
        // 1024x1024
        // can maintain only 256 characters with resolution of 64x64. The
        // texture
        // size should be calculated dynamically by looking at character sizes.

        try
        {
            // Create a temp buffered image to write to
            BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
            // Grab the graphics object to write to the image
            Graphics2D g = (Graphics2D) imgTemp.getGraphics();

            // Set the color to black
            g.setColor(new Color(0, 0, 0, 1));
            // Fill the rectangle with black
            g.fillRect(0, 0, textureWidth, textureHeight);

            // 3 values to use in writing the glyphs to the image
            // The current row's height
            int rowHeight = 0;
            // The current glyph pos x and y
            int positionX = 0;
            int positionY = 0;

            // The number of custom characters to write
            int customCharsLength = (customCharsArray != null) ? customCharsArray.length : 0;

            // Go over all 256 ascii characters and then additional custom characters
            for (int i = 0; i < 256 + customCharsLength; i++)
            {
                // Grab the current character to render
                char character = (i < 256) ? (char) i : customCharsArray[i - 256];

                // Render the character into an image
                BufferedImage fontImage = getFontImage(character);

                // Create a new character glyph for this font
                CharacterGlyph characterGlyph = new CharacterGlyph();

                // Assign the width and height fields
                characterGlyph.width = fontImage.getWidth();
                characterGlyph.height = fontImage.getHeight();

                // If the glyph is too big for the texture move down a line
                if (positionX + characterGlyph.width >= textureWidth)
                {
                    // Reset X to the far left
                    positionX = 0;
                    // Move Y down a row
                    positionY = positionY + rowHeight;
                    // Reset row height to be 0, the current row has no glyphs
                    rowHeight = 0;
                }

                // Assign the glyph position on the texture
                characterGlyph.storedX = positionX;
                characterGlyph.storedY = positionY;

                // The font height is the max of the current height and the new glyph's height
                fontHeight = Math.max(fontHeight, characterGlyph.height);

                // The row height is the ma of the current row height and the new glyph's height
                rowHeight = Math.max(rowHeight, characterGlyph.height);

                // Draw the character glyph to the large texture
                g.drawImage(fontImage, positionX, positionY, null);

                // Move the X position over by the glyph's width
                positionX = positionX + characterGlyph.width;

                // If we are working with a standard ascii glyph assign it here
                if (i < 256)
                {
                    asciiGlyphs[i] = characterGlyph;
                }
                // Otherwise store a custom character glyph into the hash map
                else
                {
                    additionalGlyphs.put(character, characterGlyph);
                }
            }

            // Once we're done writing all of our glyphs onto the 1024x1024 image we load it into open GL for rendering
            fontTextureID = loadImage(imgTemp);
        }
        // Catch any exceptions and log them
        catch (Exception e)
        {
            AfraidOfTheDark.INSTANCE.getLogger().error("Failed to create font.", e);
        }
    }

    /**
     * Converts a character to an image by writing it to an image
     *
     * @param ch The character to write
     * @return The buffered image representing the character
     */
    private BufferedImage getFontImage(char ch)
    {
        // Create a temporary image to extract the character's size
        BufferedImage tempFontImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        // Grab the graphics component
        Graphics2D g = (Graphics2D) tempFontImage.getGraphics();
        // If anti-aliasing is enabled do so for the graphics component
        if (antiAlias)
        {
            // Enable anti-aliasing
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        // Set the graphic's font
        g.setFont(font);
        // Grab the font metrics and store that off to be used later
        fontMetrics = g.getFontMetrics();
        // Compute the width of the current character (not sure why we add 8?)
        int charWidth = fontMetrics.charWidth(ch); // + 8?
        // If our character has no width just use a width of 1 and don't do anything with it
        if (charWidth <= 0)
        {
            charWidth = 1;
        }

        // Compute the height of the character
        int charHeight = fontMetrics.getHeight();
        // If the char's height is invalid just use the font size
        if (charHeight <= 0)
        {
            charHeight = fontSize;
        }

        // Create another image holding the character we are creating
        BufferedImage fontImage = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        // Update g to be the final image
        g = (Graphics2D) fontImage.getGraphics();
        // Set the anti-alias flag if needed
        if (antiAlias)
        {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        // Set the font
        g.setFont(font);
        // Set the color to white
        g.setColor(Color.WHITE);
        // Write the character onto the image
        g.drawString(String.valueOf(ch), 0, fontMetrics.getAscent());

        // Return the image
        return fontImage;
    }

    /**
     * Converts a buffered image into an open GL ready image to be loaded
     *
     * @param bufferedImage The buffered image to load into OpenGL
     * @return The ID of the texture used to reference this image
     */
    private static int loadImage(BufferedImage bufferedImage)
    {
        try
        {
            // Grab the width and height of the texture
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            // Get the number of bytes per pixel
            int bytesPerPixel = bufferedImage.getColorModel().getPixelSize();
            ByteBuffer byteBuffer;
            // Grab the data buffer used by the buffered image
            DataBuffer db = bufferedImage.getData().getDataBuffer();
            // If it's an int buffer write each int to the data buffer 4 bytes at a time
            if (db instanceof DataBufferInt)
            {
                byteBuffer = ByteBuffer.allocateDirect(width * height * (bytesPerPixel / 8)).order(ByteOrder.nativeOrder());
                Arrays.stream(((DataBufferInt) (bufferedImage.getData().getDataBuffer())).getData()).forEach(byteBuffer::putInt);
            }
            // If it's a byte buffer write it directly into the buffer
            else
            {
                byteBuffer = ByteBuffer.allocateDirect(width * height * (bytesPerPixel / 8)).order(ByteOrder.nativeOrder());
                byteBuffer.put(((DataBufferByte) (bufferedImage.getData().getDataBuffer())).getData());
            }
            // We need to flip the bytes so they get drawn correctly
            byteBuffer.flip();

            // Not very familiar with OpenGl here, but create an int buffer and generate the texture from the byte buffer

            IntBuffer textureId = GLAllocation.createDirectIntBuffer(1);
            GL11.glGenTextures(textureId);

            GlStateManager.bindTexture(textureId.get(0));

            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

            GlStateManager.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);

            GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGBA8, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);

            // Return the texture ID
            return textureId.get(0);
        }
        // Catch any exceptions, log an error, and then shutdown java since we can't continue without fonts
        catch (Exception e)
        {
            AfraidOfTheDark.INSTANCE.getLogger().error("Could not allocate an OpenGL texture!", e);
            FMLCommonHandler.instance().exitJava(-1, false);
        }

        return -1;
    }

    /**
     * Draws a string at a given x,y position with a scale, alignment, and color
     *
     * @param x The x position to draw at
     * @param y The y position to draw at
     * @param stringToDraw The string to draw
     * @param scaleX The x scale to draw with
     * @param scaleY The y scale to draw with
     * @param textAlignment The alignment to draw the text with
     * @param rgba The color to use when drawing the string
     */
    public void drawString(float x, float y, String stringToDraw, float scaleX, float scaleY, TextAlignment textAlignment, org.lwjgl.util.Color rgba)
    {
        drawString(x, y, stringToDraw, 0, stringToDraw.length() - 1, scaleX, scaleY, textAlignment, rgba);
    }

    /**
     * Draws a string at a given x,y position with a scale, alignment, and color
     *
     * @param x The x position to draw at
     * @param y The y position to draw at
     * @param stringToDraw The string to draw
     * @param startIndex The start index in the string to draw at
     * @param endIndex The end index in the string to draw at
     * @param scaleX The x scale to draw with
     * @param scaleY The y scale to draw with
     * @param textAlignment The alignment to draw the text with
     * @param rgba The color to use when drawing the string
     */
    public void drawString(float x, float y, String stringToDraw, int startIndex, int endIndex, float scaleX, float scaleY, TextAlignment textAlignment, org.lwjgl.util.Color rgba)
    {
        // The current glyph being drawn
        CharacterGlyph characterGlyph;
        // The current character to draw
        char currentChar;
        // The total width of the current line being drawn
        int totalWidth = 0;
        // The current index being drawn
        int currentIndex = startIndex;
        // The alignment flag which is more useful than an enum, -1 = right, 0 = center, 1 = left
        int alignmentFlag;
        // The spacing correction value, if we are left aligned or center then this will be CORRECT_L, otherwise CORRECT_R
        int spacingCorrection;
        // The z position to begin drawing at
        int startY = 0;

        // Based on the text alignment start drawing at different positions
        switch (textAlignment)
        {
            // Align the text right
            case ALIGN_RIGHT:
            {
                // Align right is set, so use flag = -1
                alignmentFlag = -1;
                // Set spacing correction to right since we are aligned right
                spacingCorrection = CORRECT_R;

                // Iterate while our current index is smaller than the end index so there are still characters to draw
                while (currentIndex < endIndex)
                {
                    // For each new line move the start y coordinate down by the font's height
                    if (stringToDraw.charAt(currentIndex) == '\n')
                    {
                        startY = startY - fontHeight;
                    }
                    currentIndex++;
                }
                break;
            }
            // Align the text centered
            case ALIGN_CENTER:
            {
                // Go over each character in the string
                for (int i = startIndex; i <= endIndex; i++)
                {
                    // Grab the character
                    currentChar = stringToDraw.charAt(i);
                    // If the character is a new line we're done, break and align on this alone
                    if (currentChar == '\n')
                    {
                        break;
                    }

                    // Grab the current glyph, if it's an ascii character grab it from the array, otherwise get it from
                    // our custom character hash map
                    characterGlyph = currentChar < 256 ? asciiGlyphs[currentChar] : additionalGlyphs.get(currentChar);

                    // Increase our total width by the glyph's width
                    totalWidth = totalWidth + (characterGlyph.width - CORRECT_L);
                }
                // Divide the total width by 2 to get the center the current line
                totalWidth = totalWidth / -2;
            }
            // Align the text left, also used for align center
            case ALIGN_LEFT:
            default:
            {
                // Set the alignment flag to be 1 to render l->r
                alignmentFlag = 1;
                // Use the left spacing correction
                spacingCorrection = CORRECT_L;
                break;
            }
        }

        // Bind our custom texture sheet
        GlStateManager.bindTexture(fontTextureID);
        // Grab the MC tesselator to render with
        Tessellator tessellator = Tessellator.getInstance();
        // Create a new buffer builder to draw to
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        // Begin drawing a texture
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        // Set the color to be the passed in color
        GlStateManager.color(rgba.getRed() / 255f, rgba.getGreen() / 255f, rgba.getBlue() / 255f, rgba.getAlpha() / 255f);
        // Loop while the current index is between the start and end index
        while (currentIndex >= startIndex && currentIndex <= endIndex)
        {
            // Grab the current character to draw
            currentChar = stringToDraw.charAt(currentIndex);
            // Grab the glyph to draw, it will either be ascii or in the additional glyphs map
            characterGlyph = currentChar < 256 ? asciiGlyphs[currentChar] : additionalGlyphs.get(currentChar);

            // If the glyph is valid draw it
            if (characterGlyph != null)
            {
                // If the alignment flag is -1 align right is set so we reduce the total width
                if (alignmentFlag < 0)
                {
                    totalWidth = totalWidth + (characterGlyph.width - spacingCorrection) * alignmentFlag;
                }
                // If the current character is a new line change lines
                if (currentChar == '\n')
                {
                    // Move in the right direction (up if align right, down otherwise)
                    startY = startY - (fontHeight * alignmentFlag);
                    // Reset the width since we're on a new line
                    totalWidth = 0;
                    // If the alignment is center we need to compute the width of the next line
                    if (textAlignment == TextAlignment.ALIGN_CENTER)
                    {
                        // Iterate over the next line, compute its width
                        for (int i = currentIndex + 1; i <= endIndex; i++)
                        {
                            // Grab the current character
                            currentChar = stringToDraw.charAt(i);
                            // If we've hit a new new line then the line is over
                            if (currentChar == '\n')
                            {
                                break;
                            }

                            // Set the current glyph to be either an ascii glyph or a custom glyph if it's bigger than 255
                            characterGlyph = currentChar < 256 ? asciiGlyphs[currentChar] : additionalGlyphs.get(currentChar);
                            // Increase the width by the glyph width, subtract off the spacing modifier
                            totalWidth = totalWidth + (characterGlyph.width - CORRECT_L);
                        }
                        // Center the text, so divide by 2
                        totalWidth = totalWidth / -2;
                    }
                }
                // If the current char is not a new line then we can continue drawing our character
                else
                {
                    // Draw a letter
                    drawQuad(totalWidth * scaleX + x, startY * scaleY + y, (totalWidth + characterGlyph.width) * scaleX + x, (startY + characterGlyph.height) * scaleY + y, characterGlyph.storedX + characterGlyph.width, characterGlyph.storedY + characterGlyph.height, characterGlyph.storedX, characterGlyph.storedY);
                    // If we are aligning left then increase the width of the current line
                    if (alignmentFlag > 0)
                    {
                        totalWidth = totalWidth + (characterGlyph.width - spacingCorrection) * alignmentFlag;
                    }
                }
                // Advance or reduce the current index depending on if we're aligning right or left
                currentIndex = currentIndex + alignmentFlag;
            }
        }
        // Finally draw the screen now that all the glyphs are in place
        tessellator.draw();
    }

    /**
     * Draws a glyph using a quad on the screen
     *
     * @param drawX Tbe x position to draw at
     * @param drawY The y position to draw at
     * @param drawX2 The far x position to draw to
     * @param drawY2 The far y position to draw to
     * @param srcX The source x position to draw from
     * @param srcY The source y position to draw from
     * @param srcX2 The source x position to draw to
     * @param srcY2 The source y position to draw to
     */
    private void drawQuad(float drawX, float drawY, float drawX2, float drawY2, float srcX, float srcY, float srcX2, float srcY2)
    {
        // Compute the width and height of the glyph to draw
        float drawWidth = Math.abs(drawX2 - drawX);
        float drawHeight = Math.abs(drawY2 - drawY);
        // Compute the width and height of the glyph on the source
        float srcWidth = srcX2 - srcX;
        float srcHeight = srcY2 - srcY;
        // Grab the tessellator instance and create a buffer builder
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

        // Add the 4 vertices that are used to draw the glyph. These must be done in this order
        bufferBuilder.pos(drawX, drawY + drawHeight, 0.0D)
                .tex((srcX + srcWidth) / textureWidth, srcY / textureHeight)
                .endVertex();
        bufferBuilder.pos(drawX + drawWidth, drawY + drawHeight, 0.0D)
                .tex(srcX / textureWidth, srcY / textureHeight)
                .endVertex();
        bufferBuilder.pos(drawX + drawWidth, drawY, 0.0D)
                .tex(srcX / textureWidth, (srcY + srcHeight) / textureHeight)
                .endVertex();
        bufferBuilder.pos(drawX, drawY, 0.0D)
                .tex((srcX + srcWidth) / textureWidth, (srcY + srcHeight) / textureHeight)
                .endVertex();
    }

    /**
     * Gets the width of a given string if it was printed as pixels
     *
     * @param string The string to get the width of
     * @return The width of the string if printed in pixels
     */
    public float getWidth(String string)
    {
        return this.fontMetrics.stringWidth(string);
    }

    /**
     * @return Gets the height of the font
     */
    public int getHeight()
    {
        return fontHeight;
    }

    /**
     * Destroys the font and releases the resources
     */
    public void destroy()
    {
        // Create the buffer with the texture id, bind the texture, and delete the texture
        IntBuffer scratch = BufferUtils.createIntBuffer(1);
        scratch.put(0, fontTextureID);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDeleteTextures(scratch);
    }

    /**
     * @return Gets the font size
     */
    public int getFontSize()
    {
        return this.fontSize;
    }

    /**
     * Utility class for storing glyph positions
     */
    private class CharacterGlyph
    {
        // Character's width
        private int width;
        // Character's height
        private int height;
        // Character's stored x position
        private int storedX;
        // Character's stored y position
        private float storedY;
    }
}