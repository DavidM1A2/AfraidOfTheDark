/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Insanity;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Vitae;

public class InsanityCheck implements ICommand
{
	private final List aliases;

	public InsanityCheck()
	{
		// Aliases aka command (/debug or /d)
		this.aliases = new ArrayList();
		this.aliases.add("debug");
		this.aliases.add("d");
	}

	// No idea what this does
	@Override
	public int compareTo(final Object arg0)
	{
		return 0;
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
	public void processCommand(final ICommandSender iCommandSender, final String[] p_71515_2_)
	{
		final EntityPlayer sender = (EntityPlayer) iCommandSender.getCommandSenderEntity();
		iCommandSender.addChatMessage(new ChatComponentText("Your current insanity is: " + Insanity.get(sender) + "%"));
		iCommandSender.addChatMessage(new ChatComponentText("Your current has started AOTD status is: " + HasStartedAOTD.get(sender)));
		iCommandSender.addChatMessage(new ChatComponentText("Current Vitae level is: " + Vitae.get(sender)));
		iCommandSender.addChatMessage(new ChatComponentText("Current dimension is: " + sender.dimension));
		iCommandSender.addChatMessage(new ChatComponentText("Current dimension is: " + sender.worldObj.getWorldInfo().getGeneratorOptions()));
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender p_71519_1_)
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
	public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
