package com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity;

import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDTickingTileEntity;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;

public class TileEntityEnariaSpawner extends AOTDTickingTileEntity
{
	private UUID enariaEntityID = null;
	private static final int TICKS_INBETWEEN_CHECKS = 40;
	private AxisAlignedBB playerCheckRegion = null;

	public TileEntityEnariaSpawner()
	{
		super(ModBlocks.enariaSpawner);
	}

	@Override
	public void update()
	{
		super.update();
		if (!worldObj.isRemote)
		{
			if (this.ticksExisted % TICKS_INBETWEEN_CHECKS == 0)
			{
				if (worldObj.getDifficulty() != EnumDifficulty.PEACEFUL)
				{
					if (enariaEntityID == null)
					{
						if (playerCheckRegion == null)
						{
							playerCheckRegion = AxisAlignedBB.fromBounds(this.pos.getX() - 11, this.pos.getY() - 2, this.pos.getZ() - 2, this.pos.getX() + 11, this.pos.getY() + 11, this.pos.getZ() + 20);
						}
						for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, playerCheckRegion))
						{
							if (object instanceof EntityPlayer)
							{
								EntityPlayer entityPlayer = (EntityPlayer) object;
								if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.Enaria))
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
			}
		}
	}

	private void summonEnaria()
	{
		EntityEnaria enaria = new EntityEnaria(this.worldObj);
		enaria.getEntityData().setBoolean(EntityEnaria.IS_VALID, true);
		enaria.setPosition(this.pos.getX() + 0.5, this.pos.getY() + 7, this.pos.getZ() + 0.5);
		this.worldObj.spawnEntityInWorld(enaria);
		this.enariaEntityID = enaria.getPersistentID();
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		if (this.enariaEntityID != null)
		{
			compound.setLong("enariaEntityIDMost", this.enariaEntityID.getMostSignificantBits());
			compound.setLong("enariaEntityIDLeast", this.enariaEntityID.getLeastSignificantBits());
		}
		super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("enariaEntityIDMost") && compound.hasKey("enariaEntityIDLeast"))
		{
			this.enariaEntityID = new UUID(compound.getLong("enariaEntityIDMost"), compound.getLong("enariaEntityIDLeast"));
		}
		super.readFromNBT(compound);
	}
}
