/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.playerData;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateVitae;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.relauncher.Side;

//This property is saved on ENTITIES and keeps track of their vitae levels
public class Vitae implements IExtendedEntityProperties
{
	private int vitae = 0;
	public final static String VITAE_LEVEL = "vitaeLevel";

	public static final void register(final EntityLivingBase entityLivingBase)
	{
		if (!(entityLivingBase instanceof EntityArmorStand))
		{
			if (entityLivingBase.getExtendedProperties(Vitae.VITAE_LEVEL) == null)
			{
				entityLivingBase.registerExtendedProperties(Vitae.VITAE_LEVEL, new Vitae());
			}
		}
	}

	@Override
	public void init(Entity entity, World world)
	{
	}

	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		compound.setInteger(Vitae.VITAE_LEVEL, this.vitae);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.vitae = compound.getInteger(Vitae.VITAE_LEVEL);
	}

	// Getters and setters for if a player has begun AOTD
	public static int get(final EntityLivingBase entityLivingBase)
	{
		return entityLivingBase.getEntityData().getInteger(Vitae.VITAE_LEVEL);
	}

	public static void set(final EntityLivingBase entityLivingBase, final int vitaeValue, final Side side)
	{
		entityLivingBase.getEntityData().setInteger(Vitae.VITAE_LEVEL, vitaeValue);
		if (side == Side.CLIENT)
		{
			AfraidOfTheDark.getPacketHandler().sendToServer(new UpdateVitae(Vitae.get(entityLivingBase), entityLivingBase.getEntityId()));
		}
		else if (side == Side.SERVER)
		{
			AfraidOfTheDark.getPacketHandler().sendToAll(new UpdateVitae(Vitae.get(entityLivingBase), entityLivingBase.getEntityId()));
		}
	}

	public static void addVitae(final EntityLivingBase entityLivingBase, final int additionalVitae, final Side side)
	{
		if (Constants.entityVitaeResistance.containsKey(entityLivingBase.getClass()))
		{
			if (Vitae.get(entityLivingBase) + additionalVitae > Constants.entityVitaeResistance.get(entityLivingBase.getClass()) && !(entityLivingBase instanceof EntityPlayer && ((EntityPlayer) entityLivingBase).capabilities.isCreativeMode))
			{
				entityLivingBase.worldObj.createExplosion(entityLivingBase, entityLivingBase.getPosition().getX(), entityLivingBase.getPosition().getY(), entityLivingBase.getPosition().getZ(), 2, true).doExplosionB(true);
				entityLivingBase.killCommand();
			}
			else
			{
				if (Vitae.get(entityLivingBase) + additionalVitae < 0)
				{
					entityLivingBase.getEntityData().setInteger(Vitae.VITAE_LEVEL, 0);
				}
				else
				{
					entityLivingBase.getEntityData().setInteger(Vitae.VITAE_LEVEL, Vitae.get(entityLivingBase) + additionalVitae);
				}

				if (side == Side.CLIENT)
				{
					AfraidOfTheDark.getPacketHandler().sendToServer(new UpdateVitae(Vitae.get(entityLivingBase), entityLivingBase.getEntityId()));
				}
				else if (side == Side.SERVER)
				{
					AfraidOfTheDark.getPacketHandler().sendToAll(new UpdateVitae(Vitae.get(entityLivingBase), entityLivingBase.getEntityId()));
				}

				if (entityLivingBase instanceof EntityDeeeSyft)
				{
					((EntityDeeeSyft) entityLivingBase).setFlightCeiling(85 + (int) ((double) Vitae.get(entityLivingBase) / (double) Constants.entityVitaeResistance.get(EntityDeeeSyft.class) * 150.0D));
				}
			}
		}
		else
		{
			LogHelper.warn(entityLivingBase.getClass().getSimpleName() + " is not registered in the vitae dictionary and therefore cannot receive vitae.");
		}

	}
}
