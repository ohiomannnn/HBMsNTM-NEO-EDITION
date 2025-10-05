package com.hbm;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.SellafieldSlakedBlock;
import com.hbm.config.ModConfigs;
import com.hbm.entity.ModEntities;
import com.hbm.handler.HTTPHandler;
import com.hbm.handler.gui.GeigerGUI;
import com.hbm.hazard.HazardSystem;
import com.hbm.inventory.gui.LoadingScreenRendererNT;
import com.hbm.items.ModItems;
import com.hbm.particle.*;
import com.hbm.particle.ParticleDebris;
import com.hbm.particle.helper.ParticleCreators;
import com.hbm.render.EmptyRenderer;
import com.hbm.render.entity.effect.RenderFallout;
import com.hbm.render.entity.effect.RenderTorex;
import com.hbm.render.entity.mob.EntityDuckRenderer;
import com.hbm.render.util.RenderInfoSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.awt.*;
import java.util.List;

@Mod(value = HBMsNTM.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class HBMsNTMClient {
    public HBMsNTMClient(IEventBus modEventBus, ModContainer modContainer) {
        ModParticles.register(modEventBus);
        modEventBus.addListener(GeigerGUI::RegisterGuiLayers);
        modEventBus.addListener(this::registerParticles);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private static final LoadingScreenRendererNT LOADING_RENDERER = new LoadingScreenRendererNT(Minecraft.getInstance());

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();
        if (screen instanceof LevelLoadingScreen || screen instanceof ReceivingLevelScreen) {
            LOADING_RENDERER.render(event.getGuiGraphics(), -1);
        }
    }

    @SubscribeEvent
    public static void onJoin(ClientPlayerNetworkEvent.LoggingOut event) {
        LOADING_RENDERER.resetProgressAndMessage(" ");
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

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        player.sendSystemMessage(Component.literal("Loaded world with Hbm's Nuclear Tech Mod " + HBMsNTM.VERSION + " for Minecraft 1.21.1!"));
        player.sendSystemMessage(Component.literal("Ignore message below (its just for test)"));

        if(HTTPHandler.newVersion) {
            player.sendSystemMessage(
                    Component.literal("New version " + HTTPHandler.versionNumber + " is available! Click ")
                            .withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
                            .append(
                                    Component.literal("[here]")
                                            .withStyle(Style.EMPTY
                                                    .withColor(ChatFormatting.RED)
                                                    .withUnderlined(true)
                                                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                                                            "https://github.com/HbmMods/Hbm-s-Nuclear-Tech-GIT/releases"))
                                            )
                            )
                            .append(Component.literal(" to download!")
                                    .withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)))
            );
        }
    }

    public static final int flashDuration = 5_000;
    public static long flashTimestamp;
    public static final int shakeDuration = 1_500;
    public static long shakeTimestamp;

    @SubscribeEvent
    public static void onRenderGuiPre(RenderGuiEvent.Pre event) {
        if (!ModConfigs.CLIENT.NUKE_HUD_SHAKE.get()) return;

        long now = System.currentTimeMillis();
        long end = shakeTimestamp + shakeDuration;

        if (now < end) {
            float mult = (end - now) / (float) shakeDuration * 2.0F;
            double horizontal = Mth.clamp(Math.sin(now * 0.02), -0.7, 0.7) * 15;
            double vertical   = Mth.clamp(Math.sin(now * 0.01 + 2), -0.7, 0.7) * 3;

            GuiGraphics graphics = event.getGuiGraphics();
            graphics.pose().translate(horizontal * mult, vertical * mult, 0);
        }
    }

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        if (!ModConfigs.CLIENT.NUKE_HUD_FLASH.get()) return;

        long now = System.currentTimeMillis();
        long end = flashTimestamp + flashDuration;

        if (now < end) {
            float brightness = (end - now) / (float) flashDuration;

            GuiGraphics guiGraphics = event.getGuiGraphics();
            Minecraft mc = Minecraft.getInstance();
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            guiGraphics.fill(0, 0, width, height,
                    FastColor.ARGB32.color((int)(brightness * 255), 255, 255, 255));
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
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DUCK.get(), EntityDuckRenderer::new);
        event.registerEntityRenderer(ModEntities.CREEPER_NUCLEAR.get(), CreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.NUKE_MK5.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntities.NUKE_TOREX.get(), RenderTorex::new);
        event.registerEntityRenderer(ModEntities.FALLOUT_RAIN.get(), RenderFallout::new);

        ItemProperties.register(ModItems.POLAROID.get(),
                ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "polaroid_id"),
                (stack, level, entity, seed) -> CommonEvents.polaroidID);
    }
    @SubscribeEvent
    public static void drawTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> list = event.getToolTip();

        HazardSystem.addFullTooltip(stack, list);
    }
    private void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.MUKE_CLOUD.get(), ParticleMukeCloud.Provider::new);
        event.registerSpecial(ModParticles.DEBRIS.get(), new ParticleDebris.Provider());
        event.registerSpecial(ModParticles.AMAT_FLASH.get(), new ParticleAmatFlash.Provider());
        event.registerSpriteSet(ModParticles.MUKE_CLOUD_BF.get(), ParticleMukeCloud.Provider::new);
        event.registerSpriteSet(ModParticles.EXPLOSION_SMALL.get(), sprites -> {
            ModParticles.EXPLOSION_SMALL_SPRITES = sprites;
            return new ParticleExplosionSmall.Provider(sprites);
        });
        event.registerSpriteSet(ModParticles.MUKE_WAVE.get(), sprites -> {
            ModParticles.MUKE_WAVE_SPRITES = sprites;
            return new ParticleMukeWave.Provider(sprites);
        });
        event.registerSpriteSet(ModParticles.COOLING_TOWER.get(), sprites -> {
            ModParticles.COOLING_TOWER_SPRITES = sprites;
            return new ParticleCoolingTower.Provider(sprites);
        });
        event.registerSpriteSet(ModParticles.MUKE_FLASH.get(), sprites -> {
            ModParticles.MUKE_FLASH_SPRITES = sprites;
            return new ParticleMukeFlash.Provider(sprites);
        });
        event.registerSpriteSet(ModParticles.GAS_FLAME.get(), ParticleGasFlame.Provider::new);
        event.registerSpriteSet(ModParticles.DEAD_LEAF.get(), ParticleDeadLeaf.Provider::new);
        event.registerSpriteSet(ModParticles.AURA.get(), sprites -> {
            ModParticles.AURA_SPITES = sprites;
            return new ParticleAura.Provider(sprites);
        });
        event.registerSpriteSet(ModParticles.RAD_FOG.get(), sprites -> {
            ModParticles.RAD_FOG_SPRITES = sprites;
            return new ParticleRadiationFog.Provider(sprites);
        });
        event.registerSpriteSet(ModParticles.ROCKET_FLAME.get(), sprites -> {
            ModParticles.ROCKET_FLAME_SPRITES = sprites;
            return new ParticleRocketFlame.Provider(sprites);
        });
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

            RandomSource rand = RandomSource.create();

            if(ParticleCreators.particleCreators.containsKey(type)) {
                ParticleCreators.particleCreators.get(type).makeParticle(level, player, rand, x, y, z, data);
                return;
            }

//            if ("radFog".equals(type)) {
//                ParticleRadiationFog fx = new ParticleRadiationFog(level, x, y, z, 0.62F, 0.67F, 0.38F, 5F, ModParticles.RAD_FOG_SPRITES);
//                innerMc.particleEngine.add(fx);
//            }

            if ("muke".contains(type)) {
                ParticleMukeFlash flash = new ParticleMukeFlash(
                        level,
                        x, y, z,
                        data.getBoolean("balefire"),
                        ModParticles.MUKE_FLASH_SPRITES
                );
                ParticleMukeWave wave = new ParticleMukeWave(
                        level,
                        x, y, z,
                        ModParticles.MUKE_WAVE_SPRITES
                );

                innerMc.particleEngine.add(flash);
                innerMc.particleEngine.add(wave);

                if (player != null) {
                    player.hurtTime = 15;
                    player.hurtDuration = 15;
                }
            }

            if ("tower".equals(type)) {
                if (particleSetting == 0 || (particleSetting == 1 && rand.nextBoolean())) {
                    ParticleCoolingTower particle = new ParticleCoolingTower(level, x, y, z, ModParticles.COOLING_TOWER_SPRITES);

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

            if("amat".equals(type)) {
                Minecraft.getInstance().particleEngine.add(new ParticleAmatFlash(level, x, y, z, data.getFloat("scale")));
            }

            if ("radiation".equals(type)) {
                if (player == null) return;
                for (int i = 0; i < data.getInt("count"); i++) {
                    ParticleAura flash = new ParticleAura(
                            level,
                            player.getX() + rand.nextGaussian() * 4,
                            player.getY() + rand.nextGaussian() * 2,
                            player.getZ() + rand.nextGaussian() * 4,
                            0, 0, 0,
                            ModParticles.AURA_SPITES
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

                        HBMsNTM.LOGGER.info("rendering {}", fx);

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
                            level.addParticle(
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
        });
    }
}
