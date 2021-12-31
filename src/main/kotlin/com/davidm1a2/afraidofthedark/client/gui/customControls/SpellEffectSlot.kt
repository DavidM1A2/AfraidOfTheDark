package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableConsumer
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.util.text.TranslationTextComponent

/**
 * Class used to create an effect slot UI component
 */
class SpellEffectSlot(
    offset: Position,
    prefSize: Dimensions,
    spell: Spell,
    private val stageIndex: Int,
    private val effectIndex: Int
) : SpellComponentSlot<SpellEffect>("afraidofthedark:textures/gui/spell_editor/effect_holder.png", offset, prefSize, spell), DraggableConsumer<SpellEffect> {
    override fun updateSpell() {
        this.spell.spellStages[stageIndex].effects[effectIndex] = getComponentInstance()
    }

    override fun refreshHoverText() {
        // If the component type is non-null show the effect method and stats, otherwise show the slot is empty
        val componentType = this.getComponentType()
        if (componentType != null) {
            val componentInstance = this.getComponentInstance()!!
            this.hoverTexts = arrayOf(
                TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.effect", componentType.getName()).string,
                TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost", componentType.getCost(componentInstance)).string
            )
        } else {
            this.setHoverText(TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.empty_slot", "effect").string)
        }
    }

    override fun consume(data: Any) {
        if (data is SpellEffect) {
            val inst = SpellComponentInstance(data)
            inst.setDefaults()
            this.setSpellComponent(inst)
            this.spell.spellStages[stageIndex].effects[effectIndex] = inst
            this.refreshHoverText()
            invalidate()
        }
    }

    override fun calcChildrenBounds() {
        super.calcChildrenBounds()
        this.refreshHoverText() // Update hover text whenever the component is updated
    }
}
