package com.davidm1a2.afraidofthedark.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Class that allows for scaling and resizing UI components to fit the screen more easily
 */
public class AOTDGuiUtility
{
    // We use singleton design pattern here to only make one instance of this class
    private static final AOTDGuiUtility INSTANCE = new AOTDGuiUtility();

    // The current resolution that the game is being played at
    private ScaledResolution currentScaledResolution;
    private Minecraft minecraft = Minecraft.getMinecraft();

    /**
     * Constructor just initializes fields
     */
    private AOTDGuiUtility()
    {
        this.refreshScaledResolution();
    }

    /**
     * @return Getter for the one instance of this utility class
     */
    public static AOTDGuiUtility getInstance()
    {
        return INSTANCE;
    }

    /**
     * Forces our scaled resolution variable to update to the current window size, this ensures that all scale calls are accurate
     */
    public void refreshScaledResolution()
    {
        this.currentScaledResolution = new ScaledResolution(minecraft);
    }

    /**
     * Converts a coordinate from minecraft resolution to screen resolution
     *
     * @param mcCoord The MC screen coordinate
     * @return The coordinate of the UI element on the real screen
     */
    public Integer mcToRealScreenCoord(Integer mcCoord)
    {
        return mcCoord * this.currentScaledResolution.getScaleFactor();
    }

    /**
     * Converts a real screen coordinate to minecraft screen coordinate
     *
     * @param realScreenCoord The real screen coordinate
     * @return The coordinate of the UI element on the MC coordinate system
     */
    public Integer realScreenCoordToMC(Integer realScreenCoord)
    {
        return realScreenCoord / this.currentScaledResolution.getScaleFactor();
    }

    /**
     * The y coordinate on the screen to be inverted into OpenGL y coordinate
     *
     * @param realScreenY The y coordinate of the screen in normal coordinates
     * @return The y coordinate of the screen in OpenGL coordinate
     */
    public Integer realScreenYToGLYCoord(Integer realScreenY)
    {
        return minecraft.displayHeight - realScreenY;
    }

    /**
     * @return Returns the mouse's X current position in MC coordinates
     */
    public Integer getMouseXInMCCoord()
    {
        return Mouse.getX() * this.currentScaledResolution.getScaledWidth() / minecraft.displayWidth;
    }

    /**
     * @return Returns the mouse's Y current position in MC coordinates
     */
    public Integer getMouseYInMCCoord()
    {
        int scaledHeight = this.currentScaledResolution.getScaledHeight();
        return scaledHeight - Mouse.getY() * scaledHeight / Minecraft.getMinecraft().displayHeight - 1;
    }

    /**
     * @return true if either windows ctrl key is down or if either mac meta key is down
     */
    public Boolean isCtrlKeyDown()
    {
        return Minecraft.IS_RUNNING_ON_MAC ? Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA) : Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    /**
     * Tests if the clipboard has a string in it, if so returns it, if not returns empty string
     *
     * @return The current text in the clipboard or ""
     */
    public String getClipboardString()
    {
        // Get the current clipboard contents
        Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        // Make sure the clipboard has a string
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor))
        {
            // Attempt to parse that data
            try
            {
                // Get the string from the clipboard contents
                Object data = contents.getTransferData(DataFlavor.stringFlavor);
                // If the data is a string, return it
                if (data instanceof String)
                {
                    return (String) data;
                }
            } catch (UnsupportedFlavorException | IOException ignored)
            {
            }
        }
        // Return default empty string
        return StringUtils.EMPTY;
    }

    /**
     * Sets the current system clipboard to hold the given text
     *
     * @param text The text that was cut/copied
     */
    public void setClipboardString(String text)
    {
        // Empty strings are not allowed to be copied
        if (!StringUtils.isEmpty(text))
        {
            // Use string selection to store our text
            StringSelection stringSelection = new StringSelection(text);
            // Set the clipboard's contents
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        }
    }

    /**
     * @return Getter for scaled resolution, returns the last scaled resolution that was build by refreshScaledResolution()
     */
    public ScaledResolution getScaledResolution()
    {
        return this.currentScaledResolution;
    }
}
