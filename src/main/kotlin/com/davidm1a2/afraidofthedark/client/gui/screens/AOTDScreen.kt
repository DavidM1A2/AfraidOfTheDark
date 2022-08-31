package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableConsumer
import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseDragEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseScrollEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.OverlayPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
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
        get() = Minecraft.getInstance().player!!

    val contentPane = StackPane(AOTDGuiUtility.getWindowSizeInMCCoords())
    private val dndPane = OverlayPane(null)
    private var dragAndDropIcon: ImagePane? = null
    private var dragAndDropData: Any? = null
    private var isScreenValid = true

    /**
     * Called to initialize the GUI screen
     */
    override fun init() {
        super.init()
        // Clear all buttons on the screen
        this.buttons.clear()
        // Draw the pane on the next draw cycle
        this.invalidate()
    }

    open fun invalidate() {
        this.isScreenValid = false
    }

    open fun update() {
        val windowSize = AOTDGuiUtility.getWindowSizeInMCCoords()
        // Record dimensions so we can tell when they change
        this.contentPane.prefSize = windowSize
        // Fit panes to the screen
        this.contentPane.negotiateDimensions(windowSize.width, windowSize.height)
        // Resize any children to fit the new dimensions
        this.contentPane.update()
        // Check that the overlay pane is still attached
        if (contentPane.getChildren().contains(dndPane).not()) contentPane.add(dndPane)
    }

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        // Enable blend so we can draw opacity
        RenderSystem.enableBlend()
        // If we want a gradient background draw that background
        if (this.drawGradientBackground()) this.renderBackground(matrixStack)
        // Trigger an update if the screen has changed size
        if (contentPane.prefSize != AOTDGuiUtility.getWindowSizeInMCCoords()) isScreenValid = false
        // Perform an update if necessary
        if (isScreenValid) this.update()
        // Draw the content pane
        this.contentPane.draw(matrixStack)
        // Draw the overlay on top of the content pane
        this.contentPane.drawOverlay(matrixStack)
        // Call the super method
        super.render(matrixStack, mouseX, mouseY, partialTicks)
        // Disable blend now that we drew the UI
        RenderSystem.disableBlend()
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
            KeyEvent(
                this.contentPane,
                Int.MIN_VALUE, // Default since we don't know what int the char corresponds to
                Int.MIN_VALUE, // Default since we don't know what int the char corresponds to
                char,
                modifiers,
                KeyEvent.KeyEventType.Type
            )
        )

        return super.charTyped(char, modifiers)
    }

    override fun keyPressed(key: Int, scanCode: Int, modifiers: Int): Boolean {
        // Fire the process key event on our content pane
        this.contentPane.processKeyInput(
            KeyEvent(
                this.contentPane,
                key,
                scanCode,
                Char.MIN_VALUE, // Default since we don't know what char the int corresponds to
                modifiers,
                KeyEvent.KeyEventType.Press
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
            KeyEvent(
                this.contentPane,
                key,
                scanCode,
                Char.MIN_VALUE, // Default since we don't know what char the int corresponds to
                modifiers,
                KeyEvent.KeyEventType.Release
            )
        )

        return result
    }

    /**
     * @return True if the inventory key closes the screen, false otherwise
     */
    abstract fun inventoryToCloseGuiScreen(): Boolean

    /**
     * Called when the mouse is pressed and released
     *
     * @param mouseX The X coordinate of the mouse
     * @param mouseY The Y coordinate of the mouse
     * @param mouseButton The mouse button clicked
     * @return True to continue processing, false otherwise
     */
    override fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {

        val producer = if (dragAndDropEnabled) findProducer(contentPane) else null

        if (producer != null && mouseButton == 0) {
            dragAndDropIcon?.let { dndPane.remove(it) }
            val icon = producer.getIcon()
            dragAndDropIcon = icon
            dragAndDropData = producer.produce()
            dndPane.add(icon)
            dndPane.invalidate()
            icon.offset = Position(mouseX - icon.width / 2, mouseY - icon.height / 2, false)
            dndPane.invalidate()
        } else {
            // Fire the mouse clicked event
            contentPane.processMouseInput(
                MouseEvent(
                    contentPane,
                    mouseX.roundToInt(),
                    mouseY.roundToInt(),
                    mouseButton,
                    MouseEvent.EventType.Click
                )
            )
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

        val consumers = if (dragAndDropEnabled) findConsumers(contentPane) else emptyList()

        if (consumers.isNotEmpty()) {
            for (consumer in consumers) {
                dragAndDropData?.let { consumer.consume(it) }
            }
        } else {
            // Fire the release event
            contentPane.processMouseInput(
                MouseEvent(
                    contentPane,
                    mouseX.roundToInt(),
                    mouseY.roundToInt(),
                    mouseButton,
                    MouseEvent.EventType.Release
                )
            )
        }

        if (dragAndDropEnabled) {
            dragAndDropIcon?.let { dndPane.remove(it) }
            dragAndDropIcon = null
            dragAndDropData = null
        }

        return super.mouseReleased(mouseX, mouseY, mouseButton)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, distance: Double): Boolean {
        // Fire the content pane's mouse scroll listener
        contentPane.processMouseScrollInput(MouseScrollEvent(contentPane, distance.roundToInt()))

        return super.mouseScrolled(mouseX, mouseY, distance)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, lastButtonClicked: Int, mouseXTo: Double, mouseYTo: Double): Boolean {

        if (dragAndDropEnabled && dragAndDropIcon != null) {
            dragAndDropIcon?.let { it.offset = Position(mouseX - it.width / 2, mouseY - it.height / 2, false) }
            dndPane.invalidate()
        } else {
            contentPane.processMouseDragInput(MouseDragEvent(contentPane, mouseX.roundToInt(), mouseY.roundToInt(), lastButtonClicked))
        }

        return super.mouseDragged(mouseX, mouseY, lastButtonClicked, mouseXTo, mouseYTo)
    }

    private fun findProducer(cur: AOTDGuiComponent): DraggableProducer<*>? {
        if (cur is AOTDPane) {
            for (child in cur.getChildren()) {
                val res = findProducer(child)
                if (res != null) return res
            }
        }
        return if (cur is DraggableProducer<*> && cur.inBounds && cur.isVisible && cur.isHovered) cur else null
    }

    private fun findConsumers(cur: AOTDGuiComponent): List<DraggableConsumer<*>> {
        val ret = ArrayList<DraggableConsumer<*>>()
        if (cur is DraggableConsumer<*> && cur.inBounds && cur.isVisible && cur.isHovered) ret.add(cur)
        if (cur is AOTDPane) {
            for (child in cur.getChildren()) {
                ret.addAll(findConsumers(child))
            }
        }
        return ret
    }

    override fun mouseMoved(probablyX: Double, probablyY: Double) {
        contentPane.processMouseMoveInput(
            MouseMoveEvent(
                contentPane,
                probablyX,
                probablyY,
                MouseMoveEvent.EventType.Move
            )
        )
        super.mouseMoved(probablyX, probablyY)
    }

    /**
     * @return True if the inventory keybind is pressed, false otherwise
     */
    internal fun isInventoryKeybind(key: Int, scanCode: Int): Boolean {
        return key == GLFW.GLFW_KEY_ESCAPE || Minecraft.getInstance().options.keyInventory.isActiveAndMatches(
            InputMappings.getKey(
                key,
                scanCode
            )
        )
    }
}
