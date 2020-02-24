package com.davidm1a2.afraidofthedark.common.dimension.voidChest

import com.davidm1a2.afraidofthedark.client.dimension.VoidChestSkyRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.world.DimensionType
import net.minecraft.world.WorldProvider
import net.minecraft.world.biome.BiomeProviderSingle
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class that provides the void chest world
 */
class VoidChestWorldProvider : WorldProvider() {
    /**
     * Initializes the void chest world provider
     */
    override fun init() {
        // Initialize the biome provider
        biomeProvider = BiomeProviderSingle(ModBiomes.VOID_CHEST)
        // We have no sky light
        hasSkyLight = false
        // If we're on client side set the sky renderer
        if (world.isRemote) {
            skyRenderer = VoidChestSkyRenderer()
        }
    }

    /**
     * Returns the sub-folder of the world folder that this WorldProvider saves to. ex: DIM1, DIM-1
     *
     * @return The sub-folder name to save this world's chunks to.
     */
    override fun getSaveFolder(): String {
        return "void_chest_world"
    }

    /**
     * @return The dimension type
     */
    override fun getDimensionType(): DimensionType {
        return ModDimensions.VOID_CHEST
    }

    /**
     * Creates the chunk generator for the void chest world
     *
     * @return A new VoidChestChunkGenerator
     */
    override fun createChunkGenerator(): IChunkGenerator {
        return VoidChestChunkGenerator(world)
    }

    /**
     * @return The ground level which will be the bottom of the barriers at level 100
     */
    override fun getAverageGroundLevel(): Int {
        return 100
    }

    /**
     * Tests if the X,Z can show fog or not, return false because there's no fog
     *
     * @param x ignored
     * @param z ignored
     * @return false, there's no fog in the void chest
     */
    @SideOnly(Side.CLIENT)
    override fun doesXZShowFog(x: Int, z: Int): Boolean {
        return false
    }

    /**
     * Gets the brightness of the stars in the world from 0 to 1
     *
     * @param partialTicks ignored
     * @return The star brightness
     */
    @SideOnly(Side.CLIENT)
    override fun getStarBrightness(partialTicks: Float): Float {
        return 1f
    }

    /**
     * @return True since players can respawn here
     */
    override fun canRespawnHere(): Boolean {
        return true
    }

    /**
     * @return False, this is not a surface world
     */
    override fun isSurfaceWorld(): Boolean {
        return false
    }

    /**
     * @return The height of the clouds, set it at 255 so that they're out of the way
     */
    @SideOnly(Side.CLIENT)
    override fun getCloudHeight(): Float {
        return 255f
    }

    /**
     * Creates a light brightness table that
     */
    override fun generateLightBrightnessTable() {
        // Create the light brightness table so that everything is somewhat lit
        for (i in 0..15) {
            val f1 = 1.0f - i / 15.0f
            lightBrightnessTable[i] = (1.0f - f1) / (f1 * 3.0f + 1.0f)
        }
    }

    /**
     * The map should never spin
     *
     * @param entity   ignored
     * @param x        ignored
     * @param z        ignored
     * @param rotation ignored
     * @return false
     */
    override fun shouldMapSpin(entity: String, x: Double, z: Double, rotation: Double): Boolean {
        return false
    }
}