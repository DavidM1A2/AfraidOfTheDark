package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import kotlin.math.ceil

class VitaeLanternSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "vitae_lantern"), ModResearches.VITAE_LANTERN) {
    override fun cast(entity: Entity, spell: Spell): SpellCastResult {
        if (entity !is PlayerEntity) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        val inventory = entity.inventory.items + entity.inventory.offhand
        val lantern = ModItems.VITAE_LANTERN
        var lanternVitae = 0f
        for (stack in inventory) {
            if (stack.item == lantern) {
                lanternVitae = lanternVitae + lantern.getVitae(stack)
            }
        }

        if (lanternVitae < spell.getCost()) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        var vitaeRemaining = ceil(spell.getCost()).toFloat()
        for (stack in inventory) {
            if (stack.item == lantern) {
                val vitaeInLantern = lantern.getVitae(stack)
                vitaeRemaining = if (vitaeInLantern >= vitaeRemaining) {
                    lantern.removeVitae(stack, vitaeRemaining)
                    0f
                } else {
                    lantern.removeVitae(stack, vitaeInLantern)
                    vitaeRemaining - vitaeInLantern
                }
                if (vitaeRemaining == 0f) {
                    break
                }
            }
        }

        return SpellCastResult.success()
    }

    override fun getSourceSpecificCost(rawCost: Double): Double {
        return rawCost.round(1)
    }
}