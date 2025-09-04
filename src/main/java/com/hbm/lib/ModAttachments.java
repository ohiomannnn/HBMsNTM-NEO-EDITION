package com.hbm.lib;

import com.hbm.HBMsNTM;
import com.hbm.extprop.HbmLivingProps;
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

    public static final Supplier<AttachmentType<HbmLivingProps>> LIVING_PROPS =
            ATTACHMENTS.register("living_props",
                    () -> AttachmentType.builder(HbmLivingProps::new)
                            .serialize(new IAttachmentSerializer<CompoundTag, HbmLivingProps>() {
                                @Override
                                public HbmLivingProps read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
                                    HbmLivingProps props = new HbmLivingProps(holder);
                                    props.deserializeNBT(tag);
                                    return props;
                                }

                                @Override
                                public CompoundTag write(HbmLivingProps attachment, HolderLookup.Provider provider) {
                                    return attachment.serializeNBT();
                                }
                            })
                            .build()
            );

    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }
}
