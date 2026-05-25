package com.hbm.items.weapon.sedna;

import com.hbm.entity.projectile.BulletBaseMK4;
import com.hbm.entity.projectile.BulletBeamBase;
import com.hbm.interfaces.NotableComments;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.items.weapon.sedna.factory.GunFactory.Ammo;
import com.hbm.items.weapon.sedna.factory.GunFactory.AmmoSecret;
import com.hbm.particle.SpentCasing;
import com.hbm.particle.SpentCasing.CasingType;
import com.hbm.util.DamageResistanceHandler.DamageClass;
import com.hbm.util.RayTraceResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@NotableComments
public class BulletConfig {
    public static List<BulletConfig> configs = new ArrayList<>();

    public int id;

    public ComparableStack ammo;
    public ItemStack casingItem;
    public int casingAmount;
    /** How much ammo is added to a standard mag when loading one item */
    public int ammoReloadCount = 1;
    public float velocity = 10F;
    public float spread = 0F;
    public float wear = 1F;
    public int projectilesMin = 1;
    public int projectilesMax = 1;
    public ProjectileType pType = ProjectileType.BULLET;

    public float damageMult = 1.0F;
    public float armorThresholdNegation = 0.0F;
    public float armorPiercingPercent = 0.0F;
    public float knockbackMult = 0.1F;
    public float headshotMult = 1.25F;

    public DamageClass dmgClass = DamageClass.PHYSICAL;

    public float ricochetAngle = 5F;
    public int maxRicochetCount = 2;
    /** Whether damage dealt to an entity is subtracted from the projectile's damage on penetration */
    public boolean damageFalloffByPen = true;

    public Consumer<Entity> onUpdate;
    public BiConsumer<BulletBaseMK4, RayTraceResult> onImpact;
    public BiConsumer<BulletBeamBase, RayTraceResult> onImpactBeam; //fuck fuck fuck fuck i should have used a better base class here god dammit
    public BiConsumer<BulletBaseMK4, RayTraceResult> onRicochet; //= LAMBDA_STANDARD_RICOCHET;
    public BiConsumer<BulletBaseMK4, RayTraceResult> onEntityHit; //= LAMBDA_STANDARD_ENTITY_HIT;

    public double gravity = 0;
    public int expires = 30;
    public boolean impactsEntities = true;
    public boolean doesPenetrate = false;
    /** Whether projectiles ignore blocks entirely */
    public boolean isSpectral = false;
    public int selfDamageDelay = 2;

    public boolean blackPowder = false;
    public boolean renderRotations = true;
    public SpentCasing casing;
    public BiConsumer<BulletBaseMK4, Float> renderer;
    public BiConsumer<BulletBeamBase, Float> rendererBeam;

    public BulletConfig() {
        this.id = configs.size();
        configs.add(this);
    }

    /** Required for the clone() operation to reset the ID, otherwise the ID and config entry will be the same as the original */
    public BulletConfig forceReRegister() {
        this.id = configs.size();
        configs.add(this);
        return this;
    }

    public BulletConfig setBeam() {														this.pType = ProjectileType.BEAM; return this; }
    public BulletConfig setChunkloading() {												this.pType = ProjectileType.BULLET_CHUNKLOADING; return this; }
    public BulletConfig setItem(Item ammo) {											this.ammo = new ComparableStack(ammo); return this; }
    public BulletConfig setItem(ItemStack ammo) {										this.ammo = new ComparableStack(ammo); return this; }
    public BulletConfig setItem(ComparableStack ammo) {									this.ammo = ammo; return this; }
//    public BulletConfig setItem(Ammo ammo) {										    this.ammo = new ComparableStack(ModItems.ammo_standard, 1, ammo.ordinal()); return this; }
//    public BulletConfig setItem(AmmoSecret ammo) {									    this.ammo = new ComparableStack(ModItems.ammo_secret, 1, ammo.ordinal()); return this; }
//    public BulletConfig setCasing(ItemStack item, int amount) {							this.casingItem = item; this.casingAmount = amount; return this; }
//    public BulletConfig setCasing(CasingType item, int amount) {					    this.casingItem = DictFrame.fromOne(ModItems.casing, item); this.casingAmount = amount; return this; }
    public BulletConfig setReloadCount(int ammoReloadCount) {							this.ammoReloadCount = ammoReloadCount; return this; }
    public BulletConfig setVel(float velocity) {										this.velocity = velocity; return this; }
    public BulletConfig setSpread(float spread) {										this.spread = spread; return this; }
    public BulletConfig setWear(float wear) {											this.wear = wear; return this; }
    public BulletConfig setProjectiles(int amount) {									this.projectilesMin = this.projectilesMax = amount; return this; }
    public BulletConfig setProjectiles(int min, int max) {								this.projectilesMin = min; this.projectilesMax = max; return this; }
    public BulletConfig setDamage(float damageMult) {									this.damageMult = damageMult; return this; }
    public BulletConfig setThresholdNegation(float armorThresholdNegation) {			this.armorThresholdNegation = armorThresholdNegation; return this; }
    public BulletConfig setArmorPiercing(float armorPiercingPercent) {					this.armorPiercingPercent = armorPiercingPercent; return this; }
    public BulletConfig setKnockback(float knockbackMult) {								this.knockbackMult = knockbackMult; return this; }
    public BulletConfig setHeadshot(float headshotMult) {								this.headshotMult = headshotMult; return this; }
    public BulletConfig setupDamageClass(DamageClass clazz) {							this.dmgClass = clazz; return this; }
    public BulletConfig setRicochetAngle(float angle) {									this.ricochetAngle = angle; return this; }
    public BulletConfig setRicochetCount(int count) {									this.maxRicochetCount = count; return this; }
    public BulletConfig setDamageFalloffByPen(boolean falloff) {						this.damageFalloffByPen = falloff; return this; }
    public BulletConfig setGrav(double gravity) {										this.gravity = gravity; return this; }
    public BulletConfig setLife(int expires) {											this.expires = expires; return this; }
    public BulletConfig setImpactsEntities(boolean impact) {							this.impactsEntities = impact; return this; }
    public BulletConfig setDoesPenetrate(boolean pen) {									this.doesPenetrate = pen; return this; }
    public BulletConfig setSpectral(boolean spectral) {									this.isSpectral = spectral; return this; }
    public BulletConfig setSelfDamageDelay(int delay) {									this.selfDamageDelay = delay; return this; }
    public BulletConfig setBlackPowder(boolean bp) {									this.blackPowder = bp; return this; }
    public BulletConfig setRenderRotations(boolean rot) {								this.renderRotations = rot; return this; }
    public BulletConfig setCasing(SpentCasing casing) {									this.casing = casing; return this; }

    public BulletConfig setRenderer(BiConsumer<BulletBaseMK4, Float> renderer) {		this.renderer = renderer; return this; }
    public BulletConfig setRendererBeam(BiConsumer<BulletBeamBase, Float> renderer) {	    this.rendererBeam = renderer; return this; }

    public BulletConfig setOnUpdate(Consumer<Entity> lambda) {												this.onUpdate = lambda; return this; }
    public BulletConfig setOnRicochet(BiConsumer<BulletBaseMK4, RayTraceResult> lambda) {		    this.onRicochet = lambda; return this; }
    public BulletConfig setOnImpact(BiConsumer<BulletBaseMK4, RayTraceResult> lambda) {			    this.onImpact = lambda; return this; }
    public BulletConfig setOnBeamImpact(BiConsumer<BulletBeamBase, RayTraceResult> lambda) {	    this.onImpactBeam = lambda; return this; }
    public BulletConfig setOnEntityHit(BiConsumer<BulletBaseMK4, RayTraceResult> lambda) {		    this.onEntityHit = lambda; return this; }

    public enum ProjectileType {
        BULLET,
        BULLET_CHUNKLOADING,
        BEAM
    }
}
