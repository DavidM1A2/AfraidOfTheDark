package com.DavidM1A2.AfraidOfTheDark.common.entities.spell.AOE;

import java.util.LinkedList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAOE extends EntitySpell
{
	private double size;
	private List<EntityLivingBase> affectedEntities = new LinkedList<EntityLivingBase>();

	public EntityAOE(World world)
	{
		super(world);
	}

	public EntityAOE(World world, Spell callback, int spellStageIndex, double size, BlockPos location)
	{
		super(world, callback, spellStageIndex);
		this.size = size;
		this.setPosition(location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5);
	}

	@Override
	protected void updateSpellSpecificLogic()
	{
		if (this.getTicksAlive() == 1)
		{
			this.affectedEntities = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(this.size, this.size, this.size));
			for (EntityLivingBase entityLivingBase : this.affectedEntities)
			{
				if (!(entityLivingBase instanceof EntityArmorStand))
				{
					this.performEffect(entityLivingBase);
				}
			}
			this.performEffect(this.getPosition(), this.size);
			this.spellStageComplete();
			this.setDead();
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setDouble("size", this.size);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.size = compound.getDouble("size");
	}

	@Override
	public float getSpellEntityWidth()
	{
		return 0;
	}

	@Override
	public float getSpellEntityHeight()
	{
		return 0;
	}

	@Override
	public int getSpellLifeInTicks()
	{
		return 10;
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return null;
	}

	public double getSize()
	{
		return this.size;
	}

	public List<EntityLivingBase> getAffectedEntities()
	{
		return this.affectedEntities;
	}
}
