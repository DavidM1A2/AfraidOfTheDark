package com.DavidM1A2.afraidofthedark.client.gui.fontLibrary;

import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import net.minecraft.client.renderer.BufferBuilder;
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
import java.util.HashMap;
import java.util.Map;

/**
 * TrueTyper: Open Source TTF implementation for Minecraft. Modified from Slick2D - under BSD Licensing - http://slick.ninjacave.com/license/
 * <p>
 * Copyright (c) 2013 - Slick2D
 * <p>
 * All rights reserved.
 */

public class TrueTypeFont
{
    // Flag on whether AntiAliasing is enabled or not
    private boolean antiAlias;
    // A reference to Java's AWT Font that we create our font texture from
    private Font font;
    // Array that holds necessary information about the font characters
    private FloatObject[] charArray = new FloatObject[256];
    // Map of user defined font characters (Character <-> IntObject)
    private Map<Character, FloatObject> customChars = new HashMap<>();
    // Font's size
    private float fontSize;
    // Font's height
    private float fontHeight = 0;
    // Texture used to cache the font 0-255 characters
    private int fontTextureID;
    // Default font texture width
    private int textureWidth = 1024;
    // Default font texture height
    private int textureHeight = 1024;
    // The font metrics for our Java AWT font
    private FontMetrics fontMetrics;
    // Correction constants used to squeeze letters together
    private static final int CORRECT_L = 9;
    private static final int CORRECT_R = 8;

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
        // Add 3 to font size so we have some padding
        this.fontSize = font.getSize() + 3;
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
            float rowHeight = 0;
            float positionX = 0;
            float positionY = 0;

            // The number of custom characters to write
            int customCharsLength = (customCharsArray != null) ? customCharsArray.length : 0;

            // Go over all 256 ascii characters and then additional custom characters
            for (int i = 0; i < 256 + customCharsLength; i++)
            {
                // Grab the current character to render
                char character = (i < 256) ? (char) i : customCharsArray[i - 256];

                // Render the character into an image
                BufferedImage fontImage = getFontImage(character);

                FloatObject newIntObject = new FloatObject();

                newIntObject.width = fontImage.getWidth();
                newIntObject.height = fontImage.getHeight();

                if (positionX + newIntObject.width >= textureWidth)
                {
                    positionX = 0;
                    positionY += rowHeight;
                    rowHeight = 0;
                }

                newIntObject.storedX = positionX;
                newIntObject.storedY = positionY;

                if (newIntObject.height > fontHeight)
                {
                    fontHeight = newIntObject.height;
                }

                if (newIntObject.height > rowHeight)
                {
                    rowHeight = newIntObject.height;
                }

                // Draw it here
                g.drawImage(fontImage, (int) positionX, (int) positionY, null);

                positionX += newIntObject.width;

                if (i < 256)
                { // standard characters
                    charArray[i] = newIntObject;
                }
                else
                { // custom characters
                    customChars.put(character, newIntObject);
                }

                fontImage = null;
            }

            fontTextureID = loadImage(imgTemp);

            // .getTexture(font.toString(), imgTemp);

        } catch (Exception e)
        {
            System.err.println("Failed to create font.");
            e.printStackTrace();
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
        // If antialiasing is enabled do so for the graphics component
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
        float charWidth = fontMetrics.charWidth(ch) + 8;

        if (charWidth <= 0)
        {
            charWidth = 7;
        }
        float charheight = fontMetrics.getHeight() + 3;
        if (charheight <= 0)
        {
            charheight = fontSize;
        }

        // Create another image holding the character we are creating
        BufferedImage fontImage = new BufferedImage((int) charWidth, (int) charheight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gt = (Graphics2D) fontImage.getGraphics();
        if (antiAlias)
        {
            gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        gt.setFont(font);

        gt.setColor(Color.WHITE);
        int charx = 3;
        int chary = 1;
        gt.drawString(String.valueOf(ch), (charx), (chary) + fontMetrics.getAscent());

        return fontImage;

    }

    private static int loadImage(BufferedImage bufferedImage)
    {
        try
        {
            short width = (short) bufferedImage.getWidth();
            short height = (short) bufferedImage.getHeight();
            // textureLoader.bpp = bufferedImage.getColorModel().hasAlpha() ?
            // (byte)32 : (byte)24;
            int bpp = (byte) bufferedImage.getColorModel().getPixelSize();
            ByteBuffer byteBuffer;
            DataBuffer db = bufferedImage.getData().getDataBuffer();
            if (db instanceof DataBufferInt)
            {
                int[] intI = ((DataBufferInt) (bufferedImage.getData().getDataBuffer())).getData();
                byte[] newI = new byte[intI.length * 4];
                for (int i = 0; i < intI.length; i++)
                {
                    byte[] b = intToByteArray(intI[i]);
                    int newIndex = i * 4;

                    newI[newIndex] = b[1];
                    newI[newIndex + 1] = b[2];
                    newI[newIndex + 2] = b[3];
                    newI[newIndex + 3] = b[0];
                }

                byteBuffer = ByteBuffer.allocateDirect(width * height * (bpp / 8)).order(ByteOrder.nativeOrder()).put(newI);
            }
            else
            {
                byteBuffer = ByteBuffer.allocateDirect(width * height * (bpp / 8)).order(ByteOrder.nativeOrder()).put(((DataBufferByte) (bufferedImage.getData().getDataBuffer())).getData());
            }
            byteBuffer.flip();

            int internalFormat = GL11.GL_RGBA8, format = GL11.GL_RGBA;
            IntBuffer textureId = BufferUtils.createIntBuffer(1);
            GL11.glGenTextures(textureId);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId.get(0));

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);

            GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, internalFormat, width, height, format, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            return textureId.get(0);

        } catch (Exception e)
        {
            e.printStackTrace();
            FMLCommonHandler.instance().exitJava(-1, false);
        }

        return -1;
    }

    private static byte[] intToByteArray(int value)
    {
        return new byte[]
                {(byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
    }

    private void drawQuad(float drawX, float drawY, float drawX2, float drawY2, float srcX, float srcY, float srcX2, float srcY2)
    {
        float DrawWidth = Math.abs(drawX2 - drawX);
        float DrawHeight = Math.abs(drawY2 - drawY);
        float SrcWidth = srcX2 - srcX;
        float SrcHeight = srcY2 - srcY;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

        bufferBuilder.pos(drawX, drawY + DrawHeight, 0.0D).tex((srcX + SrcWidth) / textureWidth, srcY / textureHeight).endVertex();
        // GL11.glTexCoord2f(TextureSrcX, TextureSrcY + RenderHeight);
        // GL11.glVertex2f(drawX, drawY + DrawHeight);

        bufferBuilder.pos(drawX + DrawWidth, drawY + DrawHeight, 0.0D).tex(srcX / textureWidth, srcY / textureHeight).endVertex();
        // GL11.glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY +
        // RenderHeight);
        // GL11.glVertex2f(drawX + DrawWidth, drawY + DrawHeight);

        bufferBuilder.pos(drawX + DrawWidth, drawY, 0.0D).tex(srcX / textureWidth, (srcY + SrcHeight) / textureHeight).endVertex();
        // GL11.glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY);
        // GL11.glVertex2f(drawX + DrawWidth, drawY);

        bufferBuilder.pos(drawX, drawY, 0.0D).tex((srcX + SrcWidth) / textureWidth, (srcY + SrcHeight) / textureHeight).endVertex();
        // GL11.glTexCoord2f(TextureSrcX, TextureSrcY);
        // GL11.glVertex2f(drawX, drawY);
    }

    public float getWidth(String whatchars)
    {
        float totalwidth = 0;
        FloatObject floatObject = null;
        int currentChar = 0;
        for (int i = 0; i < whatchars.length(); i++)
        {
            currentChar = whatchars.charAt(i);
            if (currentChar < 256)
            {
                floatObject = charArray[currentChar];
            }
            else
            {
                floatObject = (FloatObject) customChars.get((char) currentChar);
            }

            if (floatObject != null)
            {
                totalwidth += floatObject.width / 2;
            }
        }
        // System.out.println("Size: "+totalwidth);
        return this.fontMetrics.stringWidth(whatchars);
        // return (totalwidth);
    }

    public float getHeight()
    {
        return fontHeight;
    }

    public float getHeight(String HeightString)
    {
        return fontHeight;
    }

    public float getLineHeight()
    {
        return fontHeight;
    }

    public void drawString(float x, float y, String whatchars, float scaleX, float scaleY, float... rgba)
    {
        if (rgba.length == 0)
        {
            rgba = new float[]
                    {1f, 1f, 1f, 1f};
        }
        drawString(x, y, whatchars, 0, whatchars.length() - 1, scaleX, scaleY, TextAlignment.ALIGN_LEFT, rgba);
    }

    public void drawString(float x, float y, String whatchars, float scaleX, float scaleY, TextAlignment format, float... rgba)
    {
        if (rgba.length == 0)
        {
            rgba = new float[]
                    {1f, 1f, 1f, 1f};
        }

        drawString(x, y, whatchars, 0, whatchars.length() - 1, scaleX, scaleY, format, rgba);
    }

    public void drawString(float x, float y, String whatchars, int startIndex, int endIndex, float scaleX, float scaleY, TextAlignment format, float... rgba)
    {
        if (rgba.length == 0)
        {
            rgba = new float[]
                    {1f, 1f, 1f, 1f};
        }

        FloatObject floatObject = null;
        int charCurrent;

        float totalwidth = 0;
        int i = startIndex, d, c;
        float startY = 0;

        switch (format)
        {
            case ALIGN_RIGHT:
            {
                d = -1;
                c = CORRECT_R;

                while (i < endIndex)
                {
                    if (whatchars.charAt(i) == '\n')
                    {
                        startY -= fontHeight;
                    }
                    i++;
                }
                break;
            }
            case ALIGN_CENTER:
            {
                for (int l = startIndex; l <= endIndex; l++)
                {
                    charCurrent = whatchars.charAt(l);
                    if (charCurrent == '\n')
                    {
                        break;
                    }
                    if (charCurrent < 256)
                    {
                        floatObject = charArray[charCurrent];
                    }
                    else
                    {
                        floatObject = customChars.get((char) charCurrent);
                    }
                    totalwidth += floatObject.width - CORRECT_L;
                }
                totalwidth /= -2;
            }
            case ALIGN_LEFT:
            default:
            {
                d = 1;
                c = CORRECT_L;
                break;
            }

        }
        GlStateManager.bindTexture(fontTextureID);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        // GL11.glBegin(GL11.GL_QUADS);
        if (rgba.length == 4)
        {
            GlStateManager.color(rgba[0], rgba[1], rgba[2], rgba[3]);
        }
        while (i >= startIndex && i <= endIndex)
        {

            charCurrent = whatchars.charAt(i);
            if (charCurrent < 256)
            {
                floatObject = charArray[charCurrent];
            }
            else
            {
                floatObject = customChars.get((char) charCurrent);
            }

            if (floatObject != null)
            {
                if (d < 0)
                {
                    totalwidth += (floatObject.width - c) * d;
                }
                if (charCurrent == '\n')
                {
                    startY -= fontHeight * d;
                    totalwidth = 0;
                    if (format == TextAlignment.ALIGN_CENTER)
                    {
                        for (int l = i + 1; l <= endIndex; l++)
                        {
                            charCurrent = whatchars.charAt(l);
                            if (charCurrent == '\n')
                            {
                                break;
                            }
                            if (charCurrent < 256)
                            {
                                floatObject = charArray[charCurrent];
                            }
                            else
                            {
                                floatObject = customChars.get((char) charCurrent);
                            }
                            totalwidth += floatObject.width - CORRECT_L;
                        }
                        totalwidth /= -2;
                    }
                    // if center get next lines total width/2;
                }
                else
                {
                    drawQuad((totalwidth + floatObject.width) * scaleX + x, startY * scaleY + y, totalwidth * scaleX + x, (startY + floatObject.height) * scaleY + y, floatObject.storedX + floatObject.width, floatObject.storedY + floatObject.height, floatObject.storedX, floatObject.storedY);
                    if (d > 0)
                    {
                        totalwidth += (floatObject.width - c) * d;
                    }
                }
                i += d;

            }
        }
        tessellator.draw();
        // GL11.glEnd();
    }

    public void destroy()
    {
        IntBuffer scratch = BufferUtils.createIntBuffer(1);
        scratch.put(0, fontTextureID);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDeleteTextures(scratch);
    }

    // ADDED HERE
    public int getFontSize()
    {
        return (int) this.fontSize;
    }

    private class FloatObject
    {
        /**
         * Character's width
         */
        public float width;

        /**
         * Character's height
         */
        public float height;

        /**
         * Character's stored x position
         */
        public float storedX;

        /**
         * Character's stored y position
         */
        public float storedY;
    }
}