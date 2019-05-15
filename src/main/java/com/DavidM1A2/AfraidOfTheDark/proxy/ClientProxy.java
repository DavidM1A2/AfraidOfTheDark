/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.proxy;

import com.DavidM1A2.afraidofthedark.client.entity.bolt.*;
import com.DavidM1A2.afraidofthedark.client.entity.enaria.RenderEnaria;
import com.DavidM1A2.afraidofthedark.client.entity.enaria.RenderGhastlyEnaria;
import com.DavidM1A2.afraidofthedark.client.entity.enchantedSkeleton.RenderEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.client.entity.splinterDrone.RenderSplinterDrone;
import com.DavidM1A2.afraidofthedark.client.entity.splinterDrone.RenderSplinterDroneProjectile;
import com.DavidM1A2.afraidofthedark.client.entity.werewolf.RenderWerewolf;
import com.DavidM1A2.afraidofthedark.client.keybindings.ModKeybindings;
import com.DavidM1A2.afraidofthedark.client.tileEntity.voidChest.TileEntityVoidChestRenderer;
import com.DavidM1A2.afraidofthedark.common.entity.bolt.*;
import com.DavidM1A2.afraidofthedark.common.entity.enaria.EntityEnaria;
import com.DavidM1A2.afraidofthedark.common.entity.enaria.EntityGhastlyEnaria;
import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone;
import com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDroneProjectile;
import com.DavidM1A2.afraidofthedark.common.entity.werewolf.EntityWerewolf;
import com.DavidM1A2.afraidofthedark.common.event.ResearchOverlayHandler;
import com.DavidM1A2.afraidofthedark.common.tileEntity.TileEntityVoidChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Proxy that is only to be instantiated on the CLIENT side
 */
public class ClientProxy extends CommonProxy
{
    // Research overlay handler used to show when a player unlocks a research
    private final ResearchOverlayHandler RESEARCH_OVERLAY_HANDLER = new ResearchOverlayHandler();

    /**
     * Called to initialize entity renderers
     */
    @Override
    public void initializeEntityRenderers()
    {
        // Register all of our renderers
        RenderingRegistry.registerEntityRenderingHandler(EntityEnchantedSkeleton.class, RenderEnchantedSkeleton::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf.class, RenderWerewolf::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGhastlyEnaria.class, RenderGhastlyEnaria::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDrone.class, RenderSplinterDrone::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySplinterDroneProjectile.class, RenderSplinterDroneProjectile::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityEnaria.class, RenderEnaria::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWoodenBolt.class, RenderWoodenBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityIronBolt.class, RenderIronBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySilverBolt.class, RenderSilverBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityIgneousBolt.class, RenderIgneousBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityStarMetalBolt.class, RenderStarMetalBolt::new);
    }

    /**
     * Called to initialize tile entity renderers
     */
    @Override
    public void initializeTileEntityRenderers()
    {
        // Tell MC to render our void chest tile entity with the special renderer
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVoidChest.class, new TileEntityVoidChestRenderer());
    }

    /**
     * Called to register any key bindings
     */
    @Override
    public void registerKeyBindings()
    {
        for (KeyBinding keyBinding : ModKeybindings.KEY_BINDING_LIST)
            ClientRegistry.registerKeyBinding(keyBinding);
    }

    /**
     * @return The research overlay handler client side
     */
    @Override
    public ResearchOverlayHandler getResearchOverlay()
    {
        return RESEARCH_OVERLAY_HANDLER;
    }
}
