package com.hbm.lib;

import com.hbm.HBMsNTM;
import com.hbm.extprop.LivingProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, HBMsNTM.MODID);

    public static final Supplier<AttachmentType<LivingProperties>> LIVING_PROPS =
            ATTACHMENTS.register("ntm_living_properties",
                    () -> AttachmentType.builder(LivingProperties::new)
                            .serialize(new IAttachmentSerializer<CompoundTag, LivingProperties>() {
                                @Override
                                public LivingProperties read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
                                    LivingProperties props = new LivingProperties(holder);
                                    props.deserializeNBT(tag);
                                    return props;
                                }

                                @Override
                                public CompoundTag write(LivingProperties attachment, HolderLookup.Provider provider) {
                                    return attachment.serializeNBT();
                                }
                            }).build()
            );

    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }
}
