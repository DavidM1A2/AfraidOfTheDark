/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.savedData.entityData;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAOTDEntityData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateVitae;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class AOTDEntityData implements ICapabilitySerializable<NBTTagCompound>, IAOTDEntityData
{
	private final Entity entity;

	private int vitae = 0;

	public AOTDEntityData(Entity entity)
	{
		this.entity = entity;
	}

	public boolean isServerSide()
	{
		return this.entity != null && this.entity.world != null && !this.entity.world.isRemote;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return ModCapabilities.ENTITY_DATA != null && capability == ModCapabilities.ENTITY_DATA;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (ModCapabilities.ENTITY_DATA != null && capability == ModCapabilities.ENTITY_DATA)
		{
			return (T) this;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return (NBTTagCompound) ModCapabilities.ENTITY_DATA.getStorage().writeNBT(ModCapabilities.ENTITY_DATA, this, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound compound)
	{
		ModCapabilities.ENTITY_DATA.getStorage().readNBT(ModCapabilities.ENTITY_DATA, this, null, compound);
	}

	public int getVitaeLevel()
	{
		return this.vitae;
	}

	public boolean setVitaeLevel(int vitaeLevel)
	{
		boolean boom = false;
		if (Constants.entityVitaeResistance.containsKey(entity.getClass()))
		{
			boom = vitaeLevel > Constants.entityVitaeResistance.get(entity.getClass());
			this.vitae = MathHelper.clamp(vitaeLevel, 0, Integer.MAX_VALUE);
		}
		else
		{
			if (ConfigurationHandler.debugMessages)
			{
				LogHelper.warn(entity.getClass().getSimpleName() + " is not registered in the vitae dictionary and therefore cannot receive vitae.");
			}
		}
		return boom;
	}

	public void syncVitaeLevel()
	{
		if (this.isServerSide())
		{
			AfraidOfTheDark.INSTANCE.getPacketHandler().sendToAll(new UpdateVitae(this.getVitaeLevel(), entity));
		}
		else
		{
			AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new UpdateVitae(this.getVitaeLevel(), entity));
		}
	}

	public void syncAll()
	{
		if (this.isServerSide())
		{
			if (entity instanceof EntityPlayer)
			{
				AfraidOfTheDark.INSTANCE.getPacketHandler().sendTo(new SyncAOTDEntityData(this), (EntityPlayerMP) this.entity);
			}
		}
	}

	public void requestSyncAll()
	{
		if (!this.isServerSide())
		{
			AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new SyncAOTDPlayerData());
		}
	}
}
