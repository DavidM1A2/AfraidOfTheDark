package com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDamageSources;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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

// See EntityFireball.class
public class EntitySplinterDroneProjectile extends Entity implements IMCAnimatedEntity
{
	private int tileX = -1;
	private int tileY = -1;
	private int tileZ = -1;
	private Block insideOf;
	private boolean inGround;
	public EntityLivingBase shootingEntity;
	private int ticksAlive;
	private int ticksInAir;
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;

	protected AnimationHandler animHandler = new AnimationHandlerSplinterDroneProjectile(this);

	public EntitySplinterDroneProjectile(World par1World)
	{
		super(par1World);
		this.setSize(0.4F, 0.4F);
	}

	public EntitySplinterDroneProjectile(World world, EntityLivingBase shootingEntity, double xVelocity, double yVelocity, double zVelocity)
	{
		super(world);
		this.shootingEntity = shootingEntity;
		this.setSize(0.4F, 0.4F);
		this.setLocationAndAngles(shootingEntity.posX, shootingEntity.posY, shootingEntity.posZ, shootingEntity.rotationYaw, shootingEntity.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = this.motionY = this.motionZ = 0.0D;
		xVelocity = xVelocity + this.rand.nextGaussian() * 0.4D;
		yVelocity = yVelocity + this.rand.nextGaussian() * 0.4D;
		zVelocity = zVelocity + this.rand.nextGaussian() * 0.4D;
		double d3 = (double) MathHelper.sqrt(xVelocity * xVelocity + yVelocity * yVelocity + zVelocity * zVelocity);
		this.accelerationX = xVelocity / d3 * 0.1D;
		this.accelerationY = yVelocity / d3 * 0.1D;
		this.accelerationZ = zVelocity / d3 * 0.1D;
	}

	@Override
	protected void entityInit()
	{
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

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		if (this.world.isRemote)
			if (!this.animHandler.isAnimationActive("Sping"))
				this.animHandler.activateAnimation("Sping", 0);

		if (!this.world.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !this.world.isBlockLoaded(new BlockPos(this))))
		{
			this.setDead();
		}
		else
		{
			super.onUpdate();

			if (this.inGround)
			{
				if (this.world.getBlockState(new BlockPos(this.tileX, this.tileY, this.tileZ)).getBlock() == this.insideOf)
				{
					this.ticksAlive = this.ticksAlive + 1;

					if (this.ticksAlive == 600)
					{
						this.setDead();
					}

					return;
				}

				this.inGround = false;
				this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
				this.ticksAlive = 0;
				this.ticksInAir = 0;
			}
			else
			{
				this.ticksInAir = this.ticksInAir + 1;
			}

			if (this.ticksInAir > 600 && !world.isRemote)
			{
				this.setDead();
			}

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

				if (entity1.canBeCollidedWith() && (!entity1.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25))
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

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) + 90.0F;

			for (this.rotationPitch = (float) (Math.atan2((double) f1, this.motionY) * 180.0D / Math.PI) - 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
			{
			}

			while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
			{
				this.prevRotationPitch += 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw < -180.0F)
			{
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
			{
				this.prevRotationYaw += 360.0F;
			}

			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			float f2 = this.getMotionFactor();

			this.motionX += this.accelerationX;
			this.motionY += this.accelerationY;
			this.motionZ += this.accelerationZ;
			this.motionX *= (double) f2;
			this.motionY *= (double) f2;
			this.motionZ *= (double) f2;

			this.setPosition(this.posX, this.posY, this.posZ);
		}
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	public void onImpact(RayTraceResult rayTraceResult)
	{
		if (!this.world.isRemote)
		{
			if (rayTraceResult.entityHit != null)
			{
				if (rayTraceResult.entityHit.attackEntityFrom(AOTDDamageSources.causePlasmaBallDamage(this, this.shootingEntity), 1.0F))
				{
					this.applyEnchantments(this.shootingEntity, rayTraceResult.entityHit);

					if (rayTraceResult.entityHit instanceof EntityPlayer)
					{
						EntityPlayer entityPlayer = (EntityPlayer) rayTraceResult.entityHit;
						entityPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, 2, false, false));
					}
				}
			}

			this.setDead();
		}
	}

	/**
	 * Return the motion factor for this projectile. The factor is multiplied by the original motion.
	 */
	protected float getMotionFactor()
	{
		return 0.95F;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setShort("xTile", (short) this.tileX);
		tagCompound.setShort("yTile", (short) this.tileY);
		tagCompound.setShort("zTile", (short) this.tileZ);
		ResourceLocation resourcelocation = (ResourceLocation) Block.REGISTRY.getNameForObject(this.insideOf);
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

	@Override
	public float getCollisionBorderSize()
	{
		return 1.0F;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float brightness)
	{
		return 1.0F;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender(float brightness)
	{
		return 15728880;
	}
}