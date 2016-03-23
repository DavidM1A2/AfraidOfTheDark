/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile;

import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntitySpellProjectileDive extends EntitySpellProjectile
{
	private static final double RANGE = 40;

	public EntitySpellProjectileDive(World world)
	{
		super(world);
	}

	public EntitySpellProjectileDive(Spell callback, int spellStageIndex, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity)
	{
		super(callback, spellStageIndex, x, y, z, xVelocity, yVelocity, zVelocity);

		this.motionX = 0;
		this.motionY = velocity;
		this.motionZ = 0;
	}

	@Override
	protected void updateSpellSpecificLogic()
	{
		super.updateSpellSpecificLogic();

		if (this.getTicksAlive() == 40 && !worldObj.isRemote)
		{
			EntityLivingBase closestEntity = null;
			for (Object entity : this.worldObj.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox().expand(RANGE, RANGE, RANGE)))
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
				double distanceToClosest = this.getDistanceToEntity(closestEntity);
				this.motionX = xVelocity / distanceToClosest * velocity;
				this.motionY = yVelocity / distanceToClosest * velocity;
				this.motionZ = zVelocity / distanceToClosest * velocity;
			}
			else
			{
				this.performEffect(this.getPosition());
				this.spellStageComplete();
				this.setDead();
			}
		}
	}
}
