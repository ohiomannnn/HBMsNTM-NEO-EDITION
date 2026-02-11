package com.hbm.render.blockentity;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BlockEntityRendererNT<T extends BlockEntity> implements BlockEntityRendererProvider<T>, BlockEntityRenderer<T> { }
