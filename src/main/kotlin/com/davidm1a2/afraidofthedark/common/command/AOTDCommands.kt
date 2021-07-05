package com.davidm1a2.afraidofthedark.common.command

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.schematic.Schematic
import com.davidm1a2.afraidofthedark.common.world.schematic.SchematicUtils
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.context.CommandContext
import net.minecraft.block.Blocks
import net.minecraft.command.CommandSource
import net.minecraft.command.ISuggestionProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TranslationTextComponent

/**
 * Class containing all AOTD related commands
 */
object AOTDCommands {
    private val DEVELOPERS = setOf("David_M1A2", "namcap623", "Rheapr")

    /**
     * Registers Afraid of the Dark commands into the dispatcher
     */
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            literal<CommandSource>("aotd")
                .then(literal<CommandSource>("help").executesDefault {
                    printHelp(it.source)
                })
                .then(
                    literal<CommandSource>("debug")
                        .then(
                            literal<CommandSource>("generateRaw")
                                .then(argument<CommandSource, String>("schematic", word())
                                    .suggests { _, builder -> ISuggestionProvider.suggest(ModSchematics.LIST.map { it.getName() }, builder) }
                                    .executesDefault { generateSchematic(it.source, ModSchematics.NAME_TO_SCHEMATIC[getString(it, "schematic")], false) })
                        )
                        .then(
                            literal<CommandSource>("generateRawMarker")
                                .then(argument<CommandSource, String>("schematic", word())
                                    .suggests { _, builder -> ISuggestionProvider.suggest(ModSchematics.LIST.map { it.getName() }, builder) }
                                    .executesDefault { generateSchematic(it.source, ModSchematics.NAME_TO_SCHEMATIC[getString(it, "schematic")], true) })
                        )
                        .then(
                            literal<CommandSource>("fillVoids").executesDefault { updateStructureVoids(it.source, true) }
                        )
                        .then(
                            literal<CommandSource>("removeVoids").executesDefault { updateStructureVoids(it.source, false) }
                        )
                )
                .executesDefault { printHelp(it.source) }
        )
    }

    private fun <S, T : ArgumentBuilder<S, T>> ArgumentBuilder<S, T>.executesDefault(block: (CommandContext<S>) -> Unit): T {
        return executes {
            block(it)
            Command.SINGLE_SUCCESS
        }
    }

    private fun printHelp(sender: CommandSource) {
        sender.sendFeedback(TranslationTextComponent("message.afraidofthedark.command.help.header"), false)
        sender.sendFeedback(TranslationTextComponent("message.afraidofthedark.command.help.help"), false)
    }

    private fun generateSchematic(sender: CommandSource, schematic: Schematic?, includeCornerMarkers: Boolean) {
        val entity = sender.entity
        val world = sender.world

        if (!canExecuteDebugCommands(sender)) {
            return
        }

        if (schematic == null) {
            sender.sendFeedback(StringTextComponent("Schematic was not found"), false)
            return
        }

        SchematicUtils.placeRawSchematic(schematic, world, entity!!.position)
        if (includeCornerMarkers) {
            val db = Blocks.DIAMOND_BLOCK.defaultState
            world.setBlockState(entity.position, db)
            world.setBlockState(entity.position.add(schematic.getWidth() - 1, schematic.getHeight() - 1, schematic.getLength() - 1), db)
        }
    }

    private fun updateStructureVoids(sender: CommandSource, makeVoids: Boolean) {
        val entity = sender.entity
        val world = sender.world

        if (!canExecuteDebugCommands(sender)) {
            return
        }

        SchematicUtils.updateStructureVoids(world, entity!!.position, makeVoids)
    }

    private fun canExecuteDebugCommands(sender: CommandSource): Boolean {
        val entity = sender.entity

        if (entity == null) {
            sender.sendFeedback(StringTextComponent("Command sender entity cannot be null"), false)
            return false
        }

        if (entity !is PlayerEntity || !DEVELOPERS.contains(entity.gameProfile.name)) {
            sender.sendFeedback(StringTextComponent("Debug commands can only be used by developers"), false)
            return false
        }

        return true
    }
}