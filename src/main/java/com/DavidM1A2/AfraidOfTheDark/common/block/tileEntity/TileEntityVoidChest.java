/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDTileEntity;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityVoidChest extends AOTDTileEntity implements IUpdatePlayerListBox
{
	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;
	/** The angle of the lid last tick */
	public float prevLidAngle;

	private boolean shouldBeOpen = false;

	/** Server sync counter (once per 20 ticks) */
	private int ticksSinceSync;
	private int cachedChestType;

	private EntityPlayer entityPlayerToSend = null;

	private long lastInteraction = -1;

	public TileEntityVoidChest()
	{
		super(ModBlocks.voidChest);
	}

	@Override
	public void update()
	{
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		this.ticksSinceSync = this.ticksSinceSync + 1;;
		float f;

		if (ticksSinceSync % 20 == 0)
		{
			if ((System.currentTimeMillis() - this.lastInteraction) > 3000)
			{
				this.shouldBeOpen = false;
			}
		}

		this.prevLidAngle = this.lidAngle;
		f = 0.1F;
		double d2;

		// Opening chest
		if (shouldBeOpen && this.lidAngle == 0.0F)
		{
			double d1 = i + 0.5D;
			d2 = k + 0.5D;

			this.worldObj.playSoundEffect(d1, j + 0.5D, d2, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		// Closing chest
		if (!shouldBeOpen && this.lidAngle > 0.0F || shouldBeOpen && this.lidAngle < 1.0F)
		{
			float f1 = this.lidAngle;

			if (shouldBeOpen)
			{
				this.lidAngle += f;
			}
			else
			{
				this.lidAngle -= f;
			}

			if (this.lidAngle > 1.0F)
			{
				this.lidAngle = 1.0F;
			}

			float f2 = 0.5F;

			if (this.lidAngle < f2 && f1 >= f2)
			{
				d2 = i + 0.5D;
				double d0 = k + 0.5D;

				this.worldObj.playSoundEffect(d2, j + 0.5D, d0, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);

				if (this.worldObj.provider.getDimensionId() != Constants.VoidChestWorld.VOID_CHEST_WORLD_ID)
				{
					for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(this.pos, this.pos.add(.625D, .625D, .625D)).expand(2.0D, 2.0D, 2.0D)))
					{
						EntityPlayerMP entityPlayerMP = (EntityPlayerMP) object;
						if (entityPlayerMP == entityPlayerToSend)
						{
							Utility.sendPlayerToDimension(entityPlayerMP, Constants.VoidChestWorld.VOID_CHEST_WORLD_ID, false, VoidChestTeleporter.class);
						}
					}
				}
				else
				{
					for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(this.pos, this.pos.add(.625D, .625D, .625D)).expand(2.0D, 2.0D, 2.0D)))
					{
						EntityPlayerMP entityPlayerMP = (EntityPlayerMP) object;
						if (entityPlayerMP == entityPlayerToSend)
						{
							Utility.sendPlayerToDimension(entityPlayerMP, 0, false, VoidChestTeleporter.class);
						}
					}
				}
			}

			if (this.lidAngle < 0.0F)
			{
				this.lidAngle = 0.0F;
			}
		}
	}

	public void interact(EntityPlayer entityPlayer)
	{
		this.lastInteraction = System.currentTimeMillis();
		this.shouldBeOpen = true;
		this.entityPlayerToSend = entityPlayer;
	}
}
