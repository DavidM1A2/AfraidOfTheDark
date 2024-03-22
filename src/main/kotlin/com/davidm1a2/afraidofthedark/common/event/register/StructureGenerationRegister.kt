package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import com.google.common.collect.ImmutableMap
import net.minecraft.util.RegistryKey
import net.minecraft.util.registry.Registry
import net.minecraft.world.level.Level
import net.minecraft.world.gen.FlatChunkGenerator
import net.minecraft.world.gen.FlatGenerationSettings
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.settings.DimensionStructuresSettings
import net.minecraft.world.gen.settings.StructureSeparationSettings
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
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
        val biome = RegistryKey.create(Registry.BIOME_REGISTRY, event.name!!)

        try {
            @Suppress("UNCHECKED_CAST")
            val flatStructureFeatures = STRUCTURE_FEATURES_FIELD.get(null) as MutableMap<Structure<*>, StructureFeature<*, *>>
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

        if (world !is ServerWorld) {
            // Structure spacing isn't registered client side
            return
        }

        // Don't spawn in superflat
        if (world.chunkSource.generator is FlatChunkGenerator && world.dimension() == World.OVERWORLD) {
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
                    STRUCTURE_CONFIG_FIELD.set(
                        world.chunkSource.generator.settings, ImmutableMap.builder<Structure<*>, StructureSeparationSettings>()
                            .putAll(structureConfigs)
                            .put(structure, DimensionStructuresSettings.DEFAULTS[structure]!!)
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

        // DimensionStructuresSettings::structureConfig
        private val STRUCTURE_CONFIG_FIELD = ObfuscationReflectionHelper.findField(DimensionStructuresSettings::class.java, "field_236193_d_").apply {
            FieldUtils.removeFinalModifier(this)
            isAccessible = true
        }

        // FlatGenerationSettings::STRUCTURE_FEATURES
        private val STRUCTURE_FEATURES_FIELD = ObfuscationReflectionHelper.findField(FlatGenerationSettings::class.java, "field_202247_j").apply {
            isAccessible = true
        }
    }
}