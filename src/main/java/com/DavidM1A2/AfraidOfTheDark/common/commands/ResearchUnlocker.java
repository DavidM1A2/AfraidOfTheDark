package com.DavidM1A2.AfraidOfTheDark.common.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class ResearchUnlocker implements ICommand
{
	private final List aliases;

	public ResearchUnlocker()
	{
		// Aliases aka command (/debug or /d)
		this.aliases = new ArrayList();
		this.aliases.add("knowledge");
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
		return "knowledge";
	}

	// How do i use the command?
	@Override
	public String getCommandUsage(final ICommandSender iCommandSender)
	{
		return "knowledge";
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
		if (HasStartedAOTD.get(sender))
		{
			for (ResearchTypes type : ResearchTypes.values())
			{
				if (!Research.isResearched(sender, type))
				{
					Research.unlockResearchSynced(sender, type, Side.SERVER, false);
				}
			}
		}
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
