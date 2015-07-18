/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	public static final int BLOOD_STAINED_JOURNAL_SIGN_ID = 1;
	public static final int BLOOD_STAINED_JOURNAL_ID = 2;
	public static final int BLOOD_STAINED_JOURNAL_PAGE_ID = 3;
	public static final int BLOOD_STAINED_JOURNAL_PAGE_PRE_ID = 4;
	public static final int TELESCOPE_ID = 5;
	public static final int SEXTANT_ID = 6;
	public static final int VITAE_LANTERN_ID = 7;

	/*
	 * Create GUIS
	 */
	@Override
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z)
	{
		return null;
	}

	/*
	 * Register GUIs
	 */
	@Override
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z)
	{
		switch (ID)
		{
			case GuiHandler.BLOOD_STAINED_JOURNAL_SIGN_ID:
				return new BloodStainedJournalSignGUI();
			case GuiHandler.BLOOD_STAINED_JOURNAL_ID:
				return new BloodStainedJournalResearchGUI();
			case GuiHandler.TELESCOPE_ID:
				return new TelescopeGUI();
			case GuiHandler.SEXTANT_ID:
				return new SextantGUI();
			case GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID:
				return new BloodStainedJournalPageGUI(ClientData.currentlySelected.getResearchDescription(), ClientData.currentlySelected.formattedString(), ClientData.currentlySelected.researchRecipes());
			case GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_PRE_ID:
				return new BloodStainedJournalPageGUI(ClientData.currentlySelected.getPreResearchDescription(), "???", ClientData.currentlySelected.preResearchRecipes());
			case GuiHandler.VITAE_LANTERN_ID:
				return new VitaeLanternGUI();
		}
		return null;
	}

}
