package com.hbm.commands;

import com.hbm.handler.radiation.ChunkRadiationManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class ChunkRadCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("ntmchunkrad")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.literal("set")
                                .then(Commands.argument("rad", FloatArgumentType.floatArg())
                                        .executes(ChunkRadCommand::setRad)
                                )
                        )
        );
    }

    private static int setRad(CommandContext<CommandSourceStack> context) {

        ServerLevel level = context.getSource().getLevel();
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) return 0;

        float rad = FloatArgumentType.getFloat(context, "rad");
        ChunkRadiationManager.proxy.setRadiation(level, player.blockPosition(), rad);
        context.getSource().sendSuccess(() -> Component.translatable("commands.chunkrad.setrad", rad), true);

        return 1;
    }
}