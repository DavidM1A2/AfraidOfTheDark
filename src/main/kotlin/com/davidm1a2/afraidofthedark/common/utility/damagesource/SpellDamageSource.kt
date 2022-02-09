package com.davidm1a2.afraidofthedark.common.utility.damagesource

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import net.minecraft.entity.LivingEntity
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent

class SpellDamageSource(private val spellDamageState: DeliveryTransitionState) : EntityDamageSource("afraidofthedark.spell", spellDamageState.casterEntity) {
    override fun getLocalizedDeathMessage(attackingEntity: LivingEntity): ITextComponent {
        val baseText = "death.attack.$msgId"
        val spellCaster = spellDamageState.casterEntity
        return if (attackingEntity == spellCaster) {
            TranslationTextComponent("$baseText.suicide", attackingEntity.displayName)
        } else if (spellCaster != null) {
            TranslationTextComponent("$baseText.player", entity?.displayName, spellCaster.displayName, spellDamageState.spell.name)
        } else {
            TranslationTextComponent(baseText, entity?.displayName, entity?.displayName)
        }
    }
}