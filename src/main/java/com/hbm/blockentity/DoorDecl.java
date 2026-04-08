package com.hbm.blockentity;

import com.hbm.blockentity.door.IRenderDoors;
import com.hbm.render.anim.BusAnimation;
import com.hbm.render.loader.IModelCustomNamed;
import com.hbm.util.BobMathUtil;
import com.hbm.util.Clock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Consumer;

public abstract class DoorDecl {



    // TODO: bash drillgon to death for making this method like that, and for fucking up the documentation, like genuinely what the fuck is this
    /** Format: x, y, z, tangent amount 1 (how long the door would be if it moved
     up), tangent amount 2 (door places blocks in this direction), axis (0-x,
     1-y, 2-z) */
    public abstract int[][] getDoorOpenRanges();
    public abstract int[] getDimensions();
    public int[][] getExtraDimensions() { return null; };

    public int getBlockOffset() { return 0; }
    public boolean remoteControllable() { return false; }

    public float getDoorRangeOpenTime(int ticks, int idx) {
        return getNormTime(ticks);
    }

    public int timeToOpen() { return 20; }

    public float getNormTime(float time) {
        return getNormTime(time, 0, timeToOpen());
    }

    public float getNormTime(float time, float min, float max) {
        return BobMathUtil.remap01_clamp(time, min, max);
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTextureForPart(String partName) {
        return getTextureForPart(0, partName);
    }

    @OnlyIn(Dist.CLIENT) public ResourceLocation getTextureForPart(int skinIndex, String partName) { return null; }
    @OnlyIn(Dist.CLIENT) public IModelCustomNamed getModel() { return null; }
//    @OnlyIn(Dist.CLIENT) public AnimatedModel getAnimatedModel() { return null; }
//    @OnlyIn(Dist.CLIENT) public Animation getAnim() { return null; }

    @OnlyIn(Dist.CLIENT) public void getTranslation(String partName, float openTicks, boolean child, float[] trans) { set(trans, 0, 0, 0); }
    @OnlyIn(Dist.CLIENT) public void getRotation(String partName, float openTicks, float[] rot) { set(rot, 0, 0, 0); }
    @OnlyIn(Dist.CLIENT) public void getOrigin(String partName, float[] orig) { set(orig, 0, 0, 0); }
    @OnlyIn(Dist.CLIENT) public boolean doesRender(String partName, boolean child) { return true; }

    private static final String[] nothing = new String[] {};

    @OnlyIn(Dist.CLIENT) public String[] getChildren(String partName) { return nothing; }
    @OnlyIn(Dist.CLIENT) public double[][] getClippingPlanes() { return new double[][] {}; }
    @OnlyIn(Dist.CLIENT) public void doOffsetTransform() { }

    public AABB getBlockBound(int x, int y, int z, boolean open, boolean forCollision) {
        return open ? new AABB(0, 0, 0, 0, 0, 0) : new AABB(0, 0, 0, 1, 1, 1);
    }

    public boolean isLadder(boolean open) { return false; }
    public SoundEvent getOpenSoundLoop() { return null; }

    // Hack
    public SoundEvent getSoundLoop2() { return null; }
    public SoundEvent getCloseSoundLoop() { return getOpenSoundLoop(); }
    public SoundEvent getOpenSoundStart() { return null; }
    public SoundEvent getCloseSoundStart() { return getOpenSoundStart(); }
    public SoundEvent getOpenSoundEnd() { return null; }
    public SoundEvent getCloseSoundEnd() { return getOpenSoundEnd(); }

    public float getSoundVolume() { return 1; }

    public float[] set(float[] f, float x, float y, float z) {
        f[0] = x;
        f[1] = y;
        f[2] = z;
        return f;
    }

    public ResourceLocation[] getSEDNASkins() { return null; }
    public boolean hasSkins() { return getSkinCount() > 0; }

    public int getSkinCount() {
        ResourceLocation[] skins = this.getSEDNASkins();
        if(skins == null || skins.length <= 1) return 0;
        return skins.length;
    }

    public ResourceLocation getCyclingSkins() {
        ResourceLocation[] skins = this.getSEDNASkins();
        int index = (int) ((Clock.get_ms() % (skins.length * 1000)) / 1000);
        return skins[index];
    }

    public ResourceLocation getSkinFromIndex(int index) {
        ResourceLocation[] skins = this.getSEDNASkins();
        return skins[Math.abs(index) % skins.length];
    }

    // keyframe animation system sneakily stitched into the door decl
    public IRenderDoors getSEDNARenderer() { return null; }
    public BusAnimation getBusAnimation(byte state, byte skinIndex) { return null; }

    public com.hbm.render.anim.HbmAnimations.Animation getSEDNAAnim(byte state, byte skinIndex) {
        BusAnimation anim = this.getBusAnimation(state, skinIndex);
        if(anim != null) return new com.hbm.render.anim.HbmAnimations.Animation("DOOR_ANIM", System.currentTimeMillis(), anim);
        return null;
    }

    public Consumer<DoorGenericBlockEntity> onDoorUpdate() { return null; }
}
