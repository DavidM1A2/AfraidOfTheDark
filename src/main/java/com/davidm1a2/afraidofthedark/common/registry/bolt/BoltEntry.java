package com.davidm1a2.afraidofthedark.common.registry.bolt;

import com.davidm1a2.afraidofthedark.common.entity.bolt.EntityBolt;
import com.davidm1a2.afraidofthedark.common.registry.research.Research;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiFunction;

/**
 * Base class for all bolt entries, bolt entries are used to define new bolt item/entity/research/name types
 */
public abstract class BoltEntry extends IForgeRegistryEntry.Impl<BoltEntry>
{
    // The item that this bolt type is represented by
    private final Item boltItem;
    // The entity factory that creates this bolt once shot
    private final BiFunction<World, EntityPlayer, EntityBolt> boltEntityFactory;
    // The pre-requisite research that needs to be researched for this to be used
    private final Research preRequisite;

    /**
     * The constructor sets the class fields
     *
     * @param boltItem          The item that this bolt entry represents
     * @param boltEntityFactory The entity factory that creates this bolt once shot
     * @param preRequisite      The research that is required to use this bolt
     */
    public BoltEntry(Item boltItem, BiFunction<World, EntityPlayer, EntityBolt> boltEntityFactory, Research preRequisite)
    {
        // Initialize all fields
        this.boltItem = boltItem;
        this.boltEntityFactory = boltEntityFactory;
        this.preRequisite = preRequisite;
    }

    /**
     * @return The unlocalized name of the bolt entry
     */
    public String getUnLocalizedName()
    {
        return "bolt_entry." + this.getRegistryName().getResourcePath();
    }

    ///
    /// Getters for all fields
    ///

    public Item getBoltItem()
    {
        return this.boltItem;
    }

    public BiFunction<World, EntityPlayer, EntityBolt> getBoltEntityFactory()
    {
        return this.boltEntityFactory;
    }

    public Research getPreRequisite()
    {
        return this.preRequisite;
    }
}
