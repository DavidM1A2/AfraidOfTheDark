/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import java.io.Serializable;

import net.minecraft.util.BlockPos;

public class Point3D implements Serializable
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

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public int getZ()
	{
		return this.z;
	}
}
