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

import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDWorldData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.AOTDDungeonTypes;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class AfraidOfTheDarkCommands implements ICommand
{
	private final List aliases;

	public AfraidOfTheDarkCommands()
	{
		// Aliases aka command (/AfraidOfTheDark or /AOTD)
		this.aliases = new ArrayList();
		this.aliases.add("AOTD");
		this.aliases.add("AfraidOfTheDark");
	}

	@Override
	public int compareTo(Object o)
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return "AfraidOfTheDark";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "AOTD print";
	}

	@Override
	public List getAliases()
	{
		return this.aliases;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length == 1 && args[0].equalsIgnoreCase("print"))
		{
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

				sender.addChatMessage(new ChatComponentText("Wrote all known AOTD Dungeons to " + writeDirectory));

				output.close();
			}
			catch (IOException e)
			{
			}
		}
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender)
	{
		if (!sender.getEntityWorld().isRemote)
		{
			if (sender.getCommandSenderEntity() instanceof EntityPlayer)
			{
				if (MinecraftServer.getServer().getConfigurationManager().getOppedPlayers().getGameProfileFromName(sender.getCommandSenderEntity().getName()) != null || MinecraftServer.getServer().isSinglePlayer())
				{
					return true;
				}
			}
			else if (MinecraftServer.getServer().isDedicatedServer())
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return false;
	}

}
