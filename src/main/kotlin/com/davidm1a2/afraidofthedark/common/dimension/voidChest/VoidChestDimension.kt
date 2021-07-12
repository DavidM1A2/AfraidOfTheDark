package com.davidm1a2.afraidofthedark.common.dimension.voidChest

import com.davidm1a2.afraidofthedark.client.dimension.VoidChestSkyRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModBiomes
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.biome.provider.SingleBiomeProvider
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings
import net.minecraft.world.dimension.Dimension
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.ChunkGenerator
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.IRenderHandler

/**
 * Class that provides the void chest world
 *
 * @param world The world for this dimension
 * @param dimensionType The dimension type of this dimension
 */
class VoidChestDimension(world: World, dimensionType: DimensionType) : Dimension(world, dimensionType, 0.0f) {
    // TODO: As of 1.14 we can't use ModDimensions.VOID_CHEST_TYPE because this does not get initialized client side
    // when connecting to a dedicated server. Instead, the server sends us a dummy dimension type which we need to
    // retrieve via DimensionType.byName(). We don't have this dummy dimension until we actually join the dimension :/
    private val cachedType: DimensionType by lazy {
        DimensionType.byName(ModDimensions.VOID_CHEST.registryName!!)!!
    }

    init {
        // Register the sky renderer client side
        if (this.world.isRemote) {
            skyRenderer = VoidChestSkyRenderer()
        }

        // Create the light brightness table so that everything is somewhat lit
        for (i in 0..15) {
            val f1 = 1.0f - i / 15.0f
            lightBrightnessTable[i] = (1.0f - f1) / (f1 * 3.0f + 1.0f)
        }
    }

    /**
     * @return The dimension type
     */
    override fun getType(): DimensionType {
        return cachedType
    }

    /**
     * Initializes the nightmare world generator
     *
     * @return A new nightmare chunk generator
     */
    override fun createChunkGenerator(): ChunkGenerator<*> {
        val biomeProvider = SingleBiomeProvider(SingleBiomeProviderSettings(this.world.worldInfo).setBiome(ModBiomes.VOID_CHEST))
        return VoidChestChunkGenerator(world, biomeProvider, VoidChestGenerationSettings())
    }

    /**
     * Tests if the X,Z can show fog or not, return false because there's no fog
     *
     * @param x ignored
     * @param z ignored
     * @return false, there's no fog in the void chest
     */
    @OnlyIn(Dist.CLIENT)
    override fun doesXZShowFog(x: Int, z: Int): Boolean {
        return false
    }

    /**
     * Keep the sky locked at 0.25 (day time)
     *
     * @param worldTime    The current time of the world
     * @param partialTicks The ticks since the last tick
     * @return The celestial sky angle
     */
    override fun calculateCelestialAngle(worldTime: Long, partialTicks: Float): Float {
        return 0.25f
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
     * Sets the fog color to white
     *
     * @param p_76562_1_ ignored
     * @param p_76562_2_ ignored
     * @return A white RGB color
     */
    @OnlyIn(Dist.CLIENT)
    override fun getFogColor(p_76562_1_: Float, p_76562_2_: Float): Vec3d {
        return Vec3d(1.0, 1.0, 1.0)
    }

    /**
     * @return The height of the clouds, set it at 255 so that they're out of the way
     */
    @OnlyIn(Dist.CLIENT)
    override fun getCloudHeight(): Float {
        return 255f
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

    override fun getSkyRenderer(): IRenderHandler? {
        if (super.getSkyRenderer() == null) {
            skyRenderer = VoidChestSkyRenderer()
        }
        return super.getSkyRenderer()
    }
}