package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.FontCache
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.DropdownPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.HChainPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LabelComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ListPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.RatioPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextBoxComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextFieldPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TogglePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.VScrollBar
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.property.BooleanSpellComponentProperty
import com.davidm1a2.afraidofthedark.common.spell.component.property.EnumSpellComponentProperty
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentProperty
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import org.apache.commons.lang3.tuple.Pair
import java.awt.Color

/**
 * Compliment control to the tablet, allows players to click spell components up
 */
class SpellScroll : StackPane() {

    private val componentScrollBar = VScrollBar(Dimensions(0.1, 1.0))
    private val propertyScrollBar = VScrollBar(Dimensions(0.1, 1.0))
    private val componentList: ListPane = ListPane(ListPane.ExpandDirection.DOWN, componentScrollBar)
    private val propertyList: ListPane = ListPane(ListPane.ExpandDirection.DOWN, propertyScrollBar)
    private val currentPropEditors = mutableListOf<Pair<SpellComponentProperty<*>, AOTDPane>>()
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

        // Create the power source label
        val powerSourceHeading = LabelComponent(FontCache.getOrCreate(42f), Dimensions(1.0, 0.2))
        powerSourceHeading.textColor = Color(140, 35, 206)
        powerSourceHeading.text = "Power Sources"
        componentList.add(powerSourceHeading)

        val research = entityPlayer.getResearch()

        // Go over all power sources and add a slot for each
        var powerSourceHPane: HChainPane? = null
        val unlockedPowerSources = ModRegistries.SPELL_POWER_SOURCES.filter { it.prerequisiteResearch == null || research.isResearched(it.prerequisiteResearch) }
        for ((powerSourceIndex, powerSourceEntry) in unlockedPowerSources.withIndex()) {
            if (powerSourceIndex % COMPONENTS_PER_LINE == 0) {
                if (powerSourceHPane != null) componentList.add(powerSourceHPane)
                powerSourceHPane = HChainPane(HChainPane.Layout.CLOSE)
                powerSourceHPane.prefSize = Dimensions(1.0, 0.15)
            }
            val powerSource = SpellPowerSourceIcon(powerSourceEntry)
            powerSource.margins = Spacing(2.0, false)
            powerSourceHPane?.add(powerSource)
        }
        powerSourceHPane?.let { componentList.add(it) }

        // Create the effect label
        val effectHeading = LabelComponent(FontCache.getOrCreate(42f), Dimensions(1.0, 0.2))
        effectHeading.textColor = Color(140, 35, 206)
        effectHeading.text = "Effects"
        componentList.add(effectHeading)

        // Go over all effects and add a slot for each
        var effectHPane: HChainPane? = null
        val unlockedEffects = ModRegistries.SPELL_EFFECTS.filter { it.prerequisiteResearch == null || research.isResearched(it.prerequisiteResearch) }
        for ((effectIndex, effectEntry) in unlockedEffects.withIndex()) {
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
        val unlockedDeliveryMethods = ModRegistries.SPELL_DELIVERY_METHODS.filter { it.prerequisiteResearch == null || research.isResearched(it.prerequisiteResearch) }
        for ((deliveryMethodIndex, deliveryMethodEntry) in unlockedDeliveryMethods.withIndex()) {
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
            val heading = StackPane(Dimensions(1.0, 0.2))
            val name = LabelComponent(FontCache.getOrCreate(32f), Dimensions(1.0, 1.0))
            name.textColor = purpleText
            // This cast is required even though IntelliJ doesn't agree
            @Suppress("USELESS_CAST")
            val spellComponent = componentInstance.component as SpellComponent<*>
            name.text = TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.component_properties", spellComponent.getName()).string

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
                noPropsLine.setText(TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.no_component_properties").string)
                propertyList.add(noPropsLine)
            } else {
                // Go over each editable property and add an editor for it
                for (editableProp in editableProperties) {
                    // Create a label that states the name of the property
                    val propertyName = LabelComponent(FontCache.getOrCreate(26f), Dimensions(1.0, 0.1))
                    propertyName.textColor = purpleText
                    propertyName.text = "${editableProp.getName().string}:"
                    propertyList.add(propertyName)

                    if (editableProp is EnumSpellComponentProperty<*>) {
                        // Create a dropdown selector that edits the property value
                        val propertyPane = StackPane(Dimensions(1.0, 0.1))
                        val propertyError = ImagePane(
                            "afraidofthedark:textures/gui/error.png",
                            ImagePane.DispMode.FIT_TO_TEXTURE
                        )
                        propertyError.gravity = Gravity.CENTER_RIGHT
                        propertyError.margins = Spacing(0.0, 0.0, 0.0, 7.0, false)
                        propertyError.isVisible = false

                        val dropdownValues = editableProp.values.map {
                            it.name.lowercase().replaceFirstChar { char -> char.uppercase() }
                        }
                        val selectedIndex = editableProp.values.indexOfFirst {
                            it.name.equals(editableProp.getValue(componentInstance), true)
                        }
                        val propertyEditor = DropdownPane(FontCache.getOrCreate(26f), dropdownValues, selectedIndex)
                        propertyEditor.prefSize = Dimensions(0.8, 1.0)
                        propertyEditor.gravity = Gravity.CENTER_LEFT
                        propertyEditor.setChangeListener { _, newVal ->
                            try {
                                editableProp.setValue(componentInstance, editableProp.values[newVal].name)
                                propertyError.isVisible = false
                            } catch (e: InvalidValueException) {
                                propertyError.isVisible = true
                                propertyError.setHoverText(TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.component_property_error", e.reason).string)
                            }
                            componentPropModifiedCallback()
                        }

                        val infoPane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/info.png"), ImagePane.DispMode.FIT_TO_PARENT).apply {
                            gravity = Gravity.CENTER_RIGHT
                            setHoverText(editableProp.getDescription().string)
                        }

                        propertyEditor.add(propertyError)
                        propertyPane.add(propertyEditor)
                        propertyPane.add(infoPane)

                        propertyList.add(propertyPane)

                        // Store the editor off for later use
                        this.currentPropEditors.add(Pair.of(editableProp, propertyEditor))

                    } else if (editableProp is BooleanSpellComponentProperty) {
                        // Create a toggle control to represent the property
                        val propertyPane = StackPane(Dimensions(1.0, 0.1))
                        val togglePane = TogglePane(
                            ImagePane("afraidofthedark:textures/gui/toggle_bkg.png"),
                            toggleIcon = ImagePane("afraidofthedark:textures/gui/checkmark.png")
                        )
                        val ratioPane = RatioPane(1, 1)
                        ratioPane.add(togglePane)
                        val infoPane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/info.png"), ImagePane.DispMode.FIT_TO_PARENT).apply {
                            gravity = Gravity.CENTER_RIGHT
                            setHoverText(editableProp.getDescription().string)
                        }
                        propertyPane.add(ratioPane)
                        propertyPane.add(infoPane)

                        togglePane.toggled = editableProp.getValue(componentInstance).toBoolean()
                        togglePane.toggleListener = { newVal ->
                            editableProp.setValue(componentInstance, newVal.toString())
                            componentPropModifiedCallback()
                        }

                        propertyList.add(propertyPane)

                        // Store the editor off for later use
                        this.currentPropEditors.add(Pair.of(editableProp, togglePane))

                    } else {
                        // Create a text field that edits the property value
                        val propertyPane = StackPane(Dimensions(1.0, 0.1))
                        val propertyError = ImagePane(
                            "afraidofthedark:textures/gui/error.png",
                            ImagePane.DispMode.FIT_TO_TEXTURE
                        )
                        propertyError.gravity = Gravity.CENTER_RIGHT
                        propertyError.margins = Spacing(0.0, 0.0, 0.0, 7.0, false)
                        propertyError.isVisible = false

                        val propertyEditor =
                            TextFieldPane(prefSize = Dimensions(0.8, 1.0), font = FontCache.getOrCreate(26f))
                        propertyEditor.setTextColor(purpleText)
                        propertyEditor.setText(editableProp.getValue(componentInstance))
                        propertyEditor.addTextChangeListener { _, newText ->
                            try {
                                editableProp.setValue(componentInstance, newText)
                                propertyError.isVisible = false
                            } catch (e: InvalidValueException) {
                                propertyError.isVisible = true
                                propertyError.setHoverText(TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.component_property_error", e.reason).string)
                            }
                            componentPropModifiedCallback()
                        }

                        val infoPane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/info.png"), ImagePane.DispMode.FIT_TO_PARENT).apply {
                            gravity = Gravity.CENTER_RIGHT
                            setHoverText(editableProp.getDescription().string)
                        }

                        propertyEditor.add(propertyError)
                        propertyPane.add(propertyEditor)
                        propertyPane.add(infoPane)

                        propertyList.add(propertyPane)

                        // Store the editor off for later use
                        this.currentPropEditors.add(Pair.of(editableProp, propertyEditor))
                    }
                }
            }
        }
        invalidate()
    }

    /**
     * @return True if the inventory key should currently close the UI, false otherwise
     */
    fun inventoryKeyClosesUI(): Boolean {
        return currentPropEditors.stream().map { it.right }.noneMatch { it is TextFieldPane && it.isFocused }
    }

    fun isEditingProps(): Boolean {
        return propertyList.isVisible
    }

    companion object {
        private const val COMPONENTS_PER_LINE = 4
    }
}
