package com.DavidM1A2.afraidofthedark.common.dimension.voidChest;

import com.DavidM1A2.afraidofthedark.client.dimension.VoidChestSkyRenderer;
import com.DavidM1A2.afraidofthedark.common.constants.ModBiomes;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Class that provides the void chest world
 */
public class VoidChestWorldProvider extends WorldProvider
{
	/**
	 * Initializes the void chest world provider
	 */
	@Override
	protected void init()
	{
		// Initialize the biome provider
		this.biomeProvider = new BiomeProviderSingle(ModBiomes.VOID_CHEST);
		// We have no sky light
		this.hasSkyLight = false;
		// If we're on client side set the sky renderer
		if (this.world.isRemote)
			this.setSkyRenderer(new VoidChestSkyRenderer());
	}

	/**
	 * Returns the sub-folder of the world folder that this WorldProvider saves to. ex: DIM1, DIM-1
	 *
	 * @return The sub-folder name to save this world's chunks to.
	 */
	@Nullable
	@Override
	public String getSaveFolder()
	{
		return "void_chest_world";
	}

	/**
	 * @return The dimension type
	 */
	@Override
	public DimensionType getDimensionType()
	{
		return ModDimensions.VOID_CHEST;
	}

	/**
	 * Creates the chunk generator for the void chest world
	 *
	 * @return A new VoidChestChunkGenerator
	 */
	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new VoidChestChunkGenerator(this.world);
	}

	/**
	 * @return The ground level which will be the bottom of the barriers at level 100
	 */
	@Override
	public int getAverageGroundLevel()
	{
		return 100;
	}

	/**
	 * Tests if the X,Z can show fog or not, return false because there's no fog
	 *
	 * @param x ignored
	 * @param z ignored
	 * @return false, there's no fog in the void chest
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int x, int z)
	{
		return false;
	}

	/**
	 * Gets the brightness of the stars in the world from 0 to 1
	 *
	 * @param partialTicks ignored
	 * @return The star brightness
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float partialTicks)
	{
		return 1f;
	}

	/**
	 * @return True since players can respawn here
	 */
	@Override
	public boolean canRespawnHere()
	{
		return true;
	}

	/**
	 * @return False, this is not a surface world
	 */
	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}

	/**
	 * @return The height of the clouds, set it at 255 so that they're out of the way
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight()
	{
		return 255f;
	}

	/**
	 * Creates a light brightness table that
	 */
	@Override
	protected void generateLightBrightnessTable()
	{
		// Create the light brightness table so that everything is somewhat lit
		for (int i = 0; i <= 15; ++i)
		{
			float f1 = 1.0F - i / 15.0F;
			this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F);
		}
	}

	/**
	 * The map should never spin
	 *
	 * @param entity ignored
	 * @param x ignored
	 * @param z ignored
	 * @param rotation ignored
	 * @return false
	 */
	@Override
	public boolean shouldMapSpin(String entity, double x, double z, double rotation)
	{
		return false;
	}
}
