package com.hbm.items.tools;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.config.MainConfig;
import com.hbm.interfaces.IBomb;
import com.hbm.interfaces.IBomb.BombReturnCode;
import com.hbm.interfaces.IHoldableWeapon;
import com.hbm.lib.Library;
import com.hbm.lib.ModSounds;
import com.hbm.network.toclient.InformPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class LaserDetonatorItem extends Item implements IHoldableWeapon {

    public LaserDetonatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        for (String s : ITooltipProvider.getDescription(stack)) {
            components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        HitResult hr = Library.rayTrace(player, 500, 1);

        if (hr instanceof BlockHitResult bhr) {
            BlockPos pos = bhr.getBlockPos();
            if (!level.isClientSide) {
                Block block = level.getBlockState(pos).getBlock();
                if (block instanceof IBomb ib) {
                    BombReturnCode ret = ib.explode(level, pos);

                    if (MainConfig.COMMON.ENABLE_EXTENDED_LOGGING.get()) {
                        HBMsNTM.LOGGER.info("[LASER DETONATOR] {} detonated {} at {} / {} / {}!", player.getName().getString(), block.getName().getString(), pos.getX(), pos.getY(), pos.getZ());
                    }

                    level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.TECH_BLEEP.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
                    if (player instanceof ServerPlayer serverPlayer) {
                        PacketDistributor.sendToPlayer(serverPlayer, new InformPlayer(Component.translatable(ret.getUnlocalizedMessage()).withStyle(ret.wasSuccessful() ? ChatFormatting.YELLOW : ChatFormatting.RED), HBMsNTMClient.ID_DETONATOR, 500));
                    }
                } else {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.TECH_BOOP.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
                    if (player instanceof ServerPlayer serverPlayer) {
                        PacketDistributor.sendToPlayer(serverPlayer, new InformPlayer(Component.translatable(BombReturnCode.ERROR_NO_BOMB.getUnlocalizedMessage()).withStyle( ChatFormatting.RED), HBMsNTMClient.ID_DETONATOR, 500));
                    }
                }
            } else {
                Vec3 vec = new Vec3(pos.getX() + 0.5 - player.getX(), pos.getY() + 0.5 - player.getY() + player.getEyeHeight(), pos.getZ() + 0.5 - player.getZ());
                double len = Math.min(vec.length(), 15D);
                vec = vec.normalize();

                for (int i = 0; i < len; i++) {
                    double rand = level.random.nextDouble() * len + 3;
                    level.addParticle(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.0F), player.getX() + vec.x * rand, player.getY() + vec.y * rand, player.getZ() + vec.z * rand, 0, 0, 0);
                }
            }
        }

        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public boolean shouldChangeOffhand() {
        return true;
    }
}
