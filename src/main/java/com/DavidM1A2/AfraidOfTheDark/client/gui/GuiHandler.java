/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchDesciptions;

public class GuiHandler implements IGuiHandler
{
	public static final int BLOOD_STAINED_JOURNAL_SIGN_ID = 1;
	public static final int BLOOD_STAINED_JOURNAL_ID = 2;
	public static final int BLOOD_STAINED_JOURNAL_PAGE_ID = 3;

	/*
	 * Create GUIS
	 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	/*
	 * Register GUIs
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == BLOOD_STAINED_JOURNAL_SIGN_ID)
		{
			return new BloodStainedJournalSignGUI();
		}
		else if (ID == BLOOD_STAINED_JOURNAL_ID)
		{
			return new BloodStainedJournalResearchGUI();
		}
		else if (ID == BLOOD_STAINED_JOURNAL_PAGE_ID)
		{
			switch (Refrence.currentlySelected)
			{
				case AnUnbreakableCovenant:
					return new BloodStainedJournalPageGUI(ResearchDesciptions.anUnbreakableCovenant, "An Unbreakable Covenant");
				case PreWerewolfExamination:
					return new BloodStainedJournalPageGUI(ResearchDesciptions.preWerewolfExamination, "W~~-wo-+f");
				case WerewolfExamination:
					return new BloodStainedJournalPageGUI(ResearchDesciptions.werewolfExamination, "Werewolf Examination");
				case PreInfusingSilver:
					break;
				case InfusingSilver:
					break;
				case PreRefiningSilver:
					break;
				case RefiningSilver:
					break;
				default:
					break;

			}
		}
		return null;
	}

}
