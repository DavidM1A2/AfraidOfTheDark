/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellHitInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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
	protected float velocity = 1.0f;
	public EntityLivingBase shootingEntity;
	private EntityLivingBase targetHit = null;

	public EntitySpellProjectile(World world)
	{
		super(world);
	}

	public EntitySpellProjectile(Spell callback, EntityLivingBase shootingEntity, int spellStageIndex, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity)
	{
		super(AfraidOfTheDark.proxy.getSpellOwner(callback).world, callback, spellStageIndex);
		this.shootingEntity = shootingEntity;

		if (shootingEntity != null)
			this.setLocationAndAngles(x, y, z, shootingEntity.rotationYaw, shootingEntity.rotationPitch);
		else
			this.setPosition(x, y, z);
		double d3 = (double) MathHelper.sqrt(xVelocity * xVelocity + yVelocity * yVelocity + zVelocity * zVelocity);
		this.motionX = xVelocity / d3 * velocity;
		this.motionY = yVelocity / d3 * velocity;
		this.motionZ = zVelocity / d3 * velocity;
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
	public void updateSpellSpecificLogic()
	{
		if (world.isRemote)
		{
			if (!animHandler.isAnimationActive("Idle"))
				animHandler.activateAnimation("Idle", 0);
		}
		else
			this.performHitDetection();

		if (!this.isDead)
		{
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;

			this.setPosition(this.posX, this.posY, this.posZ);
		}
	}

	private void performHitDetection()
	{
		Vec3d vec3 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec31 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult rayTraceResult = this.world.rayTraceBlocks(vec3, vec31);
		vec3 = new Vec3d(this.posX, this.posY, this.posZ);
		vec31 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (rayTraceResult != null)
		{
			vec31 = new Vec3d(rayTraceResult.hitVec.xCoord, rayTraceResult.hitVec.yCoord, rayTraceResult.hitVec.zCoord);
		}

		Entity entity = null;
		List list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
		double d0 = 0.0D;

		for (int i = 0; i < list.size(); ++i)
		{
			Entity entity1 = (Entity) list.get(i);

			if (entity1.canBeCollidedWith() && !entity1.isEntityEqual(this.shootingEntity) && !(entity1 instanceof EntitySpellProjectile))
			{
				float f = 0.3F;
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f, (double) f, (double) f);
				RayTraceResult rayTraceResult1 = axisalignedbb.calculateIntercept(vec3, vec31);

				if (rayTraceResult1 != null)
				{
					double d1 = vec3.distanceTo(rayTraceResult1.hitVec);

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
			rayTraceResult = new RayTraceResult(entity);
		}

		if (rayTraceResult != null)
		{
			this.onImpact(rayTraceResult);
		}
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	public void onImpact(RayTraceResult rayTraceResult)
	{
		switch (rayTraceResult.typeOfHit)
		{
			case BLOCK:
				BlockPos hit = new BlockPos(rayTraceResult.hitVec);
				if (this.world.getBlockState(hit).getBlock() instanceof BlockAir)
					hit = hit.offset(rayTraceResult.sideHit.getOpposite());
				this.performEffect(new SpellHitInfo(AfraidOfTheDark.proxy.getSpellOwner(this.getSpellSource()), hit, this.world, 1));
				break;
			case ENTITY:
				if (rayTraceResult.entityHit instanceof EntityLivingBase)
					this.targetHit = (EntityLivingBase) rayTraceResult.entityHit;
				this.performEffect(new SpellHitInfo(AfraidOfTheDark.proxy.getSpellOwner(this.getSpellSource()), rayTraceResult.entityHit));
				break;
			case MISS:
				break;
			default:
				break;
		}
		this.spellStageComplete();
		this.setDead();
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
		ResourceLocation resourcelocation = (ResourceLocation) Block.REGISTRY.getNameForObject(this.insideOf);
		tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
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

	public EntityLivingBase getTargetHit()
	{
		return this.targetHit;
	}
}
