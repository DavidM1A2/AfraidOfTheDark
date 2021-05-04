package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.item.FlaskOfSoulsItem
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Class representing the experience source
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
        return (entity as? PlayerEntity)?.let {
            if (it.inventory.offHandInventory[0].item.registryName == ResourceLocation(Constants.MOD_ID, "flask_of_souls")) {
                val stack = it.inventory.offHandInventory[0]
                val flaskEntity = ResourceLocation(NBTHelper.getString(stack, FlaskOfSoulsItem.NBT_FLASK_TYPE)!!)
                val killsRequired = FlaskOfSoulsItem.ENTITY_TO_KILLS_REQUIRED[flaskEntity] ?: FlaskOfSoulsItem.DEFAULT_KILLS_REQUIRED
                val kills = NBTHelper.getInteger(stack, FlaskOfSoulsItem.NBT_FLASK_KILLS)
                val soulPower = FlaskOfSoulsItem.FLASK_POWER/killsRequired.toDouble()
                val powerAvailable = kills?.times(soulPower)
                if (NBTHelper.hasTag(stack, FlaskOfSoulsItem.NBT_FLASK_KILLS)) {
                    if (powerAvailable != null) {
                        return powerAvailable >= spell.getCost()
                    }
                }
            }
            return false
        } ?: false
    }

    /**
     * Does nothing, creative power sources don't use energy
     *
     * @param entity The entity that is casting the spell
     * @param spell        the spell to attempt to cast
     */
    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        (entity as? PlayerEntity)?.let {
            if (it.inventory.offHandInventory[0].item.registryName == ResourceLocation(Constants.MOD_ID, "flask_of_souls")) {
                val stack = it.inventory.offHandInventory[0]
                val flaskEntity = ResourceLocation(NBTHelper.getString(stack, FlaskOfSoulsItem.NBT_FLASK_TYPE)!!)
                val killsRequired = FlaskOfSoulsItem.ENTITY_TO_KILLS_REQUIRED[flaskEntity] ?: FlaskOfSoulsItem.DEFAULT_KILLS_REQUIRED
                val kills = NBTHelper.getInteger(stack, FlaskOfSoulsItem.NBT_FLASK_KILLS)
                val soulPower = FlaskOfSoulsItem.FLASK_POWER/killsRequired.toDouble()
                val soulCost = max((spell.getCost()/soulPower).roundToInt(), 1) // No cheeky 0 cost spells

                if (NBTHelper.hasTag(stack, FlaskOfSoulsItem.NBT_FLASK_KILLS)) {
                    if (kills != null) {
                        NBTHelper.setInteger(stack, FlaskOfSoulsItem.NBT_FLASK_KILLS, kills-soulCost)
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
        return "A full Flask of Souls is " + FlaskOfSoulsItem.FLASK_POWER + " spell power"
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