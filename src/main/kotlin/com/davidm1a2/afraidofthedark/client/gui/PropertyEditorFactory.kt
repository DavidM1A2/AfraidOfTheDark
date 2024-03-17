package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.*
import com.davidm1a2.afraidofthedark.common.spell.component.InvalidValueException
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.property.BooleanSpellComponentProperty
import com.davidm1a2.afraidofthedark.common.spell.component.property.EnumSpellComponentProperty
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentProperty
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.resources.ResourceLocation
import java.awt.Color

object PropertyEditorFactory {
    fun build(
        instance: SpellComponentInstance<*>,
        property: SpellComponentProperty<*>,
        textColor: Color,
        changeCallback: () -> Unit = {}
    ): AOTDGuiComponent {
        return when (property) {
            is EnumSpellComponentProperty<*> -> buildEnum(instance, property, changeCallback)
            is BooleanSpellComponentProperty -> buildBoolean(instance, property, changeCallback)
            else -> buildString(instance, property, textColor, changeCallback)
        }
    }

    private fun buildEnum(
        instance: SpellComponentInstance<*>,
        property: EnumSpellComponentProperty<*>,
        changeCallback: () -> Unit = {}
    ): AOTDGuiComponent {
        // Create a dropdown selector that edits the property value
        val propertyPane = StackPane(Dimensions(1.0, 0.1))

        val dropdownValues = property.values.map {
            it.name.lowercase().replaceFirstChar { char -> char.uppercase() }.replace("_", " ")
        }
        val selectedIndex = property.values.indexOfFirst {
            it.name.equals(property.getValue(instance), true)
        }
        val propertyEditor = DropdownPane(FontCache.getOrCreate(26f), dropdownValues, selectedIndex)
        propertyEditor.prefSize = Dimensions(0.8, 1.0)
        propertyEditor.gravity = Gravity.CENTER_LEFT
        propertyEditor.setChangeListener { _, newVal ->
            try {
                property.setValue(instance, property.values[newVal].name)
            } catch (e: InvalidValueException) {
                // Shouldn't be possible for enums
            }
            changeCallback()
        }

        val infoPane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/info.png"), ImagePane.DispMode.FIT_TO_PARENT).apply {
            gravity = Gravity.CENTER_RIGHT
            setHoverText(property.getDescription().string)
        }

        propertyPane.add(propertyEditor)
        propertyPane.add(infoPane)

        return propertyPane
    }

    private fun buildBoolean(
        instance: SpellComponentInstance<*>,
        property: BooleanSpellComponentProperty,
        changeCallback: () -> Unit = {}
    ): AOTDGuiComponent {
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
            setHoverText(property.getDescription().string)
        }
        propertyPane.add(ratioPane)
        propertyPane.add(infoPane)

        togglePane.toggled = property.getValue(instance).toBoolean()
        togglePane.toggleListener = { newVal ->
            property.setValue(instance, newVal.toString())
            changeCallback()
        }

        return propertyPane
    }

    private fun buildString(
        instance: SpellComponentInstance<*>,
        property: SpellComponentProperty<*>,
        textColor: Color,
        changeCallback: () -> Unit = {}
    ): AOTDGuiComponent {
        // Create a text field that edits the property value
        val propertyPane = StackPane(Dimensions(1.0, 0.1))
        val propertyError = ImagePane(
            "afraidofthedark:textures/gui/error.png",
            ImagePane.DispMode.FIT_TO_PARENT
        )
        propertyError.gravity = Gravity.CENTER_RIGHT
        propertyError.margins = Spacing(4.0, 4.0, 0.0, 5.0, false)
        propertyError.isVisible = false

        val propertyEditor =
            TextFieldPane(prefSize = Dimensions(0.8, 1.0), font = FontCache.getOrCreate(26f))
        propertyEditor.setTextColor(textColor)
        propertyEditor.setText(property.getValue(instance))
        propertyEditor.addTextChangeListener { _, newText ->
            try {
                property.setValue(instance, newText)
                propertyError.isVisible = false
            } catch (e: InvalidValueException) {
                propertyError.isVisible = true
                propertyError.setHoverText(TranslatableComponent("tooltip.afraidofthedark.gui.spell_crafting.component_property_error", e.reason).string)
            }
            changeCallback()
        }

        val infoPane = ImagePane(ResourceLocation("afraidofthedark:textures/gui/info.png"), ImagePane.DispMode.FIT_TO_PARENT).apply {
            gravity = Gravity.CENTER_RIGHT
            setHoverText(property.getDescription().string)
        }

        propertyEditor.add(propertyError)
        propertyPane.add(propertyEditor)
        propertyPane.add(infoPane)

        return propertyPane
    }
}