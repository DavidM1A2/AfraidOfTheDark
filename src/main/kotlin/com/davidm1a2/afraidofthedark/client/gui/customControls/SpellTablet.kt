package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.FontCache
import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.HChainPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LabelComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ListPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextFieldPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.VScrollBar
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import net.minecraft.util.text.TranslationTextComponent

/**
 * Class representing the tablet used in the spell crafting gui on the left
 */
class SpellTablet(private val spell: Spell) : StackPane() {

    private val spellName: TextFieldPane
    private val spellStagePanel: StackPane
    private val spellStageList: ListPane
    private val uiSpellStages: MutableList<GuiSpellStage> = mutableListOf()
    private val spellCost: LabelComponent
    private val scrollBar: VScrollBar
    private val addButton: ButtonPane
    private val removeButton: ButtonPane
    private val buttonLayout: HChainPane
    internal var componentClickCallback: ((SpellComponentSlot<*>) -> Unit) = {}
        set(value) {
            uiSpellStages.forEach { it.componentEditCallback = value }
            field = value
        }

    init {
        this.prefSize = Dimensions(0.35, 0.8)
        this.gravity = Gravity.CENTER_LEFT
        this.offset = Position(0.1, 0.0)

        // Setup the spell name label
        spellName = TextFieldPane(Position(0.0, 0.1), Dimensions(0.75, 0.12), FontCache.getOrCreate(36f))
        spellName.setGhostText("Spell Name")
        // When we type into this slot set the spell name
        spellName.addKeyListener {
            if (it.eventType == KeyEvent.KeyEventType.Release) {
                spell.name = spellName.getText()
            }
        }
        this.add(spellName)

        // Add a scroll bar on the left to scroll through spell stages
        scrollBar = VScrollBar(Dimensions(0.08, 0.65))
        scrollBar.offset = Position(0.0, 0.25)
        this.add(scrollBar)

        // Make the spell stage panel
        spellStagePanel = StackPane(Dimensions(0.8, 0.65))
        spellStagePanel.offset = Position(0.1, 0.25)
        this.add(spellStagePanel)

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
        buttonLayout.prefSize = Dimensions(0.25, 0.1)
        buttonLayout.add(addButton)
        buttonLayout.add(removeButton)
        spellStageList.add(buttonLayout)

        // Add the spell cost label
        spellCost = LabelComponent(FontCache.getOrCreate(36f), Dimensions(1.0, 0.1))
        spellCost.offset = Position(0.0, 0.9)
        this.add(spellCost)

        // Update all the gui components from our spell
        refresh()
    }

    /**
     * Refreshes the state of the tablet based on the current spell
     */
    private fun refresh() {
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
        refreshCostLabel()
    }

    fun refreshCostLabel() {
        val selectedPowerSource = entityPlayer.getBasics().selectedPowerSource
        val costText = if (spell.isValid()) {
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost", selectedPowerSource.getFormattedCost(spell.getCost())).string
        } else {
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.unknown_cost").string
        }
        spellCost.text = costText
        uiSpellStages.forEach {
            it.deliveryMethod.refreshHoverText()
            it.effects.forEach { effect -> effect.refreshHoverText() }
        }
    }

    /**
     * Adds a gui spell stage to the UI
     *
     * @param spellStage The stage to create a UI component for
     */
    private fun addGuiSpellStage(spellStage: SpellStage) {
        // Create the spell stage GUI
        val spellStageGui = GuiSpellStage(spellStage, spell, this.componentClickCallback)
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
        spellCost?.let { this.refreshCostLabel() }   // Refresh the cost every UI update
    }

    /**
     * @return True if the inventory key should currently close the UI, false otherwise
     */
    fun inventoryKeyClosesUI(): Boolean {
        return !spellName.isFocused
    }
}