/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.debug.ItemInsanityControl;
import com.DavidM1A2.AfraidOfTheDark.common.debug.ItemWorldGenTest;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.ItemIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.ItemIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.ItemSilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.ItemStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.ItemWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemAstralSilverIngot;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemAstralSilverSword;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemBladeOfExhumation;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemCloakOfAgility;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemEldritchMetalIngot;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemEnchantedSkeletonBone;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemFlaskOfSouls;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemIgneousGem;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemIgneousSword;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemJournal;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemSextant;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemSleepingPotion;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemStarMetalFragment;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemStarMetalIngot;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemStarMetalKhopesh;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemStarMetalPlate;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemStarMetalStaff;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemSunstoneFragment;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemTelescope;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemVitaeLantern;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemWerewolfBlood;
import com.DavidM1A2.AfraidOfTheDark.common.item.armor.IgneousArmor;
import com.DavidM1A2.AfraidOfTheDark.common.item.armor.StarMetalArmor;
import com.DavidM1A2.AfraidOfTheDark.common.item.crossbow.ItemCrossbow;
import com.DavidM1A2.AfraidOfTheDark.common.item.crossbow.ItemWristCrossbow;
import com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls.ItemResearchScrollAstronomy2;
import com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls.ItemResearchScrollCloakOfAgility;
import com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls.ItemResearchScrollInsanity;
import com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls.ItemResearchScrollVitae1;
import com.DavidM1A2.AfraidOfTheDark.common.item.researchScrolls.ItemResearchScrollWristCrossbow;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@GameRegistry.ObjectHolder(Refrence.MOD_ID)
public class ModItems
{
	public static ItemInsanityControl insanityControl;
	public static ItemWorldGenTest worldGenTest;

	// Register mod items
	public static final ItemJournal journal = new ItemJournal();
	public static final ItemAstralSilverSword astralSilverSword = new ItemAstralSilverSword();
	public static final ItemIgneousSword igneousSword = new ItemIgneousSword();
	public static final ItemAstralSilverIngot astralSilverIngot = new ItemAstralSilverIngot();
	public static final ItemCrossbow crossbow = new ItemCrossbow();
	public static final ItemWristCrossbow wristCrossbow = new ItemWristCrossbow();
	public static final ItemWoodenBolt woodenBolt = new ItemWoodenBolt();
	public static final ItemIronBolt ironBolt = new ItemIronBolt();
	public static final ItemSilverBolt silverBolt = new ItemSilverBolt();
	public static final ItemIgneousBolt igneousBolt = new ItemIgneousBolt();
	public static final ItemStarMetalBolt starMetalBolt = new ItemStarMetalBolt();
	public static final IgneousArmor igneousHelmet = new IgneousArmor(Constants.AOTDArmorMaterials.igneous, 5, 0);
	public static final IgneousArmor igneousChestplate = new IgneousArmor(Constants.AOTDArmorMaterials.igneous, 5, 1);
	public static final IgneousArmor igneousLeggings = new IgneousArmor(Constants.AOTDArmorMaterials.igneous, 5, 2);
	public static final IgneousArmor igneousBoots = new IgneousArmor(Constants.AOTDArmorMaterials.igneous, 5, 3);
	public static final StarMetalArmor starMetalHelmet = new StarMetalArmor(Constants.AOTDArmorMaterials.starMetal, 5, 0);
	public static final StarMetalArmor starMetalChestplate = new StarMetalArmor(Constants.AOTDArmorMaterials.starMetal, 5, 1);
	public static final StarMetalArmor starMetalLeggings = new StarMetalArmor(Constants.AOTDArmorMaterials.starMetal, 5, 2);
	public static final StarMetalArmor starMetalBoots = new StarMetalArmor(Constants.AOTDArmorMaterials.starMetal, 5, 3);
	public static final ItemIgneousGem igneousGem = new ItemIgneousGem();
	public static final ItemTelescope telescope = new ItemTelescope();
	public static final ItemSextant sextant = new ItemSextant();
	public static final ItemSunstoneFragment sunstoneFragment = new ItemSunstoneFragment();
	public static final ItemStarMetalFragment starMetalFragment = new ItemStarMetalFragment();
	public static final ItemStarMetalStaff starMetalStaff = new ItemStarMetalStaff();
	public static final ItemVitaeLantern vitaeLantern = new ItemVitaeLantern();
	public static final ItemStarMetalKhopesh starMetalKhopesh = new ItemStarMetalKhopesh();
	public static final ItemCloakOfAgility cloakOfAgility = new ItemCloakOfAgility();
	public static final ItemStarMetalPlate starMetalPlate = new ItemStarMetalPlate();
	public static final ItemEnchantedSkeletonBone enchantedSkeletonBone = new ItemEnchantedSkeletonBone();
	public static final ItemSleepingPotion sleepingPotion = new ItemSleepingPotion();
	public static final ItemEldritchMetalIngot eldritchMetalIngot = new ItemEldritchMetalIngot();
	public static final ItemStarMetalIngot starMetalIngot = new ItemStarMetalIngot();
	public static final ItemBladeOfExhumation bladeOfExhumation = new ItemBladeOfExhumation();
	public static final ItemFlaskOfSouls flaskOfSouls = new ItemFlaskOfSouls();
	public static final ItemWerewolfBlood werewolfBlood = new ItemWerewolfBlood();

	// Register scrolls last
	public static final ItemResearchScrollCloakOfAgility researchScrollCloakOfAgility = new ItemResearchScrollCloakOfAgility();
	public static final ItemResearchScrollAstronomy2 researchScrollAstronomy2 = new ItemResearchScrollAstronomy2();
	public static final ItemResearchScrollVitae1 researchScrollVitae1 = new ItemResearchScrollVitae1();
	public static final ItemResearchScrollWristCrossbow researchScrollWristCrossbow = new ItemResearchScrollWristCrossbow();
	public static final ItemResearchScrollInsanity researchScrollInsanity = new ItemResearchScrollInsanity();

	static
	{
		if (Constants.isDebug)
		{
			insanityControl = new ItemInsanityControl();
			worldGenTest = new ItemWorldGenTest();
		}
	}

	public static void initialize(final Side side)
	{
		// Register items
		if (Constants.isDebug)
		{
			GameRegistry.registerItem(ModItems.insanityControl, "insanityControl");
			GameRegistry.registerItem(ModItems.worldGenTest, "worldGenTest");
		}

		GameRegistry.registerItem(ModItems.journal, "journal");
		GameRegistry.registerItem(ModItems.astralSilverSword, "astralSilverSword");
		GameRegistry.registerItem(ModItems.igneousSword, "igneousSword");
		GameRegistry.registerItem(ModItems.astralSilverIngot, "astralSilverIngot");
		GameRegistry.registerItem(ModItems.crossbow, "crossbow");
		GameRegistry.registerItem(ModItems.wristCrossbow, "wristCrossbow");
		GameRegistry.registerItem(ModItems.woodenBolt, "woodenBolt");
		GameRegistry.registerItem(ModItems.ironBolt, "ironBolt");
		GameRegistry.registerItem(ModItems.silverBolt, "silverBolt");
		GameRegistry.registerItem(ModItems.igneousBolt, "igneousBolt");
		GameRegistry.registerItem(ModItems.starMetalBolt, "starMetalBolt");
		GameRegistry.registerItem(ModItems.igneousHelmet, "igneousHelmet");
		GameRegistry.registerItem(ModItems.igneousChestplate, "igneousChestplate");
		GameRegistry.registerItem(ModItems.igneousLeggings, "igneousLeggings");
		GameRegistry.registerItem(ModItems.igneousBoots, "igneousBoots");
		GameRegistry.registerItem(ModItems.igneousGem, "igneousGem");
		GameRegistry.registerItem(ModItems.starMetalHelmet, "starMetalHelmet");
		GameRegistry.registerItem(ModItems.starMetalChestplate, "starMetalChestplate");
		GameRegistry.registerItem(ModItems.starMetalLeggings, "starMetalLeggings");
		GameRegistry.registerItem(ModItems.starMetalBoots, "starMetalBoots");
		GameRegistry.registerItem(ModItems.telescope, "telescope");
		GameRegistry.registerItem(ModItems.sextant, "sextant");
		GameRegistry.registerItem(ModItems.sunstoneFragment, "sunstoneFragment");
		GameRegistry.registerItem(ModItems.starMetalFragment, "starMetalFragment");
		GameRegistry.registerItem(ModItems.starMetalStaff, "starMetalStaff");
		GameRegistry.registerItem(ModItems.vitaeLantern, "vitaeLantern");
		GameRegistry.registerItem(ModItems.starMetalKhopesh, "starMetalKhopesh");
		GameRegistry.registerItem(ModItems.cloakOfAgility, "cloakOfAgility");
		GameRegistry.registerItem(ModItems.starMetalPlate, "starMetalPlate");
		GameRegistry.registerItem(ModItems.enchantedSkeletonBone, "enchantedSkeletonBone");
		GameRegistry.registerItem(ModItems.sleepingPotion, "sleepingPotion");
		GameRegistry.registerItem(ModItems.researchScrollCloakOfAgility, "researchScrollCloakOfAgility");
		GameRegistry.registerItem(ModItems.researchScrollAstronomy2, "researchScrollAstronomy2");
		GameRegistry.registerItem(ModItems.researchScrollVitae1, "researchScrollVitae1");
		GameRegistry.registerItem(ModItems.researchScrollWristCrossbow, "researchScrollWristCrossbow");
		GameRegistry.registerItem(ModItems.researchScrollInsanity, "researchScrollInsanity");
		GameRegistry.registerItem(ModItems.eldritchMetalIngot, "eldritchMetalIngot");
		GameRegistry.registerItem(ModItems.starMetalIngot, "starMetalIngot");
		GameRegistry.registerItem(ModItems.bladeOfExhumation, "bladeOfExhumation");
		GameRegistry.registerItem(ModItems.flaskOfSouls, "flaskOfSouls");
		GameRegistry.registerItem(ModItems.werewolfBlood, "werewolfBlood");
	}

	public static void initializeRenderers(final Side side)
	{
		if (side == Side.CLIENT)
		{
			final ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

			if (Constants.isDebug)
			{
				itemModelMesher.register(ModItems.insanityControl, 0, new ModelResourceLocation(Refrence.MOD_ID + ":insanityControl", "inventory"));
				itemModelMesher.register(ModItems.worldGenTest, 0, new ModelResourceLocation(Refrence.MOD_ID + ":worldGenTest", "inventory"));
			}

			itemModelMesher.register(ModItems.journal, 0, new ModelResourceLocation(Refrence.MOD_ID + ":journal", "inventory"));
			itemModelMesher.register(ModItems.journal, 1, new ModelResourceLocation(Refrence.MOD_ID + ":journal", "inventory"));

			itemModelMesher.register(ModItems.astralSilverSword, 0, new ModelResourceLocation(Refrence.MOD_ID + ":astralSilverSword", "inventory"));
			itemModelMesher.register(ModItems.igneousSword, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousSword", "inventory"));
			itemModelMesher.register(ModItems.igneousSword, 1, new ModelResourceLocation(Refrence.MOD_ID + ":igneousSwordFullCharge", "inventory"));
			ModelBakery.addVariantName(ModItems.igneousSword, Refrence.MOD_ID + ":igneousSword", Refrence.MOD_ID + ":igneousSwordFullCharge");
			itemModelMesher.register(ModItems.astralSilverIngot, 0, new ModelResourceLocation(Refrence.MOD_ID + ":astralSilverIngot", "inventory"));

			itemModelMesher.register(ModItems.crossbow, 0, new ModelResourceLocation(Refrence.MOD_ID + ":crossbowUnloaded", "inventory"));
			itemModelMesher.register(ModItems.crossbow, 1, new ModelResourceLocation(Refrence.MOD_ID + ":crossbowQuarter", "inventory"));
			itemModelMesher.register(ModItems.crossbow, 2, new ModelResourceLocation(Refrence.MOD_ID + ":crossbowHalf", "inventory"));
			itemModelMesher.register(ModItems.crossbow, 3, new ModelResourceLocation(Refrence.MOD_ID + ":crossbowLoaded", "inventory"));
			ModelBakery.addVariantName(ModItems.crossbow, Refrence.MOD_ID + ":crossbowUnloaded", Refrence.MOD_ID + ":crossbowQuarter", Refrence.MOD_ID + ":crossbowHalf", Refrence.MOD_ID + ":crossbowLoaded");

			itemModelMesher.register(ModItems.wristCrossbow, 0, new ModelResourceLocation(Refrence.MOD_ID + ":wristCrossbow", "inventory"));
			itemModelMesher.register(ModItems.woodenBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":woodenBolt", "inventory"));
			itemModelMesher.register(ModItems.ironBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":ironBolt", "inventory"));
			itemModelMesher.register(ModItems.silverBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":silverBolt", "inventory"));
			itemModelMesher.register(ModItems.igneousBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousBolt", "inventory"));
			itemModelMesher.register(ModItems.starMetalBolt, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalBolt", "inventory"));
			itemModelMesher.register(ModItems.igneousHelmet, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousHelmet", "inventory"));
			itemModelMesher.register(ModItems.igneousChestplate, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousChestplate", "inventory"));
			itemModelMesher.register(ModItems.igneousLeggings, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousLeggings", "inventory"));
			itemModelMesher.register(ModItems.igneousBoots, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousBoots", "inventory"));
			itemModelMesher.register(ModItems.igneousGem, 0, new ModelResourceLocation(Refrence.MOD_ID + ":igneousGem", "inventory"));
			itemModelMesher.register(ModItems.starMetalHelmet, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalHelmet", "inventory"));
			itemModelMesher.register(ModItems.starMetalChestplate, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalChestplate", "inventory"));
			itemModelMesher.register(ModItems.starMetalLeggings, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalLeggings", "inventory"));
			itemModelMesher.register(ModItems.starMetalBoots, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalBoots", "inventory"));
			itemModelMesher.register(ModItems.telescope, 0, new ModelResourceLocation(Refrence.MOD_ID + ":telescope", "inventory"));
			itemModelMesher.register(ModItems.sextant, 0, new ModelResourceLocation(Refrence.MOD_ID + ":sextant", "inventory"));
			itemModelMesher.register(ModItems.sunstoneFragment, 0, new ModelResourceLocation(Refrence.MOD_ID + ":sunstoneFragment", "inventory"));
			itemModelMesher.register(ModItems.starMetalFragment, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalFragment", "inventory"));
			itemModelMesher.register(ModItems.starMetalStaff, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalStaff", "inventory"));
			itemModelMesher.register(ModItems.vitaeLantern, 0, new ModelResourceLocation(Refrence.MOD_ID + ":vitaeLanternEmpty", "inventory"));
			itemModelMesher.register(ModItems.vitaeLantern, 1, new ModelResourceLocation(Refrence.MOD_ID + ":vitaeLanternLow", "inventory"));
			itemModelMesher.register(ModItems.vitaeLantern, 2, new ModelResourceLocation(Refrence.MOD_ID + ":vitaeLanternHalf", "inventory"));
			itemModelMesher.register(ModItems.vitaeLantern, 3, new ModelResourceLocation(Refrence.MOD_ID + ":vitaeLanternThreeQuarters", "inventory"));
			itemModelMesher.register(ModItems.vitaeLantern, 4, new ModelResourceLocation(Refrence.MOD_ID + ":vitaeLanternFull", "inventory"));
			ModelBakery.addVariantName(ModItems.vitaeLantern, Refrence.MOD_ID + ":vitaeLanternEmpty", Refrence.MOD_ID + ":vitaeLanternLow", Refrence.MOD_ID + ":vitaeLanternHalf", Refrence.MOD_ID + ":vitaeLanternThreeQuarters", Refrence.MOD_ID + ":vitaeLanternFull");
			itemModelMesher.register(ModItems.starMetalKhopesh, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalKhopesh", "inventory"));
			itemModelMesher.register(ModItems.starMetalKhopesh, 1, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalKhopeshFullCharge", "inventory"));
			ModelBakery.addVariantName(ModItems.starMetalKhopesh, Refrence.MOD_ID + ":starMetalKhopesh", Refrence.MOD_ID + ":starMetalKhopeshFullCharge");
			itemModelMesher.register(ModItems.cloakOfAgility, 0, new ModelResourceLocation(Refrence.MOD_ID + ":cloakOfAgility", "inventory"));
			itemModelMesher.register(ModItems.researchScrollCloakOfAgility, 0, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollCloakOfAgility", "inventory"));
			itemModelMesher.register(ModItems.researchScrollAstronomy2, 0, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollAstronomy2", "inventory"));
			itemModelMesher.register(ModItems.researchScrollAstronomy2, 1, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollAstronomy2", "inventory"));
			itemModelMesher.register(ModItems.researchScrollAstronomy2, 2, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollAstronomy2", "inventory"));
			itemModelMesher.register(ModItems.researchScrollAstronomy2, 3, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollAstronomy2", "inventory"));
			itemModelMesher.register(ModItems.researchScrollAstronomy2, 4, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollAstronomy2", "inventory"));
			itemModelMesher.register(ModItems.researchScrollVitae1, 0, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollVitae1", "inventory"));
			itemModelMesher.register(ModItems.researchScrollVitae1, 1, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollVitae1", "inventory"));
			itemModelMesher.register(ModItems.researchScrollVitae1, 2, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollVitae1", "inventory"));
			itemModelMesher.register(ModItems.researchScrollVitae1, 3, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollVitae1", "inventory"));
			itemModelMesher.register(ModItems.researchScrollVitae1, 4, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollVitae1", "inventory"));
			itemModelMesher.register(ModItems.researchScrollVitae1, 5, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollVitae1", "inventory"));
			itemModelMesher.register(ModItems.researchScrollWristCrossbow, 0, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollWristCrossbow", "inventory"));
			itemModelMesher.register(ModItems.starMetalPlate, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalPlate", "inventory"));
			itemModelMesher.register(ModItems.enchantedSkeletonBone, 0, new ModelResourceLocation(Refrence.MOD_ID + ":enchantedSkeletonBone", "inventory"));
			itemModelMesher.register(ModItems.sleepingPotion, 0, new ModelResourceLocation(Refrence.MOD_ID + ":sleepingPotion", "inventory"));
			itemModelMesher.register(ModItems.researchScrollInsanity, 0, new ModelResourceLocation(Refrence.MOD_ID + ":researchScrollInsanity", "inventory"));
			itemModelMesher.register(ModItems.eldritchMetalIngot, 0, new ModelResourceLocation(Refrence.MOD_ID + ":eldritchMetalIngot", "inventory"));
			itemModelMesher.register(ModItems.starMetalIngot, 0, new ModelResourceLocation(Refrence.MOD_ID + ":starMetalIngot", "inventory"));
			itemModelMesher.register(ModItems.bladeOfExhumation, 0, new ModelResourceLocation(Refrence.MOD_ID + ":bladeOfExhumation", "inventory"));
			itemModelMesher.register(ModItems.flaskOfSouls, 0, new ModelResourceLocation(Refrence.MOD_ID + ":flaskOfSouls", "inventory"));
			itemModelMesher.register(ModItems.flaskOfSouls, 1, new ModelResourceLocation(Refrence.MOD_ID + ":flaskOfSoulsCharged", "inventory"));
			ModelBakery.addVariantName(ModItems.flaskOfSouls, Refrence.MOD_ID + ":flaskOfSouls", Refrence.MOD_ID + ":flaskOfSoulsCharged");
			itemModelMesher.register(ModItems.werewolfBlood, 0, new ModelResourceLocation(Refrence.MOD_ID + ":werewolfBlood", "inventory"));
		}
	}
}
