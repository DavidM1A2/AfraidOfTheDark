package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItem;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import com.DavidM1A2.afraidofthedark.common.utility.NBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Class that represents a research scroll to unlock researches
 */
public class ItemResearchScroll extends AOTDItem
{
    // NBT string constants
    private static final String NBT_RESEARCH_ID = "research_id";
    private static final String NBT_PART_NUMBER = "part_number";
    private static final String NBT_NUMBER_PARTS = "number_parts";

    /**
     * Constructor sets up item name
     */
    public ItemResearchScroll()
    {
        super("research_scroll");
    }

    /**
     * Get a list of sub-items which is one item per research
     *
     * @param tab   The creative tab to get sub-items for
     * @param items The sub-items
     */
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        // Add one itemstack for each research
        {
            for (Research research : ModRegistries.RESEARCH)
            {
                ItemStack itemStack = new ItemStack(this, 1, 0);
                this.setScrollResearch(itemStack, research);
                items.add(itemStack);
            }
        }
    }

    /**
     * When the player right clicks with a scroll unlock the research
     *
     * @param worldIn  The world the item was right clicked in
     * @param playerIn The player that right clicked the scroll
     * @param handIn   The hand the item is held in
     * @return The action result of the right click
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        // Grab the itemstack that represents the scroll
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        // Server side processing only
        if (!worldIn.isRemote)
        {
            // Grab the player's research
            IAOTDPlayerResearch playerResearch = playerIn.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
            Research scrollResearch = this.getScrollResearch(itemStack);
            // Ensure the scroll has a valid research
            if (scrollResearch != null)
            {
                // If the player can research the research do so
                if (playerResearch.canResearch(scrollResearch))
                {
                    // Test if the scroll is ready
                    if (!this.isPart(itemStack))
                    {
                        // Shrink the itemstack to consume the scroll
                        itemStack.shrink(1);
                        // Unlock the research
                        playerResearch.setResearch(scrollResearch, true);
                        playerResearch.sync(playerIn, true);
                    }
                    // The scroll is not yet complete
                    else
                    {
                        playerIn.sendMessage(new TextComponentTranslation("aotd.research_scroll.incomplete"));
                    }
                }
                // If the player does not yet have the research then state that they need additional research first
                else if (!playerResearch.isResearched(scrollResearch))
                {
                    playerIn.sendMessage(new TextComponentTranslation("aotd.research_scroll.cant_understand"));
                }
                // If the player does have the research tell them
                else
                {
                    playerIn.sendMessage(new TextComponentTranslation("aotd.research_scroll.already_researched"));
                }
            }
            // No valid research detected
            else
            {
                playerIn.sendMessage(new TextComponentTranslation("aotd.research_scroll.corrupt"));
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    /**
     * Adds a tooltip to the scroll
     *
     * @param stack   The itemstack to add tooltips for
     * @param worldIn The world the itemstack is in
     * @param tooltip The tooltip of the research
     * @param flagIn  If the advanced details is on or off
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        Research scrollResearch = this.getScrollResearch(stack);
        if (scrollResearch != null)
        {
            if (this.isPart(stack))
            {
                tooltip.add("Scroll part " + this.getPartNumber(stack) + "/" + this.getNumberParts(stack) + " of the research " + I18n.format(scrollResearch.getUnLocalizedName()) + ".");
            }
            else
            {
                tooltip.add(I18n.format(scrollResearch.getUnLocalizedName()));
            }
        }
        else
        {
            tooltip.add("Scroll is corrupt.");
        }
    }

    /**
     * Sets the research for a given scroll
     *
     * @param itemStack The item to set the research for
     * @param research  The research to set
     */
    public void setScrollResearch(ItemStack itemStack, Research research)
    {
        NBTHelper.setString(itemStack, NBT_RESEARCH_ID, research.getRegistryName().toString());
    }

    /**
     * Returns true if this scroll is a part of a full scroll, false otherwise
     *
     * @param itemStack The scroll itemstack
     * @return True if the itemstack has the number parts and part number tags, false otherwise
     */
    public boolean isPart(ItemStack itemStack)
    {
        return NBTHelper.hasTag(itemStack, NBT_NUMBER_PARTS) && NBTHelper.hasTag(itemStack, NBT_PART_NUMBER);
    }

    /**
     * Returns the number of total parts required to complete this research scroll
     *
     * @param itemStack The research scroll
     * @return The number of total parts required to combine the scroll
     */
    public Integer getNumberParts(ItemStack itemStack)
    {
        return NBTHelper.getInteger(itemStack, NBT_NUMBER_PARTS);
    }

    /**
     * Returns the part number of this scroll
     *
     * @param itemStack The itemstack to get part number for
     * @return The part number of this scroll, should be in the range [1 ... getNumberParts()]
     */
    public Integer getPartNumber(ItemStack itemStack)
    {
        return NBTHelper.getInteger(itemStack, NBT_PART_NUMBER);
    }

    /**
     * Gets the research for a given scroll to unlock
     *
     * @param itemStack The item to get research from
     * @return The research on the scroll or null if it doesn't exist
     */
    public Research getScrollResearch(ItemStack itemStack)
    {
        if (NBTHelper.hasTag(itemStack, NBT_RESEARCH_ID))
        {
            return ModRegistries.RESEARCH.getValue(new ResourceLocation(NBTHelper.getString(itemStack, NBT_RESEARCH_ID)));
        }
        else
        {
            return null;
        }
    }
}
