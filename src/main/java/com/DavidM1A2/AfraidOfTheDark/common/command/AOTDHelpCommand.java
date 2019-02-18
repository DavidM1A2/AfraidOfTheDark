package com.DavidM1A2.afraidofthedark.common.command;

import com.google.common.collect.ImmutableList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Class representing the /aotd help command to list all AOTD related commands
 */
public class AOTDHelpCommand extends CommandBase
{
    /**
     * @return The name of the command
     */
    @Override
    public String getName()
    {
        return "aotd_help";
    }

    /**
     * Returns the usage of the command
     *
     * @param sender The command sender
     * @return The usage of this command
     */
    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/aotd help";
    }

    /**
     * @return A list of possible command aliases
     */
    @Override
    public List<String> getAliases()
    {
        return ImmutableList.of("aotd", "AOTD", "afraidofthedark");
    }

    /**
     * Returns a list of strings that match the help command
     *
     * @param server The minecraft server
     * @param sender The command sender
     * @param args The current arguments
     * @param targetPos The position of the player
     * @return The list of possible command tab completions
     */
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        // Help is a possible match
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, "help");
        else
            return Collections.emptyList();
    }

    /**
     * Executes the command
     *
     * @param server The server that the command is executed on
     * @param sender The command sender
     * @param args The arguments the command is executed with
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        // If there are no arguments or only 1 argument that is 'help' show the help message
        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help")))
        {
            sender.sendMessage(new TextComponentString("/AOTD, /aotd, and /AfraidOfTheDark both work for all commands:"));
            sender.sendMessage(new TextComponentString("/AOTD help - Lists all AOTD Commands"));
        }
    }
}
