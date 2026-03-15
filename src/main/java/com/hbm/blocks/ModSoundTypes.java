package com.hbm.blocks;

import com.hbm.HBMsNTM;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SoundType;

public class ModSoundTypes {

    public static final ModSoundType PIPE = ModSoundType
            .customDig(SoundType.METAL, HBMsNTM.MODID + ":block.pipe_placed", 0.85F, 0.85F)
            .enveloped(RandomSource.create())
            .pitchFunction((in, rand, type) -> {
                if (type == ModSoundType.SubType.BREAK) in -= 0.15F;
                return in + rand.nextFloat() * 0.2F;
            });
}
