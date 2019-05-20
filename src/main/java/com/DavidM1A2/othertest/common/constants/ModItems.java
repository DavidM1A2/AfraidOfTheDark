package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.item.*;
import com.DavidM1A2.afraidofthedark.common.item.crossbow.ItemCrossbow;
import com.DavidM1A2.afraidofthedark.common.item.crossbow.ItemWristCrossbow;
import com.DavidM1A2.afraidofthedark.common.item.crossbow.bolts.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

/**
 * A static class containing all of our item references for us
 */
public class ModItems
{
    public static final ItemJournal JOURNAL = new ItemJournal();
    public static final Item ENCHANTED_SKELETON_BONE = new ItemEnchantedSkeletonBone();
    public static final Item BLADE_OF_EXHUMATION = new ItemBladeOfExhumation();
    public static final Item CROSSBOW = new ItemCrossbow();
    public static final Item WRIST_CROSSBOW = new ItemWristCrossbow();
    public static final Item RESEARCH_SCROLL = new ItemResearchScroll();
    public static final Item TELESCOPE = new ItemTelescope();
    public static final Item SEXTANT = new ItemSextant();
    public static final Item ASTRAL_SILVER_INGOT = new ItemAstralSilverIngot();
    public static final Item ASTRAL_SILVER_SWORD = new ItemAstralSilverSword();
    public static final Item WEREWOLF_BLOOD = new ItemWerewolfBlood();
    public static final Item FLASK_OF_SOULS = new ItemFlaskOfSouls();
    public static final Item CLOAK_OF_AGILITY = new ItemCloakOfAgility();
    public static final Item ELDRITCH_METAL_INGOT = new ItemEldritchMetalIngot();
    public static final Item SLEEPING_POTION = new ItemSleepingPotion();

    public static final Item SUNSTONE_FRAGMENT = new ItemSunstoneFragment();
    public static final Item IGNEOUS_GEM = new ItemIgneousGem();
    public static final Item IGNEOUS_SWORD = new ItemIgneousSword();
    public static final Item IGNEOUS_HELMET = new ItemIgneousArmor("igneous_helmet", EntityEquipmentSlot.HEAD);
    public static final Item IGNEOUS_CHESTPLATE = new ItemIgneousArmor("igneous_chestplate", EntityEquipmentSlot.CHEST);
    public static final Item IGNEOUS_LEGGINGS = new ItemIgneousArmor("igneous_leggings", EntityEquipmentSlot.LEGS);
    public static final Item IGNEOUS_BOOTS = new ItemIgneousArmor("igneous_boots", EntityEquipmentSlot.FEET);

    public static final Item STAR_METAL_FRAGMENT = new ItemStarMetalFragment();
    public static final Item STAR_METAL_INGOT = new ItemStarMetalIngot();
    public static final Item STAR_METAL_PLATE = new ItemStarMetalPlate();
    public static final Item STAR_METAL_KHOPESH = new ItemStarMetalKhopesh();
    public static final Item STAR_METAL_STAFF = new ItemStarMetalStaff();
    public static final Item STAR_METAL_HELMET = new ItemStarMetalArmor("star_metal_helmet", EntityEquipmentSlot.HEAD);
    public static final Item STAR_METAL_CHESTPLATE = new ItemStarMetalArmor("star_metal_chestplate", EntityEquipmentSlot.CHEST);
    public static final Item STAR_METAL_LEGGINGS = new ItemStarMetalArmor("star_metal_leggings", EntityEquipmentSlot.LEGS);
    public static final Item STAR_METAL_BOOTS = new ItemStarMetalArmor("star_metal_boots", EntityEquipmentSlot.FEET);

    public static final Item GNOMISH_METAL_INGOT = new ItemGnomishMetalIngot();

    public static final Item WOODEN_BOLT = new ItemWoodenBolt();
    public static final Item IRON_BOLT = new ItemIronBolt();
    public static final Item SILVER_BOLT = new ItemSilverBolt();
    public static final Item IGNEOUS_BOLT = new ItemIgneousBolt();
    public static final Item STAR_METAL_BOLT = new ItemStarMetalBolt();

    public static final Item DEBUG = new ItemDebug();

    // An array containing a list of items that AOTD adds
    public static Item[] ITEM_LIST = new Item[]
            {
                    JOURNAL,
                    ENCHANTED_SKELETON_BONE,
                    BLADE_OF_EXHUMATION,
                    CROSSBOW,
                    WRIST_CROSSBOW,
                    RESEARCH_SCROLL,
                    TELESCOPE,
                    SEXTANT,
                    ASTRAL_SILVER_INGOT,
                    ASTRAL_SILVER_SWORD,
                    WEREWOLF_BLOOD,
                    FLASK_OF_SOULS,
                    CLOAK_OF_AGILITY,
                    ELDRITCH_METAL_INGOT,
                    WOODEN_BOLT,
                    IRON_BOLT,
                    SILVER_BOLT,
                    IGNEOUS_BOLT,
                    STAR_METAL_BOLT,
                    DEBUG,
                    SLEEPING_POTION,
                    SUNSTONE_FRAGMENT,
                    IGNEOUS_GEM,
                    IGNEOUS_SWORD,
                    IGNEOUS_HELMET,
                    IGNEOUS_CHESTPLATE,
                    IGNEOUS_LEGGINGS,
                    IGNEOUS_BOOTS,
                    STAR_METAL_FRAGMENT,
                    STAR_METAL_INGOT,
                    STAR_METAL_PLATE,
                    STAR_METAL_KHOPESH,
                    STAR_METAL_STAFF,
                    STAR_METAL_HELMET,
                    STAR_METAL_CHESTPLATE,
                    STAR_METAL_LEGGINGS,
                    STAR_METAL_BOOTS,
                    GNOMISH_METAL_INGOT
            };
}
