package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility.clipboardString
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility.isCtrlKeyDown
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility.minecraft
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility.mouseXInMCCoord
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility.mouseYInMCCoord
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility.scaledResolution
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import org.apache.commons.lang3.StringUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

/**
 * Class that allows for scaling and resizing UI components to fit the screen more easily
 *
 * @constructor just initializes fields
 * @property minecraft The minecraft instance
 * @property scaledResolution Getter for scaled resolution, returns the last scaled resolution that was build by refreshScaledResolution()
 * @property mouseXInMCCoord the mouse's X current position in MC coordinates
 * @property mouseYInMCCoord the mouse's Y current position in MC coordinates
 * @property isCtrlKeyDown True if the ctrl or cmd key is down, false otherwise
 * @property clipboardString the current system clipboard text
 */
object AOTDGuiUtility
{
    private val minecraft = Minecraft.getMinecraft()
    private var scaledResolution: ScaledResolution = ScaledResolution(minecraft)

    val mouseXInMCCoord: Int
        get() = Mouse.getX() * this.scaledResolution.scaledWidth / minecraft.displayWidth
    val mouseYInMCCoord: Int
        get()
        {
            val scaledHeight = this.scaledResolution.scaledHeight
            return scaledHeight - Mouse.getY() * scaledHeight / Minecraft.getMinecraft().displayHeight - 1
        }

    val isCtrlKeyDown: Boolean
        get() =
            if (Minecraft.IS_RUNNING_ON_MAC)
                Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)
            else
                Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)

    var clipboardString: String
        get()
        {
            val contents = Toolkit.getDefaultToolkit().systemClipboard.getContents(null)
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor))
            {
                val data = contents.getTransferData(DataFlavor.stringFlavor)
                if (data is String)
                {
                    return data
                }
            }
            return StringUtils.EMPTY
        }
        set(text)
        {
            if (!StringUtils.isEmpty(text))
            {
                val stringSelection = StringSelection(text)
                Toolkit.getDefaultToolkit().systemClipboard.setContents(stringSelection, null)
            }
        }

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
}
