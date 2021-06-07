package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.base.*
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentProperty
import net.minecraft.client.resources.I18n
import net.minecraft.util.text.TranslationTextComponent
import org.apache.commons.lang3.tuple.Pair
import java.awt.Color

/**
 * Compliment control to the tablet, allows players to click spell components up
 *
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @property componentClickCallback The callback that will be fired when a spell component is selected
 * @property componentList The scroll panel containing parts or the edit UI
 * @property componentList The actual component scroll panel that holds a list of spell components
 * @property componentScrollPanelOffset The offset that the scroll panel should have when the component scroll panel is visible
 * @property currentPropEditors A List of any additional text fields we currently have editing properties
 */
class AOTDGuiSpellScroll() : ImagePane("afraidofthedark:textures/gui/spell_editor/effect_list_scroll.png", DispMode.FIT_TO_PARENT) {

    private var componentClickCallback: ((SpellComponentSlot<*>) -> Unit) = { }
    private val interiorPane: StackPane = StackPane()
    private val componentScrollBar = HScrollBar(RelativeDimensions(0.05, 1.0))
    private val propertyScrollBar = HScrollBar(RelativeDimensions(0.05, 1.0))
    private val componentList: ListPane = ListPane(ListPane.ExpandDirection.DOWN, componentScrollBar)
    private val propertyList: ListPane = ListPane(ListPane.ExpandDirection.DOWN, propertyScrollBar)
    private val currentPropEditors = mutableListOf<Pair<SpellComponentProperty, TextFieldPane>>()

    init {
        // Put everything that isn't the scroll bar into a padded pane
        interiorPane.padding = RelativeSpacing(0.2)
        this.add(interiorPane)

        // Add a scroll bar to the right of the scroll
        componentScrollBar.gravity = GuiGravity.CENTER_RIGHT
        propertyScrollBar.gravity = GuiGravity.CENTER_RIGHT
        this.add(componentScrollBar)
        this.add(propertyScrollBar)
        propertyScrollBar.isVisible = false

        // Add the two lists that may be displayed
        interiorPane.add(componentList)
        interiorPane.add(propertyList)
        propertyList.isVisible = false

        val componentsPerLine = 4

        // Create the power source label
        val powerSourceHeading =
            LabelComponent(ClientData.getOrCreate(42f), RelativeDimensions(1.0, 0.2))
        powerSourceHeading.textColor = Color(140, 35, 206)
        powerSourceHeading.text = "Power Sources"
        componentList.add(powerSourceHeading)

        // Go over all power sources and add a slot for each
        var powerSourceHPane: HPane? = null
        for ((powerSourceIndex, powerSourceEntry) in ModRegistries.SPELL_POWER_SOURCES.withIndex()) {
            if (powerSourceIndex % componentsPerLine == 0) {
                if (powerSourceHPane != null) componentList.add(powerSourceHPane)
                powerSourceHPane = HPane(HPane.Layout.CLOSE)
                powerSourceHPane.prefSize = RelativeDimensions(1.0, 0.2)
            }
            val powerSource = SpellPowerSourceIcon(powerSourceEntry)
            powerSource.margins = AbsoluteSpacing(2.0)
            powerSourceHPane?.add(powerSource)
        }
        powerSourceHPane?.let { componentList.add(it) }

        // Create the effect label
        val effectHeading = LabelComponent(ClientData.getOrCreate(42f), RelativeDimensions(1.0, 0.2))
        effectHeading.textColor = Color(140, 35, 206)
        effectHeading.text = "Effects"
        componentList.add(effectHeading)

        // Go over all effects and add a slot for each
        var effectHPane: HPane? = null
        for ((effectIndex, effectEntry) in ModRegistries.SPELL_EFFECTS.withIndex()) {
            if (effectIndex % componentsPerLine == 0) {
                if (effectHPane != null) componentList.add(effectHPane)
                effectHPane = HPane(HPane.Layout.CLOSE)
                effectHPane.prefSize = RelativeDimensions(1.0, 0.2)
            }
            val effect = SpellEffectIcon(effectEntry)
            effect.margins = AbsoluteSpacing(2.0)
            effectHPane?.add(effect)
        }
        effectHPane?.let { componentList.add(it) }

        // Create the delivery method label
        val deliveryMethodHeading =
            LabelComponent(ClientData.getOrCreate(42f), RelativeDimensions(1.0, 0.2))
        deliveryMethodHeading.textColor = Color(140, 35, 206)
        deliveryMethodHeading.text = "Delivery Methods"
        componentList.add(deliveryMethodHeading)

        // Go over all delivery methods and add a slot for each
        var deliveryMethodHPane: HPane? = null
        for ((deliveryMethodIndex, deliveryMethodEntry) in ModRegistries.SPELL_DELIVERY_METHODS.withIndex()) {
            if (deliveryMethodIndex % componentsPerLine == 0) {
                if (deliveryMethodHPane != null) componentList.add(deliveryMethodHPane)
                deliveryMethodHPane = HPane(HPane.Layout.CLOSE)
                deliveryMethodHPane.prefSize = RelativeDimensions(1.0, 0.2)
            }
            val deliveryMethod = SpellDeliveryMethodIcon(deliveryMethodEntry)
            deliveryMethod.margins = AbsoluteSpacing(2.0)
            deliveryMethodHPane?.add(deliveryMethod)
        }
        deliveryMethodHPane?.let { componentList.add(it) }
    }

    /**
     * Sets the current spell component to be edited, or null if no component is being edited
     *
     * @param componentInstance The spell component to edit, or null to clear it
     */
    fun setEditing(componentInstance: SpellComponentInstance<*>?) {
        // Clear the current list of prop editors
        currentPropEditors.clear()
        // If this is null then clear the currently edited spell
        if (componentInstance == null) {
            // Show the list of components
            componentList.isVisible = true
            propertyList.isVisible = false
            componentScrollBar.isVisible = true
            propertyScrollBar.isVisible = false
        } else {
            // Clear the existing list of properties
            propertyList.getChildren().forEach { propertyList.remove(it) }

            // Show the list of properties
            componentList.isVisible = false
            propertyList.isVisible = true
            componentScrollBar.isVisible = false
            propertyScrollBar.isVisible = true

            // Purple text color
            val purpleText = Color(140, 35, 206)

            // Create a heading label to indicate what is currently being edited
            val heading = LabelComponent(ClientData.getOrCreate(32f), RelativeDimensions(1.0, 0.2))
            heading.textColor = purpleText
            // This cast is required even though IntelliJ doesn't agree
            @Suppress("USELESS_CAST")
            val spellComponent = componentInstance.component as SpellComponent<*>
            heading.text = "${I18n.format(spellComponent.getUnlocalizedName())} Properties"
            propertyList.add(heading)

            // Grab a list of editable properties
            val editableProperties = spellComponent.getEditableProperties()

            // If there are no editable properties say so with a text box
            if (editableProperties.isEmpty()) {
                val noPropsLine = AOTDGuiTextBox(AbsoluteDimensions(120.0, 30.0), ClientData.getOrCreate(26f))
                noPropsLine.textColor = purpleText
                noPropsLine.setText("This component has no editable properties.")
                propertyList.add(noPropsLine)
            } else {
                // Go over each editable property and add an editor for it
                for (editableProp in editableProperties) {
                    // Create a label that states the name of the property
                    val propertyName = LabelComponent(ClientData.getOrCreate(26f), RelativeDimensions(1.0, 0.1))
                    propertyName.textColor = purpleText
                    propertyName.text = "Name: ${editableProp.name}"
                    propertyList.add(propertyName)

                    // Create a text box that shows the description of the property
                    val propertyDescription = AOTDGuiTextBox(AbsoluteDimensions(120.0, 36.0), ClientData.getOrCreate(26f))
                    propertyDescription.textColor = purpleText
                    propertyDescription.setText("Description: ${editableProp.description}")

                    propertyList.add(propertyDescription)

                    // Create a text field that edits the property value
                    val propertyEditor = TextFieldPane(prefSize = AbsoluteDimensions(120.0, 30.0), font = ClientData.getOrCreate(26f))
                    propertyEditor.setTextColor(purpleText)
                    propertyEditor.setText(editableProp.getter(componentInstance))
                    propertyList.add(propertyEditor)

                    // Store the editor off for later use
                    this.currentPropEditors.add(Pair.of(editableProp, propertyEditor))
                }
            }

            // If we have any editable properties show the save button
            if (editableProperties.isNotEmpty()) {
                // Add a save button at the bottom if we have any editable properties
                val save = Button(
                    icon = ImagePane("afraidofthedark:textures/gui/spell_editor/button.png"),
                    iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/button_hovered.png"),
                    prefSize = AbsoluteDimensions(50.0, 20.0),
                    font = ClientData.getOrCreate(32f)
                )
                save.setTextAlignment(TextAlignment.ALIGN_CENTER)
                save.setText("Save")
                save.addOnClick {
                    // Flag telling us at least one property was invalid
                    var onePropertyInvalid = false
                    // Go over all properties and their editors
                    for (propEditorPair in currentPropEditors) {
                        // Grab the property and editor
                        val property = propEditorPair.left
                        val editor = propEditorPair.right
                        // Attempt to set the property
                        try {
                            property.setter(componentInstance, editor.getText())
                        }
                        // If we get an exception tell the player what went wrong
                        catch (e: InvalidValueException) {
                            onePropertyInvalid = true
                            entityPlayer.sendMessage(
                                TranslationTextComponent("message.afraidofthedark.spell.property_edit_fail", propEditorPair.key.name, e.message)
                            )
                        }
                    }

                    // If no properties were invalid save successfully
                    if (!onePropertyInvalid) {
                        entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.spell.property_edit_success"))
                    }

                    // Clear the editor
                    setEditing(null)
                }
                // When we hover the button play the hover sound
                save.addMouseMoveListener {
                    if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                        if (save.isVisible) {
                            entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.7f, 1.7f)
                        }
                    }
                }
                propertyList.add(save)
            }

            // Add a cancel button at the bottom. Center it if we have no edit properties (and no save button!)
            val cancel = Button(
                icon = ImagePane("afraidofthedark:textures/gui/spell_editor/button.png"),
                iconHovered = ImagePane("afraidofthedark:textures/gui/spell_editor/button_hovered.png"),
                prefSize = AbsoluteDimensions(50.0, 20.0),
                font = ClientData.getOrCreate(32f)
            )
            cancel.setTextAlignment(TextAlignment.ALIGN_CENTER)
            cancel.setText(if (editableProperties.isEmpty()) "Close" else "Cancel")
            cancel.addOnClick {
                // Clear the currently edited spell
                setEditing(null)
            }
            // When we hover the button play the hover sound
            cancel.addMouseMoveListener {
                if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                    if (cancel.isVisible) {
                        entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.7f, 1.7f)
                    }
                }
            }
            propertyList.add(cancel)
        }
    }

    /**
     * @return True if the inventory key should currently close the UI, false otherwise
     */
    fun inventoryKeyClosesUI(): Boolean {
        return currentPropEditors.stream().map { it.right }.noneMatch { it.isFocused }
    }
}
