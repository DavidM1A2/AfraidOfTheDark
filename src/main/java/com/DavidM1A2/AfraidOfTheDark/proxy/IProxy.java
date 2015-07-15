/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import com.DavidM1A2.AfraidOfTheDark.client.particleFX.AOTDParticleFX;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

// Interface containing methods for the proxy
public interface IProxy
{
	public abstract void registerKeyBindings();

	public abstract void registerRenderThings();

	public abstract void registerChannel();

	public abstract void registerMiscelaneous();

	public abstract void generateParticles(Entity entity, Class<? extends AOTDParticleFX> particleClass);

	public abstract void generateParticles(World world, BlockPos location, Class<? extends AOTDParticleFX> particleClass);
}
