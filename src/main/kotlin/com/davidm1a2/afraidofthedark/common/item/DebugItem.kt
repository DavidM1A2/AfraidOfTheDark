package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.tileEntity.MagicCrystalTileEntity
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
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
            val te = worldIn.getBlockEntity(playerIn.blockPosition().relative(playerIn.direction)) as? MagicCrystalTileEntity
            te?.consumeVitae(500.0)
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