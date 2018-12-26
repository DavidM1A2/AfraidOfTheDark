/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class EntitySplinterDroneMoveHelper extends EntityMoveHelper
{
	private final EntitySplinterDrone splinterDrone;
	private int temp;

	public EntitySplinterDroneMoveHelper(EntitySplinterDrone splinterDrone)
	{
		super(splinterDrone);
		this.splinterDrone = splinterDrone;
	}

	@Override
	public void onUpdateMoveHelper()
	{
		if (this.isUpdating() && this.splinterDrone.getAttackTarget() == null)
		{
			double x = this.posX - this.splinterDrone.posX;
			double y = this.posY - this.splinterDrone.posY;
			double z = this.posZ - this.splinterDrone.posZ;
			double vector = x * x + y * y + z * z;

			if (this.temp-- <= 0)
			{
				this.temp = this.temp + this.splinterDrone.getRNG().nextInt(5) + 2;
				vector = MathHelper.sqrt(vector);

				if (this.canMoveHere(this.posX, this.posY, this.posZ, vector))
				{
					this.splinterDrone.motionX = this.splinterDrone.motionX + x / vector * this.speed;
					this.splinterDrone.motionY = this.splinterDrone.motionY + y / vector * this.speed;
					this.splinterDrone.motionZ = this.splinterDrone.motionZ + z / vector * this.speed;
				}
				else
				{
					this.action = Action.WAIT;
				}
			}
		}
	}

	private boolean canMoveHere(double posX, double posY, double posZ, double vector)
	{
		double x = (posX - this.splinterDrone.posX) / vector;
		double y = (posY - this.splinterDrone.posY) / vector;
		double z = (posZ - this.splinterDrone.posZ) / vector;
		AxisAlignedBB axisalignedbb = this.splinterDrone.getEntityBoundingBox();

		for (int i = 1; i < vector; i++)
		{
			axisalignedbb = axisalignedbb.offset(x, y, z);

			if (!this.splinterDrone.world.getCollisionBoxes(this.splinterDrone, axisalignedbb).isEmpty())
			{
				return false;
			}
		}

		return true;
	}
}