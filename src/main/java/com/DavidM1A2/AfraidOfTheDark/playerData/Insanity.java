package com.DavidM1A2.AfraidOfTheDark.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateInsanity;

public class Insanity implements IExtendedEntityProperties
{
	private double playerInsanity;
	private final static String PLAYER_INSANITY = "PlayerInsanity";

	public static double get(EntityPlayer myPlayer)
	{
		return myPlayer.getEntityData().getDouble(PLAYER_INSANITY);
	}

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

	// Returns true if we are at max
	public static boolean increaseInsanity(double amount, EntityPlayer myPlayer)
	{
		if (HasStartedAOTD.get(myPlayer))
		{
			NBTTagCompound compound = myPlayer.getEntityData();
			if ((get(myPlayer) + amount) > 100.0)
			{
				compound.setDouble(PLAYER_INSANITY, 100.0);
				updateClientSideInsanity(myPlayer);
				return true;
			}
			else
			{
				compound.setDouble(PLAYER_INSANITY, get(myPlayer) + amount);
				updateClientSideInsanity(myPlayer);
				return false;
			}
		}
		return false;
	}

	// Returns true if we are at the minimum
	public static boolean decreaseInsanity(double amount, EntityPlayer myPlayer)
	{
		if (HasStartedAOTD.get(myPlayer))
		{
			NBTTagCompound compound = myPlayer.getEntityData();
			if ((get(myPlayer) - amount) < 0.0)
			{
				compound.setDouble(PLAYER_INSANITY, 0.0);
				updateClientSideInsanity(myPlayer);
				return true;
			}
			else
			{
				compound.setDouble(PLAYER_INSANITY, get(myPlayer) - amount);
				updateClientSideInsanity(myPlayer);
				return false;
			}
		}
		return true;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		compound.setDouble(PLAYER_INSANITY, this.playerInsanity);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.playerInsanity = compound.getDouble(PLAYER_INSANITY);
	}

	public static void updateClientSideInsanity(EntityPlayer myPlayer)
	{
		AfraidOfTheDark.channelNew.sendTo(new UpdateInsanity(get(myPlayer)), (EntityPlayerMP) myPlayer);
	}
}
