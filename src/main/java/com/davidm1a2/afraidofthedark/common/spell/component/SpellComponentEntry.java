package com.davidm1a2.afraidofthedark.common.spell.component;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

/**
 * Class representing a base for spell delivery methods, power sources, and effects to be registered
 * in the forge registry
 *
 * @param <T> The type that will be registered
 * @param <V> The type that this entry will create
 */
public abstract class SpellComponentEntry<T extends IForgeRegistryEntry<T>, V extends SpellComponent> extends IForgeRegistryEntry.Impl<T>
{
    // A factory reference that creates instances for us
    private final Supplier<V> factory;
    // An icon resource location that is the icon in the UI of this component
    private final ResourceLocation icon;

    /**
     * Constructor sets the entry id, icon, and factory
     *
     * @param id The of the entry to register
     * @param factory The factory that creates the spell components
     */
    public SpellComponentEntry(ResourceLocation id, ResourceLocation icon, Supplier<V> factory)
    {
        this.setRegistryName(id);
        this.icon = icon;
        this.factory = factory;
    }

    /**
     * @return Gets the unlocalized name of the component
     */
    public abstract String getUnlocalizedName();

    /**
     * Utility method to fire our factory and make a new instance
     *
     * @return A new instance of the type this entry represents
     */
    public V newInstance()
    {
        return this.factory.get();
    }

    /**
     * A resource location containing an image file with the icon to be used by the component
     *
     * @return The component's icon
     */
    public ResourceLocation getIcon()
    {
        return icon;
    }
}
