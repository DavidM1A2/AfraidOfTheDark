package com.DavidM1A2.AfraidOfTheDark.common.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3i;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class NightmareTeleporter extends Teleporter
{
	public NightmareTeleporter(WorldServer worldIn)
	{
		super(worldIn);
	}

	@Override
	public void func_180266_a(Entity entity, float entityYaw)
	{
		for (int i = 0; i < 9; i++)
		{
			entity.worldObj.setBlockState(entity.getPosition().add(new Vec3i(0, -5, 0)), Blocks.stone.getDefaultState());
		}
		return;
	}
}
