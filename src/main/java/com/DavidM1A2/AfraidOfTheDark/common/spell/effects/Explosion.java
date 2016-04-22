package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

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
	public void performEffect(BlockPos location, World world)
	{
		world.createExplosion(null, location.getX(), location.getY(), location.getZ(), 3.0f, true);
	}

	@Override
	public void performEffect(Entity entity)
	{
		entity.worldObj.createExplosion(null, entity.posX, entity.posY, entity.posZ, 3.0f, true);
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
