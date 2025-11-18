package com.hbm.items.tools;

import com.google.common.collect.Sets;
import com.hbm.HBMsNTMClient;
import com.hbm.handler.abilities.*;
import com.hbm.items.IDepthRockTool;
import com.hbm.network.toclient.InformPlayer;
import com.hbm.util.TagsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ToolAbilityItem extends TieredItem implements IDepthRockTool {

    protected EnumToolType toolType;
    protected AvailableAbilities availableAbilities = new AvailableAbilities().addToolAbilities();
    private boolean rockBreaker;
    private boolean isShears;

    public static ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
        return createAttributes(tier, (float)attackDamage, attackSpeed);
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, float damage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (damage + tier.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    public ToolAbilityItem setShears() {
        this.isShears = true;
        return this;
    }

    public ToolAbilityItem(Tier tier, EnumToolType type, Properties properties) {
        super(tier, properties);
        this.toolType = type;
    }

    public enum EnumToolType {

        SHOVEL(
                Sets.newHashSet(new Block[] { Blocks.SAND, Blocks.RED_SAND })
        ),
        PICKAXE(
                Sets.newHashSet(new Block[] { Blocks.IRON_ORE, Blocks.DIAMOND_ORE, Blocks.GOLD_ORE })
        );

        EnumToolType(Set<Block> blocks) {
            this.blocks = blocks;
        }

        public Set<Block> blocks;
    }

    public ToolAbilityItem addAbility(IBaseAbility ability, int level) {
        this.availableAbilities.addAbility(ability, level);

        return this;
    }

    public ToolAbilityItem setDepthRockBreaker() {
        this.rockBreaker = true;
        return this;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) {

        if (!attacker.level().isClientSide && attacker instanceof Player && canOperate(stack)) {

            this.availableAbilities.getWeaponAbilities().forEach((ability, level) -> {
                ability.onHit(level, attacker.level(), (Player) attacker, victim, this);
            });
        }

        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        /*
         * The original implementation of this always returned FALSE which uses the vanilla block break code.
         * This one now returns TRUE when an ability applies and instead relies on breakExtraBlock, which has the minor
         * issue of only running on the sever, while the client uses the vanilla implementation. breakExtraBlock was only
         * meant to be used for AoE or vein miner and not for the block that's being mined, hence break EXTRA block.
         * The consequence was that the server would fail to break keyholes since breakExtraBlock is supposed to exclude
         * them, while the client happily removes the block, causing a desync.
         */
        if (!level.isClientSide && miningEntity instanceof Player player && (canHarvest(stack, state, player, level, pos) || canShearBlock(state, stack, level, pos)) && canOperate(stack)) {
            Configuration config = getConfiguration(stack);
            ToolPreset preset = config.getActivePreset();

            preset.harvestAbility.preHarvestAll(preset.harvestAbilityLevel, level, player);

            boolean skipRef = preset.areaAbility.onDig(preset.areaAbilityLevel, level, pos, player, this);

            if (!skipRef) {
                breakExtraBlock(level, pos, (ServerPlayer) player, pos);
            }

            preset.harvestAbility.postHarvestAll(preset.harvestAbilityLevel, level, player);

            return true;
        }

        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (!canOperate(stack))
            return 1.0F;

        if (toolType == null)
            return super.getDestroySpeed(stack, state);

        if (toolType.blocks.contains(state.getBlock()))
            return this.getTier().getSpeed();

        return super.getDestroySpeed(stack, state);
    }

    public boolean canOperate(ItemStack stack) {
        return true;
    }

    public boolean canHarvest(ItemStack stack, BlockState state, Player player, Level level, BlockPos pos) {
        if (!canOperate(stack))
            return false;

        if (this.getConfiguration(stack).getActivePreset().harvestAbility == IToolHarvestAbility.SILK)
            return true;

        return EventHooks.doPlayerHarvestCheck(player, state, level, pos) && getDestroySpeed(stack, state) > 1.0F;
    }

    public boolean canBreakRock(Level level, Player player, ItemStack tool, BlockState state, BlockPos pos) {
        return canOperate(tool) && this.rockBreaker;
    }

    public boolean canShearBlock(BlockState state, ItemStack stack, Level level, BlockPos pos) {
        return this.isShears(stack) && state.getBlock() instanceof IShearable shearable && shearable.isShearable(null, stack, level, pos);
    }

    public boolean isShears(ItemStack stack) {
        return this.isShears;
    }

    @Override
    public boolean canBreakRock(Level level, Player player, ItemStack tool, Block block, BlockPos pos) {
        return canOperate(tool) && this.rockBreaker;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || !getConfiguration(stack).getActivePreset().isNone();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        availableAbilities.addInformation(tooltipComponents);

        if (rockBreaker) {
            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.literal("Can break depth rock!").withStyle(ChatFormatting.RED));
        }
    }

    public void breakExtraBlock(Level level, BlockPos pos, ServerPlayer player, BlockPos refPos) {
        if (level.isEmptyBlock(pos)) return;

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return;

        BlockState state = level.getBlockState(pos);

        if (!(canHarvest(stack, state, player, level, pos) || canShearBlock(state, stack, level, pos)) || (state.getDestroySpeed(level, pos) == -1.0F && state.getDestroyProgress(player, level, pos) == 0.0F)) {
            return;
        }

        BlockState refState = level.getBlockState(refPos);

        float refStrength = EventHooks.getBreakSpeed(player, refState, refState.getDestroySpeed(level, refPos), refPos);
        float strength = EventHooks.getBreakSpeed(player, state, state.getDestroySpeed(level, pos), pos);

        if (!EventHooks.doPlayerHarvestCheck(player, state, level, pos) || refStrength / strength > 10f || refState.getDestroyProgress(player, level, refPos) < 0) {
            return;
        }

        BlockEvent.BreakEvent event = CommonHooks.fireBlockBreak(level, player.gameMode.getGameModeForPlayer(), player, pos, state);
        if (event.isCanceled()) return;

        Configuration config = getConfiguration(stack);
        ToolPreset preset = config.getActivePreset();

        preset.harvestAbility.onHarvestBlock(preset.harvestAbilityLevel, level, pos, player, state);
    }

    public static void shearBlock(Level level, BlockPos pos, ServerPlayer player) {
        ItemStack held = player.getMainHandItem();

        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof IShearable shearable)) return;

        if (shearable.isShearable(null, held, level, pos)) {
            List<ItemStack> drops = shearable.onSheared(null, held, level, pos);
            RandomSource rand = level.random;

            for (ItemStack stack : drops) {
                float f = 0.7F;
                double dx = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                double dy = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                double dz = rand.nextFloat() * f + (1.0F - f) * 0.5D;

                ItemEntity entity = new ItemEntity(level,
                        pos.getX() + dx,
                        pos.getY() + dy,
                        pos.getZ() + dz,
                        stack);
                entity.setPickUpDelay(10);
                level.addFreshEntity(entity);
            }

            held.hurtAndBreak(1, player, getSlotForHand(InteractionHand.MAIN_HAND));

            player.awardStat(Stats.BLOCK_MINED.get(state.getBlock()));
        }
    }

    public static EquipmentSlot getSlotForHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    public static void standardDigPost(Level level, BlockPos pos, ServerPlayer player) {
        BlockState state = level.getBlockState(pos);

        level.levelEvent(player, 2001, pos, Block.getId(state));

        boolean removedByPlayer;

        if (player.isCreative()) {
            level.destroyBlock(pos, false, player);
            player.connection.send(new ClientboundBlockUpdatePacket(pos, level.getBlockState(pos)));
        } else {
            ItemStack held = player.getMainHandItem();
            boolean canHarvest = player.hasCorrectToolForDrops(state, level, pos);

            removedByPlayer = level.destroyBlock(pos, canHarvest, player);

            if (!held.isEmpty()) {
                held.mineBlock(level, state, pos, player);
                if (held.isEmpty()) {
                    player.getInventory().removeItem(held);
                }
            }

            if (removedByPlayer && canHarvest) {
                List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, level.getBlockEntity(pos), player, held);
                for (ItemStack drop : drops) {
                    Block.popResource(level, pos, drop);
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!canOperate(player.getMainHandItem())) return InteractionResultHolder.pass(player.getItemInHand(usedHand));

        Configuration config = getConfiguration(player.getMainHandItem());
        if (config.presets.size() < 2 || level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(usedHand));

        if (player.isCrouching()) {
            config.currentPreset = 0;
        } else {
            config.currentPreset = (config.currentPreset + 1) % config.presets.size();
        }

        setConfiguration(player.getMainHandItem(), config);
        PacketDistributor.sendToPlayer((ServerPlayer) player, new InformPlayer(config.getActivePreset().getMessage(), HBMsNTMClient.ID_TOOLABILITY, 1000));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.25F, config.getActivePreset().isNone() ? 0.75F : 1.25F);

        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    public static class Configuration {
        public List<ToolPreset> presets;
        public int currentPreset;

        public Configuration() {
            this.presets = null;
            this.currentPreset = 0;
        }

        public Configuration(List<ToolPreset> presets, int currentPreset) {
            this.presets = presets;
            this.currentPreset = currentPreset;
        }

        public void writeToNBT(CompoundTag nbt) {
            nbt.putInt("ability", currentPreset);

            ListTag nbtPresets = new ListTag();

            for (ToolPreset preset : presets) {
                CompoundTag nbtPreset = new CompoundTag();
                preset.writeToNBT(nbtPreset);
                nbtPresets.add(nbtPreset);
            }

            nbt.put("abilityPresets", nbtPresets);
        }

        public void readFromNBT(CompoundTag nbt) {
            currentPreset = nbt.getInt("ability");

            ListTag nbtPresets = nbt.getList("abilityPresets", Tag.TAG_COMPOUND);
            int numPresets = Math.min(nbtPresets.size(), 99);

            presets = new ArrayList<>(numPresets);

            for (int i = 0; i < numPresets; i++) {
                CompoundTag nbtPreset = nbtPresets.getCompound(i);
                ToolPreset preset = new ToolPreset();
                preset.readFromNBT(nbtPreset);
                presets.add(preset);
            }

            currentPreset = Math.max(0, Math.min(currentPreset, presets.size() - 1));
        }

        public void reset(AvailableAbilities availableAbilities) {
            currentPreset = 0;

            presets = new ArrayList<>(availableAbilities.size());
            presets.add(new ToolPreset());

            availableAbilities.getToolAreaAbilities().forEach((ability, level) -> {
                if (ability == IToolAreaAbility.NONE) return;
                presets.add(new ToolPreset(ability, level, IToolHarvestAbility.NONE, 0));
            });

            availableAbilities.getToolHarvestAbilities().forEach((ability, level) -> {
                if (ability == IToolHarvestAbility.NONE) return;
                presets.add(new ToolPreset(IToolAreaAbility.NONE, 0, ability, level));
            });

            presets.sort(
                    Comparator
                            .comparing((ToolPreset p) -> p.harvestAbility)
                            .thenComparingInt(p -> p.harvestAbilityLevel)
                            .thenComparing(p -> p.areaAbility)
                            .thenComparingInt(p -> p.areaAbilityLevel)
            );
        }

        public void restrictTo(AvailableAbilities availableAbilities) {
            for (ToolPreset preset : presets) {
                preset.restrictTo(availableAbilities);
            }
        }

        public ToolPreset getActivePreset() {
            return presets.get(currentPreset);
        }
    }

    public Configuration getConfiguration(ItemStack stack) {
        Configuration config = new Configuration();

        if (stack.isEmpty() || !TagsUtil.hasTag(stack) || !TagsUtil.contains(stack, "ability") || !TagsUtil.contains(stack, "abilityPresets")) {
            config.reset(availableAbilities);
            return config;
        }

        config.readFromNBT(TagsUtil.getTag(stack));
        config.restrictTo(availableAbilities);
        return config;
    }

    public void setConfiguration(ItemStack stack, Configuration config) {
        if (stack.isEmpty()) {
            return;
        }

        config.writeToNBT(TagsUtil.getTag(stack));
    }
}
