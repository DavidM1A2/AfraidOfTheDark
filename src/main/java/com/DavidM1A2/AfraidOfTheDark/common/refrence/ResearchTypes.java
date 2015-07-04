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
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

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
	VitaeDisenchanter(VitaeLanternI, -3, 3),
	DeeeSyft(VitaeLanternI, -2, 4),
	DarkForest(AstralSilver, 0, 4),
	Insanity(DarkForest, 1, 4),
	SleepingPotion(DarkForest, -1, 4),
	CloakOfAgility(WerewolfExamination, -1, 2),
	Nightmares(DarkForest, 0, 5),
	AstronomyII(Nightmares, 0, 6),
	Igneous(AstronomyII, -1, 6),
	StarMetal(AstronomyII, 1, 6);

	private ResearchTypes previous = null;
	private ResourceLocation icon = null;
	private String researchDescription = "";
	private String preResearchDescription = "";
	private int x;
	private int y;

	private ResearchTypes(final ResearchTypes previous, final int x, final int y)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
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
			this.preResearchDescription = loadResearchDescription("assets/afraidofthedark/researchNotes/" + this.toString() + "Pre.txt");
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

	private String loadResearchDescription(String path)
	{
		BufferedReader bufferedReader = null;

		InputStream inputStream = Utility.getInputStreamFromPath(path);

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
