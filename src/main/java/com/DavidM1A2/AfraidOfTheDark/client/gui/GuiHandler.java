/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchDesciptions;

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
				switch (ClientData.currentlySelected)
				{
					case AnUnbreakableCovenant:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.anUnbreakableCovenant, "An Unbreakable Covenant");
					case PreWerewolfExamination:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.preWerewolfExamination, "W~~-wo-+f");
					case WerewolfExamination:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.werewolfExamination, "Werewolf Examination");
					case AstralSilver:
						break;
					case AstronomyI:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.astronomy1, "Astronomy 1");
					case AstronomyII:
						break;
					case CloakOfAgility:
						break;
					case Crossbow:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.crossbow, "Crossbow");
					case DarkForest:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.darkForest, "Dark Forest");
					case IgneousArmor:
						break;
					case PreAstralSilver:
						break;
					case PreAstronomyI:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.preAstronomy1, "Astor -- star");
					case PreAstronomyII:
						break;
					case PreCloakOfAgility:
						break;
					case PreDarkForest:
						return new BloodStainedJournalPageGUI(ResearchDesciptions.preDarkForest, "Fo-rrts");
					case AstralSilverSword:
						break;
					case PreAstralSilverSword:
						break;
					case PreIgneousArmor:
						break;
					case PreSpiderTurn:
						break;
					case PreStarMetal:
						break;
					case PreSunprotection:
						break;
					case PreSunstone:
						break;
					case PreVampire:
						break;
					case PreVitaeI:
						break;
					case PreVitaeLanternI:
						break;
					case SpiderTurn:
						break;
					case StarMetal:
						break;
					case SunProtection:
						break;
					case Sunstone:
						break;
					case Vampire:
						break;
					case VitaeI:
						break;
					case VitaeLanternI:
						break;
					default:
						break;
				}
		}
		return null;
	}

}
