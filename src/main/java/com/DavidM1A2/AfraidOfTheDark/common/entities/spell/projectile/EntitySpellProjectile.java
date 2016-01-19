/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySpellProjectile extends EntitySpell
{
	private AnimationHandler animHandler = new AnimationHandlerSpellProjectile(this);

	private int tileX = -1;
	private int tileY = -1;
	private int tileZ = -1;
	private Block insideOf;
	private boolean inGround;
	public EntityLivingBase shootingEntity;
	private int ticksAlive;
	private int ticksInAir;
	public double accelerationX = 0;
	public double accelerationY = 0;
	public double accelerationZ = 0;

	public EntitySpellProjectile(World worldIn)
	{
		super(worldIn);
		this.setSize(0.4F, 0.4F);
	}

	public EntitySpellProjectile(World world, EntityLivingBase shootingEntity, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity, boolean initialDeliveryMethod)
	{
		this(world);
		this.shootingEntity = shootingEntity;
		this.setLocationAndAngles(x, y, z, shootingEntity.rotationYaw, shootingEntity.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		double d3 = (double) MathHelper.sqrt_double(xVelocity * xVelocity + yVelocity * yVelocity + zVelocity * zVelocity);
		this.motionX = xVelocity / d3 * 0.4;
		this.motionY = yVelocity / d3 * 0.4;
		this.motionZ = zVelocity / d3 * 0.4;
	}

	/**
	 * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge length * 64 *
	 * renderDistanceWeight Args: distance
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		double d1 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return distance < d1 * d1;
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return animHandler;
	}

	@Override
	protected void updateSpellSpecificLogic()
	{
		if (!animHandler.isAnimationActive("Idle"))
			animHandler.activateAnimation("Idle", 0);

		// Fly! be free!
		if (!this.worldObj.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !this.worldObj.isBlockLoaded(new BlockPos(this))))
		{
			this.setDead();
		}
		else
		{
			Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
			Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
			vec3 = new Vec3(this.posX, this.posY, this.posZ);
			vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

			if (movingobjectposition != null)
			{
				vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
			}

			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;

			for (int i = 0; i < list.size(); ++i)
			{
				Entity entity1 = (Entity) list.get(i);

				if (entity1.canBeCollidedWith() && (!entity1.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25))
				{
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f, (double) f, (double) f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if (movingobjectposition1 != null)
					{
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D)
						{
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null)
			{
				movingobjectposition = new MovingObjectPosition(entity);
			}

			if (movingobjectposition != null)
			{
				this.onImpact(movingobjectposition);
			}

			if (!this.isDead)
			{
				this.posX += this.motionX;
				this.posY += this.motionY;
				this.posZ += this.motionZ;

				this.motionX += this.accelerationX;
				this.motionY += this.accelerationY;
				this.motionZ += this.accelerationZ;

				this.setPosition(this.posX, this.posY, this.posZ);
			}
		}
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	public void onImpact(MovingObjectPosition movingObjectPosition)
	{
		if (!this.worldObj.isRemote)
		{
			switch (movingObjectPosition.typeOfHit)
			{
				case BLOCK:
					this.performEffect(new BlockPos(movingObjectPosition.hitVec));
					break;
				case ENTITY:
					this.performEffect(movingObjectPosition.entityHit);
					break;
				case MISS:
					this.spellStageComplete();
					break;
				default:
					this.spellStageComplete();
					break;
			}
			this.setDead();
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setShort("xTile", (short) this.tileX);
		tagCompound.setShort("yTile", (short) this.tileY);
		tagCompound.setShort("zTile", (short) this.tileZ);
		ResourceLocation resourcelocation = (ResourceLocation) Block.blockRegistry.getNameForObject(this.insideOf);
		tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		tagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		tagCompound.setTag("direction", this.newDoubleNBTList(new double[]
		{ this.motionX, this.motionY, this.motionZ }));
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		this.tileX = tagCompund.getShort("xTile");
		this.tileY = tagCompund.getShort("yTile");
		this.tileZ = tagCompund.getShort("zTile");

		if (tagCompund.hasKey("inTile", 8))
		{
			this.insideOf = Block.getBlockFromName(tagCompund.getString("inTile"));
		}
		else
		{
			this.insideOf = Block.getBlockById(tagCompund.getByte("inTile") & 255);
		}

		this.inGround = tagCompund.getByte("inGround") == 1;

		if (tagCompund.hasKey("direction", 9))
		{
			NBTTagList nbttaglist = tagCompund.getTagList("direction", 6);
			this.motionX = nbttaglist.getDouble(0);
			this.motionY = nbttaglist.getDouble(1);
			this.motionZ = nbttaglist.getDouble(2);
		}
		else
		{
			this.setDead();
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	@Override
	public int getSpellLifeInTicks()
	{
		return 400;
	}
}
