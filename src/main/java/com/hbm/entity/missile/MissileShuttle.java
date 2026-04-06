package com.hbm.entity.missile;

import com.hbm.items.NtmItems;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.network.toclient.AuxParticle;
import com.hbm.util.RayTraceResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class MissileShuttle extends MissileBaseNT {

    public MissileShuttle(EntityType<? extends MissileBaseNT> entityType, Level level) { super(entityType, level); }

    @Override
    public List<ItemStack> getDebris() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(NtmItems.NOTHING.get(), 8));
        list.add(new ItemStack(NtmItems.NOTHING.get(), 2));
        list.add(new ItemStack(NtmItems.NOTHING.get(), 1));
        list.add(new ItemStack(Blocks.GLASS_PANE, 2));
        return list;
    }

    @Override public ItemStack getMissileItemForInfo() { return new ItemStack(NtmItems.MISSILE_SHUTTLE.get()); }

    @Override public void onMissileImpact(RayTraceResult result) {
        this.explodeStandard(20F, 64, false);
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "rbmkmush");
        tag.putInt("scale", 10);
        if (this.level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(serverLevel, null, this.position.x, this.position.y, this.position.z, 250, new AuxParticle(tag, this.position.x, this.position.y + 1, this.position.z));
        }
        level.playSound(null, this.position.x, this.position.y, this.position.z, NtmSoundEvents.ROBIN_EXPLOSION.get(), SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F);
    }
    @Override public ItemStack getDebrisRareDrop() { return new ItemStack(NtmItems.MISSILE_GENERIC.get()); }
}
