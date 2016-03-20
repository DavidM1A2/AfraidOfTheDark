/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
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
	private boolean useStandardAI;
	private float velocity = 0.5f;
	public EntityLivingBase shootingEntity;

	public EntitySpellProjectile(World world)
	{
		super(world);
	}

	public EntitySpellProjectile(Spell callback, int spellStageIndex, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity, boolean useStandardAI)
	{
		super(callback.attemptToGetPlayer().worldObj, callback, spellStageIndex);
		this.shootingEntity = callback.attemptToGetPlayer();
		this.useStandardAI = useStandardAI;

		if (this.useStandardAI)
		{
			this.setLocationAndAngles(x, y, z, shootingEntity.rotationYaw, shootingEntity.rotationPitch);
			this.setPosition(this.posX, this.posY, this.posZ);
			double d3 = (double) MathHelper.sqrt_double(xVelocity * xVelocity + yVelocity * yVelocity + zVelocity * zVelocity);
			this.motionX = xVelocity / d3 * velocity;
			this.motionY = yVelocity / d3 * velocity;
			this.motionZ = zVelocity / d3 * velocity;
		}
		else
		{
			this.setLocationAndAngles(x, y, z, shootingEntity.rotationYaw, shootingEntity.rotationPitch);
			this.setPosition(this.posX, this.posY, this.posZ);
			this.motionX = 0;
			this.motionY = velocity;
			this.motionZ = 0;
		}
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
		// Fly! be free!
		if (!this.worldObj.isRemote)
		{
			if (this.shootingEntity != null && this.shootingEntity.isDead || !this.worldObj.isBlockLoaded(new BlockPos(this)))
			{
				this.setDead();
			}
			else
			{
				if (!this.useStandardAI && this.getTicksAlive() == 40)
				{
					EntityLivingBase closestEntity = null;
					for (Object entity : this.worldObj.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox().expand(40, 40, 40)))
					{
						if (entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand))
						{
							EntityLivingBase entityLiving = (EntityLivingBase) entity;
							if (closestEntity == null || this.getDistanceToEntity(entityLiving) < this.getDistanceToEntity(closestEntity))
							{
								if (!(entityLiving instanceof EntityPlayer) || ((entityLiving instanceof EntityPlayer) && !((EntityPlayer) entityLiving).getPersistentID().equals(this.getSpellSource().getSpellOwner())))
									closestEntity = entityLiving;
							}
						}
					}

					if (closestEntity != null)
					{
						double xVelocity = closestEntity.posX - this.posX;
						double yVelocity = closestEntity.getEntityBoundingBox().minY + (double) (closestEntity.height / 2.0F) - (this.posY + (double) (this.height / 2.0F));
						double zVelocity = closestEntity.posZ - this.posZ;
						this.motionX = xVelocity / 20.0f;
						this.motionY = yVelocity / 20.0f;
						this.motionZ = zVelocity / 20.0f;
					}
					else
					{
						this.performEffect(this.getPosition());
						this.spellStageComplete();
						this.setDead();
					}
				}
				else
				{
					this.performHitDetection();
				}

				if (!this.isDead)
				{
					this.posX += this.motionX;
					this.posY += this.motionY;
					this.posZ += this.motionZ;

					this.setPosition(this.posX, this.posY, this.posZ);
				}
			}
		}
		else
		{
			if (!animHandler.isAnimationActive("Idle"))
				animHandler.activateAnimation("Idle", 0);
		}
	}

	private void performHitDetection()
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

			if (entity1.canBeCollidedWith() && !entity1.isEntityEqual(this.shootingEntity) && !(entity1 instanceof EntitySpellProjectile))
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
					break;
				default:
					break;
			}
			this.spellStageComplete();
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
		tagCompound.setTag("direction", this.newDoubleNBTList(new double[]
		{ this.motionX, this.motionY, this.motionZ }));
		tagCompound.setBoolean("standardAI", this.useStandardAI);
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

		if (tagCompund.hasKey("direction", 9))
		{
			NBTTagList nbttaglist = tagCompund.getTagList("direction", 6);
			this.motionX = nbttaglist.getDoubleAt(0);
			this.motionY = nbttaglist.getDoubleAt(1);
			this.motionZ = nbttaglist.getDoubleAt(2);
		}
		else
		{
			this.setDead();
		}

		this.useStandardAI = tagCompund.getBoolean("standardAI");
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

	@Override
	public float getSpellEntityWidth()
	{
		return 0.4f;
	}

	@Override
	public float getSpellEntityHeight()
	{
		return 0.4f;
	}
}
