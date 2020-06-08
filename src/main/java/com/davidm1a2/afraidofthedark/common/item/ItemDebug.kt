package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EntityEnchantedFrog
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World

/**
 * Item that allows for modding debug, does nothing else
 *
 * @constructor sets up item properties
 */
class ItemDebug : AOTDItem("debug", displayInCreative = false) {
    init {
        setMaxStackSize(1)
    }

    ///
    /// Code below here is not documented due to its temporary nature used for testing
    ///

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        if (worldIn.isRemote) {
        } else {
            SchematicGenerator.generateSchematic(ModSchematics.OBSERVATORY, worldIn, playerIn.position, null, ModLootTables.OBSERVATORY)
        }
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }

    override fun onLeftClickEntity(stack: ItemStack, player: EntityPlayer, entity: Entity): Boolean {
        if (!player.world.isRemote)
            if (entity is EntityEnchantedFrog) {
                val s = entity.frogsSpell
                player.sendMessage(TextComponentString(s.toString()))
                AfraidOfTheDark.INSTANCE.logger.info("Type is:\n$s")
            }
        return super.onLeftClickEntity(stack, player, entity)
    }
}