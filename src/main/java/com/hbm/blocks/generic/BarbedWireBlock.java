package com.hbm.blocks.generic;

import com.hbm.blocks.IMultiBlock;
import com.hbm.inventory.MetaHelper;
import com.hbm.lib.ModEffect;
import com.hbm.util.ArmorUtil;
import com.hbm.util.EnumUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class BarbedWireBlock extends Block implements IMultiBlock {

    public enum BarbedWireType {
        STANDARD(LAMBDA_STANDARD),
        FIRE(LAMBDA_FIRE),
        POISON(LAMBDA_POISON),
        ACID(LAMBDA_ACID),
        WITHER(LAMBDA_WITHER),
        ULTRADEATH(LAMBDA_ULTRADEATH);

        public final Consumer<Entity> entityInside;

        BarbedWireType(Consumer<Entity> lambda) {
            this.entityInside = lambda;
        }
    }

    public static final Consumer<Entity> LAMBDA_STANDARD = (entity) -> {
        entity.hurt(entity.damageSources().cactus(), 2F);
    };
    public static final Consumer<Entity> LAMBDA_FIRE = (entity) -> {
        entity.hurt(entity.damageSources().cactus(), 2F);
        entity.igniteForSeconds(1);
    };
    public static final Consumer<Entity> LAMBDA_POISON = (entity) -> {
        entity.hurt(entity.damageSources().cactus(), 2F);
        if(entity instanceof LivingEntity living) living.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 2));
    };
    public static final Consumer<Entity> LAMBDA_ACID = (entity) -> {
        entity.hurt(entity.damageSources().cactus(), 2F);
        if(entity instanceof LivingEntity living) {
            ArmorUtil.damageSuit(living, EquipmentSlot.HEAD, 1);
            ArmorUtil.damageSuit(living, EquipmentSlot.CHEST, 1);
            ArmorUtil.damageSuit(living, EquipmentSlot.LEGS, 1);
            ArmorUtil.damageSuit(living, EquipmentSlot.FEET, 1);
        }
    };
    public static final Consumer<Entity> LAMBDA_WITHER = (entity) -> {
        entity.hurt(entity.damageSources().cactus(), 2F);
        if(entity instanceof LivingEntity living) living.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 4));
    };
    public static final Consumer<Entity> LAMBDA_ULTRADEATH = (entity) -> {
        entity.hurt(entity.damageSources().cactus(), 5F); // todo change cactus to pc damage type
        if(entity instanceof LivingEntity living) living.addEffect(new MobEffectInstance(ModEffect.RADIATION, 100, 9));
    };

    public static final IntegerProperty SUBTYPE = IntegerProperty.create("subtype", 0, BarbedWireType.values().length);
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BarbedWireBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SUBTYPE, 0)
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SUBTYPE, HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(SUBTYPE, MetaHelper.getMeta(context.getItemInHand()))
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        MetaHelper.setMeta(stack, rectify(this.getMeta(state)));
        return stack;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, Builder params) {
        List<ItemStack> droppedStacks = super.getDrops(state, params);
        droppedStacks.forEach(stack -> MetaHelper.setMeta(stack, rectify(this.getMeta(state))));
        return droppedStacks;
    }

    @Override
    public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return PathType.DAMAGE_OTHER;
    }

    @Override
    public @Nullable PathType getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, PathType originalType) {
        return PathType.DANGER_OTHER;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.15, 0.1, 0.15));

        BarbedWireType type = EnumUtil.grabEnumSafely(BarbedWireType.class, this.getMeta(state));
        type.entityInside.accept(entity);
    }

    @Override
    public String getItemDescriptionId(ItemStack stack) {
        Enum<?> num = EnumUtil.grabEnumSafely(BarbedWireType.class, MetaHelper.getMeta(stack));
        return super.getDescriptionId() + "." + num.name().toLowerCase(Locale.US);
    }

    @Override public int getMeta(BlockState state) { return state.getValue(SUBTYPE); }
    @Override public int getSubCount() { return BarbedWireType.values().length; }
}
