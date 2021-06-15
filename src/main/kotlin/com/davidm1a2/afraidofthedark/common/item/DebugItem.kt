package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World
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
        } else {
            SchematicStructurePiece(
                playerIn.posX.toInt(),
                playerIn.posY.toInt(),
                playerIn.posZ.toInt(),
                playerIn.rng,
                ModSchematics.ENARIA_LAIR,
                facing = playerIn.horizontalFacing
            )
                .addComponentParts(
                    worldIn,
                    playerIn.rng,
                    MutableBoundingBox.createProper(-10000, -10000, -10000, 10000, 10000, 10000),
                    ChunkPos(playerIn.position)
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