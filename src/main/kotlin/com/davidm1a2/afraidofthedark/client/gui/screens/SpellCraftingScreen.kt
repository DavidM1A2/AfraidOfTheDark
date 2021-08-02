package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.customControls.SpellScroll
import com.davidm1a2.afraidofthedark.client.gui.customControls.SpellTablet
import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.HChainPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TranslationTextComponent

/**
 * Class representing the spell crating GUI screen used to edit spells
 */
class SpellCraftingScreen(spell: Spell) : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.spell_crafting"), true) {
    private val tablet: SpellTablet
    private val scroll: SpellScroll

    init {
        // Clone the spell so we don't modify the original
        val spellClone = Spell(spell.serializeNBT())

        // First ensure the spell has the minimum 1 spell stage
        if (spellClone.spellStages.isEmpty()) {
            spellClone.spellStages.add(SpellStage())
        }

        // Make an HPane to organize the different parts of the GUI
        val layoutPane = HChainPane(HChainPane.Layout.CLOSE)
        contentPane.add(layoutPane)
        contentPane.padding = Spacing(0.1)

        // Create the left side tablet to hold the current spell settings
        tablet = SpellTablet(spellClone)
        layoutPane.add(tablet)

        // Create the right side scroll to hold the current spell components available
        scroll = SpellScroll()
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
            if (it.eventType == KeyEvent.KeyEventType.Press) {
                it.source.isVisible = false
            }
        }
        // When pressing help on the tablet show the help overlay
        tablet.onHelp = { helpOverlay.isVisible = true }
        contentPane.add(helpOverlay)

        contentPane.addKeyListener {
            // If the inventory key closes the ui and is pressed open the spell list UI
            if (it.eventType == KeyEvent.KeyEventType.Press) {
                if (tablet.inventoryKeyClosesUI() && scroll.inventoryKeyClosesUI()) {
                    if (isInventoryKeybind(it.key, it.scanCode)) {
                        if (scroll.isEditingProps()) {
                            scroll.setEditing(null)
                        } else {
                            Minecraft.getInstance().setScreen(SpellListScreen())
                        }
                    }
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