package com.hbm.render.test;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// super mega giga hack
public final class GeneratedShaderResources implements ResourceProvider {

    private final ResourceProvider fallback;
    private final Map<ResourceLocation, byte[]> virtual = new HashMap<>();

    public GeneratedShaderResources(ResourceProvider fallback) {
        this.fallback = fallback;
    }

    public void put(ResourceLocation loc, String text) {
        virtual.put(loc, text.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Optional<Resource> getResource(ResourceLocation location) {
        byte[] data = virtual.get(location);
        if(data != null) return Optional.of(new Resource(DUMMY_PACK, () -> new ByteArrayInputStream(data)));
        return fallback.getResource(location);
    }

    private static final PackResources DUMMY_PACK = new PackResources() {

        @Override public String packId() { return "virtual_generated_shaders"; }
        @Override public @Nullable IoSupplier<InputStream> getRootResource(String... strings) { return null; }
        @Override public @Nullable IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) { return null; }
        @Override public void listResources(PackType packType, String s, String s1, ResourceOutput resourceOutput) {}
        @Override public Set<String> getNamespaces(PackType packType) { return Set.of(); }
        @Override public <T> @Nullable T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) { return null; }
        @Override public PackLocationInfo location() { return new PackLocationInfo("", Component.empty(), PackSource.BUILT_IN, Optional.empty()); }
        @Override public void close() {}
    };
}