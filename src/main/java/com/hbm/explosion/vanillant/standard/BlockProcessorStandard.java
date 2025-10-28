package com.hbm.explosion.vanillant.standard;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.interfaces.IBlockMutator;
import com.hbm.explosion.vanillant.interfaces.IBlockProcessor;
import com.hbm.explosion.vanillant.interfaces.IDropChanceMutator;
import com.hbm.explosion.vanillant.interfaces.IFortuneMutator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class BlockProcessorStandard implements IBlockProcessor {

    protected IDropChanceMutator chance;
    protected IFortuneMutator fortune;
    protected IBlockMutator convert;

    public BlockProcessorStandard() { }

    public BlockProcessorStandard withChance(IDropChanceMutator chance) {
        this.chance = chance;
        return this;
    }

    public BlockProcessorStandard withFortune(IFortuneMutator fortune) {
        this.fortune = fortune;
        return this;
    }

    public BlockProcessorStandard withBlockEffect(IBlockMutator convert) {
        this.convert = convert;
        return this;
    }

    @Override
    public void process(ExplosionVNT explosion, Level level, double x, double y, double z, HashSet<BlockPos> affectedBlocks) {
        Iterator<BlockPos> iterator = affectedBlocks.iterator();
        float dropChance = 1.0F / explosion.size;

        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();

            if (!state.isAir()) {
                if (state.canDropFromExplosion(level, pos, null)) {

                    if (chance != null) {
                        dropChance = chance.mutateDropChance(explosion, block, pos, dropChance);
                    }

                    int dropFortune = fortune == null ? 0 : fortune.mutateFortune(explosion, block, pos);

                    Holder<Enchantment> fortuneEnchant = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE);
                    ItemStack toolWith = new ItemStack(Items.DIAMOND_PICKAXE);
                    if (dropFortune > 0) {
                        toolWith.enchant(fortuneEnchant, dropFortune);
                    }

                    if (level instanceof ServerLevel serverLevel) {
                        LootParams.Builder builder = new LootParams.Builder(serverLevel)
                                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                                .withParameter(LootContextParams.TOOL, toolWith)
                                .withParameter(LootContextParams.BLOCK_STATE, state)
                                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos));

                        List<ItemStack> drops = state.getDrops(builder);

                        for (ItemStack drop : drops) {
                            if (serverLevel.random.nextFloat() <= dropChance) {
                                Block.popResource(serverLevel, pos, drop);
                            }
                        }
                    }
                }

                // if there is BushBlock above exploded block,
                // bush plays break sound and if there is a lot of bushes, they all playing sound and sound handler freaks out
                // how i can fix this u may ask? i dont know
                state.onBlockExploded(level, pos, explosion.compat);

                if (this.convert != null) {
                    this.convert.mutatePre(explosion, state, pos);
                }
            } else {
                iterator.remove();
            }
        }

        if (this.convert != null) {
            iterator = affectedBlocks.iterator();

            while (iterator.hasNext()) {
                BlockPos pos = iterator.next();
                BlockState state = level.getBlockState(pos);

                if (state.isAir()) {
                    this.convert.mutatePost(explosion, pos);
                }
            }
        }
    }

    public BlockProcessorStandard setNoDrop() {
        this.chance = new DropChanceMutatorStandard(0F);
        return this;
    }
    public BlockProcessorStandard setAllDrop() {
        this.chance = new DropChanceMutatorStandard(1F);
        return this;
    }

    public BlockProcessorStandard setFortune(int fortune) {
        this.fortune = new IFortuneMutator() { //no standard class because we only have one case thus far
            @Override
            public int mutateFortune(ExplosionVNT explosion, Block block, BlockPos pos) {
                return fortune;
            }
        };
        return this;
    }
}
