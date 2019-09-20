package com.davidm1a2.afraidofthedark.common.item;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions;
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Class representing the "Insanity's Heights" book you get in the nightmare realm
 */
public class ItemInsanitysHeights extends AOTDItem
{
    /**
     * Constructor sets the item's name
     */
    public ItemInsanitysHeights()
    {
        super("insanitys_heights");
        this.setMaxStackSize(1);
    }

    /**
     * Called when the book is right clicked, opens the book GUI
     *
     * @param worldIn  The world the right click occurred in
     * @param playerIn The player that right clicked
     * @param handIn   The hand the player is holding the item in
     * @return Success, the UI opened
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack heldItem = playerIn.getHeldItem(handIn);

        // Show the player the book if they're in the nightmare
        if (worldIn.provider.getDimension() == ModDimensions.NIGHTMARE.getId())
        {
            AfraidOfTheDark.proxy.showInsanitysHeightsBook(playerIn);
        }
        else
        {
            if (!worldIn.isRemote)
            {
                playerIn.sendMessage(new TextComponentTranslation("aotd.insanitys_heights.dont_understand"));
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
    }
}
