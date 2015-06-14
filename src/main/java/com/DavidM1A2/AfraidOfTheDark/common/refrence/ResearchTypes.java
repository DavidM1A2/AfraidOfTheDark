/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

// All available researches
public enum ResearchTypes
{
	AnUnbreakableCovenant(null, 0, 0),
	Crossbow(null, 1, 0),
	WristCrossbow(Crossbow, 2, 0),
	PreWristCrossbow(null, 0, 0),
	WerewolfExamination(AnUnbreakableCovenant, 0, 1),
	PreWerewolfExamination(null, 0, 0),
	AstronomyI(WerewolfExamination, 0, 2),
	PreAstronomyI(null, 0, 0),
	AstralSilver(AstronomyI, 0, 3),
	PreAstralSilver(null, 0, 0),
	VitaeI(AstronomyI, -1, 3),
	PreVitaeI(null, 0, 0),
	VitaeLanternI(VitaeI, -2, 3),
	PreVitaeLanternI(null, 0, 0),
	DarkForest(AstralSilver, 0, 4),
	PreDarkForest(null, 0, 0),
	CloakOfAgility(WerewolfExamination, -1, 2),
	PreCloakOfAgility(null, 0, 0),
	AstronomyII(DarkForest, 0, 5),
	PreAstronomyII(null, 0, 0),
	IgneousArmor(AstronomyII, -1, 5),
	PreIgneousArmor(null, 0, 0),
	StarMetal(AstronomyII, 1, 5),
	PreStarMetal(null, 0, 0);

	private ResearchTypes previous = null;
	private ResourceLocation icon = null;
	private String researchDescription = "";
	private int x;
	private int y;

	private ResearchTypes(final ResearchTypes previous, final int x, final int y)
	{
		this.previous = previous;
		this.x = x;
		this.y = y;

		if (!this.toString().startsWith("Pre"))
		{
			this.icon = new ResourceLocation("afraidofthedark:textures/gui/researchIcons/" + this.toString() + ".png");

			try
			{
				Minecraft.getMinecraft().getResourceManager().getResource(this.icon);
			}
			catch (IOException e)
			{
				this.icon = new ResourceLocation("afraidofthedark:textures/gui/researchIcons/None.png");
			}
		}
		else
		{
			this.icon = null;
		}

		BufferedReader bufferedReader = null;

		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/afraidofthedark/researchNotes/" + this.toString() + ".txt");
		if (inputStream == null)
		{
			inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/afraidofthedark/researchNotes/None.txt");
		}
		bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("     ");
		String currentLine = null;

		try
		{
			while ((currentLine = bufferedReader.readLine()) != null)
			{
				stringBuffer.append(currentLine).append(" ");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		this.researchDescription = stringBuffer.toString();
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

	public String getResearchDescription()
	{
		return this.researchDescription;
	}

	public ResearchTypes getPrevious()
	{
		return this.previous;
	}

	public int getPositionX()
	{
		return this.x;
	}

	public int getPositionY()
	{
		return this.y;
	}

	public static boolean researchTypeExists(int x, int y)
	{
		for (ResearchTypes researchType : values())
		{
			if (researchType.getPositionX() == x && researchType.getPositionY() == y)
			{
				return true;
			}
		}
		return false;
	}

	public ResourceLocation getIcon()
	{
		return this.icon;
	}
}
