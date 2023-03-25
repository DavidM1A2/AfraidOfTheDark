package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.item.*
import com.davidm1a2.afraidofthedark.common.item.amorphousmetal.*
import com.davidm1a2.afraidofthedark.common.item.astralsilver.*
import com.davidm1a2.afraidofthedark.common.item.crossbow.WristCrossbowItem
import com.davidm1a2.afraidofthedark.common.item.crossbow.bolts.*
import com.davidm1a2.afraidofthedark.common.item.disc.*
import com.davidm1a2.afraidofthedark.common.item.eggs.*
import com.davidm1a2.afraidofthedark.common.item.eldritchmetal.*
import com.davidm1a2.afraidofthedark.common.item.gnomishmetal.GnomishMetalAxeItem
import com.davidm1a2.afraidofthedark.common.item.gnomishmetal.GnomishMetalHoeItem
import com.davidm1a2.afraidofthedark.common.item.gnomishmetal.GnomishMetalPickaxeItem
import com.davidm1a2.afraidofthedark.common.item.gnomishmetal.GnomishMetalShovelItem
import com.davidm1a2.afraidofthedark.common.item.igneous.*
import com.davidm1a2.afraidofthedark.common.item.starmetal.*
import com.davidm1a2.afraidofthedark.common.item.telescope.*
import com.davidm1a2.afraidofthedark.common.item.void.*
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
    val ASTRAL_SILVER_BOLT = AstralSilverBoltItem()
    val ASTRAL_SILVER_AXE = AstralSilverAxeItem()
    val ASTRAL_SILVER_HOE = AstralSilverHoeItem()
    val ASTRAL_SILVER_PICKAXE = AstralSilverPickaxeItem()
    val ASTRAL_SILVER_SHOVEL = AstralSilverShovelItem()
    val ASTRAL_SILVER_HELMET = AstralSilverArmorItem("astral_silver_helmet", EquipmentSlotType.HEAD)
    val ASTRAL_SILVER_CHESTPLATE = AstralSilverArmorItem("astral_silver_chestplate", EquipmentSlotType.CHEST)
    val ASTRAL_SILVER_LEGGINGS = AstralSilverArmorItem("astral_silver_leggings", EquipmentSlotType.LEGS)
    val ASTRAL_SILVER_BOOTS = AstralSilverArmorItem("astral_silver_boots", EquipmentSlotType.FEET)
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
    val IGNEOUS_BOLT = IgneousBoltItem()
    val IGNEOUS_AXE = IgneousAxeItem()
    val IGNEOUS_HOE = IgneousHoeItem()
    val IGNEOUS_PICKAXE = IgneousPickaxeItem()
    val IGNEOUS_SHOVEL = IgneousShovelItem()
    val IGNEOUS_SHIELD = IgneousShieldItem()
    val IGNEOUS_HELMET = IgneousArmorItem("igneous_helmet", EquipmentSlotType.HEAD)
    val IGNEOUS_CHESTPLATE = IgneousArmorItem("igneous_chestplate", EquipmentSlotType.CHEST)
    val IGNEOUS_LEGGINGS = IgneousArmorItem("igneous_leggings", EquipmentSlotType.LEGS)
    val IGNEOUS_BOOTS = IgneousArmorItem("igneous_boots", EquipmentSlotType.FEET)
    val STAR_METAL_FRAGMENT = StarMetalFragmentItem()
    val STAR_METAL_INGOT = StarMetalIngotItem()
    val STAR_METAL_PLATE = StarMetalPlateItem()
    val STAR_METAL_KHOPESH = StarMetalKhopeshItem()
    val STAR_METAL_BOLT = StarMetalBoltItem()
    val STAR_METAL_AXE = StarMetalAxeItem()
    val STAR_METAL_HOE = StarMetalHoeItem()
    val STAR_METAL_PICKAXE = StarMetalPickaxeItem()
    val STAR_METAL_SHOVEL = StarMetalShovelItem()
    val STAR_METAL_STAFF = StarMetalStaffItem()
    val STAR_METAL_HELMET = StarMetalArmorItem("star_metal_helmet", EquipmentSlotType.HEAD)
    val STAR_METAL_CHESTPLATE = StarMetalArmorItem("star_metal_chestplate", EquipmentSlotType.CHEST)
    val STAR_METAL_LEGGINGS = StarMetalArmorItem("star_metal_leggings", EquipmentSlotType.LEGS)
    val STAR_METAL_BOOTS = StarMetalArmorItem("star_metal_boots", EquipmentSlotType.FEET)
    val ELDRITCH_METAL_SWORD = EldritchMetalSwordItem()
    val ELDRITCH_METAL_BOLT = EldritchMetalBoltItem()
    val ELDRITCH_METAL_AXE = EldritchMetalAxeItem()
    val ELDRITCH_METAL_HOE = EldritchMetalHoeItem()
    val ELDRITCH_METAL_PICKAXE = EldritchMetalPickaxeItem()
    val ELDRITCH_METAL_SHOVEL = EldritchMetalShovelItem()
    val ELDRITCH_METAL_HELMET = EldritchMetalArmorItem("eldritch_metal_helmet", EquipmentSlotType.HEAD)
    val ELDRITCH_METAL_CHESTPLATE = EldritchMetalArmorItem("eldritch_metal_chestplate", EquipmentSlotType.CHEST)
    val ELDRITCH_METAL_LEGGINGS = EldritchMetalArmorItem("eldritch_metal_leggings", EquipmentSlotType.LEGS)
    val ELDRITCH_METAL_BOOTS = EldritchMetalArmorItem("eldritch_metal_boots", EquipmentSlotType.FEET)
    val AMORPHOUS_METAL_SWORD = AmorphousMetalSwordItem()
    val AMORPHOUS_METAL_BOLT = AmorphousMetalBoltItem()
    val AMORPHOUS_METAL_AXE = AmorphousMetalAxeItem()
    val AMORPHOUS_METAL_HOE = AmorphousMetalHoeItem()
    val AMORPHOUS_METAL_PICKAXE = AmorphousMetalPickaxeItem()
    val AMORPHOUS_METAL_SHOVEL = AmorphousMetalShovelItem()
    val AMORPHOUS_METAL_HELMET = AmorphousMetalArmorItem("amorphous_metal_helmet", EquipmentSlotType.HEAD)
    val AMORPHOUS_METAL_CHESTPLATE = AmorphousMetalArmorItem("amorphous_metal_chestplate", EquipmentSlotType.CHEST)
    val AMORPHOUS_METAL_LEGGINGS = AmorphousMetalArmorItem("amorphous_metal_leggings", EquipmentSlotType.LEGS)
    val AMORPHOUS_METAL_BOOTS = AmorphousMetalArmorItem("amorphous_metal_boots", EquipmentSlotType.FEET)
    val VOID_SABER = VoidSaberItem()
    val VOID_BOLT = VoidBoltItem()
    val VOID_AXE = VoidAxeItem()
    val VOID_HOE = VoidHoeItem()
    val VOID_PICKAXE = VoidPickaxeItem()
    val VOID_SHOVEL = VoidShovelItem()
    val VOID_HELMET = VoidArmorItem("void_helmet", EquipmentSlotType.HEAD)
    val VOID_CHESTPLATE = VoidArmorItem("void_chestplate", EquipmentSlotType.CHEST)
    val VOID_LEGGINGS = VoidArmorItem("void_leggings", EquipmentSlotType.LEGS)
    val VOID_BOOTS = VoidArmorItem("void_boots", EquipmentSlotType.FEET)
    val GNOMISH_METAL_INGOT = GnomishMetalIngotItem()
    val GNOMISH_METAL_AXE = GnomishMetalAxeItem()
    val GNOMISH_METAL_HOE = GnomishMetalHoeItem()
    val GNOMISH_METAL_PICKAXE = GnomishMetalPickaxeItem()
    val GNOMISH_METAL_SHOVEL = GnomishMetalShovelItem()
    val WOODEN_BOLT = WoodenBoltItem()
    val IRON_BOLT = IronBoltItem()
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
    val GLASS_LENS = GlassLensItem()
    val DIAMOND_LENS = DiamondLensItem()
    val TOPAZ_LENS = TopazLensItem()
    val COMPOUND_LENS = CompoundLensItem()
    val VITAE_LANTERN = VitaeLanternItem()
    val SPELL_SCROLL = SpellScrollItem()
    val STABILIZING_CATALYST = StabilizingCatalystItem()
    val ENCHANTED_SKELETON_SPAWN_EGG = EnchantedSkeletonSpawnEggItem()
    val WEREWOLF_SPAWN_EGG = WerewolfSpawnEggItem()
    val SPLINTER_DRONE_SPAWN_EGG = SplinterDroneSpawnEggItem()
    val ENCHANTED_FROG_SPAWN_EGG = EnchantedFrogSpawnEggItem()
    val FROST_PHOENIX_SPAWN_EGG = FrostPhoenixSpawnEggItem()
    val ANGER_MUSIC_DISC = AngerMusicDiscItem()
    val FALL_MUSIC_DISC = FallMusicDiscItem()
    val INFERNO_MUSIC_DISC = InfernoMusicDiscItem()
    val INSIGHT_MUSIC_DISC = InsightMusicDiscItem()
    val NIGHTMARE_MUSIC_DISC = NightmareMusicDiscItem()
    val OASIS_MUSIC_DISC = OasisMusicDiscItem()
    val PRIDE_MUSIC_DISC = PrideMusicDiscItem()
    val WINDY_MUSIC_DISC = WindyMusicDiscItem()

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
        ASTRAL_SILVER_BOLT,
        ASTRAL_SILVER_AXE,
        ASTRAL_SILVER_HOE,
        ASTRAL_SILVER_PICKAXE,
        ASTRAL_SILVER_SHOVEL,
        ASTRAL_SILVER_HELMET,
        ASTRAL_SILVER_CHESTPLATE,
        ASTRAL_SILVER_LEGGINGS,
        ASTRAL_SILVER_BOOTS,
        WEREWOLF_BLOOD,
        FLASK_OF_SOULS,
        CLOAK_OF_AGILITY,
        ELDRITCH_METAL_INGOT,
        WOODEN_BOLT,
        IRON_BOLT,
        DEBUG,
        SCHEMATIC_CREATOR,
        SLEEPING_POTION,
        WAND,
        INSANITYS_HEIGHTS,
        SUNSTONE_FRAGMENT,
        IGNEOUS_GEM,
        IGNEOUS_SWORD,
        IGNEOUS_BOLT,
        IGNEOUS_AXE,
        IGNEOUS_HOE,
        IGNEOUS_PICKAXE,
        IGNEOUS_SHOVEL,
        IGNEOUS_SHIELD,
        IGNEOUS_HELMET,
        IGNEOUS_CHESTPLATE,
        IGNEOUS_LEGGINGS,
        IGNEOUS_BOOTS,
        STAR_METAL_FRAGMENT,
        STAR_METAL_INGOT,
        STAR_METAL_PLATE,
        STAR_METAL_KHOPESH,
        STAR_METAL_BOLT,
        STAR_METAL_AXE,
        STAR_METAL_HOE,
        STAR_METAL_PICKAXE,
        STAR_METAL_SHOVEL,
        STAR_METAL_STAFF,
        STAR_METAL_HELMET,
        STAR_METAL_CHESTPLATE,
        STAR_METAL_LEGGINGS,
        STAR_METAL_BOOTS,
        ELDRITCH_METAL_SWORD,
        ELDRITCH_METAL_BOLT,
        ELDRITCH_METAL_AXE,
        ELDRITCH_METAL_HOE,
        ELDRITCH_METAL_PICKAXE,
        ELDRITCH_METAL_SHOVEL,
        ELDRITCH_METAL_HELMET,
        ELDRITCH_METAL_CHESTPLATE,
        ELDRITCH_METAL_LEGGINGS,
        ELDRITCH_METAL_BOOTS,
        AMORPHOUS_METAL_SWORD,
        AMORPHOUS_METAL_BOLT,
        AMORPHOUS_METAL_AXE,
        AMORPHOUS_METAL_HOE,
        AMORPHOUS_METAL_PICKAXE,
        AMORPHOUS_METAL_SHOVEL,
        AMORPHOUS_METAL_HELMET,
        AMORPHOUS_METAL_CHESTPLATE,
        AMORPHOUS_METAL_LEGGINGS,
        AMORPHOUS_METAL_BOOTS,
        VOID_SABER,
        VOID_BOLT,
        VOID_AXE,
        VOID_HOE,
        VOID_PICKAXE,
        VOID_SHOVEL,
        VOID_HELMET,
        VOID_CHESTPLATE,
        VOID_LEGGINGS,
        VOID_BOOTS,
        GNOMISH_METAL_INGOT,
        GNOMISH_METAL_AXE,
        GNOMISH_METAL_HOE,
        GNOMISH_METAL_PICKAXE,
        GNOMISH_METAL_SHOVEL,
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
        GLASS_LENS,
        DIAMOND_LENS,
        TOPAZ_LENS,
        COMPOUND_LENS,
        VITAE_LANTERN,
        SPELL_SCROLL,
        STABILIZING_CATALYST,
        ENCHANTED_SKELETON_SPAWN_EGG,
        WEREWOLF_SPAWN_EGG,
        SPLINTER_DRONE_SPAWN_EGG,
        ENCHANTED_FROG_SPAWN_EGG,
        FROST_PHOENIX_SPAWN_EGG,
        ANGER_MUSIC_DISC,
        FALL_MUSIC_DISC,
        INFERNO_MUSIC_DISC,
        INSIGHT_MUSIC_DISC,
        NIGHTMARE_MUSIC_DISC,
        OASIS_MUSIC_DISC,
        PRIDE_MUSIC_DISC,
        WINDY_MUSIC_DISC
    )
}