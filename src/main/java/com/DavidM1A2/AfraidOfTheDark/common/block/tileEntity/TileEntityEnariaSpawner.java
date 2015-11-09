package com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity;

import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDTileEntity;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityEnariaSpawner extends AOTDTileEntity
{
	private int ticksExisted = 0;
	private UUID enariaEntityID = null;
	private static final int TICKS_INBETWEEN_CHECKS = 100;
	private static final int CHECK_RANGE = 30;
	
	
	public TileEntityEnariaSpawner() 
	{
		super(ModBlocks.enariaSpawner);
	}
	
	@Override
	public void update() 
	{
		if (!worldObj.isRemote)
		{
			if (ticksExisted % TICKS_INBETWEEN_CHECKS == 0)
			{
				if (enariaEntityID == null)
				{
					for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.fromBounds(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 1, this.pos.getZ() + 1).expand(CHECK_RANGE, CHECK_RANGE, CHECK_RANGE)))
					{
						if (object instanceof EntityPlayer)
						{
							EntityPlayer entityPlayer = (EntityPlayer) object;
							if (!AOTDPlayerData.get(entityPlayer).getHasBeatenEnaria())
							{
								this.summonEnaria();
								return;
							}
						}
					}
				}
				else
				{
					Entity entity = MinecraftServer.getServer().getEntityFromUuid(enariaEntityID);
					
					if (entity == null)
					{
						this.enariaEntityID = null;
					}
				}
			}
			ticksExisted = ticksExisted + 1;
		}
	}
	
	private void summonEnaria()
	{
		EntityEnaria enaria = new EntityEnaria(this.worldObj);
		enaria.getEntityData().setBoolean(EntityEnaria.IS_VALID, true);
		enaria.setPosition(this.pos.getX(), this.pos.getY() + 10, this.pos.getZ());
		this.worldObj.spawnEntityInWorld(enaria);
		this.enariaEntityID = enaria.getPersistentID();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) 
	{
        compound.setLong("enariaEntityIDMost", this.enariaEntityID.getMostSignificantBits());
        compound.setLong("enariaEntityIDLeast", this.enariaEntityID.getLeastSignificantBits());
		super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		this.enariaEntityID = new UUID(compound.getLong("enariaEntityIDMost"), compound.getLong("enariaEntityIDLeast"));
		super.readFromNBT(compound);
	}
}
