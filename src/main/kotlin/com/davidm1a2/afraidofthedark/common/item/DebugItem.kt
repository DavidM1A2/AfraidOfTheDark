package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import com.google.gson.Gson
import com.google.gson.JsonElement
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.apache.logging.log4j.LogManager

/**
 * Item that allows for modding debug, does nothing else
 *
 * @constructor sets up item properties
 */
class DebugItem : AOTDItem("debug", Properties().stacksTo(1), displayInCreative = false) {
    ///
    /// Code below here is not documented due to its temporary nature used for testing
    ///

    override fun use(worldIn: Level, playerIn: Player, handIn: InteractionHand): InteractionResultHolder<ItemStack> {
        if (worldIn.isClientSide) {
        } else {
//            val time = System.currentTimeMillis()
//            for (powerSource in ModSpellPowerSources.SPELL_POWER_SOURCES) {
//                val environment = powerSource.computeCastEnvironment(playerIn)
//                println("${powerSource.registryName!!.path} - Current: ${environment.vitaeAvailable}, Max: ${environment.vitaeMaximum}")
//            }
//            println("Took: ${(System.currentTimeMillis() - time)}ms")
//            playerIn.getSpellManager().getSpells().forEach {
//                println(serializeJSON(it))
//            }
        }
        return super.use(worldIn, playerIn, handIn)
    }

    override fun onLeftClickEntity(stack: ItemStack, player: Player, entity: Entity): Boolean {
        if (!player.level.isClientSide)
            if (entity is EnchantedFrogEntity) {
                val s = entity.spell
                player.sendMessage(TextComponent(s.toString()))
                logger.info("Type is:\n$s")
            }
        return super.onLeftClickEntity(stack, player, entity)
    }

    private fun serializeJSON(spell: Spell): JsonElement {
        return removeTags(GSON.toJsonTree(spell.serializeNBT()))
    }

    // Removes "tags" and "data" fields in json, replacing them with their values
    private fun removeTags(json: JsonElement): JsonElement {
        if (json.isJsonObject) {
            val jsonObj = json.asJsonObject
            val keys = jsonObj.entrySet().map { it.key }
            for (key in keys) {
                if (key == "tags" || key == "data") {
                    return removeTags(jsonObj[key])
                } else {
                    jsonObj.add(key, removeTags(jsonObj.remove(key)))
                }
            }
        } else if (json.isJsonArray) {
            val jsonArr = json.asJsonArray
            for (index in 0 until jsonArr.size()) {
                jsonArr.set(index, removeTags(jsonArr[index]))
            }
        }
        return json
    }

    companion object {
        private val GSON = Gson()

        private val logger = LogManager.getLogger()
    }
}