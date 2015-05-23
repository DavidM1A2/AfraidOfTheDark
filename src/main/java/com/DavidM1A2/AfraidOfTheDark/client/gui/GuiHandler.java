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
	public static final int TELESCOPE_ID = 4;
	public static final int SEXTANT_ID = 5;

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
				switch (Refrence.currentlySelected)
				{
					case AnUnbreakableCovenant:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.anUnbreakableCovenant, "An Unbreakable Covenant");
					case PreWerewolfExamination:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.preWerewolfExamination, "W~~-wo-+f");
					case WerewolfExamination:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.werewolfExamination, "Werewolf Examination");
					case AstralSilver:
						break;
					case Astronomy1:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.astronomy1, "Astronomy 1");
					case Astronomy2:
						break;
					case CloakOfAgility:
						break;
					case Crossbow:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.crossbow, "Crossbow");
					case DarkForest:
						break;
					case IgneousArmor:
						break;
					case PreAstralSilver:
						break;
					case PreAstronomy1:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.preAstronomy1, "Astor -- star");
					case PreAstronomy2:
						break;
					case PreCloakOfAgility:
						break;
					case PreDarkForest:
						break;
					case PreIgneousArmor:
						break;
					case PreSanityLantern:
						break;
					case PreSilverInfusion:
						break;
					case PreSpiderTurn:
						break;
					case PreStarMetal:
						break;
					case PreSunprotection:
						break;
					case PreVampire:
						break;
					case PreVitae1:
						break;
					case PreVitaeLantern1:
						break;
					case SanityLantern:
						break;
					case SilverInfusion:
						break;
					case SpiderTurn:
						break;
					case StarMetal:
						break;
					case SunProtection:
						break;
					case Vampire:
						break;
					case Vitae1:
						break;
					case VitaeLantern1:
						break;
					default:
						break;
				}
		}
		return null;
	}

}
