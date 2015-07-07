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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

// All available researches
public enum ResearchTypes
{
	AnUnbreakableCovenant(null, 0, 0, new Object[]
	{}, new Object[]
	{}),
	Crossbow(AnUnbreakableCovenant, 1, 0, new Object[]
	{ ModItems.crossbow, ModItems.woodenBolt, ModItems.ironBolt }, new Object[]
	{}),
	WristCrossbow(Crossbow, 2, 0, new Object[]
	{ ModItems.wristCrossbow }, new Object[]
	{}),
	WerewolfExamination(AnUnbreakableCovenant, 0, 1, new Object[]
	{}, new Object[]
	{}),
	AstronomyI(WerewolfExamination, 0, 2, new Object[]
	{ ModItems.telescope, ModItems.sextant }, new Object[]
	{ ModItems.telescope, ModItems.sextant }),
	AstralSilver(AstronomyI, 0, 3, new Object[]
	{ ModItems.astralSilverSword, ModItems.silverBolt }, new Object[]
	{}),
	VitaeI(AstralSilver, -1, 3, new Object[]
	{}, new Object[]
	{}),
	VitaeLanternI(VitaeI, -2, 3, new Object[]
	{ ModItems.vitaeLantern }, new Object[]
	{ ModItems.vitaeLantern }),
	VitaeDisenchanter(VitaeLanternI, -3, 3, new Object[]
	{ ModBlocks.vitaeDisenchanter }, new Object[]
	{}),
	DeeeSyft(VitaeLanternI, -2, 4, new Object[]
	{}, new Object[]
	{}),
	DarkForest(AstralSilver, 0, 4, new Object[]
	{}, new Object[]
	{}),
	Insanity(DarkForest, 1, 4, new Object[]
	{}, new Object[]
	{}),
	SleepingPotion(DarkForest, -1, 4, new Object[]
	{}, new Object[]
	{}),
	CloakOfAgility(AstronomyI, -1, 2, new Object[]
	{ ModItems.cloakOfAgility }, new Object[]
	{}),
	Nightmares(DarkForest, 0, 5, new Object[]
	{}, new Object[]
	{}),
	AstronomyII(Nightmares, 0, 6, new Object[]
	{}, new Object[]
	{}),
	Igneous(AstronomyII, -1, 6, new Object[]
	{ ModItems.igneousGem, ModBlocks.igneousBlock, ModItems.igneousBolt, ModItems.igneousSword, ModItems.igneousHelmet, ModItems.igneousChestplate, ModItems.igneousLeggings, ModItems.igneousBoots }, new Object[]
	{}),
	StarMetal(AstronomyII, 1, 6, new Object[]
	{ ModItems.starMetalPlate, ModItems.starMetalBolt, ModItems.starMetalKhopesh, ModItems.starMetalHelmet, ModItems.starMetalChestplate, ModItems.starMetalLeggings, ModItems.starMetalBoots, ModItems.starMetalStaff }, new Object[]
	{});

	private ResearchTypes previous = null;
	private ResourceLocation icon = null;
	private String researchDescription = "";
	private String preResearchDescription = "";
	private int x;
	private int y;
	private Object[] researchRecipes;
	private Object[] researchRecipesPre;

	private ResearchTypes(final ResearchTypes previous, final int x, final int y, final Object[] researchRecipes, final Object[] researchRecipesPre)
	{
		LogHelper.info(ModBlocks.igneousBlock);
		LogHelper.info(Item.getItemFromBlock(ModBlocks.igneousBlock));

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

			this.researchRecipes = researchRecipes;
			this.researchRecipesPre = researchRecipesPre;
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

	public Item[] researchRecipes()
	{
		Item[] toReturn = new Item[this.researchRecipes.length];

		for (int i = 0; i < this.researchRecipes.length; i++)
		{
			Object object = this.researchRecipes[i];
			if (object instanceof Item)
			{
				toReturn[i] = (Item) object;
			}
			else
			{
				toReturn[i] = Item.getItemFromBlock((Block) object);
			}
		}

		return toReturn;
	}

	public Item[] preResearchRecipes()
	{
		Item[] toReturn = new Item[this.researchRecipesPre.length];

		for (int i = 0; i < this.researchRecipesPre.length; i++)
		{
			Object object = this.researchRecipesPre[i];
			if (object instanceof Item)
			{
				toReturn[i] = (Item) object;
			}
			else
			{
				toReturn[i] = Item.getItemFromBlock((Block) object);
			}
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
