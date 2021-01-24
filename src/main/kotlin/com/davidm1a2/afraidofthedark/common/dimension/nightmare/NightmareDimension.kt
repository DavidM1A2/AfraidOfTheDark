package com.davidm1a2.afraidofthedark.common.dimension.nightmare

import com.davidm1a2.afraidofthedark.client.dimension.NightmareSkyRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.biome.provider.SingleBiomeProvider
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.dimension.Dimension
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.ChunkGenerator
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Class that provides the nightmare world
 *
 * @param world The world for this dimension
 * @param dimensionType The dimension type of this dimension
 */
class NightmareDimension(world: World, dimensionType: DimensionType) : Dimension(world, dimensionType) {
    init {
        // Register the sky renderer client side
        if (world.isRemote) {
            skyRenderer = NightmareSkyRenderer()
        }
    }

    /**
     * Finds a valid spawn spot in a chunk
     *
     * @param chunkPos The chunk pos to find a spawn in
     * @param checkValid If the spawn should be "valid" as in not water or lava, etc
     */
    override fun findSpawn(chunkPos: ChunkPos, checkValid: Boolean): BlockPos {
        // Spawn players at 255, they'll get teleported right after
        return BlockPos(chunkPos.x * 16, 255, chunkPos.z * 16)
    }

    /**
     * Finds a valid spawn spot at a position
     *
     * @param x The x position to find a spawn in
     * @param z The z position to find a spawn in
     * @param checkValid If the spawn should be "valid" as in not water or lava, etc
     */
    override fun findSpawn(x: Int, z: Int, checkValid: Boolean): BlockPos {
        // Spawn players at 255, they'll get teleported right after
        return BlockPos(x, 255, z)
    }

    /**
     * Returns a double value representing the Y value relative to the top of the map at which void fog is at its maximum. The default factor of
     * 0.03125 relative to 256, for example, means the void fog will be at its maximum at (256*0.03125), or 8.
     *
     * @return 0.25, or max fog at y = 64
     */
    @OnlyIn(Dist.CLIENT)
    override fun getVoidFogYFactor(): Double {
        return 0.25
    }

    /**
     * Sets the fog color to dark red
     *
     * @param p_76562_1_ ignored
     * @param p_76562_2_ ignored
     * @return A red RGB color
     */
    @OnlyIn(Dist.CLIENT)
    override fun getFogColor(p_76562_1_: Float, p_76562_2_: Float): Vec3d {
        return Vec3d(0.2, 0.0, 0.0)
    }

    /**
     * @return False, nightmare is always night
     */
    override fun isDaytime(): Boolean {
        return false
    }

    /**
     * Initializes the nightmare world generator
     *
     * @return A new nightmare chunk generator
     */
    override fun createChunkGenerator(): ChunkGenerator<*> {
        val biomeProvider = SingleBiomeProvider(SingleBiomeProviderSettings().setBiome(ModBiomes.NIGHTMARE))
        return NightmareChunkGenerator(world, biomeProvider, NightmareGenerationSettings())
    }

    /**
     * Creates the map of light level to brightness
     */
    override fun generateLightBrightnessTable() {
        super.generateLightBrightnessTable()
        // Make the brightest light 60% of what it would be in the overworld
        lightBrightnessTable.forEachIndexed { index, value -> lightBrightnessTable[index] = value * 0.6f }
    }

    /**
     * Always show fog
     *
     * @param x The chunk x
     * @param z The chunk z
     * @return True, always show fog
     */
    @OnlyIn(Dist.CLIENT)
    override fun doesXZShowFog(x: Int, z: Int): Boolean {
        return true
    }

    /**
     * @return The dimension type will be 'Nightmare'
     */
    override fun getType(): DimensionType {
        return ModDimensions.NIGHTMARE_TYPE
    }

    /**
     * @return We can respawn here, but it will teleport the player back right after
     */
    override fun canRespawnHere(): Boolean {
        return true
    }

    /**
     * @return False, this is not a standard surface world
     */
    override fun isSurfaceWorld(): Boolean {
        return false
    }

    /**
     * @return 255, put clouds at max height so they don't get in the way
     */
    override fun getCloudHeight(): Float {
        return 255.0f
    }

    /**
     * The map will always spin here
     *
     * @param entity   The entity with the map
     * @param x        The chunk x
     * @param z        The chunk z
     * @param rotation the regular rotation of the marker
     * @return True to 'spin' the cursor
     */
    override fun shouldMapSpin(entity: String, x: Double, z: Double, rotation: Double): Boolean {
        return true
    }

    /**
     * Return the dimension the player is in so that they respawn in the same dimension
     *
     * @param player The player who is respawning
     * @return The dimension id
     */
    override fun getRespawnDimension(player: ServerPlayerEntity): DimensionType {
        return player.dimension
    }

    /**
     * Sunset doesn't exist, return all 0s
     *
     * @param celestialAngle The angle of the sky
     * @param partialTicks   The ticks since last
     * @return All 0s
     */
    override fun calcSunriseSunsetColors(celestialAngle: Float, partialTicks: Float): FloatArray {
        return floatArrayOf(0f, 0f, 0f, 0f)
    }

    /**
     * Don't allow rain in the nightmare
     *
     * @param chunk The chunk to test
     * @return False, no rain allowed
     */
    override fun canDoRainSnowIce(chunk: Chunk): Boolean {
        return false
    }

    /**
     * @param partialTicks ignored
     * @return We don't have a sun, so return 0
     */
    override fun getSunBrightness(partialTicks: Float): Float {
        return 0f
    }

    /**
     * Keep the sky locked at 0.75 (night time)
     *
     * @param worldTime    The current time of the world
     * @param partialTicks The ticks since the last tick
     * @return The celestial sky angle
     */
    override fun calculateCelestialAngle(worldTime: Long, partialTicks: Float): Float {
        return 0.75f
    }
}