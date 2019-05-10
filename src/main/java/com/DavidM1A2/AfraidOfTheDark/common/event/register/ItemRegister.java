package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDSlab;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register item event and registers all of our items
 */
public class ItemRegister
{
    /**
     * Called by forge to register any of our items
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        // Register each item in our item list
        registry.registerAll(ModItems.ITEM_LIST);

        // For each block in our block list we register an item so that we can hold the block
        for (Block block : ModBlocks.BLOCK_LIST)
        {
            // If our block is a lower half slab, we register it using an ItemSlab not an ItemBlock
            if (block instanceof AOTDSlab && !((AOTDSlab) block).isDouble())
            // Cast our block to a slab, and also get a reference to the double slab from the half slab. Use the name of the half slab as the registry name
            {
                registry.register(new ItemSlab(block, (AOTDSlab) block, ((AOTDSlab) block).getOpposite()).setRegistryName(block.getRegistryName()));
            }
            // Otherwise register regularly
            else
            {
                registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }
        }
    }

    /**
     * Called by forge to register any of our item renderers
     *
     * @param event The event that signifies that ModelLoader is ready to receive item
     */
    @SubscribeEvent
    public void registerItemRenderers(ModelRegistryEvent event)
    {
        // Register models for all items in our mod
        for (Item item : ModItems.ITEM_LIST)
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));

        // Crossbows will have different textures for metadata states 0 to 3
        ModelLoader.setCustomModelResourceLocation(ModItems.CROSSBOW, 0, new ModelResourceLocation(Constants.MOD_ID + ":crossbow_unloaded", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.CROSSBOW, 1, new ModelResourceLocation(Constants.MOD_ID + ":crossbow_quarter", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.CROSSBOW, 2, new ModelResourceLocation(Constants.MOD_ID + ":crossbow_half", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.CROSSBOW, 3, new ModelResourceLocation(Constants.MOD_ID + ":crossbow_loaded", "inventory"));

        // The flask of souls has two different states, charged and not charged
        ModelLoader.setCustomModelResourceLocation(ModItems.FLASK_OF_SOULS, 0, new ModelResourceLocation(Constants.MOD_ID + ":flask_of_souls", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.FLASK_OF_SOULS, 1, new ModelResourceLocation(Constants.MOD_ID + ":flask_of_souls_charged", "inventory"));

        // The igneous sword has two different states, charged and not charged
        ModelLoader.setCustomModelResourceLocation(ModItems.IGNEOUS_SWORD, 0, new ModelResourceLocation(Constants.MOD_ID + ":igneous_sword", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.IGNEOUS_SWORD, 1, new ModelResourceLocation(Constants.MOD_ID + ":igneous_sword_full_charge", "inventory"));
    }
}
