package org.macver.playerswap;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StopCommand {

    @Contract(value = " -> new", pure = true)
    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("stop")
                .executes(StopCommand::runStartLogic);
    }

    private static int runStartLogic(CommandContext<CommandSourceStack> ctx) {
        if (PlayerSwap.isStarted()) {
            PlayerSwap.stopGame();
            ctx.getSource().getSender().sendMessage("Game stop");
        } else {
            ctx.getSource().getSender().sendRichMessage("<red>Game is already stopped");
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }
}
