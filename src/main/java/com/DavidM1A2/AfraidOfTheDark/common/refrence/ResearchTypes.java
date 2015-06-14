/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import net.minecraft.util.ResourceLocation;

// All available researches
public enum ResearchTypes
{
	AnUnbreakableCovenant(null, "afraidofthedark:textures/gui/researchIcons/AnUnbreakableCovenant.png"),
	Crossbow(null, "afraidofthedark:textures/gui/researchIcons/Crossbow.png"),
	WristCrossbow(Crossbow, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreWristCrossbow(null, null),
	WerewolfExamination(AnUnbreakableCovenant, "afraidofthedark:textures/gui/researchIcons/WerewolfExamination.png"),
	PreWerewolfExamination(null, null),
	AstronomyI(WerewolfExamination, "afraidofthedark:textures/gui/researchIcons/AstronomyI.png"),
	PreAstronomyI(null, null),
	AstralSilver(AstronomyI, "afraidofthedark:textures/gui/researchIcons/AstralSilver.png"),
	PreAstralSilver(null, null),
	AstralSilverSword(AstralSilver, "afraidofthedark:textures/gui/researchIcons/AstralSilverSword.png"),
	PreAstralSilverSword(null, null),
	VitaeI(AstronomyI, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreVitaeI(null, null),
	VitaeLanternI(VitaeI, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreVitaeLanternI(null, null),
	Vampire(VitaeLanternI, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreVampire(null, null),
	SunProtection(Vampire, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreSunprotection(null, null),
	SpiderTurn(Vampire, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreSpiderTurn(null, null),
	DarkForest(AstralSilver, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreDarkForest(null, null),
	CloakOfAgility(WerewolfExamination, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreCloakOfAgility(null, null),
	AstronomyII(DarkForest, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreAstronomyII(null, null),
	Sunstone(AstronomyII, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreSunstone(null, null),
	IgneousArmor(Sunstone, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreIgneousArmor(null, null),
	StarMetal(AstronomyII, "afraidofthedark:textures/gui/researchIcons/None.png"),
	PreStarMetal(null, null);

	private ResearchTypes previous = null;
	private ResourceLocation icon = null;

	private ResearchTypes(final ResearchTypes previous, final String icon)
	{
		this.previous = previous;
		if (icon != null)
		{
			this.icon = new ResourceLocation(icon);
		}
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

	public ResourceLocation getIcon()
	{
		return this.icon;
	}
}
