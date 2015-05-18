/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.research;

import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

// All available researches
public enum ResearchTypes
{
	AnUnbreakableCovenant,
	WerewolfExamination, PreWerewolfExamination,
	Crossbow,
	Astronomy1, PreAstronomy1,
	AstralSilver, PreAstralSilver,
	SilverInfusion, PreSilverInfusion,
	Vitae1, PreVitae1,
	SanityLantern, PreSanityLantern,
	VitaeLantern1, PreVitaeLantern1,
	Vampire, PreVampire,
	SunProtection, PreSunprotection,
	SpiderTurn, PreSpiderTurn,
	DarkForest, PreDarkForest,
	CloakOfAgility, PreCloakOfAgility,
	Astronomy2, PreAstronomy2,
	IgneousArmor, PreIgneousArmor,
	StarMetal, PreStarMetal;
	
	public String formattedString()
	{
		String toReturn = "";
		
		for (String string : this.toString().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) 
		{
	        toReturn = toReturn + string + " ";
	    }
		
		return toReturn;
	}
}
