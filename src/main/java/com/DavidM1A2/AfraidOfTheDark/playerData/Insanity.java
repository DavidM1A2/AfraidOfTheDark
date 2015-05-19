/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateInsanity;

//This property is saved on a player and keeps track of their current insanity
public class Insanity implements IExtendedEntityProperties
{
	private double playerInsanity;
	private final static String PLAYER_INSANITY = "PlayerInsanity";

	// Getter for insanity
	public static double get(EntityPlayer myPlayer)
	{
		return myPlayer.getEntityData().getDouble(PLAYER_INSANITY);
	}

	// init is for new players that don't have this IExtendedProperty yet
	@Override
	public void init(Entity entity, World world)
	{
		if (entity.getEntityData().hasKey(PLAYER_INSANITY))
		{
			loadNBTData(entity.getEntityData());
		}
		else
		{
			entity.getEntityData().setDouble(PLAYER_INSANITY, 0.0D);
			loadNBTData(entity.getEntityData());
		}
	}

	// Returns true if we are at max insanity
	public static void addInsanity(double amount, EntityPlayer myPlayer)
	{
		// if the player has begun the mod we can change his insanity
		if (HasStartedAOTD.get(myPlayer))
		{
			if (amount > 0)
			{
				NBTTagCompound compound = myPlayer.getEntityData();
				compound.setDouble(PLAYER_INSANITY, Math.min(100.0, get(myPlayer) + amount));
				updateClientSideInsanity(myPlayer);
			}
			else
			{
				NBTTagCompound compound = myPlayer.getEntityData();
				compound.setDouble(PLAYER_INSANITY, Math.max(0, get(myPlayer) + amount));
				updateClientSideInsanity(myPlayer);
			}
		}
	}

	// Save NBTData saves this piece of data to the player
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		compound.setDouble(PLAYER_INSANITY, this.playerInsanity);
	}

	// load loads the data
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.playerInsanity = compound.getDouble(PLAYER_INSANITY);
	}

	// This sends an update packet to a client (sync packet)
	public static void updateClientSideInsanity(EntityPlayer myPlayer)
	{
		AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateInsanity(get(myPlayer)), (EntityPlayerMP) myPlayer);
	}
}
