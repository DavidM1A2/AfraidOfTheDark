package com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class VoidChestTeleporter extends Teleporter
{
	private final WorldServer worldServerInstance;
	private final int dimensionOld;
	private final int dimensionNew;

	public VoidChestTeleporter(WorldServer worldIn, int dimensionOld, int dimensionNew)
	{
		super(worldIn);
		this.worldServerInstance = worldIn;
		this.dimensionOld = dimensionOld;
		this.dimensionNew = dimensionNew;
	}

	@Override
	public void func_180266_a(Entity entity, float entityYaw)
	{
		if (dimensionNew == Constants.VoidChestWorld.VOID_CHEST_WORLD_ID)
		{
			int i = MathHelper.floor_double(entity.posX);
			int j = MathHelper.floor_double(entity.posY) - 1;
			int k = MathHelper.floor_double(entity.posZ);
			entity.setLocationAndAngles((double) i, (double) j, (double) k, entity.rotationYaw, 0.0F);
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;
		}
		else if (dimensionOld == Constants.VoidChestWorld.VOID_CHEST_WORLD_ID)
		{
			if (entity instanceof EntityPlayer)
			{
				//				EntityPlayer entityPlayer = (EntityPlayer) entity;
				//				BlockPos playerPostionOld = InventorySaver.getPlayerLocationOverworld(entityPlayer);
				//				entityPlayer.setPosition(playerPostionOld.getX(), playerPostionOld.getY(), playerPostionOld.getZ());
				entity.motionX = 0.0D;
				entity.motionY = 0.0D;
				entity.motionZ = 0.0D;
			}
		}
	}
}