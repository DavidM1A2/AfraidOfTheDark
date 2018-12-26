/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.EntityArtwork;

import org.apache.commons.lang3.Validate;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDArt;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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

	public int blocksToTakeUp()
	{
		return this.art.getBlockScale();
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

			this.entityDropItem(new ItemStack(ModItems.artwork, 1, this.getArt().ordinal()), 0.0F);
		}
	}

	/**
	 * Sets the location and Yaw/Pitch of an entity in the world
	 */
	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
	{
		this.setPosition(x, y, z);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
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
		if (!this.worldObj.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty())
		{
			return false;
		}
		else
		{
			int i = Math.max(1, this.blocksToTakeUp());
			int j = Math.max(1, this.blocksToTakeUp());
			BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
			EnumFacing enumfacing = this.facingDirection.rotateYCCW();

			for (int k = -i / 2 + 1; k < i / 2 + 1; ++k)
			{
				for (int l = -j / 2 + 1; l < j / 2 + 1; ++l)
				{
					BlockPos blockpos1 = blockpos.offset(enumfacing, k).up(l);
					IBlockState block = this.worldObj.getBlockState(blockpos1);

					if (block.isSideSolid(this.worldObj, blockpos1, this.facingDirection))
						continue;

					if (!block.getMaterial().isSolid() && !BlockRedstoneDiode.isDiode(block))
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

	/**
	 * Updates facing and bounding box based on it
	 */
	@Override
	protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn)
	{
		Validate.notNull(facingDirectionIn);
		Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
		this.facingDirection = facingDirectionIn;
		this.prevRotationYaw = this.rotationYaw = (float) (this.facingDirection.getHorizontalIndex() * 90);
		this.updateBoundingBox();
	}

	/**
	 * Updates the entity bounding box based on current facing
	 */
	@Override
	protected void updateBoundingBox()
	{
		if (this.facingDirection != null)
		{
			double d0 = (double) this.hangingPosition.getX() + 0.5D;
			double d1 = (double) this.hangingPosition.getY() + 0.5D;
			double d2 = (double) this.hangingPosition.getZ() + 0.5D;
			double d3 = 0.46875D;
			double d4 = this.someFunc(this.getWidthPixels());
			double d5 = this.someFunc(this.getHeightPixels());
			d0 = d0 - (double) this.facingDirection.getFrontOffsetX() * 0.46875D;
			d2 = d2 - (double) this.facingDirection.getFrontOffsetZ() * 0.46875D;
			d1 = d1 + d5;
			EnumFacing enumfacing = this.facingDirection.rotateYCCW();
			d0 = d0 + d4 * (double) enumfacing.getFrontOffsetX();
			d2 = d2 + d4 * (double) enumfacing.getFrontOffsetZ();
			this.posX = d0;
			this.posY = d1;
			this.posZ = d2;
			double d6 = (double) this.getWidthPixels();
			double d7 = (double) this.getHeightPixels();
			double d8 = (double) this.getWidthPixels();

			if (this.facingDirection.getAxis() == EnumFacing.Axis.Z)
			{
				d8 = 1.0D;
			}
			else
			{
				d6 = 1.0D;
			}

			// ???

			d6 = d6 / (this.getWidthPixels() / this.blocksToTakeUp() * 2D);
			d7 = d7 / (this.getHeightPixels() / this.blocksToTakeUp() * 2D);
			d8 = d8 / (this.getWidthPixels() / this.blocksToTakeUp() * 2D);
			this.setEntityBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
		}
	}

	private double someFunc(int size)
	{
		return size % (this.getWidthPixels() / this.blocksToTakeUp() * 2D) == 0 ? 0.5D : 0.0D;
	}

	@Override
	public void playPlaceSound()
	{
		this.playSound(SoundEvents.ENTITY_PAINTING_PLACE, 0.15f, 1.0f);
	}
}