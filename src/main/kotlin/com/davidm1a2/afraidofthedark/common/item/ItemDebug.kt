package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EntityEnchantedFrog
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.world.WorldHeightmap
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import net.minecraft.world.gen.Heightmap
import org.apache.logging.log4j.LogManager
import kotlin.math.roundToInt

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
            playerIn.sendMessage(
                TextComponentString(
                    "Height: ${WorldHeightmap.getHeight(
                        playerIn.posX.roundToInt(),
                        playerIn.posZ.roundToInt(),
                        worldIn,
                        worldIn.chunkProvider.chunkGenerator
                    )}"
                )
            )
            playerIn.sendMessage(TextComponentString("Height2: ${worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, playerIn.position)}"))
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