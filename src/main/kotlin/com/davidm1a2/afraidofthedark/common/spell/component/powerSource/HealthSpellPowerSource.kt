package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.CastEnvironment
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.text.TranslationTextComponent
import kotlin.math.ceil

/**
 * Class representing the experience source
 */
class HealthSpellPowerSource : AOTDSpellPowerSource<Unit>("health", ModResearches.BLOOD_MAGIC) {
    override fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<Unit>): SpellCastResult {
        if (entity !is LivingEntity) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        if (entity.hurtTime > 0) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.was_just_hurt"))
        }

        if (environment.vitaeAvailable < spell.getCost()) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        // Creative/Spectator players can't take dmg
        if (entity !is PlayerEntity || (!entity.isCreative && !entity.isSpectator)) {
            val hpCost = spell.getCost() / VITAE_PER_HP
            entity.hurt(DamageSource.OUT_OF_WORLD, hpCost.toFloat())
        }

        return SpellCastResult.success()
    }

    override fun computeCastEnvironment(entity: Entity): CastEnvironment<Unit> {
        if (entity !is LivingEntity) {
            return CastEnvironment.noVitae(Unit)
        }

        // They can use 10 "deficit" hearts (i.e. they can kill themselves :P)
        val absorptionAmount = entity.absorptionAmount
        return CastEnvironment.withVitae(
            (entity.health + 20 + absorptionAmount) * VITAE_PER_HP,
            (entity.maxHealth + 20 + absorptionAmount) * VITAE_PER_HP,
            Unit
        )
    }

    override fun getSourceSpecificCost(vitae: Double): Double {
        return ceil(vitae / VITAE_PER_HP) / 2.0
    }

    companion object {
        // The number of units each hp supplies
        private const val VITAE_PER_HP = 5.0
    }
}