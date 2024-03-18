package com.davidm1a2.afraidofthedark.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Exists as an extension of ICapabilitySerializable that does not @Nonnull the "cap" param. Should help it play better
 * with other mods & Kotlin.
 */
public interface INullableCapabilitySerializable<T extends Tag> extends ICapabilitySerializable<T> {
    @Nonnull
    @Override
    <V> LazyOptional<V> getCapability(final @Nullable Capability<V> cap, final @Nullable Direction side);
}
