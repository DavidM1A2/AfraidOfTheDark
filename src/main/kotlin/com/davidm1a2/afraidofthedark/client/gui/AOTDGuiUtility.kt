package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility.minecraft
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility.window
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import net.minecraft.client.Minecraft
import kotlin.math.roundToInt

/**
 * Class that allows for scaling and resizing UI components to fit the screen more easily
 *
 * @constructor just initializes fields
 * @property minecraft The minecraft instance
 * @property window Getter for window, returns the last scaled resolution that was build by refreshScaledResolution()
 */
object AOTDGuiUtility {
    private val minecraft = Minecraft.getInstance()
    private val window = minecraft.mainWindow

    /**
     * Converts a coordinate from minecraft resolution to screen resolution
     *
     * @param mcCoord The MC screen coordinate
     * @return The coordinate of the UI element on the real screen
     */
    fun mcToRealScreenCoord(mcCoord: Int): Int {
        return mcCoord * this.window.calcGuiScale(minecraft.gameSettings.guiScale, minecraft.forceUnicodeFont)
    }

    /**
     * Converts a real screen coordinate to minecraft screen coordinate
     *
     * @param realScreenCoord The real screen coordinate
     * @return The coordinate of the UI element on the MC coordinate system
     */
    fun realScreenCoordToMC(realScreenCoord: Int): Int {
        return realScreenCoord / this.window.calcGuiScale(minecraft.gameSettings.guiScale, minecraft.forceUnicodeFont)
    }

    /**
     * The y coordinate on the screen to be inverted into OpenGL y coordinate
     *
     * @param realScreenY The y coordinate of the screen in normal coordinates
     * @return The y coordinate of the screen in OpenGL coordinate
     */
    fun realScreenYToGLYCoord(realScreenY: Int): Int {
        return window.height - realScreenY
    }

    /**
     * @return Returns the mouse's X current position in MC coordinates
     */
    fun getMouseXInMCCoord(): Int {
        return if (window.width == 0) 0 else minecraft.mouseHelper.mouseX.roundToInt() * this.window.scaledWidth / window.width
    }

    /**
     * @return Returns the mouse's Y current position in MC coordinates
     */
    fun getMouseYInMCCoord(): Int {
        return if (window.height == 0) 0 else minecraft.mouseHelper.mouseY.roundToInt() * this.window.scaledHeight / window.height
    }

    fun getWindowWidthInMCCoords(): Int {
        return realScreenCoordToMC(window.width)
    }

    fun getWindowHeightInMCCoords(): Int {
        return realScreenCoordToMC(window.height)
    }

    fun getWindowSizeInMCCoords(): Dimensions {
        return Dimensions(getWindowWidthInMCCoords().toDouble(), getWindowHeightInMCCoords().toDouble(), false)
    }
}
