package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectInstance
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceInstance
import com.davidm1a2.afraidofthedark.common.utility.openGui
import net.minecraft.util.text.TextComponentTranslation
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
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    private val spell: Spell,
    private val selectedComponentGetter: () -> AOTDGuiSpellComponentSlot<*>?,
    private val clearSelectedComponent: () -> Unit
) : AOTDGuiContainer(x, y, width, height) {
    private val spellName: AOTDGuiTextField
    private val spellStagePanel: AOTDGuiScrollPanel
    private val uiSpellStages: MutableList<AOTDGuiSpellStage> = mutableListOf()
    private val uiPowerSource: AOTDGuiSpellPowerSourceSlot
    private val spellCost: AOTDGuiLabel
    var onHelp: (() -> Unit)? = null
    var componentEditCallback: ((AOTDGuiSpellComponentSlot<*>) -> Unit)? = null

    init {
        // A base panel to contain all tablet gui controls
        val tablet = AOTDGuiPanel(0, 0, width, height, false)
        // Create the background image
        val backgroundImage =
            AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spell_editor/tablet_background.png")
        tablet.add(backgroundImage)

        // Setup the spell name label
        spellName = AOTDGuiTextField(60, 30, 85, 25, ClientData.getOrCreate(36f))
        spellName.setGhostText("Spell Name")
        // When we type into this slot set the spell name
        spellName.addKeyListener {
            if (it.eventType == AOTDKeyEvent.KeyEventType.Type) {
                spell.name = spellName.getText()
            }
        }
        tablet.add(spellName)

        // Add a scroll bar on the left to scroll through spell stages
        val scrollBar = AOTDGuiScrollBar(10, 75, 15, 170)
        tablet.add(scrollBar)

        // Add the background for the spell stages
        val spellStageBackground =
            AOTDGuiImage(30, 55, 120, 170, "afraidofthedark:textures/gui/spell_editor/spell_stage_panel_background.png")
        tablet.add(spellStageBackground)

        // Add the spell stage container scroll panel
        spellStagePanel = AOTDGuiScrollPanel(30, 55, 120, 170, true, scrollBar)
        tablet.add(spellStagePanel)

        // Create a save spell button
        val saveButton =
            AOTDGuiButton(
                152,
                105,
                20,
                20,
                "afraidofthedark:textures/gui/spell_editor/save.png",
                "afraidofthedark:textures/gui/spell_editor/save_hovered.png"
            )
        saveButton.setHoverText("Save Spell")
        saveButton.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Press) {
                // When we press the save spell button and it's hovered save the spell and send changes to server
                if (it.source.isVisible && it.source.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    // Grab the player's spell manager
                    val spellManager = entityPlayer.getSpellManager()
                    // Clone the spell so we don't modify it further
                    val spellClone = Spell(spell.serializeNBT())
                    // Update the spell
                    spellManager.addOrUpdateSpell(spellClone)
                    // Sync the spell server side
                    spellManager.sync(entityPlayer, spellClone)
                    // Tell the player the save was successful
                    entityPlayer.sendMessage(
                        TextComponentTranslation(
                            "message.afraidofthedark:spell.save_successful",
                            spellClone.name
                        )
                    )
                }
            }
        }
        saveButton.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                // When hovering the button play the hover sound
                if (it.source.isHovered && it.source.isVisible) {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f)
                }
            }
        }
        tablet.add(saveButton)

        // Create a close UI and don't save button
        val closeButton =
            AOTDGuiButton(
                152,
                130,
                20,
                20,
                "afraidofthedark:textures/gui/spell_editor/delete.png",
                "afraidofthedark:textures/gui/spell_editor/delete_hovered.png"
            )
        closeButton.setHoverText("Exit without saving")
        // When we click the close button show the spell list
        closeButton.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Press) {
                // Ensure the button is visible and hovered
                if (closeButton.isVisible && closeButton.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    // Open the list gui without saving
                    entityPlayer.openGui(AOTDGuiHandler.SPELL_LIST_ID)
                }
            }
        }
        closeButton.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                // Play the hover sound effect if the button is visible
                if (closeButton.isHovered && closeButton.isVisible) {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f)
                }
            }
        }
        tablet.add(closeButton)

        // Create a help button
        val helpButton = AOTDGuiButton(
            152,
            180,
            20,
            20,
            "afraidofthedark:textures/gui/spell_editor/question.png",
            "afraidofthedark:textures/gui/spell_editor/question_hovered.png"
        )
        helpButton.setHoverText("Help")
        // When pressing help execute our on help runnable
        helpButton.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Press) {
                // Ensure the button is visible, hovered, and the callback is non-null
                if (helpButton.isVisible && helpButton.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    onHelp?.invoke()
                }
            }
        }
        helpButton.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                // Play the hover sound effect if the button is visible
                if (it.source.isHovered && it.source.isVisible) {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f)
                }
            }
        }
        tablet.add(helpButton)

        // Create the power source spell slot
        uiPowerSource = AOTDGuiSpellPowerSourceSlot(152, 155, 20, 20)
        uiPowerSource.setSpellComponent(spell.powerSource)
        // When we click the power source check the selected component, if it's a power source perform additional updates
        uiPowerSource.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Release) {
                // Test if we're hovering over the component...
                if (uiPowerSource.isHovered && uiPowerSource.isVisible) {
                    // If we left click update the power source
                    if (it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                        // Grab the selected component
                        val selectedComponent = selectedComponentGetter()
                        // If it's a spell power slot update the spell's power source
                        if (selectedComponent is AOTDGuiSpellPowerSourceSlot) {
                            // Grab the selected power source
                            // Unhighlight the power source UI element
                            uiPowerSource.setHighlight(false)
                            // Create a new instance of the selected power source
                            val spellPowerSource = SpellPowerSourceInstance(selectedComponent.getComponentType()!!)
                            spellPowerSource.setDefaults()
                            // Update the slot and spell
                            spell.powerSource = spellPowerSource
                            uiPowerSource.setSpellComponent(spellPowerSource)
                            // Clear the selected component
                            clearSelectedComponent()
                        } else if (selectedComponent == null && uiPowerSource.getComponentType() != null) {
                            componentEditCallback?.invoke(uiPowerSource)
                        }
                    } else if (it.clickedButton == AOTDMouseEvent.RIGHT_MOUSE_BUTTON) {
                        spell.powerSource = null
                        uiPowerSource.setSpellComponent(null)
                    }
                    // Update cost
                    refreshCost()
                }
            }
        }
        uiPowerSource.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                // When we hover the power source with a selected component "in hand" highlight it
                val selectedComponent = selectedComponentGetter()
                if (selectedComponent is AOTDGuiSpellPowerSourceSlot) {
                    uiPowerSource.setHighlight(true)
                }
            } else if (it.eventType == AOTDMouseMoveEvent.EventType.Exit) {
                // When we unhover the power source with a selected component "in hand" de-highlight it
                val spellComponentSlot = selectedComponentGetter()
                if (spellComponentSlot is AOTDGuiSpellPowerSourceSlot) {
                    uiPowerSource.setHighlight(false)
                }
            }
        }
        tablet.add(uiPowerSource)
        // Add the spell cost label
        spellCost = AOTDGuiLabel(30, 225, 120, 20, ClientData.getOrCreate(32f))
        tablet.add(spellCost)
        // Update all the gui components from our spell
        refresh()
        add(tablet)
    }

    /**
     * Refreshes the state of the tablet based on the current spell
     */
    private fun refresh() {
        // Update the spell cost label
        refreshCost()
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
        // Update the amount of scroll based on the number of spell stages that exist
        updateScrollOffset()
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
        // Create the GUI spell stage
        val nextSpellStage = AOTDGuiSpellStage(5, 5 + uiSpellStages.size * 35, 110, 45, spellStage)
        // If we click add then add a spell stage and refresh this UI component
        nextSpellStage.addRunnable =
            {
                spell.spellStages.add(SpellStage())
                refresh()
            }
        // If we click remove then the last spell stage and refresh this UI component
        nextSpellStage.removeRunnable =
            {
                spell.spellStages.removeAt(spell.spellStages.size - 1)
                refresh()
            }

        // When we click the delivery method check the selected component, if it's a delivery method perform additional updates
        val uiDeliveryMethod = nextSpellStage.deliveryMethod
        uiDeliveryMethod.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Release) {
                // Test if we're hovering over the component...
                if (uiDeliveryMethod.isHovered && uiDeliveryMethod.isVisible) {
                    // If we left click update the delivery method
                    if (it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                        // Grab the selected component
                        val selectedComponent = selectedComponentGetter()
                        // If it's a delivery method slot update the spell stage's delivery method
                        if (selectedComponent is AOTDGuiSpellDeliveryMethodSlot) {
                            // Grab the selected delivery method
                            // Unhighlight the delivery method UI element
                            uiDeliveryMethod.setHighlight(false)
                            // Create a new instance of the selected delivery method
                            val spellDeliveryMethod =
                                SpellDeliveryMethodInstance(selectedComponent.getComponentType()!!)
                            spellDeliveryMethod.setDefaults()
                            // Update the slot and spell
                            spellStage.deliveryInstance = spellDeliveryMethod
                            uiDeliveryMethod.setSpellComponent(spellDeliveryMethod)
                            // Clear the selected component
                            clearSelectedComponent()
                        } else if (selectedComponent == null && uiDeliveryMethod.getComponentType() != null) {
                            componentEditCallback?.invoke(uiDeliveryMethod)
                        }
                    } else if (it.clickedButton == AOTDMouseEvent.RIGHT_MOUSE_BUTTON) {
                        spellStage.deliveryInstance = null
                        uiDeliveryMethod.setSpellComponent(null)
                    }
                    // Update cost
                    refreshCost()
                }
            }
        }
        uiDeliveryMethod.addMouseMoveListener {
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                // When we hover the delivery method with a selected component "in hand" highlight it
                val selectedComponent = selectedComponentGetter()
                if (selectedComponent is AOTDGuiSpellDeliveryMethodSlot) {
                    uiDeliveryMethod.setHighlight(true)
                }
            } else if (it.eventType == AOTDMouseMoveEvent.EventType.Exit) {
                // When we unhover the delivery method with a selected component "in hand" de-highlight it
                val spellComponentSlot = selectedComponentGetter()
                if (spellComponentSlot is AOTDGuiSpellDeliveryMethodSlot) {
                    uiDeliveryMethod.setHighlight(false)
                }
            }
        }

        // When we click the any of the effect slots check the selected component, if it's an effect perform additional updates
        for (i in nextSpellStage.effects.indices) {
            val uiEffect = nextSpellStage.effects[i]
            uiEffect.addMouseListener {
                if (it.eventType == AOTDMouseEvent.EventType.Release) {
                    // Test if we're hovering over the component...
                    if (uiEffect.isHovered && uiEffect.isVisible) {
                        // If we left click update the effect
                        if (it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                            // Grab the selected component
                            val selectedComponent = selectedComponentGetter()
                            // If it's an effect slot update the spell stage's effect method
                            if (selectedComponent is AOTDGuiSpellEffectSlot) {
                                // Grab the selected effect
                                // Unhighlight the delivery method UI element
                                uiEffect.setHighlight(false)
                                // Create a new instance of the selected effect
                                val spellEffect = SpellEffectInstance(selectedComponent.getComponentType()!!)
                                spellEffect.setDefaults()
                                // Update the slot and spell
                                spellStage.effects[i] = spellEffect
                                uiEffect.setSpellComponent(spellEffect)
                                // Clear the selected component
                                clearSelectedComponent()
                            } else if (selectedComponent == null && uiEffect.getComponentType() != null) {
                                componentEditCallback?.invoke(uiEffect)
                            }
                        } else if (it.clickedButton == AOTDMouseEvent.RIGHT_MOUSE_BUTTON) {
                            spellStage.effects[i] = null
                            uiEffect.setSpellComponent(null)
                        }
                        // Update cost
                        refreshCost()
                    }
                }
            }
            uiEffect.addMouseMoveListener {
                if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                    // When we hover the effect with a selected component "in hand" highlight it
                    val selectedComponent = selectedComponentGetter()
                    if (selectedComponent is AOTDGuiSpellEffectSlot) {
                        uiEffect.setHighlight(true)
                    }
                } else if (it.eventType == AOTDMouseMoveEvent.EventType.Exit) {
                    // When we unhover the effect with a selected component "in hand" de-highlight it
                    val spellComponentSlot = selectedComponentGetter()
                    if (spellComponentSlot is AOTDGuiSpellEffectSlot) {
                        uiEffect.setHighlight(false)
                    }
                }
            }
        }

        // Add the spell stage to the panel
        spellStagePanel.add(nextSpellStage)
        // Add the spell stage to the stage list
        uiSpellStages.add(nextSpellStage)

        // If only 1 spell stage exists hide the delete button
        if (uiSpellStages.size == 1) {
            nextSpellStage.hideMinus()
        } else {
            for (i in 0 until uiSpellStages.size - 1) {
                val otherSpellStage = uiSpellStages[i]
                otherSpellStage.hideMinus()
                otherSpellStage.hidePlus()
            }
        }
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
            spellStagePanel.remove(lastStage)
            uiSpellStages.remove(lastStage)

            // If any spell stages remain update the last one
            if (uiSpellStages.isNotEmpty()) {
                // Grab the new last spell stage
                val secondToLastStage = uiSpellStages[uiSpellStages.size - 1]
                // Always show the + button
                secondToLastStage.showPlus()
                // Show the minus if this isn't the last spell stage
                if (uiSpellStages.size != 1) {
                    secondToLastStage.showMinus()
                }
            }
        }
    }

    /**
     * Updates the scroll offset of the panel based on the number of spell stages
     */
    private fun updateScrollOffset() {
        spellStagePanel.maximumOffset = if (uiSpellStages.size > 4) (uiSpellStages.size - 4) * 35 else 0
    }

    /**
     * @return True if the inventory key should currently close the UI, false otherwise
     */
    fun inventoryKeyClosesUI(): Boolean {
        return !spellName.isFocused
    }
}