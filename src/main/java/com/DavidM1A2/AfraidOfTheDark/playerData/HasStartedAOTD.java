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
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateAOTDStatus;

// This property is saved on a player and keeps track of if they have begun the mod
public class HasStartedAOTD implements IExtendedEntityProperties
{
	private boolean hasStartedAOTD = false;
	public final static String PLAYER_STARTED_AOTD = "playerStartedAOTD";

	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(HasStartedAOTD.PLAYER_STARTED_AOTD, new HasStartedAOTD());
	}

	@Override
	public void init(Entity entity, World world)
	{
	}

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

	// Getters and setters for if a player has begun AOTD
	public static boolean get(EntityPlayer entityPlayer)
	{
		return entityPlayer.getEntityData().getBoolean(PLAYER_STARTED_AOTD);
	}

	public static void set(EntityPlayer entityPlayer, boolean value, Side side)
	{
		entityPlayer.getEntityData().setBoolean(PLAYER_STARTED_AOTD, value);
		if (side == Side.CLIENT)
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new UpdateAOTDStatus(get(entityPlayer)));
		}
		else
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateAOTDStatus(get(entityPlayer)), (EntityPlayerMP) entityPlayer);
		}
	}
}
