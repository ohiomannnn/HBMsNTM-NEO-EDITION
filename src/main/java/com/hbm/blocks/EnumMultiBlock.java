package com.hbm.blocks;

import com.hbm.blocks.states.NtmBlockStateProperties;
import com.hbm.inventory.MetaHelper;
import com.hbm.main.NuclearTechMod;
import com.hbm.util.EnumUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.Locale;

public class EnumMultiBlock extends MultiBlock {

    public Class<? extends Enum<?>> theEnum;
    public final boolean multiName;
    public final boolean multiTexture;

    public EnumMultiBlock(Properties properties, Class<? extends Enum<?>> theEnum, boolean multiName, boolean multiTexture) {
        super(properties);
        this.theEnum = theEnum;
        this.multiName = multiName;
        this.multiTexture = multiTexture;
    }

    // UNTESTED!!!!
    @Override
    public void registerModel(BlockStateProvider provider, ResourceLocation modelLocation) {
        if(multiTexture) {

            Enum<?>[] enums = theEnum.getEnumConstants();
            provider.getVariantBuilder(this).forAllStates(state -> {

                int meta = state.getValue(NtmBlockStateProperties.META);
                Enum<?> num = enums[meta];

                String path = BuiltInRegistries.BLOCK.getKey(this).getPath() + "_" + num.name().toLowerCase(Locale.US);
                ModelFile model = provider.models().cubeAll(path, provider.modLoc(path));

                return ConfiguredModel.builder().modelFile(model).build();
            });

            ItemModelBuilder builder = provider.itemModels().getBuilder(modelLocation.toString());
            for(int i = 0; i < enums.length; i++) {
                builder.override()
                        .predicate(NuclearTechMod.withDefaultNamespace("item_meta"), i)
                        .model(provider.itemModels().getBuilder(modelLocation.getPath() + "_" + i)
                                .parent(new ModelFile.UncheckedModelFile(modelLocation.getPath() + "_" + i)
                        )).end();
            }
        }
    }

    @Override
    public String getOverrideDescriptionId(ItemStack stack) {
        if(multiName) {
            Enum<?> num = EnumUtil.grabEnumSafely(theEnum, MetaHelper.getMeta(stack));
            return super.getDescriptionId() + "." + num.name().toLowerCase(Locale.US);
        } else {
            return super.getOverrideDescriptionId(stack);
        }
    }

    @Override
    public int getSubCount() {
        return this.theEnum.getEnumConstants().length;
    }
}
