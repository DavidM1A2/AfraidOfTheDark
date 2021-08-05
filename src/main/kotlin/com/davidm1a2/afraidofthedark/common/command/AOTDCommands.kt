package com.davidm1a2.afraidofthedark.common.command

import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.schematic.Schematic
import com.davidm1a2.afraidofthedark.common.schematic.SchematicUtils
import com.mojang.brigadier.Command
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
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class containing all AOTD related commands
 */
class AOTDCommands {
    /**
     * Registers Afraid of the Dark commands into the dispatcher
     */
    @SubscribeEvent
    fun register(event: RegisterCommandsEvent) {
        event.dispatcher.register(
            literal<CommandSource>("aotd")
                .then(literal<CommandSource>("help").executesDefault {
                    printHelp(it.source)
                })
                .then(
                    literal<CommandSource>("debug")
                        .then(
                            literal<CommandSource>("generateRaw")
                                .then(
                                    argument<CommandSource, String>("schematic", word())
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
        sender.sendSuccess(TranslationTextComponent("message.afraidofthedark.command.help.header"), false)
        sender.sendSuccess(TranslationTextComponent("message.afraidofthedark.command.help.help"), false)
    }

    private fun generateSchematic(sender: CommandSource, schematic: Schematic?, includeCornerMarkers: Boolean) {
        val entity = sender.entity
        val world = sender.level

        if (!canExecuteDebugCommands(sender)) {
            return
        }

        if (schematic == null) {
            sender.sendFailure(StringTextComponent("Schematic was not found"))
            return
        }

        SchematicUtils.placeRawSchematic(schematic, world, entity!!.blockPosition())
        if (includeCornerMarkers) {
            val db = Blocks.DIAMOND_BLOCK.defaultBlockState()
            world.setBlock(entity.blockPosition(), db, 4)
            world.setBlock(entity.blockPosition().offset(schematic.getWidth() - 1, schematic.getHeight() - 1, schematic.getLength() - 1), db, 4)
        }
    }

    private fun updateStructureVoids(sender: CommandSource, makeVoids: Boolean) {
        val entity = sender.entity
        val world = sender.level

        if (!canExecuteDebugCommands(sender)) {
            return
        }

        SchematicUtils.updateStructureVoids(world, entity!!.blockPosition(), makeVoids)
    }

    private fun canExecuteDebugCommands(sender: CommandSource): Boolean {
        val entity = sender.entity

        if (entity == null) {
            sender.sendFailure(StringTextComponent("Command sender entity cannot be null"))
            return false
        }

        if (entity !is PlayerEntity || !DEVELOPERS.contains(entity.gameProfile.name)) {
            sender.sendFailure(StringTextComponent("Debug commands can only be used by developers"))
            return false
        }

        return true
    }

    companion object {
        private val DEVELOPERS = setOf("David_M1A2", "namcap623", "Rheapr")
    }
}