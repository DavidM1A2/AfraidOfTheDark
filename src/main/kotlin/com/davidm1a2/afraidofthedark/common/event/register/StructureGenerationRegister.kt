package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import com.google.common.collect.ImmutableMap
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.levelgen.FlatLevelSource
import net.minecraft.world.level.levelgen.StructureSettings
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.util.ObfuscationReflectionHelper
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.logging.log4j.LogManager

class StructureGenerationRegister {
    /**
     * Called by forge when biomes are loaded. Used to add structures to non-modded biomes
     *
     * @param event The biome being loaded
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    fun addStructuresToBiomes(event: BiomeLoadingEvent) {
        val structures = event.generation.structures
        val biome = ResourceKey.create(Registry.BIOME_REGISTRY, event.name!!)

        try {
            @Suppress("UNCHECKED_CAST")
            val flatStructureFeatures = STRUCTURE_FEATURES_FIELD.get(null) as MutableMap<StructureFeature<*>, ConfiguredStructureFeature<*, *>>
            ModStructures.STRUCTURES.forEach { flatStructureFeatures[it] = it.configuredFlat() }
        } catch (e: Exception) {
            LOG.error("Failed to register Afraid of the Dark structure placements into the flat generation settings", e)
        }

        for (structure in ModStructures.STRUCTURES) {
            structure.configured(biome, event.category)?.let {
                structures.add { it }
            }
        }
    }

    @SubscribeEvent
    fun setupDimensionalSpacing(event: WorldEvent.Load) {
        val world = event.world

        if (world !is ServerLevel) {
            // Structure spacing isn't registered client side
            return
        }

        // Don't spawn in superflat
        if (world.chunkSource.generator is FlatLevelSource && world.dimension() == Level.OVERWORLD) {
            return
        }

        /*
        if mods want to change the StructureSeparationSettings dynamically, they must construct the separation
        settings while a world is loading, because a chunk generator won't know which world it is bound to.
         */
        val structureConfigs = world.chunkSource.generator.settings.structureConfig()
        for (structure in ModStructures.STRUCTURES) {
            try {
                if (!structureConfigs.containsKey(structure)) {
                    world.chunkSource.generator.settings.structureConfig()
                    STRUCTURE_CONFIG_FIELD.set(
                        world.chunkSource.generator.settings, ImmutableMap.builder<StructureFeature<*>, StructureFeatureConfiguration>()
                            .putAll(structureConfigs)
                            .put(structure, StructureSettings.DEFAULTS[structure]!!)
                            .build()
                    )
                }
            } catch (e: Exception) {
                LOG.error("Failed to register Afraid of the Dark structure placements into the world", e)
            }
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()

        // TODO: Figure out new obfuscation mappings

        // StructureSettings::structureConfig
        private val STRUCTURE_CONFIG_FIELD = ObfuscationReflectionHelper.findField(StructureSettings::class.java, "field_236193_d_").apply {
            FieldUtils.removeFinalModifier(this)
            isAccessible = true
        }

        // FlatLevelGeneratorSettings::STRUCTURE_FEATURES
        private val STRUCTURE_FEATURES_FIELD = ObfuscationReflectionHelper.findField(FlatLevelGeneratorSettings::class.java, "field_202247_j").apply {
            isAccessible = true
        }
    }
}