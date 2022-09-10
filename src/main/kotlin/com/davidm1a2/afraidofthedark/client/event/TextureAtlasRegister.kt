package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.common.constants.ModRenderMaterials
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class TextureAtlasRegister {
    @SubscribeEvent
    fun onTextureStitchEvent(event: TextureStitchEvent.Pre) {
        val sprites = ModRenderMaterials.ATLAS_TO_SPRITE[event.map.location()]
        if (sprites != null) {
            for (sprite in sprites) {
                event.addSprite(sprite.texture())
            }
        }
    }
}