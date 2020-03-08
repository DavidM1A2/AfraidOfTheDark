package com.davidm1a2.afraidofthedark.common.dimension.nightmare

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.dimension.NightmareSkyRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.math.Vec3d
import net.minecraft.world.DimensionType
import net.minecraft.world.WorldProvider
import net.minecraft.world.biome.BiomeProviderSingle
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class that provides the nightmare world
 */
class NightmareWorldProvider : WorldProvider() {
    /**
     * Initializes the provider
     */
    override fun init() {
        // The only biome is nightmare
        biomeProvider = BiomeProviderSingle(ModBiomes.NIGHTMARE)
        // We need a sky light for torches to light up properly
        hasSkyLight = true
        // Register the sky renderer client side
        if (world.isRemote) {
            skyRenderer = NightmareSkyRenderer()
        }
    }

    /**
     * @return The name of the save folder to save world data in
     */
    override fun getSaveFolder(): String {
        return "nightmare_world"
    }

    /**
     * Returns a double value representing the Y value relative to the top of the map at which void fog is at its maximum. The default factor of
     * 0.03125 relative to 256, for example, means the void fog will be at its maximum at (256*0.03125), or 8.
     *
     * @return 0.25, or max fog at y = 64
     */
    @SideOnly(Side.CLIENT)
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
    @SideOnly(Side.CLIENT)
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
    override fun createChunkGenerator(): IChunkGenerator {
        return NightmareChunkGenerator(world)
    }

    /**
     * Average ground level is 64 like in the overworld
     *
     * @return The average ground level
     */
    override fun getAverageGroundLevel(): Int {
        return 72
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
    @SideOnly(Side.CLIENT)
    override fun doesXZShowFog(x: Int, z: Int): Boolean {
        return true
    }

    /**
     * @return The dimension type will be 'Nightmare'
     */
    override fun getDimensionType(): DimensionType {
        return ModDimensions.NIGHTMARE
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
    override fun getRespawnDimension(player: EntityPlayerMP): Int {
        return player.dimension
    }

    /**
     * True if the coordinate can spawn players, false otherwise
     *
     * @param x The blockpos x
     * @param z The blockpos z
     * @return True if x is a multiple of the blocks between islands and z is 0, false otherwise
     */
    override fun canCoordinateBeSpawn(x: Int, z: Int): Boolean {
        return x % AfraidOfTheDark.INSTANCE.configurationHandler.blocksBetweenIslands == 0 && z == 0
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
     * @param partialTicks ignored
     * @return We don't have a sun, so return 0
     */
    override fun getSunBrightnessFactor(partialTicks: Float): Float {
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