/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public enum AOTDCrossbowBoltTypes
{
	Wooden(0, ModItems.woodenBolt),
	Iron(1, ModItems.ironBolt),
	Silver(2, ModItems.silverBolt),
	Igneous(3, ModItems.igneousBolt),
	StarMetal(4, ModItems.starMetalBolt);

	private int id = 0;
	private Item myBoltItem;

	private AOTDCrossbowBoltTypes(int id, Item myBoltItem)
	{
		this.id = id;
		this.myBoltItem = myBoltItem;
	}

	public static int getIDFromType(AOTDCrossbowBoltTypes crossbowType)
	{
		return crossbowType.id;
	}

	public static AOTDCrossbowBoltTypes getTypeFromID(int id)
	{
		for (AOTDCrossbowBoltTypes crossbowTypes : AOTDCrossbowBoltTypes.values())
		{
			if (crossbowTypes.id == id)
			{
				return crossbowTypes;
			}
		}
		return null;
	}

	public String formattedString()
	{
		String toReturn = "";

		for (final String string : this.toString().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"))
		{
			toReturn = toReturn + string + " ";
		}

		return toReturn;
	}

	public Item getMyBoltItem()
	{
		return this.myBoltItem;
	}

	public AOTDCrossbowBoltTypes next()
	{
		if (this == AOTDCrossbowBoltTypes.values()[AOTDCrossbowBoltTypes.values().length - 1])
		{
			return AOTDCrossbowBoltTypes.values()[0];
		}
		else
		{
			return getTypeFromID(this.id + 1);
		}
	}

	public EntityBolt createBolt(World world, EntityPlayer entityPlayer)
	{
		EntityBolt toReturn = null;

		switch (this)
		{
			case Igneous:
				toReturn = new EntityIgneousBolt(world, entityPlayer);
				break;
			case Iron:
				toReturn = new EntityIronBolt(world, entityPlayer);
				break;
			case Silver:
				toReturn = new EntitySilverBolt(world, entityPlayer);
				break;
			case StarMetal:
				toReturn = new EntityStarMetalBolt(world, entityPlayer);
				break;
			case Wooden:
				toReturn = new EntityWoodenBolt(world, entityPlayer);
				break;
			default:
				break;
		}

		return toReturn;
	}
}
