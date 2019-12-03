package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentProperty
import net.minecraft.client.resources.I18n
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.TextComponentTranslation
import org.apache.commons.lang3.tuple.Pair
import org.lwjgl.util.Color
import java.util.*
import java.util.function.Consumer
import kotlin.math.max

/**
 * Compliment control to the tablet, allows players to click spell components up
 *
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @property componentClickCallback The callback that will be fired when a spell component is selected
 * @property scrollPanel The scroll panel containing parts or the edit UI
 * @property componentScrollPanel The actual component scroll panel that holds a list of spell components
 * @property componentScrollPanelOffset The offset that the scroll panel should have when the component scroll panel is visible
 * @property currentPropEditors A List of any additional text fields we currently have editing properties
 */
class AOTDGuiSpellScroll(x: Int, y: Int, width: Int, height: Int) : AOTDGuiContainer(x, y, width, height)
{
    private var componentClickCallback: ((AOTDGuiSpellComponentSlot<*, *>) -> Unit) = { }
    private val scrollPanel: AOTDGuiScrollPanel
    private val componentScrollPanel: AOTDGuiPanel
    private val componentScrollPanelOffset: Int
    private val currentPropEditors = ArrayList<Pair<SpellComponentProperty, AOTDGuiTextField>>()

    init
    {
        // Create the base panel to attach all of our components to
        // The scroll that contains either a list of components or a component editor
        val scroll = AOTDGuiPanel(0, 0, width, height, false)

        // Add the background scroll texture image
        val backgroundScroll = AOTDGuiImage(0, 0, width - 20, height, "afraidofthedark:textures/gui/spell_editor/effect_list_scroll.png")
        scroll.add(backgroundScroll)

        // Add a scroll bar to the right of the scroll
        val componentScrollBar = AOTDGuiScrollBar(backgroundScroll.width, 50, 13, height - 100)
        scroll.add(componentScrollBar)

        // Add a scroll panel to the scroll
        scrollPanel = AOTDGuiScrollPanel(40, 50, 120, 175, true, componentScrollBar)
        scroll.add(scrollPanel)

        // Create a base panel for all components
        this.componentScrollPanel = AOTDGuiPanel(0, 0, 120, 175, false)

        // The current component index we're adding to the scroll
        val componentsPerLine = 5
        var currentComponent = 0

        // Create the power source label
        val powerSourceHeading =
                AOTDGuiLabel(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 120, 20, ClientData.getTargaMSHandFontSized(46f))
        powerSourceHeading.textColor = Color(140, 35, 206)
        powerSourceHeading.text = "Power Sources"
        this.componentScrollPanel.add(powerSourceHeading)
        currentComponent = currentComponent + componentsPerLine

        // Listener to be used by all of our spell components
        val componentClickListener: ((AOTDMouseEvent) -> Unit) =
                {
                    if (it.eventType === AOTDMouseEvent.EventType.Press)
                    {
                        // If the component is hovered fire the listener
                        if (it.source.isHovered && it.source.isVisible && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                        {
                            componentClickCallback(it.source as AOTDGuiSpellComponentSlot<*, *>)
                        }
                    }
                }

        // Go over all power sources and add a slot for each
        for (powerSourceEntry in ModRegistries.SPELL_POWER_SOURCES)
        {
            val powerSource = AOTDGuiSpellPowerSourceSlot(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 20, 20, powerSourceEntry)
            powerSource.addMouseListener(componentClickListener)
            this.componentScrollPanel.add(powerSource)
            currentComponent = currentComponent + 1
        }

        // Round the current component to a multiple of COMPONENTS_PER_LINE
        currentComponent = MathHelper.roundUp(currentComponent, componentsPerLine)

        // Create the effect label
        val effectHeading = AOTDGuiLabel(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 120, 20, ClientData.getTargaMSHandFontSized(46f))
        effectHeading.textColor = Color(140, 35, 206)
        effectHeading.text = "Effects"
        this.componentScrollPanel.add(effectHeading)
        currentComponent = currentComponent + componentsPerLine

        // Go over all effects and add a slot for each
        for (effectEntry in ModRegistries.SPELL_EFFECTS)
        {
            val effect = AOTDGuiSpellEffectSlot(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 20, 20, effectEntry)
            effect.addMouseListener(componentClickListener)
            this.componentScrollPanel.add(effect)
            currentComponent = currentComponent + 1
        }

        // Round the current component to a multiple of COMPONENTS_PER_LINE
        currentComponent = MathHelper.roundUp(currentComponent, componentsPerLine)

        // Create the delivery method label
        val deliveryMethodHeading =
                AOTDGuiLabel(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 120, 20, ClientData.getTargaMSHandFontSized(46f))
        deliveryMethodHeading.textColor = Color(140, 35, 206)
        deliveryMethodHeading.text = "Delivery Methods"
        this.componentScrollPanel.add(deliveryMethodHeading)
        currentComponent = currentComponent + componentsPerLine

        // Go over all delivery methods and add a slot for each
        for (deliveryMethodEntry in ModRegistries.SPELL_DELIVERY_METHODS)
        {
            val deliveryMethod = AOTDGuiSpellDeliveryMethodSlot(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 20, 20, deliveryMethodEntry)
            deliveryMethod.addMouseListener(componentClickListener)
            this.componentScrollPanel.add(deliveryMethod)
            currentComponent = currentComponent + 1
        }

        // Update the scroll offset accordingly
        var offset = currentComponent / 6 - 5
        offset = if (offset > 0) offset else 0
        this.componentScrollPanelOffset = offset * 30
        this.scrollPanel.maximumOffset = this.componentScrollPanelOffset
        this.scrollPanel.add(componentScrollPanel)

        // Add the scroll to the component
        this.add(scroll)
    }

    /**
     * Called when a component is clicked
     *
     * @param componentClickCallback The callback that to fire
     */
    fun setComponentClickCallback(componentClickCallback: (AOTDGuiSpellComponentSlot<*, *>) -> Unit)
    {
        this.componentClickCallback = componentClickCallback
    }

    /**
     * Sets the current spell component to be edited, or null if no component is being edited
     *
     * @param spellComponent The spell component to edit, or null to clear it
     */
    fun setEditing(spellComponent: SpellComponent?)
    {
        // Clear the current list of prop editors
        currentPropEditors.clear()
        // If this is null then clear the currently edited spell
        if (spellComponent == null)
        {
            // Remove all nodes from the scroll panel
            this.scrollPanel.children.forEach(Consumer { this.scrollPanel.remove(it) })
            // Add the component scroll panel back in
            this.scrollPanel.add(this.componentScrollPanel)
            // Reset the maximum offset
            this.scrollPanel.maximumOffset = this.componentScrollPanelOffset
        }
        else
        {
            // Create a panel to hold all of our controls
            val editPanel = AOTDGuiPanel(0, 0, 120, 175, false)
            // Start at y=0, or the "top" of the scroll
            var currentY = 0

            // Purple text color
            val purpleText = Color(140, 35, 206)

            // Create a heading label to indicate what is currently being edited
            val heading = AOTDGuiLabel(0, currentY, 120, 30, ClientData.getTargaMSHandFontSized(32f))
            heading.textColor = purpleText
            heading.text = I18n.format(spellComponent.entityTypeUnlocalizedName) + " Properties"
            editPanel.add(heading)
            currentY = currentY + heading.height

            // Grab a list of editable properties
            val editableProperties = spellComponent.editableProperties

            // If there are no editable properties say so with a text box
            if (editableProperties.isEmpty())
            {
                val noPropsLine = AOTDGuiTextBox(0, currentY, 120, 30, ClientData.getTargaMSHandFontSized(26f))
                noPropsLine.textColor = purpleText
                noPropsLine.text = "This component has no editable properties."
                editPanel.add(noPropsLine)
                currentY = currentY + noPropsLine.height
            }
            else
            {
                // Go over each editable property and add an editor for it
                for (editableProp in editableProperties)
                {
                    // Create a label that states the name of the property
                    val propertyName = AOTDGuiLabel(0, currentY, 120, 15, ClientData.getTargaMSHandFontSized(26f))
                    propertyName.textColor = purpleText
                    propertyName.text = "Name: " + editableProp.name
                    editPanel.add(propertyName)
                    currentY = currentY + propertyName.height
                    // Create a text box that shows the description of the property
                    val propertyDescription = AOTDGuiTextBox(0, currentY, 120, 12, ClientData.getTargaMSHandFontSized(26f))
                    propertyDescription.textColor = purpleText
                    propertyDescription.text = "Description: " + editableProp.description
                    // While we don't have enough room for the description increase the size by a constant
                    while (propertyDescription.overflowText.isNotEmpty())
                    {
                        propertyDescription.height = propertyDescription.height + 12
                        propertyDescription.text = "Description: " + editableProp.description
                    }
                    editPanel.add(propertyDescription)
                    currentY = currentY + propertyDescription.height
                    // Create a text field that edits the property value
                    val propertyEditor = AOTDGuiTextField(0, currentY, 120, 30, ClientData.getTargaMSHandFontSized(26f))
                    propertyEditor.textColor = purpleText
                    propertyEditor.text = editableProp.getter.get()
                    editPanel.add(propertyEditor)
                    // Store the editor off for later use
                    this.currentPropEditors.add(Pair.of(editableProp, propertyEditor))
                    currentY = currentY + propertyEditor.height
                }
            }

            // If we have any editable properties show the save button
            if (editableProperties.isNotEmpty())
            {
                // Add a save button at the bottom if we have any editable properties
                val save = AOTDGuiButton(
                        0,
                        currentY + 5,
                        50,
                        20,
                        "afraidofthedark:textures/gui/spell_editor/button.png",
                        "afraidofthedark:textures/gui/spell_editor/button_hovered.png",
                        ClientData.getTargaMSHandFontSized(32f)
                )
                save.setTextAlignment(TextAlignment.ALIGN_CENTER)
                save.setText("Save")
                save.addMouseListener()
                {
                    if (it.eventType === AOTDMouseEvent.EventType.Press)
                    {
                        if (save.isVisible && save.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                        {
                            // Go over all properties and their editors
                            for (propEditorPair in currentPropEditors)
                            {
                                // Grab the property and editor
                                val property = propEditorPair.left
                                val editor = propEditorPair.right
                                // Attempt to set the property
                                try
                                {
                                    property.setter.accept(editor.text)
                                }
                                catch (e: InvalidValueException)
                                {
                                    entityPlayer.sendMessage(
                                            TextComponentTranslation(
                                                    "aotd.spell.property_edit_fail",
                                                    propEditorPair.key.name,
                                                    e.message
                                            )
                                    )
                                }
                                // If we get an exception tell the player what went wrong
                            }
                            // Clear the editor
                            setEditing(null)
                        }
                    }
                }
                // When we hover the button play the hover sound
                save.addMouseMoveListener()
                {
                    if (it.eventType == AOTDMouseMoveEvent.EventType.Enter)
                    {
                        if (save.isVisible)
                        {
                            entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.7f, 1.7f)
                        }
                    }
                }
                editPanel.add(save)
            }

            // Add a cancel button at the bottom. Center it if we have no edit properties (and no save button!)
            val cancelX = if (editableProperties.isEmpty()) editPanel.width / 2 - 25 else editPanel.width - 50
            val cancel = AOTDGuiButton(
                    cancelX,
                    currentY + 5,
                    50,
                    20,
                    "afraidofthedark:textures/gui/spell_editor/button.png",
                    "afraidofthedark:textures/gui/spell_editor/button_hovered.png",
                    ClientData.getTargaMSHandFontSized(32f)
            )
            cancel.setTextAlignment(TextAlignment.ALIGN_CENTER)
            cancel.setText(if (editableProperties.isEmpty()) "Close" else "Cancel")
            cancel.addMouseListener()
            {
                if (it.eventType == AOTDMouseEvent.EventType.Press)
                {
                    if (cancel.isVisible && cancel.isHovered && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                    {
                        // Clear the currently edited spell
                        setEditing(null)
                    }
                }
            }
            // When we hover the button play the hover sound
            cancel.addMouseMoveListener()
            {
                if (it.eventType == AOTDMouseMoveEvent.EventType.Enter)
                {
                    if (cancel.isVisible)
                    {
                        entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.7f, 1.7f)
                    }
                }
            }
            editPanel.add(cancel)
            currentY = currentY + cancel.height + 5

            // Remove all nodes from the scroll panel
            this.scrollPanel.children.forEach { this.scrollPanel.remove(it) }
            // Add in the edit panel
            this.scrollPanel.add(editPanel)
            // Update the scroll offset
            this.scrollPanel.maximumOffset = max(0, currentY - editPanel.height)
        }
    }

    /**
     * @return True if the inventory key should currently close the UI, false otherwise
     */
    fun inventoryKeyClosesUI(): Boolean
    {
        return currentPropEditors.stream().map { it.right }.noneMatch { it.isFocused }
    }
}
