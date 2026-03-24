package com.hbm.datagen;

import com.hbm.NuclearTechMod;
import com.hbm.registry.NtmDamageTypes;
import com.hbm.registry.tags.NtmDamageTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {

    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
        super(output, provider, NuclearTechMod.MODID, helper);
    }

    @Override
    @SuppressWarnings("unchecked") // no
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.IS_EXPLOSION)
                .add(
                        NtmDamageTypes.NUCLEAR_BLAST
                );

        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY)
                .add(
                        NtmDamageTypes.DIGAMMA
                );

        this.tag(DamageTypeTags.BYPASSES_ARMOR)
                .add(
                        NtmDamageTypes.DIGAMMA,
                        NtmDamageTypes.RADIATION,
                        NtmDamageTypes.ASBESTOS,
                        NtmDamageTypes.BLACKLUNG,
                        NtmDamageTypes.TAINT,
                        NtmDamageTypes.BANG,
                        NtmDamageTypes.LEAD,
                        NtmDamageTypes.MONOXIDE
                );

        this.tag(DamageTypeTags.IS_PROJECTILE)
                .add(
                        NtmDamageTypes.SHRAPNEL,
                        NtmDamageTypes.RUBBLE
                );

        this.tag(NtmDamageTypeTags.IS_ENERGY)
                .add(
                        NtmDamageTypes.LASER,
                        NtmDamageTypes.MICROWAVE,
                        NtmDamageTypes.SUBATOMIC,
                        NtmDamageTypes.ELECTRIC
                );

        this.tag(NtmDamageTypeTags.ABSOLUTE)
                .addTags(
                        DamageTypeTags.BYPASSES_EFFECTS,
                        DamageTypeTags.BYPASSES_RESISTANCE
                );
    }
}
