package com.hbm.items.special;

import com.hbm.HBMsNTM;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntityTypes;
import com.hbm.entity.effect.BlackHole;
import com.hbm.entity.effect.RagingVortex;
import com.hbm.entity.effect.Vortex;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.hazard.type.HazardTypeDigamma;
import com.hbm.interfaces.IBomb;
import com.hbm.interfaces.Spaghetti;
import com.hbm.items.ModItems;
import com.hbm.lib.ModSounds;
import com.hbm.util.TagsUtilDegradation;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

@Spaghetti("holy shet")
public class DangerousDropItem extends Item {

    public DangerousDropItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity itemEntity) {
        Level level = itemEntity.level();

        if (level.isClientSide) return false;

        // if this item has gotten from /give command it will create EntityItem that will do things
        // check for phantom item
        int lifespan = stack.getEntityLifespan(level);
        if (itemEntity.getAge() >= lifespan - 1) return false;

        String throwerName = "Unknown";
        if (TagsUtilDegradation.getTag(stack).contains("lastUser")) throwerName = TagsUtilDegradation.getTag(stack).getString("lastUser");

        if (itemEntity.getAge() > 5) {
            if (stack.is(ModItems.DETONATOR_DEADMAN.get())) {
                if (TagsUtilDegradation.containsAnyTag(stack)) {
                    CompoundTag tag = TagsUtilDegradation.getTag(stack);
                    int x = tag.getInt("x");
                    int y = tag.getInt("y");
                    int z = tag.getInt("z");

                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = level.getBlockState(pos).getBlock();
                    if (block instanceof IBomb bomb) {
                        bomb.explode(level, pos);

                        if (MainConfig.COMMON.ENABLE_EXTENDED_LOGGING.get()) {
                            HBMsNTM.LOGGER.info("[DEAD MAN'S DETONATOR] {} detonated {} at {} / {} / {}!", throwerName, block.getName().getString(), x, y, z);
                        }
                    }
                }
                level.explode(null, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 0.0F, Level.ExplosionInteraction.NONE);
                itemEntity.discard();
                return true;
            }
            if (stack.is(ModItems.DETONATOR_DE.get()) && MainConfig.COMMON.DROP_DEAD_MANS_EXPLOSIVE.get()) {
                level.explode(null, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 15.0F, Level.ExplosionInteraction.TNT);
                itemEntity.discard();
                return true;
            }
        }

        if (itemEntity.onGround()) {

            if (stack.is(ModItems.PARTICLE_DIGAMMA.get()) && MainConfig.COMMON.DROP_SINGULARITY.get()) {
                BlackHole quasar = new BlackHole(ModEntityTypes.QUASAR.get(), level);
                quasar.setPos(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
                quasar.setSize(5F);
                level.addFreshEntity(quasar);
            }

            if (stack.is(ModItems.CELL_ANTIMATTER.get()) && MainConfig.COMMON.DROP_CELL.get()) {
                new ExplosionVNT(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 5F).makeAmat().explode();
            }

            if (stack.is(ModItems.PELLET_ANTIMATTER.get()) && MainConfig.COMMON.DROP_CELL.get()) {
                new ExplosionVNT(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 20F).makeAmat().explode();
            }

            if (stack.is(ModItems.SINGULARITY.get()) && MainConfig.COMMON.DROP_SINGULARITY.get()) {
                Vortex vortex = new Vortex(ModEntityTypes.VORTEX.get(), level);
                vortex.setPos(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
                vortex.setSize(1.5F);
                level.addFreshEntity(vortex);
            }

            if (stack.is(ModItems.SINGULARITY_COUNTER_RESONANT.get()) && MainConfig.COMMON.DROP_SINGULARITY.get()) {
                Vortex vortex = new Vortex(ModEntityTypes.VORTEX.get(), level);
                vortex.setPos(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
                vortex.setSize(2.5F);
                level.addFreshEntity(vortex);
            }

            if (stack.is(ModItems.SINGULARITY_SUPER_HEATED.get()) && MainConfig.COMMON.DROP_SINGULARITY.get()) {
                Vortex vortex = new Vortex(ModEntityTypes.VORTEX.get(), level);
                vortex.setPos(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
                vortex.setSize(2.5F);
                level.addFreshEntity(vortex);
            }

            if (stack.is(ModItems.BLACK_HOLE.get()) && MainConfig.COMMON.DROP_SINGULARITY.get()) {
                BlackHole blackHole = new BlackHole(ModEntityTypes.BLACK_HOLE.get(), level);
                blackHole.setPos(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
                blackHole.setSize(1.5F);
                level.addFreshEntity(blackHole);
            }

            if (stack.is(ModItems.SINGULARITY_SPARK.get()) && MainConfig.COMMON.DROP_SINGULARITY.get()) {
                RagingVortex ragingVortex = new RagingVortex(ModEntityTypes.RAGING_VORTEX.get(), level);
                ragingVortex.setPos(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
                ragingVortex.setSize(3.5F);
                level.addFreshEntity(ragingVortex);
            }

            itemEntity.discard();
        }

        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        String[] desc = ITooltipProvider.getDescriptionOrNull(stack);
        if (desc != null) {
            for (String s : desc) {
                components.add(Component.translatable(s).withStyle(ChatFormatting.GRAY));
            }
        }
        if (this == ModItems.DETONATOR_DEADMAN.get()) {
            if (!TagsUtilDegradation.containsAnyTag(stack)) {
                components.add(Component.translatable("detonator.no_pos"));
            } else {
                CompoundTag tag = TagsUtilDegradation.getTag(stack);
                int x = tag.getInt("x");
                int y = tag.getInt("y");
                int z = tag.getInt("z");
                components.add(Component.translatable("detonator.set_to", x, y, z));
            }
        }
        if (this == ModItems.PARTICLE_DIGAMMA.get()) {
            components.add(Component.translatable("trait.hlParticle", "1.67*10³⁴a").withStyle(ChatFormatting.GOLD));
            components.add(Component.translatable("trait.hlPlayer", (HazardSystem.getHazardLevelFromStack(stack, HazardRegistry.DIGAMMA) / 20F) + "s").withStyle(ChatFormatting.RED));
        }

        components.add(Component.literal("[" + I18nUtil.resolveKey("trait.drop") + "]").withStyle(ChatFormatting.RED));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (this != ModItems.DETONATOR_DEADMAN.get()) {
            return super.useOn(context);
        }
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();

        if (!level.isClientSide) {
            if (player.isCrouching()) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("x", context.getClickedPos().getX());
                tag.putInt("y", context.getClickedPos().getY());
                tag.putInt("z", context.getClickedPos().getZ());
                tag.putString("lastUser", player.getName().getString());
                TagsUtilDegradation.putTag(stack, tag);

                level.playSound(null, player.blockPosition(), ModSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
