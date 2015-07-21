/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.playerData;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.relauncher.Side;

// This property is saved on a player and keeps track of if they have begun the mod
public class HasStartedAOTD implements IExtendedEntityProperties
{
	private boolean hasStartedAOTD = false;
	public final static String PLAYER_STARTED_AOTD = "playerStartedAOTD";

	public static final void register(final EntityPlayer entityPlayer)
	{
		/*
		 * This second block of code will check if the player has begun the mod.
		 */
		if (entityPlayer.getExtendedProperties(HasStartedAOTD.PLAYER_STARTED_AOTD) == null)
		{
			entityPlayer.registerExtendedProperties(HasStartedAOTD.PLAYER_STARTED_AOTD, new HasStartedAOTD());
		}
	}

	@Override
	public void init(final Entity entity, final World world)
	{
	}

	// saveNBTData is called whenever data needs to be saved
	@Override
	public void saveNBTData(final NBTTagCompound compound)
	{
		compound.setBoolean(HasStartedAOTD.PLAYER_STARTED_AOTD, this.hasStartedAOTD);
	}

	// load works the same way
	@Override
	public void loadNBTData(final NBTTagCompound compound)
	{
		this.hasStartedAOTD = compound.getBoolean(HasStartedAOTD.PLAYER_STARTED_AOTD);
	}

	// Getters and setters for if a player has begun AOTD
	public static boolean get(final EntityPlayer entityPlayer)
	{
		return entityPlayer.getEntityData().getBoolean(HasStartedAOTD.PLAYER_STARTED_AOTD);
	}

	public static void set(final EntityPlayer entityPlayer, final boolean value, final Side side)
	{
		entityPlayer.getEntityData().setBoolean(HasStartedAOTD.PLAYER_STARTED_AOTD, value);
		if (side == Side.CLIENT)
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new UpdateAOTDStatus(HasStartedAOTD.get(entityPlayer)));
		}
		else
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateAOTDStatus(HasStartedAOTD.get(entityPlayer)), (EntityPlayerMP) entityPlayer);
		}
	}
}
