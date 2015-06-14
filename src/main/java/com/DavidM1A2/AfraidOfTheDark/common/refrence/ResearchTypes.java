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
	Crossbow(AnUnbreakableCovenant, 1, 0),
	WristCrossbow(Crossbow, 2, 0),
	WerewolfExamination(AnUnbreakableCovenant, 0, 1),
	AstronomyI(WerewolfExamination, 0, 2),
	AstralSilver(AstronomyI, 0, 3),
	VitaeI(AstronomyI, -1, 3),
	VitaeLanternI(VitaeI, -2, 3),
	DarkForest(AstralSilver, 0, 4),
	CloakOfAgility(WerewolfExamination, -1, 2),
	AstronomyII(DarkForest, 0, 5),
	IgneousArmor(AstronomyII, -1, 5),
	StarMetal(AstronomyII, 1, 5);

	private ResearchTypes previous = null;
	private ResourceLocation icon = null;
	private String researchDescription = "";
	private String preResearchDescription = "";
	private int x;
	private int y;

	private ResearchTypes(final ResearchTypes previous, final int x, final int y)
	{
		this.previous = previous;
		this.x = x;
		this.y = y;

		this.icon = new ResourceLocation("afraidofthedark:textures/gui/researchIcons/" + this.toString() + ".png");

		try
		{
			Minecraft.getMinecraft().getResourceManager().getResource(this.icon);
		}
		catch (IOException e)
		{
			this.icon = new ResourceLocation("afraidofthedark:textures/gui/researchIcons/None.png");
		}

		this.researchDescription = loadResearchDescription("assets/afraidofthedark/researchNotes/" + this.toString() + ".txt");
		this.preResearchDescription = loadResearchDescription("assets/afraidofthedark/researchNotes/Pre" + this.toString() + ".txt");
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

	private String loadResearchDescription(String path)
	{
		BufferedReader bufferedReader = null;

		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
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

		return stringBuffer.toString();
	}

	public String getResearchDescription()
	{
		return this.researchDescription;
	}

	public String getPreResearchDescription()
	{
		return this.preResearchDescription;
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
