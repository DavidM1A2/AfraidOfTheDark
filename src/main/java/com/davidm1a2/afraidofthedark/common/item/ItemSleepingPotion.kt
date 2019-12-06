package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModPotions
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Potion that gives you the drowsy potion effect
 *
 * @constructor sets the item name
 */
class ItemSleepingPotion : AOTDItem("sleeping_potion")
{
    /**
     * It takes 32 ticks to drink the potion
     *
     * @param stack The item stack being drunk
     * @return The number of ticks to drink the potion
     */
    override fun getMaxItemUseDuration(stack: ItemStack): Int
    {
        return 32
    }

    /**
     * True since the potion glows
     *
     * @param stack The item stack
     * @return True since the potion has the 'enchanted' look
     */
    @SideOnly(Side.CLIENT)
    override fun hasEffect(stack: ItemStack): Boolean
    {
        return true
    }

    /**
     * It's a potion, so when the item is being used show the drink animation
     *
     * @param stack The item stack being drunk
     * @return DRINK since this is a potion
     */
    override fun getItemUseAction(stack: ItemStack): EnumAction
    {
        return EnumAction.DRINK
    }

    /**
     * Upon right clicking begin drinking the potion
     *
     * @param world  The world the player is in
     * @param player The player drinking the potion
     * @param hand   The hand the player is using to hold the potion
     * @return SUCCESS since the potion drinking began
     */
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>
    {
        player.activeHand = hand
        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    /**
     * Called when the item is finished being drunk
     *
     * @param stack        The itemstack being drunk
     * @param worldIn      The world that the player is in
     * @param entityLiving The entity that drunk the potion
     * @return The itemstack that this item became
     */
    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack
    {
        // Server side only processing
        if (!worldIn.isRemote)
        {
            // This potion only effects players
            if (entityLiving is EntityPlayer)
            {
                entityLiving.addPotionEffect(PotionEffect(ModPotions.SLEEPING_POTION, 4800, 0, false, true))
                // If the player is not in creative mode reduce the bottle stack size by 1 and return the bottle
                if (!entityLiving.capabilities.isCreativeMode)
                {
                    stack.shrink(1)
                    // If the stack is empty return a glass bottle, otherwise add a glass bottle to the player's inventory
                    if (stack.isEmpty)
                    {
                        return ItemStack(Items.GLASS_BOTTLE)
                    }
                    entityLiving.inventory.addItemStackToInventory(ItemStack(Items.GLASS_BOTTLE))
                }
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving)
    }
}