package com.hbm.lib;

import com.hbm.HBMsNTM;
import com.hbm.extprop.HbmLivingAttachments;
import com.hbm.extprop.HbmPlayerAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, HBMsNTM.MODID);

    public static final Supplier<AttachmentType<HbmLivingAttachments>> LIVING_ATTACHMENT = ATTACHMENTS.register(
            "ntm_living_attachments",
            () -> AttachmentType.builder(HbmLivingAttachments::new)
                    .serialize(HbmLivingAttachments.CODEC)
                    .sync(HbmLivingAttachments.STREAM_CODEC)
                    .build()
    );

    public static final Supplier<AttachmentType<HbmPlayerAttachments>> PLAYER_ATTACHMENT = ATTACHMENTS.register(
            "ntm_player_attachments",
            () -> AttachmentType.builder(HbmPlayerAttachments::new)
                    .serialize(HbmPlayerAttachments.CODEC)
                    .sync(HbmPlayerAttachments.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }
}
