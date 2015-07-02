package com.DavidM1A2.AfraidOfTheDark.common.utility;

import net.minecraft.util.BlockPos;

public class Point3D
{
	private final int x;
	private final int y;
	private final int z;

	public Point3D(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockPos toBlockPos()
	{
		return new BlockPos(this.x, this.y, this.z);
	}
}
