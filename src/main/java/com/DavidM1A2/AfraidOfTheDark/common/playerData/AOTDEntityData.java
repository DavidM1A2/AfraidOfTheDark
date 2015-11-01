
package com.DavidM1A2.AfraidOfTheDark.common.playerData;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAOTDEntityData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateVitae;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class AOTDEntityData implements IExtendedEntityProperties
{
	// PROPERTIES =============================================================

	private final Entity entity;

	private int vitae = 0;
	private final static String VITAE_LEVEL = "vitaeLevel";

	// CONSTRUCTOR, GETTER, REGISTER ==========================================

	public AOTDEntityData(Entity entity)
	{
		this.entity = entity;
	}

	private static String getIdentifier()
	{
		return "AOTDEntityData";
	}

	public static AOTDEntityData get(Entity entity)
	{
		return (AOTDEntityData) entity.getExtendedProperties(AOTDEntityData.getIdentifier());
	}

	public static void register(Entity entity)
	{
		if (entity.getExtendedProperties(AOTDEntityData.getIdentifier()) == null)
		{
			entity.registerExtendedProperties(AOTDEntityData.getIdentifier(), new AOTDEntityData(entity));
		}
	}

	// LOAD, SAVE =============================================================

	@Override
	public void saveNBTData(NBTTagCompound nbt)
	{
		nbt.setInteger(VITAE_LEVEL, vitae);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt)
	{
		if (nbt.hasKey(VITAE_LEVEL))
		{
			this.setVitaeLevel(nbt.getInteger(VITAE_LEVEL));
		}
	}

	@Override
	public void init(Entity entity, World world)
	{
	}

	public boolean isServerSide()
	{
		return this.entity != null && this.entity.worldObj != null && !this.entity.worldObj.isRemote;
	}

	// GETTER, SETTER, SYNCER =================================================

	public int getVitaeLevel()
	{
		return this.vitae;
	}

	public boolean setVitaeLevel(int vitaeLevel)
	{
		boolean boom = vitaeLevel > Constants.entityVitaeResistance.get(entity.getClass());
		if (Constants.entityVitaeResistance.containsKey(entity.getClass()))
		{
			this.vitae = MathHelper.clamp_int(vitaeLevel, 0, Integer.MAX_VALUE);
		}
		else
		{
			LogHelper.warn(entity.getClass().getSimpleName() + " is not registered in the vitae dictionary and therefore cannot receive vitae.");
		}
		return boom;
	}

	public void syncVitaeLevel()
	{
		if (this.isServerSide())
		{
			AfraidOfTheDark.getPacketHandler().sendToAll(new UpdateVitae(this.getVitaeLevel(), entity.getEntityId()));
		}
		else
		{
			AfraidOfTheDark.getPacketHandler().sendToServer(new UpdateVitae(this.getVitaeLevel(), entity.getEntityId()));
		}
	}

	public void syncAll()
	{
		if (this.isServerSide())
		{
			if (entity instanceof EntityPlayer)
			{
				AfraidOfTheDark.getPacketHandler().sendTo(new SyncAOTDEntityData(this), (EntityPlayerMP) this.entity);
			}
		}
	}

	public void requestSyncAll()
	{
		if (!this.isServerSide())
		{
			AfraidOfTheDark.getPacketHandler().sendToServer(new SyncAOTDPlayerData());
		}
	}
}
