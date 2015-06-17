package com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft;

import java.util.Random;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.MCA.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCA.MCACommonLibrary.animation.AnimationHandler;

public class EntityDeeeSyft extends EntityFlying implements IMCAnimatedEntity
{
	protected AnimationHandler animHandler = new AnimationHandlerDeeeSyft(this);

	public EntityDeeeSyft(World par1World)
	{
		super(par1World);
		this.setSize(2.5f, 3.5f);

		this.moveHelper = new EntityDeeeSyft.DeeeSyftMoveHelper();
		this.tasks.addTask(5, new EntityDeeeSyft.AIRandomFly());
		this.tasks.addTask(7, new EntityDeeeSyft.AILookAround());
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return animHandler;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (!this.getAnimationHandler().isAnimationActive("jiggle"))
		{
			this.getAnimationHandler().activateAnimation("jiggle", 0);
		}
		return super.attackEntityFrom(source, amount);
	}

	// Apply entity attributes
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.0D);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
		}
	}

	class AILookAround extends EntityAIBase
	{
		private EntityDeeeSyft instance = EntityDeeeSyft.this;

		public AILookAround()
		{
			this.setMutexBits(2);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute()
		{
			return true;
		}

		/**
		 * Updates the task
		 */
		public void updateTask()
		{
			if (this.instance.getAttackTarget() == null)
			{
				this.instance.renderYawOffset = this.instance.rotationYaw = -((float) Math.atan2(this.instance.motionX, this.instance.motionZ)) * 180.0F / (float) Math.PI;
			}
			else
			{
				EntityLivingBase entitylivingbase = this.instance.getAttackTarget();
				double d0 = 64.0D;

				if (entitylivingbase.getDistanceSqToEntity(this.instance) < d0 * d0)
				{
					double d1 = entitylivingbase.posX - this.instance.posX;
					double d2 = entitylivingbase.posZ - this.instance.posZ;
					this.instance.renderYawOffset = this.instance.rotationYaw = -((float) Math.atan2(d1, d2)) * 180.0F / (float) Math.PI;
				}
			}
		}
	}

	class AIRandomFly extends EntityAIBase
	{
		private EntityDeeeSyft instance = EntityDeeeSyft.this;

		public AIRandomFly()
		{
			this.setMutexBits(1);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute()
		{
			EntityMoveHelper entitymovehelper = this.instance.getMoveHelper();

			if (!entitymovehelper.isUpdating())
			{
				return true;
			}
			else
			{
				double d0 = entitymovehelper.func_179917_d() - this.instance.posX;
				double d1 = entitymovehelper.func_179919_e() - this.instance.posY;
				double d2 = entitymovehelper.func_179918_f() - this.instance.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return d3 < 1.0D || d3 > 3600.0D;
			}
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean continueExecuting()
		{
			return false;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting()
		{
			Random random = this.instance.getRNG();
			double d0 = this.instance.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d1 = this.instance.posY + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d2 = this.instance.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.instance.getMoveHelper().setMoveTo(d0, d1, d2, EntityDeeeSyft.this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed).getBaseValue());
		}
	}

	class DeeeSyftMoveHelper extends EntityMoveHelper
	{
		private EntityDeeeSyft instance = EntityDeeeSyft.this;
		private int temp;

		public DeeeSyftMoveHelper()
		{
			super(EntityDeeeSyft.this);
		}

		public void onUpdateMoveHelper()
		{
			if (this.update)
			{
				double d0 = this.posX - this.instance.posX;
				double d1 = this.posY - this.instance.posY;
				double d2 = this.posZ - this.instance.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;

				if (this.temp-- <= 0)
				{
					this.temp += this.instance.getRNG().nextInt(5) + 2;
					d3 = (double) MathHelper.sqrt_double(d3);

					if (this.func_179926_b(this.posX, this.posY, this.posZ, d3))
					{
						this.instance.motionX += d0 / d3 * 0.1D;
						this.instance.motionY += d1 / d3 * 0.1D;
						this.instance.motionZ += d2 / d3 * 0.1D;
					}
					else
					{
						this.update = false;
					}
				}
			}
		}

		private boolean func_179926_b(double p_179926_1_, double p_179926_3_, double p_179926_5_, double p_179926_7_)
		{
			double d4 = (p_179926_1_ - this.instance.posX) / p_179926_7_;
			double d5 = (p_179926_3_ - this.instance.posY) / p_179926_7_;
			double d6 = (p_179926_5_ - this.instance.posZ) / p_179926_7_;
			AxisAlignedBB axisalignedbb = this.instance.getEntityBoundingBox();

			for (int i = 1; (double) i < p_179926_7_; ++i)
			{
				axisalignedbb = axisalignedbb.offset(d4, d5, d6);

				if (!this.instance.worldObj.getCollidingBoundingBoxes(this.instance, axisalignedbb).isEmpty())
				{
					return false;
				}
			}

			return true;
		}
	}
}