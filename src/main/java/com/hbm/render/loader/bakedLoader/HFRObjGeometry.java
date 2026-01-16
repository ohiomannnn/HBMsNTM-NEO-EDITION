package com.hbm.render.loader.bakedLoader;

import com.hbm.render.loader.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.*;
import java.util.function.Function;

public class HFRObjGeometry implements IUnbakedGeometry<HFRObjGeometry> {

    private final ResourceLocation modelLocation;
    private final boolean smoothing;
    private final boolean asVbo;
    private final boolean renderAll;
    private final List<String> renderOnly;
    private final List<String> exclude;

    public HFRObjGeometry(ResourceLocation modelLocation, boolean smoothing, boolean asVbo, boolean renderAll, List<String> renderOnly, List<String> exclude) {
        this.modelLocation = modelLocation;
        this.smoothing = smoothing;
        this.asVbo = asVbo;
        this.renderAll = renderAll;
        this.renderOnly = renderOnly;
        this.exclude = exclude;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
        TextureAtlasSprite particleSprite = null;
        TextureAtlasSprite textureSprite = null;

        if (context.hasMaterial("particle")) {
            particleSprite = spriteGetter.apply(context.getMaterial("particle"));
        }

        if (context.hasMaterial("texture")) {
            textureSprite = spriteGetter.apply(context.getMaterial("texture"));
        }

        if (textureSprite == null) {
            textureSprite = particleSprite;
        }
        if (particleSprite == null) {
            particleSprite = textureSprite;
        }

        HFRWavefrontObject wavefrontObj = HFRObjModelCache.getOrLoad(modelLocation, smoothing);

        IModelCustom renderModel = asVbo ? wavefrontObj.asVBO() : wavefrontObj;

        Set<String> renderOnlySet = renderOnly.isEmpty() ? null : new HashSet<>(renderOnly);
        Set<String> excludeSet = exclude.isEmpty() ? null : new HashSet<>(exclude);

        ItemTransforms itemTransforms = context.getTransforms();

        Transformation rootTransformation = context.getRootTransform();
        Matrix4f rootTransform = rootTransformation.getMatrix();

        return new HFRObjBakedModel(
                renderModel,
                wavefrontObj,
                textureSprite,
                particleSprite,
                context.useAmbientOcclusion(),
                context.isGui3d(),
                context.useBlockLight(),
                modelState,
                renderAll,
                renderOnlySet,
                excludeSet,
                itemTransforms,
                rootTransform
        );
    }

    public ResourceLocation getModelLocation() {
        return modelLocation;
    }

    public boolean isSmoothing() {
        return smoothing;
    }

    public boolean isAsVbo() {
        return asVbo;
    }

    public static class HFRObjBakedModel implements IDynamicBakedModel {

        private final IModelCustom model;
        private final HFRWavefrontObject wavefrontObj;
        private final TextureAtlasSprite texture;
        private final TextureAtlasSprite particleSprite;
        private final boolean useAmbientOcclusion;
        private final boolean isGui3d;
        private final boolean usesBlockLight;
        private final ModelState modelState;
        private final boolean renderAll;
        private final Set<String> renderOnly;
        private final Set<String> exclude;

        private final ItemTransforms itemTransforms;
        private final Matrix4f rootTransform;

        private List<BakedQuad> cachedQuads;
        private final Object cacheLock = new Object();

        public HFRObjBakedModel(IModelCustom model, HFRWavefrontObject wavefrontObj,
                                TextureAtlasSprite texture, TextureAtlasSprite particleSprite,
                                boolean useAmbientOcclusion, boolean isGui3d, boolean usesBlockLight,
                                ModelState modelState, boolean renderAll,
                                @Nullable Set<String> renderOnly, @Nullable Set<String> exclude,
                                ItemTransforms itemTransforms, Matrix4f rootTransform) {
            this.model = model;
            this.wavefrontObj = wavefrontObj;
            this.texture = texture;
            this.particleSprite = particleSprite != null ? particleSprite : texture;
            this.useAmbientOcclusion = useAmbientOcclusion;
            this.isGui3d = isGui3d;
            this.usesBlockLight = usesBlockLight;
            this.modelState = modelState;
            this.renderAll = renderAll;
            this.renderOnly = renderOnly;
            this.exclude = exclude;
            this.itemTransforms = itemTransforms != null ? itemTransforms : ItemTransforms.NO_TRANSFORMS;
            this.rootTransform = rootTransform != null ? rootTransform : new Matrix4f();
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
            if (side != null) {
                return Collections.emptyList();
            }

            synchronized (cacheLock) {
                if (cachedQuads == null) {
                    cachedQuads = buildQuads();
                }
                return cachedQuads;
            }
        }

        private List<BakedQuad> buildQuads() {
            List<BakedQuad> quads = new ArrayList<>();

            if (texture == null) {
                return quads;
            }

            List<S_GroupObject> groupsToRender = getGroupsToRender();

            Matrix4f combinedTransform = new Matrix4f(rootTransform);
            combinedTransform.mul(modelState.getRotation().getMatrix());

            for (S_GroupObject group : groupsToRender) {
                for (S_Face face : group.faces) {
                    BakedQuad quad = createQuadFromFace(face, combinedTransform);
                    if (quad != null) {
                        quads.add(quad);
                    }
                }
            }

            return quads;
        }

        private List<S_GroupObject> getGroupsToRender() {
            List<S_GroupObject> result = new ArrayList<>();

            for (S_GroupObject group : wavefrontObj.groupObjects) {
                if (shouldRenderGroup(group.name)) {
                    result.add(group);
                }
            }

            return result;
        }

        private boolean shouldRenderGroup(String groupName) {
            if (renderAll) {
                return true;
            }

            boolean shouldRender = true;

            if (renderOnly != null && !renderOnly.isEmpty()) {
                shouldRender = renderOnly.contains(groupName);
            }

            if (shouldRender && exclude != null && exclude.contains(groupName)) {
                shouldRender = false;
            }

            return shouldRender;
        }

        private BakedQuad createQuadFromFace(S_Face face, Matrix4f transform) {
            if (face.vertices == null || face.vertices.length < 3) {
                return null;
            }

            int vertexCount = face.vertices.length;
            int[] indices = vertexCount == 3 ? new int[]{0, 1, 2, 2} : new int[]{0, 1, 2, 3};

            int[] vertexData = new int[32];

            Vertex faceNormal = face.faceNormal != null ? face.faceNormal : new Vertex(0, 1, 0);

            Matrix3f normalMatrix = new Matrix3f();
            transform.get3x3(normalMatrix);
            normalMatrix.invert();
            normalMatrix.transpose();

            for (int i = 0; i < 4; i++) {
                int idx = indices[i];
                idx = Math.min(idx, vertexCount - 1);

                Vertex v = face.vertices[idx];

                Vector4f pos = new Vector4f(v.x, v.y, v.z, 1.0f);
                pos.mul(transform);

                float u = 0, vCoord = 0;
                if (face.textureCoordinates != null && idx < face.textureCoordinates.length) {
                    u = face.textureCoordinates[idx].u;
                    vCoord = face.textureCoordinates[idx].v;
                }

                float atlasU = Mth.lerp(u, texture.getU0(), texture.getU1());
                float atlasV = Mth.lerp(vCoord, texture.getV0(), texture.getV1());

                Vertex normal = faceNormal;
                if (face.vertexNormals != null && idx < face.vertexNormals.length) {
                    normal = face.vertexNormals[idx];
                }

                Vector3f normalVec = new Vector3f(normal.x, normal.y, normal.z);
                normalVec.mul(normalMatrix);
                normalVec.normalize();

                int offset = i * 8;
                vertexData[offset + 0] = Float.floatToRawIntBits(pos.x);
                vertexData[offset + 1] = Float.floatToRawIntBits(pos.y);
                vertexData[offset + 2] = Float.floatToRawIntBits(pos.z);
                vertexData[offset + 3] = -1;
                vertexData[offset + 4] = Float.floatToRawIntBits(atlasU);
                vertexData[offset + 5] = Float.floatToRawIntBits(atlasV);
                vertexData[offset + 6] = 0;
                vertexData[offset + 7] = packNormal(normalVec.x, normalVec.y, normalVec.z);
            }

            Direction facing = calculateFacing(faceNormal, transform);

            return new BakedQuad(vertexData, 0, facing, texture, true);
        }

        private Direction calculateFacing(Vertex normal, Matrix4f transform) {
            Matrix3f normalMatrix = new Matrix3f();
            transform.get3x3(normalMatrix);
            normalMatrix.invert();
            normalMatrix.transpose();

            Vector3f normalVec = new Vector3f(normal.x, normal.y, normal.z);
            normalVec.mul(normalMatrix);
            normalVec.normalize();

            return Direction.getNearest(normalVec.x, normalVec.y, normalVec.z);
        }

        private int packNormal(float x, float y, float z) {
            int nx = ((byte)(x * 127.0f)) & 0xFF;
            int ny = ((byte)(y * 127.0f)) & 0xFF;
            int nz = ((byte)(z * 127.0f)) & 0xFF;
            return nx | (ny << 8) | (nz << 16);
        }

        @Override
        public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
            itemTransforms.getTransform(transformType).apply(applyLeftHandTransform, poseStack);
            return this;
        }

        @Override
        public boolean useAmbientOcclusion() {
            return useAmbientOcclusion;
        }

        @Override
        public boolean isGui3d() {
            return isGui3d;
        }

        @Override
        public boolean usesBlockLight() {
            return usesBlockLight;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return particleSprite;
        }

        @Override
        public TextureAtlasSprite getParticleIcon(ModelData data) {
            return particleSprite;
        }

        @Override
        public ItemOverrides getOverrides() {
            return ItemOverrides.EMPTY;
        }

        @Override
        public ItemTransforms getTransforms() {
            return itemTransforms;
        }

        public IModelCustom getRenderModel() {
            return model;
        }

        public HFRWavefrontObject getWavefrontObject() {
            return wavefrontObj;
        }

        public Set<String> getRenderOnly() {
            return renderOnly;
        }

        public Set<String> getExclude() {
            return exclude;
        }

        public boolean isRenderAll() {
            return renderAll;
        }

        public List<String> getAllGroupNames() {
            return wavefrontObj.getPartNames();
        }

        public List<String> getRenderedGroupNames() {
            List<String> result = new ArrayList<>();
            for (S_GroupObject group : wavefrontObj.groupObjects) {
                if (shouldRenderGroup(group.name)) {
                    result.add(group.name);
                }
            }
            return result;
        }
    }
}