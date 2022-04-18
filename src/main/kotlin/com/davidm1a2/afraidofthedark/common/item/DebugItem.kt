package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellLunarData
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellSolarData
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellThermalData
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import com.google.gson.Gson
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World
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

    override fun use(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack> {
        if (worldIn.isClientSide) {
        } else {
            val gson = Gson()
            playerIn.getSpellManager().getSpells().forEach {
                println(gson.toJson(it.serializeNBT()))
            }
            println(playerIn.getSpellLunarData().vitae)
            println(playerIn.getSpellSolarData().vitae)
            println(playerIn.getSpellThermalData().vitae)
        }
        return super.use(worldIn, playerIn, handIn)
    }

    override fun onLeftClickEntity(stack: ItemStack, player: PlayerEntity, entity: Entity): Boolean {
        if (!player.level.isClientSide)
            if (entity is EnchantedFrogEntity) {
                val s = entity.spell
                player.sendMessage(StringTextComponent(s.toString()))
                logger.info("Type is:\n$s")
            }
        return super.onLeftClickEntity(stack, player, entity)
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}