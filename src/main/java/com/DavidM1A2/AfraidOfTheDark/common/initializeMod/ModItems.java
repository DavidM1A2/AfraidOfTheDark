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

import net.minecraft.inventory.EntityEquipmentSlot;
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
	public static final IgneousArmor igneousHelmet = new IgneousArmor(AOTDArmorMaterials.Igneous.getArmorMaterial(), 0, EntityEquipmentSlot.HEAD);
	public static final IgneousArmor igneousChestplate = new IgneousArmor(AOTDArmorMaterials.Igneous.getArmorMaterial(), 1, EntityEquipmentSlot.CHEST);
	public static final IgneousArmor igneousLeggings = new IgneousArmor(AOTDArmorMaterials.Igneous.getArmorMaterial(), 2, EntityEquipmentSlot.LEGS);
	public static final IgneousArmor igneousBoots = new IgneousArmor(AOTDArmorMaterials.Igneous.getArmorMaterial(), 3, EntityEquipmentSlot.FEET);
	public static final StarMetalArmor starMetalHelmet = new StarMetalArmor(AOTDArmorMaterials.StarMetal.getArmorMaterial(), 0, EntityEquipmentSlot.HEAD);
	public static final StarMetalArmor starMetalChestplate = new StarMetalArmor(AOTDArmorMaterials.StarMetal.getArmorMaterial(), 1, EntityEquipmentSlot.CHEST);
	public static final StarMetalArmor starMetalLeggings = new StarMetalArmor(AOTDArmorMaterials.StarMetal.getArmorMaterial(), 2, EntityEquipmentSlot.LEGS);
	public static final StarMetalArmor starMetalBoots = new StarMetalArmor(AOTDArmorMaterials.StarMetal.getArmorMaterial(), 3, EntityEquipmentSlot.FEET);
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
			GameRegistry.register(ModItems.insanityControl);
			GameRegistry.register(ModItems.worldGenTest);
		}

		GameRegistry.register(ModItems.journal);
		GameRegistry.register(ModItems.astralSilverSword);
		GameRegistry.register(ModItems.igneousSword);
		GameRegistry.register(ModItems.astralSilverIngot);
		GameRegistry.register(ModItems.crossbow);
		GameRegistry.register(ModItems.wristCrossbow);
		GameRegistry.register(ModItems.woodenBolt);
		GameRegistry.register(ModItems.ironBolt);
		GameRegistry.register(ModItems.silverBolt);
		GameRegistry.register(ModItems.igneousBolt);
		GameRegistry.register(ModItems.starMetalBolt);
		GameRegistry.register(ModItems.igneousHelmet);
		GameRegistry.register(ModItems.igneousChestplate);
		GameRegistry.register(ModItems.igneousLeggings);
		GameRegistry.register(ModItems.igneousBoots);
		GameRegistry.register(ModItems.igneousGem);
		GameRegistry.register(ModItems.starMetalHelmet);
		GameRegistry.register(ModItems.starMetalChestplate);
		GameRegistry.register(ModItems.starMetalLeggings);
		GameRegistry.register(ModItems.starMetalBoots);
		GameRegistry.register(ModItems.telescope);
		GameRegistry.register(ModItems.sextant);
		GameRegistry.register(ModItems.sunstoneFragment);
		GameRegistry.register(ModItems.starMetalFragment);
		GameRegistry.register(ModItems.starMetalStaff);
		GameRegistry.register(ModItems.vitaeLantern);
		GameRegistry.register(ModItems.starMetalKhopesh);
		GameRegistry.register(ModItems.cloakOfAgility);
		GameRegistry.register(ModItems.starMetalPlate);
		GameRegistry.register(ModItems.enchantedSkeletonBone);
		GameRegistry.register(ModItems.sleepingPotion);
		GameRegistry.register(ModItems.researchScrollCloakOfAgility);
		GameRegistry.register(ModItems.researchScrollAstronomy2);
		GameRegistry.register(ModItems.researchScrollVitae1);
		GameRegistry.register(ModItems.researchScrollWristCrossbow);
		GameRegistry.register(ModItems.researchScrollInsanity);
		GameRegistry.register(ModItems.eldritchMetalIngot);
		GameRegistry.register(ModItems.starMetalIngot);
		GameRegistry.register(ModItems.bladeOfExhumation);
		GameRegistry.register(ModItems.flaskOfSouls);
		GameRegistry.register(ModItems.werewolfBlood);
		GameRegistry.register(ModItems.gnomishMetalIngot);
		GameRegistry.register(ModItems.artwork);
		GameRegistry.register(ModItems.schematicGenerator);
	}

	// Register renderers in proxy!
}
