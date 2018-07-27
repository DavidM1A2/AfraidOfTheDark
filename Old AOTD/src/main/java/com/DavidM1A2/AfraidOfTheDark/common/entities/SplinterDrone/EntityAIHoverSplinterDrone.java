/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone;

import java.util.Random;

import net.minecraft.block.BlockAir;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EntityAIHoverSplinterDrone extends EntityAIBase
{
	private final EntitySplinterDrone splinterDrone;

	public EntityAIHoverSplinterDrone(EntitySplinterDrone splinterDrone)
	{
		this.setMutexBits(1);
		this.splinterDrone = splinterDrone;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		EntityMoveHelper entitymovehelper = this.splinterDrone.getMoveHelper();

		if (!entitymovehelper.isUpdating())
		{
			if (this.splinterDrone.getAttackTarget() == null)
			{
				return true;
			}
		}

		double d0 = entitymovehelper.getX() - this.splinterDrone.posX;
		double d1 = entitymovehelper.getY() - this.splinterDrone.posY;
		double d2 = entitymovehelper.getZ() - this.splinterDrone.posZ;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;
		return d3 < 1.0D || d3 > 3600.0D;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting()
	{
		return false;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		Random random = this.splinterDrone.getRNG();

		double xToGoTo = this.splinterDrone.posX + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;

		double zToGoTo = this.splinterDrone.posZ + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;

		double yToGoTo = getHeightToMoveTo(xToGoTo, zToGoTo);

		this.splinterDrone.getMoveHelper().setMoveTo(xToGoTo, yToGoTo, zToGoTo, this.splinterDrone.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
	}

	private double getHeightToMoveTo(double x, double z)
	{
		for (int y = MathHelper.floor(splinterDrone.posY); y > 0; y--)
		{
			if (!(this.splinterDrone.world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockAir))
			{
				return MathHelper.clamp(y + 3, 0.0D, 255D);
			}
		}
		return splinterDrone.posY;
	}
}
