package com.davidm1a2.afraidofthedark.common.command

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.world.PlacedStructure
import com.davidm1a2.afraidofthedark.common.capabilities.world.StructurePlan
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import java.util.*
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * Class containing all AOTD related commands
 */
class AOTDCommands : CommandBase()
{
    /**
     * @return The name of the command
     */
    override fun getName(): String
    {
        return "${Constants.MOD_ID}_commands"
    }

    /**
     * Returns the usage of the command
     *
     * @param sender The command sender
     * @return The usage of this command
     */
    override fun getUsage(sender: ICommandSender): String
    {
        return "/aotd <text>"
    }

    /**
     * @return A list of possible command aliases
     */
    override fun getAliases(): List<String>
    {
        return listOf("aotd", "AOTD", "afraidofthedark")
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
    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String>
    {
        // Commands can start with /aotd, /AOTD, or /afraidofthedark
        return when
        {
            args.isEmpty() ->
            {
                getListOfStringsMatchingLastWord(args, "aotd", "AOTD", "afraidofthedark")
            }
            args.size == 1 ->
            {
                getListOfStringsMatchingLastWord(args, "help", "dungeon")
            }
            args.size == 2 && args[0].equals("dungeon", ignoreCase = true) ->
            {
                getListOfStringsMatchingLastWord(args, "list", "types", "info", "regenerate")
            }
            args.size == 3 && args[0].equals("dungeon", ignoreCase = true) && args[1].equals("list", ignoreCase = true) ->
            {
                // Valid structure ids
                val structureNames = ModRegistries.STRUCTURE.valuesCollection
                        .map { it.registryName.toString() }
                        .toTypedArray()
                getListOfStringsMatchingLastWord(args, *structureNames)
            }
            else ->
            {
                emptyList()
            }
        }
    }

    /**
     * Executes the command
     *
     * @param server The server that the command is executed on
     * @param sender The command sender
     * @param args   The arguments the command is executed with
     */
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>)
    {
        when
        {
            // /aotd or /aotd help
            args.isEmpty() || args.size == 1 && args[0].equals("help", ignoreCase = true) ->
            {
                printHelp(sender)
            }
            // /aotd dungeon
            args.size == 1 && args[0].equals("dungeon", ignoreCase = true) ->
            {
                printDungeonHelp(sender)
            }
            // /aotd dungeon info
            args.size == 2 && args[0].equals("dungeon", ignoreCase = true) && args[1].equals("info", ignoreCase = true) ->
            {
                printDungeonInfo(sender)
            }
            // /aotd dungeon regenerate
            args.size == 2 && args[0].equals("dungeon", ignoreCase = true) && args[1].equals("regenerate", ignoreCase = true) ->
            {
                regenerateDungeonChunk(sender)
            }
            // /aotd dungeon types
            args.size == 2 && args[0].equals("dungeon", ignoreCase = true) && args[1].equals("types", ignoreCase = true) ->
            {
                printStructureTypes(sender)
            }
            // /aotd dungeon list
            args.size == 2 && args[0].equals("dungeon", ignoreCase = true) && args[1].equals("list", ignoreCase = true) ->
            {
                printAllStructures(sender, server)
            }
            // /aotd dungeon list <type>
            args.size == 3 && args[0].equals("dungeon", ignoreCase = true) && args[1].equals("list", ignoreCase = true) ->
            {
                printSpecificStructures(sender, server, args[2])
            }
        }
    }

    /**
     * Simple method to print out the result of the help command
     *
     * @param sender The player who sent the command
     */
    private fun printHelp(sender: ICommandSender)
    {
        sender.sendMessage(TextComponentTranslation("aotd.command.help.header"))
        sender.sendMessage(TextComponentTranslation("aotd.command.help.help"))
        sender.sendMessage(TextComponentTranslation("aotd.command.help.dungeon"))
    }

    /**
     * Simple method to print out the result of the dungeon help command
     *
     * @param sender The player who sent the command
     */
    private fun printDungeonHelp(sender: ICommandSender)
    {
        sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.help.header"))
        sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.help.help"))
        sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.help.types"))
        sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.help.list"))
        sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.help.list_type"))
        sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.help.info"))
        sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.help.regenerate"))
    }

    /**
     * Prints out a list of valid structure types found in AOTD
     *
     * @param sender The player who sent the command
     */
    private fun printStructureTypes(sender: ICommandSender)
    {
        sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.types"))
        // Iterate over structures and print each one out
        ModRegistries.STRUCTURE.valuesCollection.forEach { sender.sendMessage(TextComponentString(it.registryName.toString())) }
    }

    /**
     * Prints any dungeon information in the player's chunk
     *
     * @param sender The player to test dungeon chunk info around
     */
    private fun printDungeonInfo(sender: ICommandSender)
    {
        // This command only works in the overworld
        if (sender.entityWorld.provider.dimension == 0)
        {
            // Grab the chunk the player is in
            val chunkPos = ChunkPos(sender.position)
            // Grab the structure plan
            val structurePlan = StructurePlan.get(sender.entityWorld)!!
            // If the structure exists print info about the structure, otherwise show no structures exist
            if (structurePlan.structureExistsAt(chunkPos))
            {
                // Grab the structure at the position
                val placedStructure = structurePlan.getPlacedStructureAt(chunkPos)!!
                // Grab structure position
                val blockPos = placedStructure.structure.getPosition(placedStructure.data)
                // Send the structure info and if debug is enabled send debug info too
                sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.info",
                        TextComponentTranslation(placedStructure.structure.registryName.toString()),
                        blockPos.x,
                        blockPos.y,
                        blockPos.z))
                if (AfraidOfTheDark.INSTANCE.configurationHandler.showDebugMessages())
                {
                    sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.info.extra_nbt", placedStructure.data.toString()))
                }
            }
            else
            {
                sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.info.no_structures"))
            }
        }
        else
        {
            sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.info.invalid_world"))
        }
    }

    /**
     * Regenerates a chunk of the structure that the player is in
     *
     * @param sender The player whose chunk to regenerate the structure in
     */
    private fun regenerateDungeonChunk(sender: ICommandSender)
    {
        // This command only works in the overworld
        if (sender.entityWorld.provider.dimension == 0)
        {
            // Grab the chunk the player is in
            val chunkPos = ChunkPos(sender.position)
            // Grab the structure plan
            val structurePlan = StructurePlan.get(sender.entityWorld)!!
            // If the structure exists print info about the structure, otherwise show no structures exist
            if (structurePlan.structureExistsAt(chunkPos))
            {
                // Grab the structure at the position
                val placedStructure = structurePlan.getPlacedStructureAt(chunkPos)!!
                // Generate the structure in the player's chunk
                placedStructure.structure.generate(sender.entityWorld, chunkPos, placedStructure.data)
            }
            else
            {
                sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.regenerate.no_structures"))
            }
        }
        else
        {
            sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.regenerate.invalid_world"))
        }
    }

    /**
     * Prints out a list of all known structures in a world
     *
     * @param sender The player who sent the command
     * @param server The server used to get the overworld reference
     */
    private fun printAllStructures(sender: ICommandSender, server: MinecraftServer)
    {
        val overworld = server.getWorld(0)
        sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.list.header"))
        val structurePlan = StructurePlan.get(overworld)!!
        // Print all placed structures in the world
        filterSortAndPrint(structurePlan.getPlacedStructures(), { true }, sender)
    }

    /**
     * Prints out a list of all known structures of a specific type in a world
     *
     * @param sender        The player who sent the command
     * @param server        The server used to get the overworld reference
     * @param structureName The structure the player is looking for
     */
    private fun printSpecificStructures(sender: ICommandSender, server: MinecraftServer, structureName: String)
    {
        // If the structure is invalid tell the user that
        if (ModRegistries.STRUCTURE.containsKey(ResourceLocation(structureName)))
        {
            // Otherwise we list the dungeons of that type
            val overworld = server.getWorld(0)
            sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.list_type.header", TextComponentTranslation(structureName)))
            val structurePlan = StructurePlan.get(overworld)!!
            // Go over all placed structures and only print them if they have the right name
            filterSortAndPrint(
                    structurePlan.getPlacedStructures(),
                    { it.structure.registryName.toString().equals(structureName, ignoreCase = true) },
                    sender)
        }
        else
        {
            sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.list_type.unknown_type", structureName))
        }
    }

    /**
     * Utility function, prints a list of structures that match a given predicate by distance from the player
     *
     * @param original The list to sort
     * @param filter   The filter to apply to the list
     * @param sender   The sender to send messages to and sort by
     */
    private fun filterSortAndPrint(original: List<PlacedStructure>, filter: (PlacedStructure) -> Boolean, sender: ICommandSender)
    {
        // Filter the list, sort it by distance to player, and print it out
        original.filter(filter)
                .sortedWith(Comparator.comparingDouble()
                {
                    it.structure.getPosition(it.data).distanceSq(sender.position)
                })
                .reversed()
                .forEach { printStructure(it, sender) }
    }

    /**
     * Prints a structure by sending a message to the player
     *
     * @param placedStructure The structure to print
     * @param sender          The player to send the message to
     */
    private fun printStructure(placedStructure: PlacedStructure, sender: ICommandSender)
    {
        val position = placedStructure.structure.getPosition(placedStructure.data)
        // Send the message in the format: <dungeon_type> at [<x>, <y>, <z>] ~ <number> blocks away
        sender.sendMessage(TextComponentTranslation("aotd.command.dungeon.list",
                TextComponentTranslation(placedStructure.structure.registryName.toString()),
                position.x,
                position.y,
                position.z,
                ceil(sqrt(sender.position.distanceSq(placedStructure.structure.getPosition(placedStructure.data))))))
    }
}