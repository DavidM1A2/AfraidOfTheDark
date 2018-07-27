/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NightmareWorldProvider extends WorldProvider
{
    @Override
    protected void init()
    {
        this.biomeProvider = new BiomeProviderSingle(ModBiomes.nightmare);
        this.hasSkyLight = false;

        if (this.world.isRemote)
        {
            this.setSkyRenderer(new NightmareSkyRenderer());
        }
    }

    /**
	 * Returns the sub-folder of the world folder that this WorldProvider saves to. EXA: DIM1, DIM-1
	 * 
	 * @return The sub-folder name to save this world's chunks to.
	 */
	@Override
	public String getSaveFolder()
	{
		return "NightmareWorld";
	}

	/**
	 * Returns a double value representing the Y value relative to the top of the map at which void fog is at its maximum. The default factor of
	 * 0.03125 relative to 256, for example, means the void fog will be at its maximum at (256*0.03125), or 8.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public double getVoidFogYFactor()
	{
		return 0.25d;
	}

	@Override
	public boolean isDaytime()
	{
		return false;
	}

	/**
	 * The current sun brightness factor for this dimension. 0.0f means no light at all, and 1.0f means maximum sunlight. This will be used for the
	 * "calculateSkylightSubtracted" which is for Sky light value calculation.
	 *
	 * @return The current brightness factor
	 */
	@Override
	public float getSunBrightnessFactor(float par1)
	{
		return 0.0f;
	}

	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new NightmareChunkGenerator(this.world);
	}

	@Override
	public int getAverageGroundLevel()
	{
		return 255;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int par1, int par2)
	{
		return true;
	}

	@Override
	public DimensionType getDimensionType()
	{
		return DimensionType.getById(AOTDDimensions.Nightmare.getWorldID());
	}

	/**
	 * Gets the Star Brightness for rendering sky.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1)
	{
		return 13f;
	}

	@Override
	public boolean canRespawnHere()
	{
		return true;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public float getCloudHeight()
	{
		return 255.0F;
	}

	@Override
	protected void generateLightBrightnessTable()
	{
		float f = 0.0F;

		for (int i = 0; i <= 15; ++i)
		{
			float f1 = 1.0F - i / 15.0F;
			this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}
	}

	@Override
	public boolean shouldMapSpin(String entity, double x, double y, double z)
	{
		return true;
	}

	/**
	 * Determines the dimension the player will be respawned in, typically this brings them back to the overworld.
	 *
	 * @param player
	 *            The player that is respawning
	 * @return The dimension to respawn the player in
	 */
	@Override
	public int getRespawnDimension(net.minecraft.entity.player.EntityPlayerMP player)
	{
		return player.dimension;
	}

	@Override
	public boolean canCoordinateBeSpawn(int x, int z)
	{
		return x % AOTDDimensions.getBlocksBetweenIslands() == 0 && z == 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float par1, float par2)
	{
		return new float[]
		{ 0, 0, 0, 0 };
	}

	@Override
	public float calculateCelestialAngle(long par1, float par3)
	{
		return 0.5f;
	}
}
