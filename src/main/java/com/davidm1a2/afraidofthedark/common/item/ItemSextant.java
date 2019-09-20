package com.davidm1a2.afraidofthedark.common.item;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiHandler;
import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModResearches;
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Class representing the telescope item used to track meteors
 */
public class ItemSextant extends AOTDItem
{
    /**
     * Constructor sets up item properties
     */
    public ItemSextant()
    {
        super("sextant");
    }

    /**
     * Called when the player right clicks with the sextant
     *
     * @param worldIn  The world that the sextant was right clicked in
     * @param playerIn The player that right clicked the sextant
     * @param handIn   The hand the telescope is in
     * @return The result of the right click
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        IAOTDPlayerResearch playerResearch = playerIn.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
        // If the player has astronomy 1 open the GUI client side
        if (playerResearch.isResearched(ModResearches.ASTRONOMY_1))
        {
            // Only open GUIs client side
            if (worldIn.isRemote)
            {
                playerIn.openGui(AfraidOfTheDark.INSTANCE, AOTDGuiHandler.SEXTANT_ID, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
            }
        }
        // If the player does not have the research send him a chat message from the server
        else
        {
            if (!worldIn.isRemote)
            {
                playerIn.sendMessage(new TextComponentTranslation("aotd.dont_understand"));
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }
}
