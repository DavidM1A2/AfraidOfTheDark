package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.FontCache
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.LabelComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.StackPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.TextBoxComponent
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentBase
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import java.awt.Color

/**
 * Advanced control that displays a journal page spell component
 */
class SpellComponentPane(prefSize: Dimensions) : AOTDPane(prefSize = prefSize) {
    private val componentSlot: ImagePane
    private val componentSocket: ImagePane
    private val title: LabelComponent
    private val largeText: TextBoxComponent
    private val smallText: TextBoxComponent

    init {
        title = LabelComponent(FontCache.getOrCreate(55f), Dimensions(1.0, 0.1))
        title.textAlignment = TextAlignment.ALIGN_CENTER
        title.textColor = Color(135, 70, 44)
        title.gravity = Gravity.TOP_CENTER
        add(title)

        val componentHolder = StackPane(Dimensions(1.0, 0.4))
        componentHolder.gravity = Gravity.TOP_CENTER
        componentHolder.padding = Spacing(20.0, 0.0, 0.0, 0.0, isRelative = false)
        componentSocket = ImagePane(displayMode = ImagePane.DispMode.FIT_TO_PARENT)
        componentSocket.gravity = Gravity.CENTER
        componentSlot = ImagePane(displayMode = ImagePane.DispMode.FIT_TO_PARENT)
        componentSlot.gravity = Gravity.CENTER
        componentSlot.prefSize = Dimensions(0.42, 0.42)
        componentHolder.add(componentSocket)
        componentHolder.add(componentSlot)
        add(componentHolder)

        largeText = TextBoxComponent(Dimensions(1.0, 0.6), font = FontCache.getOrCreate(38f))
        largeText.textColor = Color(135, 70, 44)
        largeText.gravity = Gravity.BOTTOM_CENTER
        add(largeText)
        smallText = TextBoxComponent(Dimensions(1.0, 0.58), font = FontCache.getOrCreate(26f))
        smallText.textColor = Color(135, 70, 44)
        smallText.gravity = Gravity.BOTTOM_CENTER
        add(smallText)
    }

    fun setComponent(spellComponent: SpellComponentBase<*>?) {
        if (spellComponent != null) {
            componentSlot.updateImageTexture(spellComponent.icon)
            when (spellComponent) {
                is SpellPowerSource<*> -> {
                    componentSlot.setHoverText(TranslationTextComponent("tooltip.afraidofthedark.gui.journal_page.power_source", spellComponent.getName()).string)
                    componentSocket.updateImageTexture(POWER_SOURCE_SOCKET_TEXTURE)
                }
                is SpellDeliveryMethod -> {
                    componentSlot.setHoverText(TranslationTextComponent("tooltip.afraidofthedark.gui.journal_page.delivery_method", spellComponent.getName()).string)
                    componentSocket.updateImageTexture(DELIVERY_METHOD_SOCKET_TEXTURE)
                }
                else -> {
                    componentSlot.setHoverText(TranslationTextComponent("tooltip.afraidofthedark.gui.journal_page.effect", spellComponent.getName()).string)
                    componentSocket.updateImageTexture(EFFECT_SOCKET_TEXTURE)
                }
            }
            title.text = spellComponent.getName().string
            val textBody = buildString {
                append(spellComponent.getDescription().string)
                // SpellComponentBase's don't have editable properties (eg: PowerSources)
                if (spellComponent is SpellComponent<*>) {
                    val editableProperties = spellComponent.getEditableProperties()
                    if (editableProperties.isNotEmpty()) {
                        appendLine()
                        appendLine(TranslationTextComponent("tooltip.afraidofthedark.gui.journal_page.editable_properties_header").string)
                        editableProperties.forEach {
                            appendLine(TranslationTextComponent("tooltip.afraidofthedark.gui.journal_page.editable_property", it.getName(), it.getDescription()).string)
                        }
                    }
                }
            }
            // Try using the larger text, if it doesn't fit use the smaller one
            largeText.setText(textBody)
            smallText.setText("")
            if (largeText.overflowText.isNotEmpty()) {
                largeText.setText("")
                smallText.setText(textBody)
            }
        }
        isVisible = spellComponent != null
    }

    /**
     * Called to draw the control, just draws all of its children
     */
    override fun draw(matrixStack: MatrixStack) {
        if (this.isVisible) {
            super.draw(matrixStack)
        }
    }

    companion object {
        private val POWER_SOURCE_SOCKET_TEXTURE = ResourceLocation(Constants.MOD_ID, "textures/gui/arcane_journal_page/power_source_socket.png")
        private val EFFECT_SOCKET_TEXTURE = ResourceLocation(Constants.MOD_ID, "textures/gui/arcane_journal_page/effect_socket.png")
        private val DELIVERY_METHOD_SOCKET_TEXTURE = ResourceLocation(Constants.MOD_ID, "textures/gui/arcane_journal_page/delivery_method_socket.png")
    }
}
