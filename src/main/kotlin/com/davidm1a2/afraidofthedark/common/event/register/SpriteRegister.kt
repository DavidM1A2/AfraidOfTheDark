package com.davidm1a2.afraidofthedark.common.event.register

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Class used to register all of our mod sprites used for particle FX
 */
class SpriteRegister {
    /**
     * Registers our sprites to be used for particle FX
     *
     * @param event The event to register sprites into
     */
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    fun onTextureStitchEvent(event: TextureStitchEvent.Pre) {
        // There's normally only one map, but just to make sure ensure it's the right one
        if (event.map.basePath == "textures") {
            /*
            ModSprites.ENARIA_BASIC_ATTACK = event.map
                .registerSprite(ResourceLocation(Constants.MOD_ID, "particles/enaria_basic_attack"))
            ModSprites.ENARIA_SPELL_CAST = event.map
                .registerSprite(ResourceLocation(Constants.MOD_ID, "particles/enaria_spell_cast"))
            ModSprites.ENARIA_SPELL_CAST_2 = event.map
                .registerSprite(ResourceLocation(Constants.MOD_ID, "particles/enaria_spell_cast_2"))
            ModSprites.ENARIA_TELEPORT = event.map
                .registerSprite(ResourceLocation(Constants.MOD_ID, "particles/enaria_teleport"))
            ModSprites.SPELL_CAST = event.map.registerSprite(ResourceLocation(Constants.MOD_ID, "particles/spell_cast"))
            ModSprites.SPELL_HIT = event.map.registerSprite(ResourceLocation(Constants.MOD_ID, "particles/spell_hit"))
            ModSprites.SMOKE_SCREEN =
                event.map.registerSprite(ResourceLocation(Constants.MOD_ID, "particles/smoke_screen"))
            ModSprites.SPELL_LASER =
                event.map.registerSprite(ResourceLocation(Constants.MOD_ID, "particles/spell_laser"))
            ModSprites.ENCHANTED_FROG_SPAWN =
                event.map.registerSprite(ResourceLocation(Constants.MOD_ID, "particles/enchanted_frog_spawn"))
            ModSprites.ENARIAS_ALTAR =
                event.map.registerSprite(ResourceLocation(Constants.MOD_ID, "particles/enarias_altar"))

             */
        }
    }
}