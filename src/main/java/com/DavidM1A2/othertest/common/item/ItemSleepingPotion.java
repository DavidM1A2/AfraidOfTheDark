package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.common.constants.ModPotions;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Potion that gives you the drowsy potion effect
 */
public class ItemSleepingPotion extends AOTDItem
{
    /**
     * Constructor sets the item name
     */
    public ItemSleepingPotion()
    {
        super("sleeping_potion");
    }

    /**
     * It takes 32 ticks to drink the potion
     *
     * @param stack The item stack being drunk
     * @return The number of ticks to drink the potion
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

    /**
     * True since the potion glows
     *
     * @param stack The item stack
     * @return True since the potion has the 'enchanted' look
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    /**
     * It's a potion, so when the item is being used show the drink animation
     *
     * @param stack The item stack being drunk
     * @return DRINK since this is a potion
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    /**
     * Upon right clicking begin drinking the potion
     *
     * @param worldIn  The world the player is in
     * @param playerIn The player drinking the potion
     * @param handIn   The hand the player is using to hold the potion
     * @return SUCCESS since the potion drinking began
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(handIn);
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    /**
     * Called when the item is finished being drunk
     *
     * @param stack        The itemstack being drunk
     * @param worldIn      The world that the player is in
     * @param entityLiving The entity that drunk the potion
     * @return The itemstack that this item became
     */
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        // Server side only processing
        if (!worldIn.isRemote)
        {
            // This potion only effects players
            if (entityLiving instanceof EntityPlayer)
            {
                EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
                entityPlayer.addPotionEffect(new PotionEffect(ModPotions.SLEEPING_POTION, 4800, 0, false, true));
                // If the player is not in creative mode reduce the bottle stack size by 1 and return the bottle
                if (!entityPlayer.capabilities.isCreativeMode)
                {
                    stack.shrink(1);
                    // If the stack is empty return a glass bottle, otherwise add a glass bottle to the player's inventory
                    if (stack.isEmpty())
                    {
                        return new ItemStack(Items.GLASS_BOTTLE);
                    }
                    entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
                }
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
