package com.DavidM1A2.AfraidOfTheDark.common.dimension;

import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

public class NightmareWorldProvider extends WorldProvider
{
	private float[] colorsSunriseSunset = new float[4];

	@Override
	public void registerWorldChunkManager()
	{
		this.dimensionId = Constants.NightmareWorld.NIGHTMARE_WORLD_ID;
		this.worldChunkMgr = new WorldChunkManager(ModBiomes.erieForest.biomeID, WorldType.CUSTOMIZED, "");
		this.hasNoSky = false;
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), false, "3;1*afraidofthedark:gravewood;1;biome_1");
	}

	@Override
	public int getAverageGroundLevel()
	{
		return 0;
	}

	//	@SideOnly(Side.CLIENT)
	//	public boolean doesXZShowFog(int par1, int par2)
	//	{
	//		return false;
	//	}

	@Override
	public String getDimensionName()
	{
		return Constants.NightmareWorld.NIGHTMARE_WORLD_NAME;
	}

	/**
	 * Gets the Star Brightness for rendering sky.
	 * */
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1)
	{
		return 15f;
	}

	@Override
	public boolean canRespawnHere()
	{
		return false;
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

	//	@Override
	//	protected void generateLightBrightnessTable()
	//	{
	//		float f = 12.0F;
	//		for (int i = 0; i <= 15; i++)
	//		{
	//			float f1 = 12.0F - i / 15.0F;
	//			this.lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f);
	//		}
	//	}

	@SideOnly(Side.CLIENT)
	public String getWelcomeMessage()
	{
		return "Welcome...";
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
		return 0;
	}

	public BiomeGenBase getBiomeGenForCoords(BlockPos pos)
	{
		return ModBiomes.erieForest;
	}

	@Override
	public boolean canCoordinateBeSpawn(int par1, int par2)
	{
		return true;
	}

	//	@Override
	//	@SideOnly(Side.CLIENT)
	//	public float[] calcSunriseSunsetColors(float par1, float par2)
	//	{
	//		float f2 = 0.4F;
	//		float f3 = MathHelper.cos(par1 * 3.141593F * 2.0F) - 0.0F;
	//		float f4 = -0.0F;
	//		if ((f3 >= f4 - f2) && (f3 <= f4 + f2))
	//		{
	//			float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
	//			float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * 3.141593F)) * 0.99F;
	//			f6 *= f6;
	//			this.colorsSunriseSunset[0] = (f5 * 0.3F + 0.7F);
	//			this.colorsSunriseSunset[1] = (f5 * f5 * 0.7F + 0.2F);
	//			this.colorsSunriseSunset[2] = (f5 * f5 * 0.0F + 0.2F);
	//			this.colorsSunriseSunset[3] = f6;
	//			return this.colorsSunriseSunset;
	//		}
	//		return null;
	//	}

	//	@Override
	//	public float calculateCelestialAngle(long par1, float par3)
	//	{
	//		int j = (int) (par1 % 24000L);
	//		float f1 = (j + par3) / 24000.0F - 0.25F;
	//		if (f1 < 0.0F)
	//		{
	//			f1 += 1.0F;
	//		}
	//		if (f1 > 1.0F)
	//		{
	//			f1 -= 1.0F;
	//		}
	//		float f2 = f1;
	//		f1 = 1.0F - (float) ((Math.cos(f1 * Math.PI) + 1.0D) / 2.0D);
	//		f1 = f2 + (f1 - f2) / 3.0F;
	//		return f1;
	//	}
	//
	//	@SideOnly(Side.CLIENT)
	//	public Vec3 getFogColor(float par1, float par2)
	//	{
	//		int i = 10518688;
	//		float f2 = MathHelper.cos(par1 * 3.141593F * 2.0F) * 2.0F + 0.5F;
	//		if (f2 < 0.0F)
	//		{
	//			f2 = 0.0F;
	//		}
	//		if (f2 > 1.0F)
	//		{
	//			f2 = 1.0F;
	//		}
	//		float f3 = (i >> 16 & 0xFF) / 255.0F;
	//		float f4 = (i >> 8 & 0xFF) / 255.0F;
	//		float f5 = (i & 0xFF) / 255.0F;
	//		f3 *= (f2 * 0.0F + 0.15F);
	//		f4 *= (f2 * 0.0F + 0.15F);
	//		f5 *= (f2 * 0.0F + 0.15F);
	//		return new Vec3(f3, f4, f5);
	//	}

	@Override
	public String getInternalNameSuffix()
	{
		return "";
	}

}
