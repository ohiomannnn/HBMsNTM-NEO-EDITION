package com.hbm.items.tools;

import com.hbm.entity.logic.Bomber;
import com.hbm.lib.Library;
import com.hbm.lib.ModSounds;
import com.hbm.util.RayTraceResult;
import com.hbm.util.i18n.I18nUtil;
import com.hbm.world.WorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class BombCallerItem extends Item {

    private BomberType type;

    public BombCallerItem(Properties properties, BomberType type) {
        super(properties);
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            RayTraceResult ray = Library.rayTrace(player, 500, 1);
            int x = ray.getBlockPos().getX();
            int y = ray.getBlockPos().getY();
            int z = ray.getBlockPos().getZ();

            Bomber bomber = switch (type) {
                case NAPALM -> Bomber.statFacNapalm(level, x, y, z);
                case ATOMIC_BOMB -> Bomber.statFacABomb(level, x, y, z);
                default -> Bomber.statFacCarpet(level, x, y, z);
            };

            WorldUtil.loadAndSpawnEntityInWorld(bomber);

            player.sendSystemMessage(Component.translatable("item.hbmsntm.bomb_caller.message.call"));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.TECH_BLEEP, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.isCreative()) {
                stack.setCount(stack.getCount() - 1);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        for (String s : I18nUtil.resolveKeyArray("item.hbmsntm.bomb_caller.desc")) {
            components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }
        switch (type) {
            case CARPET -> components.add(Component.translatable("item.hbmsntm.bomb_caller.desc.carpet").withStyle(ChatFormatting.GRAY));
            case NAPALM -> components.add(Component.translatable("item.hbmsntm.bomb_caller.desc.napalm").withStyle(ChatFormatting.GRAY));
            case ATOMIC_BOMB -> components.add(Component.translatable("item.hbmsntm.bomb_caller.desc.atomic").withStyle(ChatFormatting.GRAY));
        };
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || this.type == BomberType.ATOMIC_BOMB;
    }

    public enum BomberType {
        NAPALM,
        CARPET,
        ATOMIC_BOMB
    }
}
