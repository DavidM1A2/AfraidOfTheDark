package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiGravity
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDScreen
import com.davidm1a2.afraidofthedark.client.gui.base.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.base.RelativeDimensions
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpell
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.keybindings.KeybindingUtils
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.util.text.TranslationTextComponent
import kotlin.math.round

/**
 * Spell selection/list gui allows players to create spells and keybind them
 *
 * @constructor initializes the gui elements
 * @property scrollPanel The scroll panel that holds the spell list
 * @property btnCreateSpell The button used to create more spells
 * @property guiSpells A list of spells to be shown
 * @property spellManager Cache the player's spell manager
 * @property spellWaitingOnKeybind The spell we are currently waiting on a keybind to be pressed or not
 */
class SpellListScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.spell_list")) {
    private val scrollPanel: AOTDGuiScrollPanel
    private val btnCreateSpell: AOTDGuiButton
    private val guiSpells = mutableListOf<AOTDGuiSpell>()
    private val spellManager = entityPlayer.getSpellManager()
    private var spellWaitingOnKeybind: AOTDGuiSpell? = null

    init {

        // Place the background panel in the center
        val backgroundPanel = StackPane(Dimensions(GUI_WIDTH.toDouble(), GUI_HEIGHT.toDouble()), gravity = AOTDGuiGravity.CENTER, scissorEnabled = false)

        // Create a magic mirror background image
        val mirrorBackgroundImage = ImagePane(
            "afraidofthedark:textures/gui/spell_list/spell_list_background.png",
            ImagePane.DispMode.FIT_TO_PARENT
        )
        backgroundPanel.add(mirrorBackgroundImage)

        // Create the scroll bar
        val scrollBar = AOTDGuiScrollBar(
            RelativeDimensions(0.1, 1.0),
            "afraidofthedark:textures/gui/spell_list/scroll_bar.png",
            "afraidofthedark:textures/gui/spell_list/scroll_bar_handle.png",
            "afraidofthedark:textures/gui/spell_list/scroll_bar_handle_hovered.png"
        )

        // Create the scroll panel to add spells to, position it centered on the background image
        scrollPanel = AOTDGuiScrollPanel(175.0, 238.0, true, scrollBar)
        // Start with a max offset of 0
        scrollPanel.maximumOffset = 0
        // Add the panel the the background and the scroll bar
        backgroundPanel.add(scrollPanel)
        backgroundPanel.add(scrollBar)

        // When we press a key test if we are waiting for a keybind, if so set the spell's keybind
        contentPane.addKeyListener {
            if (it.eventType == AOTDKeyEvent.KeyEventType.Press) {
                // If we're waiting on a keybind assign the keybind and update each spell
                if (spellWaitingOnKeybind != null) {
                    // Test if the key down is bindable
                    if (KeybindingUtils.isKeyBindable(it.key)) {
                        // Grab the keybind being held
                        val keybind = KeybindingUtils.getCurrentlyHeldKeybind(it.key, it.scanCode)
                        // Keybind the spell
                        spellManager.keybindSpell(keybind, spellWaitingOnKeybind!!.spell)
                        // Update all gui spell's labels
                        guiSpells.forEach { guiSpell -> guiSpell.refreshLabels() }
                        // We're no longer waiting on a keybind
                        spellWaitingOnKeybind = null
                    }
                }
            }
        }

        // Add a button to create a new spell, center it under the scrollPanel spell entries
        btnCreateSpell = AOTDGuiButton(
            prefSize = Dimensions(26.0, 26.0),
            icon = ImagePane("afraidofthedark:textures/gui/spell_list/create_spell.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/spell_list/create_spell_hovered.png")
        )
        btnCreateSpell.setHoverText("Create a new spell")
        btnCreateSpell.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                // When we click the button create a new spell
                if (it.source.isVisible && it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    // Create a new spell
                    val spell = Spell()
                    // Add the spell
                    spellManager.addOrUpdateSpell(spell)
                    // Add the UI spell
                    addSpell(spell)
                }
            }
        }
        btnCreateSpell.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                // Play the button hover when hovering the add button
                entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f)
            }
        }
        scrollPanel.add(btnCreateSpell)
        // Go over each spell the player has and add a gui spell for it
        spellManager.getSpells().forEach { addSpell(it) }

        // When we scroll we want to move the content pane up or down
        scrollPanel.addMouseScrollListener {
            // Only scroll the pane if it's hovered
            if (scrollPanel.isHovered) {
                // Only move the handle if scrollDistance is non-zero
                if (it.scrollDistance != 0) {
                    // Move the scroll bar by the distance amount
                    scrollBar.moveHandle(
                        round(-it.scrollDistance / scrollPanel.maximumOffset.toFloat() * SCROLL_SPEED).toInt(),
                        true
                    )
                }
            }
        }

        contentPane.add(backgroundPanel)
    }

    /**
     * Adds a gui spell for a given spell into the list
     *
     * @param spell The spell to create a GUI spell for
     */
    private fun addSpell(spell: Spell) {
        // Create a gui spell for this spell
        val guiSpell = AOTDGuiSpell(0, guiSpells.size * DISTANCE_BETWEEN_SPELLS, 175, 40, spell)
        // When delete is pressed remove the GUI spell
        guiSpell.setDeleteCallback { removeSpell(guiSpell) }
        // When keybind is pressed update our variable to let us know we're waiting on a keybind, or clear the keybind
        guiSpell.setKeybindCallback {
            // No keybind exists, expect one now
            if (spellManager.getKeybindingForSpell(spell) == null) {
                spellWaitingOnKeybind = guiSpell
            } else {
                spellManager.unbindSpell(spell)
                guiSpell.refreshLabels()
            }
        }
        // Add the gui spell to the panel
        scrollPanel.add(guiSpell)
        // Add the gui spell to the list of spells for later use
        guiSpells.add(guiSpell)
        // Move our create spell button down
        btnCreateSpell.y += DISTANCE_BETWEEN_SPELLS
        // Update our scroll panel offset
        refreshScrollPanelOffset()
    }

    /**
     * Removes a gui spell from the list and also removes the spell from the spell manager
     *
     * @param spell The spell to remove
     */
    private fun removeSpell(spell: AOTDGuiSpell) {
        // Find the index of this gui spell
        val index = guiSpells.indexOf(spell)
        // Remove the spell at the index
        guiSpells.removeAt(index)
        // Remove the spell from the scroll panel
        scrollPanel.remove(spell)
        // Remove the spell from the spell manager
        spellManager.deleteSpell(spell.spell)
        // Go over all spells after this one and move them up one slot
        for (i in index until guiSpells.size) {
            val guiSpell = guiSpells[i]
            guiSpell.y -= DISTANCE_BETWEEN_SPELLS
        }
        // Move our create spell button up
        btnCreateSpell.y -= DISTANCE_BETWEEN_SPELLS
        // Update our scroll panel offset
        refreshScrollPanelOffset()
    }

    /**
     * Updates the scroll panel offset based on the gui spells present
     */
    private fun refreshScrollPanelOffset() {
        // If there's more than 4 spells update the maximum offset so we can scroll over spells
        if (guiSpells.size > 4) {
            scrollPanel.maximumOffset = (guiSpells.size - 4) * DISTANCE_BETWEEN_SPELLS
        } else {
            scrollPanel.maximumOffset = 0
        }
    }

    /**
     * When the GUI is closed sync the player's spell manager
     */
    override fun onClose() {
        spellManager.syncAll(entityPlayer)
        super.onClose()
    }

    /**
     * @return True, the inventory key closes the screen
     */
    override fun inventoryToCloseGuiScreen(): Boolean {
        return true
    }

    /**
     * @return True, the screen should have a gradient background
     */
    override fun drawGradientBackground(): Boolean {
        return true
    }

    companion object {
        // The distance between spell entries in pixels
        private const val DISTANCE_BETWEEN_SPELLS = 45

        // Constants for scroll bar width and padding
        private const val SCROLL_BAR_WIDTH = 15
        private const val SCROLL_BAR_HORIZONTAL_PADDING = 5

        // The gui will be 256x256
        private const val GUI_WIDTH = 256
        private const val GUI_HEIGHT = 256

        // Scroll speed
        private const val SCROLL_SPEED = 1000
    }
}