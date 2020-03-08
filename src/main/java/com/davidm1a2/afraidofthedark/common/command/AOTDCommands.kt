package com.davidm1a2.afraidofthedark.common.command

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.world.PlacedStructure
import com.davidm1a2.afraidofthedark.common.capabilities.world.StructurePlan
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.worldGeneration.relightChunk
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
class AOTDCommands : CommandBase() {
    /**
     * @return The name of the command
     */
    override fun getName(): String {
        return "${Constants.MOD_ID}_commands"
    }

    /**
     * Returns the usage of the command
     *
     * @param sender The command sender
     * @return The usage of this command
     */
    override fun getUsage(sender: ICommandSender): String {
        return "/aotd <text>"
    }

    /**
     * @return A list of possible command aliases
     */
    override fun getAliases(): List<String> {
        return listOf("aotd", "afraidofthedark")
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
    override fun getTabCompletions(
        server: MinecraftServer,
        sender: ICommandSender,
        args: Array<String>,
        targetPos: BlockPos?
    ): List<String> {
        // Commands can start with /aotd, or /afraidofthedark
        return when {
            args.isEmpty() -> {
                getListOfStringsMatchingLastWord(args, "aotd", "afraidofthedark")
            }
            matchArgs(args, expectedArgCount = 1) -> {
                getListOfStringsMatchingLastWord(args, "help", "dungeon")
            }
            matchArgs(args, "dungeon", expectedArgCount = 2) -> {
                getListOfStringsMatchingLastWord(args, "list", "types", "info", "regenerate")
            }
            matchArgs(args, "dungeon", "list", expectedArgCount = 3) -> {
                // Valid structure ids
                val structureNames = ModRegistries.STRUCTURE.valuesCollection
                    .map { it.registryName.toString() }
                    .toTypedArray()
                getListOfStringsMatchingLastWord(args, *structureNames)
            }
            else -> {
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
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        when {
            // /aotd or /aotd help
            args.isEmpty() || matchArgs(args, "help") -> {
                printHelp(sender)
            }
            // /aotd dungeon
            matchArgs(args, "dungeon") -> {
                printDungeonHelp(sender)
            }
            // /aotd dungeon info
            matchArgs(args, "dungeon", "info") -> {
                printDungeonInfo(sender)
            }
            // /aotd dungeon regenerate
            matchArgs(args, "dungeon", "regenerate") -> {
                regenerateDungeonChunk(sender)
            }
            // /aotd dungeon types
            matchArgs(args, "dungeon", "types") -> {
                printStructureTypes(sender)
            }
            // /aotd dungeon list
            matchArgs(args, "dungeon", "list") -> {
                printAllStructures(sender, server)
            }
            // /aotd dungeon list <type>
            matchArgs(args, "dungeon", "list", expectedArgCount = 3) -> {
                printSpecificStructures(sender, server, args[2])
            }
        }
    }

    /**
     * Tests if arguments match the expected arguments
     *
     * @param args The arguments that the user passed
     * @param expectedArgs The expected arguments, must be less than or equal to the length of the actual arguments
     * @param expectedArgCount The expected argument count, can be more than the expected arguments to indicate unknown arguments
     * @return True if the first n arguments match the n expected arguments
     */
    private fun matchArgs(
        args: Array<String>,
        vararg expectedArgs: String,
        expectedArgCount: Int = expectedArgs.size
    ): Boolean {
        // Make sure we got the expected argument count
        if (args.size != expectedArgCount) {
            return false
        }

        // Go over each argument, ensure it's correct
        for (expectedArg in expectedArgs.withIndex()) {
            if (!args[expectedArg.index].equals(expectedArg.value, ignoreCase = true)) {
                return false
            }
        }

        return true
    }

    /**
     * Simple method to print out the result of the help command
     *
     * @param sender The player who sent the command
     */
    private fun printHelp(sender: ICommandSender) {
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.help.header"))
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.help.help"))
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.help.dungeon"))
    }

    /**
     * Simple method to print out the result of the dungeon help command
     *
     * @param sender The player who sent the command
     */
    private fun printDungeonHelp(sender: ICommandSender) {
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.help.header"))
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.help.help"))
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.help.types"))
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.help.list"))
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.help.list.type"))
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.help.info"))
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.help.regenerate"))
    }

    /**
     * Prints out a list of valid structure types found in AOTD
     *
     * @param sender The player who sent the command
     */
    private fun printStructureTypes(sender: ICommandSender) {
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.types"))
        // Iterate over structures and print each one out
        ModRegistries.STRUCTURE.valuesCollection.forEach { sender.sendMessage(TextComponentString(it.registryName.toString())) }
    }

    /**
     * Prints any dungeon information in the player's chunk
     *
     * @param sender The player to test dungeon chunk info around
     */
    private fun printDungeonInfo(sender: ICommandSender) {
        // This command only works in the overworld
        if (sender.entityWorld.provider.dimension == 0) {
            // Grab the chunk the player is in
            val chunkPos = ChunkPos(sender.position)
            // Grab the structure plan
            val structurePlan = StructurePlan.get(sender.entityWorld)!!
            // If the structure exists print info about the structure, otherwise show no structures exist
            if (structurePlan.structureExistsAt(chunkPos)) {
                // Grab the structure at the position
                val placedStructure = structurePlan.getPlacedStructureAt(chunkPos)!!
                // Grab structure position
                val blockPos = placedStructure.structure.getPosition(placedStructure.data)
                // Send the structure info and if debug is enabled send debug info too
                sender.sendMessage(
                    TextComponentTranslation(
                        "message.afraidofthedark:command.dungeon.info",
                        TextComponentTranslation(placedStructure.structure.getUnlocalizedName()),
                        blockPos.x,
                        blockPos.y,
                        blockPos.z
                    )
                )
                if (AfraidOfTheDark.INSTANCE.configurationHandler.debugMessages) {
                    sender.sendMessage(
                        TextComponentTranslation(
                            "message.afraidofthedark:command.dungeon.info.extra_nbt",
                            placedStructure.data.toString()
                        )
                    )
                }
            } else {
                sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.info.no_structures"))
            }
        } else {
            sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.info.invalid_world"))
        }
    }

    /**
     * Regenerates a chunk of the structure that the player is in
     *
     * @param sender The player whose chunk to regenerate the structure in
     */
    private fun regenerateDungeonChunk(sender: ICommandSender) {
        // This command only works in the overworld
        if (sender.entityWorld.provider.dimension == 0) {
            // Grab the chunk the player is in
            val chunkPos = ChunkPos(sender.position)
            // Grab the structure plan
            val structurePlan = StructurePlan.get(sender.entityWorld)!!
            // If the structure exists print info about the structure, otherwise show no structures exist
            if (structurePlan.structureExistsAt(chunkPos)) {
                // Grab the structure at the position
                val placedStructure = structurePlan.getPlacedStructureAt(chunkPos)!!
                // Generate the structure in the player's chunk
                placedStructure.structure.generate(sender.entityWorld, chunkPos, placedStructure.data)
                // Re-light the chunk at the position
                sender.entityWorld.relightChunk(chunkPos)
            } else {
                sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.regenerate.no_structures"))
            }
        } else {
            sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.regenerate.invalid_world"))
        }
    }

    /**
     * Prints out a list of all known structures in a world
     *
     * @param sender The player who sent the command
     * @param server The server used to get the overworld reference
     */
    private fun printAllStructures(sender: ICommandSender, server: MinecraftServer) {
        val overworld = server.getWorld(0)
        sender.sendMessage(TextComponentTranslation("message.afraidofthedark:command.dungeon.list.header"))
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
    private fun printSpecificStructures(sender: ICommandSender, server: MinecraftServer, structureName: String) {
        // If the structure is invalid tell the user that
        if (ModRegistries.STRUCTURE.containsKey(ResourceLocation(structureName))) {
            // Otherwise we list the dungeons of that type
            val overworld = server.getWorld(0)
            sender.sendMessage(
                TextComponentTranslation(
                    "message.afraidofthedark:command.dungeon.list_type.header",
                    TextComponentTranslation(structureName)
                )
            )
            val structurePlan = StructurePlan.get(overworld)!!
            // Go over all placed structures and only print them if they have the right name
            filterSortAndPrint(
                structurePlan.getPlacedStructures(),
                { it.structure.registryName.toString().equals(structureName, ignoreCase = true) },
                sender
            )
        } else {
            sender.sendMessage(
                TextComponentTranslation(
                    "message.afraidofthedark:command.dungeon.list_type.unknown_type",
                    structureName
                )
            )
        }
    }

    /**
     * Utility function, prints a list of structures that match a given predicate by distance from the player
     *
     * @param original The list to sort
     * @param filter   The filter to apply to the list
     * @param sender   The sender to send messages to and sort by
     */
    private fun filterSortAndPrint(
        original: List<PlacedStructure>,
        filter: (PlacedStructure) -> Boolean,
        sender: ICommandSender
    ) {
        // Filter the list, sort it by distance to player, and print it out
        original.filter(filter)
            .sortedWith(Comparator.comparingDouble {
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
    private fun printStructure(placedStructure: PlacedStructure, sender: ICommandSender) {
        val position = placedStructure.structure.getPosition(placedStructure.data)
        // Send the message in the format: <dungeon_type> at [<x>, <y>, <z>] ~ <number> blocks away
        sender.sendMessage(
            TextComponentTranslation(
                "message.afraidofthedark:command.dungeon.list",
                TextComponentTranslation(placedStructure.structure.getUnlocalizedName()),
                position.x,
                position.y,
                position.z,
                ceil(sqrt(sender.position.distanceSq(placedStructure.structure.getPosition(placedStructure.data))))
            )
        )
    }
}