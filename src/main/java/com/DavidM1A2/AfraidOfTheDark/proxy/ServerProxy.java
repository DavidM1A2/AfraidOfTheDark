/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import com.DavidM1A2.AfraidOfTheDark.client.particleFX.AOTDParticleFX;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

// Any server side-only things we want to do
public class ServerProxy extends CommonProxy
{
	@Override
	public void registerKeyBindings()
	{
		// NOOP
	}

	// Here we register packets and a channel
	@Override
	public void registerChannel()
	{
		super.registerChannel();
	}

	@Override
	public void registerRenderThings()
	{
		// NOOP
	}

	@Override
	public void registerMiscelaneous()
	{
		// Not used
	}

	@Override
	public void generateParticles(Entity entity, Class<? extends AOTDParticleFX> particleClass)
	{
		// Not used
	}

	@Override
	public void generateParticles(World world, double x, double y, double z, Class<? extends AOTDParticleFX> particleClass)
	{
		// Not used
	}

}
