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
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Blocks
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
            literal<CommandSourceStack>("aotd")
                .then(literal<CommandSourceStack>("help").executesDefault {
                    printHelp(it.source)
                })
                .then(
                    literal<CommandSourceStack>("debug")
                        .then(
                            literal<CommandSourceStack>("generateRaw")
                                .then(
                                    argument<CommandSourceStack, String>("schematic", word())
                                        .suggests { _, builder -> SharedSuggestionProvider.suggest(ModSchematics.LIST.map { it.getName() }, builder) }
                                        .executesDefault { generateSchematic(it.source, ModSchematics.NAME_TO_SCHEMATIC[getString(it, "schematic")], false) })
                        )
                        .then(
                            literal<CommandSourceStack>("generateRawMarker")
                                .then(
                                    argument<CommandSourceStack, String>("schematic", word())
                                        .suggests { _, builder -> SharedSuggestionProvider.suggest(ModSchematics.LIST.map { it.getName() }, builder) }
                                    .executesDefault { generateSchematic(it.source, ModSchematics.NAME_TO_SCHEMATIC[getString(it, "schematic")], true) })
                        )
                        .then(
                            literal<CommandSourceStack>("fillVoids").executesDefault { updateStructureVoids(it.source, true) }
                        )
                        .then(
                            literal<CommandSourceStack>("removeVoids").executesDefault { updateStructureVoids(it.source, false) }
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

    private fun printHelp(sender: CommandSourceStack) {
        sender.sendSuccess(TranslatableComponent("message.afraidofthedark.command.help.header"), false)
        sender.sendSuccess(TranslatableComponent("message.afraidofthedark.command.help.help"), false)
    }

    private fun generateSchematic(sender: CommandSourceStack, schematic: Schematic?, includeCornerMarkers: Boolean) {
        val entity = sender.entity
        val world = sender.level

        if (!canExecuteDebugCommands(sender)) {
            return
        }

        if (schematic == null) {
            sender.sendFailure(TextComponent("Schematic was not found"))
            return
        }

        SchematicUtils.placeRawSchematic(schematic, world, entity!!.blockPosition())
        if (includeCornerMarkers) {
            val db = Blocks.DIAMOND_BLOCK.defaultBlockState()
            world.setBlockAndUpdate(entity.blockPosition(), db)
            world.setBlockAndUpdate(entity.blockPosition().offset(schematic.getWidth() - 1, schematic.getHeight() - 1, schematic.getLength() - 1), db)
        }
    }

    private fun updateStructureVoids(sender: CommandSourceStack, makeVoids: Boolean) {
        val entity = sender.entity
        val world = sender.level

        if (!canExecuteDebugCommands(sender)) {
            return
        }

        SchematicUtils.updateStructureVoids(world, entity!!.blockPosition(), makeVoids)
    }

    private fun canExecuteDebugCommands(sender: CommandSourceStack): Boolean {
        val entity = sender.entity

        if (entity == null) {
            sender.sendFailure(TextComponent("Command sender entity cannot be null"))
            return false
        }

        if (entity !is Player || !DEVELOPERS.contains(entity.gameProfile.name)) {
            sender.sendFailure(TextComponent("Debug commands can only be used by developers"))
            return false
        }

        return true
    }

    companion object {
        private val DEVELOPERS = setOf("David_M1A2", "namcap623", "Rheapr")
    }
}