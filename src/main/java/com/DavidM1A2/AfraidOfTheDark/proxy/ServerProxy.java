/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import java.util.Map;
import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

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
		Map<UUID, EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().uuidToPlayerMap;
		if (players.containsKey(spell.getSpellOwner()))
			return players.get(spell.getSpellOwner());
		else
			return null;
	}
}
