package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDScreen
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellComponentSlot
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellScroll
import com.davidm1a2.afraidofthedark.client.gui.specialControls.AOTDGuiSpellTablet
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Image

/**
 * Class representing the spell crating GUI screen used to edit spells
 *
 * @constructor creates the spell GUI
 * @param spell The spell that this gui will edit
 * @property tablet The tablet left side of the GUI
 * @property scroll The scroll right side of the GUI
 * @property selectedCursorIcon The selected cursor icon to hold the currently selected component's icon
 * @property selectedComponent The currently selected spell component slot on the spell scroll, or null
 */
class SpellCraftingScreen(spell: Spell) : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.spell_crafting")) {
    private val tablet: AOTDGuiSpellTablet
    private val scroll: AOTDGuiSpellScroll
    private val selectedCursorIcon: ImagePane
    private var selectedComponent: AOTDGuiSpellComponentSlot<*>? = null

    init {
        // Clone the spell so we don't modify the original\
        val spellClone = Spell(spell.serializeNBT())

        // First ensure the spell has the minimum 1 spell stage
        if (spellClone.spellStages.isEmpty()) {
            spellClone.spellStages.add(SpellStage())
        }

        // Create the left side tablet to hold the current spell settings
        tablet = AOTDGuiSpellTablet(
            100,
            (Constants.BASE_GUI_HEIGHT - 256) / 2,
            192,
            256,
            spellClone,
            { this.selectedComponent },
            { this.setSelectedComponent(null) }
        )
        contentPane.add(tablet)

        // Setup the selected component hover under the mouse cursor using image component
        selectedCursorIcon = ImagePane("afraidofthedark:textures/gui/spell_editor/blank_slot.png", ImagePane.DispMode.STRETCH)
        selectedCursorIcon.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Move) {
                // If we have nothing selected put the component off in the middle of nowhere
                if (selectedComponent == null) {
                    selectedCursorIcon.x = -20
                    selectedCursorIcon.y = -20
                } else {
                    selectedCursorIcon.x = ((it.mouseX) - selectedCursorIcon.width / 2)
                    selectedCursorIcon.y = ((it.mouseY) - selectedCursorIcon.height / 2)
                }
            }
        }

        // If we right click clear the selected component
        contentPane.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                if (it.clickedButton == AOTDMouseEvent.RIGHT_MOUSE_BUTTON && selectedComponent != null) {
                    setSelectedComponent(null)
                }
            }
        }

        // Create the right side scroll to hold the current spell components available
        scroll = AOTDGuiSpellScroll(340, (Constants.BASE_GUI_HEIGHT - 256) / 2, 220, 256)
        // When we click a component on the scroll update it as hovered
        scroll.setComponentClickCallback { setSelectedComponent(it) }
        // When we click a component on the tablet update it as being edited
        tablet.componentEditCallback = { scroll.setEditing(it.getComponentInstance()) }
        contentPane.add(scroll)
        contentPane.add(selectedCursorIcon)

        // Create a help overlay that comes up when you press the ? button
        val helpOverlay = ImagePane(
            "afraidofthedark:textures/gui/spell_editor/help_screen.png",
            ImagePane.DispMode.FIT_TO_PARENT
        )
        helpOverlay.isVisible = false
        // When pressing any key hide the overlay
        helpOverlay.addKeyListener {
            if (it.eventType == AOTDKeyEvent.KeyEventType.Press) {
                it.source.isVisible = false
            }
        }
        // When pressing help on the tablet show the help overlay
        tablet.onHelp = { helpOverlay.isVisible = true }
        contentPane.add(helpOverlay)

        contentPane.addKeyListener {
            // If the inventory key closes the ui and is pressed open the spell list UI
            if (tablet.inventoryKeyClosesUI() && scroll.inventoryKeyClosesUI()) {
                if (isInventoryKeybind(it.key, it.scanCode)) {
                    Minecraft.getInstance().displayGuiScreen(SpellListScreen())
                }
            }
        }
    }

    /**
     * Update the selected component, highlight the component// If we have a previously selected component deselect it
     * If the new component is non-null update our image texture and highlight the component
     *
     * @param selectedComponent The newly selected component, could be null to clear
     */
    private fun setSelectedComponent(selectedComponent: AOTDGuiSpellComponentSlot<*>?) {
        // If we have a previously selected component deselect it
        this.selectedComponent?.setHighlight(false)

        // If the new component is non-null update our image texture and highlight the component
        if (selectedComponent != null) {
            // Update the selected component, highlight the component
            this.selectedComponent = selectedComponent
            this.selectedComponent!!.setHighlight(true)
            // Cast is not useless, don't trust IJ here :)
            @Suppress("USELESS_CAST")
            selectedCursorIcon.imageTexture = (this.selectedComponent!!.getComponentType()!! as SpellComponent<*>).icon
            selectedCursorIcon.isVisible = true
        } else {
            this.selectedComponent = null
            selectedCursorIcon.isVisible = false
        }
    }

    /**
     * False, inventory doesn't close the gui screen. We have more advanced logic in keyTyped()
     *
     * @return False to avoid any super code going off
     */
    override fun inventoryToCloseGuiScreen(): Boolean {
        return false
    }

    /**
     * True, we want the background to be a gradient
     *
     * @return True
     */
    override fun drawGradientBackground(): Boolean {
        return true
    }
}