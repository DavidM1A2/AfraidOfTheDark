package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModPotions;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class that receives the register potion event and registers all of our potions
 */
public class PotionRegister
{
    /**
     * Called by forge to register any of our potions
     *
     * @param event The event to register to
     */
    @SubscribeEvent
    public void registerPotions(RegistryEvent.Register<Potion> event)
    {
        IForgeRegistry<Potion> registry = event.getRegistry();

        // Register each potion in our potion list
        registry.registerAll(ModPotions.POTION_LIST);
    }
}
