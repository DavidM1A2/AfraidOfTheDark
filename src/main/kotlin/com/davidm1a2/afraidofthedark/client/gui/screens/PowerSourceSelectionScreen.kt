package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.FontCache
import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LabelComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.RadialPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.SpritePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import net.minecraft.client.util.InputMappings
import net.minecraft.util.text.TranslationTextComponent
import org.lwjgl.glfw.GLFW
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class PowerSourceSelectionScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.power_source_selection")) {
    private val powerSourcePanes = mutableListOf<StackPane>()
    private val selectionIcons = mutableListOf<ImagePane>()
    private var radialMenuPane: RadialPane? = null
    private val lastSelection = entityPlayer.getBasics().selectedPowerSource
    private var selectedPowerSource: SpellPowerSource<*>? = null
    private val availablePowerSources = ModRegistries.SPELL_POWER_SOURCES
        .filter { it.shouldShowInSpellEditor(entityPlayer) }
        .sortedBy { it.prerequisiteResearch?.getName()?.string }
    private val pageCount = getPageCountForPlayer()

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

        // Highlight selection based on mouse movement
        this.contentPane.addMouseMoveListener { mouseEvent ->
            if (radialMenuPane != null) {
                val x = mouseEvent.mouseX
                val y = mouseEvent.mouseY
                val offset = curPage * RADIAL_SIZE
                // Math stuff
                val radiusSquared = x * x + y * y
                val radiusAbsoluteMin = MOUSE_DEADZONE_SIZE * radialMenuPane!!.width / 2
                val radiusMinSquared = radiusAbsoluteMin * radiusAbsoluteMin
                if (radiusSquared > radiusMinSquared) {
                    val radiusAbsoluteMax = MOUSE_BOUNDS_SIZE * radialMenuPane!!.width / 2
                    val radiusMaxSquared = radiusAbsoluteMax * radiusAbsoluteMax
                    val theta = atan2(y, x).mod(2 * PI)  // From -PI to PI
                    val sectionIndex = ((theta + (PI / RADIAL_SIZE)) / (2 * PI) * RADIAL_SIZE).mod(RADIAL_SIZE.toDouble()).toInt()
                    // Make the hovered option larger
                    powerSourcePanes.forEach { it.prefSize = Dimensions(0.13, 0.13) }
                    powerSourcePanes[sectionIndex].prefSize = Dimensions(0.15, 0.15)
                    // Set the selected power source client-side
                    if (sectionIndex in 0 until RADIAL_SIZE) {
                        selectedPowerSource = if (sectionIndex + offset in availablePowerSources.indices) availablePowerSources[sectionIndex + offset] else null
                    }
                    // Redraw the pane, since elements have changed
                    this.contentPane.invalidate()
                    // Bound Cursor
                    if (radiusSquared > radiusMaxSquared) {
                        GLFW.glfwSetCursorPos(minecraft!!.window.window, cos(theta) * radiusAbsoluteMax, sin(theta) * radiusAbsoluteMax)
                    }
                }
            }
        }

        // Right click changes page
        this.contentPane.addMouseListener {
            if (it.clickedButton == MouseEvent.RIGHT_MOUSE_BUTTON && it.eventType == MouseEvent.EventType.Click) {
                curPage = (curPage + 1).mod(pageCount)
                populateMenuWithOffset(RADIAL_SIZE * curPage)
            }
        }

        curPage = curPage.coerceIn(0, pageCount - 1)
        populateMenuWithOffset(RADIAL_SIZE * curPage)
    }

    private fun getPageCountForPlayer(): Int {
        return (availablePowerSources.size - 1) / RADIAL_SIZE + 1
    }

    private fun populateMenuWithOffset(offset: Int) {
        // Clear the pane
        this.contentPane.getChildren().forEach { contentPane.remove(it) }

        // Set up the main GUI elements
        this.radialMenuPane = RadialPane()
        radialMenuPane!!.gravity = Gravity.CENTER
        this.contentPane.add(radialMenuPane!!)

        // Add the page count
        val pageLabel = LabelComponent(FontCache.getOrCreate(36f), Dimensions(0.5, 0.5), Gravity.CENTER)
        pageLabel.textAlignment = TextAlignment.ALIGN_CENTER
        pageLabel.text = "Page ${curPage + 1}/$pageCount (RMB)"
        this.contentPane.add(pageLabel)

        // Fill the radial menu with power sources
        powerSourcePanes.clear()
        selectionIcons.clear()
        for (i in 0 until RADIAL_SIZE) {
            val liquidSprite = SpritePane("afraidofthedark:textures/gui/power_source_selector/liquid_spritesheet.png", 4, 4, displayMode = ImagePane.DispMode.STRETCH)
            liquidSprite.setFrame(12)
            val orbImage = ImagePane("afraidofthedark:textures/gui/power_source_selector/orb_front_colored.png", displayMode = ImagePane.DispMode.STRETCH)
            val buttonPane = StackPane(gravity = Gravity.CENTER, prefSize = Dimensions(0.13, 0.13), offset = Position(0.5, i.toDouble() / RADIAL_SIZE))
            val selectorImage = ImagePane("afraidofthedark:textures/gui/power_source_selector/orb_selector.png", displayMode = ImagePane.DispMode.STRETCH)
            selectorImage.isVisible = false
            buttonPane.add(liquidSprite)
            selectionIcons.add(selectorImage)
            radialMenuPane!!.add(buttonPane)
            powerSourcePanes.add(buttonPane)
            // Add number descriptor
            val numberPane = LabelComponent(FontCache.getOrCreate(28f), Dimensions(1.0, 0.3))
            numberPane.gravity = Gravity.TOP_CENTER
            numberPane.offset = Position(0.0, 0.95)
            numberPane.text = "N/A"
            numberPane.textAlignment = TextAlignment.ALIGN_CENTER
            // Only fill out the gui while there are still available power sources
            if (i + offset < availablePowerSources.size) {
                val ssIcon = ImagePane(availablePowerSources[i + offset].icon)
                ssIcon.margins = Spacing(0.4)
                buttonPane.add(ssIcon)
                val castEnvironment = availablePowerSources[i + offset].computeCastEnvironment(entityPlayer)
                numberPane.text = if (castEnvironment.vitaeMaximum == Double.POSITIVE_INFINITY) {
                    "%.1f".format(castEnvironment.vitaeAvailable)
                } else {
                    "%.1f/%.1f".format(castEnvironment.vitaeAvailable, castEnvironment.vitaeMaximum)
                }
                if (castEnvironment.vitaeMaximum == 0.0 || castEnvironment.vitaeAvailable == 0.0) { // Zero Case
                    liquidSprite.setFrame(12)
                } else if (castEnvironment.vitaeAvailable == Double.POSITIVE_INFINITY || castEnvironment.vitaeMaximum == Double.POSITIVE_INFINITY || castEnvironment.vitaeAvailable / castEnvironment.vitaeMaximum > 0.75) {  // Full case
                    liquidSprite.setAnimation(listOf(0, 1, 2, 3), SpritePane.AnimMode.LOOP, 4.0)
                } else if (castEnvironment.vitaeAvailable / castEnvironment.vitaeMaximum > 0.5) {
                    liquidSprite.setAnimation(listOf(4, 5, 6, 7), SpritePane.AnimMode.LOOP, 4.0)
                } else if (castEnvironment.vitaeAvailable / castEnvironment.vitaeMaximum > 0.0) {
                    liquidSprite.setAnimation(listOf(8, 9, 10, 11), SpritePane.AnimMode.LOOP, 4.0)
                }
            }
            buttonPane.add(orbImage)
            buttonPane.add(selectorImage)
            buttonPane.add(numberPane)
        }

        // Start with the current power source selected
        val selectedIndex = availablePowerSources.indexOf(lastSelection)
        if (selectedIndex in offset until RADIAL_SIZE + offset) {
            selectionIcons[selectedIndex - offset].isVisible = true
        }

        // Draw new menu
        this.contentPane.invalidate()
    }

    override fun init() {
        InputMappings.grabOrReleaseMouse(minecraft!!.window.window, GLFW.GLFW_CURSOR_DISABLED, 0.0, 0.0)
        super.init()
    }

    override fun drawGradientBackground(): Boolean {
        return false
    }

    override fun inventoryToCloseGuiScreen(): Boolean {
        return true
    }

    override fun onClose() {
        entityPlayer.getBasics().selectedPowerSource = selectedPowerSource ?: lastSelection
        entityPlayer.getBasics().syncSelectedPowerSource(entityPlayer)
        InputMappings.grabOrReleaseMouse(minecraft!!.window.window, GLFW.GLFW_CURSOR_NORMAL, AOTDGuiUtility.getWindowWidthInMCCoords() / 2.0, AOTDGuiUtility.getWindowHeightInMCCoords() / 2.0)
        super.onClose()
    }

    companion object {
        const val RADIAL_SIZE = 8
        const val MOUSE_BOUNDS_SIZE = 0.5
        const val MOUSE_DEADZONE_SIZE = 0.1

        var curPage = 0
    }
}