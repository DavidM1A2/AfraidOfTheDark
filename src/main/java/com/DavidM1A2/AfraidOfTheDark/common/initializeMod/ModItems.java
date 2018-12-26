/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.debug.ItemInsanityControl;
import com.DavidM1A2.AfraidOfTheDark.common.debug.ItemWorldGenTest;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.ItemIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.ItemIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.ItemSilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.ItemStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.ItemWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemArtwork;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemAstralSilverIngot;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemAstralSilverSword;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemBladeOfExhumation;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemCloakOfAgility;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemEldritchMetalIngot;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemEnchantedSkeletonBone;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemFlaskOfSouls;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemGnomishMetalIngot;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemIgneousGem;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemIgneousSword;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemJournal;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemSchematicGenerator;
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
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDArmorMaterials;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
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
	public static final IgneousArmor igneousHelmet = new IgneousArmor(AOTDArmorMaterials.Igneous.getArmorMaterial(), 5, 0);
	public static final IgneousArmor igneousChestplate = new IgneousArmor(AOTDArmorMaterials.Igneous.getArmorMaterial(), 5, 1);
	public static final IgneousArmor igneousLeggings = new IgneousArmor(AOTDArmorMaterials.Igneous.getArmorMaterial(), 5, 2);
	public static final IgneousArmor igneousBoots = new IgneousArmor(AOTDArmorMaterials.Igneous.getArmorMaterial(), 5, 3);
	public static final StarMetalArmor starMetalHelmet = new StarMetalArmor(AOTDArmorMaterials.StarMetal.getArmorMaterial(), 5, 0);
	public static final StarMetalArmor starMetalChestplate = new StarMetalArmor(AOTDArmorMaterials.StarMetal.getArmorMaterial(), 5, 1);
	public static final StarMetalArmor starMetalLeggings = new StarMetalArmor(AOTDArmorMaterials.StarMetal.getArmorMaterial(), 5, 2);
	public static final StarMetalArmor starMetalBoots = new StarMetalArmor(AOTDArmorMaterials.StarMetal.getArmorMaterial(), 5, 3);
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
	public static final ItemGnomishMetalIngot gnomishMetalIngot = new ItemGnomishMetalIngot();
	public static final ItemArtwork artwork = new ItemArtwork();
	public static final ItemSchematicGenerator schematicGenerator = new ItemSchematicGenerator();

	// Register scrolls last
	public static final ItemResearchScrollCloakOfAgility researchScrollCloakOfAgility = new ItemResearchScrollCloakOfAgility();
	public static final ItemResearchScrollAstronomy2 researchScrollAstronomy2 = new ItemResearchScrollAstronomy2();
	public static final ItemResearchScrollVitae1 researchScrollVitae1 = new ItemResearchScrollVitae1();
	public static final ItemResearchScrollWristCrossbow researchScrollWristCrossbow = new ItemResearchScrollWristCrossbow();
	public static final ItemResearchScrollInsanity researchScrollInsanity = new ItemResearchScrollInsanity();

	static
	{
		if (Reference.isDebug)
		{
			insanityControl = new ItemInsanityControl();
			worldGenTest = new ItemWorldGenTest();
		}
	}

	public static void initialize(final Side side)
	{
		// Register items
		if (Reference.isDebug)
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
		GameRegistry.registerItem(ModItems.gnomishMetalIngot, "gnomishMetalIngot");
		GameRegistry.registerItem(ModItems.artwork, "artwork");
		GameRegistry.registerItem(ModItems.schematicGenerator, "schematicGenerator");
	}

	// Register renderers in proxy!
}
