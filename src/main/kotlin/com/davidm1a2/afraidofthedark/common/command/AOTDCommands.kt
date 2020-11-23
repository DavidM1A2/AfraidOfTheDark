package com.davidm1a2.afraidofthedark.common.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.util.text.TranslationTextComponent

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
                .then(literal<CommandSource>("help").executesDefault {
                    printHelp(it.source)
                })
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
}