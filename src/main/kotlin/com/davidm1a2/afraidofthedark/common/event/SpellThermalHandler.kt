package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellThermalData
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.block.Blocks
import net.minecraft.world.entity.player.Player
import net.minecraft.entity.player.ServerPlayer
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import kotlin.math.max

class SpellThermalHandler {
    private val requiredResearch: Research? by lazy {
        ModSpellPowerSources.THERMAL.prerequisiteResearch
    }

    @SubscribeEvent
    fun onPlayerTick(event: TickEvent.PlayerTickEvent) {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            val entityPlayer = event.player
            // Dead players don't have capabilities
            if (entityPlayer.tickCount % VITAE_TICK_INTERVAL == 0 && entityPlayer.isAlive) {
                if (requiredResearch == null || entityPlayer.getResearch().isResearched(requiredResearch!!)) {
                    tickThermalVitae(entityPlayer)
                }
            }
        }
    }

    private fun tickThermalVitae(entityPlayer: Player) {
        val thermalData = entityPlayer.getSpellThermalData()

        val oldThermalVitae = thermalData.vitae
        tickBiome(entityPlayer)
        tickDepth(entityPlayer)
        tickNearbyBlocks(entityPlayer)

        // Small optimization, only sync on an actual update
        if (oldThermalVitae != thermalData.vitae) {
            thermalData.sync(entityPlayer as ServerPlayer)
        }
    }

    private fun tickBiome(entityPlayer: Player) {
        // Wiki says Temp is 0 [cold] to 4 [hot], but it's actually from 0-2?
        val biomeTemperature = entityPlayer.level.getBiome(entityPlayer.blockPosition()).getTemperature(entityPlayer.blockPosition())
        val vitaeChange = (biomeTemperature - 1.0).coerceIn(0.0, 1.0) * BIOME_VITAE_MODIFIER_PER_INTERVAL

        val thermalData = entityPlayer.getSpellThermalData()
        thermalData.vitae = (thermalData.vitae + vitaeChange).coerceIn(0.0, thermalData.getMaxVitae(entityPlayer.level))
    }

    private fun tickDepth(entityPlayer: Player) {
        // 0-64 gain vitae. 64-256 lose vitae. Treat 128-256 the same
        val depth = entityPlayer.blockPosition().y.coerceIn(0, 128)
        val vitaeChange = if (depth <= 64) {
            (64.0 - depth) / 64.0 * DEPTH_VITAE_MODIFIER_PER_INTERVAL
        } else {
            -(depth - 64.0) / 64.0 * DEPTH_VITAE_MODIFIER_PER_INTERVAL
        }

        val thermalData = entityPlayer.getSpellThermalData()
        thermalData.vitae = (thermalData.vitae + vitaeChange).coerceIn(0.0, thermalData.getMaxVitae(entityPlayer.level))
    }

    private fun tickNearbyBlocks(entityPlayer: Player) {
        val world = entityPlayer.level

        var currentHeatEstimate = Double.NEGATIVE_INFINITY
        // Sample nearby blocks and get their heat values
        for (i in 0 until HEAT_SAMPLE_COUNT) {
            val samplePos = entityPlayer.blockPosition().offset(
                entityPlayer.random.nextInt(HEAT_SAMPLE_DISTANCE * 2) - HEAT_SAMPLE_DISTANCE,
                entityPlayer.random.nextInt(HEAT_SAMPLE_DISTANCE * 2) - HEAT_SAMPLE_DISTANCE,
                entityPlayer.random.nextInt(HEAT_SAMPLE_DISTANCE * 2) - HEAT_SAMPLE_DISTANCE
            )

            val heatEmission = HEAT_BLOCK_EMISSION[world.getBlockState(samplePos).block] ?: continue
            // Hot things beat cold things. Take the warmest block around to set the heat level
            currentHeatEstimate = max(currentHeatEstimate, heatEmission)
        }

        // If no blocks around us were hot or cold, move towards equilibrium (0.0)
        if (currentHeatEstimate == Double.NEGATIVE_INFINITY) {
            currentHeatEstimate = 0.0
        }

        // Heat will slowly change over time. Take 70% of the old value and 30% of the new value as the new "heat" value
        val thermalData = entityPlayer.getSpellThermalData()
        thermalData.heat = thermalData.heat * 0.7 + currentHeatEstimate * 0.3

        val vitaeChange = thermalData.heat * HEAT_VITAE_MODIFIER_PER_INTERVAL
        thermalData.vitae = (thermalData.vitae + vitaeChange).coerceIn(0.0, thermalData.getMaxVitae(entityPlayer.level))
    }

    companion object {
        private const val VITAE_TICK_INTERVAL = 20

        // Max of +/- 5 vitae per interval because of biome
        private const val BIOME_VITAE_MODIFIER_PER_INTERVAL = 5.0

        // Max of +/- 15 vitae per interval because of depth
        private const val DEPTH_VITAE_MODIFIER_PER_INTERVAL = 15.0

        // Max of +/- 15 vitae per interval because of heat
        private const val HEAT_VITAE_MODIFIER_PER_INTERVAL = 15.0
        private const val HEAT_SAMPLE_DISTANCE = 4
        private const val HEAT_SAMPLE_COUNT = 7

        // How much heat each block emits. Default is 0, and ranges from -1 to 1
        private val HEAT_BLOCK_EMISSION = mapOf(
            // Warm blocks
            Blocks.BLAST_FURNACE to 0.2,
            Blocks.FURNACE to 0.1,
            Blocks.CAMPFIRE to 0.8,
            Blocks.FIRE to 0.8,
            Blocks.LAVA to 1.0,
            Blocks.MAGMA_BLOCK to 0.4,
            Blocks.LANTERN to 0.2,
            Blocks.JACK_O_LANTERN to 0.05,
            Blocks.SOUL_LANTERN to 0.05,
            Blocks.TORCH to 0.5,
            Blocks.WALL_TORCH to 0.5,
            Blocks.SOUL_TORCH to 0.1,
            Blocks.SOUL_WALL_TORCH to 0.1,
            Blocks.REDSTONE_TORCH to 0.1,
            Blocks.REDSTONE_WALL_TORCH to 0.1,
            // Cold blocks
            Blocks.FROSTED_ICE to -0.8,
            Blocks.ICE to -0.8,
            Blocks.PACKED_ICE to -0.9,
            Blocks.BLUE_ICE to -1.0,
            Blocks.WATER to -0.3,
            Blocks.SNOW_BLOCK to -0.5,
            Blocks.SNOW to -0.4,
        )
    }
}