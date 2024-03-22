package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.CastEnvironment
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import net.minecraft.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.item.Items
import net.minecraft.util.text.TranslationTextComponent
import kotlin.math.ceil

/**
 * Class representing the alchemy source
 */
class AlchemySpellPowerSource : AOTDSpellPowerSource<Unit>("alchemy", ModResearches.ALCHEMY) {
    override fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<Unit>): SpellCastResult {
        if (entity !is Player) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        if (environment.vitaeAvailable < spell.getCost()) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        var costRemaining = spell.getCost()
        val inventory = entity.inventory.items + entity.inventory.offhand
        for (stack in inventory) {
            if (stack.item in VALID_INGOTS) {
                while (costRemaining > 0.0 && stack.count > 0) {
                    costRemaining -= VITAE_PER_INGOT[stack.item]!!
                    stack.count--
                }
            }
            if (costRemaining <= 0.0) break
        }
        return SpellCastResult.success()
    }

    override fun computeCastEnvironment(entity: Entity): CastEnvironment<Unit> {
        if (entity !is Player) {
            return CastEnvironment.noVitae(Unit)
        }

        val inventory = entity.inventory.items + entity.inventory.offhand
        var vitaeAvailable = 0.0
        for (stack in inventory) {
            if (stack.item in VALID_INGOTS) {
                vitaeAvailable = vitaeAvailable + VITAE_PER_INGOT[stack.item]!! * stack.count
            }
        }

        return CastEnvironment.withVitae(vitaeAvailable, inventory.size * 64 * VITAE_PER_INGOT.values.maxOf { it }, Unit)
    }

    override fun getSourceSpecificCost(vitae: Double): Number {
        return ceil(vitae / VITAE_PER_INGOT[Items.GOLD_INGOT]!!).toInt()
    }

    companion object {
        private val VALID_INGOTS = setOf(Items.GOLD_INGOT, ModItems.ASTRAL_SILVER_INGOT)
        private val VITAE_PER_INGOT = mapOf(
            Items.GOLD_INGOT to 10.0,
            ModItems.ASTRAL_SILVER_INGOT to 20.0
        )
    }
}