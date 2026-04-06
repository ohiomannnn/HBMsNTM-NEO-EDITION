package com.hbm.items;

import com.hbm.main.NuclearTechModClient;
import com.hbm.interfaces.IOrderedEnum;
import com.hbm.inventory.MetaHelper;
import com.hbm.main.NuclearTechMod;
import com.hbm.util.EnumUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;

import java.util.List;
import java.util.Locale;

public class EnumMultiItem extends Item implements IMetaItem {

    //hell yes, now we're thinking with enums!
    protected final Class<? extends Enum<?>> theEnum;
    protected final boolean multiName;
    protected final boolean multiTexture;

    public EnumMultiItem(Properties properties, Class<? extends Enum<?>> theEnum, boolean multiName, boolean multiTexture) {
        super(properties);
        this.theEnum = theEnum;
        this.multiName = multiName;
        if (multiTexture) NuclearTechModClient.registerMetaItemProperties(this);
        this.multiTexture = multiTexture;
    }

    @Override
    public void getSubItems(Item item, List<ItemStack> stacks) {

        Enum<?>[] order = theEnum.getEnumConstants();
        if(order[0] instanceof IOrderedEnum ord) order = ord.getOrder();

        for(int i = 0; i < order.length; i++) {
            stacks.add(MetaHelper.metaStack(new ItemStack(item, 1), order[i].ordinal()));
        }
    }

    @Override
    public void registerModel(ModelProvider<ItemModelBuilder> provider, ResourceLocation modelLocation) {
        if (multiTexture) {
            Enum<?>[] enums = theEnum.getEnumConstants();

            ItemModelBuilder builder = provider.getBuilder(modelLocation.toString());

            for(int i = 0; i < enums.length; i++) {
                Enum<?> num = enums[i];

                builder.override()
                        .predicate(NuclearTechMod.withDefaultNamespace("item_meta"), i)
                        .model(provider.getBuilder(modelLocation.getPath() + "_" + i)
                                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                                .texture("layer0", ResourceLocation.fromNamespaceAndPath(modelLocation.getNamespace(), "item/" + modelLocation.getPath() + "_" + num.name().toLowerCase(Locale.US))))
                        .end();
            }
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if(multiName) {
            Enum<?> num = EnumUtil.grabEnumSafely(theEnum, MetaHelper.getMeta(stack));
            return super.getDescriptionId() + "." + num.name().toLowerCase(Locale.US);
        } else {
            return super.getDescriptionId(stack);
        }
    }
}
