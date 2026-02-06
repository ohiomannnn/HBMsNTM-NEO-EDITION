package com.hbm;

import com.hbm.blockentity.IGUIProvider;
import com.hbm.blockentity.ModBlockEntities;
import com.hbm.blocks.ICustomBlockHighlight;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.bomb.BalefireBlock;
import com.hbm.blocks.generic.SellafieldSlakedBlock;
import com.hbm.config.MainConfig;
import com.hbm.entity.ModEntityTypes;
import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.handler.HazmatRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.interfaces.IHoldableWeapon;
import com.hbm.interfaces.Spaghetti;
import com.hbm.inventory.screens.LoadingScreenRendererNT;
import com.hbm.items.IItemHUD;
import com.hbm.items.ModItems;
import com.hbm.items.datacomps.FluidTypeComponent;
import com.hbm.items.special.PolaroidItem;
import com.hbm.items.tools.GeigerCounterItem;
import com.hbm.main.ResourceManager;
import com.hbm.particle.*;
import com.hbm.particle.engine.ParticleEngineNT;
import com.hbm.particle.helper.ParticleCreators;
import com.hbm.particle.vanilla.PlayerCloudParticle;
import com.hbm.render.blockentity.*;
import com.hbm.render.entity.EmptyEntityRenderer;
import com.hbm.render.entity.effect.*;
import com.hbm.render.entity.item.RenderTNTPrimedBase;
import com.hbm.render.entity.mob.CreeperNuclearRenderer;
import com.hbm.render.entity.mob.DuckRenderer;
import com.hbm.render.entity.projectile.*;
import com.hbm.render.item.*;
import com.hbm.render.loader.bakedLoader.HFRObjGeometryLoader;
import com.hbm.render.util.RenderInfoSystem;
import com.hbm.render.util.RenderScreenOverlay;
import com.hbm.sound.AudioWrapper;
import com.hbm.sound.AudioWrapperClient;
import com.hbm.util.ArmorRegistry;
import com.hbm.util.Clock;
import com.hbm.util.DamageResistanceHandler;
import com.hbm.util.Vec3NT;
import com.hbm.util.i18n.I18nClient;
import com.hbm.util.i18n.ITranslate;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

@Spaghetti("die")
@Mod(value = HBMsNTM.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class HBMsNTMClient {
    public HBMsNTMClient(ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static LocalPlayer me() {
        return Minecraft.getInstance().player;
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ResourceManager.init();
        });
    }

    private static final I18nClient I18N = new I18nClient();
    public static ITranslate getI18n() { return I18N; }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (MainConfig.CLIENT.ENABLE_TIPS.get()) {
            Screen screen = event.getScreen();
            if (screen instanceof LevelLoadingScreen || screen instanceof ReceivingLevelScreen) {
                LoadingScreenRendererNT.render(event.getGuiGraphics());
            }
        }
    }

    @SubscribeEvent
    public static void onJoin(ClientPlayerNetworkEvent.LoggingOut event) {
        if (MainConfig.CLIENT.ENABLE_TIPS.get()) {
            LoadingScreenRendererNT.resetMessage();
        }
        RenderInfoSystem.clear();
    }

    public static final int ID_DUCK = 0;
    public static final int ID_FILTER = 1;
    public static final int ID_COMPASS = 2;
    public static final int ID_CABLE = 3;
    public static final int ID_DRONE = 4;
    public static final int ID_JETPACK = 5;
    public static final int ID_MAGNET = 6;
    public static final int ID_HUD = 7;
    public static final int ID_DETONATOR = 8;
    public static final int ID_FLUID_ID = 9;
    public static final int ID_FAN_MODE = 10;
    public static final int ID_TOOLABILITY = 11;
    public static final int ID_GAS_HAZARD = 12;

    public static void displayTooltip(Component component, int time, int id) {
        RenderInfoSystem.InfoEntry entry = new RenderInfoSystem.InfoEntry(component, time);
        if (id != 0) {
            RenderInfoSystem.push(entry, id);
        } else {
            RenderInfoSystem.push(entry);
        }
    }

    public static AudioWrapper getLoopedSound(SoundEvent event, SoundSource source, float x, float y, float z, float volume, float range, float pitch) {
        AudioWrapperClient audio = new AudioWrapperClient(event, source);
        audio.updatePosition(x, y, z);
        audio.updateVolume(volume);
        audio.updateRange(range);
        return audio;
    }

    public static AudioWrapper getLoopedSound(SoundEvent event, SoundSource source, float x, float y, float z, float volume, float range, float pitch, int keepAlive) {
        AudioWrapper audio = getLoopedSound(event, source, x, y, z, volume, range, pitch);
        audio.setKeepAlive(keepAlive);
        return audio;
    }

    public static final int flashDuration = 5_000;
    public static long flashTimestamp;
    public static final int shakeDuration = 1_500;
    public static long shakeTimestamp;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderGuiPre(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = Minecraft.getInstance().player;

        /// NUKE SHAKE ///
        if ((shakeTimestamp + shakeDuration - System.currentTimeMillis()) > 0 && MainConfig.CLIENT.ENABLE_NUKE_HUD_SHAKE.get()) {
            double mult = (shakeTimestamp + shakeDuration - System.currentTimeMillis()) / (double) shakeDuration * 2;
            double horizontal = Mth.clamp(Math.sin(System.currentTimeMillis() * 0.02), -0.7, 0.7) * 15;
            double vertical = Mth.clamp(Math.sin(System.currentTimeMillis() * 0.01 + 2), -0.7, 0.7) * 3;
            event.getGuiGraphics().pose().translate(horizontal * mult, vertical * mult, 0);
        }

        if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof IItemHUD hudProvider) {
            hudProvider.renderHUD(event, player, player.getMainHandItem());
        }

        HitResult hr = mc.hitResult;
        Level level = mc.level;

        if (hr != null) {
            if (hr.getType() == Type.BLOCK) {
                BlockHitResult bhr = (BlockHitResult) hr;
                if (player.getMainHandItem().getItem() instanceof ILookOverlay ilo) {
                    ilo.printHook(event, level, bhr.getBlockPos());
                }
                if (level.getBlockState(bhr.getBlockPos()).getBlock() instanceof ILookOverlay ilo) {
                    ilo.printHook(event, level, bhr.getBlockPos());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        /// NUKE FLASH ///
        if (MainConfig.CLIENT.ENABLE_NUKE_HUD_FLASH.get() && (flashTimestamp + flashDuration - Clock.get_ms()) > 0) {
            float brightness = (flashTimestamp + flashDuration - Clock.get_ms()) / (float) flashDuration;
            int alpha = (int)(brightness * 255.0F);
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            // finally!
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            Tesselator tess = Tesselator.getInstance();
            BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            buf.addVertex(width, 0, 0).setColor(255, 255, 255, alpha);
            buf.addVertex(0, 0, 0).setColor(255, 255, 255, alpha);
            buf.addVertex(0, height, 0).setColor(255, 255, 255, alpha);
            buf.addVertex(width, height, 0).setColor(255, 255, 255, alpha);
            BufferUploader.drawWithShader(buf.buildOrThrow());

            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
        }

        /// GEIGER GUI ///
        if (checkForGeiger(mc.player)) {
            float rads = HbmLivingAttachments.getRadiation(mc.player);

            RenderScreenOverlay.renderRadCounter(event.getGuiGraphics(), rads);
        }
    }

    private static boolean checkForGeiger(Player player) {
        if (player.getOffhandItem().getItem() instanceof GeigerCounterItem) { return true; }
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof GeigerCounterItem) {
                return true;
            }
        }
        return false;
    }

    public static boolean renderLodeStar = false;
    public static long lastStarCheck = 0L;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onClientTickLast(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        long millis = Clock.get_ms();
        if (millis == 0) millis = System.currentTimeMillis();

        Player player = mc.player;

        if (lastStarCheck + 100 < millis) {
            renderLodeStar = false;
            lastStarCheck = millis;

            if (player != null) {
                Vec3 pos = player.position();
                Vec3 lodestarHeading = new Vec3(0, 0, -1).xRot((float) Math.toRadians(-15)).scale(25);

                Vec3 nextPos = pos.add(lodestarHeading);
                ClipContext context = new ClipContext(
                        pos,
                        nextPos,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        player
                );

                BlockHitResult hit = player.level().clip(context);
                if (hit.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockPos = hit.getBlockPos();
                    BlockState state = player.level().getBlockState(blockPos);

                    if (state.is(Blocks.GLASS)) {
                        renderLodeStar = true;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHighlight(RenderHighlightEvent.Block event) {

        Level level = Minecraft.getInstance().level;

        BlockHitResult bhr = event.getTarget();

        if (bhr.getType() == HitResult.Type.BLOCK) {
            Block b = level.getBlockState(bhr.getBlockPos()).getBlock();
            if (b instanceof ICustomBlockHighlight cus) {
                if (cus.shouldDrawHighlight(level, bhr.getBlockPos())) {
                    cus.drawHighlight(event, level, bhr.getBlockPos());
                    event.setCanceled(true);
                }
            }
        }
    }
    @SubscribeEvent
    public static void onOpenGUI(ScreenEvent.Opening event) {
        if (event.getScreen() instanceof TitleScreen main && MainConfig.CLIENT.ENABLE_MAIN_MENU_WACKY_SPLASHES.get()) {
            int rand = (int) (Math.random() * 150);
            String text = switch (rand) {
                case 0 -> "Floppenheimer!";
                case 1 -> "i should dip my balls in sulfuric acid";
                case 2 -> "All answers are popbob!";
                case 3 -> "None may enter The Orb!";
                case 4 -> "Wacarb was here";
                case 5 -> "SpongeBoy me Bob I am overdosing on ketamine agagagagaga";
                case 6 -> ChatFormatting.RED + "I know where you live, " + System.getProperty("user.name");
                case 7 -> "Nice toes, now hand them over.";
                case 8 -> "I smell burnt toast!";
                case 9 -> "There are bugs under your skin!";
                case 10 -> "Fentanyl!";
                case 11 -> "Do drugs!";
                case 12 -> "Imagine being scared by splash texts!";
                case 13 -> ChatFormatting.RED + "" + ChatFormatting.BOLD + "AW SHUCKS!";
                default -> null;
            };

            double d = Math.random();
            if (d < 0.1) text = "Redditors aren't people!";
            else if (d < 0.2) text = "Can someone tell me what corrosive fumes the people on Reddit are huffing so I can avoid those more effectively?";

            if (text == null) return;

            try {
                Field splashField = TitleScreen.class.getDeclaredField("splash");
                splashField.setAccessible(true);

                splashField.set(main, new SplashRenderer(text));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, world, pos, tintIndex) -> {
                    int variant = state.getValue(SellafieldSlakedBlock.COLOR_LEVEL);
                    return Color.HSBtoRGB(0F, 0F, 1F - variant / 15F);
                },
                ModBlocks.SELLAFIELD_SLAKED.get(),
                ModBlocks.SELLAFIELD_BEDROCK.get(),
                ModBlocks.ORE_SELLAFIELD_DIAMOND.get(),
                ModBlocks.ORE_SELLAFIELD_EMERALD.get()
        );
        event.register(
                (state, world, pos, tintIndex) -> {
                    int age = state.getValue(BalefireBlock.AGE);
                    return Color.HSBtoRGB(0F, 0F, 1F - age / 30F);
                },
                ModBlocks.BALEFIRE.get()
        );
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> event.getBlockColors().getColor(
                        ((BlockItem) stack.getItem()).getBlock().defaultBlockState(),
                        null, null, tintIndex
                ),
                ModBlocks.SELLAFIELD_SLAKED.get(),
                ModBlocks.SELLAFIELD_BEDROCK.get(),
                ModBlocks.ORE_SELLAFIELD_DIAMOND.get(),
                ModBlocks.ORE_SELLAFIELD_EMERALD.get()
        );
        event.register(
                (stack, tintIndex) -> 0xFF000000 | FluidTypeComponent.getFluidType(stack).getColor(),
                ModItems.FLUID_ICON.get()
        );
        event.register(
                (stack, tintIndex) -> {
                    if (tintIndex == 1) {
                        return 0xFF000000 | FluidTypeComponent.getFluidType(stack).getColor();
                    }
                    return 0xFFFFFFFF;
                },
                ModItems.FLUID_IDENTIFIER_MULTI.get(),
                ModItems.FLUID_TANK_FULL.get(),
                ModItems.FLUID_TANK_LEAD_FULL.get(),
                ModItems.FLUID_BARREL_FULL.get(),
                ModItems.FLUID_PACK_FULL.get()
        );
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            Clock.update();
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getLevel().isClientSide) return;
        Minecraft mc = Minecraft.getInstance();

        if (event.getItemStack().getItem() instanceof IGUIProvider provider) {
            mc.setScreen(provider.provideScreenOnRightClick(mc.player, event.getPos()));
        }
    }

    // uhh sure, no more convenient solutions...
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (!level.isClientSide) return;
        BlockHitResult bhr = event.getHitVec();

        Minecraft mc = Minecraft.getInstance();

        if (bhr.getType() == HitResult.Type.BLOCK && level.getBlockState(event.getPos()).getBlock() instanceof IGUIProvider provider) {
            mc.setScreen(provider.provideScreenOnRightClick(mc.player, mc.player.blockPosition()));
        }

        if (bhr.getType() == HitResult.Type.BLOCK && level.getBlockEntity(event.getPos()) instanceof IGUIProvider provider) {
            mc.setScreen(provider.provideScreenOnRightClick(mc.player, mc.player.blockPosition()));
        }
    }

    private static final HashMap<Integer, Long> vanished = new HashMap<>();
    public static void vanish(int ent) { vanished.put(ent, System.currentTimeMillis() + 2000); }
    public static void vanish(int ent, int duration) { vanished.put(ent, System.currentTimeMillis() + duration); }

    public static boolean isVanished(Entity e) {
        if (e == null) return false;
        if (!vanished.containsKey(e.getId())) return false;
        return vanished.get(e.getId()) > System.currentTimeMillis();
    }

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<?>> event) {
        if (isVanished(event.getEntity())) event.setCanceled(true);
        if (!(event.getRenderer().getModel() instanceof HumanoidModel<?> model)) return;

        ItemStack mainHand = event.getEntity().getMainHandItem();
        ItemStack offHand = event.getEntity().getOffhandItem();

        if (mainHand.getItem() instanceof IHoldableWeapon) {
            model.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
        if (offHand.getItem() instanceof IHoldableWeapon ihw && ihw.shouldChangeOffhand()) {
            model.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelShrapnel.LAYER, ModelShrapnel::createBodyLayer);
        event.registerLayerDefinition(ModelRubble.LAYER, ModelRubble::createBodyLayer);

        event.registerLayerDefinition(SkeletonModel.SKELETON_PART_LAYER, SkeletonModel::createLayer);
    }

    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(HFRObjGeometryLoader.ID, HFRObjGeometryLoader.INSTANCE);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.DUCK.get(), DuckRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CREEPER_NUCLEAR.get(), CreeperNuclearRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.TNT_PRIMED_BASE.get(), RenderTNTPrimedBase::new);
        event.registerEntityRenderer(ModEntityTypes.NUKE_MK5.get(), EmptyEntityRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.NUKE_BALEFIRE.get(), EmptyEntityRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.NUKE_FALLOUT_RAIN.get(), RenderFallout::new);
        event.registerEntityRenderer(ModEntityTypes.SHRAPNEL.get(), RenderShrapnel::new);
        event.registerEntityRenderer(ModEntityTypes.RUBBLE.get(), RenderRubble::new);
        
        event.registerEntityRenderer(ModEntityTypes.BLACK_HOLE.get(), RenderBlackHole::new);
        event.registerEntityRenderer(ModEntityTypes.VORTEX.get(), RenderBlackHole::new);
        event.registerEntityRenderer(ModEntityTypes.RAGING_VORTEX.get(), RenderBlackHole::new);
        event.registerEntityRenderer(ModEntityTypes.QUASAR.get(), RenderQuasar::new);

        event.registerEntityRenderer(ModEntityTypes.METEOR.get(), RenderMeteor::new);

        event.registerEntityRenderer(ModEntityTypes.DEATH_BLAST.get(), RenderDeathBlast::new);

        event.registerEntityRenderer(ModEntityTypes.BOMBER.get(), RenderBomber::new);
        event.registerEntityRenderer(ModEntityTypes.BOMBLET_ZETA.get(), RenderBombletZeta::new);

        ItemProperties.register(ModItems.POLAROID.get(), HBMsNTM.withDefaultNamespaceNT("polaroid_id"), (stack, level, entity, seed) -> PolaroidItem.polaroidID);

        event.registerBlockEntityRenderer(ModBlockEntities.NUKE_GADGET.get(), RenderNukeGadget::new);
        event.registerBlockEntityRenderer(ModBlockEntities.NUKE_LITTLE_BOY.get(), RenderNukeLittleBoy::new);
        event.registerBlockEntityRenderer(ModBlockEntities.NUKE_FAT_MAN.get(), RenderNukeFatMan::new);
        event.registerBlockEntityRenderer(ModBlockEntities.NUKE_IVY_MIKE.get(), RenderNukeIvyMike::new);
        event.registerBlockEntityRenderer(ModBlockEntities.NUKE_TSAR_BOMBA.get(), RenderNukeTsarBomba::new);

        event.registerBlockEntityRenderer(ModBlockEntities.LANDMINE.get(), RenderLandMine::new);

        event.registerBlockEntityRenderer(ModBlockEntities.BARREL.get(), RenderBarrel::new);

        event.registerBlockEntityRenderer(ModBlockEntities.GEIGER_COUNTER.get(), RenderGeigerBlock::new);

        event.registerBlockEntityRenderer(ModBlockEntities.CRASHED_BOMB_BALEFIRE.get(), RenderCrashedBomb::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CRASHED_BOMB_CONVENTIONAL.get(), RenderCrashedBomb::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CRASHED_BOMB_NUKE.get(), RenderCrashedBomb::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CRASHED_BOMB_SALTED.get(), RenderCrashedBomb::new);

        event.registerBlockEntityRenderer(ModBlockEntities.PLUSHIE_YOMI.get(), RenderPlushie::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PLUSHIE_NUMBERNINE.get(), RenderPlushie::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PLUSHIE_HUNDUN.get(), RenderPlushie::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PLUSHIE_DERG.get(), RenderPlushie::new);

        event.registerBlockEntityRenderer(ModBlockEntities.FLUID_TANK.get(), RenderFluidTank::new);

        event.registerBlockEntityRenderer(ModBlockEntities.BATTERY_SOCKET.get(), RenderBatterySocket::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BATTERY_REDD.get(), RenderBatteryREDD::new);

        event.registerBlockEntityRenderer(ModBlockEntities.NETWORK_CABLE.get(), RenderCable::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DET_CORD.get(), RenderDetCord::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {

        registerItemRenderer(event, RenderFluidTankItem::new, ModBlocks.MACHINE_FLUID_TANK.asItem());
        registerItemRenderer(event, RenderLaserDetonator::new, ModItems.DETONATOR_LASER.get());

        registerItemRenderer(event, RenderBarrelItem::new,
                ModBlocks.BARREL_RED.asItem(),
                ModBlocks.BARREL_PINK.asItem()
        );

        registerItemRenderer(event, RenderNukeGadgetItem::new, ModBlocks.NUKE_GADGET.asItem());
        registerItemRenderer(event, RenderNukeLittleBoyItem::new, ModBlocks.NUKE_LITTLE_BOY.asItem());
        registerItemRenderer(event, RenderNukeFatManItem::new, ModBlocks.NUKE_FAT_MAN.asItem());
        registerItemRenderer(event, RenderNukeIvyMikeItem::new, ModBlocks.NUKE_IVY_MIKE.asItem());
        registerItemRenderer(event, RenderNukeTsarBombaItem::new, ModBlocks.NUKE_TSAR_BOMBA.asItem());

        registerItemRenderer(event, RenderPlushieItem::new,
                ModBlocks.PLUSHIE_YOMI.asItem(),
                ModBlocks.PLUSHIE_NUMBERNINE.asItem(),
                ModBlocks.PLUSHIE_HUNDUN.asItem(),
                ModBlocks.PLUSHIE_DERG.asItem()
        );

        registerItemRenderer(event, RenderGeigerItem::new, ModBlocks.GEIGER.asItem());

        registerItemRenderer(event, RenderCableItem::new, ModBlocks.CABLE.asItem());
        registerItemRenderer(event, RenderDetCordItem::new, ModBlocks.DET_CORD.asItem());

        registerItemRenderer(event, RenderBatterySocketItem::new, ModBlocks.MACHINE_BATTERY_SOCKET.asItem());

        registerItemRenderer(event, RenderBatteryPackItem::new,
                ModItems.BATTERY_PACK_REDSTONE.get(),
                ModItems.BATTERY_PACK_LEAD.get(),
                ModItems.BATTERY_PACK_LITHIUM.get(),
                ModItems.BATTERY_PACK_SODIUM.get(),
                ModItems.BATTERY_PACK_SCHRABIDIUM.get(),
                ModItems.BATTERY_PACK_QUANTUM.get(),
                ModItems.CAPACITOR_COPPER.get(),
                ModItems.CAPACITOR_GOLD.get(),
                ModItems.CAPACITOR_NIOBIUM.get(),
                ModItems.CAPACITOR_TANTALUM.get(),
                ModItems.CAPACITOR_BISMUTH.get(),
                ModItems.CAPACITOR_SPARK.get()
        );

        registerItemRenderer(event, RenderBatteryREDDItem::new, ModBlocks.MACHINE_BATTERY_REDD.asItem());
        registerItemRenderer(event, RenderCrashedBombItem::new,
                ModBlocks.CRASHED_BOMB_BALEFIRE.asItem(),
                ModBlocks.CRASHED_BOMB_CONVENTIONAL.asItem(),
                ModBlocks.CRASHED_BOMB_NUKE.asItem(),
                ModBlocks.CRASHED_BOMB_SALTED.asItem()
        );
        registerItemRenderer(event, RenderLandmineItem::new,
                ModBlocks.MINE_AP.asItem(),
                ModBlocks.MINE_HE.asItem(),
                ModBlocks.MINE_SHRAP.asItem(),
                ModBlocks.MINE_NAVAL.asItem(),
                ModBlocks.MINE_FAT.asItem()
        );
    }

    private static void registerItemRenderer(RegisterClientExtensionsEvent event, Supplier<BlockEntityWithoutLevelRenderer> rendererFactory, Item... items) {
        event.registerItem(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = rendererFactory.get();
                }
                return renderer;
            }
        }, items);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void drawTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> list = event.getToolTip();
        Item.TooltipContext context = event.getContext();
        TooltipFlag flag = event.getFlags();

        DamageResistanceHandler.addInfo(stack, list);
        HazardSystem.addFullTooltip(stack, list);
        HazmatRegistry.addInfo(list, context.level(), stack);
        ArmorRegistry.addTooltip(list, stack);

        if (flag.isAdvanced()) {
            boolean hasTags = event.getItemStack().getTags().findAny().isPresent();

            if (hasTags) {
                list.add(Component.empty());
                list.add(Component.literal("Item tags:").withStyle(ChatFormatting.BLUE));
                stack.getTags().map(TagKey::location).sorted(ResourceLocation::compareTo).forEach(location ->
                        list.add(Component.literal(" -" + location).withStyle(ChatFormatting.AQUA)
                ));
            }
            Item item = stack.getItem();
            if (item instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                boolean hasBlockTags = block.builtInRegistryHolder().tags().findAny().isPresent();

                if (hasBlockTags) {
                    list.add(Component.empty());
                    list.add(Component.literal("Block tags:").withStyle(ChatFormatting.GREEN));
                    block.builtInRegistryHolder().tags().map(TagKey::location).sorted(ResourceLocation::compareTo).forEach(location ->
                            list.add(Component.literal(" -" + location).withStyle(ChatFormatting.DARK_GREEN))
                    );
                }
            }
        }
    }
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.BASE_PARTICLE.get(), BaseParticle.Provider::new);
        event.registerSpecial(ModParticles.COOLING_TOWER.get(), new CoolingTowerParticle.Provider());
        event.registerSpecial(ModParticles.DIGAMMA_SMOKE.get(), new DigammaSmokeParticle.Provider());
        event.registerSpriteSet(ModParticles.RBMK_MUSH.get(), RBMKMushParticle.Provider::new);
        event.registerSpriteSet(ModParticles.HAZE.get(), HazeParticle.Provider::new);
        event.registerSpriteSet(ModParticles.GIBLET.get(), ParticleGiblet.Provider::new);
        event.registerSpecial(ModParticles.DEBRIS.get(), new ParticleDebris.Provider());
        event.registerSpecial(ModParticles.EX_SMOKE.get(), new ParticleExSmoke.Provider());
        event.registerSpecial(ModParticles.FOAM.get(), new ParticleFoam.Provider());
        event.registerSpecial(ModParticles.ASHES.get(), new AshesParticle.Provider());
        event.registerSpecial(ModParticles.AMAT_FLASH.get(), new AmatFlashParticle.Provider());
        event.registerSpriteSet(ModParticles.GAS_FLAME.get(), ParticleGasFlame.Provider::new);
        event.registerSpriteSet(ModParticles.DEAD_LEAF.get(), DeadLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticles.AURA.get(), ParticleAura.Provider::new);
        event.registerSpriteSet(ModParticles.RAD_FOG.get(), RadiationFogParticle.Provider::new);
        event.registerSpecial(ModParticles.ROCKET_FLAME.get(), new RocketFlameParticle.Provider());
        event.registerSpecial(ModParticles.SKELETON.get(), new SkeletonParticle.Provider());
        event.registerSpriteSet(ModParticles.HADRON.get(), ParticleHadron.Provider::new);
        event.registerSpriteSet(ModParticles.POWER_DEBUG.get(), DebugParticle.PowerProvider::new);
        event.registerSpriteSet(ModParticles.FLUID_DEBUG.get(), DebugParticle.FluidProvider::new);
        event.registerSpecial(ModParticles.SPARK.get(), new SparkParticle.Provider());

        event.registerSpriteSet(ModParticles.VANILLA_CLOUD.get(), PlayerCloudParticle.Provider::new);
    }

    public static void effectNT(CompoundTag data) {
        Minecraft mc = Minecraft.getInstance();

        mc.execute(() -> {
            Minecraft innerMc = Minecraft.getInstance();
            ClientLevel level = innerMc.level;
            if (level == null) return;

            Player player = innerMc.player;
            int particleSetting = innerMc.options.particles().get().getId();

            String type = data.getString("type");
            double x = data.getDouble("posX");
            double y = data.getDouble("posY");
            double z = data.getDouble("posZ");

            HBMsNTM.LOGGER.info("type = {}", data);

            RandomSource rand = RandomSource.create();

            if (ParticleCreators.particleCreators.containsKey(type)) {
                ParticleCreators.particleCreators.get(type).makeParticle(level, player, rand, x, y, z, data);
                return;
            }

            if ("radFog".equals(type)) {
                RadiationFogParticle fx = new RadiationFogParticle(level, x, y, z);
                innerMc.particleEngine.add(fx);
            }

            if ("missileContrail".equals(type)) {

                if (new Vec3(player.getX() - x, player.getY() - y, player.getZ() - z).length() > 350) return;

                float scale = data.contains("scale") ? data.getFloat("scale") : 1F;
                double mX = data.getDouble("moX");
                double mY = data.getDouble("moY");
                double mZ = data.getDouble("moZ");

                RocketFlameParticle particle = new RocketFlameParticle(level, x, y, z).setScale(scale);
                particle.setParticleSpeed(mX, mY, mZ);
                if (data.contains("maxAge")) particle.setMaxAge(data.getInt("maxAge"));
                innerMc.particleEngine.add(particle);
            }

            if ("smoke".equals(type)) {

                String mode = data.getString("mode");
                int count = Math.max(1, data.getInt("count"));

                if ("cloud".equals(mode)) {

                    for (int i = 0; i < count; i++) {
                        ParticleExSmoke particle = new ParticleExSmoke(level, x, y, z);
                        particle.setMotionX(rand.nextGaussian() * (1 + (count / 150)));
                        particle.setMotionY(rand.nextGaussian() * (1 + (count / 100)));
                        particle.setMotionZ(rand.nextGaussian() * (1 + (count / 150)));
                        if (rand.nextBoolean()) particle.setMotionX(Math.abs(particle.getMotionY()));
                        Minecraft.getInstance().particleEngine.add(particle);
                    }
                }

                if ("radial".equals(mode)) {

                    for (int i = 0; i < count; i++) {
                        ParticleExSmoke particle = new ParticleExSmoke(level, x, y, z);
                        particle.setMotionX(rand.nextGaussian() * (1 + (count / 50)));
                        particle.setMotionY(rand.nextGaussian() * (1 + (count / 50)));
                        particle.setMotionZ(rand.nextGaussian() * (1 + (count / 50)));
                        Minecraft.getInstance().particleEngine.add(particle);
                    }
                }

                if ("radialDigamma".equals(mode)) {

                    Vec3NT vec = new Vec3NT(2, 0, 0);
                    vec.rotateAroundYRad(rand.nextFloat() * (float)Math.PI * 2F);

                    for (int i = 0; i < count; i++) {
                        DigammaSmokeParticle particle = new DigammaSmokeParticle(level, x, y, z);
                        particle.setParticleSpeed(vec.xCoord, 0, vec.zCoord);
                        innerMc.particleEngine.add(particle);

                        vec.rotateAroundYRad((float)Math.PI * 2F / (float)count);
                    }
                }

                if ("shock".equals(mode)) {

                    double strength = data.getDouble("strength");

                    Vec3NT vec = new Vec3NT(strength, 0, 0);
                    vec.rotateAroundYRad(rand.nextInt(360));

                    for (int i = 0; i < count; i++) {
                        ParticleExSmoke particle = new ParticleExSmoke(level, x, y, z);
                        particle.setParticleSpeed(vec.xCoord, 0, vec.zCoord);
                        innerMc.particleEngine.add(particle);

                        vec.rotateAroundXRad((float)Math.PI * 2F / (float)count);
                    }
                }

                if ("shockRand".equals(mode)) {

                    double strength = data.getDouble("strength");

                    Vec3NT vec = new Vec3NT(strength, 0, 0);
                    vec.rotateAroundYRad(rand.nextInt(360));
                    double r;

                    for (int i = 0; i < count; i++) {
                        r = rand.nextDouble();
                        ParticleExSmoke particle = new ParticleExSmoke(level, x, y, z);
                        particle.setParticleSpeed(vec.xCoord * r, 0, vec.zCoord * r);
                        innerMc.particleEngine.add(particle);

                        vec.rotateAroundYRad(360 / count);
                    }
                }

                if ("wave".equals(mode)) {

                    double strength = data.getDouble("range");

                    Vec3NT vec = new Vec3NT(strength, 0, 0);

                    for (int i = 0; i < count; i++) {

                        vec.rotateAroundYRad((float) Math.toRadians(rand.nextFloat() * 360F));

                        ParticleExSmoke particle = new ParticleExSmoke(level, x + vec.xCoord, y, z + vec.zCoord);
                        particle.maxAge = 50;
                        particle.setParticleSpeed(0, 0, 0);
                        innerMc.particleEngine.add(particle);

                        vec.rotateAroundYRad(360 / count);
                    }
                }

                if ("foamSplash".equals(mode)) {

                    double strength = data.getDouble("range");

                    Vec3NT vec = new Vec3NT(strength, 0, 0);

                    for (int i = 0; i < count; i++) {

                        vec.rotateAroundYRad((float) Math.toRadians(rand.nextFloat() * 360F));

                        ParticleFoam particle = new ParticleFoam(level, x + vec.xCoord, y, z + vec.zCoord);
                        particle.setLifetime(50);
                        particle.setParticleSpeed(0, 0, 0);
                        innerMc.particleEngine.add(particle);

                        vec.rotateAroundYRad(360 / count);
                    }
                }
            }

            if ("exhaust".equals(type)) {

                String mode = data.getString("mode");

                if ("soyuz".equals(mode)) {

                    if (new Vec3(player.getX() - x, player.getY() - y, player.getZ() - z).length() > 350)
                        return;

                    int count = Math.max(1, data.getInt("count"));
                    double width = data.getDouble("width");

                    for (int i = 0; i < count; i++) {
                        RocketFlameParticle particle = new RocketFlameParticle(level, x + rand.nextGaussian() * width, y, z + rand.nextGaussian() * width);
                        particle.setParticleSpeed(0, -0.75 + rand.nextDouble() * 0.5, 0);
                        innerMc.particleEngine.add(particle);
                    }
                }

                if ("meteor".equals(mode)) {

                    if (new Vec3(player.getX() - x, player.getY() - y, player.getZ() - z).length() > 350)
                        return;

                    int count = Math.max(1, data.getInt("count"));
                    double width = data.getDouble("width");

                    for (int i = 0; i < count; i++) {

                        RocketFlameParticle particle = new RocketFlameParticle(level, x + rand.nextGaussian() * width, y + rand.nextGaussian() * width, z + rand.nextGaussian() * width);
                        innerMc.particleEngine.add(particle);
                    }
                }
            }

            if ("muke".contains(type)) {

                ParticleEngineNT.INSTANCE.add(new MukeFlashParticle(level, x, y, z, data.getBoolean("balefire")));
                ParticleEngineNT.INSTANCE.add(new MukeWaveParticle(level, x, y, z));

                //single swing: 			HT 15,  MHT 15
                //double swing: 			HT 60,  MHT 50

                if (player != null) {
                    player.hurtTime = 15;
                    player.hurtDuration = 15;
                }
            }

            if ("tinytot".contains(type)) {
                ParticleEngineNT.INSTANCE.add(new MukeWaveParticle(level, x, y, z));

                for (double d = 0.0D; d <= 1.6D; d += 0.1) {
                    MukeCloudParticle cloud = new MukeCloudParticle(level, x, y, z, rand.nextGaussian() * 0.05, d + rand.nextGaussian() * 0.02, rand.nextGaussian() * 0.05);
                    ParticleEngineNT.INSTANCE.add(cloud);
                }
                for (int i = 0; i < 50; i++) {
                    MukeCloudParticle cloud = new MukeCloudParticle(level, x, y + 0.5, z, rand.nextGaussian() * 0.5, rand.nextInt(5) == 0 ? 0.02 : 0, rand.nextGaussian() * 0.5);
                    ParticleEngineNT.INSTANCE.add(cloud);
                }
                for (int i = 0; i < 15; i++) {
                    double ix = rand.nextGaussian() * 0.2;
                    double iz = rand.nextGaussian() * 0.2;

                    if(ix * ix + iz * iz > 0.75) {
                        ix *= 0.5;
                        iz *= 0.5;
                    }

                    double iy = 1.6 + (rand.nextDouble() * 2 - 1) * (0.75 - (ix * ix + iz * iz)) * 0.5;

                    MukeCloudParticle cloud = new MukeCloudParticle(level, x, y, z, ix, iy + rand.nextGaussian() * 0.02, iz);
                    ParticleEngineNT.INSTANCE.add(cloud);
                }

                if (player != null) {
                    player.hurtTime = 15;
                    player.hurtDuration = 15;
                }
            }

            if ("tower".equals(type)) {
                if (particleSetting == 0 || (particleSetting == 1 && rand.nextBoolean())) {
                    CoolingTowerParticle particle = new CoolingTowerParticle(level, x, y, z);

                    particle.setLift(data.getFloat("lift"));
                    particle.setBaseScale(data.getFloat("base"));
                    particle.setMaxScale(data.getFloat("max"));
                    particle.setLife(data.getInt("life") / (particleSetting + 1));
                    if (data.contains("noWind")) particle.noWind();
                    if (data.contains("strafe")) particle.setStrafe(data.getFloat("strafe"));
                    if (data.contains("alpha")) particle.alphaMod(data.getFloat("alpha"));

                    if (data.contains("color")) {
                        int color = data.getInt("color");
                        particle.setColor((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F);
                    }

                    innerMc.particleEngine.add(particle);
                }
            }

            if ("network".equals(type)) {
                DebugParticle particle = null;
                double mX = data.getDouble("mX");
                double mY = data.getDouble("mY");
                double mZ = data.getDouble("mZ");

                if ("power".equals(data.getString("mode"))) {
                    particle = new DebugParticle(level, x, y, z, mX, mY, mZ);
                }
                if ("fluid".equals(data.getString("mode"))) {
                    int color = data.getInt("color");
                    particle = new DebugParticle(level, x, y, z, mX, mY, mZ, color);
                }

                if (particle != null) innerMc.particleEngine.add(particle);
            }


            if ("gasfire".equals(type)) {
                double mX = data.getDouble("mX");
                double mY = data.getDouble("mY");
                double mZ = data.getDouble("mZ");
                float scale = data.getFloat("scale");
                ParticleGasFlame fx = new ParticleGasFlame(level, x, y, z, mX, mY, mZ, scale > 0 ? scale : 0.5F);
                innerMc.particleEngine.add(fx);
            }

            if ("deadleaf".equals(type)) {
                if (particleSetting == 0 || (particleSetting == 1 && rand.nextBoolean())) {
                    innerMc.particleEngine.add(new DeadLeafParticle(level, x, y, z));
                }
            }

            if ("amat".equals(type)) {
                innerMc.particleEngine.add(new AmatFlashParticle(level, x, y, z, data.getFloat("scale")));
            }

            if ("radiation".equals(type)) {
                if (player == null) return;
                for (int i = 0; i < data.getInt("count"); i++) {
                    ParticleAura flash = new ParticleAura(
                            level,
                            player.getX() + rand.nextGaussian() * 4,
                            player.getY() + rand.nextGaussian() * 2,
                            player.getZ() + rand.nextGaussian() * 4,
                            0, 0, 0
                    );

                    flash.setColor(0F, 0.75F, 1F);
                    flash.setParticleSpeed(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
                    innerMc.particleEngine.add(flash);
                }
            }

            if ("sweat".equals(type)) {
                Entity entity = level.getEntity(data.getInt("entity"));
                BlockState state = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), data.getCompound("BlockState"));
                int count = data.getInt("count");

                if (entity instanceof LivingEntity) {
                    for (int i = 0; i < count; i++) {
                        double ix = entity.getBoundingBox().minX - 0.2 + (entity.getBoundingBox().maxX - entity.getBoundingBox().minX + 0.4) * rand.nextDouble();
                        double iy = entity.getBoundingBox().minY + (entity.getBoundingBox().maxY - entity.getBoundingBox().minY + 0.2) * rand.nextDouble();
                        double iz = entity.getBoundingBox().minZ - 0.2 + (entity.getBoundingBox().maxZ - entity.getBoundingBox().minZ + 0.4) * rand.nextDouble();

                        ParticleDust fx = new ParticleDust(level, ix, iy, iz, 0, 0, 0, state);
                        fx.setLifetime(150 + rand.nextInt(50));
                        fx.setOriginalSize();

                        innerMc.particleEngine.add(fx);
                    }
                }
            }

            if ("vomit".equals(type)) {
                Entity e = level.getEntity(data.getInt("entity"));
                int count = data.getInt("count") / (particleSetting + 1);

                if (e instanceof LivingEntity living) {
                    double ix = living.getX();
                    double iy = living.getY() + living.getEyeHeight();
                    double iz = living.getZ();

                    Vec3 vec = living.getLookAngle();

                    for (int i = 0; i < count; i++) {
                        String mode = data.getString("mode");

                        if ("normal".equals(mode)) {
                            ParticleDust fx = new ParticleDust(level,
                                    ix, iy, iz,
                                    (vec.x + rand.nextGaussian() * 0.2) * 0.2,
                                    (vec.y + rand.nextGaussian() * 0.2) * 0.2,
                                    (vec.z + rand.nextGaussian() * 0.2) * 0.2,
                                    rand.nextBoolean() ? Blocks.GREEN_TERRACOTTA.defaultBlockState() : Blocks.LIME_TERRACOTTA.defaultBlockState());
                            fx.setLifetime(150 + rand.nextInt(50));
                            fx.setOriginalSize();
                            innerMc.particleEngine.add(fx);
                        }

                        if ("blood".equals(mode)) {
                            ParticleDust fx = new ParticleDust(level,
                                    ix, iy, iz,
                                    (vec.x + rand.nextGaussian() * 0.2) * 0.2,
                                    (vec.y + rand.nextGaussian() * 0.2) * 0.2,
                                    (vec.z + rand.nextGaussian() * 0.2) * 0.2,
                                    Blocks.REDSTONE_BLOCK.defaultBlockState());
                            fx.setLifetime(150 + rand.nextInt(50));
                            fx.setOriginalSize();
                            innerMc.particleEngine.add(fx);
                        }

                        if ("smoke".equals(mode)) {
                            innerMc.particleEngine.createParticle(
                                    ParticleTypes.SMOKE,
                                    ix, iy, iz,
                                    (vec.x + rand.nextGaussian() * 0.1) * 0.05,
                                    (vec.y + rand.nextGaussian() * 0.1) * 0.05,
                                    (vec.z + rand.nextGaussian() * 0.1) * 0.05
                            );
                        }
                    }
                }
            }

            if ("vanillaExt".equals(type)) {
                double mX = data.getDouble("mX");
                double mY = data.getDouble("mY");
                double mZ = data.getDouble("mZ");

                Particle particle = null;

                if ("cloud".equals(data.getString("mode"))) {
                    particle = new PlayerCloudParticle(level, x, y, z, mX, mY, mZ);

                    if (data.contains("r")) {
                        float rng = rand.nextFloat() * 0.1F;
                        particle.setColor(data.getFloat("r") + rng, data.getFloat("g") + rng, data.getFloat("b") + rng);
                        ((PlayerCloudParticle) particle).scaleFactor = 7.5F;
                        particle.setParticleSpeed(0, 0, 0);
                    }
                }

                if ("townaura".equals(data.getString("mode"))) {
                    particle = new ParticleAura(level, x, y, z, 0, 0, 0);
                    float color = 0.5F + rand.nextFloat() * 0.5F;
                    particle.setColor(0.8F * color, 0.9F * color, 1.0F * color);
                    particle.setParticleSpeed(mX, mY, mZ);
                }


                if (particle != null) {
                    innerMc.particleEngine.add(particle);
                }
            }

            if ("tau".equals(type)) {
                for (int i = 0; i < data.getByte("count"); i++)
                    innerMc.particleEngine.add(new SparkParticle(level, x, y, z, rand.nextGaussian() * 0.05, 0.05, rand.nextGaussian() * 0.05));
                ParticleEngineNT.INSTANCE.add(new ParticleNTHadron(level, x, y, z));
            }

            if ("giblets".equals(type)) {
                int ent = data.getInt("ent");
                Entity e = level.getEntity(ent);

                if (e == null) return;

                vanish(e.getId());

                float width = e.getBbWidth();
                float height = e.getBbHeight();
                int gW = (int)(width / 0.25F);
                int gH = (int)(height / 0.25F);

                int count = (int) (gW * 1.5 * gH);

                if (data.contains("cDiv")) count = (int) Math.ceil(count / (double)data.getInt("cDiv"));

                boolean blowMeIntoTheGodDamnStratosphere = rand.nextInt(15) == 0;
                double mult = 1D;

                if (blowMeIntoTheGodDamnStratosphere) mult *= 10;

                for (int i = 0; i < count; i++) {
                    innerMc.particleEngine.add(new ParticleGiblet(level, x, y, z, rand.nextGaussian() * 0.25 * mult, rand.nextDouble() * mult, rand.nextGaussian() * 0.25 * mult));
                }
            }
        });
    }
}
