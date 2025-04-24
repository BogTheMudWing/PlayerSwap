package org.macver.playerswap;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StartCommand {

    @Contract(value = " -> new", pure = true)
    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("start")
                .executes(StartCommand::runStartLogic);
    }

    private static int runStartLogic(CommandContext<CommandSourceStack> ctx) {
        if (!PlayerSwap.isStarted()) {
            PlayerSwap.startGame();
            ctx.getSource().getSender().sendMessage("Game start");
        } else {
            ctx.getSource().getSender().sendRichMessage("<red>Game is already started");
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }
}
