package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Explosion extends Effect
{
	public static final int id = 1;

	@Override
	public int getCost()
	{
		return 0;
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

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("id", Effects.Explosion.getID());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Effects getType()
	{
		return Effects.Explosion;
	}
}
