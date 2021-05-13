package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDImageDispMode
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.screens.SpellCraftingScreen
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiLabel
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiPanel
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.client.Minecraft
import net.minecraft.util.SoundEvents
import java.awt.Color

/**
 * UI component representing a spell in the GUI
 *
 * @constructor Just initializes the gui spell by laying out all necessary controls
 * @param x The x position of the control
 * @param y The y position of the control
 * @param width The width of the control
 * @param height The height of the control
 * @param spell The spell that this control represents
 * @property lblKeybind Reference to the keybind label that allows us to bind keys to spells
 * @property keybindCallback Callback function that we fire when we want a new keybind for this spell
 * @property deleteCallback Callback function that we fire when the delete spell button is pressed
 */
class AOTDGuiSpell(x: Int, y: Int, width: Int, height: Int, val spell: Spell) : AOTDGuiContainer(x, y, width, height) {
    private val lblKeybind: AOTDGuiLabel
    private var keybindCallback: (() -> Unit) = { }
    private var deleteCallback: (() -> Unit) = { }

    init {
        // The background image to hold all the buttons
        val background =
            AOTDGuiImage("afraidofthedark:textures/gui/spell_list/spell_background.png", AOTDImageDispMode.FIT_TO_PARENT)
        this.add(background)

        // The container for spell name
        val spellNameContainer = AOTDGuiPanel(width - 20, 15)

        // The label holding the actual spell name
        val lblSpellName = AOTDGuiLabel(ClientData.getOrCreate(36f))
        // Set the name label's name and color
        lblSpellName.text = this.spell.name
        lblSpellName.textColor = Color(245, 61, 199)
        lblSpellName.textAlignment = TextAlignment.ALIGN_CENTER
        // Update the hover text of the container and add the spell label to it
        spellNameContainer.setHoverText(lblSpellName.text)
        spellNameContainer.add(lblSpellName)
        this.add(spellNameContainer)

        // When we hover any button play hover sound
        val hoverSound: ((AOTDMouseMoveEvent) -> Unit) =
            {
                if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                    // Play a hover sound for visible buttons
                    if (it.source.isVisible && it.source.isHovered) {
                        entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.7f, 1.9f)
                    }
                }
            }

        // When we click any button play the click sound
        val clickSound: ((AOTDMouseEvent) -> Unit) =
            {
                if (it.eventType == AOTDMouseEvent.EventType.Click) {
                    // Play a clicked sound for visible buttons
                    if (it.source.isVisible && it.source.isHovered) {
                        entityPlayer.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f)
                    }
                }
            }

        // Create a button to edit the spell
        val btnEdit =
            AOTDGuiButton(
                24,
                13,
                icon = AOTDGuiImage("afraidofthedark:textures/gui/spell_list/spell_edit.png"),
                iconHovered = AOTDGuiImage("afraidofthedark:textures/gui/spell_list/spell_edit_hovered.png")
            )
        btnEdit.addMouseListener(clickSound)
        btnEdit.addMouseMoveListener(hoverSound)
        btnEdit.setHoverText("Edit Spell")
        btnEdit.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    // Open the spell edit GUI
                    Minecraft.getInstance().displayGuiScreen(SpellCraftingScreen(spell))
                }
            }
        }
        this.add(btnEdit)

        // Create a button to delete a spell
        val btnDelete = AOTDGuiButton(
            24,
            13,
            icon = AOTDGuiImage("afraidofthedark:textures/gui/spell_list/spell_delete.png"),
            iconHovered = AOTDGuiImage("afraidofthedark:textures/gui/spell_list/spell_delete_hovered.png")
        )
        btnDelete.addMouseListener(clickSound)
        btnDelete.addMouseMoveListener(hoverSound)
        btnDelete.hoverTexts = arrayOf("Delete Spell", "This cannot be undone")
        btnDelete.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                if (it.source.isHovered && it.source.isVisible && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    deleteCallback()
                }
            }
        }
        this.add(btnDelete)

        // Create a button to keybind this spell
        lblKeybind = AOTDGuiLabel(ClientData.getOrCreate(30f))
        lblKeybind.textAlignment = TextAlignment.ALIGN_CENTER
        btnEdit.addMouseListener(clickSound)
        btnEdit.addMouseMoveListener(hoverSound)
        lblKeybind.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                if (it.source.isHovered && it.source.isVisible && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    lblKeybind.setHoverText("Awaiting keypress...")
                    lblKeybind.text = "Awaiting keypress..."
                    keybindCallback()
                }
            }
        }
        this.add(lblKeybind)

        // Refresh the spell labels
        this.refreshLabels()
    }

    /**
     * Refreshes this gui spell based on the current spell state if it's changed
     */
    fun refreshLabels() {
        // Grab the player's spell manager
        val spellManager = entityPlayer.getSpellManager()
        // Get the keybinding for the spell
        val keybindingForSpell = spellManager.getKeybindingForSpell(this.spell)
        // if the keybind is non-null show it, otherwise mention it's unbound
        if (keybindingForSpell != null) {
            lblKeybind.setHoverText("Spell is bound to: $keybindingForSpell")
            lblKeybind.text = keybindingForSpell
        } else {
            lblKeybind.setHoverText("Spell is unbound.")
            lblKeybind.text = ""
        }
    }

    /**
     * Sets the callback when the keybind button is pressed
     *
     * @param keybindCallback The keybind callback to fire
     */
    fun setKeybindCallback(keybindCallback: () -> Unit) {
        this.keybindCallback = keybindCallback
    }

    /**
     * Sets the callback when the delete button is pressed
     *
     * @param deleteCallback The delete callback to fire
     */
    fun setDeleteCallback(deleteCallback: () -> Unit) {
        this.deleteCallback = deleteCallback
    }
}
