package com.davidm1a2.afraidofthedark.client

import com.davidm1a2.afraidofthedark.client.event.BlockEntityRendererRegister
import com.davidm1a2.afraidofthedark.client.event.BlockRenderTypeRegister
import com.davidm1a2.afraidofthedark.client.event.ClientLateRenderHandler
import com.davidm1a2.afraidofthedark.client.event.ClientNightmareHandler
import com.davidm1a2.afraidofthedark.client.event.ClientResearchOverlayHandler
import com.davidm1a2.afraidofthedark.client.event.ClientSpellFreezeHandler
import com.davidm1a2.afraidofthedark.client.event.DimensionRenderInfoRegister
import com.davidm1a2.afraidofthedark.client.event.EntityRendererRegister
import com.davidm1a2.afraidofthedark.client.event.ItemModelPropertyRegister
import com.davidm1a2.afraidofthedark.client.event.KeybindingRegister
import com.davidm1a2.afraidofthedark.client.event.ModColorRegister
import com.davidm1a2.afraidofthedark.common.IProxy
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import net.minecraftforge.eventbus.api.IEventBus

class ClientProxy : IProxy {
    override val researchOverlayHandler: ResearchOverlayHandler = ClientResearchOverlayHandler()

    override fun registerSidedHandlers(forgeBus: IEventBus, modBus: IEventBus) {
        forgeBus.register(researchOverlayHandler)
        forgeBus.register(ClientNightmareHandler())
        forgeBus.register(ClientSpellFreezeHandler())
        forgeBus.register(ClientLateRenderHandler())

        modBus.register(ModColorRegister())
        modBus.register(BlockRenderTypeRegister())
        modBus.register(EntityRendererRegister())
        modBus.register(BlockEntityRendererRegister())
        modBus.register(DimensionRenderInfoRegister())
        modBus.register(ItemModelPropertyRegister())
        modBus.register(KeybindingRegister())
    }
}