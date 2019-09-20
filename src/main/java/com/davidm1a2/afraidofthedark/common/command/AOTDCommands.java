package com.davidm1a2.afraidofthedark.common.command;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.common.capabilities.world.IStructurePlan;
import com.davidm1a2.afraidofthedark.common.capabilities.world.PlacedStructure;
import com.davidm1a2.afraidofthedark.common.capabilities.world.StructurePlan;
import com.davidm1a2.afraidofthedark.common.constants.Constants;
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries;
import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.base.Structure;
import com.google.common.collect.ImmutableList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Class containing all AOTD related commands
 */
public class AOTDCommands extends CommandBase
{
    /**
     * @return The name of the command
     */
    @Override
    public String getName()
    {
        return Constants.MOD_ID + "_commands";
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
        return "/aotd <text>";
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
     * Returns a list of strings that match the command
     *
     * @param server    The minecraft server
     * @param sender    The command sender
     * @param args      The current arguments
     * @param targetPos The position of the player
     * @return The list of possible command tab completions
     */
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        // Commands can start with /aotd, /AOTD, or /afraidofthedark
        if (args.length == 0)
        {
            return getListOfStringsMatchingLastWord(args, "aotd", "AOTD", "afraidofthedark");
        }
        // Next token should be 'help' or 'dungeon'
        else if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "help", "dungeon");
        }
        // Next token should be 'list' or 'types' after 'dungeon'
        else if (args.length == 2 && args[0].equalsIgnoreCase("dungeon"))
        {
            return getListOfStringsMatchingLastWord(args, "list", "types", "info", "regenerate");
        }
        // Third token should be a structure id after 'dungeon list'
        else if (args.length == 3 && args[0].equalsIgnoreCase("dungeon") && args[1].equalsIgnoreCase("list"))
        {
            // Valid structure ids
            String[] structureNames = ModRegistries.STRUCTURE.getValuesCollection().stream().map(structure -> structure.getRegistryName().toString()).toArray(String[]::new);
            return getListOfStringsMatchingLastWord(args, structureNames);
        }
        else
        {
            return Collections.emptyList();
        }
    }

    /**
     * Executes the command
     *
     * @param server The server that the command is executed on
     * @param sender The command sender
     * @param args   The arguments the command is executed with
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        // / or /aotd help
        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help")))
        {
            printHelp(sender);
        }
        // /aotd dungeon
        else if (args.length == 1 && args[0].equalsIgnoreCase("dungeon"))
        {
            printDungeonHelp(sender);
        }
        // /aotd dungeon info
        else if (args.length == 2 && args[0].equalsIgnoreCase("dungeon") && args[1].equalsIgnoreCase("info"))
        {
            printDungeonInfo(sender);
        }
        // /aotd dungeon regenerate
        else if (args.length == 2 && args[0].equalsIgnoreCase("dungeon") && args[1].equalsIgnoreCase("regenerate"))
        {
            regenerateDungeonChunk(sender);
        }
        // /aotd dungeon types
        else if (args.length == 2 && args[0].equalsIgnoreCase("dungeon") && args[1].equalsIgnoreCase("types"))
        {
            printStructureTypes(sender);
        }
        // /aotd dungeon list
        else if (args.length == 2 && args[0].equalsIgnoreCase("dungeon") && args[1].equalsIgnoreCase("list"))
        {
            printAllStructures(sender, server);
        }
        // /aotd dungeon list <type>
        else if (args.length == 3 && args[0].equalsIgnoreCase("dungeon") && args[1].equalsIgnoreCase("list"))
        {
            printSpecificStructures(sender, server, args[2]);
        }
    }

    /**
     * Simple method to print out the result of the help command
     *
     * @param sender The player who sent the command
     */
    private void printHelp(ICommandSender sender)
    {
        sender.sendMessage(new TextComponentTranslation("aotd.command.help.header"));
        sender.sendMessage(new TextComponentTranslation("aotd.command.help.help"));
        sender.sendMessage(new TextComponentTranslation("aotd.command.help.dungeon"));
    }

    /**
     * Simple method to print out the result of the dungeon help command
     *
     * @param sender The player who sent the command
     */
    private void printDungeonHelp(ICommandSender sender)
    {
        sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.help.header"));
        sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.help.help"));
        sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.help.types"));
        sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.help.list"));
        sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.help.list_type"));
        sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.help.info"));
        sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.help.regenerate"));
    }

    /**
     * Prints out a list of valid structure types found in AOTD
     *
     * @param sender The player who sent the command
     */
    private void printStructureTypes(ICommandSender sender)
    {
        sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.types"));
        // Iterate over structures and print each one out
        for (Structure structure : ModRegistries.STRUCTURE)
            sender.sendMessage(new TextComponentString(structure.getRegistryName().toString()));
    }

    /**
     * Prints any dungeon information in the player's chunk
     *
     * @param sender The player to test dungeon chunk info around
     */
    private void printDungeonInfo(ICommandSender sender)
    {
        // This command only works in the overworld
        if (sender.getEntityWorld().provider.getDimension() == 0)
        {
            // Grab the chunk the player is in
            ChunkPos chunkPos = new ChunkPos(sender.getPosition());
            // Grab the structure plan
            IStructurePlan structurePlan = StructurePlan.get(sender.getEntityWorld());
            // If the structure exists print info about the structure, otherwise show no structures exist
            if (structurePlan.structureExistsAt(chunkPos))
            {
                // Grab the structure at the position
                PlacedStructure placedStructure = structurePlan.getPlacedStructureAt(chunkPos);
                // Grab structure position
                BlockPos blockPos = placedStructure.getStructure().getPosition(placedStructure.getData());
                // Send the structure info and if debug is enabled send debug info too
                sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.info",
                        new TextComponentTranslation(placedStructure.getStructure().getRegistryName().toString()),
                        blockPos.getX(),
                        blockPos.getY(),
                        blockPos.getZ()));
                if (AfraidOfTheDark.INSTANCE.getConfigurationHandler().showDebugMessages())
                {
                    sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.info.extra_nbt", placedStructure.getData().toString()));
                }
            }
            else
            {
                sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.info.no_structures"));
            }
        }
        else
        {
            sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.info.invalid_world"));
        }
    }

    /**
     * Regenerates a chunk of the structure that the player is in
     *
     * @param sender The player whose chunk to regenerate the structure in
     */
    private void regenerateDungeonChunk(ICommandSender sender)
    {
        // This command only works in the overworld
        if (sender.getEntityWorld().provider.getDimension() == 0)
        {
            // Grab the chunk the player is in
            ChunkPos chunkPos = new ChunkPos(sender.getPosition());
            // Grab the structure plan
            IStructurePlan structurePlan = StructurePlan.get(sender.getEntityWorld());
            // If the structure exists print info about the structure, otherwise show no structures exist
            if (structurePlan.structureExistsAt(chunkPos))
            {
                // Grab the structure at the position
                PlacedStructure placedStructure = structurePlan.getPlacedStructureAt(chunkPos);
                // Generate the structure in the player's chunk
                placedStructure.getStructure().generate(sender.getEntityWorld(), chunkPos, placedStructure.getData());
            }
            else
            {
                sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.regenerate.no_structures"));
            }
        }
        else
        {
            sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.regenerate.invalid_world"));
        }
    }

    /**
     * Prints out a list of all known structures in a world
     *
     * @param sender The player who sent the command
     * @param server The server used to get the overworld reference
     */
    private void printAllStructures(ICommandSender sender, MinecraftServer server)
    {
        World overworld = server.getWorld(0);
        sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.list.header"));
        IStructurePlan structurePlan = StructurePlan.get(overworld);
        // Print all placed structures in the world
        filterSortAndPrint(structurePlan.getPlacedStructures(), s -> true, sender);
    }

    /**
     * Prints out a list of all known structures of a specific type in a world
     *
     * @param sender        The player who sent the command
     * @param server        The server used to get the overworld reference
     * @param structureName The structure the player is looking for
     */
    private void printSpecificStructures(ICommandSender sender, MinecraftServer server, String structureName)
    {
        // If the structure is invalid tell the user that
        if (ModRegistries.STRUCTURE.containsKey(new ResourceLocation(structureName)))
        {
            // Otherwise we list the dungeons of that type
            World overworld = server.getWorld(0);
            sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.list_type.header", new TextComponentTranslation(structureName)));
            IStructurePlan structurePlan = StructurePlan.get(overworld);
            // Go over all placed structures and only print them if they have the right name
            filterSortAndPrint(
                    structurePlan.getPlacedStructures(),
                    placedStructure -> placedStructure.getStructure().getRegistryName().toString().equalsIgnoreCase(structureName),
                    sender);
        }
        else
        {
            sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.list_type.unknown_type", structureName));
        }
    }

    /**
     * Utility function, prints a list of structures that match a given predicate by distance from the player
     *
     * @param original The list to sort
     * @param filter   The filter to apply to the list
     * @param sender   The sender to send messages to and sort by
     */
    private void filterSortAndPrint(List<PlacedStructure> original, Predicate<PlacedStructure> filter, ICommandSender sender)
    {
        // Filter the list, sort it by distance to player, and print it out
        original.stream()
                .filter(filter)
                .sorted(Comparator.<PlacedStructure>comparingDouble(s -> s.getStructure().getPosition(s.getData()).distanceSq(sender.getPosition())).reversed())
                .forEach(placedStructure -> this.printStructure(placedStructure, sender));
    }

    /**
     * Prints a structure by sending a message to the player
     *
     * @param placedStructure The structure to print
     * @param sender          The player to send the message to
     */
    private void printStructure(PlacedStructure placedStructure, ICommandSender sender)
    {
        BlockPos position = placedStructure.getStructure().getPosition(placedStructure.getData());
        // Send the message in the format: <dungeon_type> at [<x>, <y>, <z>] ~ <number> blocks away
        sender.sendMessage(new TextComponentTranslation("aotd.command.dungeon.list",
                new TextComponentTranslation(placedStructure.getStructure().getRegistryName().toString()),
                position.getX(),
                position.getY(),
                position.getZ(),
                MathHelper.ceil(Math.sqrt(sender.getPosition().distanceSq(placedStructure.getStructure().getPosition(placedStructure.getData()))))));
    }
}
