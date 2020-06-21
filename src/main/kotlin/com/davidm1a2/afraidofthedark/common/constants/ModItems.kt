package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.item.*
import com.davidm1a2.afraidofthedark.common.item.crossbow.ItemCrossbow
import com.davidm1a2.afraidofthedark.common.item.crossbow.ItemWristCrossbow
import com.davidm1a2.afraidofthedark.common.item.crossbow.bolts.*
import com.davidm1a2.afraidofthedark.common.item.telescope.ItemAdvancedTelescope
import com.davidm1a2.afraidofthedark.common.item.telescope.ItemLens
import com.davidm1a2.afraidofthedark.common.item.telescope.ItemMastercraftedTelescope
import com.davidm1a2.afraidofthedark.common.item.telescope.ItemTelescope
import net.minecraft.inventory.EntityEquipmentSlot

/**
 * A static class containing all of our item references for us
 */
object ModItems {
    val JOURNAL = ItemJournal()
    val ENCHANTED_SKELETON_BONE = ItemEnchantedSkeletonBone()
    val BLADE_OF_EXHUMATION = ItemBladeOfExhumation()
    val CROSSBOW = ItemCrossbow()
    val WRIST_CROSSBOW = ItemWristCrossbow()
    val RESEARCH_SCROLL = ItemResearchScroll()
    val TELESCOPE = ItemTelescope()
    val SEXTANT = ItemSextant()
    val ASTRAL_SILVER_INGOT = ItemAstralSilverIngot()
    val ASTRAL_SILVER_SWORD = ItemAstralSilverSword()
    val WEREWOLF_BLOOD = ItemWerewolfBlood()
    val FLASK_OF_SOULS = ItemFlaskOfSouls()
    val CLOAK_OF_AGILITY = ItemCloakOfAgility()
    val ELDRITCH_METAL_INGOT = ItemEldritchMetalIngot()
    val SLEEPING_POTION = ItemSleepingPotion()
    val WAND = ItemWand()
    val INSANITYS_HEIGHTS = ItemInsanitysHeights()
    val SUNSTONE_FRAGMENT = ItemSunstoneFragment()
    val IGNEOUS_GEM = ItemIgneousGem()
    val IGNEOUS_SWORD = ItemIgneousSword()
    val IGNEOUS_HELMET = ItemIgneousArmor("igneous_helmet", EntityEquipmentSlot.HEAD)
    val IGNEOUS_CHESTPLATE = ItemIgneousArmor("igneous_chestplate", EntityEquipmentSlot.CHEST)
    val IGNEOUS_LEGGINGS = ItemIgneousArmor("igneous_leggings", EntityEquipmentSlot.LEGS)
    val IGNEOUS_BOOTS = ItemIgneousArmor("igneous_boots", EntityEquipmentSlot.FEET)
    val STAR_METAL_FRAGMENT = ItemStarMetalFragment()
    val STAR_METAL_INGOT = ItemStarMetalIngot()
    val STAR_METAL_PLATE = ItemStarMetalPlate()
    val STAR_METAL_KHOPESH = ItemStarMetalKhopesh()
    val STAR_METAL_STAFF = ItemStarMetalStaff()
    val STAR_METAL_HELMET = ItemStarMetalArmor("star_metal_helmet", EntityEquipmentSlot.HEAD)
    val STAR_METAL_CHESTPLATE = ItemStarMetalArmor("star_metal_chestplate", EntityEquipmentSlot.CHEST)
    val STAR_METAL_LEGGINGS = ItemStarMetalArmor("star_metal_leggings", EntityEquipmentSlot.LEGS)
    val STAR_METAL_BOOTS = ItemStarMetalArmor("star_metal_boots", EntityEquipmentSlot.FEET)
    val GNOMISH_METAL_INGOT = ItemGnomishMetalIngot()
    val WOODEN_BOLT = ItemWoodenBolt()
    val IRON_BOLT = ItemIronBolt()
    val SILVER_BOLT = ItemSilverBolt()
    val IGNEOUS_BOLT = ItemIgneousBolt()
    val STAR_METAL_BOLT = ItemStarMetalBolt()
    val DEBUG = ItemDebug()
    val SCHEMATIC_CREATOR = ItemSchematicCreator()
    val DESERT_FRUIT = ItemDesertFruit()
    val BONE_DUST = ItemBoneDust()
    val MAGIC_ESSENCE = ItemMagicEssence()
    val MYSTIC_OPAL = ItemMysticOpal()
    val POWER_CORE = ItemPowerCore()
    val SMOKEY_QUARTZ = ItemSmokeyQuartz()
    val SAPPHIRE_HEART = ItemSapphireHeart()
    val MYSTIC_TOPAZ = ItemMysticTopaz()
    val CURSED_VEIL = ItemCursedVeil()
    val NIGHTMARE_STONE = ItemNightmareStone()
    val CURSED_HEART = ItemCursedHeart()
    val CULTIST_TOME = ItemCultistTome()
    val ADVANCED_TELESCOPE = ItemAdvancedTelescope()

    val MASTERCRAFTED_TELESCOPE = ItemMastercraftedTelescope()
    val LENS = ItemLens()

    // An array containing a list of items that AOTD adds
    var ITEM_LIST = arrayOf(
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
        SCHEMATIC_CREATOR,
        SLEEPING_POTION,
        WAND,
        INSANITYS_HEIGHTS,
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
        GNOMISH_METAL_INGOT,
        DESERT_FRUIT,
        BONE_DUST,
        MAGIC_ESSENCE,
        MYSTIC_OPAL,
        POWER_CORE,
        SMOKEY_QUARTZ,
        SAPPHIRE_HEART,
        MYSTIC_TOPAZ,
        CURSED_VEIL,
        NIGHTMARE_STONE,
        CURSED_HEART,
        CULTIST_TOME,
        ADVANCED_TELESCOPE,
        MASTERCRAFTED_TELESCOPE,
        LENS
    )
}