package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDScreen
import com.davidm1a2.afraidofthedark.client.gui.customControls.AOTDGuiSpellScroll
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.customControls.SpellComponentSlot
import com.davidm1a2.afraidofthedark.client.gui.customControls.AOTDGuiSpellTablet
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativeSpacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.HPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TranslationTextComponent

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
class SpellCraftingScreen(spell: Spell) : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.spell_crafting"), true) {
    private val tablet: AOTDGuiSpellTablet
    private val scroll: AOTDGuiSpellScroll
    private val selectedCursorIcon: ImagePane
    private var selectedComponent: SpellComponentSlot<*>? = null

    init {
        // Clone the spell so we don't modify the original
        val spellClone = Spell(spell.serializeNBT())

        // First ensure the spell has the minimum 1 spell stage
        if (spellClone.spellStages.isEmpty()) {
            spellClone.spellStages.add(SpellStage())
        }

        // Make an HPane to organize the different parts of the GUI
        val layoutPane = HPane(HPane.Layout.CLOSE)
        contentPane.add(layoutPane)
        contentPane.padding = RelativeSpacing(0.1)

        // Create the left side tablet to hold the current spell settings
        tablet = AOTDGuiSpellTablet(spellClone)
        layoutPane.add(tablet)

        // Setup the selected component hover under the mouse cursor using image component
        selectedCursorIcon = ImagePane("afraidofthedark:textures/gui/spell_editor/blank_slot.png", ImagePane.DispMode.FIT_TO_TEXTURE)
        contentPane.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Move) {
                if (selectedComponent != null) {
                    selectedCursorIcon.x = ((it.mouseX) - selectedCursorIcon.width / 2)
                    selectedCursorIcon.y = ((it.mouseY) - selectedCursorIcon.height / 2)
                }
            }
        }

        // Create the right side scroll to hold the current spell components available
        scroll = AOTDGuiSpellScroll()
        // When we click a component on the tablet update it as being edited
        tablet.componentEditCallback = { scroll.setEditing(it.getComponentInstance()) }
        layoutPane.add(scroll)

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
            if (tablet.inventoryKeyClosesUI()) { //  && scroll.inventoryKeyClosesUI()
                if (isInventoryKeybind(it.key, it.scanCode)) {
                    Minecraft.getInstance().displayGuiScreen(SpellListScreen())
                }
            }
        }
    }

    override fun inventoryToCloseGuiScreen(): Boolean {
        return false
    }

    override fun drawGradientBackground(): Boolean {
        return true
    }
}