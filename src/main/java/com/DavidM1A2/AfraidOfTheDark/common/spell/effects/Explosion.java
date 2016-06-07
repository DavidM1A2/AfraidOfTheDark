package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Explosion extends Effect
{
	private float explosionSize = 3.0f;

	@Override
	public int getCost()
	{
		return 10;
	}

	@Override
	public void performEffect(BlockPos location, World world, double radius)
	{
		if (radius < 0)
			radius = 0;
		world.createExplosion(null, location.getX(), location.getY(), location.getZ(), (float) radius, true);
		VitaeUtils.vitaeReleasedFX(world, location, radius, 5);
	}

	@Override
	public void performEffect(Entity entity)
	{
		entity.worldObj.createExplosion(null, entity.posX, entity.posY, entity.posZ, 3.0f, true);
		VitaeUtils.vitaeReleasedFX(entity.worldObj, entity.getPosition(), 3, 5);
	}

	public void setExplosionSize(float explosionSize)
	{
		this.explosionSize = explosionSize;
	}

	public float getExplosionSize()
	{
		return this.explosionSize;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		this.setExplosionSize(compound.getFloat("explosionSize"));
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setFloat("explosionSize", this.getExplosionSize());
	}

	@Override
	public Effects getType()
	{
		return Effects.Explosion;
	}
}
