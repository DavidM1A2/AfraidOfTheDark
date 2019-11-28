package com.davidm1a2.afraidofthedark.client.gui.fontLibrary

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraftforge.fml.common.FMLCommonHandler
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.GLU
import java.awt.*
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import java.util.function.IntConsumer
import kotlin.math.abs
import kotlin.math.max

/**
 * TrueTyper: Open Source TTF implementation for Minecraft. Modified from Slick2D - under BSD Licensing - http://slick.ninjacave.com/license/
 *
 *
 * Copyright (c) 2013 - Slick2D
 *
 *
 * All rights reserved.
 *
 * Heavily modified the implementation for later versions of MC 1.12+
 *
 * @constructor initializes the font glyphs
 * @param font The java font to render
 * @param antiAlias True if anti-alias is on, false otherwise
 * @param additionalChars Additional characters to draw
 * @property asciiGlyphs Array that holds necessary information about the font characters
 * @property additionalGlyphs Map of user defined font characters (Character <-> IntObject)
 * @property fontSize Font's size
 * @property height Font's height
 * @property fontTextureID Texture used to cache the font 0-255 characters
 * @property textureWidth Default font texture width
 * @property textureHeight Default font texture height
 * @property fontMetrics The font metrics for our Java AWT font
 */
class TrueTypeFont internal constructor(private val font: Font, private val antiAlias: Boolean, additionalChars: CharArray?)
{
    private val asciiGlyphs = arrayOfNulls<CharacterGlyph>(256)
    private val additionalGlyphs = HashMap<Char, CharacterGlyph>()
    private val fontSize: Int = font.size
    var height = 0
        private set
    private var fontTextureID: Int = 0
    private var textureWidth = 1024
    private val textureHeight = 1024
    private var fontMetrics: FontMetrics? = null

    init
    {
        // Render the characters into open GL format
        createSet(additionalChars)
    }

    /**
     * Initializes the font by rendering each character to an image
     *
     * @param customCharsArray The extra non-ascii characters to render
     */
    private fun createSet(customCharsArray: CharArray?)
    {
        // If there are custom chars then expand the font texture twice
        if (customCharsArray != null && customCharsArray.isNotEmpty())
        {
            textureWidth = textureWidth * 2
        }

        // In any case this should be done in other way. Texture with size
        // 1024x1024
        // can maintain only 256 characters with resolution of 64x64. The
        // texture
        // size should be calculated dynamically by looking at character sizes.

        try
        {
            // Create a temp buffered image to write to
            val imgTemp = BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB)
            // Grab the graphics object to write to the image
            val g = imgTemp.graphics as Graphics2D

            // Set the color to black
            g.color = Color(0, 0, 0, 1)
            // Fill the rectangle with black
            g.fillRect(0, 0, textureWidth, textureHeight)

            // 3 values to use in writing the glyphs to the image
            // The current row's height
            var rowHeight = 0
            // The current glyph pos x and y
            var positionX = 0
            var positionY = 0

            // The number of custom characters to write
            val customCharsLength = customCharsArray?.size ?: 0

            // Go over all 256 ascii characters and then additional custom characters
            for (i in 0 until 256 + customCharsLength)
            {
                // Grab the current character to render
                val character = if (i < 256) i.toChar() else customCharsArray!![i - 256]

                // Render the character into an image
                val fontImage = getFontImage(character)

                // Create a new character glyph for this font
                val characterGlyph = CharacterGlyph()

                // Assign the width and height fields
                characterGlyph.width = fontImage.width
                characterGlyph.height = fontImage.height

                // If the glyph is too big for the texture move down a line
                if (positionX + characterGlyph.width >= textureWidth)
                {
                    // Reset X to the far left
                    positionX = 0
                    // Move Y down a row
                    positionY = positionY + rowHeight
                    // Reset row height to be 0, the current row has no glyphs
                    rowHeight = 0
                }

                // Assign the glyph position on the texture
                characterGlyph.storedX = positionX
                characterGlyph.storedY = positionY.toFloat()

                // The font height is the max of the current height and the new glyph's height
                height = max(height, characterGlyph.height)

                // The row height is the ma of the current row height and the new glyph's height
                rowHeight = max(rowHeight, characterGlyph.height)

                // Draw the character glyph to the large texture
                g.drawImage(fontImage, positionX, positionY, null)

                // Move the X position over by the glyph's width
                positionX = positionX + characterGlyph.width

                // If we are working with a standard ascii glyph assign it here
                if (i < 256)
                {
                    asciiGlyphs[i] = characterGlyph
                }
                // Otherwise store a custom character glyph into the hash map
                else
                {
                    additionalGlyphs[character] = characterGlyph
                }
            }

            // Once we're done writing all of our glyphs onto the 1024x1024 image we load it into open GL for rendering
            fontTextureID = loadImage(imgTemp)
        }
        // Catch any exceptions and log them
        catch (e: Exception)
        {
            AfraidOfTheDark.INSTANCE.logger.error("Failed to create font.", e)
        }
    }

    /**
     * Converts a character to an image by writing it to an image
     *
     * @param ch The character to write
     * @return The buffered image representing the character
     */
    private fun getFontImage(ch: Char): BufferedImage
    {
        // Create a temporary image to extract the character's size
        val tempFontImage = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
        // Grab the graphics component
        var g = tempFontImage.graphics as Graphics2D
        // If anti-aliasing is enabled do so for the graphics component
        if (antiAlias)
        {
            // Enable anti-aliasing
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        }
        // Set the graphic's font
        g.font = font
        // Grab the font metrics and store that off to be used later
        fontMetrics = g.fontMetrics
        // Compute the width of the current character (not sure why we add 8?)
        var charWidth = fontMetrics!!.charWidth(ch) // + 8?
        // If our character has no width just use a width of 1 and don't do anything with it
        if (charWidth <= 0)
        {
            charWidth = 1
        }

        // Compute the height of the character
        var charHeight = fontMetrics!!.height
        // If the char's height is invalid just use the font size
        if (charHeight <= 0)
        {
            charHeight = fontSize
        }

        // Create another image holding the character we are creating
        val fontImage = BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB)
        // Update g to be the final image
        g = fontImage.graphics as Graphics2D
        // Set the anti-alias flag if needed
        if (antiAlias)
        {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        }
        // Set the font
        g.font = font
        // Set the color to white
        g.color = Color.WHITE
        // Write the character onto the image
        g.drawString(ch.toString(), 0, fontMetrics!!.ascent)

        // Return the image
        return fontImage
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
    fun drawString(x: Float, y: Float, stringToDraw: String, scaleX: Float, scaleY: Float, textAlignment: TextAlignment, rgba: org.lwjgl.util.Color)
    {
        drawString(x, y, stringToDraw, 0, stringToDraw.length - 1, scaleX, scaleY, textAlignment, rgba)
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
    fun drawString(
        x: Float,
        y: Float,
        stringToDraw: String,
        startIndex: Int,
        endIndex: Int,
        scaleX: Float,
        scaleY: Float,
        textAlignment: TextAlignment,
        rgba: org.lwjgl.util.Color
    )
    {
        // The current glyph being drawn
        var characterGlyph: CharacterGlyph?
        // The current character to draw
        var currentChar: Char
        // The total width of the current line being drawn
        var totalWidth = 0
        // The current index being drawn
        var currentIndex = startIndex
        // The alignment flag which is more useful than an enum, -1 = right, 0 = center, 1 = left
        val alignmentFlag: Int
        // The spacing correction value, if we are left aligned or center then this will be CORRECT_L, otherwise CORRECT_R
        val spacingCorrection: Int
        // The z position to begin drawing at
        var startY = 0

        // Based on the text alignment start drawing at different positions
        when (textAlignment)
        {
            // Align the text right
            TextAlignment.ALIGN_RIGHT ->
            {
                // Align right is set, so use flag = -1
                alignmentFlag = -1
                // Set spacing correction to right since we are aligned right
                spacingCorrection = CORRECT_R

                // Iterate while our current index is smaller than the end index so there are still characters to draw
                while (currentIndex < endIndex)
                {
                    // For each new line move the start y coordinate down by the font's height
                    if (stringToDraw[currentIndex] == '\n')
                    {
                        startY = startY - height
                    }
                    currentIndex++
                }
            }
            // Align the text centered
            TextAlignment.ALIGN_CENTER ->
            {
                // Go over each character in the string
                for (i in startIndex..endIndex)
                {
                    // Grab the character
                    currentChar = stringToDraw[i]
                    // If the character is a new line we're done, break and align on this alone
                    if (currentChar == '\n')
                    {
                        break
                    }

                    // Grab the current glyph, if it's an ascii character grab it from the array, otherwise get it from
                    // our custom character hash map
                    characterGlyph = if (currentChar.toInt() < 256) asciiGlyphs[currentChar.toInt()] else additionalGlyphs[currentChar]

                    // Increase our total width by the glyph's width
                    totalWidth = totalWidth + (characterGlyph!!.width - CORRECT_L)
                }
                // Divide the total width by 2 to get the center the current line
                totalWidth = totalWidth / -2

                // Set the alignment flag to be 1 to render l->r
                alignmentFlag = 1
                // Use the left spacing correction
                spacingCorrection = CORRECT_L
            }
            // Align the text left, also used for align center
            TextAlignment.ALIGN_LEFT ->
            {
                alignmentFlag = 1
                spacingCorrection = CORRECT_L
            }
        }

        // Bind our custom texture sheet
        GlStateManager.bindTexture(fontTextureID)
        // Grab the MC tesselator to render with
        val tessellator = Tessellator.getInstance()
        // Create a new buffer builder to draw to
        val bufferBuilder = tessellator.buffer
        // Begin drawing a texture
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
        // Set the color to be the passed in color
        GlStateManager.color(rgba.red / 255f, rgba.green / 255f, rgba.blue / 255f, rgba.alpha / 255f)
        // Loop while the current index is between the start and end index
        while (currentIndex in startIndex..endIndex)
        {
            // Grab the current character to draw
            currentChar = stringToDraw[currentIndex]
            // Grab the glyph to draw, it will either be ascii or in the additional glyphs map
            characterGlyph = if (currentChar.toInt() < 256) asciiGlyphs[currentChar.toInt()] else additionalGlyphs[currentChar]

            // If the glyph is valid draw it
            if (characterGlyph != null)
            {
                // If the alignment flag is -1 align right is set so we reduce the total width
                if (alignmentFlag < 0)
                {
                    totalWidth = totalWidth + (characterGlyph.width - spacingCorrection) * alignmentFlag
                }
                // If the current character is a new line change lines
                if (currentChar == '\n')
                {
                    // Move in the right direction (up if align right, down otherwise)
                    startY = startY - height * alignmentFlag
                    // Reset the width since we're on a new line
                    totalWidth = 0
                    // If the alignment is center we need to compute the width of the next line
                    if (textAlignment == TextAlignment.ALIGN_CENTER)
                    {
                        // Iterate over the next line, compute its width
                        for (i in currentIndex + 1..endIndex)
                        {
                            // Grab the current character
                            currentChar = stringToDraw[i]
                            // If we've hit a new new line then the line is over
                            if (currentChar == '\n')
                            {
                                break
                            }

                            // Set the current glyph to be either an ascii glyph or a custom glyph if it's bigger than 255
                            characterGlyph = if (currentChar.toInt() < 256) asciiGlyphs[currentChar.toInt()] else additionalGlyphs[currentChar]
                            // Increase the width by the glyph width, subtract off the spacing modifier
                            totalWidth = totalWidth + (characterGlyph!!.width - CORRECT_L)
                        }
                        // Center the text, so divide by 2
                        totalWidth = totalWidth / -2
                    }
                }
                // If the current char is not a new line then we can continue drawing our character
                else
                {
                    // Draw a letter
                    drawQuad(
                        totalWidth * scaleX + x,
                        startY * scaleY + y,
                        (totalWidth + characterGlyph.width) * scaleX + x,
                        (startY + characterGlyph.height) * scaleY + y,
                        (characterGlyph.storedX + characterGlyph.width).toFloat(),
                        characterGlyph.storedY + characterGlyph.height,
                        characterGlyph.storedX.toFloat(),
                        characterGlyph.storedY
                    )
                    // If we are aligning left then increase the width of the current line
                    if (alignmentFlag > 0)
                    {
                        totalWidth = totalWidth + (characterGlyph.width - spacingCorrection) * alignmentFlag
                    }
                }
                // Advance or reduce the current index depending on if we're aligning right or left
                currentIndex = currentIndex + alignmentFlag
            }
        }
        // Finally draw the screen now that all the glyphs are in place
        tessellator.draw()
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
    private fun drawQuad(drawX: Float, drawY: Float, drawX2: Float, drawY2: Float, srcX: Float, srcY: Float, srcX2: Float, srcY2: Float)
    {
        // Compute the width and height of the glyph to draw
        val drawWidth = abs(drawX2 - drawX)
        val drawHeight = abs(drawY2 - drawY)
        // Compute the width and height of the glyph on the source
        val srcWidth = srcX2 - srcX
        val srcHeight = srcY2 - srcY
        // Grab the tessellator instance and create a buffer builder
        val bufferBuilder = Tessellator.getInstance().buffer

        // Add the 4 vertices that are used to draw the glyph. These must be done in this order
        bufferBuilder.pos(drawX.toDouble(), (drawY + drawHeight).toDouble(), 0.0)
            .tex(((srcX + srcWidth) / textureWidth).toDouble(), (srcY / textureHeight).toDouble())
            .endVertex()
        bufferBuilder.pos((drawX + drawWidth).toDouble(), (drawY + drawHeight).toDouble(), 0.0)
            .tex((srcX / textureWidth).toDouble(), (srcY / textureHeight).toDouble())
            .endVertex()
        bufferBuilder.pos((drawX + drawWidth).toDouble(), drawY.toDouble(), 0.0)
            .tex((srcX / textureWidth).toDouble(), ((srcY + srcHeight) / textureHeight).toDouble())
            .endVertex()
        bufferBuilder.pos(drawX.toDouble(), drawY.toDouble(), 0.0)
            .tex(((srcX + srcWidth) / textureWidth).toDouble(), ((srcY + srcHeight) / textureHeight).toDouble())
            .endVertex()
    }

    /**
     * Gets the width of a given string if it was printed as pixels
     *
     * @param string The string to get the width of
     * @return The width of the string if printed in pixels
     */
    fun getWidth(string: String): Float
    {
        return this.fontMetrics?.stringWidth(string)?.toFloat() ?: 0f
    }

    /**
     * Destroys the font and releases the resources
     */
    fun destroy()
    {
        // Create the buffer with the texture id, bind the texture, and delete the texture
        val scratch = BufferUtils.createIntBuffer(1)
        scratch.put(0, fontTextureID)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
        GL11.glDeleteTextures(scratch)
    }

    /**
     * Utility class for storing glyph positions
     */
    private class CharacterGlyph
    {
        // Character's width
        var width: Int = 0
        // Character's height
        var height: Int = 0
        // Character's stored x position
        var storedX: Int = 0
        // Character's stored y position
        var storedY: Float = 0.toFloat()
    }

    companion object
    {
        // Correction constants used to render letters closer together. These cause a big with getWidth() currently
        private const val CORRECT_L = 0 // 2
        private const val CORRECT_R = 0 // 1

        /**
         * Converts a buffered image into an open GL ready image to be loaded
         *
         * @param bufferedImage The buffered image to load into OpenGL
         * @return The ID of the texture used to reference this image
         */
        private fun loadImage(bufferedImage: BufferedImage): Int
        {
            try
            {
                // Grab the width and height of the texture
                val width = bufferedImage.width
                val height = bufferedImage.height
                // Get the number of bytes per pixel
                val bytesPerPixel = bufferedImage.colorModel.pixelSize
                val byteBuffer: ByteBuffer
                // Grab the data buffer used by the buffered image
                val db = bufferedImage.data.dataBuffer
                // If it's an int buffer write each int to the data buffer 4 bytes at a time
                if (db is DataBufferInt)
                {
                    byteBuffer = ByteBuffer.allocateDirect(width * height * (bytesPerPixel / 8)).order(ByteOrder.nativeOrder())
                    Arrays.stream((bufferedImage.data.dataBuffer as DataBufferInt).data).forEach(IntConsumer { byteBuffer.putInt(it) })
                }
                // If it's a byte buffer write it directly into the buffer
                else
                {
                    byteBuffer = ByteBuffer.allocateDirect(width * height * (bytesPerPixel / 8)).order(ByteOrder.nativeOrder())
                    byteBuffer.put((bufferedImage.data.dataBuffer as DataBufferByte).data)
                }
                // We need to flip the bytes so they get drawn correctly
                byteBuffer.flip()

                // Not very familiar with OpenGl here, but create an int buffer and generate the texture from the byte buffer

                val textureId = GLAllocation.createDirectIntBuffer(1)
                GL11.glGenTextures(textureId)

                GlStateManager.bindTexture(textureId.get(0))

                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP)
                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP)

                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)

                GlStateManager.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE.toFloat())

                GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGBA8, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer)

                // Return the texture ID
                return textureId.get(0)
            }
            // Catch any exceptions, log an error, and then shutdown java since we can't continue without fonts
            catch (e: Exception)
            {
                AfraidOfTheDark.INSTANCE.logger.error("Could not allocate an OpenGL texture!", e)
                FMLCommonHandler.instance().exitJava(-1, false)
            }

            return -1
        }
    }
}