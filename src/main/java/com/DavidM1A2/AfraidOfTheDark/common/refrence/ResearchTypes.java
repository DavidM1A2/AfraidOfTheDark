/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.refrence;

// All available researches
public enum ResearchTypes
{
	AnUnbreakableCovenant(null),
	Crossbow(null),
	WerewolfExamination(AnUnbreakableCovenant),
	PreWerewolfExamination(null),
	AstronomyI(WerewolfExamination),
	PreAstronomyI(null),
	AstralSilver(AstronomyI),
	PreAstralSilver(null),
	AstralSilverSword(AstralSilver),
	PreAstralSilverSword(null),
	VitaeI(AstronomyI),
	PreVitaeI(null),
	VitaeLanternI(VitaeI),
	PreVitaeLanternI(null),
	Vampire(VitaeLanternI),
	PreVampire(null),
	SunProtection(Vampire),
	PreSunprotection(null),
	SpiderTurn(Vampire),
	PreSpiderTurn(null),
	DarkForest(AstralSilver),
	PreDarkForest(null),
	CloakOfAgility(WerewolfExamination),
	PreCloakOfAgility(null),
	AstronomyII(DarkForest),
	PreAstronomyII(null),
	IgneousGem(PreAstronomyII),
	PreIgneousGem(null),
	IgneousArmor(IgneousGem),
	PreIgneousArmor(null),
	StarMetal(AstronomyII),
	PreStarMetal(null);

	private ResearchTypes previous = null;

	private ResearchTypes(final ResearchTypes previous)
	{
		this.previous = previous;
	}

	public String formattedString()
	{
		String toReturn = "";

		for (final String string : this.toString().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
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
