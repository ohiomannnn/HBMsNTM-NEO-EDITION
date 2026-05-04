package com.hbm.datagen;

import com.hbm.main.NuclearTechMod;
import com.hbm.registry.NtmSoundEvents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class NtmSoundDefinitionsProvider extends SoundDefinitionsProvider {

    protected NtmSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, NuclearTechMod.MODID, helper);
    }

    @Override
    public void registerSounds() {

        /// WEAPONS
        this.add(NtmSoundEvents.MISSILE_TAKEOFF, SoundDefinition.definition()
                .with(sound("hbmsntm:weapon/missile_takeoff"))
        );
        this.add(NtmSoundEvents.MUKE_EXPLOSION, SoundDefinition.definition()
                .with(sound("hbmsntm:weapon/muke_explosion"))
        );
        this.add(NtmSoundEvents.ROBIN_EXPLOSION, SoundDefinition.definition()
                .with(sound("hbmsntm:weapon/robin_explosion"))
        );
        this.add(NtmSoundEvents.NUCLEAR_EXPLOSION, SoundDefinition.definition()
                .with(sound("hbmsntm:weapon/nuclear_explosion").stream())
        );
        this.add(NtmSoundEvents.EXPLOSION_LARGE_NEAR, SoundDefinition.definition()
                .with(sound("hbmsntm:weapon/explosion_large_near"))
        );
        this.add(NtmSoundEvents.EXPLOSION_LARGE_FAR, SoundDefinition.definition()
                .with(sound("hbmsntm:weapon/explosion_large_far"))
        );
        this.add(NtmSoundEvents.EXPLOSION_SMALL_NEAR, SoundDefinition.definition()
                .with(
                        sound("hbmsntm:weapon/explosion_small_near1"),
                        sound("hbmsntm:weapon/explosion_small_near2"),
                        sound("hbmsntm:weapon/explosion_small_near3")
                )
        );
        this.add(NtmSoundEvents.EXPLOSION_SMALL_FAR, SoundDefinition.definition()
                .with(
                        sound("hbmsntm:weapon/explosion_small_far1"),
                        sound("hbmsntm:weapon/explosion_small_far2")
                )
        );
        this.add(NtmSoundEvents.EXPLOSION_TINY, SoundDefinition.definition()
                .with(
                        sound("hbmsntm:weapon/explosion_tiny1"),
                        sound("hbmsntm:weapon/explosion_tiny2")
                )
        );
        this.add(NtmSoundEvents.FSTBMB_START, SoundDefinition.definition()
                .with(sound("hbmsntm:weapon/fstbmb_start"))
        );
        this.add(NtmSoundEvents.FSTBMB_PING, SoundDefinition.definition()
                .with(sound("hbmsntm:weapon/fstbmb_ping"))
        );
        /// FIRE WEAPONS
        this.add(NtmSoundEvents.FIRE_DISINTEGRATION, SoundDefinition.definition()
                .with(sound("hbmsntm:weapon/fire/disintegration"))
        );
        /// ENTITIES
        this.add(NtmSoundEvents.OLD_EXPLOSION, SoundDefinition.definition()
                .with(sound("hbmsntm:entity/old_explosion"))
        );
        this.add(NtmSoundEvents.BOMB_WHISTLE, SoundDefinition.definition()
                .with(sound("hbmsntm:entity/bomb_whistle"))
        );
        this.add(NtmSoundEvents.BOMBER_LOOP, SoundDefinition.definition()
                .with(sound("hbmsntm:entity/bomber_loop"))
        );
        this.add(NtmSoundEvents.BOMBER_SMALL_LOOP, SoundDefinition.definition()
                .with(sound("hbmsntm:entity/bomber_small_loop"))
        );
        this.add(NtmSoundEvents.PLANE_CRASH, SoundDefinition.definition()
                .with(sound("hbmsntm:entity/plane_crash"))
        );
        this.add(NtmSoundEvents.PLANE_SHOT_DOWN, SoundDefinition.definition()
                .with(sound("hbmsntm:entity/plane_shot_down"))
        );
        this.add(NtmSoundEvents.DUCC, SoundDefinition.definition()
                .with(
                        sound("hbmsntm:entity/ducc1"),
                        sound("hbmsntm:entity/ducc2")
                )
        );
        this.add(NtmSoundEvents.METEORITE_FALLING_LOOP, SoundDefinition.definition()
                .with(sound("hbmsntm:entity/meteorite_falling_loop"))
        );
        /// PLAYERS
        this.add(NtmSoundEvents.VOMIT, SoundDefinition.definition()
                .with(sound("hbmsntm:player/vomit"))
        );
        this.add(NtmSoundEvents.COUGH, SoundDefinition.definition()
                .with(
                        sound("hbmsntm:player/cough1"),
                        sound("hbmsntm:player/cough2"),
                        sound("hbmsntm:player/cough3"),
                        sound("hbmsntm:player/cough4")
                )
        );
        /// BLOCKS
        this.add(NtmSoundEvents.PIPE_PLACED, SoundDefinition.definition()
                .with(sound("hbmsntm:block/pipe_placed"))
        );
        this.add(NtmSoundEvents.FENSU_HUM, SoundDefinition.definition()
                .with(sound("hbmsntm:block/fensu_hum"))
        );
        this.add(NtmSoundEvents.DEBRIS, SoundDefinition.definition()
                .with(
                        sound("hbmsntm:block/debris1"),
                        sound("hbmsntm:block/debris2"),
                        sound("hbmsntm:block/debris3")
                )
        );
        this.add(NtmSoundEvents.LOCK_OPEN, SoundDefinition.definition()
                .with(sound("hbmsntm:block/lock_open"))
        );
        this.add(NtmSoundEvents.CRATE_CLOSE, SoundDefinition.definition()
                .with(sound("hbmsntm:block/crate_close"))
        );
        this.add(NtmSoundEvents.CRATE_OPEN, SoundDefinition.definition()
                .with(sound("hbmsntm:block/crate_open"))
        );
        this.add(NtmSoundEvents.SQUEAKY_TOY, SoundDefinition.definition()
                .with(sound("hbmsntm:block/squeaky_toy"))
        );
        this.add(NtmSoundEvents.HUNDUNS_MAGNIFICENT_HOWL, SoundDefinition.definition()
                .with(sound("hbmsntm:block/hunduns_magnificent_howl"))
        );
        /// ITEMS
        this.add(NtmSoundEvents.TECH_BLEEP, SoundDefinition.definition()
                .with(sound("hbmsntm:tool/tech_bleep"))
        );
        this.add(NtmSoundEvents.TECH_BOOP, SoundDefinition.definition()
                .with(sound("hbmsntm:tool/tech_boop"))
        );
        this.add(NtmSoundEvents.GEIGER1, SoundDefinition.definition().with(sound("hbmsntm:tool/geiger1")));
        this.add(NtmSoundEvents.GEIGER2, SoundDefinition.definition().with(sound("hbmsntm:tool/geiger2")));
        this.add(NtmSoundEvents.GEIGER3, SoundDefinition.definition().with(sound("hbmsntm:tool/geiger3")));
        this.add(NtmSoundEvents.GEIGER4, SoundDefinition.definition().with(sound("hbmsntm:tool/geiger4")));
        this.add(NtmSoundEvents.GEIGER5, SoundDefinition.definition().with(sound("hbmsntm:tool/geiger5")));
        this.add(NtmSoundEvents.GEIGER6, SoundDefinition.definition().with(sound("hbmsntm:tool/geiger6")));
        this.add(NtmSoundEvents.PIN_UNLOCK, SoundDefinition.definition()
                .with(sound("hbmsntm:tool/pin_unlock"))
        );
        this.add(NtmSoundEvents.PIN_BREAK, SoundDefinition.definition()
                .with(sound("hbmsntm:tool/pin_break"))
        );
    }
}
