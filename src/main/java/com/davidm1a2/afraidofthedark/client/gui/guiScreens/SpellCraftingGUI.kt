package com.davidm1a2.afraidofthedark.client.gui.guiScreens

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiScreen
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellComponentSlot
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellScroll
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellTablet
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import java.io.IOException

/**
 * Class representing the spell crating GUI screen used to edit spells
 *
 * @param spell The spell that this gui will edit
 * @property tablet The tablet left side of the GUI
 * @property scroll The scroll right side of the GUI
 * @property selectedCursorIcon The selected cursor icon to hold the currently selected component's icon
 * @property selectedComponent The currently selected spell component slot on the spell scroll, or null
 */
class SpellCraftingGUI(spell: Spell) : AOTDGuiScreen()
{
    private val tablet: AOTDGuiSpellTablet
    private val scroll: AOTDGuiSpellScroll
    private val selectedCursorIcon: AOTDGuiImage
    private var selectedComponent: AOTDGuiSpellComponentSlot<*, *>? = null
        /**
         * Update the selected component, highlight the component// If we have a previously selected component deselect it
         * If the new component is non-null update our image texture and highlight the component
         */
        private set(selectedComponent)
        {
            // If we have a previously selected component deselect it
            field?.setHighlight(false)

            // If the new component is non-null update our image texture and highlight the component
            if (selectedComponent != null)
            {
                // Update the selected component, highlight the component
                field = selectedComponent
                field!!.setHighlight(true)
                // TODO: FIX GENERIC
                // selectedCursorIcon.imageTexture = field!!.getComponentType().getIcon()
                selectedCursorIcon.isVisible = true
            }
            else
            {
                field = null
                selectedCursorIcon.isVisible = false
            }
        }

    /**
     * Constructor creates the spell GUI
     */
    init
    {
        // Clone the spell so we don't modify the original\
        val spellClone = Spell(spell.serializeNBT())

        // First ensure the spell has the minimum 1 spell stage
        if (spellClone.spellStages.isEmpty())
        {
            spellClone.spellStages.add(SpellStage())
        }

        // Create the left side tablet to hold the current spell settings
        tablet = AOTDGuiSpellTablet(
                100,
                (Constants.GUI_HEIGHT - 256) / 2,
                192,
                256,
                spellClone,
                { this.selectedComponent },
                { this.selectedComponent = null }
        )
        contentPane.add(tablet)

        // Setup the selected component hover under the mouse cursor using image component
        selectedCursorIcon = AOTDGuiImage(0, 0, 20, 20, "afraidofthedark:textures/gui/spell_editor/blank_slot.png")
        selectedCursorIcon.addMouseMoveListener()
        {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Move)
            {
                // If we have nothing selected put the component off in the middle of nowhere
                if (selectedComponent == null)
                {
                    selectedCursorIcon.x = -20
                    selectedCursorIcon.y = -20
                }
                else
                {
                    selectedCursorIcon.x = (it.mouseX / tablet.scaleX).toInt() - selectedCursorIcon.widthScaled / 2
                    selectedCursorIcon.y = (it.mouseY / tablet.scaleY).toInt() - selectedCursorIcon.heightScaled / 2
                }
            }
        }

        // If we right click clear the selected component
        contentPane.addMouseListener()
        {
            if (it.eventType == AOTDMouseEvent.EventType.Press)
            {
                if (it.clickedButton == AOTDMouseEvent.RIGHT_MOUSE_BUTTON && selectedComponent != null)
                {
                    selectedComponent = null
                }
            }
        }

        // Create the right side scroll to hold the current spell components available
        scroll = AOTDGuiSpellScroll(340, (Constants.GUI_HEIGHT - 256) / 2, 220, 256)
        // When we click a component on the scroll update it as hovered
        scroll.setComponentClickCallback { selectedComponent = it }
        // When we click a component on the tablet update it as being edited
        tablet.componentEditCallback = { scroll.setEditing(it.getComponentInstance()) }
        contentPane.add(scroll)
        contentPane.add(selectedCursorIcon)

        // Create a help overlay that comes up when you press the ? button
        val helpOverlay = AOTDGuiImage(0, 0, Constants.GUI_WIDTH, Constants.GUI_HEIGHT, "afraidofthedark:textures/gui/spell_editor/help_screen.png")
        helpOverlay.isVisible = false
        // When pressing any key hide the overlay
        helpOverlay.addKeyListener()
        {
            if (it.eventType == AOTDKeyEvent.KeyEventType.Type)
            {
                it.source.isVisible = false
            }
        }
        // When pressing help on the tablet show the help overlay
        tablet.onHelp = { helpOverlay.isVisible = true }
        contentPane.add(helpOverlay)
    }

    /**
     * Called whenever a key is typed, we ask our key handler to handle the event
     * Also open the spell list GUI
     *
     * @param character The character typed
     * @param keyCode   The code of the character typed
     * @throws IOException forwarded from the super method
     */
    override fun keyTyped(character: Char, keyCode: Int)
    {
        super.keyTyped(character, keyCode)
        // If the inventory key closes the ui and is pressed open the spell list UI
        if (tablet.inventoryKeyClosesUI() && scroll.inventoryKeyClosesUI())
        {
            if (keyCode == inventoryKeycode)
            {
                entityPlayer.openGui(Constants.MOD_ID, AOTDGuiHandler.SPELL_LIST_ID, entityPlayer.world, entityPlayer.position.x, entityPlayer.position.y, entityPlayer.position.z)
            }
        }
    }

    /**
     * False, inventory doesn't close the gui screen. We have more advanced logic in keyTyped()
     *
     * @return False to avoid any super code going off
     */
    override fun inventoryToCloseGuiScreen(): Boolean
    {
        return false
    }

    /**
     * True, we want the background to be a gradient
     *
     * @return True
     */
    override fun drawGradientBackground(): Boolean
    {
        return true
    }
}