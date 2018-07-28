/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDLootTables;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDSchematics;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;

public class NightmareChunkGenerator implements IChunkGenerator
{
	private final World world;

	public NightmareChunkGenerator(World world)
	{
		this.world = world;
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}

	@Override
	public Chunk generateChunk(int x, int z)
	{
		ChunkPrimer chunkprimer = new ChunkPrimer();
		IBlockState iblockstate = Blocks.AIR.getDefaultState();

		Chunk chunk = new Chunk(this.world, chunkprimer, x, z);

		byte[] biome = chunk.getBiomeArray();

		for (int i = 0; i < biome.length; i++)
		{
			biome[i] = (byte) Biome.getIdForBiome(ModBiomes.nightmare);
		}

		chunk.generateSkylightMap();

		return chunk;
	}

	@Override
	public void populate(int x, int z)
	{
		// Every 62 chunks in the x direction (992 blocks)
		if (x * 16 % AOTDDimensions.getBlocksBetweenIslands() == 0 && z == 0 && x != 0)
		{
			FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString("A player has entered his/her nightmare realm for the first time. Expect a server freeze for the next 5 or so seconds."));

			SchematicGenerator.generateSchematicWithLoot(AOTDSchematics.NightmareIsland.getSchematic(), this.world, x * 16, 0, z * 16, AOTDLootTables.NightmareIsland.getLootTable());
		}
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPos)
	{
		return this.world.getBiome(blockPos).getSpawnableList(enumCreatureType);
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
	{
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunk, int x, int z)
	{
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
	{
		return false;
	}
}
