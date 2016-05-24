/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.EntityAOTDPainting;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDArt;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityArtwork extends EntityHanging implements IEntityAdditionalSpawnData
{
	private AOTDArt art;

	public EntityArtwork(World world)
	{
		super(world);
	}

	public EntityArtwork(World world, BlockPos blockPos, EnumFacing facing, AOTDArt artwork)
	{
		super(world, blockPos);
		this.art = artwork;
		this.updateFacingWithBoundingBox(facing);
	}

	@Override
	public int getWidthPixels()
	{
		return this.art.getWidthPixels();
	}

	@Override
	public int getHeightPixels()
	{
		return this.art.getHeightPixels();
	}

	public AOTDArt getArt()
	{
		return this.art;
	}

	/**
	 * Called when this entity is broken. Entity parameter may be null.
	 */
	@Override
	public void onBroken(Entity brokenEntity)
	{
		if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
		{
			if (brokenEntity instanceof EntityPlayer)
				if (((EntityPlayer) brokenEntity).capabilities.isCreativeMode)
					return;

			this.entityDropItem(new ItemStack(ModItems.artwork), 0.0F);
		}
	}

	/**
	 * Sets the location and Yaw/Pitch of an entity in the world
	 */
	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
	{
		BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
		this.setPosition((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
	{
		BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
		this.setPosition((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setString("artID", this.art.getName());
		super.writeEntityToNBT(compound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		this.art = AOTDArt.fromName(compound.getString("artID"));
		super.readEntityFromNBT(compound);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		ByteBufUtils.writeUTF8String(buffer, this.art.getName());
		buffer.writeDouble(this.getHangingPosition().getX());
		buffer.writeDouble(this.getHangingPosition().getY());
		buffer.writeDouble(this.getHangingPosition().getZ());
		ByteBufUtils.writeUTF8String(buffer, this.facingDirection.getName());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.art = AOTDArt.fromName(ByteBufUtils.readUTF8String(additionalData));
		this.hangingPosition = new BlockPos(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());
		this.facingDirection = EnumFacing.byName(ByteBufUtils.readUTF8String(additionalData));
		this.updateFacingWithBoundingBox(facingDirection);
	}

	/**
	 * checks to make sure painting can be placed there
	 */
	@Override
	public boolean onValidSurface()
	{
		if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty())
		{
			return false;
		}
		else
		{
			int i = Math.max(1, this.getWidthPixels() / 16);
			int j = Math.max(1, this.getHeightPixels() / 16);
			BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
			EnumFacing enumfacing = this.facingDirection.rotateYCCW();

			for (int k = -i / 2 + 1; k < i / 2 + 1; ++k)
			{
				for (int l = -j / 2 + 1; l < j / 2 + 1; ++l)
				{
					BlockPos blockpos1 = blockpos.offset(enumfacing, k).up(l);
					Block block = this.worldObj.getBlockState(blockpos1).getBlock();

					if (block.isSideSolid(this.worldObj, blockpos1, this.facingDirection))
						continue;

					if (!block.getMaterial().isSolid() && !BlockRedstoneDiode.isRedstoneRepeaterBlockID(block))
					{
						return false;
					}
				}
			}

			for (Entity entity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()))
			{
				if (entity instanceof EntityHanging)
				{
					return false;
				}
			}

			return true;
		}
	}
}