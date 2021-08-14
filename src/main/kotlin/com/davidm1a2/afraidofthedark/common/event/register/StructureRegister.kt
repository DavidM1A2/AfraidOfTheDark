package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import com.google.common.collect.ImmutableMap
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.WorldGenRegistries
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.settings.DimensionStructuresSettings
import net.minecraft.world.gen.settings.StructureSeparationSettings
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.logging.log4j.LogManager

/**
 * Class used to register our structures into the game
 */
class StructureRegister {
    /**
     * Called by forge to register any of our structures
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    fun registerStructures(event: RegistryEvent.Register<Structure<*>>) {
        val registry = event.registry

        ModStructures.STRUCTURE_PIECES.forEach {
            Registry.register(Registry.STRUCTURE_PIECE, it.first.toString(), it.second)
        }

        registry.registerAll(*ModStructures.STRUCTURES)
    }

    @SubscribeEvent
    fun setupVanillaRegistries(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            for (structure in ModStructures.STRUCTURES) {
                /*
                Structures must be added to Structure.STRUCTURES_REGISTRY so the game is aware of the structure when it is used in other contexts.
                This also ensures the /locate command will list your structure.
                 */
                Structure.STRUCTURES_REGISTRY[structure.registryName.toString()] = structure

                /*
                The StructureSeparationSettings need to be added to the DimensionStructuresSettings's static map used when saving the world's chunks.
                 */
                val currentDefaultSeparationSettings = DEFAULTS_FIELD.get(null) as ImmutableMap<Structure<*>, StructureSeparationSettings>
                try {
                    if (!currentDefaultSeparationSettings.containsKey(structure)) {
                        DEFAULTS_FIELD.set(
                            null,
                            ImmutableMap.builder<Structure<*>, StructureSeparationSettings>()
                                .putAll(currentDefaultSeparationSettings)
                                .put(structure, DEFAULT_STRUCTURE_SEPARATION_SETTINGS)
                                .build()
                        )
                    }
                } catch (e: Exception) {
                    LOG.error("Failed to register Afraid of the Dark structure placement separation defaults", e)
                }

                /*
                The StructureSeparationSettings also needs to added to a world's ChunkGenerator to allow the chunk generator to know how many
                 chunks it should iterate over before spawning the structure. Without this, the structure simply won't generate.
                 */
                WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach {
                    val structureConfigs = it.value.structureSettings().structureConfig()

                    if (structureConfigs is ImmutableMap) {
                        try {
                            if (!structureConfigs.containsKey(structure)) {
                                STRUCTURE_CONFIG_FIELD.set(
                                    it.value.structureSettings(), ImmutableMap.builder<Structure<*>, StructureSeparationSettings>()
                                        .putAll(structureConfigs)
                                        .put(structure, DEFAULT_STRUCTURE_SEPARATION_SETTINGS)
                                        .build()
                                )
                            }
                        } catch (e: Exception) {
                            LOG.error("Failed to register Afraid of the Dark structure placements into the noise generator", e)
                        }
                    } else {
                        structureConfigs[structure] = DEFAULT_STRUCTURE_SEPARATION_SETTINGS
                    }
                }
            }
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()

        // DimensionStructuresSettings::DEFAULTS
        private val DEFAULTS_FIELD = ObfuscationReflectionHelper.findField(DimensionStructuresSettings::class.java, "field_236191_b_").apply {
            FieldUtils.removeFinalModifier(this)
            isAccessible = true
        }

        // DimensionStructuresSettings::structureConfig
        private val STRUCTURE_CONFIG_FIELD = ObfuscationReflectionHelper.findField(DimensionStructuresSettings::class.java, "field_236193_d_").apply {
            FieldUtils.removeFinalModifier(this)
            isAccessible = true
        }

        // 1 separation, 0 spacing, 0 salt. This ensures every chunk has the potential to contain the structure
        private val DEFAULT_STRUCTURE_SEPARATION_SETTINGS = StructureSeparationSettings(1, 0, 0)
    }
}