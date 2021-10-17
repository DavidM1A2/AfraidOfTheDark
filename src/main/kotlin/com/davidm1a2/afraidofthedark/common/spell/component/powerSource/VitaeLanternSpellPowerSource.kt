package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TranslationTextComponent
import kotlin.math.ceil

class VitaeLanternSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "vitae_lantern")) {
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        if (entity is PlayerEntity) {
            val inventory = entity.inventory.items + entity.inventory.offhand
            val lantern = ModItems.VITAE_LANTERN
            var lanternVitae = 0
            for (stack in inventory) {
                if (stack.item == lantern) {
                    lanternVitae = lanternVitae + lantern.getVitae(stack)
                }
            }
            return lanternVitae >= spell.getCost()
        }
        return false
    }

    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        if (entity is PlayerEntity) {
            val inventory = entity.inventory.items + entity.inventory.offhand
            val lantern = ModItems.VITAE_LANTERN
            var vitaeRemaining = ceil(spell.getCost()).toInt()
            for (stack in inventory) {
                if (stack.item == lantern) {
                    val vitaeInLantern = lantern.getVitae(stack)
                    vitaeRemaining = if (vitaeInLantern >= vitaeRemaining) {
                        lantern.removeVitae(stack, vitaeRemaining)
                        0
                    } else {
                        lantern.removeVitae(stack, vitaeInLantern)
                        vitaeRemaining - vitaeInLantern
                    }
                    if (vitaeRemaining == 0) {
                        return
                    }
                }
            }
        }
    }

    override fun getCostDescription(): ITextComponent {
        return StringTextComponent("Consume vitae from lanterns in your inventory at a rate of one vitae per unit cost")
    }

    override fun getOutOfPowerMsg(): ITextComponent {
        return TranslationTextComponent("message.afraidofthedark.spell.power_source.vitae_lantern.invalid_msg")
    }
}