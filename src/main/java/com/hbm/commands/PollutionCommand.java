package com.hbm.commands;

import com.hbm.handler.pollution.PollutionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Locale;

// todo suggestions and make translatable strings
public class PollutionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("ntmpollution")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.literal("set")
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                                .executes(PollutionCommand::setPollution)
                                        )
                                )
                        )
                        .then(Commands.literal("get")
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .executes(PollutionCommand::getPollution)
                                )
                        )
        );
    }

    private static int setPollution(CommandContext<CommandSourceStack> context) {

        ServerLevel level = context.getSource().getLevel();
        ServerPlayer player = context.getSource().getPlayer();
        if(player == null) return 0;

        float amount = FloatArgumentType.getFloat(context, "amount");
        String rad = StringArgumentType.getString(context, "type").toUpperCase(Locale.US);
        PollutionHandler.setPollution(level, player.blockPosition(), PollutionHandler.PollutionType.valueOf(rad), amount);
        context.getSource().sendSuccess(() -> Component.literal("SET POLLUTION TO " + amount), true);

        return 1;
    }

    private static int getPollution(CommandContext<CommandSourceStack> context) {

        ServerLevel level = context.getSource().getLevel();
        ServerPlayer player = context.getSource().getPlayer();
        if(player == null) return 0;

        String type = StringArgumentType.getString(context, "type").toUpperCase(Locale.US);
        PollutionHandler.PollutionType pollutionType = PollutionHandler.PollutionType.valueOf(type);
        PollutionHandler.PollutionData data = PollutionHandler.getPollutionData(level,  player.blockPosition());
        if(data == null) return 0;
        context.getSource().sendSuccess(() -> Component.literal("POLLUTION IS " + data.pollution[pollutionType.ordinal()]), true);

        return 1;
    }
}