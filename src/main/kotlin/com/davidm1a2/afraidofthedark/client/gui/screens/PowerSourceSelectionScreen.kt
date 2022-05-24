package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.RadialPane
import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.client.util.InputMappings
import net.minecraft.util.text.TranslationTextComponent
import org.lwjgl.glfw.GLFW
import java.awt.Color
import kotlin.math.PI
import kotlin.math.atan2

class PowerSourceSelectionScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.power_source_selection")) {
    init {
        val mouseQueue = ArrayDeque<Pair<Double, Double>>(MOUSE_QUEUE_SIZE)

        // Close the screen when TOGGLE_POWER_SOURCE_SELECTOR is released. It must be pressed to open this screen
        this.contentPane.addKeyListener {
            if (it.eventType == KeyEvent.KeyEventType.Release && it.key == ModKeybindings.POWER_SOURCE_SELECTOR.key.value) {
                onClose()
            }
        }
        // Left click also closes the gui
        this.contentPane.addMouseListener {
            if (it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                onClose()
            }
        }

        val radialMenuPane = RadialPane()
        radialMenuPane.gravity = Gravity.CENTER
        this.contentPane.add(radialMenuPane)

        val availablePowerSources = ModRegistries.SPELL_POWER_SOURCES.filter { it.shouldShowInSpellEditor(entityPlayer) }
        val powerSourceButtons = mutableListOf<ButtonPane>()
        for (i in 0 until RADIAL_SIZE) {
            val img = ImagePane("afraidofthedark:textures/gui/arcane_journal_tech_tree/research_background.png", ImagePane.DispMode.FIT_TO_PARENT)
            val buttonPane = ButtonPane(img, gravity = Gravity.CENTER, prefSize = Dimensions(0.1, 0.1), offset = Position(0.5, i.toDouble() / RADIAL_SIZE))
            radialMenuPane.add(buttonPane)
            powerSourceButtons.add(buttonPane)

            if (i < availablePowerSources.size) buttonPane.add(ImagePane(availablePowerSources[i].icon, ImagePane.DispMode.FIT_TO_PARENT))
        }

        // Highlight selection based on mouse movement
        this.contentPane.addMouseMoveListener { mouseEvent ->
            if (mouseQueue.size >= MOUSE_QUEUE_SIZE) {
                mouseQueue.removeLast()
            }
            mouseQueue.addFirst(Pair(mouseEvent.mouseX.toDouble(), mouseEvent.mouseY.toDouble()))
            var sumX = 0.0
            var sumY = 0.0
            for (p in mouseQueue) {
                sumX += p.first
                sumY += p.second
            }
            val theta = (-atan2(sumY, sumX) + PI / 2).mod(2 * PI)
            val sectionIndex = ((theta + PI / RADIAL_SIZE) / (2 * PI) * RADIAL_SIZE).mod(RADIAL_SIZE.toDouble()).toInt()
            powerSourceButtons.forEach { it.color = Color(255, 255, 255) }
            powerSourceButtons[sectionIndex].color = Color(200, 200, 200)
            this.contentPane.invalidate()
            GLFW.glfwSetCursorPos(minecraft!!.window.window, 0.0, 0.0)
        }
    }

    override fun init() {
        InputMappings.grabOrReleaseMouse(minecraft!!.window.window, GLFW.GLFW_CURSOR_DISABLED, 0.0, 0.0)
        super.init()
    }

    override fun drawGradientBackground(): Boolean {
        return true
    }

    override fun inventoryToCloseGuiScreen(): Boolean {
        return true
    }

    companion object {
        const val RADIAL_SIZE = 8
        const val MOUSE_QUEUE_SIZE = 5
    }
}