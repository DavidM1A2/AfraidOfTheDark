package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.registry.JsonCodecLoader
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.util.ResourceLocation

/**
 * A static class containing all of our research references for us
 */
object ModResearches {
    val ADVANCED_MAGIC = load("advanced_magic")
    val ALCHEMY = load("alchemy")
    val ARCH_SORCERESS = load("arch_sorceress")
    val THE_JOURNEY_BEGINS = load("the_journey_begins")
    val ASTRAL_SILVER = load("astral_silver")
    val ASTRONOMY_1 = load("astronomy_1")
    val ASTRONOMY_2 = load("astronomy_2")
    val BLOOD_MAGIC = load("blood_magic")
    val BLOODBATH = load("bloodbath")
    val BONE_DUST = load("bone_dust")
    val BONE_SWORD = load("bone_sword")
    val CLOAK_OF_AGILITY = load("cloak_of_agility")
    val DARK_FOREST = load("dark_forest")
    val CRYPT = load("crypt")
    val SILVER_SLAYER = load("silver_slayer")
    val DESERT_OASIS = load("desert_oasis")
    val EERIE_FOREST = load("eerie_forest")
    val ELEMENTAL_MAGIC = load("elemental_magic")
    val ENARIA = load("enaria")
    val ENCHANTED_FROGS = load("enchanted_frogs")
    val ENCHANTED_SKELETON = load("enchanted_skeleton")
    val FLASK_OF_SOULS = load("flask_of_souls")
    val FORBIDDEN_CITY = load("forbidden_city")
    val IGNEOUS = load("igneous")
    val INFERNO = load("inferno")
    val INSANITY = load("insanity");
    val MAGIC_MASTERY = load("magic_mastery")
    val MANGROVE = load("mangrove")
    val NIGHTMARE_REALM = load("nightmare_realm")
    val OBSERVATORY = load("observatory")
    val OPTICS = load("optics")
    val POCKET_DIMENSION = load("pocket_dimension")
    val REANIMATION = load("reanimation")
    val SACRED_MANGROVE = load("sacred_mangrove")
    val SLEEPING_POTION = load("sleeping_potion")
    val SPELLMASON = load("spellmason")
    val STAR_METAL = load("star_metal")
    val THRONE_ROOM = load("throne_room")
    val VITAE = load("vitae")
    val VITAE_EXTRACTION = load("vitae_extraction")
    val VITAE_LANTERN = load("vitae_lantern")
    val VOID_CHEST = load("void_chest")
    val SNOWY_ANOMALY = load("snowy_anomaly")
    val VOID_PARTY = load("void_party")
    val WEREWOLF_BLOOD = load("werewolf_blood")
    val WEREWOLVES = load("werewolves")
    val WISDOM = load("wisdom")
    val WITCH_HUTS = load("witch_huts")
    val WRIST_CROSSBOW = load("wrist_crossbow")

    // An array containing a list of researches that AOTD adds
    val RESEARCH_LIST = arrayOf(
        ADVANCED_MAGIC,
        ALCHEMY,
        ARCH_SORCERESS,
        THE_JOURNEY_BEGINS,
        ASTRAL_SILVER,
        ASTRONOMY_1,
        ASTRONOMY_2,
        BLOOD_MAGIC,
        BLOODBATH,
        BONE_DUST,
        BONE_SWORD,
        CLOAK_OF_AGILITY,
        CRYPT,
        DARK_FOREST,
        SILVER_SLAYER,
        DESERT_OASIS,
        EERIE_FOREST,
        ELEMENTAL_MAGIC,
        ENARIA,
        ENCHANTED_FROGS,
        ENCHANTED_SKELETON,
        FLASK_OF_SOULS,
        FORBIDDEN_CITY,
        IGNEOUS,
        INFERNO,
        INSANITY,
        MAGIC_MASTERY,
        MANGROVE,
        NIGHTMARE_REALM,
        OBSERVATORY,
        OPTICS,
        POCKET_DIMENSION,
        REANIMATION,
        SACRED_MANGROVE,
        SLEEPING_POTION,
        SPELLMASON,
        STAR_METAL,
        THRONE_ROOM,
        VITAE,
        VITAE_EXTRACTION,
        VITAE_LANTERN,
        VOID_CHEST,
        SNOWY_ANOMALY,
        VOID_PARTY,
        WEREWOLF_BLOOD,
        WEREWOLVES,
        WISDOM,
        WITCH_HUTS,
        WRIST_CROSSBOW
    )

    private fun load(name: String): Research {
        return JsonCodecLoader.load(ResourceLocation(Constants.MOD_ID, "researches/$name.json"), Research.CODEC)
    }
}