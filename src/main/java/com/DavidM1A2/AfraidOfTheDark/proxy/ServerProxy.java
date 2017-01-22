/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
	public void registerMiscelaneous()
	{
		// Not used
	}

	@Override
	public void registerEntityRenders()
	{
		// Not used
	}

	@Override
	public void registerMiscRenders()
	{
		// Not used
	}

	@Override
	public EntityPlayer getSpellOwner(Spell spell)
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(spell.getSpellOwner());
	}

	@Override
	public void registerItemRenders()
	{
		// Not used
	}

	@Override
	public void registerBlockRenders()
	{
		// Not used		
	}

	@Override
	public void preInit()
	{
		// Unused
	}
}
