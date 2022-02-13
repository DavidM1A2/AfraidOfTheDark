package com.davidm1a2.afraidofthedark.client.gui.screens

import com.davidm1a2.afraidofthedark.client.gui.customControls.SpellScroll
import com.davidm1a2.afraidofthedark.client.gui.customControls.SpellTablet
import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TranslationTextComponent

/**
 * Class representing the spell crating GUI screen used to edit spells
 */
class SpellCraftingScreen(private val spell: Spell) : AOTDScreen(TranslationTextComponent("screen.afraidofthedark.spell_crafting"), true) {
    private val tablet: SpellTablet
    private val scroll: SpellScroll

    init {
        // First ensure the spell has the minimum 1 spell stage
        if (spell.spellStages.isEmpty()) {
            spell.spellStages.add(SpellStage())
        }

        // Add the background image
        val bkgPane = ImagePane("afraidofthedark:textures/gui/spell_editor/spell_crafting_table.png", ImagePane.DispMode.FIT_TO_PARENT)
        bkgPane.gravity = Gravity.CENTER
        contentPane.add(bkgPane)
        contentPane.padding = Spacing(0.1)

        // Create the left side tablet to hold the current spell settings
        tablet = SpellTablet(spell)
        bkgPane.add(tablet)

        // Create the right side scroll to hold the current spell components available
        scroll = SpellScroll()
        bkgPane.add(scroll)

        // Cross component listeners
        tablet.componentClickCallback = { scroll.setEditing(it.getComponentInstance()) }
        scroll.componentPropModifiedCallback = { tablet.refreshCostLabel() }

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

    override fun removed() {
        // Grab the player's spell manager
        val spellManager = entityPlayer.getSpellManager()
        // Sync the spell server side to "save" it
        spellManager.sync(entityPlayer, spell)
        super.removed()
    }

    override fun inventoryToCloseGuiScreen(): Boolean {
        return false
    }

    override fun drawGradientBackground(): Boolean {
        return true
    }
}