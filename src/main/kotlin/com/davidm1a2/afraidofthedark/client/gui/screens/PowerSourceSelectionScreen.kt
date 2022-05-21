package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
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
    }

    override fun drawGradientBackground(): Boolean {
        return true
    }

    override fun inventoryToCloseGuiScreen(): Boolean {
        return true
    }
}