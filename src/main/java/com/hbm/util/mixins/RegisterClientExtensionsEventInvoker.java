package com.hbm.util.mixins;

import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RegisterClientExtensionsEvent.class)
public interface RegisterClientExtensionsEventInvoker {

    @Invoker("<init>")
    static RegisterClientExtensionsEvent create() {
        throw new AssertionError();
    }
}
