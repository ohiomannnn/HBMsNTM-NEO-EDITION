package com.hbm.items.food;

import com.hbm.blocks.ITooltipProvider;
import com.hbm.entity.NtmEntityTypes;
import com.hbm.entity.effect.Vortex;
import com.hbm.inventory.MetaHelper;
import com.hbm.items.EnumMultiItem;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import com.hbm.util.EnumUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class ConserveItem extends EnumMultiItem {

    // we need more fancy enum lambdas and method references!!!
    public enum ConserveType {
        BEEF(8, 0.75F),
        TUNA(4, 0.75F),
        MYSTERY(6, 0.5F),
        PASHTET(4, 0.5F),
        CHEESE(3, 1F),
        SLIME(15, 5F),
        MILK(5, 0.25F),
        ASS(6, 0.75F), // :3
        PIZZA(8, 075F),
        TUBE(2, 0.25F),
        TOMATO(4, 0.5F),
        ASBESTOS(7, 1F),
        BHOLE(LAMBDA_BHOLE, 10, 1F),
        HOTDOGS(5, 0.75F),
        LEFTOVERS(1, 0.1F),
        YOGURT(3, 0.5F),
        STEW(5, 0.5F),
        CHINESE(6, 0.1F),
        OIL(3, 1F),
        FIST(LAMBDA_FIST, 6, 0.75F),
        SPAM(8, 1F),
        FRIED(10, 0.75F),
        NAPALM(6, 1F),
        DIESEL(6, 1F),
        KEROSENE(6, 1F),
        RECURSION(1, 1F),
        BARK(2, 1F);

        @Nullable public final Consumer<LivingEntity> finishUsingItem;

        public final int foodLevel;
        public final float saturation;

        ConserveType(int level, float sat) { this(null, level, sat); }

        ConserveType(@Nullable Consumer<LivingEntity> lambda, int level, float sat) {
            this.finishUsingItem = lambda;
            this.foodLevel = level;
            this.saturation = sat;
        }
    }

    public static final Consumer<LivingEntity> LAMBDA_BHOLE = (eater) -> {
        Vortex vortex = (Vortex) new Vortex(NtmEntityTypes.VORTEX.get(), eater.level).setShrinkRate(0.01F).setSize(0.5F).noBreak();
        vortex.setPos(eater.position());
        eater.level.addFreshEntity(vortex);
    };
    public static final Consumer<LivingEntity> LAMBDA_FIST = (eater) -> {
        eater.hurt(eater.damageSources().magic(), 2F);
    };

    public ConserveItem(Properties properties) {
        super(properties, ConserveType.class, true, true);
    }

    @Override
    public void registerItemModel(ItemModelProvider provider, ResourceLocation modelLocation) {
        Enum<?>[] enums = theEnum.getEnumConstants();

        ItemModelBuilder builder = provider.getBuilder(modelLocation.toString());

        for(int i = 0; i < enums.length; i++) {
            Enum<?> num = enums[i];

            builder.override()
                    .predicate(NuclearTechMod.withDefaultNamespace("item_meta"), i)
                    .model(provider.getBuilder(modelLocation.getPath() + "_" + i)
                            .parent(new ModelFile.UncheckedModelFile("item/generated"))
                            .texture("layer0", ResourceLocation.fromNamespaceAndPath(modelLocation.getNamespace(), "item/canned" + "_" + num.name().toLowerCase(Locale.US))))
                    .end();
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        for(String s : ITooltipProvider.getDescription(stack)) components.add(Component.literal(s).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        Enum<?> num = EnumUtil.grabEnumSafely(theEnum, MetaHelper.getMeta(stack));
        return "item.hbmsntm.canned_" + num.name().toLowerCase(Locale.US);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity eater) {
        Player player = eater instanceof Player ? (Player) eater : null;
        ConserveType type = EnumUtil.grabEnumSafely(ConserveType.class, MetaHelper.getMeta(stack));
        if(player != null) {
            player.getFoodData().eat(type.foodLevel, type.saturation);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            player.getInventory().add(new ItemStack(NtmItems.CAN_KEY.get()));

            if(player instanceof ServerPlayer serverPlayer) CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
        }
        if(!level.isClientSide) if(type.finishUsingItem != null) type.finishUsingItem.accept(eater);
        level.playSound(null, eater.getX(), eater.getY(), eater.getZ(), eater.getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
        stack.consume(1, eater);
        // hack
        if(type == ConserveType.RECURSION && level.random.nextInt(10) > 0) stack.grow(1);
        eater.gameEvent(GameEvent.EAT);
        return stack;
    }

    @Override public int getUseDuration(ItemStack stack, LivingEntity entity) { return 32; }

    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.EAT; }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(player.canEat(false)) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }
}
