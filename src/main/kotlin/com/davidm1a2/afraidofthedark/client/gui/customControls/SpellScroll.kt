package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.FontCache
import com.davidm1a2.afraidofthedark.client.gui.PropertyEditorFactory
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.resources.ResourceLocation
import java.awt.Color

/**
 * Compliment control to the tablet, allows players to click spell components up
 */
class SpellScroll : StackPane() {

    private val componentScrollBar = VScrollBar(Dimensions(0.1, 1.0))
    private val propertyScrollBar = VScrollBar(Dimensions(0.1, 1.0))
    private val componentList: ListPane = ListPane(ListPane.ExpandDirection.DOWN, componentScrollBar)
    private val propertyList: ListPane = ListPane(ListPane.ExpandDirection.DOWN, propertyScrollBar)
    internal var componentPropModifiedCallback: () -> Unit = {}

    init {
        this.prefSize = Dimensions(0.32, 0.7)
        this.gravity = Gravity.CENTER_RIGHT
        this.offset = Position(-0.13, 0.0)

        // Add a scroll bar to the right of the scroll
        componentScrollBar.gravity = Gravity.CENTER_RIGHT
        propertyScrollBar.gravity = Gravity.CENTER_RIGHT
        this.add(componentScrollBar)
        this.add(propertyScrollBar)
        propertyScrollBar.isVisible = false

        // Set properties of the lists
        componentList.prefSize = Dimensions(0.9, 1.0)
        propertyList.prefSize = Dimensions(0.9, 1.0)

        // Add the two lists that may be displayed
        this.add(componentList)
        this.add(propertyList)
        propertyList.isVisible = false

        // Create the effect label
        val effectHeading = LabelComponent(FontCache.getOrCreate(42f), Dimensions(1.0, 0.2))
        effectHeading.textColor = Color(140, 35, 206)
        effectHeading.text = "Effects"
        componentList.add(effectHeading)

        // Go over all effects and add a slot for each
        var effectHPane: HChainPane? = null
        val availableEffects = ModRegistries.SPELL_EFFECTS.filter { it.shouldShowInSpellEditor(entityPlayer) }
        for ((effectIndex, effectEntry) in availableEffects.withIndex()) {
            if (effectIndex % COMPONENTS_PER_LINE == 0) {
                if (effectHPane != null) componentList.add(effectHPane)
                effectHPane = HChainPane(HChainPane.Layout.CLOSE)
                effectHPane.prefSize = Dimensions(1.0, 0.15)
            }
            val effect = SpellEffectIcon(effectEntry)
            effect.margins = Spacing(2.0, false)
            effectHPane?.add(effect)
        }
        effectHPane?.let { componentList.add(it) }

        // Create the delivery method label
        val deliveryMethodHeading =
            LabelComponent(FontCache.getOrCreate(42f), Dimensions(1.0, 0.2))
        deliveryMethodHeading.textColor = Color(140, 35, 206)
        deliveryMethodHeading.text = "Delivery Methods"
        componentList.add(deliveryMethodHeading)

        // Go over all delivery methods and add a slot for each
        var deliveryMethodHPane: HChainPane? = null
        val availableDeliveryMethods = ModRegistries.SPELL_DELIVERY_METHODS.filter { it.shouldShowInSpellEditor(entityPlayer) }
        for ((deliveryMethodIndex, deliveryMethodEntry) in availableDeliveryMethods.withIndex()) {
            if (deliveryMethodIndex % COMPONENTS_PER_LINE == 0) {
                if (deliveryMethodHPane != null) componentList.add(deliveryMethodHPane)
                deliveryMethodHPane = HChainPane(HChainPane.Layout.CLOSE)
                deliveryMethodHPane.prefSize = Dimensions(1.0, 0.15)
            }
            val deliveryMethod = SpellDeliveryMethodIcon(deliveryMethodEntry)
            deliveryMethod.margins = Spacing(2.0, false)
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
            val heading = StackPane(Dimensions(1.0, 0.2))
            val name = LabelComponent(FontCache.getOrCreate(32f), Dimensions(1.0, 1.0))
            name.textColor = purpleText
            // This cast is required even though IntelliJ doesn't agree
            @Suppress("USELESS_CAST")
            val spellComponent = componentInstance.component as SpellComponent<*>
            name.text = TranslatableComponent("tooltip.afraidofthedark.gui.spell_crafting.component_properties", spellComponent.getName()).string

            val closeEditor = ButtonPane(
                ImagePane(ResourceLocation("afraidofthedark:textures/gui/backward_button.png")),
                ImagePane(ResourceLocation("afraidofthedark:textures/gui/backward_button_hovered.png")),
                gravity = Gravity.TOP_RIGHT,
                prefSize = Dimensions(16.0, 16.0, false)
            )
            closeEditor.addOnClick {
                if (it.eventType == MouseEvent.EventType.Click && it.clickedButton == 0) {
                    setEditing(null)
                }
            }

            heading.add(name)
            heading.add(closeEditor)

            propertyList.add(heading)

            // Grab a list of editable properties
            val editableProperties = spellComponent.getEditableProperties()

            // If there are no editable properties say so with a text box
            if (editableProperties.isEmpty()) {
                val noPropsLine = TextBoxComponent(Dimensions(1.0, 0.1), FontCache.getOrCreate(26f))
                noPropsLine.textColor = purpleText
                noPropsLine.setText(TranslatableComponent("tooltip.afraidofthedark.gui.spell_crafting.no_component_properties").string)
                propertyList.add(noPropsLine)
            } else {
                // Go over each editable property and add an editor for it
                for (editableProp in editableProperties) {
                    // Create a label that states the name of the property
                    val propertyName = LabelComponent(FontCache.getOrCreate(26f), Dimensions(1.0, 0.1))
                    propertyName.textColor = purpleText
                    propertyName.text = "${editableProp.getName().string}:"
                    propertyList.add(propertyName)
                    propertyList.add(PropertyEditorFactory.build(componentInstance, editableProp, purpleText, componentPropModifiedCallback))
                }
            }
        }
        invalidate()
    }

    /**
     * @return True if the inventory key should currently close the UI, false otherwise
     */
    fun inventoryKeyClosesUI(): Boolean {
        return !anyChildTextFieldHovered(propertyList)
    }

    private fun anyChildTextFieldHovered(pane: AOTDPane): Boolean {
        val children = pane.getChildren()
        val focusedTextField = children.filterIsInstance<TextFieldPane>().firstOrNull(TextFieldPane::isFocused)
        if (focusedTextField != null) {
            return true
        }

        val subPanes = children.filterIsInstance<AOTDPane>()
        for (subPane in subPanes) {
            if (anyChildTextFieldHovered(subPane)) {
                return true
            }
        }
        return false
    }

    fun isEditingProps(): Boolean {
        return propertyList.isVisible
    }

    companion object {
        private const val COMPONENTS_PER_LINE = 4
    }
}
