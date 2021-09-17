package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.registry.JsonCodecLoader
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.util.ResourceLocation

/**
 * A static class containing all of our research references for us
 */
object ModResearches {
    val AN_UNBREAKABLE_COVENANT = load("an_unbreakable_covenant")
    val OBSERVATORY = load("observatory")
    val EERIE_FOREST = load("eerie_forest")
    val WITCH_HUTS = load("witch_huts")
    val WRIST_CROSSBOW = load("wrist_crossbow")
    val CLOAK_OF_AGILITY = load("cloak_of_agility")
    val VOID_OBELISK = load("void_obelisk")
    val DESERT_OASIS = load("desert_oasis")
    val ENCHANTED_SKELETON = load("enchanted_skeleton")
    val BLADE_OF_EXHUMATION = load("blade_of_exhumation")
    val WEREWOLVES = load("werewolves")
    val ASTRONOMY_1 = load("astronomy_1")
    val ASTRAL_SILVER = load("astral_silver")
    val OPTICS = load("optics")
    val ELVOVRAS = load("elvovras")
    val WEREWOLF_BLOOD = load("werewolf_blood")
    val FLASK_OF_SOULS = load("flask_of_souls")
    val VOID_CHEST = load("void_chest")
    val ELDRITCH_DECORATION = load("eldritch_decoration")
    val DARK_FOREST = load("dark_forest")
    val SLEEPING_POTION = load("sleeping_potion")
    val NIGHTMARE = load("nightmare")
    val INSANITY = load("insanity")
    val VITAE_LANTERN = load("vitae_lantern")
    val ASTRONOMY_2 = load("astronomy_2")
    val IGNEOUS = load("igneous")
    val STAR_METAL = load("star_metal")
    val FORBIDDEN_CITY = load("forbidden_city")
    val ENARIA = load("enaria")
    val ENARIAS_SECRET = load("enarias_secret")

    // An array containing a list of researches that AOTD adds
    val RESEARCH_LIST = arrayOf(
        AN_UNBREAKABLE_COVENANT,
        OBSERVATORY,
        EERIE_FOREST,
        WITCH_HUTS,
        WRIST_CROSSBOW,
        CLOAK_OF_AGILITY,
        VOID_OBELISK,
        VOID_CHEST,
        DESERT_OASIS,
        ENCHANTED_SKELETON,
        BLADE_OF_EXHUMATION,
        WEREWOLVES,
        ASTRONOMY_1,
        ASTRAL_SILVER,
        OPTICS,
        ELVOVRAS,
        WEREWOLF_BLOOD,
        FLASK_OF_SOULS,
        ELDRITCH_DECORATION,
        DARK_FOREST,
        SLEEPING_POTION,
        NIGHTMARE,
        INSANITY,
        VITAE_LANTERN,
        ASTRONOMY_2,
        IGNEOUS,
        STAR_METAL,
        FORBIDDEN_CITY,
        ENARIA,
        ENARIAS_SECRET
    )

    private fun load(name: String): Research {
        return JsonCodecLoader.load(ResourceLocation(Constants.MOD_ID, "researches/$name.json"), Research.CODEC)
    }
}