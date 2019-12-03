package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.client.gui.guiScreens.*
import com.davidm1a2.afraidofthedark.client.settings.ClientData
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

/**
 * Gui Handler for AOTD that opens a UI based on ID. It can open UIs sever or client side.
 */
class AOTDGuiHandler : IGuiHandler
{
    /**
     * Returns a container from the server side that allows synchronized editing of tile entities like containers (chests). It's not used for
     * AOTD yet since none of the UIs edit server-side containers
     *
     * @param ID     The ID of the UI to open
     * @param player The player that opened the UI
     * @param world  The world that the UI was opened in
     * @param x      The X location of the player or block that the UI is associated with
     * @param y      The Y location of the player or block that the UI is associated with
     * @param z      The Z location of the player or block that the UI is associated with
     * @return A class that extends Container representing the GUI object to be edited by the user
     */
    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any?
    {
        return when (ID)
        {
            else -> null
        }
    }

    /**
     * Returns a GUI screen for the client to edit
     *
     * @param ID     The ID of the UI to open
     * @param player The player that opened the UI
     * @param world  The world that the UI was opened in
     * @param x      The X location of the player or block that the UI is associated with
     * @param y      The Y location of the player or block that the UI is associated with
     * @param z      The Z location of the player or block that the UI is associated with
     * @return A class that extends GuiScreen representing the GUI object to be edited by the user
     */
    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any?
    {
        // Grab the last selected research used in the page UI
        val lastSelectedResearch = ClientData.lastSelectedResearch
        // Grab the last selected spell used in the spell edit UI
        val lastSelectedSpell = ClientData.lastSelectedSpell
        return when (ID)
        {
            BLOOD_STAINED_JOURNAL_SIGN_ID -> BloodStainedJournalSignGUI()
            BLOOD_STAINED_JOURNAL_ID -> BloodStainedJournalResearchGUI(false)
            BLOOD_STAINED_JOURNAL_CHEAT_SHEET -> BloodStainedJournalResearchGUI(true)
            BLOOD_STAINED_JOURNAL_PAGE_ID -> BloodStainedJournalPageGUI(
                lastSelectedResearch!!.researchedText,
                I18n.format(lastSelectedResearch.unLocalizedName),
                lastSelectedResearch.researchedRecipes
            )
            BLOOD_STAINED_JOURNAL_PAGE_PRE_ID -> BloodStainedJournalPageGUI(
                lastSelectedResearch!!.preResearchedText,
                "???",
                lastSelectedResearch.preResearchedRecipes
            )
            TELESCOPE_ID -> TelescopeGUI()
            SEXTANT_ID -> SextantGUI()
            SPELL_LIST_ID -> SpellListGUI()
            SPELL_CRAFTING_ID -> SpellCraftingGUI(lastSelectedSpell!!)
            else -> null
        }
    }

    companion object
    {
        // Each AOTD GUI has a unique ID:
        const val BLOOD_STAINED_JOURNAL_SIGN_ID = 1
        const val BLOOD_STAINED_JOURNAL_ID = 2
        const val BLOOD_STAINED_JOURNAL_PAGE_ID = 3
        const val BLOOD_STAINED_JOURNAL_PAGE_PRE_ID = 4
        const val TELESCOPE_ID = 5
        const val SEXTANT_ID = 6
        const val BLOOD_STAINED_JOURNAL_CHEAT_SHEET = 8
        const val SPELL_CRAFTING_ID = 9
        const val SPELL_LIST_ID = 10
    }
}