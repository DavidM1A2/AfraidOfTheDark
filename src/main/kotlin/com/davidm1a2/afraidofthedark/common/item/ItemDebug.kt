package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EntityEnchantedFrog
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.worldGeneration.generateSchematic
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
    var i = 0

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        if (worldIn.isRemote) {
        } else {
            worldIn.generateSchematic(ModSchematics.LIST[i], playerIn.position.add(1, 0, 1))
            if (i++ > ModSchematics.LIST.size - 1) {
                i = 0
            }
            playerIn.sendMessage(TextComponentString("Next schematic is ${ModSchematics.LIST[i].getName()}"))
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