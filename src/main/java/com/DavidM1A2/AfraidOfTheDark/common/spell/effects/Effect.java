/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.effects;

import java.io.Serializable;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class Effect implements Serializable
{
	public abstract double getCost();

	public abstract void performEffect(Entity entity);

	public abstract void performEffect(World world, BlockPos location);
}
