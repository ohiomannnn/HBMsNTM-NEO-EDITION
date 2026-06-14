package com.hbm.entity.item;

import api.hbm.conveyor.IConveyorItem;
import api.hbm.conveyor.IEnterableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class MovingItem extends MovingConveyorObject implements IConveyorItem {

    private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(MovingItem.class, EntityDataSerializers.ITEM_STACK);

    @Nullable public ItemEntity cacheForRender = null;

    public MovingItem(EntityType<? extends MovingItem> entityType, Level level) {
        super(entityType, level);
    }

    public void setItemStack(ItemStack stack) {
        this.entityData.set(ITEM, stack);
    }

    @Override
    public ItemStack getItemStack() {
        return this.entityData.get(ITEM);
    }

    /** Adds the item to the player's inventory */
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {

        if(!this.level.isClientSide && !this.isRemoved() && player.getInventory().add(this.getItemStack().copy())) {
            this.discard();
        }

        return InteractionResult.PASS;
    }

    /** Ensures the item is knocked off the belt due to non-player attacks (explosions, etc) */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
    }

    @Override
    public void enterBlock(IEnterableBlock enterable, BlockPos pos, Direction dir) {

    }

    @Override
    public boolean onLeaveConveyor() {

        if(this.isRemoved()) return true;

        this.discard();
        ItemEntity item = new ItemEntity(
                this.level,
                this.position().x + this.getDeltaMovement().x * 2,
                this.position().y + this.getDeltaMovement().y * 2,
                this.position().z + this.getDeltaMovement().z * 2,
                this.getItemStack()
        );
        item.setDeltaMovement(this.getDeltaMovement().x * 2, 0.1, this.getDeltaMovement().z * 2);
        item.hasImpulse = true;
        this.level.addFreshEntity(item);

        return true;
    }

    @Override
    protected void defineSynchedData(Builder builder) {
        builder.define(ITEM, ItemStack.EMPTY);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        ItemStack stack = tag.contains("Item", Tag.TAG_COMPOUND) ? ItemStack.parseOptional(this.registryAccess(), tag.getCompound("Item")) : ItemStack.EMPTY;

        this.setItemStack(stack);

        if(stack.isEmpty()) this.discard();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        ItemStack stack = this.getItemStack();

        if(!stack.isEmpty()) tag.put("Item", stack.save(this.registryAccess()));
    }
}
