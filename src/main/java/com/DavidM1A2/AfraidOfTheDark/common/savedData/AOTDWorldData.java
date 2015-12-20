/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.savedData;

import java.util.HashSet;
import java.util.Set;

import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTObjectWriter;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class AOTDWorldData extends WorldSavedData
{
	private static final String IDENTIFIER = "AOTDWorldData";

	// PROPERTIES =============================================================
	private Set<Point3D> dungeonsAboveGround = new HashSet<Point3D>();
	private Set<Point3D> dungeonsBelowGround = new HashSet<Point3D>();

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
		return (AOTDWorldData) world.loadItemData(AOTDWorldData.class, getIdentifier());
	}

	public static void register(World world)
	{
		if (world.loadItemData(AOTDWorldData.class, getIdentifier()) == null)
		{
			world.setItemData(getIdentifier(), new AOTDWorldData());
		}
	}

	// LOAD, SAVE =============================================================

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.dungeonsAboveGround = (Set<Point3D>) NBTObjectWriter.readObjectFromNBT("AOTDDungeonsAboveGround", nbt);
		if (this.dungeonsAboveGround == null)
		{
			this.dungeonsAboveGround = new HashSet<Point3D>();
		}
		this.dungeonsBelowGround = (Set<Point3D>) NBTObjectWriter.readObjectFromNBT("AOTDDungeonsBelowGround", nbt);
		if (this.dungeonsBelowGround == null)
		{
			this.dungeonsBelowGround = new HashSet<Point3D>();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTObjectWriter.writeObjectToNBT("AOTDDungeonsAboveGround", this.dungeonsAboveGround, nbt);
		NBTObjectWriter.writeObjectToNBT("AOTDDungeonsBelowGround", this.dungeonsBelowGround, nbt);
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
