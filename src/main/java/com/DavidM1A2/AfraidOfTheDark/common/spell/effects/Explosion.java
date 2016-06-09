package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellHitInfo;
import com.DavidM1A2.AfraidOfTheDark.common.utility.VitaeUtils;

import net.minecraft.nbt.NBTTagCompound;

public class Explosion extends Effect
{
	private float explosionSize = 3.0f;

	@Override
	public int getCost()
	{
		return 10;
	}

	@Override
	public void performEffect(SpellHitInfo hitInfo)
	{
		if (hitInfo.getEntityHit() == null)
		{
			int radius = hitInfo.getRadius();
			if (radius < 0)
				radius = 0;
			hitInfo.getWorld().createExplosion(null, hitInfo.getLocation().getX(), hitInfo.getLocation().getY(), hitInfo.getLocation().getZ(), (float) radius, true);
			VitaeUtils.vitaeReleasedFX(hitInfo.getWorld(), hitInfo.getLocation(), radius, 5);
		}
		else
		{
			hitInfo.getEntityHit().worldObj.createExplosion(null, hitInfo.getLocation().getX(), hitInfo.getLocation().getY(), hitInfo.getLocation().getZ(), 3.0f, true);
			VitaeUtils.vitaeReleasedFX(hitInfo.getWorld(), hitInfo.getLocation(), 3, 5);
		}
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
