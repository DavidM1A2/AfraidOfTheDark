/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDWorldData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.AOTDDungeonTypes;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class AOTDCommands extends CommandBase
{
	private final List aliases;

	public AOTDCommands()
	{
		// Aliases aka command (/AfraidOfTheDark or /AOTD)
		this.aliases = new ArrayList();
		this.aliases.add("AOTD");
		this.aliases.add("AfraidOfTheDark");
	}

	@Override
	public String getName()
	{
		return "printDungeons";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "/AOTD OR /AfraidOfTheDark";
	}

	@Override
	public List getAliases()
	{
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length == 1 && args[0].equalsIgnoreCase("printDungeons"))
		{
			this.printKnownDungeons(sender);
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("help") || args.length == 0)
		{
			this.printHelp(sender);
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length == 0)
			return this.getListOfStringsMatchingLastWord(args, "AOTD", "AfraidOfTheDark");
		if (args.length == 1)
			return this.getListOfStringsMatchingLastWord(args, "printDungeons", "help");

		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return false;
	}

	/**
	 * Check if the given ICommandSender has permission to execute this command
	 */
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
	}

	@Override
	public int compareTo(ICommand object)
	{
		return this.getName().compareTo(((ICommand) object).getName());
	}

	//
	// Command executions below
	//

	private void printKnownDungeons(ICommandSender sender)
	{
		boolean validSender = false;
		if (sender.getCommandSenderEntity() instanceof EntityPlayer)
		{
			if (((EntityPlayer) sender).canUseCommand(2, "") || sender.getEntityWorld().getMinecraftServer().isSinglePlayer())
			{
				validSender = true;
			}
		}
		else if (sender.getEntityWorld().getMinecraftServer().isDedicatedServer())
		{
			validSender = true;
		}

		if (!validSender)
		{
			sender.sendMessage(new TextComponentString(ChatFormatting.RED + "You do not have permission to use this command"));
			return;
		}

		String writeDirectory = System.getProperty("user.dir") + "\\AOTDDungeons.txt";
		File file = new File(writeDirectory);
		try
		{
			Writer output = new BufferedWriter(new FileWriter(file));

			output.write("Known dungeons above ground: \n");
			for (Point3D point3d : AOTDWorldData.get(sender.getEntityWorld()).getDungeonsAboveGround())
			{
				AOTDDungeonTypes dungeon = AOTDDungeonTypes.getDungeonFromRadius(point3d.getY());
				if (dungeon == null)
					continue;
				String toPrint = dungeon.getName() + " at x = " + point3d.getX() + ", z = " + point3d.getZ() + ".\n";
				output.write(toPrint);
			}
			output.write("\n\n");

			output.write("Known dungeons below ground: \n");
			for (Point3D point3d : AOTDWorldData.get(sender.getEntityWorld()).getDungeonsBelowGround())
			{
				AOTDDungeonTypes dungeon = AOTDDungeonTypes.getDungeonFromRadius(point3d.getY());
				if (dungeon == null)
					continue;
				String toPrint = dungeon.getName() + " at x = " + point3d.getX() + ", z = " + point3d.getZ() + ".\n";
				output.write(toPrint);
			}

			sender.sendMessage(new TextComponentString("Wrote all known AOTD Dungeons in this world to\n" + writeDirectory));

			output.close();
		}
		catch (IOException e)
		{
		}
	}

	private void printHelp(ICommandSender sender)
	{
		sender.sendMessage(new TextComponentString("/AOTD and /AfraidOfTheDark both work for all commands:"));
		sender.sendMessage(new TextComponentString("/AOTD help - Lists all AOTD Commands"));
		sender.sendMessage(new TextComponentString("/AOTD printDungeons - Prints all known AOTD dungeons to the .minecraft folder. Only works if opped"));
	}
}
