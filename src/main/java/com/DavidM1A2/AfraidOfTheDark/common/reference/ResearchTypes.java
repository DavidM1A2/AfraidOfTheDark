/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.reference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

// All available researches
public enum ResearchTypes
{
	AnUnbreakableCovenant(null, 0, 0, new Object[]
	{}, new Object[]
	{}),
	Crossbow(AnUnbreakableCovenant, 1, 0, new Object[]
	{ ModItems.crossbow, ModItems.woodenBolt, ModItems.ironBolt }, new Object[]
	{}),
	EnchantedSkeleton(AnUnbreakableCovenant, -1, 0, new Object[]
	{ Items.dye }, new Object[]
	{}),
	BladeOfExhumation(EnchantedSkeleton, -2, 0, new Object[]
	{ ModItems.bladeOfExhumation }, new Object[]
	{}),
	WristCrossbow(Crossbow, 2, 0, new Object[]
	{ ModItems.wristCrossbow }, new Object[]
	{}),
	WerewolfExamination(AnUnbreakableCovenant, 0, 1, new Object[]
	{}, new Object[]
	{}),
	AstronomyI(WerewolfExamination, 0, 2, new Object[]
	{ ModItems.telescope, ModItems.sextant }, new Object[]
	{ ModItems.telescope }),
	AstralSilver(AstronomyI, 0, 3, new Object[]
	{ ModItems.astralSilverSword, ModItems.silverBolt }, new Object[]
	{}),
	SlayingOfTheWolves(AstralSilver, -1, 3, new Object[]
	{}, new Object[]
	{}),
	PhylacteryOfSouls(SlayingOfTheWolves, -2, 3, new Object[]
	{ ModItems.flaskOfSouls }, new Object[]
	{ ModItems.flaskOfSouls }),
	VoidChest(AstralSilver, 1, 3, new Object[]
	{ ModBlocks.voidChest }, new Object[]
	{}),
	EldritchDecoration(VoidChest, 2, 3, new Object[]
	{ ModBlocks.eldritchObsidian, ModBlocks.amorphousEldritchMetal, ModBlocks.eldritchStone, ModItems.eldritchMetalIngot }, new Object[]
	{}),
	DarkForest(AstralSilver, 0, 4, new Object[]
	{}, new Object[]
	{}),
	SleepingPotion(DarkForest, -1, 4, new Object[]
	{}, new Object[]
	{}),
	CloakOfAgility(DarkForest, 1, 4, new Object[]
	{ ModItems.cloakOfAgility }, new Object[]
	{}),
	Nightmares(DarkForest, 0, 5, new Object[]
	{}, new Object[]
	{}),
	VitaeI(Nightmares, -1, 5, new Object[]
	{}, new Object[]
	{}),
	VitaeLanternI(VitaeI, -2, 5, new Object[]
	{ ModItems.vitaeLantern }, new Object[]
	{ ModItems.vitaeLantern }),
	Insanity(Nightmares, 1, 5, new Object[]
	{}, new Object[]
	{}),
	VitaeDisenchanter(VitaeLanternI, -3, 5, new Object[]
	{ ModBlocks.vitaeDisenchanter }, new Object[]
	{}),
	DeeeSyft(VitaeLanternI, -2, 6, new Object[]
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
	{}),
	GnomishCity(AstronomyII, 0, 7, new Object[]
	{ ModBlocks.gnomishMetalPlate, ModBlocks.gnomishMetalStrut, ModItems.gnomishMetalIngot }, new Object[]
	{}),
	Enaria(GnomishCity, 1, 7, new Object[]
	{}, new Object[]
	{});

	private ResearchTypes previous = null;
	private ResourceLocation icon = null;
	private String researchDescription = "";
	private String preResearchDescription = "";
	private int x;
	private int y;
	private Object[] researchRecipes;
	private Object[] researchRecipesPre;
	private String toolTip = "";

	private ResearchTypes(final ResearchTypes previous, final int x, final int y, final Object[] researchRecipes, final Object[] researchRecipesPre)
	{
		this.previous = previous;
		this.x = x;
		this.y = y;
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
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

			this.researchDescription = this.loadResearchDescription("assets/afraidofthedark/researchNotes/" + this.toString() + ".txt");
			this.preResearchDescription = this.loadResearchDescription("assets/afraidofthedark/researchNotes/" + this.toString() + "Pre.txt");

			this.researchRecipes = researchRecipes;
			this.researchRecipesPre = researchRecipesPre;

			this.toolTip = this.loadReserachTooltip();
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
		stringBuffer.append("	");
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

		String toReturn = stringBuffer.toString();

		try
		{
			bufferedReader.close();
			inputStream.close();
		}
		catch (IOException e)
		{
			LogHelper.error("Error closing input streams... please report this to the mod author.");
		}

		return toReturn;
	}

	private String loadReserachTooltip()
	{
		BufferedReader bufferedReader = null;
		String toReturn = "";

		InputStream inputStream = Utility.getInputStreamFromPath("assets/afraidofthedark/researchNotes/ResearchToolTips.txt");

		bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String currentLine = null;

		try
		{
			while ((currentLine = bufferedReader.readLine()) != null)
			{
				if (currentLine.length() > this.toString().length() && currentLine.substring(0, this.toString().length()).equals(this.toString()) && currentLine.charAt(this.toString().length()) == ':')
				{
					toReturn = currentLine.substring(this.toString().length() + 1, currentLine.length());
					break;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			bufferedReader.close();
			inputStream.close();
		}
		catch (IOException e)
		{
			LogHelper.error("Error closing input streams... please report this to the mod author.");
		}

		return toReturn;
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

	public String getTooltip()
	{
		return this.toolTip;
	}

	public ResourceLocation getIcon()
	{
		return this.icon;
	}
}
