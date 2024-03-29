package com.davidm1a2.afraidofthedark.client.gui.fontLibrary

import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.apache.logging.log4j.LogManager
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.system.exitProcess

/**
 * Modified TTF implementation for Minecraft.
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
                    currentRowLength += currentCharWidth
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
                positionY += rowHeight
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
            positionX += characterGlyph.width

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

    private fun calcGuiScale(): Float {
        val minecraft = Minecraft.getInstance()
        val min = min(minecraft.window.width, minecraft.window.height)
        val refSize = Constants.REFERENCE_SIZE  // Our reference screen size is 480p (ie. text will look like gui scale 1 at 480p)
        return Constants.TEXT_SCALE_FACTOR * min / refSize / minecraft.window.calculateScale(minecraft.options.guiScale, minecraft.isEnforceUnicode)
    }

    /**
     * Draws a string at a given x,y position with a scale, alignment, and color
     *
     * @param x The x position to draw at
     * @param y The y position to draw at
     * @param stringToDraw The string to draw
     * @param textAlignment The alignment to draw the text with
     * @param rgba The color to use when drawing the string
     */
    fun drawString(
        x: Float,
        y: Float,
        stringToDraw: String,
        textAlignment: TextAlignment,
        rgba: Color
    ) {
        // The current glyph being drawn
        var characterGlyph: CharacterGlyph
        var totalWidth = 0
        var drawX = 0
        var drawY = 0

        // Multiply the scale by the overall gui scale
        val guiScale = calcGuiScale()

        // Get the width of the widest line of text
        for (line in stringToDraw.split("\n")) {
            var lineLen = 0
            for (ch in line) {
                lineLen += fontMetrics.charWidth(ch)
            }
            if (lineLen > totalWidth) totalWidth = lineLen
        }

        // Bind our custom texture sheet
        RenderSystem.bindTexture(fontTextureID)
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.builder
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX)
        val red = rgba.red / 255f
        val green = rgba.green / 255f
        val blue = rgba.blue / 255f
        val alpha = rgba.alpha / 255f
        for (line in stringToDraw.split("\n")) {
            // Set start position
            drawX = when (textAlignment) {
                TextAlignment.ALIGN_CENTER -> -(this.fontMetrics.stringWidth(line) / 2)
                TextAlignment.ALIGN_LEFT -> 0
                TextAlignment.ALIGN_RIGHT -> -this.fontMetrics.stringWidth(line)
            }
            // Draw each character
            for (currentChar in line) {
                // Grab the glyph to draw, it will either be ascii or in the additional glyphs map
                characterGlyph = glyphs[currentChar] ?: glyphs[DEFAULT_CHARACTER]!!
                // Draw a letter
                drawQuad(
                    drawX * guiScale + x,
                    drawY * guiScale + y,
                    (drawX + characterGlyph.width) * guiScale + x,
                    (drawY + characterGlyph.height) * guiScale + y,
                    characterGlyph.storedX.toFloat(),
                    characterGlyph.storedY.toFloat(),
                    characterGlyph.storedX.toFloat() + characterGlyph.width,
                    characterGlyph.storedY.toFloat() + characterGlyph.height,
                    red,
                    green,
                    blue,
                    alpha
                )
                drawX += characterGlyph.width
            }
            // On newline
            drawY += height
        }
        // Finally draw the screen now that all the glyphs are in place
        tessellator.end()
    }

    /**
     * Draws a glyph using a quad on the screen
     */
    private fun drawQuad(
        drawX: Float,
        drawY: Float,
        drawX2: Float,
        drawY2: Float,
        srcX: Float,
        srcY: Float,
        srcX2: Float,
        srcY2: Float,
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ) {
        // Compute the width and height of the glyph to draw
        val drawWidth = abs(drawX2 - drawX)
        val drawHeight = abs(drawY2 - drawY)
        // Compute the width and height of the glyph on the source
        val srcWidth = abs(srcX2 - srcX)
        val srcHeight = abs(srcY2 - srcY)
        // Grab the tessellator instance and buffer builder
        val bufferBuilder = Tessellator.getInstance().builder

        // Add the 4 vertices that are used to draw the glyph. These must be done in this order
        bufferBuilder.vertex(drawX.toDouble(), (drawY + drawHeight).toDouble(), 0.0)
            .color(r, g, b, a)
            .uv(srcX / textureWidth, (srcY + srcHeight) / textureHeight)
            .endVertex()
        bufferBuilder.vertex((drawX + drawWidth).toDouble(), (drawY + drawHeight).toDouble(), 0.0)
            .color(r, g, b, a)
            .uv((srcX + srcWidth) / textureWidth, (srcY + srcHeight) / textureHeight)
            .endVertex()
        bufferBuilder.vertex((drawX + drawWidth).toDouble(), drawY.toDouble(), 0.0)
            .color(r, g, b, a)
            .uv((srcX + srcWidth) / textureWidth, srcY / textureHeight)
            .endVertex()
        bufferBuilder.vertex(drawX.toDouble(), drawY.toDouble(), 0.0)
            .color(r, g, b, a)
            .uv(srcX / textureWidth, srcY / textureHeight)
            .endVertex()
    }

    /**
     * Gets the rendered width of a given string
     */
    fun getWidth(string: String): Int {
        return (this.fontMetrics.stringWidth(string) * calcGuiScale()).roundToInt()
    }

    /**
     * Gets the rendered height of a given string
     */
    fun getHeight(string: String = ""): Int {
        return (this.fontMetrics.height * calcGuiScale() * (string.count { it == '\n' } + 1)).roundToInt()
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

                val textureBuffer = GLAllocation.createByteBuffer(4).asIntBuffer()
                GL11.glGenTextures(textureBuffer)

                val textureId = textureBuffer.get(0)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                RenderSystem.bindTexture(textureId)

                RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP)
                RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP)

                RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
                RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)

                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE)

                RenderSystem.pixelStore(GL11.GL_UNPACK_ROW_LENGTH, 0)
                RenderSystem.pixelStore(GL11.GL_UNPACK_SKIP_ROWS, 0)
                RenderSystem.pixelStore(GL11.GL_UNPACK_SKIP_PIXELS, 0)
                RenderSystem.pixelStore(GL11.GL_UNPACK_ALIGNMENT, bitsPerPixel / 8)

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