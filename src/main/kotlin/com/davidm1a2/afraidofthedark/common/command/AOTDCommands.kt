package com.davidm1a2.afraidofthedark.common.command

import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.minecraft.command.CommandSource
import net.minecraft.util.text.TextComponentTranslation

/**
 * Class containing all AOTD related commands
 */
object AOTDCommands {
    /**
     * Registers Afraid of the Dark commands into the dispatcher
     */
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            literal<CommandSource>("aotd")
                .then(
                    argument<CommandSource, String>("command", word())
                        .executes {
                            printHelp(it.source)
                            1
                        }
                )
                .executes {
                    printHelp(it.source)
                    1
                }
        )
    }

    /*
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

     */
//
//    /**
//     * Tests if arguments match the expected arguments
//     *
//     * @param args The arguments that the user passed
//     * @param expectedArgs The expected arguments, must be less than or equal to the length of the actual arguments
//     * @param expectedArgCount The expected argument count, can be more than the expected arguments to indicate unknown arguments
//     * @return True if the first n arguments match the n expected arguments
//     */
//    private fun matchArgs(
//        args: Array<String>,
//        vararg expectedArgs: String,
//        expectedArgCount: Int = expectedArgs.size
//    ): Boolean {
//        // Make sure we got the expected argument count
//        if (args.size != expectedArgCount) {
//            return false
//        }
//
//        // Go over each argument, ensure it's correct
//        for (expectedArg in expectedArgs.withIndex()) {
//            if (!args[expectedArg.index].equals(expectedArg.value, ignoreCase = true)) {
//                return false
//            }
//        }
//
//        return true
//    }
//
    /**
     * Simple method to print out the result of the help command
     *
     * @param sender The player who sent the command
     */
    private fun printHelp(sender: CommandSource) {
        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.HELP_HEADER), false)
        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.HELP_HELP), false)
        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.HELP_DUNGEON), false)
    }
//
//    /**
//     * Simple method to print out the result of the dungeon help command
//     *
//     * @param sender The player who sent the command
//     */
//    private fun printDungeonHelp(sender: CommandSource) {
//        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_HELP_HEADER), false)
//        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_HELP_HELP), false)
//        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_HELP_TYPES), false)
//        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_HELP_LIST), false)
//        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_HELP_LIST_TYPE), false)
//        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_HELP_INFO), false)
//        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_HELP_REGENERATE), false)
//    }
//
//    /**
//     * Prints out a list of valid structure types found in AOTD
//     *
//     * @param sender The player who sent the command
//     */
//    private fun printStructureTypes(sender: CommandSource) {
//        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_TYPES), false)
//        // Iterate over structures and print each one out
//        ModRegistries.STRUCTURE.values.forEach { sender.sendFeedback(TextComponentTranslation(it.registryName.toString()), false) }
//    }
//
//    /**
//     * Prints any dungeon information in the player's chunk
//     *
//     * @param sender The player to test dungeon chunk info around
//     */
//    private fun printDungeonInfo(sender: CommandSource) {
//        // This command only works in the overworld
//        if (sender.world.dimension.type == DimensionType.OVERWORLD) {
//            // Grab the chunk the player is in
//            val chunkPos = ChunkPos(BlockPos(sender.pos))
//            // Grab the structure plan
//            val structurePlan = StructurePlan.get(sender.world)!!
//            // If the structure exists print info about the structure, otherwise show no structures exist
//            if (structurePlan.structureExistsAt(chunkPos)) {
//                // Grab the structure at the position
//                val placedStructure = structurePlan.getPlacedStructureAt(chunkPos)!!
//                // Grab structure position
//                val blockPos = placedStructure.structure.getPosition(placedStructure.data)
//                // Send the structure info and if debug is enabled send debug info too
//                sender.sendFeedback(
//                    TextComponentTranslation(
//                        LocalizationConstants.Command.DUNGEON_INFO,
//                        TextComponentTranslation(placedStructure.structure.getUnlocalizedName()),
//                        blockPos.x,
//                        blockPos.y,
//                        blockPos.z
//                    ),
//                    false
//                )
//            } else {
//                sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_INFO_NO_STRUCTURES), false)
//            }
//        } else {
//            sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_INFO_INVALID_WORLD), false)
//        }
//    }
//
//    /**
//     * Regenerates a chunk of the structure that the player is in
//     *
//     * @param sender The player whose chunk to regenerate the structure in
//     */
//    private fun regenerateDungeonChunk(sender: CommandSource) {
//        // This command only works in the overworld
//        if (sender.world.dimension.type == DimensionType.OVERWORLD) {
//            // Grab the chunk the player is in
//            val chunkPos = ChunkPos(BlockPos(sender.pos))
//            // Grab the structure plan
//            val structurePlan = StructurePlan.get(sender.world)!!
//            // If the structure exists print info about the structure, otherwise show no structures exist
//            if (structurePlan.structureExistsAt(chunkPos)) {
//                // Grab the structure at the position
//                val placedStructure = structurePlan.getPlacedStructureAt(chunkPos)!!
//                // Generate the structure in the player's chunk
//                placedStructure.structure.generate(sender.world, chunkPos, placedStructure.data)
//                // Re-light the chunk at the position
//                sender.world.relightChunk(chunkPos)
//            } else {
//                sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_REGENERATE_NO_STRUCTURES), false)
//            }
//        } else {
//            sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_REGENERATE_INVALID_WORLD), false)
//        }
//    }
//
//    /**
//     * Prints out a list of all known structures in a world
//     *
//     * @param sender The player who sent the command
//     * @param server The server used to get the overworld reference
//     */
//    private fun printAllStructures(sender: CommandSource, server: MinecraftServer) {
//        val overworld = server.getWorld(DimensionType.OVERWORLD)
//        sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_LIST_HEADER), false)
//        val structurePlan = StructurePlan.get(overworld)!!
//        // Print all placed structures in the world
//        filterSortAndPrint(structurePlan.getPlacedStructures(), { true }, sender)
//    }
//
//    /**
//     * Prints out a list of all known structures of a specific type in a world
//     *
//     * @param sender        The player who sent the command
//     * @param server        The server used to get the overworld reference
//     * @param structureName The structure the player is looking for
//     */
//    private fun printSpecificStructures(sender: CommandSource, server: MinecraftServer, structureName: String) {
//        // If the structure is invalid tell the user that
//        if (ModRegistries.STRUCTURE.containsKey(ResourceLocation(structureName))) {
//            // Otherwise we list the dungeons of that type
//            val overworld = server.getWorld(DimensionType.OVERWORLD)
//            sender.sendFeedback(
//                TextComponentTranslation(LocalizationConstants.Command.DUNGEON_LIST_TYPE_HEADER, TextComponentTranslation(structureName)),
//                false
//            )
//            val structurePlan = StructurePlan.get(overworld)!!
//            // Go over all placed structures and only print them if they have the right name
//            filterSortAndPrint(
//                structurePlan.getPlacedStructures(),
//                { it.structure.registryName.toString().equals(structureName, ignoreCase = true) },
//                sender
//            )
//        } else {
//            sender.sendFeedback(TextComponentTranslation(LocalizationConstants.Command.DUNGEON_LIST_TYPE_UNKNOWN_TYPE, structureName), false)
//        }
//    }
//
//    /**
//     * Utility function, prints a list of structures that match a given predicate by distance from the player
//     *
//     * @param original The list to sort
//     * @param filter   The filter to apply to the list
//     * @param sender   The sender to send messages to and sort by
//     */
//    private fun filterSortAndPrint(
//        original: List<PlacedStructure>,
//        filter: (PlacedStructure) -> Boolean,
//        sender: CommandSource
//    ) {
//        // Filter the list, sort it by distance to player, and print it out
//        original.filter(filter)
//            .sortedWith(Comparator.comparingDouble {
//                it.structure.getPosition(it.data).distanceSq(sender.pos.x, sender.pos.y, sender.pos.z)
//            })
//            .reversed()
//            .forEach { printStructure(it, sender) }
//    }
//
//    /**
//     * Prints a structure by sending a message to the player
//     *
//     * @param placedStructure The structure to print
//     * @param sender          The player to send the message to
//     */
//    private fun printStructure(placedStructure: PlacedStructure, sender: CommandSource) {
//        val position = placedStructure.structure.getPosition(placedStructure.data)
//        // Send the message in the format: <dungeon_type> at [<x>, <y>, <z>] ~ <number> blocks away
//        sender.sendFeedback(
//            TextComponentTranslation(
//                LocalizationConstants.Command.DUNGEON_LIST,
//                TextComponentTranslation(placedStructure.structure.getUnlocalizedName()),
//                position.x,
//                position.y,
//                position.z,
//                ceil(sqrt(placedStructure.structure.getPosition(placedStructure.data).distanceSq(sender.pos.x, sender.pos.y, sender.pos.z)))
//            ),
//            false
//        )
//    }
}