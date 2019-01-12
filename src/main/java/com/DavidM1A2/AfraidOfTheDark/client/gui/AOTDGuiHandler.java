package com.DavidM1A2.afraidofthedark.client.gui;

import com.DavidM1A2.afraidofthedark.client.gui.guiScreens.BloodStainedJournalResearchGUI;
import com.DavidM1A2.afraidofthedark.client.gui.guiScreens.BloodStainedJournalSignGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Gui Handler for AOTD that opens a UI based on ID. It can open UIs sever or client side.
 */
public class AOTDGuiHandler implements IGuiHandler
{
	// Each AOTD GUI has a unique ID:
	public static final int BLOOD_STAINED_JOURNAL_SIGN_ID = 1;
	public static final int BLOOD_STAINED_JOURNAL_ID = 2;
	public static final int BLOOD_STAINED_JOURNAL_PAGE_ID = 3;
	public static final int BLOOD_STAINED_JOURNAL_PAGE_PRE_ID = 4;
	public static final int TELESCOPE_ID = 5;
	public static final int SEXTANT_ID = 6;
	public static final int BLOOD_STAINED_JOURNAL_CHEAT_SHEET = 8;
	public static final int SPELL_CRAFTING_ID = 9;
	public static final int SPELL_SELECTION_ID = 10;

	/**
	 * Returns a container from the server side that allows synchronized editing of tile entities like containers (chests). It's not used for
	 * AOTD yet since none of the UIs edit server-side containers
	 *
	 * @param ID The ID of the UI to open
	 * @param player The player that opened the UI
	 * @param world The world that the UI was opened in
	 * @param x The X location of the player or block that the UI is associated with
	 * @param y The Y location of the player or block that the UI is associated with
	 * @param z The Z location of the player or block that the UI is associated with
	 * @return A class that extends Container representing the GUI object to be edited by the user
	 */
	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID)
		{
			default:
				return null;
		}
	}

	/**
	 * Returns a GUI screen for the client to edit
	 *
	 * @param ID The ID of the UI to open
	 * @param player The player that opened the UI
	 * @param world The world that the UI was opened in
	 * @param x The X location of the player or block that the UI is associated with
	 * @param y The Y location of the player or block that the UI is associated with
	 * @param z The Z location of the player or block that the UI is associated with
	 * @return A class that extends GuiScreen representing the GUI object to be edited by the user
	 */
	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID)
		{
			case BLOOD_STAINED_JOURNAL_SIGN_ID:
				return new BloodStainedJournalSignGUI();
			case BLOOD_STAINED_JOURNAL_ID:
				return new BloodStainedJournalResearchGUI();
			default:
				return null;
		}
	}
}
