package com.hbm.blocks.bomb;

import com.hbm.blockentity.bomb.NukePrototypeBlockEntity;
import com.hbm.blockentity.bomb.NukeTsarBombaBlockEntity;
import com.hbm.config.MainConfig;
import com.hbm.entity.logic.NukeExplosionMK3;
import com.hbm.items.NtmItems;
import com.hbm.particle.helper.CloudCreator;
import com.hbm.particle.helper.CloudCreator.CloudType;
import com.hbm.util.InventoryUtil;
import com.hbm.util.ItemStackUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class NukePrototypeBlock extends NukeBaseBlock {

    public NukePrototypeBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<NukePrototypeBlock> CODEC = simpleCodec(NukePrototypeBlock::new);
    @Override protected MapCodec<NukePrototypeBlock> codec() { return CODEC; }

    @Override
    protected void explode(Level level, double x, double y, double z) {
        NukeExplosionMK3 explosion = NukeExplosionMK3.statFacFleija(level, x, y, z, MainConfig.COMMON.PROTOTYPE_RADIUS.get());
        if(!explosion.isRemoved()) {
            level.addFreshEntity(explosion);
            CloudCreator.composeEffect(level, x, y, z, CloudType.FLEIJA);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if(!player.isShiftKeyDown() && stack.is(NtmItems.IGNITER.get())) {
            this.explode(level, pos);

            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NukePrototypeBlockEntity(pos, state);
    }
}
