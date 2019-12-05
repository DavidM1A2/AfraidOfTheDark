package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModSprites
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class used to register all of our mod sprites used for particle FX
 */
class SpriteRegister
{
    /**
     * Registers our sprites to be used for particle FX
     *
     * @param event The event to register sprites into
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onTextureStitchEvent(event: TextureStitchEvent.Pre)
    {
        // There's normally only one map, but just to make sure ensure it's the right one
        if (event.map.basePath == "textures")
        {
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
            ModSprites.SMOKE_SCREEN = event.map.registerSprite(ResourceLocation(Constants.MOD_ID, "particles/smoke_screen"))
            ModSprites.SPELL_LASER = event.map.registerSprite(ResourceLocation(Constants.MOD_ID, "particles/spell_laser"))
        }
    }
}