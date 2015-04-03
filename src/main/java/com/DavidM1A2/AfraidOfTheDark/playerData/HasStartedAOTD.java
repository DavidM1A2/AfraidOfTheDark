/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

// This property is saved on a player and keeps track of if they have begun the mod
public class HasStartedAOTD implements IExtendedEntityProperties
{
	private boolean hasStartedAOTD = false;
	private final static String PLAYER_STARTED_AOTD = "playerStartedAOTD";

	// saveNBTData is called whenever data needs to be saved
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		compound.setBoolean(PLAYER_STARTED_AOTD, hasStartedAOTD);
	}

	// load works the same way
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.hasStartedAOTD = compound.getBoolean(PLAYER_STARTED_AOTD);
	}

	// init is for new players that don't have this IExtendedProperty yet
	@Override
	public void init(Entity entity, World world)
	{
		if (entity.getEntityData().hasKey(PLAYER_STARTED_AOTD))
		{
			loadNBTData(entity.getEntityData());
		}
		else
		{
			entity.getEntityData().setBoolean(PLAYER_STARTED_AOTD, false);
			loadNBTData(entity.getEntityData());
		}
	}

	// Getters and setters for if a player has begun AOTD
	public static boolean get(EntityPlayer entityPlayer)
	{
		return entityPlayer.getEntityData().getBoolean(PLAYER_STARTED_AOTD);
	}

	public static void set(EntityPlayer entityPlayer, boolean value)
	{
		entityPlayer.getEntityData().setBoolean(PLAYER_STARTED_AOTD, value);
	}
}
