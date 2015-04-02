package com.DavidM1A2.AfraidOfTheDark.playerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class HasStartedAOTD implements IExtendedEntityProperties  
{
	private boolean hasStartedAOTD = false;
	private final static String PLAYER_STARTED_AOTD = "playerStartedAOTD";
	
	@Override
	public void saveNBTData(NBTTagCompound compound) 
	{
		compound.setBoolean(PLAYER_STARTED_AOTD, hasStartedAOTD);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) 
	{
		this.hasStartedAOTD = compound.getBoolean(PLAYER_STARTED_AOTD);
	}

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
	
	public static boolean get(EntityPlayer entityPlayer)
	{
		return entityPlayer.getEntityData().getBoolean(PLAYER_STARTED_AOTD);
	}	
	public static void set(EntityPlayer entityPlayer, boolean value)
	{
		entityPlayer.getEntityData().setBoolean(PLAYER_STARTED_AOTD, value);
	}
}
