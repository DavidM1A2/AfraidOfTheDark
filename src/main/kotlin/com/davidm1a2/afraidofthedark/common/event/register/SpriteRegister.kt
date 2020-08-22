package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModSprites
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
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
    fun onPreTextureStitchEvent(event: TextureStitchEvent.Pre) {
        // There's normally only one map, but just to make sure ensure it's the right one
        if (event.map.basePath == "textures") {
            event.map.registerSprite(Minecraft.getInstance().resourceManager, ResourceLocation(Constants.MOD_ID, "particles/enaria_basic_attack"))
            event.map.registerSprite(Minecraft.getInstance().resourceManager, ResourceLocation(Constants.MOD_ID, "particles/enaria_spell_cast"))
            event.map.registerSprite(Minecraft.getInstance().resourceManager, ResourceLocation(Constants.MOD_ID, "particles/enaria_spell_cast_2"))
            event.map.registerSprite(Minecraft.getInstance().resourceManager, ResourceLocation(Constants.MOD_ID, "particles/enaria_teleport"))
            event.map.registerSprite(Minecraft.getInstance().resourceManager, ResourceLocation(Constants.MOD_ID, "particles/spell_cast"))
            event.map.registerSprite(Minecraft.getInstance().resourceManager, ResourceLocation(Constants.MOD_ID, "particles/spell_hit"))
            event.map.registerSprite(Minecraft.getInstance().resourceManager, ResourceLocation(Constants.MOD_ID, "particles/smoke_screen"))
            event.map.registerSprite(Minecraft.getInstance().resourceManager, ResourceLocation(Constants.MOD_ID, "particles/spell_laser"))
            event.map.registerSprite(Minecraft.getInstance().resourceManager, ResourceLocation(Constants.MOD_ID, "particles/enchanted_frog_spawn"))
            event.map.registerSprite(Minecraft.getInstance().resourceManager, ResourceLocation(Constants.MOD_ID, "particles/enarias_altar"))
        }
    }

    /**
     * Loads our sprites to be used for particle FX
     *
     * @param event The event to load sprites from
     */
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    fun onPostTextureStitchEvent(event: TextureStitchEvent.Post) {
        // There's normally only one map, but just to make sure ensure it's the right one
        if (event.map.basePath == "textures") {
            ModSprites.ENARIA_BASIC_ATTACK = event.map.getSprite(ResourceLocation(Constants.MOD_ID, "particles/enaria_basic_attack"))
            ModSprites.ENARIA_SPELL_CAST = event.map.getSprite(ResourceLocation(Constants.MOD_ID, "particles/enaria_spell_cast"))
            ModSprites.ENARIA_SPELL_CAST_2 = event.map.getSprite(ResourceLocation(Constants.MOD_ID, "particles/enaria_spell_cast_2"))
            ModSprites.ENARIA_TELEPORT = event.map.getSprite(ResourceLocation(Constants.MOD_ID, "particles/enaria_teleport"))
            ModSprites.SPELL_CAST = event.map.getSprite(ResourceLocation(Constants.MOD_ID, "particles/spell_cast"))
            ModSprites.SPELL_HIT = event.map.getSprite(ResourceLocation(Constants.MOD_ID, "particles/spell_hit"))
            ModSprites.SMOKE_SCREEN = event.map.getSprite(ResourceLocation(Constants.MOD_ID, "particles/smoke_screen"))
            ModSprites.SPELL_LASER = event.map.getSprite(ResourceLocation(Constants.MOD_ID, "particles/spell_laser"))
            ModSprites.ENCHANTED_FROG_SPAWN = event.map.getSprite(ResourceLocation(Constants.MOD_ID, "particles/enchanted_frog_spawn"))
            ModSprites.ENARIAS_ALTAR = event.map.getSprite(ResourceLocation(Constants.MOD_ID, "particles/enarias_altar"))
        }
    }
}