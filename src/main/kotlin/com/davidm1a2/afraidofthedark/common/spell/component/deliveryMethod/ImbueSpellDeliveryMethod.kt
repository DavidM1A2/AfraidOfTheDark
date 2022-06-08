package com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod

import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.AOTDSpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.spell.component.property.SpellComponentPropertyFactory
import net.minecraft.entity.player.PlayerEntity

class ImbueSpellDeliveryMethod : AOTDSpellDeliveryMethod("imbue", ModResearches.SCROLL_FORMATION) {
    init {
        addEditableProperty(
            SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("uses"))
                .withSetter(this::setUses)
                .withGetter(this::getUses)
                .withDefaultValue(1)
                .withMinValue(1)
                .withMaxValue(16)
                .build()
        )
    }

    override fun executeDelivery(state: DeliveryTransitionState) {
        // Can't do anything against blocks
        val entity = state.entity ?: return

        if (entity is PlayerEntity) {
            val inventory = entity.inventory
            for (itemStack in inventory.items + inventory.offhand) {
                if (itemStack.item == ModItems.SPELL_SCROLL) {
                    if (ModItems.SPELL_SCROLL.isEmpty(itemStack)) {
                        val uses = getUses(state.getCurrentStage().deliveryInstance!!)
                        ModItems.SPELL_SCROLL.setUses(itemStack, uses)
                        ModItems.SPELL_SCROLL.setMaxUses(itemStack, uses)
                        val oldSpellStages = state.spell.spellStages
                        val newSpellStages = oldSpellStages.subList(state.stageIndex, oldSpellStages.size).map {
                            SpellStage(it.serializeNBT())
                        }
                        newSpellStages[0].deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.SELF).apply {
                            setDefaults()
                        }
                        val newSpell = Spell().apply {
                            name = state.spell.name
                            spellStages.addAll(newSpellStages)
                        }
                        ModItems.SPELL_SCROLL.setSpell(itemStack, newSpell)
                        return
                    }
                }
            }
        }
    }

    override fun getDeliveryCost(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return 10.0
    }

    override fun getMultiplicity(instance: SpellComponentInstance<SpellDeliveryMethod>): Double {
        return getUses(instance).toDouble()
    }

    fun setUses(instance: SpellComponentInstance<*>, uses: Int) {
        instance.data.putInt(NBT_USES, uses)
    }

    fun getUses(instance: SpellComponentInstance<*>): Int {
        return instance.data.getInt(NBT_USES)
    }

    companion object {
        private const val NBT_USES = "uses"
    }
}