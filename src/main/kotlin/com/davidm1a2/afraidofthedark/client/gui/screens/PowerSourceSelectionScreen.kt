package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.RadialPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.RatioPane
import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings
import net.minecraft.util.text.TranslationTextComponent

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

        for (i in 0..RADIAL_SIZE) {
            val temp = RatioPane(1, 1)
            temp.gravity = Gravity.CENTER
            temp.offset = Position(0.5, i.toDouble()/RADIAL_SIZE)
            temp.prefSize = Dimensions(0.1, 0.1)
            temp.add(ImagePane("afraidofthedark:textures/gui/arcane_journal_tech_tree/research_background.png", ImagePane.DispMode.FIT_TO_PARENT))
            radialMenuPane.add(temp)
        }
    }

    override fun drawGradientBackground(): Boolean {
        return true
    }

    override fun inventoryToCloseGuiScreen(): Boolean {
        return true
    }

    companion object {
        val RADIAL_SIZE = 8
    }
}