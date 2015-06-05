/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;

//This property is saved on a player and keeps track of their current insanity
public class Insanity implements IExtendedEntityProperties
{
	private double playerInsanity;
	public final static String PLAYER_INSANITY = "PlayerInsanity";

	public static final void register(final EntityPlayer player)
	{
		player.registerExtendedProperties(Insanity.PLAYER_INSANITY, new Insanity());
	}

	@Override
	public void init(final Entity entity, final World world)
	{
	}

	// Save NBTData saves this piece of data to the player
	@Override
	public void saveNBTData(final NBTTagCompound compound)
	{
		compound.setDouble(Insanity.PLAYER_INSANITY, this.playerInsanity);
	}

	// load loads the data
	@Override
	public void loadNBTData(final NBTTagCompound compound)
	{
		this.playerInsanity = compound.getDouble(Insanity.PLAYER_INSANITY);
	}

	// Getter for insanity
	public static double get(final EntityPlayer myPlayer)
	{
		return myPlayer.getEntityData().getDouble(Insanity.PLAYER_INSANITY);
	}

	// Returns true if we are at max insanity
	public static void addInsanity(final double amount, final EntityPlayer myPlayer)
	{
		// if the player has begun the mod we can change his insanity
		if (HasStartedAOTD.get(myPlayer))
		{
			if (amount > 0)
			{
				final NBTTagCompound compound = myPlayer.getEntityData();
				compound.setDouble(Insanity.PLAYER_INSANITY, Math.min(100.0, Insanity.get(myPlayer) + amount));
				Insanity.updateClientSideInsanity(myPlayer);
			}
			else
			{
				final NBTTagCompound compound = myPlayer.getEntityData();
				compound.setDouble(Insanity.PLAYER_INSANITY, Math.max(0, Insanity.get(myPlayer) + amount));
				Insanity.updateClientSideInsanity(myPlayer);
			}
		}
	}

	// This sends an update packet to a client (sync packet)
	public static void updateClientSideInsanity(final EntityPlayer myPlayer)
	{
		AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateInsanity(Insanity.get(myPlayer)), (EntityPlayerMP) myPlayer);
	}
}
