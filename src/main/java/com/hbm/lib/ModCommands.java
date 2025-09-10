package com.hbm.lib;

import com.hbm.extprop.HbmLivingProps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ModCommands {

    private static final SuggestionProvider<CommandSourceStack> NTMEntityFields_SUGGESTION =
            (context, builder) -> {
                builder.suggest("radiation");
                builder.suggest("digamma");
                builder.suggest("asbestos");
                builder.suggest("blacklung");
                builder.suggest("oil");
                builder.suggest("fire");
                builder.suggest("phosphorus");
                builder.suggest("balefire");
                builder.suggest("blackfire");
                return builder.buildFuture();
            };

    public static void registerCommandNTMEntityFields(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("NTMEntityFields")
                        .requires(src -> src.hasPermission(4))
                        /// ===== GET ===== ///
                        .then(Commands.literal("get")
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .then(Commands.argument("field", StringArgumentType.word())
                                                .suggests(NTMEntityFields_SUGGESTION)
                                                .executes(context -> {
                                                    LivingEntity target;
                                                    Entity targetEntity = EntityArgument.getEntity(context, "target");
                                                    if (targetEntity instanceof LivingEntity livingEntity) {
                                                        target = livingEntity;
                                                    } else {
                                                        context.getSource().sendFailure(Component.literal("Target is not a living entity!"));
                                                        return 0;
                                                    }
                                                    String field = StringArgumentType.getString(context, "field");
                                                    HbmLivingProps data = HbmLivingProps.getData(target);

                                                    String value;
                                                    switch (field.toLowerCase()) {
                                                        case "radiation" -> value = String.valueOf(HbmLivingProps.getRadiation(target));
                                                        case "digamma" -> value = String.valueOf(HbmLivingProps.getDigamma(target));
                                                        case "asbestos" -> value = String.valueOf(HbmLivingProps.getAsbestos(target));
                                                        case "blacklung" -> value = String.valueOf(HbmLivingProps.getBlackLung(target));
                                                        case "oil" -> value = String.valueOf(HbmLivingProps.getOil(target));
                                                        case "fire" -> value = String.valueOf(data.fire);
                                                        case "phosphorus" -> value = String.valueOf(data.phosphorus);
                                                        case "balefire" -> value = String.valueOf(data.balefire);
                                                        case "blackfire" -> value = String.valueOf(data.blackFire);
                                                        default -> {
                                                            context.getSource().sendFailure(Component.literal("Unknown field: " + field));
                                                            return 0;
                                                        }
                                                    }

                                                    context.getSource().sendSuccess(
                                                            () -> Component.literal("Field '" + field + "' at " + target.getName().getString() +
                                                                    " = " + value),
                                                            false
                                                    );
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        /// ===== ADD ===== ///
                        .then(Commands.literal("add")
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .then(Commands.argument("field", StringArgumentType.word())
                                                .suggests(NTMEntityFields_SUGGESTION)
                                                .then(Commands.argument("delta", FloatArgumentType.floatArg())
                                                        .executes(context -> {
                                                            LivingEntity target;
                                                            Entity targetEntity = EntityArgument.getEntity(context, "target");
                                                            if (targetEntity instanceof LivingEntity livingEntity) {
                                                                target = livingEntity;
                                                            } else {
                                                                context.getSource().sendFailure(Component.literal("Target is not a living entity!"));
                                                                return 0;
                                                            }
                                                            String field = StringArgumentType.getString(context, "field");
                                                            float delta = FloatArgumentType.getFloat(context, "delta");
                                                            HbmLivingProps data = HbmLivingProps.getData(target);

                                                            switch (field.toLowerCase()) {
                                                                case "radiation" -> HbmLivingProps.setRadiation(target, HbmLivingProps.getRadiation(target) + delta);
                                                                case "digamma" -> HbmLivingProps.setDigamma(target, HbmLivingProps.getDigamma(target) + delta);
                                                                case "asbestos" -> HbmLivingProps.setAsbestos(target, HbmLivingProps.getAsbestos(target) + (int) delta);
                                                                case "blacklung" -> HbmLivingProps.setBlackLung(target, HbmLivingProps.getBlackLung(target) + (int) delta);
                                                                case "oil" -> HbmLivingProps.setOil(target, HbmLivingProps.getOil(target) + (int) delta);
                                                                case "fire" -> data.fire += (int) delta;
                                                                case "phosphorus" -> data.phosphorus += (int) delta;
                                                                case "balefire" -> data.balefire += (int) delta;
                                                                case "blackfire" -> data.blackFire += (int) delta;
                                                                default -> {
                                                                    context.getSource().sendFailure(Component.literal("Unknown field: " + field));
                                                                    return 0;
                                                                }
                                                            }

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal("Field '" + field + "' at " + target.getName().getString() +
                                                                            " got changed by " + delta),
                                                                    true
                                                            );
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
        );

    }
}