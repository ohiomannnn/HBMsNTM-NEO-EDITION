package com.hbm.blocks;

import com.hbm.inventory.MetaHelper;
import com.hbm.util.EnumUtil;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public abstract class EnumMultiBlock extends MultiBlock {

    public Class<? extends Enum<?>> theEnum;
    public final boolean multiName;
    public final boolean multiTexture;

    public EnumMultiBlock(Properties properties, Class<? extends Enum<?>> theEnum, boolean multiName, boolean multiTexture) {
        super(properties);
        this.theEnum = theEnum;
        this.multiName = multiName;
        this.multiTexture = multiTexture;
    }

//    @Override
//    public void registerModel(BlockStateProvider provider, ResourceLocation modelLocation) {
//        if(multiTexture) {
//
//            Enum<?>[] enums = theEnum.getEnumConstants();
//            provider.getVariantBuilder(this).forAllStates(state -> {
//
//                int meta = state.getValue(NtmBlockStateProperties.META);
//                Enum<?> num = enums[meta];
//
//                String path = BuiltInRegistries.BLOCK.getKey(this).getPath() + "_" + num.name().toLowerCase(Locale.US);
//                ModelFile model = provider.models().cubeAll(path, provider.modLoc(path));
//
//                return ConfiguredModel.builder().modelFile(model).build();
//            });
//
//            ItemModelBuilder builder = provider.itemModels().getBuilder(modelLocation.toString());
//            for(int i = 0; i < enums.length; i++) {
//                builder.override()
//                        .predicate(NuclearTechMod.withDefaultNamespace("item_meta"), i)
//                        .model(provider.itemModels().getBuilder(modelLocation.getPath() + "_" + i)
//                                .parent(new ModelFile.UncheckedModelFile(modelLocation.getPath() + "_" + i)
//                        )).end();
//            }
//        }
//    }

    @Override
    public String getItemDescriptionId(ItemStack stack) {
        if(multiName) {
            Enum<?> num = EnumUtil.grabEnumSafely(theEnum, MetaHelper.getMeta(stack));
            return super.getDescriptionId() + "." + num.name().toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    @Override
    public int getSubCount() {
        return this.theEnum.getEnumConstants().length;
    }
}
