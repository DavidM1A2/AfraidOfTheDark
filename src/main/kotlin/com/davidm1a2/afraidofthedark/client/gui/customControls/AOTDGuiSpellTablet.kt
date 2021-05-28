package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import com.davidm1a2.afraidofthedark.client.gui.screens.SpellListScreen
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TranslationTextComponent
import java.util.*
import kotlin.math.roundToInt

/**
 * Class representing the tablet used in the spell crafting gui on the left
 *
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @property spell  The spell being edited
 * @property selectedComponentGetter A special supplier that gets the currently selected component
 * @property clearSelectedComponent A special runnable that tells the crafting UI to clear it's currently selected spell component
 * @property spellName The text field containing the spell's name
 * @property spellStagePanel The scroll panel containing the spell's stages
 * @property uiSpellStages A list of spell stages that this spell has
 * @property uiPowerSource The spell's power source
 * @property spellCost A label to show the spell's cost
 * @property onHelp Listener for the help button
 * @property componentEditCallback The callback that will be fired when a spell component is selected
 *
 */
class AOTDGuiSpellTablet(
    private val spell: Spell
) : ImagePane("afraidofthedark:textures/gui/spell_editor/tablet_background.png", DispMode.FIT_TO_PARENT) {

    private val spellName: AOTDGuiTextField
    private val spellStagePanel: StackPane
    private val spellStageList: ListPane
    private val spellStageBackground: ImagePane
    private val uiSpellStages: MutableList<AOTDGuiSpellStage> = mutableListOf()
    private val uiPowerSource: SpellPowerSourceSlot
    private val spellCost: AOTDGuiLabel
    private val scrollBar: HScrollBar
    private val addButton: Button
    private val removeButton: Button
    private val buttonLayout: HPane
    var onHelp: (() -> Unit)? = null
    var componentEditCallback: ((SpellComponentSlot<*>) -> Unit)? = null
    set(value) {
        field = value
        uiSpellStages.forEach { it.componentEditCallback = field }
    }

    init {

        // Setup the spell name label
        spellName = AOTDGuiTextField(RelativePosition(0.2, 0.18), RelativeDimensions(0.5, 0.1), ClientData.getOrCreate(36f))
        spellName.setGhostText("Spell Name")
        // When we type into this slot set the spell name
        spellName.addKeyListener {
            if (it.eventType == AOTDKeyEvent.KeyEventType.Release) {
                spell.name = spellName.getText()
            }
        }
        this.add(spellName)

        // Add a scroll bar on the left to scroll through spell stages
        scrollBar = HScrollBar(RelativeDimensions(0.08, 0.6))
        scrollBar.offset = RelativePosition(0.05, 0.3)
        this.add(scrollBar)

        // Make the spell stage panel
        spellStagePanel = StackPane(RelativeDimensions(0.6, 0.6))
        spellStagePanel.offset = RelativePosition(0.15, 0.3)
        this.add(spellStagePanel)

        // Add the background for the spell stages
        spellStageBackground =
            ImagePane("afraidofthedark:textures/gui/spell_editor/spell_stage_panel_background.png", DispMode.STRETCH)
        spellStageBackground.prefSize = RelativeDimensions(1.0, 1.0)
        spellStagePanel.add(spellStageBackground)

        // Add the spell stage scroll panel
        spellStageList = ListPane(ListPane.ExpandDirection.DOWN, scrollBar)
        spellStageList.padding = RelativeSpacing(0.02)
        spellStagePanel.add(spellStageList)

        // Create add and remove buttons for spell stages
        addButton = Button(
            icon = ImagePane("afraidofthedark:textures/gui/spell_editor/add.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/add_hovered.png"),
            prefSize = RelativeDimensions(0.45, 1.0)
        )
        addButton.addOnClick {
            val newStage = SpellStage()
            spell.spellStages.add(newStage)
            addGuiSpellStage(newStage)
        }
        removeButton = Button(
            icon = ImagePane("afraidofthedark:textures/gui/spell_editor/delete.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/delete_hovered.png"),
            prefSize = RelativeDimensions(0.45, 1.0)
        )
        removeButton.addOnClick {
            if (spell.spellStages.size > 1) spell.spellStages.removeLast()
            removeLastGuiSpellStage()
        }
        buttonLayout = HPane()
        buttonLayout.prefSize = RelativeDimensions(0.25, 0.1)
        buttonLayout.add(addButton)
        buttonLayout.add(removeButton)
        spellStageList.add(buttonLayout)

        // Create a save spell button
        val saveButton =
            Button(
                icon = ImagePane("afraidofthedark:textures/gui/spell_editor/save.png"),
                iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/save_hovered.png"),
                prefSize = RelativeDimensions(0.13, 0.1),
                offset = RelativePosition(0.76, 0.4)
            )
        saveButton.setHoverText("Save Spell")
        saveButton.addOnClick {
            // Grab the player's spell manager
            val spellManager = entityPlayer.getSpellManager()
            // Clone the spell so we don't modify it further
            val spellClone = Spell(spell.serializeNBT())
            // Update the spell
            spellManager.addOrUpdateSpell(spellClone)
            // Sync the spell server side
            spellManager.sync(entityPlayer, spellClone)
            // Tell the player the save was successful
            entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.spell.save_successful", spellClone.name))
        }
        this.add(saveButton)

        // Create a close UI and don't save button
        val closeButton =
            Button(
                icon = ImagePane("afraidofthedark:textures/gui/spell_editor/delete.png"),
                iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/delete_hovered.png"),
                prefSize = RelativeDimensions(0.13, 0.1),
                offset = RelativePosition(0.76, 0.52)
            )
        closeButton.setHoverText("Exit (Without Saving)")
        closeButton.addOnClick {
            // Open the list gui without saving
            Minecraft.getInstance().displayGuiScreen(SpellListScreen())
        }
        this.add(closeButton)

        // Create a help button
        val helpButton = Button(
            icon = ImagePane("afraidofthedark:textures/gui/spell_editor/question.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/question_hovered.png"),
            prefSize = RelativeDimensions(0.13, 0.1),
            offset = RelativePosition(0.76, 0.64)
        )
        helpButton.setHoverText("Help")
        // When pressing help execute our on help runnable
        helpButton.addOnClick { onHelp?.invoke() }
        this.add(helpButton)

        // Create the power source spell slot
        uiPowerSource = SpellPowerSourceSlot(RelativePosition(0.57, 0.07), RelativeDimensions(0.13, 0.1), spell)
        uiPowerSource.setSpellComponent(spell.powerSource)
        this.add(uiPowerSource)

        // Add the spell cost label
        spellCost = AOTDGuiLabel(ClientData.getOrCreate(32f), RelativeDimensions(0.8, 0.2))
        spellCost.offset = RelativePosition(0.1, 0.8)
        this.add(spellCost)

        // Update all the gui components from our spell
        refresh()
    }

    /**
     * Refreshes the state of the tablet based on the current spell
     */
    private fun refresh() {
        // Update the power source instance
        uiPowerSource.setSpellComponent(spell.powerSource)
        // Update the spell's name
        spellName.setText(spell.name)
        // Remove all existing spell stages
        while (uiSpellStages.isNotEmpty()) {
            removeLastGuiSpellStage()
        }
        // Add one gui spell stage for each stage
        spell.spellStages.forEach { addGuiSpellStage(it) }
        // Refresh each gui stage
        uiSpellStages.forEach { it.refresh() }
        // Update the spell cost label
        refreshCost()
    }

    /**
     * Updates the cost label
     */
    private fun refreshCost() {
        // Update the spell cost label
        spellCost.text = "Cost: ${spell.getCost().roundToInt()}"
    }

    /**
     * Adds a gui spell stage to the UI
     *
     * @param spellStage The stage to create a UI component for
     */
    private fun addGuiSpellStage(spellStage: SpellStage) {
        // Create the spell stage GUI
        val spellStageGui = AOTDGuiSpellStage(spellStage, spell, componentEditCallback)
        // Add the spell stage to the panel
        spellStageList.remove(buttonLayout)
        spellStageList.add(spellStageGui)
        spellStageList.add(buttonLayout)
        // Add the spell stage to the stage list
        uiSpellStages.add(spellStageGui)
        // If only 1 spell stage exists hide the delete button
        removeButton.isVisible = uiSpellStages.size > 1
    }

    /**
     * Removes the last GUI spell stage from the list
     */
    private fun removeLastGuiSpellStage() {
        // If there is a spell stage to remove...
        if (uiSpellStages.isNotEmpty()) {
            // Grab the last spell stage in the list to remove
            val lastStage = uiSpellStages[uiSpellStages.size - 1]
            // Remove the stage from the panel and list of stages
            spellStageList.remove(lastStage)
            uiSpellStages.remove(lastStage)
            // If only 1 spell stage exists hide the delete button
            removeButton.isVisible = uiSpellStages.size > 1
        }
    }

    /**
     * @return True if the inventory key should currently close the UI, false otherwise
     */
    fun inventoryKeyClosesUI(): Boolean {
        return !spellName.isFocused
    }
}