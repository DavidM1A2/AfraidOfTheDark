package com.DavidM1A2.AfraidOfTheDark.playerData;

import io.netty.handler.codec.bytes.ByteArrayEncoder;

import org.apache.http.entity.ByteArrayEntity;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.research.Research;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;
import com.sun.xml.internal.ws.message.ByteArrayAttachment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class LoadResearchData implements IExtendedEntityProperties
{
	private byte[] unlockedResearches;
	private final static String RESEARCH_DATA = "unlockedResearches";

	@Override
	public void saveNBTData(NBTTagCompound compound) 
	{
		unlockedResearches = Refrence.myResearch.unlockedResearches();
		for (int i = 0; i < unlockedResearches.length; i++)
		{
			compound.setByteArray(RESEARCH_DATA, unlockedResearches);
		}
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) 
	{
		unlockedResearches = compound.getByteArray(RESEARCH_DATA);
		for (int i = 0; i < unlockedResearches.length; i++)
		{
			if (unlockedResearches[i] == 1)
			{
				Refrence.myResearch.unlockResearch(i);
			}
		}
	}

	@Override
	public void init(Entity entity, World world)
	{
		loadNBTData(entity.getEntityData());
	}
	
	public static byte[] get(EntityPlayer myPlayer)
	{
		return myPlayer.getEntityData().getByteArray(RESEARCH_DATA);
	}
	public static void set(EntityPlayer myPlayer, byte[] unlockedResearch)
	{
		myPlayer.getEntityData().setByteArray(RESEARCH_DATA, unlockedResearch);
	}
}
