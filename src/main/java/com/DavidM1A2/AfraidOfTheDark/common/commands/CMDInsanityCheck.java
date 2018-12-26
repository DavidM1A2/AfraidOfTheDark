/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.commands;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.SaveHandler;

public class CMDInsanityCheck implements ICommand
{
	private final List aliases;

	public CMDInsanityCheck()
	{
		// Aliases aka command (/debug or /d)
		this.aliases = new ArrayList();
		this.aliases.add("debug");
		this.aliases.add("d");
	}

	// This is the name of the command
	@Override
	public String getCommandName()
	{
		return "debug";
	}

	// How do i use the command?
	@Override
	public String getCommandUsage(final ICommandSender iCommandSender)
	{
		return "debug";
	}

	// Aliases of the command
	@Override
	public List getCommandAliases()
	{
		return this.aliases;
	}

	// What to do when the command happens
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args)
	{
		final EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
		sender.addChatMessage(new TextComponentString("Your current insanity is: " + player.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerInsanity() + "%"));
		sender.addChatMessage(new TextComponentString("Your current has started AOTD status is: " + player.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD()));
		sender.addChatMessage(new TextComponentString("Current Vitae level is: " + player.getCapability(ModCapabilities.ENTITY_DATA, null).getVitaeLevel()));
		sender.addChatMessage(new TextComponentString("Current dimension is: " + player.dimension));
		if (!sender.getEntityWorld().isRemote)
		{
			sender.addChatMessage(new TextComponentString("Number of registered players: " + ((SaveHandler) server.worldServers[0].getSaveHandler()).getAvailablePlayerDat().length));
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

	// No username or tab completes
	@Override
	public boolean isUsernameIndex(final String[] p_82358_1_, final int p_82358_2_)
	{
		return false;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		return null;
	}

	@Override
	public int compareTo(ICommand object)
	{
		return this.getCommandName().compareTo(((ICommand) object).getCommandName());
	}
}
