package com.hbm.commands;

import com.hbm.items.ISatChip;
import com.hbm.saveddata.SatelliteSavedData;
import com.hbm.saveddata.satellite.Satellite;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class SatellitesCommand {

    private static final SuggestionProvider<CommandSourceStack> FREQ_SUGGESTIONS =
            (context, builder) -> {
                SatelliteSavedData data = SatelliteSavedData.get(context.getSource().getLevel());
                data.sats.keySet().forEach(freq -> builder.suggest(String.valueOf(freq)));
                return builder.buildFuture();
            };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("ntmsatellites")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.literal("orbit")
                                .executes(SatellitesCommand::orbit)
                        )
                        .then(Commands.literal("descend")
                                .then(Commands.argument("frequency", IntegerArgumentType.integer())
                                        .suggests(FREQ_SUGGESTIONS)
                                        .executes(SatellitesCommand::descend)
                                )
                        )
                        .then(Commands.literal("list")
                                .executes(SatellitesCommand::listSatellites)
                        )
        );
    }

    private static int orbit(CommandContext<CommandSourceStack> context) {

        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) return 0;

        var stack = player.getMainHandItem();

        if (stack.getItem() instanceof ISatChip) {

            int id = Satellite.getIDFromItem(stack.getItem());
            int freq = ISatChip.getFreqS(stack);
            ServerLevel level = player.serverLevel();

            Satellite.orbit(level, id, freq, player.getX(), player.getY(), player.getZ());
            stack.shrink(1);
            context.getSource().sendSuccess(() -> Component.translatable("commands.satellite.satellite_orbited"), false);

        } else {
            context.getSource().sendFailure(Component.translatable("commands.satellite.not_a_satellite"));
            return 0;
        }

        return 1;
    }

    private static int descend(CommandContext<CommandSourceStack> context) {

        CommandSourceStack source = context.getSource();
        ServerLevel level = source.getLevel();
        int freq = IntegerArgumentType.getInteger(context, "frequency");

        SatelliteSavedData data = SatelliteSavedData.get(level);

        if (data.sats.containsKey(freq)) {
            data.sats.remove(freq);
            data.setDirty();

            source.sendSuccess(() -> Component.translatable("commands.satellite.satellite_descended"), true);
        } else {
            source.sendFailure(Component.translatable("commands.satellite.no_satellite"));
            return 0;
        }

        return 1;
    }

    private static int listSatellites(CommandContext<CommandSourceStack> context) {

        CommandSourceStack source = context.getSource();
        ServerLevel level = source.getLevel();

        SatelliteSavedData data = SatelliteSavedData.get(level);

        if (data.sats.isEmpty()) {
            source.sendFailure(Component.translatable("commands.satellite.no_active_satellites"));
            return 0;
        }

        data.sats.forEach(
                (freq, sat) ->
                source.sendSuccess(() -> Component.literal(freq + " - " + sat.getClass().getSimpleName()), false)
        );

        return 1;
    }
}