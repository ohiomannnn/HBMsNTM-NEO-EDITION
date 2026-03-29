package com.hbm.blocks;

import com.hbm.inventory.MetaHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;

import java.util.List;

/**
 * Standard imp of meta block
 */
public class MetaBlock extends Block implements IMetaBlock {

//    protected static final ResourceLocation[] iconLocations = new ResourceLocation[] {
//            ResourceLocation.fromNamespaceAndPath("mymod", "block/my_block_0"),
//            ResourceLocation.fromNamespaceAndPath("mymod", "block/my_block_1"),
//            ResourceLocation.fromNamespaceAndPath("mymod", "block/my_block_2")
//    };
//    protected static final ResourceLocation[] overlayLocations = new ResourceLocation[] {
//            ResourceLocation.fromNamespaceAndPath("mymod", "block/my_block_overlay_0"),
//            ResourceLocation.fromNamespaceAndPath("mymod", "block/my_block_overlay_1"),
//            ResourceLocation.fromNamespaceAndPath("mymod", "block/my_block_overlay_2")
//    };
//
//    /** Client only */
//    protected static TextureAtlasSprite[] icon;
//    /** Client only */
//    protected static TextureAtlasSprite[] overlay;

    public static final IntegerProperty META = IntegerProperty.create("meta", 0, 15);

    public MetaBlock(Properties properties) {
        super(properties);
    }

//    public static void registerSprites() {
//        Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
//
//        icon = new TextureAtlasSprite[iconLocations.length];
//        for (int i = 0; i < iconLocations.length; i++) {
//            icon[i] = atlas.apply(iconLocations[i]);
//        }
//
//        overlay = new TextureAtlasSprite[overlayLocations.length];
//        for (int i = 0; i < overlayLocations.length; i++) {
//            overlay[i] = atlas.apply(overlayLocations[i]);
//        }
//    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(META, MetaHelper.getMeta(context.getItemInHand()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(META);
    }

    public static final MapCodec<MetaBlock> CODEC = simpleCodec(MetaBlock::new);
    @Override protected MapCodec<? extends Block> codec() { return CODEC; }

    @Override public int getMeta(BlockState state) { return state.getValue(META); }
    @Override public int getMaxMeta() { return 15; }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return MetaHelper.metaStack(new ItemStack(this), this.getMeta(state));
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return List.of(MetaHelper.metaStack(new ItemStack(this), this.getMeta(state)));
    }
}
