package com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft;

import net.minecraft.entity.EntityCreature;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.MCA.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCA.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.MCA.animations.DeeeSyft.AnimationHandlerDeeeSyft;

public class EntityDeeeSyft extends EntityCreature implements IMCAnimatedEntity
{
	protected AnimationHandler animHandler = new AnimationHandlerDeeeSyft(this);

	public EntityDeeeSyft(World par1World)
	{
		super(par1World);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return animHandler;
	}

	@Override
	public void onUpdate()
	{
		if (!this.getAnimationHandler().isAnimationActive("jiggle"))
		{
			this.getAnimationHandler().activateAnimation("jiggle", 0);
		}

		super.onUpdate();
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readEntityFromNBT(nbttagcompound);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeEntityToNBT(nbttagcompound);
	}
}