package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.registry.research.AOTDResearch

/**
 * A static class containing all of our research references for us
 */
object ModResearches
{
    val AN_UNBREAKABLE_COVENANT = AOTDResearch("an_unbreakable_covenant", null)
    val ENCHANTED_SKELETON = AOTDResearch("enchanted_skeleton", AN_UNBREAKABLE_COVENANT)
    val BLADE_OF_EXHUMATION = AOTDResearch("blade_of_exhumation", ENCHANTED_SKELETON)
    val CROSSBOW = AOTDResearch("crossbow", AN_UNBREAKABLE_COVENANT)
    val WRIST_CROSSBOW = AOTDResearch("wrist_crossbow", CROSSBOW)
    val WEREWOLF_EXAMINATION = AOTDResearch("werewolf_examination", AN_UNBREAKABLE_COVENANT)
    val ASTRONOMY_1 = AOTDResearch("astronomy_1", WEREWOLF_EXAMINATION)
    val ASTRAL_SILVER = AOTDResearch("astral_silver", ASTRONOMY_1)
    val SLAYING_OF_THE_WOLVES = AOTDResearch("slaying_of_the_wolves", ASTRAL_SILVER)
    val PHYLACTERY_OF_SOULS = AOTDResearch("phylactery_of_souls", SLAYING_OF_THE_WOLVES)
    val CLOAK_OF_AGILITY = AOTDResearch("cloak_of_agility", WEREWOLF_EXAMINATION)
    val VOID_CHEST = AOTDResearch("void_chest", ASTRAL_SILVER)
    val ELDRITCH_DECORATION = AOTDResearch("eldritch_decoration", VOID_CHEST)
    val DARK_FOREST = AOTDResearch("dark_forest", ASTRAL_SILVER)
    val SLEEPING_POTION = AOTDResearch("sleeping_potion", DARK_FOREST)
    val NIGHTMARE = AOTDResearch("nightmare", DARK_FOREST)
    val INSANITY = AOTDResearch("insanity", NIGHTMARE)
    val VITAE_1 = AOTDResearch("vitae_1", NIGHTMARE)
    val ASTRONOMY_2 = AOTDResearch("astronomy_2", NIGHTMARE)
    val IGNEOUS = AOTDResearch("igneous", ASTRONOMY_2)
    val STAR_METAL = AOTDResearch("star_metal", ASTRONOMY_2)
    val GNOMISH_CITY = AOTDResearch("gnomish_city", ASTRONOMY_2)
    val ENARIA = AOTDResearch("enaria", GNOMISH_CITY)
    val ENARIAS_SECRET = AOTDResearch("enarias_secret", ENARIA)

    // An array containing a list of researches that AOTD adds
    var RESEARCH_LIST = arrayOf(
        AN_UNBREAKABLE_COVENANT,
        ENCHANTED_SKELETON,
        BLADE_OF_EXHUMATION,
        CROSSBOW,
        WRIST_CROSSBOW,
        WEREWOLF_EXAMINATION,
        ASTRONOMY_1,
        ASTRAL_SILVER,
        SLAYING_OF_THE_WOLVES,
        PHYLACTERY_OF_SOULS,
        CLOAK_OF_AGILITY,
        VOID_CHEST,
        ELDRITCH_DECORATION,
        DARK_FOREST,
        SLEEPING_POTION,
        NIGHTMARE,
        INSANITY,
        VITAE_1,
        ASTRONOMY_2,
        IGNEOUS,
        STAR_METAL,
        GNOMISH_CITY,
        ENARIA,
        ENARIAS_SECRET
    )
}