package com.DavidM1A2.afraidofthedark.common.dimension.nightmare;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.dimension.NightmareSkyRenderer;
import com.DavidM1A2.afraidofthedark.common.constants.ModBiomes;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Class that provides the nightmare world
 */
public class NightmareWorldProvider extends WorldProvider
{
    /**
     * Initializes the provider
     */
    @Override
    protected void init()
    {
        // The only biome is nightmare
        this.biomeProvider = new BiomeProviderSingle(ModBiomes.NIGHTMARE);
        // The sky provides no light
        this.hasSkyLight = false;
        // Register the sky renderer client side
        if (this.world.isRemote)
        {
            this.setSkyRenderer(new NightmareSkyRenderer());
        }
    }

    /**
     * @return The name of the save folder to save world data in
     */
    @Nullable
    @Override
    public String getSaveFolder()
    {
        return "nightmare_world";
    }

    /**
     * Returns a double value representing the Y value relative to the top of the map at which void fog is at its maximum. The default factor of
     * 0.03125 relative to 256, for example, means the void fog will be at its maximum at (256*0.03125), or 8.
     *
     * @return 0.25, or max fog at y = 64
     */
    @Override
    @SideOnly(Side.CLIENT)
    public double getVoidFogYFactor()
    {
        return 0.25d;
    }

    /**
     * Sets the fog color to dark red
     *
     * @param p_76562_1_ ignored
     * @param p_76562_2_ ignored
     * @return A red RGB color
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
    {
        return new Vec3d(0.2, 0.0, 0.0);
    }

    /**
     * @return False, nightmare is always night
     */
    @Override
    public boolean isDaytime()
    {
        return false;
    }

    /**
     * Initializes the nightmare world generator
     *
     * @return A new nightmare chunk generator
     */
    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return new NightmareChunkGenerator(this.world);
    }

    /**
     * Average ground level is 64 like in the overworld
     *
     * @return The average ground level
     */
    @Override
    public int getAverageGroundLevel()
    {
        return 72;
    }

    /**
     * Always show fog
     *
     * @param x The chunk x
     * @param z The chunk z
     * @return True, always show fog
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z)
    {
        return true;
    }

    @Override
    public DimensionType getDimensionType()
    {
        return ModDimensions.NIGHTMARE;
    }

    /**
     * Star brightness is very high
     *
     * @param par1 ignored
     * @return Return a value from 0 to 16 for star brightness
     */
    @Override
    public float getStarBrightness(float par1)
    {
        return 13f;
    }

    /**
     * @return We can respawn here, but it will teleport the player back right after
     */
    @Override
    public boolean canRespawnHere()
    {
        return true;
    }

    /**
     * @return False, this is not a standard surface world
     */
    @Override
    public boolean isSurfaceWorld()
    {
        return false;
    }

    /**
     * @return 255, put clouds at max height so they don't get in the way
     */
    @Override
    public float getCloudHeight()
    {
        return 255.0f;
    }

    /**
     * The map will always spin here
     *
     * @param entity The entity with the map
     * @param x The chunk x
     * @param z The chunk z
     * @param rotation the regular rotation of the marker
     * @return True to 'spin' the cursor
     */
    @Override
    public boolean shouldMapSpin(String entity, double x, double z, double rotation)
    {
        return true;
    }

    /**
     * Return the dimension the player is in so that they respawn in the same dimension
     *
     * @param player The player who is respawning
     * @return The dimension id
     */
    @Override
    public int getRespawnDimension(EntityPlayerMP player)
    {
        return player.dimension;
    }

    /**
     * True if the coordinate can spawn players, false otherwise
     *
     * @param x The blockpos x
     * @param z The blockpos z
     * @return True if x is a multiple of the blocks between islands and z is 0, false otherwise
     */
    @Override
    public boolean canCoordinateBeSpawn(int x, int z)
    {
        return x % AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands() == 0 && z == 0;
    }

    /**
     * Sunset doesn't exist, return all 0s
     *
     * @param celestialAngle The angle of the sky
     * @param partialTicks The ticks since last
     * @return All 0s
     */
    @Nullable
    @Override
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
    {
        return new float[] {0, 0, 0, 0};
    }

    /**
     * Don't allow rain in the nightmare
     *
     * @param chunk The chunk to test
     * @return False, no rain allowed
     */
    @Override
    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return false;
    }

    /**
     * Keep the sky locked at 0.5 (technically, 'noon')
     *
     * @param worldTime The current time of the world
     * @param partialTicks The ticks since the last tick
     * @return The celestial sky angle
     */
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks)
    {
        return 0.5f;
    }
}
