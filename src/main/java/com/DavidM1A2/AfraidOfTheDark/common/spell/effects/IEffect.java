package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import java.io.Serializable;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IEffect extends Serializable
{
	public abstract int getCost();

	public abstract void performEffect(BlockPos location, World world);

	public abstract void performEffect(Entity entity);
}
