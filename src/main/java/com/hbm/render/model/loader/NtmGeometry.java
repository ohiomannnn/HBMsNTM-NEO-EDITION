package com.hbm.render.model.loader;

import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.blocks.machine.NTMAnvilBlock;
import com.hbm.render.model.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class NtmGeometry implements IUnbakedGeometry<NtmGeometry> {

    public enum BakedModelType {
        BARBED_WIRE,
        SPIKES,
        BARREL,
        CABLE,
        DET_CORD,
        PIPE,
        ANVIL
    }

    private final BakedModelType type;

    public NtmGeometry(BakedModelType type) {
        this.type = type;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state, ItemOverrides overrides) {

        TextureAtlasSprite textureSprite = null;
        TextureAtlasSprite overlaySprite = null;

        if(context.hasMaterial("texture")) textureSprite = spriteGetter.apply(context.getMaterial("texture"));
        if(context.hasMaterial("overlay")) overlaySprite = spriteGetter.apply(context.getMaterial("overlay"));

        final TextureAtlasSprite finalTextureSprite = textureSprite;

        return switch(this.type) {
            case BARBED_WIRE -> new BarbedWireBakedModel(new HFRWavefrontObject("models/obj/block/barbed_wire.obj"), textureSprite);
            case SPIKES -> new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/block/spikes.obj"), textureSprite);
            case BARREL -> new SimpleWavefrontBakedModel(new HFRWavefrontObject("models/obj/block/barrel.obj"), textureSprite);
            case CABLE -> new CableBakedModel(new HFRWavefrontObject("models/obj/block/cable_neo.obj"), textureSprite);
            case DET_CORD -> new DetCordBakedModel(new HFRWavefrontObject("models/obj/block/cable_neo.obj"), textureSprite);
            case PIPE -> new PipeNeoBakedModel(new HFRWavefrontObject("models/obj/block/pipe_neo.obj"), textureSprite, overlaySprite);
            case ANVIL -> new SimpleWavefrontBakedModel(new HFRWavefrontObject(resolveAnvilModelPath(context)), textureSprite) {
                private List<BakedQuad> anvilQuads;
                private final List<BakedQuad>[] anvilWorldQuads = new List[4];

                @Override
                public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource random, ModelData data, @Nullable RenderType type) {
                    if(direction != null) {
                        return Collections.emptyList();
                    }

                    if(!data.has(IN_LEVEL)) {
                        if(this.anvilQuads == null) {
                            this.anvilQuads = bakeSimpleQuads(null, null, finalTextureSprite);
                        }
                        return this.anvilQuads;
                    }

                    Direction dir = Direction.NORTH;
                    if(blockState != null && blockState.hasProperty(NTMAnvilBlock.FACING)) {
                        dir = blockState.getValue(NTMAnvilBlock.FACING);
                    }

                    int index = switch(dir) {
                        case NORTH -> 0;
                        case SOUTH -> 1;
                        case WEST -> 2;
                        case EAST -> 3;
                        default -> 0;
                    };

                    List<BakedQuad> quads = this.anvilWorldQuads[index];
                    if(quads != null) return quads;

                    Matrix4f matrix = new Matrix4f();
                    matrix.translate(0.5F, 0.0F, 0.5F);
                    matrix.rotateY((float) Math.toRadians(dir.toYRot() + 90.0D));

                    quads = bakeSimpleQuads(null, matrix, finalTextureSprite);
                    this.anvilWorldQuads[index] = quads;
                    return quads;
                }
            };
        };
    }

    private static String resolveAnvilModelPath(IGeometryBakingContext context) {
        String modelName = context.getModelName();
        int colon = modelName.indexOf(':');
        String path = colon >= 0 ? modelName.substring(colon + 1) : modelName;
        int slash = path.lastIndexOf('/');
        String fileName = slash >= 0 ? path.substring(slash + 1) : path;
        int hash = fileName.indexOf('#');
        if(hash >= 0) fileName = fileName.substring(0, hash);
        int dot = fileName.lastIndexOf('.');
        if(dot >= 0) fileName = fileName.substring(dot + 1);
        if(!fileName.startsWith("anvil_")) fileName = "anvil_iron";
        return "models/obj/machines/" + fileName + ".obj";
    }
}
