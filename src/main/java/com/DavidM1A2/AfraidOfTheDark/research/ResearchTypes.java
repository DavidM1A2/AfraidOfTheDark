/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.research;


// All available researches
public enum ResearchTypes
{
	AnUnbreakableCovenant(null), Crossbow(null), WerewolfExamination(AnUnbreakableCovenant), PreWerewolfExamination(null), Astronomy1(WerewolfExamination), PreAstronomy1(null), AstralSilver(Astronomy1), PreAstralSilver(null), SilverInfusion(AstralSilver), PreSilverInfusion(null), Vitae1(Astronomy1), PreVitae1(
			null), SanityLantern(Vitae1), PreSanityLantern(null), VitaeLantern1(SanityLantern), PreVitaeLantern1(null), Vampire(VitaeLantern1), PreVampire(null), SunProtection(Vampire), PreSunprotection(null), SpiderTurn(Vampire), PreSpiderTurn(null), DarkForest(AstralSilver), PreDarkForest(null), CloakOfAgility(
			DarkForest), PreCloakOfAgility(null), Astronomy2(DarkForest), PreAstronomy2(null), IgneousArmor(Astronomy2), PreIgneousArmor(null), StarMetal(Astronomy2), PreStarMetal(null);

	private ResearchTypes previous = null;

	private ResearchTypes(ResearchTypes previous)
	{
		this.previous = previous;
	}

	public String formattedString()
	{
		String toReturn = "";

		for (String string : this.toString().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
		{
			toReturn = toReturn + string + " ";
		}

		return toReturn;
	}

	public ResearchTypes getPrevious()
	{
		return this.previous;
	}
}
