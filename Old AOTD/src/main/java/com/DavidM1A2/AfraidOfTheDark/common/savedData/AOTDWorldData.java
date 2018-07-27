/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.savedData;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class AOTDWorldData extends WorldSavedData
{
	private static final String IDENTIFIER = "AOTDWorldData";

	// PROPERTIES =============================================================
	private HashSet<Point3D> dungeonsAboveGround = new HashSet<Point3D>();
	private HashSet<Point3D> dungeonsBelowGround = new HashSet<Point3D>();

	// CONSTRUCTOR, GETTER, REGISTER ==========================================

	public AOTDWorldData()
	{
		super(getIdentifier());
	}

	public AOTDWorldData(String string)
	{
		super(getIdentifier());
	}

	private static String getIdentifier()
	{
		return "AOTDWorldData";
	}

	public static AOTDWorldData get(World world)
	{
		return (AOTDWorldData) world.loadData(AOTDWorldData.class, getIdentifier());
	}

	public static void register(World world)
	{
		if (world.loadData(AOTDWorldData.class, getIdentifier()) == null)
		{
			world.setData(getIdentifier(), new AOTDWorldData());
		}
	}

	// LOAD, SAVE =============================================================

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("numberDungeonsAbove", this.dungeonsAboveGround.size());
		nbt.setInteger("numberDungeonsBelow", this.dungeonsBelowGround.size());
		Iterator<Point3D> iterator = this.dungeonsAboveGround.iterator();
		int index = 0;
		while (iterator.hasNext())
		{
			Point3D current = iterator.next();
			NBTTagCompound pointData = new NBTTagCompound();
			current.writeToNBT(pointData);
			nbt.setTag("pointA " + index, pointData);
			index = index + 1;
		}
		iterator = this.dungeonsBelowGround.iterator();
		index = 0;
		while (iterator.hasNext())
		{
			Point3D current = iterator.next();
			NBTTagCompound pointData = new NBTTagCompound();
			current.writeToNBT(pointData);
			nbt.setTag("pointB " + index, pointData);
			index = index + 1;
		}
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.dungeonsAboveGround = new HashSet<Point3D>();
		this.dungeonsBelowGround = new HashSet<Point3D>();
		for (int i = 0; i < nbt.getInteger("numberDungeonsAbove"); i++)
		{
			NBTTagCompound pointData = nbt.getCompoundTag("pointA " + i);
			Point3D point = new Point3D(pointData);
			this.dungeonsAboveGround.add(point);
		}
		for (int i = 0; i < nbt.getInteger("numberDungeonsBelow"); i++)
		{
			NBTTagCompound pointData = nbt.getCompoundTag("pointB " + i);
			Point3D point = new Point3D(pointData);
			this.dungeonsBelowGround.add(point);
		}
	}

	// GETTER, SETTER, SYNCER =================================================

	public void addDungeon(Point3D location, boolean isAboveGround)
	{
		if (isAboveGround)
		{
			this.dungeonsAboveGround.add(location);
		}
		else
		{
			this.dungeonsBelowGround.add(location);
		}
		this.markDirty();
	}

	public boolean isValidLocation(Point3D location, boolean isAboveGround)
	{
		if (isAboveGround)
		{
			for (Point3D otherLocation : this.dungeonsAboveGround)
			{
				double distance = Math.sqrt((otherLocation.getZ() - location.getZ()) * (otherLocation.getZ() - location.getZ()) + (otherLocation.getX() - location.getX()) * (otherLocation.getX() - location.getX()));
				if (distance <= (location.getY() + otherLocation.getY()))
				{
					return false;
				}
			}
		}
		else
		{
			for (Point3D otherLocation : this.dungeonsBelowGround)
			{
				double distance = Math.sqrt((otherLocation.getZ() - location.getZ()) * (otherLocation.getZ() - location.getZ()) + (otherLocation.getX() - location.getX()) * (otherLocation.getX() - location.getX()));
				if (distance <= (location.getY() + otherLocation.getY()))
				{
					return false;
				}
			}
		}
		return true;
	}

	public Set<Point3D> getDungeonsAboveGround()
	{
		return this.dungeonsAboveGround;
	}

	public Set<Point3D> getDungeonsBelowGround()
	{
		return this.dungeonsBelowGround;
	}
}
