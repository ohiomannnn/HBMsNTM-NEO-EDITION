package com.hbm.commands;

import com.hbm.extprop.LivingProperties;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class LivingPropsCommand {

    private static final SimpleCommandExceptionType ERROR_NOT_LIVING = new SimpleCommandExceptionType(
            Component.translatable("commands.hbmsntm.error.not_living")
    );

    private static final SuggestionProvider<CommandSourceStack> PROPS_SUGGESTIONS =
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

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("ntmprops")
                        .requires(src -> src.hasPermission(4))
                        // ===== GET =====
                        .then(Commands.literal("get")
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .then(Commands.argument("field", StringArgumentType.word())
                                                .suggests(PROPS_SUGGESTIONS)
                                                .executes(LivingPropsCommand::getValue)
                                        )
                                )
                        )
                        // ===== SET =====
                        .then(Commands.literal("set")
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .then(Commands.argument("field", StringArgumentType.word())
                                                .suggests(PROPS_SUGGESTIONS)
                                                .then(Commands.argument("value", FloatArgumentType.floatArg())
                                                        .executes(LivingPropsCommand::setValue)
                                                )
                                        )
                                )
                        )
        );
    }


    private static LivingEntity getLiving(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(context, "target");
        if (entity instanceof LivingEntity livingEntity) return livingEntity;
        throw ERROR_NOT_LIVING.create();
    }

    private static int getValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        LivingEntity target = getLiving(context);
        String field = StringArgumentType.getString(context, "field");

        float value = switch (field) {
            case "radiation"  -> LivingProperties.getRadiation(target);
            case "digamma"    -> LivingProperties.getDigamma(target);
            case "asbestos"   -> LivingProperties.getAsbestos(target);
            case "blacklung"  -> LivingProperties.getBlackLung(target);
            case "oil"        -> LivingProperties.getOil(target);
            case "fire"       -> LivingProperties.getData(target).fire;
            case "phosphorus" -> LivingProperties.getData(target).phosphorus;
            case "balefire"   -> LivingProperties.getData(target).balefire;
            case "blackfire"  -> LivingProperties.getData(target).blackFire;
            default -> throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
        };

        context.getSource().sendSuccess(() -> Component.translatable("commands.hbmsntm.success.props.get", field, target.getName(), value), false);
        return 1;
    }

    private static int setValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        LivingEntity target = getLiving(context);
        String field = StringArgumentType.getString(context, "field");
        float value = FloatArgumentType.getFloat(context, "value");

        switch (field) {
            case "radiation"  -> LivingProperties.setRadiation(target, value);
            case "digamma"    -> LivingProperties.setDigamma(target, value);
            case "asbestos"   -> LivingProperties.setAsbestos(target, (int) value);
            case "blacklung"  -> LivingProperties.setBlackLung(target, (int) value);
            case "oil"        -> LivingProperties.setOil(target, (int) value);
            case "fire"       -> LivingProperties.getData(target).fire = (int) value;
            case "phosphorus" -> LivingProperties.getData(target).phosphorus = (int) value;
            case "balefire"   -> LivingProperties.getData(target).balefire = (int) value;
            case "blackfire"  -> LivingProperties.getData(target).blackFire = (int) value;
            default -> throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
        }

        context.getSource().sendSuccess(() -> Component.translatable("commands.hbmsntm.success.props.set", field, target.getName(), value), true);
        return 1;
    }
}