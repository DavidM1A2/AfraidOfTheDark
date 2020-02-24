package com.davidm1a2.afraidofthedark.client.gui.base

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseScrollEvent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel
import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import java.io.IOException
import kotlin.math.min
import kotlin.math.round

/**
 * Base class for all GuiScreens used by the mod, allows support for things such as action listeners and proper UI scaling
 *
 * @property contentPane The panel that contains all the content on the screen
 * @property spriteSheetControllers A list of sprite sheet controllers that are used to control sprite sheets
 * @property leftMouseButtonDown Flag telling us if the left mouse button is down or not
 */
abstract class AOTDGuiScreen : GuiScreen() {
    // Don't cache these in a companion object, they can change!
    val entityPlayer: EntityPlayerSP
        get() = Minecraft.getMinecraft().player
    val inventoryKeycode: Int
        get() = Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode

    val contentPane: AOTDGuiPanel = AOTDGuiPanel(0, 0, Constants.GUI_WIDTH, Constants.GUI_HEIGHT, false)
    private val spriteSheetControllers = mutableListOf<SpriteSheetController>()
    private var leftMouseButtonDown = false

    /**
     * Called to initialize the GUI screen
     */
    override fun initGui() {
        super.initGui()
        // Force our GUI utility to update its scaled resolution
        AOTDGuiUtility.refreshScaledResolution()
        // Clear all buttons on the screen
        this.buttonList.clear()

        // Compute the correct X and Y gui scale that we should use
        val guiScaleX = this.width / Constants.GUI_WIDTH.toDouble()
        val guiScaleY = this.height / Constants.GUI_HEIGHT.toDouble()
        // Set the gui screen's gui scale
        val guiScale = min(guiScaleX, guiScaleY)
        // Set the content pane's gui scale
        this.contentPane.setScaleXAndY(guiScale)

        // If our X scale is less than our Y scale we pin the X coordinate to the left side of the screen and set the Y to center the GUI panel
        if (guiScaleX < guiScaleY) {
            this.contentPane.setX(0)
            // We must multiply by 1 / guiScale so that our Y position is centered and not scaled since 1 / guiScale * guiScale = 1
            this.contentPane.setY(round((this.height - this.contentPane.getHeightScaled()) / 2f * (1 / guiScale)).toInt())
        }
        // If our Y scale is less than our X scale we pin the Y coordinate to the top of the screen and set the X to center the GUI panel
        else {
            // We must multiply by 1 / guiScale so that our X position is centered and not scaled since 1 / guiScale * guiScale = 1
            this.contentPane.setX(round((this.width - this.contentPane.getWidthScaled()) / 2f * (1 / guiScale)).toInt())
            this.contentPane.setY(0)
        }
    }

    /**
     * Most important method that draws the GUI screen, here we ask our content pane to draw itself
     *
     * @param mouseX       The current mouse's X position
     * @param mouseY       The current mouse's Y position
     * @param partialTicks How much time has happened since the last tick, ignored
     */
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        // First update all of our sprite sheet controllers
        this.spriteSheetControllers.forEach { it.performUpdate() }
        // Enable blend so we can draw opacity
        GlStateManager.enableBlend()
        // If we want a gradient background draw that background
        if (this.drawGradientBackground()) {
            this.drawDefaultBackground()
        }
        // Draw the content pane
        this.contentPane.draw()
        // Draw the overlay on top of the content pane
        this.contentPane.drawOverlay()
        // Call the super method
        super.drawScreen(mouseX, mouseY, partialTicks)
        // Disable blend now that we drew the UI
        GlStateManager.disableBlend()
    }

    /**
     * @return True if the screen should have a gradient background, false otherwise
     */
    abstract fun drawGradientBackground(): Boolean

    /**
     * @return True if this gui screen pauses the game, false otherwise
     */
    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    /**
     * Called whenever a key is typed, we ask our key handler to handle the event
     *
     * @param character The character typed
     * @param keyCode   The code of the character typed
     * @throws IOException forwarded from the super method
     */
    override fun keyTyped(character: Char, keyCode: Int) {
        // Fire the process key event on our content pane
        this.contentPane.processKeyInput(
            AOTDKeyEvent(
                this.contentPane,
                character,
                keyCode,
                AOTDKeyEvent.KeyEventType.Type
            )
        )
        // If our inventory key closes the screen, test if that key was pressed
        if (this.inventoryToCloseGuiScreen()) {
            // if the keycode is the inventory key bind close the GUI screen
            if (keyCode == inventoryKeycode) {
                // Close the screen
                entityPlayer.closeScreen()
                GL11.glFlush()
            }
        }
        // Call super afterwards to finish any default MC processing
        super.keyTyped(character, keyCode)
    }

    /**
     * @return True if the inventory key closes the screen, false otherwise
     */
    abstract fun inventoryToCloseGuiScreen(): Boolean

    /**
     * Adds a sprite sheet controller to the list that will be updated every tick
     *
     * @param sheetController The controller to update every tick
     */
    fun addSpriteSheetController(sheetController: SpriteSheetController) {
        this.spriteSheetControllers.add(sheetController)
    }

    /**
     * Called whenever mouse input should be handled
     */
    override fun handleMouseInput() {
        // Ensure to call super so default MC functions are called
        super.handleMouseInput()
        // Figure out what mouse button was pressed
        val mouseButton = Mouse.getEventButton()
        if (mouseButton != -1) {
            this.processMouseClick(mouseButton)
        } else {
            this.processMouseMove()
        }

        val mouseWheelDistance = Mouse.getDWheel()
        // - distance means we scrolled backwards, + distance means we scrolled forwards
        if (mouseWheelDistance != 0) {
            this.processMouseScroll(mouseWheelDistance)
        }
    }

    /**
     * Called to process the mouse click
     *
     * @param clickedButton The button that was clicked
     */
    private fun processMouseClick(clickedButton: Int) {
        // The X position of the mouse
        val mouseX = AOTDGuiUtility.getMouseXInMCCoord()
        // The Y position of the mouse
        val mouseY = AOTDGuiUtility.getMouseYInMCCoord()

        // If the mouse button is pressed set the flag
        if (Mouse.getEventButtonState()) {
            leftMouseButtonDown = true
            // Fire the mouse clicked event
            contentPane.processMouseInput(
                AOTDMouseEvent(
                    contentPane,
                    mouseX,
                    mouseY,
                    clickedButton,
                    AOTDMouseEvent.EventType.Click
                )
            )
        }
        // If it's not pressed fire the mouse released event and press event
        else {
            // Fire the release event for sure
            contentPane.processMouseInput(
                AOTDMouseEvent(
                    contentPane,
                    mouseX,
                    mouseY,
                    clickedButton,
                    AOTDMouseEvent.EventType.Release
                )
            )
            // If the left mouse button was down fire the press event
            if (leftMouseButtonDown) {
                leftMouseButtonDown = false
                contentPane.processMouseInput(
                    AOTDMouseEvent(
                        contentPane,
                        mouseX,
                        mouseY,
                        clickedButton,
                        AOTDMouseEvent.EventType.Press
                    )
                )
            }
        }
    }

    /**
     * Called to process any mouse move events
     */
    private fun processMouseMove() {
        // Grab the X and Y coordinates of the mouse
        val mouseX = AOTDGuiUtility.getMouseXInMCCoord()
        val mouseY = AOTDGuiUtility.getMouseYInMCCoord()
        // Fire the content pane's move listener
        contentPane.processMouseMoveInput(
            AOTDMouseMoveEvent(
                contentPane,
                mouseX,
                mouseY,
                AOTDMouseMoveEvent.EventType.Move
            )
        )
        // If the left mouse button is down fire the content pane's drag listener
        if (leftMouseButtonDown) {
            contentPane.processMouseMoveInput(
                AOTDMouseMoveEvent(
                    contentPane,
                    mouseX,
                    mouseY,
                    AOTDMouseMoveEvent.EventType.Drag
                )
            )
        }
    }

    /**
     * Processes the mouse scroll distance
     *
     * @param distance A non-zero value of the amount we scrolled
     */
    private fun processMouseScroll(distance: Int) {
        // Fire the content pane's mouse scroll listener
        contentPane.processMouseScrollInput(AOTDMouseScrollEvent(contentPane, distance))
    }
}
