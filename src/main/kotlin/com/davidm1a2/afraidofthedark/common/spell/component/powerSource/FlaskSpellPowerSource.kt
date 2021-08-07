package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.item.FlaskOfSoulsItem
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation

/**
 * Class representing the flask of souls source
 */
class FlaskSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "flask")) {
    /**
     * True if the given spell can be cast, false otherwise
     *
     * @param entity The entity that is casting the spell
     * @param spell        The spell to attempt to cast
     * @return True if the spell can be cast, false otherwise
     */
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        if (entity is PlayerEntity) {
            val inventory = entity.inventory.items + entity.inventory.offhand
            var remainingCost = spell.getCost()
            for (stack in inventory) {
                if (stack.item == ModItems.FLASK_OF_SOULS) {
                    val killsRequired = ModItems.FLASK_OF_SOULS.getKillsRequired(stack)
                    val kills = ModItems.FLASK_OF_SOULS.getKills(stack)
                    val soulPower = FlaskOfSoulsItem.FLASK_POWER / killsRequired.toDouble()
                    val powerAvailable = kills.times(soulPower)
                    if (remainingCost <= powerAvailable) {
                        return true
                    } else {
                        remainingCost -= powerAvailable
                    }
                }
            }
        }
        return false
    }

    /**
     * Consume souls equivalent to the spell cost
     *
     * @param entity The entity that is casting the spell
     * @param spell        the spell to attempt to cast
     */
    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        if (entity is PlayerEntity) {
            val inventory = entity.inventory.items + entity.inventory.offhand
            var remainingCost = spell.getCost()
            for (stack in inventory) {
                if (stack.item == ModItems.FLASK_OF_SOULS) {
                    val killsRequired = ModItems.FLASK_OF_SOULS.getKillsRequired(stack)
                    val kills = ModItems.FLASK_OF_SOULS.getKills(stack)
                    val soulPower = FlaskOfSoulsItem.FLASK_POWER / killsRequired.toDouble()
                    val powerAvailable = kills.times(soulPower)
                    if (remainingCost <= powerAvailable) {
                        while (remainingCost > 0.0) {
                            remainingCost -= soulPower
                            ModItems.FLASK_OF_SOULS.addKills(stack, -1)
                        }
                    } else {
                        remainingCost -= powerAvailable
                        ModItems.FLASK_OF_SOULS.setKills(stack, 0)
                    }
                }
            }
        }
    }

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    override fun getCostDescription(): String {
        return "A full Flask of Souls provides " + FlaskOfSoulsItem.FLASK_POWER + " spell power"
    }

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @return A string describing why the power source doesn't have enough energy
     */
    override fun getUnlocalizedOutOfPowerMsg(): String {
        return "message.afraidofthedark.spell.power_source.flask.invalid_msg"
    }
}