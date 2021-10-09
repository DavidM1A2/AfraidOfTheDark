package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.item.ArcaneJournalItem
import com.davidm1a2.afraidofthedark.common.item.AstralSilverIngotItem
import com.davidm1a2.afraidofthedark.common.item.AstralSilverSwordItem
import com.davidm1a2.afraidofthedark.common.item.BoneDustItem
import com.davidm1a2.afraidofthedark.common.item.BoneSwordItem
import com.davidm1a2.afraidofthedark.common.item.CloakOfAgilityItem
import com.davidm1a2.afraidofthedark.common.item.CultistTomeItem
import com.davidm1a2.afraidofthedark.common.item.CursedHeartItem
import com.davidm1a2.afraidofthedark.common.item.CursedVeilItem
import com.davidm1a2.afraidofthedark.common.item.DebugItem
import com.davidm1a2.afraidofthedark.common.item.DesertFruitItem
import com.davidm1a2.afraidofthedark.common.item.EldritchMetalIngotItem
import com.davidm1a2.afraidofthedark.common.item.EnchantedSkeletonBoneItem
import com.davidm1a2.afraidofthedark.common.item.FlaskOfSoulsItem
import com.davidm1a2.afraidofthedark.common.item.GnomishMetalIngotItem
import com.davidm1a2.afraidofthedark.common.item.IgneousArmorItem
import com.davidm1a2.afraidofthedark.common.item.IgneousGemItem
import com.davidm1a2.afraidofthedark.common.item.IgneousSwordItem
import com.davidm1a2.afraidofthedark.common.item.InsanitysHeightsItem
import com.davidm1a2.afraidofthedark.common.item.MagicEssenceItem
import com.davidm1a2.afraidofthedark.common.item.MysticOpalItem
import com.davidm1a2.afraidofthedark.common.item.MysticTopazItem
import com.davidm1a2.afraidofthedark.common.item.NightmareStoneItem
import com.davidm1a2.afraidofthedark.common.item.PowerCoreItem
import com.davidm1a2.afraidofthedark.common.item.ResearchScrollItem
import com.davidm1a2.afraidofthedark.common.item.SapphireHeartItem
import com.davidm1a2.afraidofthedark.common.item.SchematicCreatorItem
import com.davidm1a2.afraidofthedark.common.item.SextantItem
import com.davidm1a2.afraidofthedark.common.item.SleepingPotionItem
import com.davidm1a2.afraidofthedark.common.item.SmokeyQuartzItem
import com.davidm1a2.afraidofthedark.common.item.StarMetalArmorItem
import com.davidm1a2.afraidofthedark.common.item.StarMetalFragmentItem
import com.davidm1a2.afraidofthedark.common.item.StarMetalIngotItem
import com.davidm1a2.afraidofthedark.common.item.StarMetalKhopeshItem
import com.davidm1a2.afraidofthedark.common.item.StarMetalPlateItem
import com.davidm1a2.afraidofthedark.common.item.StarMetalStaffItem
import com.davidm1a2.afraidofthedark.common.item.SunstoneFragmentItem
import com.davidm1a2.afraidofthedark.common.item.WandItem
import com.davidm1a2.afraidofthedark.common.item.WerewolfBloodItem
import com.davidm1a2.afraidofthedark.common.item.crossbow.WristCrossbowItem
import com.davidm1a2.afraidofthedark.common.item.crossbow.bolts.IgneousBoltItem
import com.davidm1a2.afraidofthedark.common.item.crossbow.bolts.IronBoltItem
import com.davidm1a2.afraidofthedark.common.item.crossbow.bolts.SilverBoltItem
import com.davidm1a2.afraidofthedark.common.item.crossbow.bolts.StarMetalBoltItem
import com.davidm1a2.afraidofthedark.common.item.crossbow.bolts.WoodenBoltItem
import com.davidm1a2.afraidofthedark.common.item.eggs.EnchantedFrogSpawnEggItem
import com.davidm1a2.afraidofthedark.common.item.eggs.EnchantedSkeletonSpawnEggItem
import com.davidm1a2.afraidofthedark.common.item.eggs.SplinterDroneSpawnEggItem
import com.davidm1a2.afraidofthedark.common.item.eggs.WerewolfSpawnEggItem
import com.davidm1a2.afraidofthedark.common.item.telescope.AdvancedTelescopeItem
import com.davidm1a2.afraidofthedark.common.item.telescope.LensItem
import com.davidm1a2.afraidofthedark.common.item.telescope.MastercraftedTelescopeItem
import com.davidm1a2.afraidofthedark.common.item.telescope.TelescopeItem
import net.minecraft.inventory.EquipmentSlotType

/**
 * A static class containing all of our item references for us
 */
object ModItems {
    val ARCANE_JOURNAL = ArcaneJournalItem()
    val ENCHANTED_SKELETON_BONE = EnchantedSkeletonBoneItem()
    val BONE_SWORD = BoneSwordItem()
    val WRIST_CROSSBOW = WristCrossbowItem()
    val RESEARCH_SCROLL = ResearchScrollItem()
    val TELESCOPE = TelescopeItem()
    val SEXTANT = SextantItem()
    val ASTRAL_SILVER_INGOT = AstralSilverIngotItem()
    val ASTRAL_SILVER_SWORD = AstralSilverSwordItem()
    val WEREWOLF_BLOOD = WerewolfBloodItem()
    val FLASK_OF_SOULS = FlaskOfSoulsItem()
    val CLOAK_OF_AGILITY = CloakOfAgilityItem()
    val ELDRITCH_METAL_INGOT = EldritchMetalIngotItem()
    val SLEEPING_POTION = SleepingPotionItem()
    val WAND = WandItem()
    val INSANITYS_HEIGHTS = InsanitysHeightsItem()
    val SUNSTONE_FRAGMENT = SunstoneFragmentItem()
    val IGNEOUS_GEM = IgneousGemItem()
    val IGNEOUS_SWORD = IgneousSwordItem()
    val IGNEOUS_HELMET = IgneousArmorItem("igneous_helmet", EquipmentSlotType.HEAD)
    val IGNEOUS_CHESTPLATE = IgneousArmorItem("igneous_chestplate", EquipmentSlotType.CHEST)
    val IGNEOUS_LEGGINGS = IgneousArmorItem("igneous_leggings", EquipmentSlotType.LEGS)
    val IGNEOUS_BOOTS = IgneousArmorItem("igneous_boots", EquipmentSlotType.FEET)
    val STAR_METAL_FRAGMENT = StarMetalFragmentItem()
    val STAR_METAL_INGOT = StarMetalIngotItem()
    val STAR_METAL_PLATE = StarMetalPlateItem()
    val STAR_METAL_KHOPESH = StarMetalKhopeshItem()
    val STAR_METAL_STAFF = StarMetalStaffItem()
    val STAR_METAL_HELMET = StarMetalArmorItem("star_metal_helmet", EquipmentSlotType.HEAD)
    val STAR_METAL_CHESTPLATE = StarMetalArmorItem("star_metal_chestplate", EquipmentSlotType.CHEST)
    val STAR_METAL_LEGGINGS = StarMetalArmorItem("star_metal_leggings", EquipmentSlotType.LEGS)
    val STAR_METAL_BOOTS = StarMetalArmorItem("star_metal_boots", EquipmentSlotType.FEET)
    val GNOMISH_METAL_INGOT = GnomishMetalIngotItem()
    val WOODEN_BOLT = WoodenBoltItem()
    val IRON_BOLT = IronBoltItem()
    val SILVER_BOLT = SilverBoltItem()
    val IGNEOUS_BOLT = IgneousBoltItem()
    val STAR_METAL_BOLT = StarMetalBoltItem()
    val DEBUG = DebugItem()
    val SCHEMATIC_CREATOR = SchematicCreatorItem()
    val DESERT_FRUIT = DesertFruitItem()
    val BONE_DUST = BoneDustItem()
    val MAGIC_ESSENCE = MagicEssenceItem()
    val MYSTIC_OPAL = MysticOpalItem()
    val POWER_CORE = PowerCoreItem()
    val SMOKEY_QUARTZ = SmokeyQuartzItem()
    val SAPPHIRE_HEART = SapphireHeartItem()
    val MYSTIC_TOPAZ = MysticTopazItem()
    val CURSED_VEIL = CursedVeilItem()
    val NIGHTMARE_STONE = NightmareStoneItem()
    val CURSED_HEART = CursedHeartItem()
    val CULTIST_TOME = CultistTomeItem()
    val ADVANCED_TELESCOPE = AdvancedTelescopeItem()
    val MASTERCRAFTED_TELESCOPE = MastercraftedTelescopeItem()
    val LENS = LensItem()
    val ENCHANTED_SKELETON_SPAWN_EGG = EnchantedSkeletonSpawnEggItem()
    val WEREWOLF_SPAWN_EGG = WerewolfSpawnEggItem()
    val SPLINTER_DRONE_SPAWN_EGG = SplinterDroneSpawnEggItem()
    val ENCHANTED_FROG_SPAWN_EGG = EnchantedFrogSpawnEggItem()

    // An array containing a list of items that AOTD adds
    var ITEM_LIST = arrayOf(
        ARCANE_JOURNAL,
        ENCHANTED_SKELETON_BONE,
        BONE_SWORD,
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
        LENS,
        ENCHANTED_SKELETON_SPAWN_EGG,
        WEREWOLF_SPAWN_EGG,
        SPLINTER_DRONE_SPAWN_EGG,
        ENCHANTED_FROG_SPAWN_EGG
    )
}