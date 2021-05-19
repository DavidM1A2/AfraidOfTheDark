package com.davidm1a2.afraidofthedark.client.gui.fontLibrary

import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.apache.logging.log4j.LogManager
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.awt.*
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.system.exitProcess

/**
 * TrueTyper: Open Source TTF implementation for Minecraft.
 *
 * Heavily modified the implementation found online for later versions of MC 1.12+
 *
 * @constructor initializes the font glyphs
 * @param font The java font to render
 * @param antiAlias True if anti-alias is on, false otherwise
 * @param alphabet A list of characters to support
 * @property glyphs Map of font characters (Character <-> IntObject)
 * @property height Font's height
 * @property fontTextureID Texture used to cache the font 0-255 characters
 * @property textureWidth Default font texture width
 * @property textureHeight Default font texture height
 * @property fontMetrics The font metrics for our Java AWT font
 */
class TrueTypeFont internal constructor(private val font: Font, private val antiAlias: Boolean, alphabet: Set<Char>) {
    private val glyphs = mutableMapOf<Char, CharacterGlyph>()
    var height: Int = 0
        private set
    private val fontTextureID: Int
    private val textureWidth: Int
    private val textureHeight: Int
    private val fontMetrics: FontMetrics

    init {
        val supportedAlphabet = alphabet + DEFAULT_CHARACTER

        // Compute and cache the font's metrics
        fontMetrics = computeFontMetrics()

        // A multiple of 2 for the opengl texture (ex. 256, 512, or 1024)
        val textureWidthHeight = getTextureSize(supportedAlphabet)
        textureWidth = textureWidthHeight
        textureHeight = textureWidthHeight

        // Render the characters into open GL format
        fontTextureID = createTextureSheet(supportedAlphabet)
    }

    /**
     * Sets up the font metrics object used to determine the width and height of characters
     *
     * @return A font metrics instance for the font
     */
    private fun computeFontMetrics(): FontMetrics {
        // To get a graphics object we need a buffered image...
        val g = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).graphics as Graphics2D
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        }
        g.font = font
        return g.fontMetrics
    }

    /**
     * Gets the size of the texture in pixels
     *
     * @param alphabet The characters that are allowed
     * @return The size with the constraints that width = height, and it's a multiple of 2
     */
    private fun getTextureSize(alphabet: Set<Char>): Int {
        // Get the maximum possible height of each character
        val maxCharHeight = fontMetrics.height

        // Use a list of characters so it is indexable
        val indexableAlphabet = alphabet.toList()

        // Find the closest valid texture size that will hold all the glyphs up to 4096x4096
        for (possibleTextureSize in validTextureSizes) {
            var rowsRemaining = possibleTextureSize / maxCharHeight
            var currentRowLength = 0.0
            var currentCharIndex = 0

            // Go row by row and see if all the glpyhs will fit
            while (rowsRemaining > 0) {
                // If no glyphs are left, we're done
                if (currentCharIndex >= indexableAlphabet.size) {
                    return possibleTextureSize
                }

                // Get the glyph, see if it fits in this row. If not, move on to the next row
                val currentCharWidth = fontMetrics.charWidth(indexableAlphabet[currentCharIndex])
                if (currentRowLength + currentCharWidth > possibleTextureSize) {
                    currentRowLength = 0.0
                    rowsRemaining--
                } else {
                    currentRowLength = currentRowLength + currentCharWidth
                    currentCharIndex++
                }
            }
        }

        throw IllegalArgumentException("Texture width/height could not be created as it would be larger than ${validTextureSizes.maxOrNull()}")
    }

    /**
     * Initializes the font by rendering each character to an image
     *
     * @param alphabet The extra non-ascii characters to render
     */
    private fun createTextureSheet(alphabet: Set<Char>): Int {
        // Create a temp buffered image to write to
        val imgTemp = BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB)
        // Grab the graphics object to write to the image
        val g = imgTemp.graphics as Graphics2D

        // Set the color to blank
        g.color = Color(0, 0, 0, 0)
        // Fill the rectangle with black
        g.fillRect(0, 0, textureWidth, textureHeight)

        // 3 values to use in writing the glyphs to the image
        // The current row's height
        var rowHeight = 0
        // The current glyph pos x and y
        var positionX = 0
        var positionY = 0

        // Go over each character
        for (character in alphabet) {
            // Render the character into an image
            val fontImage = getFontImage(character)

            // Create a new character glyph for this font
            val characterGlyph = CharacterGlyph()

            // Assign the width and height fields
            characterGlyph.width = fontImage.width
            characterGlyph.height = fontImage.height

            // If the glyph is too big for the texture move down a line
            if (positionX + characterGlyph.width >= textureWidth) {
                // Reset X to the far left
                positionX = 0
                // Move Y down a row
                positionY = positionY + rowHeight
                // Reset row height to be 0, the current row has no glyphs
                rowHeight = 0
            }

            // Assign the glyph position on the texture
            characterGlyph.storedX = positionX
            characterGlyph.storedY = positionY

            // The font height is the max of the current height and the new glyph's height
            height = max(height, characterGlyph.height)

            // The row height is the max of the current row height and the new glyph's height
            rowHeight = max(rowHeight, characterGlyph.height)

            // Draw the character glyph to the large texture
            g.drawImage(fontImage, positionX, positionY, null)

            // Move the X position over by the glyph's width
            positionX = positionX + characterGlyph.width

            glyphs[character] = characterGlyph
        }

        // Once we're done writing all of our glyphs onto the 1024x1024 image we load it into open GL for rendering
        return loadImage(imgTemp)
    }

    /**
     * Converts a character to an image by writing it to an image
     *
     * @param character The character to write
     * @return The buffered image representing the character
     */
    private fun getFontImage(character: Char): BufferedImage {
        // Compute the width of the current character
        var charWidth = fontMetrics.charWidth(character)

        // If our character has no width just use a width of 1 and don't do anything with it
        charWidth = charWidth.coerceAtLeast(1)

        // Compute the height of the character
        var charHeight = fontMetrics.height

        // If the char's height is invalid just use the font size
        if (charHeight <= 0) {
            charHeight = font.size
        }

        // Create another image holding the character we are creating
        val fontImage = BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB)

        // Extract the graphics component that we write to
        val g = fontImage.graphics as Graphics2D

        // Set the anti-alias flag if needed
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        }

        // Set the font
        g.font = font

        // Set the color to white
        g.color = Color.WHITE

        // Write the character onto the image
        g.drawString(character.toString(), 0, fontMetrics.ascent)

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
    fun drawString(
            x: Float,
            y: Float,
            stringToDraw: String,
            scaleX: Float,
            scaleY: Float,
            textAlignment: TextAlignment,
            rgba: Color
    ) {
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
            rgba: Color
    ) {
        // The current glyph being drawn
        var characterGlyph: CharacterGlyph
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
        when (textAlignment) {
            // Align the text right
            TextAlignment.ALIGN_RIGHT -> {
                // Align right is set, so use flag = -1
                alignmentFlag = -1
                // Set spacing correction to right since we are aligned right
                spacingCorrection = CORRECT_R

                // Iterate while our current index is smaller than the end index so there are still characters to draw
                while (currentIndex < endIndex) {
                    // For each new line move the start y coordinate down by the font's height
                    if (stringToDraw[currentIndex] == '\n') {
                        startY = startY - height
                    }
                    currentIndex++
                }
            }
            // Align the text centered
            TextAlignment.ALIGN_CENTER -> {
                // Go over each character in the string
                for (i in startIndex..endIndex) {
                    // Grab the character
                    currentChar = stringToDraw[i]

                    // If the character is a new line we're done, break and align on this alone
                    if (currentChar == '\n') {
                        break
                    }

                    // Grab the current glyph, if it's an ascii character grab it from the array, otherwise get it from
                    // our custom character hash map
                    characterGlyph = glyphs[currentChar] ?: glyphs[DEFAULT_CHARACTER]!!

                    // Increase our total width by the glyph's width
                    totalWidth = totalWidth + (characterGlyph.width - CORRECT_L)
                }
                // Divide the total width by 2 to get the center the current line
                totalWidth = totalWidth / -2

                // Set the alignment flag to be 1 to render l->r
                alignmentFlag = 1
                // Use the left spacing correction
                spacingCorrection = CORRECT_L
            }
            // Align the text left, also used for align center
            TextAlignment.ALIGN_LEFT -> {
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
        GlStateManager.color4f(rgba.red / 255f, rgba.green / 255f, rgba.blue / 255f, rgba.alpha / 255f)
        // Loop while the current index is between the start and end index
        while (currentIndex in startIndex..endIndex) {
            // Grab the current character to draw
            currentChar = stringToDraw[currentIndex]
            // Grab the glyph to draw, it will either be ascii or in the additional glyphs map
            characterGlyph = glyphs[currentChar] ?: glyphs[DEFAULT_CHARACTER]!!

            // If the alignment flag is -1 align right is set so we reduce the total width
            if (alignmentFlag < 0) {
                totalWidth = totalWidth + (characterGlyph.width - spacingCorrection) * alignmentFlag
            }
            // If the current character is a new line change lines
            if (currentChar == '\n') {
                // Move in the right direction (up if align right, down otherwise)
                startY = startY - height * alignmentFlag
                // Reset the width since we're on a new line
                totalWidth = 0
                // If the alignment is center we need to compute the width of the next line
                if (textAlignment == TextAlignment.ALIGN_CENTER) {
                    // Iterate over the next line, compute its width
                    for (i in currentIndex + 1..endIndex) {
                        // Grab the current character
                        currentChar = stringToDraw[i]
                        // If we've hit a new new line then the line is over
                        if (currentChar == '\n') {
                            break
                        }

                        // Set the current glyph to be either an ascii glyph or a custom glyph if it's bigger than 255
                        characterGlyph = glyphs[currentChar] ?: glyphs[DEFAULT_CHARACTER]!!
                        // Increase the width by the glyph width, subtract off the spacing modifier
                        totalWidth = totalWidth + (characterGlyph.width - CORRECT_L)
                    }
                    // Center the text, so divide by 2
                    totalWidth = totalWidth / -2
                }
            }
            // If the current char is not a new line then we can continue drawing our character
            else {
                // Draw a letter
                drawQuad(
                    totalWidth * scaleX + x,
                    startY * scaleY + y,
                    (totalWidth + characterGlyph.width) * scaleX + x,
                    (startY + characterGlyph.height) * scaleY + y,
                    (characterGlyph.storedX + characterGlyph.width).toFloat(),
                    characterGlyph.storedY.toFloat() + characterGlyph.height,
                    characterGlyph.storedX.toFloat(),
                    characterGlyph.storedY.toFloat()
                )
                // If we are aligning left then increase the width of the current line
                if (alignmentFlag > 0) {
                    totalWidth = totalWidth + (characterGlyph.width - spacingCorrection) * alignmentFlag
                }
            }
            // Advance or reduce the current index depending on if we're aligning right or left
            currentIndex = currentIndex + alignmentFlag
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
    private fun drawQuad(
        drawX: Float,
        drawY: Float,
        drawX2: Float,
        drawY2: Float,
        srcX: Float,
        srcY: Float,
        srcX2: Float,
        srcY2: Float
    ) {
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
    fun getWidth(string: String): Float {
        return this.fontMetrics.stringWidth(string).toFloat()
    }

    /**
     * Destroys the font and releases the resources
     */
    fun destroy() {
        // Create the buffer with the texture id, bind the texture, and delete the texture
        val scratch = BufferUtils.createIntBuffer(1)
        scratch.put(0, fontTextureID)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
        GL11.glDeleteTextures(scratch)
    }

    /**
     * Utility class for storing glyph positions
     */
    private data class CharacterGlyph(
        // Character's width
        var width: Int = 0,
        // Character's height
        var height: Int = 0,
        // Character's stored x position
        var storedX: Int = 0,
        // Character's stored y position
        var storedY: Int = 0
    )

    companion object {
        private val logger = LogManager.getLogger()

        // Correction constants used to render letters closer together. These cause a big with getWidth() currently
        private const val CORRECT_L = 0 // 2
        private const val CORRECT_R = 0 // 1

        // Default character
        private const val DEFAULT_CHARACTER = '?'

        private val validTextureSizes = (6..12).map { 2.0.pow(it).toInt() }

        /**
         * Converts a buffered image into an open GL ready image to be loaded
         *
         * @param bufferedImage The buffered image to load into OpenGL
         * @return The ID of the texture used to reference this image
         */
        private fun loadImage(bufferedImage: BufferedImage): Int {
            try {
                // Grab the width and height of the texture
                val width = bufferedImage.width
                val height = bufferedImage.height
                // Get the number of bytes per pixel
                val bitsPerPixel = bufferedImage.colorModel.pixelSize
                val byteBuffer: ByteBuffer
                // Grab the data buffer used by the buffered image
                val db = bufferedImage.data.dataBuffer
                // If it's an int buffer write each int to the data buffer 4 bytes at a time
                if (db is DataBufferInt) {
                    byteBuffer =
                        ByteBuffer.allocateDirect(width * height * (bitsPerPixel / 8)).order(ByteOrder.nativeOrder())
                    db.data.forEach { byteBuffer.putInt(it) }
                }
                // If it's a byte buffer write it directly into the buffer
                else {
                    byteBuffer =
                        ByteBuffer.allocateDirect(width * height * (bitsPerPixel / 8)).order(ByteOrder.nativeOrder())
                    byteBuffer.put((db as DataBufferByte).data)
                }
                // We need to flip the bytes so they get drawn correctly
                byteBuffer.flip()

                // Not very familiar with OpenGl here, but create an int buffer and generate the texture from the byte buffer

                val textureBuffer = GLAllocation.createDirectByteBuffer(4).asIntBuffer()
                GL11.glGenTextures(textureBuffer)

                val textureId = textureBuffer.get(0)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GlStateManager.bindTexture(textureId)

                GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP)
                GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP)

                GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
                GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)

                GlStateManager.texEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE.toFloat())

                GlStateManager.pixelStore(GL11.GL_UNPACK_ROW_LENGTH, 0)
                GlStateManager.pixelStore(GL11.GL_UNPACK_SKIP_ROWS, 0)
                GlStateManager.pixelStore(GL11.GL_UNPACK_SKIP_PIXELS, 0)
                GlStateManager.pixelStore(GL11.GL_UNPACK_ALIGNMENT, bitsPerPixel / 8)

                GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL11.GL_RGBA8,
                    width,
                    height,
                    0,
                    GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE,
                    byteBuffer
                )
                GL11.glDisable(GL11.GL_TEXTURE_2D)

                // Return the texture ID
                return textureId
            }
            // Catch any exceptions, log an error, and then shutdown java since we can't continue without fonts
            catch (e: Exception) {
                logger.error("Could not allocate an OpenGL texture!", e)
                exitProcess(-1)
            }
        }
    }
}