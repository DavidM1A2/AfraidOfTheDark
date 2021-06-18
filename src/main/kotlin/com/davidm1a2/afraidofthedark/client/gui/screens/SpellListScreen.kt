package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.customControls.SpellListItem
import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.HChainPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ListPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.VScrollBar
import com.davidm1a2.afraidofthedark.client.keybindings.KeybindingUtils
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.util.text.TranslationTextComponent

/**
 * Spell selection/list gui allows players to create spells and keybind them
 */
class SpellListScreen : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.spell_list")) {
    private val scrollPanel: ListPane
    private val btnCreateSpell: ButtonPane
    private val guiSpells = mutableListOf<SpellListItem>()
    private val spellManager = entityPlayer.getSpellManager()
    private var spellWaitingOnKeybind: SpellListItem? = null

    init {

        // Place the background panel in the center
        contentPane.padding = Spacing(0.1)

        // Create a magic mirror background image
        val mainGui = ImagePane(
            "afraidofthedark:textures/gui/spell_list/spell_list_background.png",
            ImagePane.DispMode.FIT_TO_PARENT
        )

        // Create the scroll bar
        val scrollBar = VScrollBar(
            Dimensions(0.1, 1.0),
            "afraidofthedark:textures/gui/spell_list/scroll_bar.png",
            "afraidofthedark:textures/gui/spell_list/scroll_bar_handle.png",
            "afraidofthedark:textures/gui/spell_list/scroll_bar_handle_hovered.png"
        )

        // Create the scroll panel to add spells to, position it centered on the background image
        scrollPanel = ListPane(ListPane.ExpandDirection.DOWN, scrollBar)
        scrollPanel.gravity = Gravity.CENTER
        scrollPanel.prefSize = Dimensions(0.8, 0.8)
        // Add the panel the the background and the scroll bar
        mainGui.add(scrollPanel)

        // Use an HPane to position the main gui and the scroll bar
        val layoutPane = HChainPane(HChainPane.Layout.CLOSE)
        layoutPane.add(mainGui)
        layoutPane.add(scrollBar)
        contentPane.add(layoutPane)

        // When we press a key test if we are waiting for a keybind, if so set the spell's keybind
        contentPane.addKeyListener {
            if (it.eventType == KeyEvent.KeyEventType.Press) {
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
                        // Update the GUI
                        this.invalidate()
                    }
                }
            }
        }

        // Add a button to create a new spell, center it under the scrollPanel spell entries
        btnCreateSpell = ButtonPane(
            icon = ImagePane("afraidofthedark:textures/gui/spell_list/create_spell.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/spell_list/create_spell_hovered.png"),
            prefSize = Dimensions(0.2, 0.15)
        )
        btnCreateSpell.margins = Spacing(0.01, 0.1, 0.0, 0.0)
        btnCreateSpell.setHoverText("Create a new spell")
        btnCreateSpell.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                // When we click the button create a new spell
                if (it.source.isVisible && it.source.isHovered && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    // Create a new spell
                    val spell = Spell()
                    // Add the spell
                    spellManager.addOrUpdateSpell(spell)
                    // Add the UI spell
                    addSpell(spell)
                    // Update the GUI
                    this.invalidate()
                }
            }
        }
        btnCreateSpell.addMouseMoveListener {
            if (it.eventType == MouseMoveEvent.EventType.Enter) {
                // Play the button hover when hovering the add button
                entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f)
            }
        }
        scrollPanel.add(btnCreateSpell)
        // Go over each spell the player has and add a gui spell for it
        spellManager.getSpells().forEach { addSpell(it) }
    }

    /**
     * Adds a gui spell for a given spell into the list
     *
     * @param spell The spell to create a GUI spell for
     */
    private fun addSpell(spell: Spell) {
        // Create a gui spell for this spell
        val guiSpell = SpellListItem(Dimensions(1.0, 0.20), spell)
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
        scrollPanel.remove(btnCreateSpell)
        scrollPanel.add(guiSpell)
        scrollPanel.add(btnCreateSpell)
        // Add the gui spell to the list of spells for later use
        guiSpells.add(guiSpell)
    }

    /**
     * Removes a gui spell from the list and also removes the spell from the spell manager
     *
     * @param spell The spell to remove
     */
    private fun removeSpell(spell: SpellListItem) {
        // Find the index of this gui spell
        val index = guiSpells.indexOf(spell)
        // Remove the spell at the index
        guiSpells.removeAt(index)
        // Remove the spell from the scroll panel
        scrollPanel.remove(spell)
        // Remove the spell from the spell manager
        spellManager.deleteSpell(spell.spell)
        // Redraw the screen
        invalidate()
    }

    /**
     * When the GUI is closed sync the player's spell manager
     */
    override fun removed() {
        spellManager.syncAll(entityPlayer)
        super.removed()
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
}