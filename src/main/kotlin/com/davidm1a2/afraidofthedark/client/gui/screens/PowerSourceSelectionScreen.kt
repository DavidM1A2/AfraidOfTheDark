package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.client.util.InputMappings
import net.minecraft.util.text.TranslationTextComponent
import org.lwjgl.glfw.GLFW
import kotlin.math.*

class PowerSourceSelectionScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.power_source_selection")) {
    init {
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

        // Set up the main GUI elements
        val radialMenuPane = RadialPane()
        radialMenuPane.gravity = Gravity.CENTER
        this.contentPane.add(radialMenuPane)

        // Fill the radial menu with power sources
        val availablePowerSources = ModRegistries.SPELL_POWER_SOURCES.filter { it.shouldShowInSpellEditor(entityPlayer) }
        val powerSourcePanes = mutableListOf<StackPane>()
        val selectionIcons = mutableListOf<ImagePane>()
        for (i in 0 until RADIAL_SIZE) {
            val liquidSprite = SpritePane("afraidofthedark:textures/gui/power_source_selector/liquid_spritesheet.png", 4, 4, displayMode = ImagePane.DispMode.STRETCH)
            liquidSprite.setFrame(12)
            val orbImage = ImagePane("afraidofthedark:textures/gui/power_source_selector/orb_front_colored.png", displayMode = ImagePane.DispMode.STRETCH)
            val buttonPane = StackPane(gravity = Gravity.CENTER, prefSize = Dimensions(0.13, 0.13), offset = Position(0.5, i.toDouble() / RADIAL_SIZE))
            val selectorImage = ImagePane("afraidofthedark:textures/gui/power_source_selector/orb_selector.png", displayMode = ImagePane.DispMode.STRETCH)
            selectorImage.isVisible = false
            buttonPane.add(liquidSprite)
            buttonPane.add(orbImage)
            buttonPane.add(selectorImage)
            selectionIcons.add(selectorImage)
            radialMenuPane.add(buttonPane)
            powerSourcePanes.add(buttonPane)
            // Only fill out the gui while there are still available power sources
            if (i < availablePowerSources.size) {
                val ssIcon = ImagePane(availablePowerSources[i].icon)
                ssIcon.margins = Spacing(0.25)
                buttonPane.add(ssIcon)
                val castEnvironment = availablePowerSources[i].computeCastEnvironment(entityPlayer)
                if (castEnvironment.vitaeMaximum == 0.0 || castEnvironment.vitaeAvailable == 0.0) { // Zero Case
                    liquidSprite.setFrame(12)
                } else if (castEnvironment.vitaeAvailable == Double.POSITIVE_INFINITY || castEnvironment.vitaeMaximum == Double.POSITIVE_INFINITY || castEnvironment.vitaeAvailable/castEnvironment.vitaeMaximum > 0.75) {  // Full case
                    liquidSprite.setAnimation(listOf(0, 1, 2, 3), SpritePane.AnimMode.LOOP, 4.0)
                } else if (castEnvironment.vitaeAvailable/castEnvironment.vitaeMaximum > 0.5) {
                    liquidSprite.setAnimation(listOf(4, 5, 6, 7), SpritePane.AnimMode.LOOP, 4.0)
                } else if (castEnvironment.vitaeAvailable/castEnvironment.vitaeMaximum > 0.0) {
                    liquidSprite.setAnimation(listOf(8, 9, 10, 11), SpritePane.AnimMode.LOOP, 4.0)
                }
            }
        }

        // Start with the current power source selected
        val selectedIndex = availablePowerSources.indexOf(entityPlayer.getBasics().selectedPowerSource)
        if (selectedIndex in 0 until RADIAL_SIZE) {
            selectionIcons[selectedIndex].isVisible = true
            println(entityPlayer.getBasics().selectedPowerSource)
        }

        // Highlight selection based on mouse movement
        this.contentPane.addMouseMoveListener { mouseEvent ->
            val x = mouseEvent.mouseX
            val y = mouseEvent.mouseY
            println("$x, $y")
            // Math stuff
            val radiusSquared = x*x + y*y
            val radiusAbsoluteMin = MOUSE_DEADZONE_SIZE*radialMenuPane.width/2
            val radiusMinSquared = radiusAbsoluteMin*radiusAbsoluteMin
            if (radiusSquared > radiusMinSquared) {
                val radiusAbsoluteMax = MOUSE_BOUNDS_SIZE*radialMenuPane.width/2
                val radiusMaxSquared = radiusAbsoluteMax*radiusAbsoluteMax
                val theta = atan2(y, x).mod(2*PI)  // From -PI to PI
                val sectionIndex = ((theta + (PI/RADIAL_SIZE)) / (2*PI) * RADIAL_SIZE).mod(RADIAL_SIZE.toDouble()).toInt()
                // Make the hovered option larger
                powerSourcePanes.forEach { it.prefSize = Dimensions(0.13, 0.13) }
                powerSourcePanes[sectionIndex].prefSize = Dimensions(0.15, 0.15)
                // Set the selected power source client-side
                if (sectionIndex in 0 until RADIAL_SIZE) {
                    if (sectionIndex in availablePowerSources.indices) {
                        entityPlayer.getBasics().selectedPowerSource = availablePowerSources[sectionIndex]
                        selectionIcons.forEach { it.isVisible = false }
                        selectionIcons[sectionIndex].isVisible = true
                    }
                }
                // Redraw the pane, since elements have changed
                this.contentPane.invalidate()
                // Bound Cursor
                if (radiusSquared > radiusMaxSquared) {
                    GLFW.glfwSetCursorPos(minecraft!!.window.window, cos(theta)*radiusAbsoluteMax, sin(theta)*radiusAbsoluteMax)
                }
            }
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

    override fun onClose() {
        entityPlayer.getBasics().syncSelectedPowerSource(entityPlayer)
        InputMappings.grabOrReleaseMouse(minecraft!!.window.window, GLFW.GLFW_CURSOR_NORMAL, AOTDGuiUtility.getWindowWidthInMCCoords()/2.0, AOTDGuiUtility.getWindowHeightInMCCoords()/2.0)
        super.onClose()
    }

    companion object {
        const val RADIAL_SIZE = 8
        const val MOUSE_BOUNDS_SIZE = 0.5
        const val MOUSE_DEADZONE_SIZE = 0.1
    }
}