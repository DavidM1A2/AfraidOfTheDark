package com.davidm1a2.afraidofthedark.common.command

import com.davidm1a2.afraidofthedark.common.constants.ModStructures
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.minecraft.command.CommandSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.util.text.event.ClickEvent
import net.minecraft.util.text.event.HoverEvent
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Class containing all AOTD related commands
 */
object AOTDCommands {
    private val FAILED_EXCEPTION = SimpleCommandExceptionType(TranslationTextComponent("commands.locate.failed"))

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
                    thenForAll(
                        literal<CommandSource>("locate").executesDefault { printLocateHelp(it.source) },
                        ModStructures.STRUCTURES.map {
                            literal<CommandSource>(it.structureName.removePrefix("afraidofthedark:").capitalize())
                        }
                    ) {
                        printStructureLocate(it.source, it.nodes.map { node -> node.node.name }.last())
                    }
                )
                .executesDefault { printHelp(it.source) }
        )
    }

    private fun <S, T : ArgumentBuilder<S, T>> thenForAll(
        base: ArgumentBuilder<S, T>,
        args: List<ArgumentBuilder<S, T>>,
        block: (CommandContext<S>) -> Unit
    ): T {
        if (args.isEmpty()) {
            throw IllegalArgumentException("Can't have a then clause with no args")
        }
        var result: T? = null
        args.forEach { result = base.then(it.executesDefault(block)) }
        return result!!
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
        sender.sendFeedback(TranslationTextComponent("message.afraidofthedark.command.help.locate"), false)
    }

    private fun printLocateHelp(sender: CommandSource) {
        sender.sendFeedback(TranslationTextComponent("message.afraidofthedark.command.help.locate.help"), false)
    }

    private fun printStructureLocate(sender: CommandSource, structureName: String) {
        val structureRegistryName = "afraidofthedark:${structureName.decapitalize()}"
        val position = sender.world.findNearestStructure(structureRegistryName, BlockPos(sender.pos), 100, false)
        if (position == null) {
            throw FAILED_EXCEPTION.create()
        } else {
            val xDiff = position.x.toDouble() - sender.pos.x
            val zDiff = position.z.toDouble() - sender.pos.z
            val distance = sqrt(xDiff * xDiff + zDiff * zDiff).roundToInt()
            val textComponent = TextComponentUtils.wrapInSquareBrackets(
                TranslationTextComponent("chat.coordinates", position.x, "~", position.z)
            ).applyTextStyle {
                it.color = TextFormatting.GREEN
                it.clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s ${position.x} ~ ${position.z}")
                it.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, TranslationTextComponent("chat.coordinates.tooltip"))
            }
            sender.sendFeedback(TranslationTextComponent("commands.locate.success", structureName, textComponent, distance), false)
        }
    }
}