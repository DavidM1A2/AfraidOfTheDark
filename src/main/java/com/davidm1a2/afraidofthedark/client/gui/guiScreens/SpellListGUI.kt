package com.davidm1a2.afraidofthedark.client.gui.guiScreens

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiScreen
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpell
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.keybindings.KeybindingUtils
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.spell.Spell
import java.util.*

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
class SpellListGUI : AOTDGuiScreen()
{
    private val scrollPanel: AOTDGuiScrollPanel
    private val btnCreateSpell: AOTDGuiButton
    private val guiSpells: MutableList<AOTDGuiSpell> = ArrayList()
    private val spellManager = entityPlayer.getCapability(ModCapabilities.PLAYER_SPELL_MANAGER, null)!!
    private var spellWaitingOnKeybind: AOTDGuiSpell? = null

    init
    {
        // Calculate the x,y base position of the UI
        val xPosSpellList = (Constants.GUI_WIDTH - GUI_WIDTH) / 2
        val yPosSpellList = (Constants.GUI_HEIGHT - GUI_HEIGHT) / 2

        // Place the background panel in the center
        val backgroundPanel = AOTDGuiPanel(xPosSpellList, yPosSpellList, GUI_WIDTH, GUI_HEIGHT, false)

        // Create a magic mirror background image
        val mirrorBackgroundImage = AOTDGuiImage(
            0,
            0,
            GUI_WIDTH - SCROLL_BAR_WIDTH - SCROLL_BAR_HORIZONTAL_PADDING * 2,
            GUI_HEIGHT,
            "afraidofthedark:textures/gui/spell_list/spell_list_background.png"
        )
        backgroundPanel.add(mirrorBackgroundImage)

        // Compute the scroll bar's x and y position
        val scrollBarX = mirrorBackgroundImage.getWidth() + SCROLL_BAR_HORIZONTAL_PADDING
        val scrollBarY = 0
        // Create the scroll bar
        val scrollBar = AOTDGuiScrollBar(
            scrollBarX,
            scrollBarY,
            GUI_WIDTH - mirrorBackgroundImage.getWidth() - SCROLL_BAR_HORIZONTAL_PADDING * 2,
            GUI_HEIGHT,
            "afraidofthedark:textures/gui/spell_list/scroll_bar.png",
            "afraidofthedark:textures/gui/spell_list/scroll_bar_handle.png",
            "afraidofthedark:textures/gui/spell_list/scroll_bar_handle_hovered.png"
        )

        // Create the scroll panel to add spells to, position it centered on the background image
        scrollPanel = AOTDGuiScrollPanel(28, 8, 175, 238, true, scrollBar)
        // Start with a max offset of 0
        scrollPanel.maximumOffset = 0
        // Add the panel the the background and the scroll bar
        backgroundPanel.add(scrollPanel)
        backgroundPanel.add(scrollBar)

        // When we press a key test if we are waiting for a keybind, if so set the spell's keybind
        contentPane.addKeyListener()
        {
            if (it.eventType == AOTDKeyEvent.KeyEventType.Type)
            {
                // If we're waiting on a keybind assign the keybind and update each spell
                if (spellWaitingOnKeybind != null)
                {
                    // Test if the key down is bindable
                    if (KeybindingUtils.keybindableKeyDown())
                    {
                        // Grab the keybind being held
                        val keybind = KeybindingUtils.getCurrentlyHeldKeybind()
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
            scrollPanel.getWidth() / 2 - 13,
            0,
            26,
            26,
            "afraidofthedark:textures/gui/spell_list/create_spell.png",
            "afraidofthedark:textures/gui/spell_list/create_spell_hovered.png"
        )
        btnCreateSpell.setHoverText("Create a new spell")
        btnCreateSpell.addMouseListener()
        {
            if (it.eventType == AOTDMouseEvent.EventType.Press)
            {
                // When we click the button create a new spell
                if (it.source.isVisible && it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    // Create a new spell
                    val spell = Spell(entityPlayer)
                    // Add the spell
                    spellManager.addOrUpdateSpell(spell)
                    // Add the UI spell
                    addSpell(spell)
                }
            }
        }
        btnCreateSpell.addMouseMoveListener()
        {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter)
            {
                // Play the button hover when hovering the add button
                entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f)
            }
        }
        scrollPanel.add(btnCreateSpell)
        // Go over each spell the player has and add a gui spell for it
        spellManager.spells.forEach { addSpell(it) }

        contentPane.add(backgroundPanel)
    }

    /**
     * Adds a gui spell for a given spell into the list
     *
     * @param spell The spell to create a GUI spell for
     */
    private fun addSpell(spell: Spell)
    {
        // Create a gui spell for this spell
        val guiSpell = AOTDGuiSpell(0, guiSpells.size * DISTANCE_BETWEEN_SPELLS, 175, 40, spell)
        // When delete is pressed remove the GUI spell
        guiSpell.setDeleteCallback { removeSpell(guiSpell) }
        // When keybind is pressed update our variable to let us know we're waiting on a keybind, or clear the keybind
        guiSpell.setKeybindCallback()
        {
            // No keybind exists, expect one now
            if (spellManager.getKeybindingForSpell(spell) == null)
            {
                spellWaitingOnKeybind = guiSpell
            }
            else
            {
                spellManager.unbindSpell(spell)
                guiSpell.refreshLabels()
            }
        }
        // Add the gui spell to the panel
        scrollPanel.add(guiSpell)
        // Add the gui spell to the list of spells for later use
        guiSpells.add(guiSpell)
        // Move our create spell button down
        btnCreateSpell.setY(btnCreateSpell.getY() + DISTANCE_BETWEEN_SPELLS)
        // Update our scroll panel offset
        refreshScrollPanelOffset()
    }

    /**
     * Removes a gui spell from the list and also removes the spell from the spell manager
     *
     * @param spell The spell to remove
     */
    private fun removeSpell(spell: AOTDGuiSpell)
    {
        // Find the index of this gui spell
        val index = guiSpells.indexOf(spell)
        // Remove the spell at the index
        guiSpells.removeAt(index)
        // Remove the spell from the scroll panel
        scrollPanel.remove(spell)
        // Remove the spell from the spell manager
        spellManager.deleteSpell(spell.spell)
        // Go over all spells after this one and move them up one slot
        for (i in index until guiSpells.size)
        {
            val guiSpell = guiSpells[i]
            guiSpell.setY(guiSpell.getY() - DISTANCE_BETWEEN_SPELLS)
        }
        // Move our create spell button up
        btnCreateSpell.setY(btnCreateSpell.getY() - DISTANCE_BETWEEN_SPELLS)
        // Update our scroll panel offset
        refreshScrollPanelOffset()
    }

    /**
     * Updates the scroll panel offset based on the gui spells present
     */
    private fun refreshScrollPanelOffset()
    {
        // If there's more than 4 spells update the maximum offset so we can scroll over spells
        if (guiSpells.size > 4)
        {
            scrollPanel.maximumOffset = (guiSpells.size - 4) * DISTANCE_BETWEEN_SPELLS
        }
        else
        {
            scrollPanel.maximumOffset = 0
        }
    }

    /**
     * When the GUI is closed sync the player's spell manager
     */
    override fun onGuiClosed()
    {
        spellManager.syncAll(entityPlayer)
    }

    /**
     * @return True, the inventory key closes the screen
     */
    override fun inventoryToCloseGuiScreen(): Boolean
    {
        return true
    }

    /**
     * @return True, the screen should have a gradient background
     */
    override fun drawGradientBackground(): Boolean
    {
        return true
    }

    companion object
    {
        // The distance between spell entries in pixels
        private const val DISTANCE_BETWEEN_SPELLS = 45
        // Constants for scroll bar width and padding
        private const val SCROLL_BAR_WIDTH = 15
        private const val SCROLL_BAR_HORIZONTAL_PADDING = 5
        // The gui will be 256x256
        private const val GUI_WIDTH = 256
        private const val GUI_HEIGHT = 256
    }
}