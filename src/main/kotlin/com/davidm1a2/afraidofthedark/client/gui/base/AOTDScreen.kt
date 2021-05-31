package com.davidm1a2.afraidofthedark.client.gui.base

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableConsumer
import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.events.*
import com.davidm1a2.afraidofthedark.client.gui.layout.AbsolutePosition
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.InputMappings
import net.minecraft.util.text.ITextComponent
import org.lwjgl.glfw.GLFW
import kotlin.math.roundToInt

/**
 * Base class for all GuiScreens used by the mod, allows support for things such as action listeners and proper UI scaling
 */
abstract class AOTDScreen(name: ITextComponent, private val dragAndDropEnabled: Boolean = false) : Screen(name) {
    // Don't cache these in a companion object, they can change!
    val entityPlayer: ClientPlayerEntity
        get() = Minecraft.getInstance().player

    val contentPane = StackPane(AOTDGuiUtility.getWindowSizeInMCCoords())
    private val overlayPane = StackPane(AOTDGuiUtility.getWindowSizeInMCCoords())
    private val spriteSheetControllers = mutableListOf<SpriteSheetController>()
    private var dragAndDropIcon : ImagePane? = null
    private var dragAndDropData : Any? = null
    private var prevMouseX = 0
    private var prevMouseY = 0

    /**
     * Called to initialize the GUI screen
     */
    override fun init() {
        super.init()
        // Clear all buttons on the screen
        this.buttons.clear()
        // Draw the pane
        this.invalidate()
    }

    open fun invalidate() {
        println("Window invalidated!")
        val windowSize = AOTDGuiUtility.getWindowSizeInMCCoords()
        // Only redraw screen if the new dimensions are valid
        if (windowSize.width > 0 && windowSize.height > 0) {
            // Record dimensions so we can tell when they change
            this.contentPane.prefSize = windowSize
            // Fit panes to the screen
            this.contentPane.negotiateDimensions(windowSize.width, windowSize.height)
            this.overlayPane.negotiateDimensions(windowSize.width, windowSize.height)
            // Resize any children to fit the new dimensions
            this.contentPane.calcChildrenBounds()
            this.overlayPane.calcChildrenBounds()
        }
        // Send the mouse position to the updated pane
        this.contentPane.processMouseMoveInput(AOTDMouseMoveEvent(
            contentPane,
            AOTDGuiUtility.getMouseXInMCCoord(),
            AOTDGuiUtility.getMouseYInMCCoord(),
            AOTDMouseMoveEvent.EventType.Move)
        )
    }

    /**
     * Most important method that draws the GUI screen, here we ask our content pane to draw itself
     *
     * @param mouseX       The current mouse's X position
     * @param mouseY       The current mouse's Y position
     * @param partialTicks How much time has happened since the last tick, ignored
     */
    override fun render(mouseX: Int, mouseY: Int, partialTicks: Float) {
        // First update all of our sprite sheet controllers
        this.spriteSheetControllers.forEach { it.performUpdate() }
        // Enable blend so we can draw opacity
        GlStateManager.enableBlend()
        // If we want a gradient background draw that background
        if (this.drawGradientBackground()) {
            this.renderBackground()
        }
        // Do lazy updates only when the screen has changed size
        if (contentPane.prefSize != AOTDGuiUtility.getWindowSizeInMCCoords()) {
            this.invalidate()
        }
        // Draw the content pane
        this.contentPane.draw()
        // Draw the overlay on top of the content pane
        this.contentPane.drawOverlay()
        // Draw the overlay pane
        if (dragAndDropEnabled) this.overlayPane.draw()
        // Call the super method
        super.render(mouseX, mouseY, partialTicks)
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
    override fun isPauseScreen(): Boolean {
        return false
    }

    override fun charTyped(char: Char, modifiers: Int): Boolean {
        // Fire the process key event on our content pane
        this.contentPane.processKeyInput(
            AOTDKeyEvent(
                this.contentPane,
                Int.MIN_VALUE, // Default since we don't know what int the char corresponds to
                Int.MIN_VALUE, // Default since we don't know what int the char corresponds to
                char,
                modifiers,
                AOTDKeyEvent.KeyEventType.Type
            )
        )

        return super.charTyped(char, modifiers)
    }

    override fun keyPressed(key: Int, scanCode: Int, modifiers: Int): Boolean {
        // Fire the process key event on our content pane
        this.contentPane.processKeyInput(
            AOTDKeyEvent(
                this.contentPane,
                key,
                scanCode,
                Char.MIN_VALUE, // Default since we don't know what char the int corresponds to
                modifiers,
                AOTDKeyEvent.KeyEventType.Press
            )
        )

        if (super.keyPressed(key, scanCode, modifiers)) {
            return true
        } else {
            // If our inventory key closes the screen, test if that key was pressed
            if (this.inventoryToCloseGuiScreen()) {
                // if the keycode is the inventory key bind close the GUI screen
                if (isInventoryKeybind(key, scanCode)) {
                    // Close the screen
                    onClose()
                    return true
                }
            }
        }

        return false
    }

    override fun keyReleased(key: Int, scanCode: Int, modifiers: Int): Boolean {
        val result = super.keyReleased(key, scanCode, modifiers)

        // Fire the process key event on our content pane
        this.contentPane.processKeyInput(
            AOTDKeyEvent(
                this.contentPane,
                key,
                scanCode,
                Char.MIN_VALUE, // Default since we don't know what char the int corresponds to
                modifiers,
                AOTDKeyEvent.KeyEventType.Release
            )
        )

        return result
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
     * Called when the mouse is pressed and released
     *
     * @param mouseX The X coordinate of the mouse
     * @param mouseY The Y coordinate of the mouse
     * @param mouseButton The mouse button clicked
     * @return True to continue processing, false otherwise
     */
    override fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        // Fire the mouse clicked event
        contentPane.processMouseInput(
            AOTDMouseEvent(
                contentPane,
                mouseX.roundToInt(),
                mouseY.roundToInt(),
                mouseButton,
                AOTDMouseEvent.EventType.Click
            )
        )

        if (dragAndDropEnabled) {
            val producer = findProducer(contentPane)
            if (producer != null) {
                dragAndDropIcon?.let { overlayPane.remove(it) }
                val icon = producer.getIcon()
                dragAndDropIcon = icon
                dragAndDropData = producer.produce()
                overlayPane.add(icon)
                icon.offset = AbsolutePosition(mouseX - icon.width/2, mouseY - icon.height/2)
                invalidate()
            }
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    /**
     * Called when the mouse is pressed and released
     *
     * @param mouseX The X coordinate of the mouse
     * @param mouseY The Y coordinate of the mouse
     * @param mouseButton The mouse button clicked
     * @return True to continue processing, false otherwise
     */
    override fun mouseReleased(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        // Fire the release event
        contentPane.processMouseInput(
            AOTDMouseEvent(
                contentPane,
                mouseX.roundToInt(),
                mouseY.roundToInt(),
                mouseButton,
                AOTDMouseEvent.EventType.Release
            )
        )

        if (dragAndDropEnabled) {
            val consumers = findConsumers(contentPane)
            for (consumer in consumers) {
                dragAndDropData?.let { consumer.consume(it) }
            }
            dragAndDropIcon?.let { overlayPane.remove(it) }
            dragAndDropIcon = null
            dragAndDropData = null
        }

        return super.mouseReleased(mouseX, mouseY, mouseButton)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, distance: Double): Boolean {
        // Fire the content pane's mouse scroll listener
        contentPane.processMouseScrollInput(AOTDMouseScrollEvent(contentPane, distance.roundToInt()))

        return super.mouseScrolled(mouseX, mouseY, distance)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, lastButtonClicked: Int, mouseXTo: Double, mouseYTo: Double): Boolean {
        contentPane.processMouseDragInput(MouseDragEvent(contentPane, mouseX.roundToInt(), mouseY.roundToInt(), lastButtonClicked))

        if (dragAndDropEnabled) {
            dragAndDropIcon?.let { it.offset = AbsolutePosition(mouseX - it.width/2, mouseY - it.height/2) }
            invalidate()
        }

        return super.mouseDragged(mouseX, mouseY, lastButtonClicked, mouseXTo, mouseYTo)
    }

    private fun findProducer(cur: AOTDGuiComponent) : DraggableProducer<*>? {
        if (cur is AOTDPane) {
            for (child in cur.getChildren()) {
                val res = findProducer(child)
                if (res != null) return res
            }
        }
        return if (cur is DraggableProducer<*> && cur.inBounds && cur.isVisible && cur.isHovered) cur else null
    }

    private fun findConsumers(cur: AOTDGuiComponent) : List<DraggableConsumer<*>> {
        val ret = ArrayList<DraggableConsumer<*>>()
        if (cur is DraggableConsumer<*> && cur.inBounds && cur.isVisible && cur.isHovered) ret.add(cur)
        if (cur is AOTDPane) {
            for (child in cur.getChildren()) {
                ret.addAll(findConsumers(child))
            }
        }
        return ret
    }

    override fun tick() {
        // Check if we should fire a mouse move event
        val mouseX = AOTDGuiUtility.getMouseXInMCCoord()
        val mouseY = AOTDGuiUtility.getMouseYInMCCoord()
        if (mouseX != prevMouseX || mouseY != prevMouseY) {
            prevMouseX = mouseX
            prevMouseY = mouseY

            // Fire the content pane's move listener
            contentPane.processMouseMoveInput(
                AOTDMouseMoveEvent(
                    contentPane,
                    prevMouseX,
                    prevMouseY,
                    AOTDMouseMoveEvent.EventType.Move
                )
            )
        }

        super.tick()
    }

    /**
     * @return True if the inventory keybind is pressed, false otherwise
     */
    internal fun isInventoryKeybind(key: Int, scanCode: Int): Boolean {
        return key == GLFW.GLFW_KEY_ESCAPE || Minecraft.getInstance().gameSettings.keyBindInventory.isActiveAndMatches(
            InputMappings.getInputByCode(
                key,
                scanCode
            )
        )
    }
}
