/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellHitInfo;

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

	public EntitySpellProjectileDive(Spell callback, EntityLivingBase shootingEntity, int spellStageIndex, double x, double y, double z)
	{
		super(callback, shootingEntity, spellStageIndex, x, y, z, 0, 0, 0);

		this.motionX = 0;
		this.motionY = velocity;
		this.motionZ = 0;
	}

	@Override
	public void updateSpellSpecificLogic()
	{
		super.updateSpellSpecificLogic();

		if (this.getTicksAlive() == 20 && !world.isRemote)
		{
			EntityLivingBase closestEntity = null;
			for (Object entity : this.world.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox().expand(RANGE, RANGE, RANGE)))
			{
				if (entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand))
				{
					EntityLivingBase entityLiving = (EntityLivingBase) entity;
					if (closestEntity == null || this.getDistance(entityLiving) < this.getDistance(closestEntity))
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
				double distanceToClosest = this.getDistance(closestEntity);
				this.motionX = xVelocity / distanceToClosest * velocity;
				this.motionY = yVelocity / distanceToClosest * velocity;
				this.motionZ = zVelocity / distanceToClosest * velocity;
			}
			else
			{
				this.performEffect(new SpellHitInfo(AfraidOfTheDark.proxy.getSpellOwner(this.getSpellSource()), this.getPosition(), this.world, 1));
				this.spellStageComplete();
				this.setDead();
			}
		}
	}
}
