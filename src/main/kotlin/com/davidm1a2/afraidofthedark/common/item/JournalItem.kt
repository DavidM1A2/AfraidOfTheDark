package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.client.gui.screens.BloodStainedJournalResearchScreen
import com.davidm1a2.afraidofthedark.client.gui.screens.BloodStainedJournalSignScreen
import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.NonNullList
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

/**
 * Class representing the blood stained journal item
 *
 * @constructor sets up item properties
 */
class JournalItem : AOTDItem("journal", Properties().maxStackSize(1)) {
    /**
     * Called when the user right clicks with the journal. We show the research UI if they have started the mod
     *
     * @param world  The world that the item was right clicked in
     * @param player The player that right clicked the item
     * @param hand   The hand that the item is in
     * @return An action result that determines if the right click was.
     * Success = The call has succeeded in doing what was needed and should stop here.
     * Pass    = The call succeeded, but more calls can be made farther down the call stack.
     * Fail    = The call has failed to do what was intended and should stop here.
     */
    override fun onItemRightClick(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        val heldItemStack = player.getHeldItem(hand)
        // Test if the journal is a cheat sheet or not
        val isCheatSheet =
            NBTHelper.hasTag(heldItemStack, NBT_CHEAT_SHEET) && NBTHelper.getBoolean(heldItemStack, NBT_CHEAT_SHEET)!!

        // If the journal does not have an owner yet...
        if (!NBTHelper.hasTag(heldItemStack, NBT_OWNER)) {
            // If the player has started AOTD, set the NBT tag and open the journal
            if (player.getBasics().startedAOTD) {
                // Set the owner tag to the player's username
                setOwner(heldItemStack, player.gameProfile.name)

                // Show the journal UI
                if (world.isRemote) {
                    Minecraft.getInstance().displayGuiScreen(BloodStainedJournalResearchScreen(isCheatSheet))
                }
            } else {
                if (world.isRemote) {
                    Minecraft.getInstance().displayGuiScreen(BloodStainedJournalSignScreen())
                }
            }
        }
        // If the journal does have an owner, check if that owner is us
        else if (player.gameProfile.name == NBTHelper.getString(heldItemStack, NBT_OWNER)) {
            // If the player has started AOTD show the journal UI
            if (player.getBasics().startedAOTD) {
                if (world.isRemote) {
                    Minecraft.getInstance().displayGuiScreen(BloodStainedJournalResearchScreen(isCheatSheet))
                }
            }
            // If the player has not started AOTD show the sign UI and clear the owner
            else {
                if (world.isRemote) {
                    Minecraft.getInstance().displayGuiScreen(BloodStainedJournalSignScreen())
                }
                setOwner(heldItemStack, null)
            }
        } else {
            // Send chat messages on server side only
            if (!world.isRemote) {
                player.sendMessage(TranslationTextComponent("message.afraidofthedark.journal.cant_comprehend"))
            }
        }

        // Return success because the journal processed the right click successfully
        return ActionResult.newResult(ActionResultType.SUCCESS, heldItemStack)
    }

    /**
     * Sets the owner of the journal
     *
     * @param itemStack The itemstack to modify
     * @param owner     The new journal owner
     */
    fun setOwner(itemStack: ItemStack, owner: String?) {
        if (owner == null) {
            NBTHelper.removeTag(itemStack, NBT_OWNER)
        } else {
            NBTHelper.setString(itemStack, NBT_OWNER, owner)
        }
    }

    /**
     * Returns a list of sub-items that this item has. In our case there's 2 journal types, one is a cheat sheet and one is not
     *
     * @param group The creative tab that we can add items to if we want, we don't use this
     * @param items A list of items (one cheatsheet, and one regular journal)
     */
    override fun fillItemGroup(group: ItemGroup, items: NonNullList<ItemStack>) {
        // Ensure that the item is in the creative tab first...
        if (isInGroup(group)) {
            // Two item stacks one standard and one cheatsheet journal
            val standardJournal = ItemStack(this)
            val cheatsheetJournal = ItemStack(this)

            // The cheat sheet journal will have the cheat sheet tag set to true
            NBTHelper.setBoolean(cheatsheetJournal, NBT_CHEAT_SHEET, true)

            // Add the two journals to the item list
            items.add(standardJournal)
            items.add(cheatsheetJournal)
        }
    }

    /**
     * Called to add a tooltip to the journal. If the journal has an owner, that owner is shown. If the journal does not have
     * an owner, that is also shown. If the journal is a cheat sheet, show that.
     *
     * @param stack   The itemstack to add information about
     * @param world The world that the item was hovered over in
     * @param tooltip The tooltip that we need to fill out
     * @param flag  The flag telling us if we should show advanced or normal tooltips
     */
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        // If the stack has an owner tag, show who owns the stack, otherwise show that the journal is not bound
        if (NBTHelper.hasTag(stack, NBT_OWNER)) {
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.journal.bound", NBTHelper.getString(stack, NBT_OWNER)))
        } else {
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.journal.unbound"))
        }

        // If the journal is a cheat sheet, show that
        if (NBTHelper.hasTag(stack, NBT_CHEAT_SHEET)) {
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.journal.cheatsheet.line1"))
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.journal.cheatsheet.line2"))
        }
    }

    companion object {
        // Two constant tag names, one that tells us who the journal owner is, and one that tells us if the journal is a cheatsheet
        private const val NBT_OWNER = "owner"
        private const val NBT_CHEAT_SHEET = "cheatsheet"
    }
}