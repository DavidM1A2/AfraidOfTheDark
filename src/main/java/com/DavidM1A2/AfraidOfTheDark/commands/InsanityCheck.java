/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.playerData.Insanity;

public class InsanityCheck implements ICommand
{
	private List aliases;

	public InsanityCheck()
	{
		// Aliases aka command (/debug or /d)
		aliases = new ArrayList();
		aliases.add("debug");
		aliases.add("d");
	}

	// No idea what this does
	@Override
	public int compareTo(Object arg0)
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
	public String getCommandUsage(ICommandSender iCommandSender)
	{
		return "debug";
	}

	// Aliases of the command
	@Override
	public List getCommandAliases()
	{
		return aliases;
	}

	// What to do when the command happens
	@Override
	public void processCommand(ICommandSender iCommandSender, String[] p_71515_2_)
	{
		EntityPlayer sender = iCommandSender.getEntityWorld().getPlayerEntityByName(iCommandSender.getCommandSenderName());
		iCommandSender.addChatMessage(new ChatComponentText(("Your current insanity is: " + Insanity.get(sender))));
		iCommandSender.addChatMessage(new ChatComponentText(("Your current has started AOTD status is: " + HasStartedAOTD.get(sender))));
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
	{
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
	{
		return null;
	}

	// No username or tab completes
	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
	{
		return false;
	}

}
