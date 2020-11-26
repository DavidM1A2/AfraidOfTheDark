package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World
import net.minecraft.world.gen.feature.structure.StructureStart
import org.apache.logging.log4j.LogManager

/**
 * Item that allows for modding debug, does nothing else
 *
 * @constructor sets up item properties
 */
class DebugItem : AOTDItem("debug", Properties().maxStackSize(1), displayInCreative = false) {
    ///
    /// Code below here is not documented due to its temporary nature used for testing
    ///

    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack> {
        if (worldIn.isRemote) {
            //worldIn.spawnParticle(ModParticles.ENARIA_BASIC_ATTACK, playerIn.posX, playerIn.posY + 4, playerIn.posZ, 0.0, 0.0, 0.0)
        } else {
            playerIn.sendMessage(
                StringTextComponent(
                    worldIn.getChunkAt(playerIn.position).structureStarts.filter { it.value != StructureStart.DUMMY }.map { it.key }.joinToString()
                )
            )
            playerIn.sendMessage(
                StringTextComponent(
                    worldIn.getChunkAt(playerIn.position).structureReferences.filter { it.value.isNotEmpty() }.map { it.key }.joinToString()
                )
            )
        }
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }

    override fun onLeftClickEntity(stack: ItemStack, player: PlayerEntity, entity: Entity): Boolean {
        if (!player.world.isRemote)
            if (entity is EnchantedFrogEntity) {
                val s = entity.getSpell()
                player.sendMessage(StringTextComponent(s.toString()))
                logger.info("Type is:\n$s")
            }
        return super.onLeftClickEntity(stack, player, entity)
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}