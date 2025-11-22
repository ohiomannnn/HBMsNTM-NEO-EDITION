package com.hbm.items.tools;

import com.google.common.collect.Sets;
import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.config.MainConfig;
import com.hbm.handler.KeyHandler.EnumKeybind;
import com.hbm.handler.abilities.*;
import com.hbm.interfaces.IItemHUD;
import com.hbm.items.IDepthRockTool;
import com.hbm.items.IItemControlReceiver;
import com.hbm.items.IKeybindReceiver;
import com.hbm.network.toclient.InformPlayer;
import com.hbm.util.TagsUtil;
import com.hbm.util.Tuple.Pair;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

public class ToolAbilityItem extends TieredItem implements IDepthRockTool, IKeybindReceiver, IItemControlReceiver, IItemHUD {

    protected EnumToolType toolType;
    protected AvailableAbilities availableAbilities = new AvailableAbilities().addToolAbilities();
    private boolean rockBreaker;
    private boolean isShears;

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
        super(tier, properties.component(DataComponents.TOOL, tier.createToolProperties(BlockTags.MINEABLE_WITH_PICKAXE)));
        this.toolType = type;
    }

    public enum EnumToolType {

        SHOVEL(
                Sets.newHashSet(new Block[]{Blocks.SAND, Blocks.RED_SAND})
        ),
        PICKAXE(
                Sets.newHashSet(new Block[]{Blocks.IRON_ORE, Blocks.DIAMOND_ORE, Blocks.GOLD_ORE})
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
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide && miningEntity instanceof Player player && (canHarvest(stack, state, player, level, pos) || canShearBlock(state, stack, level, pos)) && canOperate(stack)) {
            Configuration config = getConfiguration(stack);
            ToolPreset preset = config.getActivePreset();
            boolean harvestAllowed = preset.harvestAbility.isAllowed();
            boolean areaAllowed = preset.areaAbility.isAllowed();

            if (harvestAllowed) preset.harvestAbility.preHarvestAll(preset.harvestAbilityLevel, level, player, stack);

            if (areaAllowed) {
                boolean skipRef = preset.areaAbility.onDig(preset.areaAbilityLevel, level, pos, player, this);

                if (!skipRef) {
                    breakExtraBlock(level, pos, player, pos);
                }
            }

            if (harvestAllowed) preset.harvestAbility.postHarvestAll(preset.harvestAbilityLevel, level, player, stack);
        }

        return super.mineBlock(stack, level, state, pos, miningEntity);
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
        availableAbilities.appendHoverText(tooltipComponents);

        if (rockBreaker) {
            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.literal("Can break depth rock!").withStyle(ChatFormatting.RED));
        }
    }

    public void breakExtraBlock(Level level, BlockPos pos, Player player, BlockPos refPos) {
        BlockState state = level.getBlockState(pos);
        BlockState refState = level.getBlockState(refPos);

        if (state.is(refState.getBlock())) {
            Block.dropResources(state, level, pos, level.getBlockEntity(pos), player, player.getMainHandItem());
            level.removeBlock(pos, false);
            player.getMainHandItem().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }

//        if (level.isEmptyBlock(pos)) return;
//
//        if (!(player instanceof ServerPlayer serverPlayer)) return;
//
//        ItemStack stack = serverPlayer.getMainHandItem();
//        if (stack.isEmpty()) return;
//
//        BlockState state = level.getBlockState(pos);
//
//        if (!(canHarvest(stack, state, serverPlayer, level, pos) ||
//                canShearBlock(state, stack, level, pos)) ||
//                (state.getDestroySpeed(level, pos) == -1.0F && state.getDestroyProgress(serverPlayer, level, pos) == 0.0F)
//        ) return;
//
//        BlockState refState = level.getBlockState(refPos);
//
//        float refStrength = EventHooks.getBreakSpeed(serverPlayer, refState, refState.getDestroySpeed(level, refPos), refPos);
//        float strength = EventHooks.getBreakSpeed(serverPlayer, state, state.getDestroySpeed(level, pos), pos);
//
//        if (
//                !EventHooks.doPlayerHarvestCheck(serverPlayer, state, level, pos) ||
//                        refStrength / strength > 10f ||
//                        refState.getDestroyProgress(serverPlayer, level, refPos) < 0
//        ) return;
//
//        BlockEvent.BreakEvent event = CommonHooks.fireBlockBreak(level, serverPlayer.gameMode.getGameModeForPlayer(), serverPlayer, pos, state);
//        if (event.isCanceled()) return;
//
//        Configuration config = this.getConfiguration(stack);
//        ToolPreset preset = config.getActivePreset();
//
//        preset.harvestAbility.onHarvestBlock(preset.harvestAbilityLevel, level, pos, serverPlayer, state);
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

    @Override
    public boolean canHandleKeybind(Player player, ItemStack stack, EnumKeybind keybind) {
        if (player.level().isClientSide) return keybind == EnumKeybind.ABILITY_ALT;
        return keybind == EnumKeybind.ABILITY_CYCLE;
    }

    @Override
    public void handleKeybindClient(Player player, ItemStack stack, EnumKeybind keybind, boolean state) {
        if (state) HBMsNTM.LOGGER.info("hello from client");
    }

    @Override
    public void handleKeybind(Player player, ItemStack stack, EnumKeybind keybind, boolean state) {
        if (keybind == EnumKeybind.ABILITY_CYCLE && state) {
            Level level = player.level();
            if (!canOperate(stack)) return;

            Configuration config = this.getConfiguration(stack);
            if (config.presets.size() < 2 || level.isClientSide) return;

            if (player.isCrouching()) {
                config.currentPreset = 0;
            } else {
                config.currentPreset = (config.currentPreset + 1) % config.presets.size();
            }

            this.setConfiguration(stack, config);
            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new InformPlayer(config.getActivePreset().getMessage(), HBMsNTMClient.ID_TOOLABILITY, 1000));
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.25F, config.getActivePreset().isNone() ? 0.75F : 1.25F);
        }
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
        if (!TagsUtil.isValid(stack)) return;

        CompoundTag tag = TagsUtil.getOrCreateTag(stack);
        config.writeToNBT(tag);
        TagsUtil.setTag(stack, tag);
    }

    @Override
    public void receiveControl(ItemStack stack, CompoundTag tag) {
        Configuration config = new Configuration();
        config.readFromNBT(tag);
        config.restrictTo(availableAbilities);
        this.setConfiguration(stack, config);
    }

    private static final ResourceLocation tex = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "textures/gui/tool/gui_tool_ability.png");

    private static final Map<IBaseAbility, Pair<Integer, Integer>> abilityGui = new HashMap<>();

    static {
        abilityGui.put(IToolAreaAbility.RECURSION, new Pair<>(48, 138));
        abilityGui.put(IToolAreaAbility.HAMMER, new Pair<>(80, 138));
        abilityGui.put(IToolAreaAbility.HAMMER_FLAT, new Pair<>(112, 138));
        abilityGui.put(IToolAreaAbility.EXPLOSION, new Pair<>(144, 138));
    }


    @Override
    public void renderHUD(RenderGuiEvent.Pre event, Player player, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();

        Options options = mc.options;
        if (!options.getCameraType().isFirstPerson()) return;
        if (options.hideGui) return;
        if (mc.gameMode.getPlayerMode() == GameType.SPECTATOR) return;

        Configuration config = this.getConfiguration(stack);
        ToolPreset preset = config.getActivePreset();
        Pair<Integer, Integer> uv = abilityGui.get(preset.areaAbility);
        if (uv == null) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int size = 16;
        int ox = MainConfig.CLIENT.TOOL_HUD_INDICATOR_X.get();
        int oy = MainConfig.CLIENT.TOOL_HUD_INDICATOR_Y.get();

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        guiGraphics.blit(tex,
                mc.getWindow().getGuiScaledWidth() / 2 - size - 8 + ox, mc.getWindow().getGuiScaledHeight() / 2 + 8 + oy,
                size, size,
                uv.key, uv.value,
                size, size,
                256, 256
        );

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}

