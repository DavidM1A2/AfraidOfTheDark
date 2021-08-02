package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableConsumer
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.client.resources.I18n

/**
 * Class used to create an effect slot UI component
 *
 * @constructor Initializes the bounding box and spell delivery method
 * @param x           The X location of the top left corner
 * @param y           The Y location of the top left corner
 * @param width       The width of the component
 * @param height      The height of the component
 */
class SpellEffectSlot(offset: Position, prefSize: Dimensions, spell: Spell, val stageIndex: Int, val effectIndex: Int) :
    SpellComponentSlot<SpellEffect>("afraidofthedark:textures/gui/spell_editor/effect_holder.png", offset, prefSize, spell),
    DraggableConsumer<SpellEffect> {

    init {
        this.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click && it.clickedButton == MouseEvent.RIGHT_MOUSE_BUTTON) {
                if (this.isHovered && this.inBounds && this.isVisible) {
                    this.spell.spellStages[stageIndex].effects[effectIndex] = null
                }
            }
        }
    }

    override fun refreshHoverText() {
        // If the component type is non-null show the effect method and stats, otherwise show the slot is empty
        val componentType = this.getComponentType()
        if (componentType != null) {
            val componentInstance = this.getComponentInstance()!!
            this.hoverTexts = arrayOf(
                "Effect (${I18n.get(componentType.getUnlocalizedName())})",
                "Cost: %.1f".format(componentType.getCost(componentInstance))
            )
        } else {
            this.setHoverText("Empty effect slot")
        }
    }

    override fun consume(data: Any) {
        if (data is SpellEffect) {
            val inst = SpellComponentInstance(data)
            inst.setDefaults()
            this.setSpellComponent(inst)
            this.spell.spellStages[stageIndex].effects[effectIndex] = inst
            this.refreshHoverText()
        }
    }

    override fun calcChildrenBounds() {
        super.calcChildrenBounds()
        this.refreshHoverText() // Update hover text whenever the component is updated
    }
}
