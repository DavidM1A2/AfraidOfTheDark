package com.davidm1a2.afraidofthedark.common.config

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.item.FlaskOfSoulsItem
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.LeechSpellPowerSource
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.ForgeConfigSpec

/**
 * AfraidOfTheDark common config
 *
 * @param builder The builder to append each config option into
 */
class CommonConfig(builder: ForgeConfigSpec.Builder) {
    // Multipliers for each dungeon type
    private val cryptMultiplier: ForgeConfigSpec.DoubleValue
    private val darkForestMultiplier: ForgeConfigSpec.DoubleValue
    private val forbiddenCityMultiplier: ForgeConfigSpec.DoubleValue
    private val voidChestMultiplier: ForgeConfigSpec.DoubleValue
    private val witchHutMultiplier: ForgeConfigSpec.DoubleValue
    private val desertOasisMultiplier: ForgeConfigSpec.DoubleValue
    private val observatoryMultiplier: ForgeConfigSpec.DoubleValue
    private val altarRuinsMultiplier: ForgeConfigSpec.DoubleValue
    private val magicCrystalMultiplier: ForgeConfigSpec.DoubleValue

    // The eerie biome frequency
    private val eerieBiomeFrequency: ForgeConfigSpec.IntValue

    // True if structures should be cached in memory, false otherwise
    private val cacheStructures: ForgeConfigSpec.BooleanValue

    // Timeout to clear structures in milliseconds if cache is set to false
    private val cacheTimeout: ForgeConfigSpec.LongValue

    private val flaskOfSoulsCommonEntities: ForgeConfigSpec.ConfigValue<List<String>>
    private val flaskOfSoulsRareEntities: ForgeConfigSpec.ConfigValue<List<String>>
    private val flaskOfSoulsEpicEntities: ForgeConfigSpec.ConfigValue<List<String>>

    private val leechPowerSourceEntities: ForgeConfigSpec.ConfigValue<List<String>>

    init {
        builder.push("dungeon_frequency")

        cryptMultiplier = builder
            .comment("Increases the number of Crypts in the world by the multiplier specified.")
            .translation("config.afraidofthedark:crypt_multiplier")
            .defineInRange("crypt_multiplier", 1.0, 0.0, 100.0)
        darkForestMultiplier = builder
            .comment("Increases the number of Dark Forests in the world by the multiplier specified.")
            .translation("config.afraidofthedark:dark_forest_multiplier")
            .defineInRange("dark_forest_multiplier", 1.0, 0.0, 100.0)
        forbiddenCityMultiplier = builder
            .comment("Increases the number of Forbidden Cities in the world by the multiplier specified.")
            .translation("config.afraidofthedark:forbidden_city_multiplier")
            .defineInRange("forbidden_city_multiplier", 1.0, 0.0, 100.0)
        voidChestMultiplier = builder
            .comment("Increases the number of Void Chests in the world by the multiplier specified.")
            .translation("config.afraidofthedark:void_chest_multiplier")
            .defineInRange("void_chest_multiplier", 1.0, 0.0, 100.0)
        witchHutMultiplier = builder
            .comment("Increases the number of Witch Huts in the world by the multiplier specified.")
            .translation("config.afraidofthedark:witch_hut_multiplier")
            .defineInRange("witch_hut_multiplier", 1.0, 0.0, 100.0)
        desertOasisMultiplier = builder
            .comment("Increases the number of Desert Oasis in the world by the multiplier specified.")
            .translation("config.afraidofthedark:desert_oasis_multiplier")
            .defineInRange("desert_oasis_multiplier", 1.0, 0.0, 100.0)
        observatoryMultiplier = builder
            .comment("Increases the number of Observatories in the world by the multiplier specified.")
            .translation("config.afraidofthedark:observatory_multiplier")
            .defineInRange("observatory_multiplier", 1.0, 0.0, 100.0)
        altarRuinsMultiplier = builder
            .comment("Increases the number of Altar Ruins in the world by the multiplier specified.")
            .translation("config.afraidofthedark:altar_ruins_multiplier")
            .defineInRange("altar_ruins_multiplier", 1.0, 0.0, 100.0)
        magicCrystalMultiplier = builder
            .comment("Increases the number of Magic Crystals in the world by the multiplier specified.")
            .translation("config.afraidofthedark:magic_crystals_multiplier")
            .defineInRange("magic_crystals_multiplier", 1.0, 0.0, 100.0)

        builder.pop()

        builder.push("world_generation")

        eerieBiomeFrequency = builder
            .comment("Increase this value to increase the number of Eerie Biomes. 10 is the default MC forest value.")
            .translation("config.afraidofthedark:eerie_biome_frequency")
            .defineInRange("eerie_biome_frequency", 10, 0, 1000)

        cacheStructures = builder
            .comment("True means structures will be loaded into computer memory when Minecraft is started up. This will accelerate world generation at the cost of RAM usage. False means structures will be loaded when needed, which will require less RAM but can incur lag spikes when finding structures.")
            .translation("config.afraidofthedark:cache_structures")
            .define("cache_structures", true)
        cacheTimeout = builder
            .comment("Required if 'Cache Structures' is set to false, otherwise ignored. If a structure isn't needed for 'Cache Timeout' milliseconds it will be forgotten and cleared from RAM.")
            .translation("config.afraidofthedark:cache_timeout")
            .defineInRange("cache_timeout", 60000L, 10000L, Long.MAX_VALUE)

        builder.pop()

        builder.push("flask_of_souls")

        flaskOfSoulsCommonEntities = builder
            .comment("A list of entities that take 32 kills to complete the flask with a 5 second cooldown.")
            .translation("config.afraidofthedark:basic_entities")
            .defineList("basic_entities", { FlaskOfSoulsItem.BASIC_ENTITIES }, { it is String && it.toResourceLocation() != null })
        flaskOfSoulsRareEntities = builder
            .comment("A list of entities that take 16 kills to complete the flask with a 10 second cooldown.")
            .translation("config.afraidofthedark:rare_entities")
            .defineList("rare_entities", { FlaskOfSoulsItem.RARE_ENTITIES }, { it is String && it.toResourceLocation() != null })
        flaskOfSoulsEpicEntities = builder
            .comment("A list of entities that take 8 kills to complete the flask with a 20 second cooldown.")
            .translation("config.afraidofthedark:epic_entities")
            .defineList("epic_entities", { FlaskOfSoulsItem.EPIC_ENTITIES }, { it is String && it.toResourceLocation() != null })

        builder.pop()

        builder.push("leech_power_source")

        leechPowerSourceEntities = builder
            .comment("A list of entities that can be damaged by the leech spell power source.")
            .translation("config.afraidofthedark:leech_power_source_entities")
            .defineList("leech_power_source_entities", { LeechSpellPowerSource.LEECH_ENTITIES }, { it is String && it.toResourceLocation() != null })

        builder.pop()
    }

    fun reload() {
        ModCommonConfiguration.cryptMultiplier = cryptMultiplier.get()
        ModCommonConfiguration.darkForestMultiplier = darkForestMultiplier.get()
        ModCommonConfiguration.forbiddenCityFrequency = forbiddenCityMultiplier.get()
        ModCommonConfiguration.voidChestMultiplier = voidChestMultiplier.get()
        ModCommonConfiguration.witchHutMultiplier = witchHutMultiplier.get()
        ModCommonConfiguration.desertOasisMultiplier = desertOasisMultiplier.get()
        ModCommonConfiguration.observatoryMultiplier = observatoryMultiplier.get()
        ModCommonConfiguration.altarRuinsMultiplier = altarRuinsMultiplier.get()
        ModCommonConfiguration.magicCrystalMultiplier = magicCrystalMultiplier.get()
        ModCommonConfiguration.eerieBiomeFrequency = eerieBiomeFrequency.get()
        ModCommonConfiguration.cacheStructures = cacheStructures.get()
        ModCommonConfiguration.cacheTimeout = cacheTimeout.get()
        ModCommonConfiguration.flaskOfSoulsBasicEntities = flaskOfSoulsCommonEntities.get().mapNotNull { it.toResourceLocation() }.toSet()
        ModCommonConfiguration.flaskOfSoulsRareEntities = flaskOfSoulsRareEntities.get().mapNotNull { it.toResourceLocation() }.toSet()
        ModCommonConfiguration.flaskOfSoulsEpicEntities = flaskOfSoulsEpicEntities.get().mapNotNull { it.toResourceLocation() }.toSet()
        ModCommonConfiguration.leechPowerSourceEntities = leechPowerSourceEntities.get().mapNotNull { it.toResourceLocation() }.toSet()
    }

    private fun String.toResourceLocation(): ResourceLocation? {
        return try {
            ResourceLocation(this)
        } catch (e: Exception) {
            return null
        }
    }
}