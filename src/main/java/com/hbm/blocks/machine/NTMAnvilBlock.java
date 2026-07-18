package com.hbm.blocks.machine;

import com.hbm.blocks.ICustomBlockModelRegister;
import com.hbm.blocks.IMultiBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.blockentity.IScreenProvider;
import com.hbm.inventory.MetaHelper;
import com.hbm.inventory.menus.AnvilMenu;
import com.hbm.inventory.screens.AnvilScreen;
import com.hbm.datagen.NtmBlockStateProvider;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NTMAnvilBlock extends FallingBlock implements IMultiBlock, ICustomBlockModelRegister, IScreenProvider {

    public static final MapCodec<NTMAnvilBlock> CODEC = simpleCodec(NTMAnvilBlock::new);

    public static final int TIER_IRON = 1;
    public static final int TIER_STEEL = 2;
    public static final int TIER_OIL = 3;
    public static final int TIER_NUCLEAR = 4;
    public static final int TIER_RBMK = 5;
    public static final int TIER_FUSION = 6;
    public static final int TIER_PARTICLE = 7;
    public static final int TIER_GERALD = 8;

    public enum Variant {
        IRON(TIER_IRON, "anvil_iron", "anvil_iron"),
        LEAD(TIER_IRON, "anvil_lead", "anvil_lead"),
        STEEL(TIER_STEEL, "anvil_steel", "anvil_steel"),
        DESH(TIER_OIL, "anvil_desh", "anvil_desh"),
        FERROURANIUM(TIER_NUCLEAR, "anvil_ferrouranium", "anvil_ferrouranium"),
        SATURNITE(TIER_RBMK, "anvil_saturnite", "anvil_saturnite"),
        BISMUTH_BRONZE(TIER_RBMK, "anvil_bismuth_bronze", "anvil_bismuth_bronze"),
        ARSENIC_BRONZE(TIER_RBMK, "anvil_arsenic_bronze", "anvil_arsenic_bronze"),
        FERRIC_SCHRABIDATE(TIER_FUSION, "anvil_ferric_schrabidate", "anvil_ferric_schrabidate"),
        DINEUTRONIUM(TIER_PARTICLE, "anvil_dineutronium", "anvil_dineutronium"),
        OSMIRIDIUM(TIER_GERALD, "anvil_osmiridium", "anvil_osmiridium");

        public final int tier;
        public final String modelName;
        public final String textureName;

        Variant(int tier, String modelName, String textureName) {
            this.tier = tier;
            this.modelName = modelName;
            this.textureName = textureName;
        }
    }

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final IntegerProperty SUBTYPE = IntegerProperty.create("subtype", 0, Variant.values().length - 1);
    private static final VoxelShape SHAPE_X = Block.box(4.0D, 0.0D, 0.0D, 12.0D, 12.0D, 16.0D);
    private static final VoxelShape SHAPE_Z = Block.box(0.0D, 0.0D, 4.0D, 16.0D, 12.0D, 12.0D);

    public NTMAnvilBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SUBTYPE, 0));
    }

    @Override
    protected MapCodec<NTMAnvilBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SUBTYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(SUBTYPE, normalizeMeta(MetaHelper.getMeta(context.getItemInHand())));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        MetaHelper.setMeta(stack, this.getMeta(state));
        return stack;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        drops.forEach(stack -> MetaHelper.setMeta(stack, this.getMeta(state)));
        return drops;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if(player instanceof ServerPlayer serverPlayer) {
            int tier = this.getTier(state);
            serverPlayer.openMenu(new SimpleMenuProvider((id, inventory, openedPlayer) -> new AnvilMenu(id, inventory, pos, tier), Component.translatable("container.anvil")), buf -> {
                buf.writeBlockPos(pos);
                buf.writeVarInt(tier);
            });
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public int getMeta(BlockState state) {
        return state.getValue(SUBTYPE);
    }

    @Override
    public int getSubCount() {
        return Variant.values().length;
    }

    @Override
    public String getItemDescriptionId(ItemStack stack) {
        Variant variant = getVariant(MetaHelper.getMeta(stack));
        return super.getDescriptionId() + "." + variant.name().toLowerCase(Locale.US);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        Variant variant = getVariant(MetaHelper.getMeta(stack));
        components.add(Component.translatable("block.hbmsntm.anvil.tier", variant.tier).withStyle(ChatFormatting.GOLD));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object provideScreen(Player player, BlockPos pos) {
        BlockState state = player.level.getBlockState(pos);
        return new AnvilScreen(getVariant(state.getValue(SUBTYPE)), getTier(state));
    }

    @Override
    public void registerModel(BlockStateProvider provider, ResourceLocation modelLocation) {
        String baseName = modelLocation.getPath();
        Variant[] variants = Variant.values();

        for(Variant variant : variants) {
            String modelName = variant.modelName;
            ResourceLocation texture = provider.modLoc("block/" + variant.textureName);

            provider.models().getBuilder(modelName)
                    .customLoader(NtmBlockStateProvider.AnvilLoaderBuilder::new)
                    .texture("texture", texture)
                    .end();
        }

        provider.getVariantBuilder(this).forAllStates(state -> {
            Variant variant = getVariant(state.getValue(SUBTYPE));
            String modelName = variant.modelName;
            return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(provider.modLoc("block/" + modelName))).build();
        });

        provider.itemModels().getBuilder(baseName)
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(BlockModel.GuiLight.SIDE);
    }

    public static Variant getVariant(int meta) {
        Variant[] variants = Variant.values();
        return variants[Math.abs(meta % variants.length)];
    }

    public static int getTierForMeta(int meta) {
        return getVariant(meta).tier;
    }

    public static List<ItemStack> getAnvilsFromTier(int tier) {
        NTMAnvilBlock block = (NTMAnvilBlock) NtmBlocks.ANVIL.get();
        return block.getStacksForTier(block.asItem(), tier);
    }

    public int getTier(BlockState state) {
        return getVariant(this.getMeta(state)).tier;
    }

    public List<ItemStack> getStacksForTier(ItemLike item, int tier) {
        List<ItemStack> stacks = new ArrayList<>();

        for(int i = 0; i < Variant.values().length; i++) {
            if(Variant.values()[i].tier == tier) {
                ItemStack stack = new ItemStack(item.asItem());
                MetaHelper.setMeta(stack, i);
                stacks.add(stack);
            }
        }

        return stacks;
    }

    private static int normalizeMeta(int meta) {
        return Math.abs(meta % Variant.values().length);
    }
}
