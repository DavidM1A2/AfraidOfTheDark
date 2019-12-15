package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.resources.I18n
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

/**
 * Class that represents a research scroll to unlock researches
 *
 * @constructor
 */
class ItemResearchScroll : AOTDItem("research_scroll")
{
    /**
     * Get a list of sub-items which is one item per research
     *
     * @param tab   The creative tab to get sub-items for
     * @param items The sub-items
     */
    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>)
    {
        if (isInCreativeTab(tab))
        {
            // Add one itemstack for each research
            for (research in ModRegistries.RESEARCH)
            {
                val itemStack = ItemStack(this, 1, 0)
                setScrollResearch(itemStack, research)
                items.add(itemStack)
            }
        }
    }

    /**
     * When the player right clicks with a scroll unlock the research
     *
     * @param world  The world the item was right clicked in
     * @param player The player that right clicked the scroll
     * @param hand   The hand the item is held in
     * @return The action result of the right click
     */
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>
    {
        // Grab the itemstack that represents the scroll
        val itemStack = player.getHeldItem(hand)

        // Server side processing only
        if (!world.isRemote)
        {
            // Grab the player's research
            val playerResearch = player.getCapability(ModCapabilities.PLAYER_RESEARCH, null)!!

            val scrollResearch = getScrollResearch(itemStack)
            // Ensure the scroll has a valid research
            if (scrollResearch != null)
            {
                // If the player can research the research do so
                if (playerResearch.canResearch(scrollResearch))
                {
                    // Test if the scroll is ready
                    if (!isPart(itemStack))
                    {
                        // Shrink the itemstack to consume the scroll
                        itemStack.shrink(1)
                        // Unlock the research
                        playerResearch.setResearch(scrollResearch, true)
                        playerResearch.sync(player, true)
                    }
                    else
                    {
                        player.sendMessage(TextComponentTranslation("aotd.research_scroll.incomplete"))
                    }
                }
                // If the player does not yet have the research then state that they need additional research first
                else if (!playerResearch.isResearched(scrollResearch))
                {
                    player.sendMessage(TextComponentTranslation("aotd.research_scroll.cant_understand"))
                }
                // If the player does have the research tell them
                else
                {
                    player.sendMessage(TextComponentTranslation("aotd.research_scroll.already_researched"))
                }
            }
            // No valid research detected
            else
            {
                player.sendMessage(TextComponentTranslation("aotd.research_scroll.corrupt"))
            }
        }
        return super.onItemRightClick(world, player, hand)
    }

    /**
     * Adds a tooltip to the scroll
     *
     * @param stack   The itemstack to add tooltips for
     * @param world The world the itemstack is in
     * @param tooltip The tooltip of the research
     * @param flag  If the advanced details is on or off
     */
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag)
    {
        val scrollResearch = getScrollResearch(stack)
        if (scrollResearch != null)
        {
            if (isPart(stack))
            {
                tooltip.add("Scroll part " + getPartNumber(stack) + "/" + getNumberParts(stack) + " of the research " + I18n.format(scrollResearch.getUnlocalizedName()) + ".")
            }
            else
            {
                tooltip.add(I18n.format(scrollResearch.getUnlocalizedName()))
            }
        }
        else
        {
            tooltip.add("Scroll is corrupt.")
        }
    }

    /**
     * Sets the research for a given scroll
     *
     * @param itemStack The item to set the research for
     * @param research  The research to set
     */
    fun setScrollResearch(itemStack: ItemStack, research: Research)
    {
        NBTHelper.setString(itemStack, NBT_RESEARCH_ID, research.registryName.toString())
    }

    /**
     * Returns true if this scroll is a part of a full scroll, false otherwise
     *
     * @param itemStack The scroll itemstack
     * @return True if the itemstack has the number parts and part number tags, false otherwise
     */
    fun isPart(itemStack: ItemStack): Boolean
    {
        return NBTHelper.hasTag(itemStack, NBT_NUMBER_PARTS) && NBTHelper.hasTag(itemStack, NBT_PART_NUMBER)
    }

    /**
     * Returns the number of total parts required to complete this research scroll
     *
     * @param itemStack The research scroll
     * @return The number of total parts required to combine the scroll
     */
    fun getNumberParts(itemStack: ItemStack): Int
    {
        return NBTHelper.getInteger(itemStack, NBT_NUMBER_PARTS)!!
    }

    /**
     * Returns the part number of this scroll
     *
     * @param itemStack The itemstack to get part number for
     * @return The part number of this scroll, should be in the range [1 ... getNumberParts()]
     */
    fun getPartNumber(itemStack: ItemStack): Int
    {
        return NBTHelper.getInteger(itemStack, NBT_PART_NUMBER)!!
    }

    /**
     * Gets the research for a given scroll to unlock
     *
     * @param itemStack The item to get research from
     * @return The research on the scroll or null if it doesn't exist
     */
    fun getScrollResearch(itemStack: ItemStack): Research?
    {
        return if (NBTHelper.hasTag(itemStack, NBT_RESEARCH_ID))
        {
            ModRegistries.RESEARCH.getValue(ResourceLocation(NBTHelper.getString(itemStack, NBT_RESEARCH_ID)))
        }
        else
        {
            null
        }
    }

    companion object
    {
        // NBT string constants
        private const val NBT_RESEARCH_ID = "research_id"
        private const val NBT_PART_NUMBER = "part_number"
        private const val NBT_NUMBER_PARTS = "number_parts"
    }
}