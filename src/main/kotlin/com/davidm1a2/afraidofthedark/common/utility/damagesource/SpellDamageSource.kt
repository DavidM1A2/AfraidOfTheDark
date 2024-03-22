package com.davidm1a2.afraidofthedark.common.utility.damagesource

import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.damagesource.EntityDamageSource
import net.minecraft.world.entity.LivingEntity

class SpellDamageSource(private val spellDamageState: DeliveryTransitionState) : EntityDamageSource("afraidofthedark.spell", spellDamageState.casterEntity) {
    override fun getLocalizedDeathMessage(killedEntity: LivingEntity): Component {
        val baseText = "death.attack.$msgId"
        val spellCaster = spellDamageState.casterEntity
        return if (killedEntity == spellCaster) {
            TranslatableComponent("$baseText.suicide", killedEntity.displayName)
        } else if (spellCaster != null) {
            TranslatableComponent("$baseText.player", killedEntity.displayName, spellCaster.displayName, spellDamageState.spell.name)
        } else {
            TranslatableComponent(baseText, killedEntity.displayName, spellDamageState.spell.name)
        }
    }
}