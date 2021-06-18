package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import com.davidm1a2.afraidofthedark.client.gui.screens.SpellCraftingScreen
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LabelComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.client.Minecraft
import net.minecraft.util.SoundEvents
import java.awt.Color

/**
 * UI component representing a spell in the GUI
 */
class SpellListItem(prefSize: Dimensions, val spell: Spell) : StackPane(prefSize = prefSize, margins = Spacing(0.0, 0.0, 0.0, 0.0)) {

    private val lblKeybind: LabelComponent
    private var keybindCallback: (() -> Unit) = { }
    private var deleteCallback: (() -> Unit) = { }

    init {
        // The background image to hold all the buttons
        val background =
            ImagePane("afraidofthedark:textures/gui/spell_list/spell_background.png", ImagePane.DispMode.STRETCH)
        this.add(background)

        // The container for spell name
        val spellNameContainer = StackPane(Dimensions(0.8, 0.5))

        // The label holding the actual spell name
        val lblSpellName = LabelComponent(ClientData.getOrCreate(36f), Dimensions(1.0, 1.0))
        // Set the name label's name and color
        lblSpellName.text = this.spell.name
        lblSpellName.textColor = Color(245, 61, 199)
        lblSpellName.textAlignment = TextAlignment.ALIGN_CENTER
        // Update the hover text of the container and add the spell label to it
        spellNameContainer.setHoverText(lblSpellName.text)
        spellNameContainer.gravity = Gravity.TOP_CENTER
        spellNameContainer.add(lblSpellName)
        this.add(spellNameContainer)

        // When we hover any button play hover sound
        val hoverSound: ((MouseMoveEvent) -> Unit) =
            {
                if (it.eventType == MouseMoveEvent.EventType.Enter) {
                    // Play a hover sound for visible buttons
                    if (it.source.isVisible && it.source.isHovered) {
                        entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.7f, 1.9f)
                    }
                }
            }

        // When we click any button play the click sound
        val clickSound: ((MouseEvent) -> Unit) =
            {
                if (it.eventType == MouseEvent.EventType.Click) {
                    // Play a clicked sound for visible buttons
                    if (it.source.isVisible && it.source.isHovered) {
                        entityPlayer.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f)
                    }
                }
            }

        // Create a button to edit the spell
        val btnEdit =
            ButtonPane(
                icon = ImagePane("afraidofthedark:textures/gui/spell_list/spell_edit.png"),
                iconHovered = ImagePane("afraidofthedark:textures/gui/spell_list/spell_edit_hovered.png"),
                prefSize = Dimensions(0.145, 0.35),
                offset = Position(0.82, 0.52)
            )
        btnEdit.addMouseListener(clickSound)
        btnEdit.addMouseMoveListener(hoverSound)
        btnEdit.setHoverText("Edit Spell")
        btnEdit.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.source.isHovered && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    // Open the spell edit GUI
                    Minecraft.getInstance().displayGuiScreen(SpellCraftingScreen(spell))
                }
            }
        }
        this.add(btnEdit)

        // Create a button to delete a spell
        val btnDelete = ButtonPane(
            icon = ImagePane("afraidofthedark:textures/gui/spell_list/spell_delete.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/spell_list/spell_delete_hovered.png"),
            prefSize = Dimensions(0.145, 0.35),
            offset = Position(0.035, 0.52)
        )
        btnDelete.addMouseListener(clickSound)
        btnDelete.addMouseMoveListener(hoverSound)
        btnDelete.hoverTexts = arrayOf("Delete Spell", "This cannot be undone")
        btnDelete.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.source.isHovered && it.source.isVisible && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    it.consume()    // Consume so we don't accidentally delete multiple spells at once
                    deleteCallback()
                }
            }
        }
        this.add(btnDelete)

        // Create a button to keybind this spell
        lblKeybind = LabelComponent(ClientData.getOrCreate(30f), Dimensions(0.6, 0.3))
        lblKeybind.textAlignment = TextAlignment.ALIGN_CENTER
        lblKeybind.offset = Position(0.2, 0.5)
        btnEdit.addMouseListener(clickSound)
        btnEdit.addMouseMoveListener(hoverSound)
        lblKeybind.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.source.isHovered && it.source.isVisible && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
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
