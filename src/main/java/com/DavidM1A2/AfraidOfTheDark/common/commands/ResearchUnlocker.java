/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.commands;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

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
	public String getName()
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
	public List getAliases()
	{
		return this.aliases;
	}

	// What to do when the command happens
	@Override
	public void execute(final ICommandSender iCommandSender, final String[] p_71515_2_)
	{
		final EntityPlayer sender = (EntityPlayer) iCommandSender.getCommandSenderEntity();
		if (!sender.worldObj.isRemote)
		{
			EntityEnaria enaria = new EntityEnaria(sender.worldObj);
			enaria.getEntityData().setBoolean(EntityEnaria.IS_VALID, true);
			enaria.setPosition(sender.posX, sender.posY, sender.posZ);
			sender.worldObj.spawnEntityInWorld(enaria);
		}
		//		if (HasStartedAOTD.get(sender))
		//		{
		//			for (ResearchTypes type : ResearchTypes.values())
		//			{
		//				if (!Research.isResearched(sender, type))
		//				{
		//					Research.unlockResearchSynced(sender, type, Side.SERVER, false);
		//				}
		//			}
		//		}
	}

	@Override
	public boolean canCommandSenderUse(final ICommandSender p_71519_1_)
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
		return null;
	}
}
