package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility.minecraft
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility.scaledResolution
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.IOException

/**
 * Class that allows for scaling and resizing UI components to fit the screen more easily
 *
 * @constructor just initializes fields
 * @property minecraft The minecraft instance
 * @property scaledResolution Getter for scaled resolution, returns the last scaled resolution that was build by refreshScaledResolution()
 */
object AOTDGuiUtility
{
    private val minecraft = Minecraft.getMinecraft()
    private var scaledResolution: ScaledResolution = ScaledResolution(minecraft)

    /**
     * Forces our scaled resolution variable to update to the current window size, this ensures that all scale calls are accurate
     */
    fun refreshScaledResolution()
    {
        this.scaledResolution = ScaledResolution(minecraft)
    }

    /**
     * Converts a coordinate from minecraft resolution to screen resolution
     *
     * @param mcCoord The MC screen coordinate
     * @return The coordinate of the UI element on the real screen
     */
    fun mcToRealScreenCoord(mcCoord: Int): Int
    {
        return mcCoord * this.scaledResolution.scaleFactor
    }

    /**
     * Converts a real screen coordinate to minecraft screen coordinate
     *
     * @param realScreenCoord The real screen coordinate
     * @return The coordinate of the UI element on the MC coordinate system
     */
    fun realScreenCoordToMC(realScreenCoord: Int): Int
    {
        return realScreenCoord / this.scaledResolution.scaleFactor
    }

    /**
     * The y coordinate on the screen to be inverted into OpenGL y coordinate
     *
     * @param realScreenY The y coordinate of the screen in normal coordinates
     * @return The y coordinate of the screen in OpenGL coordinate
     */
    fun realScreenYToGLYCoord(realScreenY: Int): Int
    {
        return minecraft.displayHeight - realScreenY
    }

    /**
     * @return Returns the mouse's X current position in MC coordinates
     */
    fun getMouseXInMCCoord(): Int
    {
        return Mouse.getX() * this.scaledResolution.scaledWidth / minecraft.displayWidth
    }

    /**
     * @return Returns the mouse's Y current position in MC coordinates
     */
    fun getMouseYInMCCoord(): Int
    {
        val scaledHeight: Int = this.scaledResolution.scaledHeight
        return scaledHeight - Mouse.getY() * scaledHeight / Minecraft.getMinecraft().displayHeight - 1
    }

    /**
     * @return true if either windows ctrl key is down or if either mac meta key is down
     */
    fun isCtrlKeyDown(): Boolean
    {
        return if (Minecraft.IS_RUNNING_ON_MAC)
        {
            Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)
        }
        else
        {
            Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)
        }
    }

    /**
     * Sets the current system clipboard to hold the given text
     *
     * @param text The text that was cut/copied
     */
    fun setClipboardString(text: String)
    {
        // Empty strings are not allowed to be copied
        if (text.isNotEmpty())
        {
            // Use string selection to store our text
            val stringSelection = StringSelection(text)
            // Set the clipboard's contents
            Toolkit.getDefaultToolkit().systemClipboard.setContents(stringSelection, null)
        }
    }

    /**
     * Tests if the clipboard has a string in it, if so returns it, if not returns empty string
     *
     * @return The current text in the clipboard or ""
     */
    fun getClipboardString(): String
    {
        // Get the current clipboard contents
        val contents = Toolkit.getDefaultToolkit().systemClipboard.getContents(null)

        // Make sure the clipboard has a string
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor))
        {
            // Attempt to parse that data
            try
            {
                // Get the string from the clipboard contents
                val data = contents.getTransferData(DataFlavor.stringFlavor)

                // If the data is a string, return it
                if (data is String)
                {
                    return data
                }
            } catch (ignored: UnsupportedFlavorException)
            {
            } catch (ignored: IOException)
            {
            }
        }

        // Return default empty string
        return ""
    }
}
