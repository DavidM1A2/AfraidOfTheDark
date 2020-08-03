package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EntityEnchantedFrog
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager

/**
 * Item that allows for modding debug, does nothing else
 *
 * @constructor sets up item properties
 */
class ItemDebug : AOTDItem("debug", Properties().maxStackSize(1), displayInCreative = false) {
    ///
    /// Code below here is not documented due to its temporary nature used for testing
    ///

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        if (worldIn.isRemote) {
        } else {
            val pos = worldIn.findNearestStructure("afraidofthedark:void_chest_portal", playerIn.position, 100, false)
            playerIn.sendMessage(TextComponentString("Pos is: $pos"))
        }
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }

    override fun onLeftClickEntity(stack: ItemStack, player: EntityPlayer, entity: Entity): Boolean {
        if (!player.world.isRemote)
            if (entity is EntityEnchantedFrog) {
                val s = entity.frogsSpell
                player.sendMessage(TextComponentString(s.toString()))
                logger.info("Type is:\n$s")
            }
        return super.onLeftClickEntity(stack, player, entity)
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}