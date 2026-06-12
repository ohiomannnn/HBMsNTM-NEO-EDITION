package com.hbm.render.blockentity;

import com.hbm.blocks.NtmBlocks;
import com.hbm.blocks.generic.BobbleBlock;
import com.hbm.blocks.generic.BobbleBlock.BobbleBlockEntity;
import com.hbm.blocks.generic.BobbleBlock.BobbleType;
import com.hbm.inventory.MetaHelper;
import com.hbm.items.NtmItems;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustom;
import com.hbm.render.util.FullBright;
import com.hbm.render.util.RenderContext;
import com.hbm.util.EnumUtil;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class RenderBobble extends BlockEntityRendererNT<BobbleBlockEntity> implements IBEWLRProvider {

    @Override
    public BlockEntityRenderer<BobbleBlockEntity> create(Context context) {

        if(bobble == null) bobble = new HFRWavefrontObject("models/obj/trinkets/bobble.obj").asVBO();

        return new RenderBobble();
    }

    public static IModelCustom bobble;
    public static final ResourceLocation SOCKET_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/socket.png");
    public static final ResourceLocation GLOW_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/glow.png");
    public static final ResourceLocation LAMP_TEX = NuclearTechMod.withDefaultNamespace("textures/block/fluorescent_lamp.png");

    public static final ResourceLocation BOBBLE_VAULTBOY_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/vaultboy.png");
    public static final ResourceLocation BOBBLE_HBM_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/hbm.png");
    public static final ResourceLocation BOBBLE_PU238_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/pellet.png");
    public static final ResourceLocation BOBBLE_FRIZZLE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/frizzle.png");
    public static final ResourceLocation BOBBLE_VT_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/vt.png");
    public static final ResourceLocation BOBBLE_DOC_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/doctor17ph.png");
    public static final ResourceLocation BOBBLE_BLUE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/thebluehat.png");
    public static final ResourceLocation BOBBLE_PHEO_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/pheo.png");
    public static final ResourceLocation BOBBLE_ADAM_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/adam29.png");
    public static final ResourceLocation BOBBLE_UFFR_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/uffr.png");
    public static final ResourceLocation BOBBLE_VAER_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/vaer.png");
    public static final ResourceLocation BOBBLE_NOS_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/nos.png");
    public static final ResourceLocation BOBBLE_DRILLGON_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/drillgon200.png");
    public static final ResourceLocation BOBBLE_CIRNO_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/cirno.png");
    public static final ResourceLocation BOBBLE_MICROWAVE_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/microwave.png");
    public static final ResourceLocation BOBBLE_PEEP_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/peep.png");
    public static final ResourceLocation BOBBLE_MELLOW_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/mellowrpg8.png");
    public static final ResourceLocation BOBBLE_MELLOW_GLOW_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/mellowrpg8_glow.png");
    public static final ResourceLocation BOBBLE_ABEL_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/abel.png");
    public static final ResourceLocation BOBBLE_ABEL_GLOW_TEX = NuclearTechMod.withDefaultNamespace("textures/models/trinkets/abel_glow.png");

    private long time;

    @Override
    public void render(BobbleBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        time = System.currentTimeMillis();

        RenderContext.setup(poseStack, packedLight, packedOverlay);
        RenderContext.translate(0.5F, 0, 0.5F);

        float scale = 0.25F;
        RenderContext.scale(scale, scale, scale);

        BobbleType type = EnumUtil.grabEnumSafely(BobbleType.class, be.getMeta());

        RenderContext.mulPose(Axis.YN.rotationDegrees(22.5F * be.getBlockState().getValue(BobbleBlock.DIRECTION) + 90));
        renderBobble(type, buffer);

        RenderContext.end();
    }

    public void renderBobble(BobbleType type, MultiBufferSource buffer) {

        bindTexture(SOCKET_TEX);
        bobble.renderPart("Socket");

        switch(type) {
            case STRENGTH, PERCEPTION, ENDURANCE, CHARISMA, INTELLIGENCE, AGILITY, LUCK -> bindTexture(BOBBLE_VAULTBOY_TEX);
            case BOB ->       bindTexture(BOBBLE_HBM_TEX);
            case PU238 ->     bindTexture(BOBBLE_PU238_TEX);
            case FRIZZLE ->   bindTexture(BOBBLE_FRIZZLE_TEX);
            case VT ->        bindTexture(BOBBLE_VT_TEX);
            case DOC ->       bindTexture(BOBBLE_DOC_TEX);
            case BLUEHAT ->   bindTexture(BOBBLE_BLUE_TEX);
            case PHEO ->      bindTexture(BOBBLE_PHEO_TEX);
            case CIRNO ->     bindTexture(BOBBLE_CIRNO_TEX);
            case ADAM29 ->    bindTexture(BOBBLE_ADAM_TEX);
            case UFFR ->      bindTexture(BOBBLE_UFFR_TEX);
            case VAER ->      bindTexture(BOBBLE_VAER_TEX);
            case NOS ->       bindTexture(BOBBLE_NOS_TEX);
            case DRILLGON ->  bindTexture(BOBBLE_DRILLGON_TEX);
            case MICROWAVE -> bindTexture(BOBBLE_MICROWAVE_TEX);
            case PEEP ->      bindTexture(BOBBLE_PEEP_TEX);
            case MELLOW ->    bindTexture(BOBBLE_MELLOW_TEX);
            case ABEL ->      bindTexture(BOBBLE_ABEL_TEX);
            default ->        bindTexture(ResourceManager.EMPTY);
        }

        switch(type) {
            case PU238 -> renderPellet();
            case UFFR -> renderFumo();
            case DRILLGON -> renderDrillgon();
            default -> renderGuy(type, buffer);
        }

        RenderContext.pushPose();
        renderPost(type);
        RenderContext.popPose();

        renderSocket(type, buffer);
    }

    /*
     * RENDER STANDARD PLAYER MODEL
     */

    public static float[] rotLeftArm = {0, 0, 0};
    public static float[] rotRightArm = {0, 0, 0};
    public static float[] rotLeftLeg = {0, 0, 0};
    public static float[] rotRightLeg = {0, 0, 0};
    public static float rotBody = 0;
    public static float[] rotHead = {0, 0, 0};

    public void resetFigurineRotation() {
        rotLeftArm = new float[]{0, 0, 0};
        rotRightArm = new float[]{0, 0, 0};
        rotLeftLeg = new float[]{0, 0, 0};
        rotRightLeg = new float[]{0, 0, 0};
        rotBody = 0;
        rotHead = new float[]{0, 0, 0};
    }

    public void setupFigurineRotation(BobbleType type) {
        switch(type) {
            case STRENGTH -> {
                rotLeftArm = new float[]{0, 25, 135};
                rotRightArm = new float[]{0, -45, 135};
                rotLeftLeg = new float[]{0, 0, -5};
                rotRightLeg = new float[]{0, 0, 5};
                rotHead = new float[]{15, 0, 0};
            }
            case PERCEPTION -> {
                rotLeftArm = new float[]{0, -15, 135};
                rotRightArm = new float[]{-5, 0, 0};
            }
            case ENDURANCE -> {
                rotBody = 45;
                rotLeftArm = new float[]{0, -25, 30};
                rotRightArm = new float[]{0, 45, 30};
                rotHead = new float[]{0, -45, 0};
            }
            case CHARISMA -> {
                rotBody = 45;
                rotRightArm = new float[]{0, -45, 90};
                rotLeftLeg = new float[]{0, 0, -5};
                rotRightLeg = new float[]{0, 0, 5};
                rotHead = new float[]{-5, -45, 0};
            }
            case INTELLIGENCE -> {
                rotHead = new float[]{0, 30, 0};
                rotLeftArm = new float[]{5, 0, 0};
                rotRightArm = new float[]{15, 0, 170};
            }
            case AGILITY -> {
                rotLeftArm = new float[]{0, 0, 60};
                rotRightArm = new float[]{0, 0, -45};
                rotLeftLeg = new float[]{0, 0, -15};
                rotRightLeg = new float[]{0, 0, 45};
            }
            case LUCK -> {
                rotLeftArm = new float[]{135, 45, 0};
                rotRightArm = new float[]{-135, -45, 0};
                rotRightLeg = new float[]{-5, 0, 0};
            }
            case VT -> {
                rotLeftArm = new float[]{0, -45, 60};
                rotRightArm = new float[]{0, 0, 45};
                rotLeftLeg = new float[]{2, 0, 0};
                rotRightLeg = new float[]{-2, 0, 0};
            }
            case BLUEHAT -> {
                rotLeftArm = new float[]{0, 90, 60};
            }
            case FRIZZLE -> {
                rotLeftArm = new float[]{0, 15, 45};
                rotRightArm = new float[]{0, 0, 80};
                rotLeftLeg = new float[]{0, 0, 2};
                rotRightLeg = new float[]{0, 0, -2};
            }
            case ADAM29 -> {
                rotRightArm = new float[]{0, 0, 60};
            }
            case PHEO -> {
                rotLeftArm = new float[]{0, 0, 80};
                rotRightArm = new float[]{0, 0, 45};
            }
            case VAER -> {
                rotLeftArm = new float[]{0, -5, 45};
                rotRightArm = new float[]{0, 15, 45};
            }
            case PEEP -> {
                rotLeftArm = new float[]{0, 0, 1};
                rotRightArm = new float[]{0, 0, 1};
            }
            case MELLOW -> {
                rotLeftArm = new float[]{0, 10, 0};
                rotRightArm = new float[]{0, -10, 0};
                rotLeftLeg = new float[]{3, 5, 2};
                rotRightLeg = new float[]{-3, -5, 0};
            }
            case ABEL -> {
                rotLeftArm = new float[]{0, 80, 90};
                rotRightArm = new float[]{0, -80, 90};
            }
        }
    }

    public void renderGuy(BobbleType type, @Nullable MultiBufferSource buffer) {

        this.resetFigurineRotation();
        this.setupFigurineRotation(type);

        RenderContext.pushPose();
        RenderContext.mulPose(Axis.YP.rotationDegrees(rotBody));

        if(type == BobbleType.PEEP) bobble.renderPart("PeepTail");

        RenderSystem.disableCull();

        String suffix = type.skinLayers ? "" : "17";

        //LEFT LEG//
        RenderContext.pushPose(); {
            RenderContext.translate(0, 1, -0.125F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(rotLeftLeg[0]));
            RenderContext.mulPose(Axis.YP.rotationDegrees(rotLeftLeg[1]));
            RenderContext.mulPose(Axis.ZP.rotationDegrees(rotLeftLeg[2]));
            RenderContext.translate(0, -1, 0.125F);
            bobble.renderPart("LL" + suffix);
        } RenderContext.popPose();

        //RIGHT LEG//
        RenderContext.pushPose(); {
            RenderContext.translate(0, 1, 0.125F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(rotRightLeg[0]));
            RenderContext.mulPose(Axis.YP.rotationDegrees(rotRightLeg[1]));
            RenderContext.mulPose(Axis.ZP.rotationDegrees(rotRightLeg[2]));
            RenderContext.translate(0, -1, -0.125F);
            bobble.renderPart("RL" + suffix);
        } RenderContext.popPose();

        //LEFT ARM//
        RenderContext.pushPose(); {
            RenderContext.translate(0, 1.625F, -0.25F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(rotLeftArm[0]));
            RenderContext.mulPose(Axis.YP.rotationDegrees(rotLeftArm[1]));
            RenderContext.mulPose(Axis.ZP.rotationDegrees(rotLeftArm[2]));
            RenderContext.translate(0, -1.625F, 0.25F);
            bobble.renderPart("LA" + suffix);
        } RenderContext.popPose();

        //RIGHT ARM//
        RenderContext.pushPose(); {
            RenderContext.translate(0, 1.625F, 0.25F);
            RenderContext.mulPose(Axis.XP.rotationDegrees(rotRightArm[0]));
            RenderContext.mulPose(Axis.YP.rotationDegrees(rotRightArm[1]));
            RenderContext.mulPose(Axis.ZP.rotationDegrees(rotRightArm[2]));
            RenderContext.translate(0, -1.625F, -0.25F);
            bobble.renderPart("RA" + suffix);
        } RenderContext.popPose();

        //BODY//
        bobble.renderPart("Body" + suffix);

        //HEAD//
        double speed = 0.005;
        double amplitude = 1;

        RenderContext.pushPose();
        RenderContext.translate(0, 1.75F, 0);
        RenderContext.mulPose(Axis.XP.rotationDegrees((float) (Math.sin(time * speed) * amplitude)));
        RenderContext.mulPose(Axis.ZP.rotationDegrees((float) (Math.sin(time * speed + (Math.PI * 0.5)) * amplitude)));

        RenderContext.mulPose(Axis.XP.rotationDegrees(rotHead[0]));
        RenderContext.mulPose(Axis.YP.rotationDegrees(rotHead[1]));
        RenderContext.mulPose(Axis.ZP.rotationDegrees(rotHead[2]));

        RenderContext.translate(0, -1.75F, 0);
        bobble.renderPart("Head" + suffix);

        if(type == BobbleType.VT) bobble.renderPart("Horn");
        if(type == BobbleType.PEEP) bobble.renderPart("PeepHat");

        if(type == BobbleType.VAER && buffer != null) {
            RenderContext.translate(0.6F, 1.82F, 0.075F);
            RenderContext.mulPose(Axis.ZP.rotationDegrees(-150F));
            RenderContext.scale(0.5F, 0.5F, 0.5F);
            ItemStack stack = new ItemStack(NtmItems.CIGARETTE.get());
            this.renderItem(stack, buffer);
        }

        if(type == BobbleType.NOS) {
            RenderContext.translate(0, 1.75F, 0);
            RenderContext.mulPose(Axis.XP.rotationDegrees(180F));
            float scale = 0.095F;
            RenderContext.scale(scale, scale, scale);
            this.bindTexture(ResourceManager.HAT_TEX);
            ResourceManager.armor_hat.renderAll();
        }

        RenderContext.popPose();

        RenderSystem.enableCull();

        RenderContext.popPose();
    }

    public void renderPellet() {

        RenderContext.pushPose();

        FullBright.enable();
        RenderContext.setLightning(false);
        bobble.renderPart("Pellet");

        bindTexture(ResourceManager.WHITE_TEX);
        RenderSystem.enableBlend();

        RenderContext.setColor(1.0F, 1.0F, 0.0F, 0.1F + (float) Math.sin(time * 0.001D) * 0.05F);
        bobble.renderPart("PelletShine");
        RenderContext.setColor(1.0F, 1.0F, 0.0F, 1.0F);

        RenderSystem.disableBlend();
        RenderContext.setLightning(true);
        FullBright.disable();

        RenderContext.popPose();
    }

    public void renderFumo() {

        bobble.renderPart("Fumo");

        double speed = 0.005;
        double amplitude = 1;

        RenderContext.pushPose();
        RenderContext.translate(0, 0.75F, 0);
        RenderContext.mulPose(Axis.XP.rotationDegrees((float) (Math.sin(time * speed) * amplitude)));
        RenderContext.mulPose(Axis.ZP.rotationDegrees((float) (Math.sin(time * speed + (Math.PI * 0.5)) * amplitude)));
        RenderContext.translate(0, -0.75F, 0);

        bobble.renderPart("FumoHead");

        RenderContext.popPose();
    }

    public void renderDrillgon() {
        bobble.renderPart("Drillgon");
    }

    /*
     * RENDER ADDITIONAL ITEMS
     */

    public void renderPost(BobbleType type) {
        switch(type) {
            case BLUEHAT -> {
                float scale = 0.0625F;
                RenderContext.translate(0F, 0.875F, -0.5F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(-90F));
                RenderContext.mulPose(Axis.ZP.rotationDegrees(-160F));
                RenderContext.scale(scale, scale, scale);
                bindTexture(ResourceManager.HEV_HELMET);
                ResourceManager.armor_hev.renderPart("Head");
            }
            case FRIZZLE -> {
                RenderContext.pushPose();
                RenderContext.translate(0.8F, 1.6F, 0.4F);
                RenderContext.scale(0.125F, 0.125F, 0.125F);
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.mulPose(Axis.XP.rotationDegrees(10F));
                bindTexture(ResourceManager.N_I_4_N_I_TEX);
                ResourceManager.n_i_4_n_i.renderPart("FrameDark");
                ResourceManager.n_i_4_n_i.renderPart("Grip");
                ResourceManager.n_i_4_n_i.renderPart("FrameLight");
                ResourceManager.n_i_4_n_i.renderPart("Cylinder");
                ResourceManager.n_i_4_n_i.renderPart("Barrel");
                RenderContext.popPose();

                RenderContext.translate(0.3F, 1.4F, -0.2F);
                RenderContext.mulPose(Axis.XP.rotationDegrees(-100F));
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                // todo add item renderer
            }
            case ADAM29 -> {
                // todo add item renderer
//                GL11.glTranslated(0.4, 1.15, 0.4);
//                GL11.glScaled(0.5, 0.5, 0.5);
//                renderItem(new ItemStack(ModItems.can_redbomb));
            }
            case PHEO -> {
                RenderContext.translate(0.5F, 1.15F, 0.45F);
                RenderContext.mulPose(Axis.XP.rotationDegrees(-60F));
                RenderContext.scale(2, 2, 2);
                bindTexture(ResourceManager.SHIMMER_AXE_TEX);
                ResourceManager.shimmer_axe.renderAll();
            }
            case BOB -> {
                RenderContext.pushPose();
                RenderContext.translate(0, 0.6875F, 0.625F);
                RenderContext.mulPose(Axis.XP.rotationDegrees(-90F));
                RenderContext.scale(0.125F, 0.125F, 0.125F);
                bindTexture(ResourceManager.FATMAN_MININUKE_TEX);
                RenderContext.translate(-6, 0, 0);
                for(int i = -1; i <= 1; i++) {
                    RenderContext.translate(3, 0, 0);
                    ResourceManager.fatman.renderPart("MiniNuke");
                }
                RenderContext.popPose();
                RenderContext.pushPose();
                RenderContext.translate(0.25F, 0.3125F, -0.5F);
                RenderContext.mulPose(Axis.XP.rotationDegrees(-90F));
                RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
                RenderContext.scale(0.1F, 0.1F, 0.1F);
                bindTexture(ResourceManager.DOUBLE_BARREL_SACRED_DRAGON_TEX);
                ResourceManager.double_barrel.renderPart("Stock");
                ResourceManager.double_barrel.renderPart("BarrelShort");
                ResourceManager.double_barrel.renderPart("Buckle");
                ResourceManager.double_barrel.renderPart("Lever");
                RenderContext.popPose();
            }
            // idk what it meant to be
            /*case VAER:
                this.bindTexture(shot_tex);
                GL11.glTranslated(0.6, 1.5, 0);
                GL11.glRotated(140, 0, 0, 1);
                GL11.glRotated(-60, 0, 1, 0);
                GL11.glTranslated(-0.2, 0, 0);
                GL11.glScaled(0.5, 0.5, 0.5);
                //shotgun.renderDud(0.0625F);
                break;
                */
            case MELLOW -> {
                FullBright.enable();
                bindTexture(BOBBLE_MELLOW_GLOW_TEX);
                renderGuy(type, null);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
                // why the hell we are rendering lamp with additive blending????
                this.bindTexture(LAMP_TEX);
                bobble.renderPart("Fluoro");
                this.bindTexture(GLOW_TEX);
                bobble.renderPart("Glow");
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
                FullBright.disable();
            }
            case ABEL -> {
                FullBright.enable();
                bindTexture(BOBBLE_ABEL_GLOW_TEX);
                renderGuy(type, null);
                FullBright.disable();
            }
        }
    }

    private void renderItem(ItemStack stack, MultiBufferSource buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = renderer.getModel(stack, null, null, 0);
        renderer.render(stack, ItemDisplayContext.NONE, false, RenderContext.poseStack(), buffer, RenderContext.light(), RenderContext.overlay(), model);
    }

    /*
     * RENDER BASE PEDESTAL
     */
    public void renderSocket(BobbleType type, MultiBufferSource buffer) {
        Font font = Minecraft.getInstance().font;
        float f3 = 0.01F;
        RenderContext.translate(0.63F, 0.175F, 0F);
        RenderContext.scale(f3, -f3, f3);
        RenderContext.translate(0F, 0F, font.width(type.label) * 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(90F));
        RenderContext.translate(0F, 1F, 0F);
        font.drawInBatch(type.label, 0, 0, type == BobbleType.VT ? 0xff0000 : 0xffffff, false, RenderContext.poseStack().last().pose(), buffer, DisplayMode.NORMAL, 0, RenderContext.light());
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.BOBBLEHEAD.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0, -3.5F, 0);
                RenderContext.scale(10F, 10F, 10F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.5F, 0.5F, 0.5F);
                renderBobble(EnumUtil.grabEnumSafely(BobbleType.class, MetaHelper.getMeta(stack)), buffer);
            }
        };
    }
}
