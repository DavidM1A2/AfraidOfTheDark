package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.client.util.InputMappings
import net.minecraft.util.text.TranslationTextComponent
import org.lwjgl.glfw.GLFW
import java.awt.Color
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

        val radialMenuPane = RadialPane()
        radialMenuPane.gravity = Gravity.CENTER
        this.contentPane.add(radialMenuPane)

        val availablePowerSources = ModRegistries.SPELL_POWER_SOURCES.filter { it.shouldShowInSpellEditor(entityPlayer) }
        val powerSourcePanes = mutableListOf<StackPane>()
        for (i in 0 until RADIAL_SIZE) {
            val liquidSprite = SpritePane("afraidofthedark:textures/gui/power_source_selector/liquid_spritesheet.png", 4, 4, displayMode = ImagePane.DispMode.FIT_TO_PARENT)
            liquidSprite.setFrame(12)
            val orbImage = ImagePane("afraidofthedark:textures/gui/power_source_selector/orb_front_colored.png", displayMode = ImagePane.DispMode.FIT_TO_PARENT)
            val buttonPane = StackPane(gravity = Gravity.CENTER, prefSize = Dimensions(0.13, 0.13), offset = Position(0.5, i.toDouble() / RADIAL_SIZE))
            buttonPane.add(liquidSprite)
            buttonPane.add(orbImage)
            radialMenuPane.add(buttonPane)
            powerSourcePanes.add(buttonPane)

            if (i < availablePowerSources.size) {
                val ssIcon = ImagePane(availablePowerSources[i].icon)
                ssIcon.margins = Spacing(0.25)
                buttonPane.add(ssIcon)
                val castEnvironment = availablePowerSources[i].computeCastEnvironment(entityPlayer)
                if (castEnvironment.vitaeMaximum == 0.0 || castEnvironment.vitaeAvailable == 0.0) { // Zero Case
                    liquidSprite.setFrame(12)
                } else if (castEnvironment.vitaeAvailable == Double.POSITIVE_INFINITY || castEnvironment.vitaeAvailable/castEnvironment.vitaeMaximum > 0.75) {  // Full case
                    liquidSprite.setAnimation(listOf(0, 1, 2, 3), SpritePane.AnimMode.LOOP, 4.0)
                } else if (castEnvironment.vitaeAvailable/castEnvironment.vitaeMaximum > 0.5) {
                    liquidSprite.setAnimation(listOf(4, 5, 6, 7), SpritePane.AnimMode.LOOP, 4.0)
                } else if (castEnvironment.vitaeAvailable/castEnvironment.vitaeMaximum > 0.0) {
                    liquidSprite.setAnimation(listOf(8, 9, 10, 11), SpritePane.AnimMode.LOOP, 4.0)
                }
            }
        }

        // Highlight selection based on mouse movement
        this.contentPane.addMouseMoveListener { mouseEvent ->
            val x = mouseEvent.mouseX
            val y = mouseEvent.mouseY
            val radiusSquared = x*x + y*y
            val radiusAbsoluteMax = MOUSE_BOUNDS_SIZE*radialMenuPane.width/2
            val radiusMaxSquared = radiusAbsoluteMax*radiusAbsoluteMax
            val theta = atan2(y.toDouble(), x.toDouble()).mod(2*PI)  // From -PI to PI
            val sectionIndex = ((theta + (PI/RADIAL_SIZE)) / (2*PI) * RADIAL_SIZE).mod(RADIAL_SIZE.toDouble()).toInt()
            powerSourcePanes.forEach { it.prefSize = Dimensions(0.13, 0.13) }
            powerSourcePanes[sectionIndex].prefSize = Dimensions(0.15, 0.15)
            this.contentPane.invalidate()
            if (radiusSquared > radiusMaxSquared) {
                GLFW.glfwSetCursorPos(minecraft!!.window.window, cos(theta)*radiusAbsoluteMax, sin(theta)*radiusAbsoluteMax)
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

    companion object {
        const val RADIAL_SIZE = 8
        const val MOUSE_BOUNDS_SIZE = 0.5
    }
}