/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.playerData;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

//This property is saved on a player and keeps track of their current insanity
public class Insanity implements IExtendedEntityProperties
{
	private double playerInsanity;
	public final static String PLAYER_INSANITY = "PlayerInsanity";

	public static final void register(final EntityPlayer entityPlayer)
	{
		/*
		 * This third block of code will determine if the player has insanity yet, and if he/she does we will load it.
		 */
		if (entityPlayer.getExtendedProperties(Insanity.PLAYER_INSANITY) == null)
		{
			entityPlayer.registerExtendedProperties(Insanity.PLAYER_INSANITY, new Insanity());
		}
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
			final NBTTagCompound compound = myPlayer.getEntityData();
			if (amount > 0)
			{
				compound.setDouble(Insanity.PLAYER_INSANITY, Math.min(100.0, Insanity.get(myPlayer) + amount));
			}
			else
			{
				compound.setDouble(Insanity.PLAYER_INSANITY, Math.max(0, Insanity.get(myPlayer) + amount));
			}
			AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateInsanity(Insanity.get(myPlayer)), (EntityPlayerMP) myPlayer);
		}
	}
}
