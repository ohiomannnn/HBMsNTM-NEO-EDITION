package com.hbm.items;

import com.hbm.util.TagsUtil;
import com.hbm.util.ItemStackUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public abstract class ItemInventory extends SimpleContainer {

    protected Player player;
    protected ItemStack target;

    public ItemInventory(Player player, ItemStack target, int size) {
        super(size);
        this.player = player;
        this.target = target;
    }

    @Override
    public void setChanged() {
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty() && stack.getCount() == 0) {
                setItem(i, ItemStack.EMPTY);
            }
        }

        ItemStackUtil.addStacksToNBT(target, (HolderLookup.Provider) this.getItems());
        TagsUtil.setTag(target, checkNBT(TagsUtil.getTag(target)));
    }

    public CompoundTag checkNBT(CompoundTag tag) {
        if (tag == null || tag.isEmpty()) return null;

        Random random = new Random();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NbtIo.writeCompressed(tag, baos);
            byte[] data = baos.toByteArray();

            if (data.length > 6000) {
                player.displayClientMessage(Component.literal("§cWarning: Container NBT exceeds 6kB, contents will be ejected!"), false);

                Level level = player.level();
                for (int i = 0; i < getContainerSize(); i++) {
                    ItemStack stack = getItem(i);
                    if (!stack.isEmpty()) {
                        float fx = random.nextFloat() * 0.8F + 0.1F;
                        float fy = random.nextFloat() * 0.8F + 0.1F;
                        float fz = random.nextFloat() * 0.8F + 0.1F;

                        while (!stack.isEmpty()) {
                            int dropCount = Math.min(random.nextInt(21) + 10, stack.getCount());
                            ItemStack drop = stack.split(dropCount);

                            ItemEntity entity = new ItemEntity(
                                    level,
                                    player.getX() + fx,
                                    player.getY() + fy,
                                    player.getZ() + fz,
                                    drop
                            );

                            entity.setDeltaMovement(
                                    random.nextGaussian() * 0.05 + player.getDeltaMovement().x,
                                    random.nextGaussian() * 0.05 + 0.2 + player.getDeltaMovement().y,
                                    random.nextGaussian() * 0.05 + player.getDeltaMovement().z
                            );

                            level.addFreshEntity(entity);
                        }
                    }
                }

                return null;
            }
        } catch (IOException ignored) {}

        return tag;
    }

    public void openInventory() {
        player.level().playSound(null, player.blockPosition(), SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 1.0F, 0.8F);
    }

    public void closeInventory() {
        player.level().playSound(null, player.blockPosition(), SoundEvents.CHEST_CLOSE, SoundSource.PLAYERS, 1.0F, 0.8F);
    }
}
