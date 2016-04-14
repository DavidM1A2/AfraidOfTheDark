/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class Point3D
{
	private int x;
	private int y;
	private int z;

	public Point3D(NBTTagCompound compound)
	{
		this.readFromNBT(compound);
	}

	public Point3D(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D(Entity target)
	{
		this.x = target.getPosition().getX();
		this.y = target.getPosition().getY();
		this.z = target.getPosition().getZ();
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

	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("x", this.x);
		compound.setInteger("y", this.y);
		compound.setInteger("z", this.z);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.x = compound.getInteger("x");
		this.y = compound.getInteger("y");
		this.z = compound.getInteger("z");
	}
}
