package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import com.davidm1a2.afraidofthedark.client.gui.screens.SpellListScreen
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TranslationTextComponent
import kotlin.math.roundToInt

/**
 * Class representing the tablet used in the spell crafting gui on the left
 */
class SpellTablet(
    private val spell: Spell
) : ImagePane("afraidofthedark:textures/gui/spell_editor/tablet_background.png", DispMode.FIT_TO_PARENT) {

    private val spellName: TextFieldPane
    private val spellStagePanel: StackPane
    private val spellStageList: ListPane
    private val spellStageBackground: ImagePane
    private val uiSpellStages: MutableList<GuiSpellStage> = mutableListOf()
    private val uiPowerSource: SpellPowerSourceSlot
    private val spellCost: LabelComponent
    private val scrollBar: VScrollBar
    private val addButton: ButtonPane
    private val removeButton: ButtonPane
    private val buttonLayout: HChainPane
    var onHelp: (() -> Unit)? = null
    var componentEditCallback: ((SpellComponentSlot<*>) -> Unit)? = null
    set(value) {
        field = value
        uiSpellStages.forEach { it.componentEditCallback = field }
    }

    init {

        // Setup the spell name label
        spellName = TextFieldPane(Position(0.2, 0.18), Dimensions(0.5, 0.1), ClientData.getOrCreate(36f))
        spellName.setGhostText("Spell Name")
        // When we type into this slot set the spell name
        spellName.addKeyListener {
            if (it.eventType == KeyEvent.KeyEventType.Release) {
                spell.name = spellName.getText()
            }
        }
        this.add(spellName)

        // Add a scroll bar on the left to scroll through spell stages
        scrollBar = VScrollBar(Dimensions(0.08, 0.6))
        scrollBar.offset = Position(0.05, 0.3)
        this.add(scrollBar)

        // Make the spell stage panel
        spellStagePanel = StackPane(Dimensions(0.6, 0.6))
        spellStagePanel.offset = Position(0.15, 0.3)
        this.add(spellStagePanel)

        // Add the background for the spell stages
        spellStageBackground =
            ImagePane("afraidofthedark:textures/gui/spell_editor/spell_stage_panel_background.png", DispMode.STRETCH)
        spellStageBackground.prefSize = Dimensions(1.0, 1.0)
        spellStagePanel.add(spellStageBackground)

        // Add the spell stage scroll panel
        spellStageList = ListPane(ListPane.ExpandDirection.DOWN, scrollBar)
        spellStageList.padding = Spacing(0.02)
        spellStagePanel.add(spellStageList)

        // Create add and remove buttons for spell stages
        addButton = ButtonPane(
            icon = ImagePane("afraidofthedark:textures/gui/spell_editor/add.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/add_hovered.png"),
            prefSize = Dimensions(0.45, 1.0)
        )
        addButton.addOnClick {
            val newStage = SpellStage()
            spell.spellStages.add(newStage)
            addGuiSpellStage(newStage)
            invalidate()
            addButton.isHovered = false // Button moves, so un-hover it
        }
        removeButton = ButtonPane(
            icon = ImagePane("afraidofthedark:textures/gui/spell_editor/delete.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/delete_hovered.png"),
            prefSize = Dimensions(0.45, 1.0)
        )
        removeButton.addOnClick {
            if (spell.spellStages.size > 1) spell.spellStages.removeLast()
            removeLastGuiSpellStage()
            invalidate()
            removeButton.isHovered = false // Button moves, so un-hover it
        }
        buttonLayout = HChainPane()
        buttonLayout.prefSize = Dimensions(0.25, 0.08)
        buttonLayout.add(addButton)
        buttonLayout.add(removeButton)
        spellStageList.add(buttonLayout)

        // Create a save spell button
        val saveButton =
            ButtonPane(
                icon = ImagePane("afraidofthedark:textures/gui/spell_editor/save.png"),
                iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/save_hovered.png"),
                prefSize = Dimensions(0.13, 0.1),
                offset = Position(0.76, 0.4)
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
            ButtonPane(
                icon = ImagePane("afraidofthedark:textures/gui/spell_editor/delete.png"),
                iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/delete_hovered.png"),
                prefSize = Dimensions(0.13, 0.1),
                offset = Position(0.76, 0.52)
            )
        closeButton.setHoverText("Exit (Without Saving)")
        closeButton.addOnClick {
            // Open the list gui without saving
            Minecraft.getInstance().displayGuiScreen(SpellListScreen())
        }
        this.add(closeButton)

        // Create a help button
        val helpButton = ButtonPane(
            icon = ImagePane("afraidofthedark:textures/gui/spell_editor/question.png"),
            iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/question_hovered.png"),
            prefSize = Dimensions(0.13, 0.1),
            offset = Position(0.76, 0.64)
        )
        helpButton.setHoverText("Help")
        // When pressing help execute our on help runnable
        helpButton.addOnClick { onHelp?.invoke() }
        this.add(helpButton)

        // Create the power source spell slot
        uiPowerSource = SpellPowerSourceSlot(Position(0.57, 0.07), Dimensions(0.13, 0.1), spell)
        uiPowerSource.setSpellComponent(spell.powerSource)
        this.add(uiPowerSource)

        // Add the spell cost label
        spellCost = LabelComponent(ClientData.getOrCreate(36f), Dimensions(0.7, 0.13))
        spellCost.offset = Position(0.15, 0.87)
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

    private fun refreshCost() {
        spellCost.text = "Cost: ${spell.getCost().roundToInt()}"
    }

    /**
     * Adds a gui spell stage to the UI
     *
     * @param spellStage The stage to create a UI component for
     */
    private fun addGuiSpellStage(spellStage: SpellStage) {
        // Create the spell stage GUI
        val spellStageGui = GuiSpellStage(spellStage, spell, componentEditCallback)
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

    override fun calcChildrenBounds() {
        super.calcChildrenBounds()
        // Safe call is necessary here because calcChildrenBounds gets called in the constructor before the label is added
        spellCost?.let { this.refreshCost() }   // Refresh the cost every UI update
    }

    /**
     * @return True if the inventory key should currently close the UI, false otherwise
     */
    fun inventoryKeyClosesUI(): Boolean {
        return !spellName.isFocused
    }
}