package com.hbm.items.special;

import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.MetaHelper;
import com.hbm.items.EnumMultiItem;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.BreedingRodItem.BreedingRodType;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.EnumUtil;
import com.hbm.util.SoundUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StarterKitItem extends EnumMultiItem {

    // todo add more kits
    public enum KitType {
        /// NUKES
        GADGET,
        LITTLE_BOY,
        FAT_MAN,
        IVY_MIKE,
        TSAR_BOMBA,
        PROTOTYPE,
        FLEIJA,
        // No n2 or balefire bomb kit :(
    }

    public StarterKitItem(Properties properties) {
        super(properties, KitType.class, true, true);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        KitType type = EnumUtil.grabEnumSafely(KitType.class, MetaHelper.getMeta(stack));

        switch(type) {
            case GADGET -> {
                player.getInventory().add(new ItemStack(NtmBlocks.NUKE_GADGET.asItem(), 1));
                player.getInventory().add(new ItemStack(NtmItems.EARLY_EXPLOSIVE_LENSES.get(), 4));
                player.getInventory().add(new ItemStack(NtmItems.GADGET_WIREING.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.GADGET_CORE.get(), 1));
            }
            case LITTLE_BOY -> {
                player.getInventory().add(new ItemStack(NtmBlocks.NUKE_LITTLE_BOY.asItem(), 1));
                player.getInventory().add(new ItemStack(NtmItems.LITTLE_BOY_SHIELDING.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.LITTLE_BOY_TARGET.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.LITTLE_BOY_BULLET.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.LITTLE_BOY_PROPELLANT.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.LITTLE_BOY_IGNITER.get(), 1));
            }
            case FAT_MAN -> {
                player.getInventory().add(new ItemStack(NtmBlocks.NUKE_FAT_MAN.asItem(), 1));
                player.getInventory().add(new ItemStack(NtmItems.EARLY_EXPLOSIVE_LENSES.get(), 4));
                player.getInventory().add(new ItemStack(NtmItems.FAT_MAN_IGNITER.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FAT_MAN_CORE.get(), 1));
            }
            case IVY_MIKE -> {
                player.getInventory().add(new ItemStack(NtmBlocks.NUKE_IVY_MIKE.asItem(), 1));
                player.getInventory().add(new ItemStack(NtmItems.EARLY_EXPLOSIVE_LENSES.get(), 4));
                player.getInventory().add(new ItemStack(NtmItems.FAT_MAN_CORE.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.IVY_MIKE_CORE.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.IVY_MIKE_DEUT.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.IVY_MIKE_COOLING_UNIT.get(), 1));
            }
            case TSAR_BOMBA -> {
                player.getInventory().add(new ItemStack(NtmBlocks.NUKE_TSAR_BOMBA.asItem(), 1));
                player.getInventory().add(new ItemStack(NtmItems.EARLY_EXPLOSIVE_LENSES.get(), 4));
                player.getInventory().add(new ItemStack(NtmItems.FAT_MAN_CORE.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.TSAR_BOMBA_CORE.get(), 1));
            }
            case PROTOTYPE -> {
                player.getInventory().add(new ItemStack(NtmBlocks.NUKE_PROTOTYPE.asItem(), 1));
                player.getInventory().add(new ItemStack(NtmItems.IGNITER.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.CELL_SAS3.get(), 4));
                player.getInventory().add(MetaHelper.newStack(NtmItems.ROD_QUAD, 4, BreedingRodType.URANIUM));
                player.getInventory().add(MetaHelper.newStack(NtmItems.ROD_QUAD, 4, BreedingRodType.LEAD));
                player.getInventory().add(MetaHelper.newStack(NtmItems.ROD_QUAD, 2, BreedingRodType.NP237));
            }
            case FLEIJA -> {
                player.getInventory().add(new ItemStack(NtmBlocks.NUKE_FLEIJA.asItem(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_IGNITER.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_IGNITER.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_PROPELLANT.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_PROPELLANT.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_PROPELLANT.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_CORE.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_CORE.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_CORE.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_CORE.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_CORE.get(), 1));
                player.getInventory().add(new ItemStack(NtmItems.FLEIJA_CORE.get(), 1));
            }
        }

        SoundUtils.playAtEntity(player, NtmSoundEvents.UNPACK.get(), SoundSource.PLAYERS);
        stack.shrink(1);

        return InteractionResultHolder.consume(stack);
    }
}
