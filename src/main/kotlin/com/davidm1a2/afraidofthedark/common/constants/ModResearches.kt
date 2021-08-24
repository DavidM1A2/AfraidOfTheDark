package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.registry.JsonCodecLoader
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.util.ResourceLocation

/**
 * A static class containing all of our research references for us
 */
object ModResearches {
    val AN_UNBREAKABLE_COVENANT = load("an_unbreakable_covenant")
    val ENCHANTED_SKELETON = load("enchanted_skeleton")
    val BLADE_OF_EXHUMATION = load("blade_of_exhumation")
    val CROSSBOW = load("crossbow")
    val WRIST_CROSSBOW = load("wrist_crossbow")
    val WEREWOLF_EXAMINATION = load("werewolf_examination")
    val ASTRONOMY_1 = load("astronomy_1")
    val ASTRAL_SILVER = load("astral_silver")
    val OPTICS = load("optics")
    val ELVOVRAS = load("elvovras")
    val SLAYING_OF_THE_WOLVES = load("slaying_of_the_wolves")
    val PHYLACTERY_OF_SOULS = load("phylactery_of_souls")
    val CLOAK_OF_AGILITY = load("cloak_of_agility")
    val VOID_CHEST = load("void_chest")
    val ELDRITCH_DECORATION = load("eldritch_decoration")
    val DARK_FOREST = load("dark_forest")
    val SLEEPING_POTION = load("sleeping_potion")
    val NIGHTMARE = load("nightmare")
    val INSANITY = load("insanity")
    val VITAE_1 = load("vitae_1")
    val ASTRONOMY_2 = load("astronomy_2")
    val IGNEOUS = load("igneous")
    val STAR_METAL = load("star_metal")
    val GNOMISH_CITY = load("gnomish_city")
    val ENARIA = load("enaria")
    val ENARIAS_SECRET = load("enarias_secret")

    // An array containing a list of researches that AOTD adds
    val RESEARCH_LIST = arrayOf(
        AN_UNBREAKABLE_COVENANT,
        ENCHANTED_SKELETON,
        BLADE_OF_EXHUMATION,
        CROSSBOW,
        WRIST_CROSSBOW,
        WEREWOLF_EXAMINATION,
        ASTRONOMY_1,
        ASTRAL_SILVER,
        OPTICS,
        ELVOVRAS,
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

    private fun load(name: String): Research {
        return JsonCodecLoader.load(ResourceLocation(Constants.MOD_ID, "researches/$name.json"), Research.CODEC)
    }
}