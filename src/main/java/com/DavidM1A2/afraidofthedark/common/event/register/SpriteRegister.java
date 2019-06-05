package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModSprites;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class used to register all of our mod sprites used for particle FX
 */
public class SpriteRegister
{
    /**
     * Registers our sprites to be used for particle FX
     *
     * @param event The event to register sprites into
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTextureStitchEvent(TextureStitchEvent.Pre event)
    {
        // There's normally only one map, but just to make sure ensure it's the right one
        if (event.getMap().getBasePath().equals("textures"))
        {
            ModSprites.ENARIA_BASIC_ATTACK = event.getMap().registerSprite(new ResourceLocation(Constants.MOD_ID, "particles/enaria_basic_attack"));
            ModSprites.ENARIA_SPELL_CAST = event.getMap().registerSprite(new ResourceLocation(Constants.MOD_ID, "particles/enaria_spell_cast"));
            ModSprites.ENARIA_SPELL_CAST_2 = event.getMap().registerSprite(new ResourceLocation(Constants.MOD_ID, "particles/enaria_spell_cast_2"));
            ModSprites.ENARIA_TELEPORT = event.getMap().registerSprite(new ResourceLocation(Constants.MOD_ID, "particles/enaria_teleport"));
            ModSprites.SPELL_CAST = event.getMap().registerSprite(new ResourceLocation(Constants.MOD_ID, "particles/spell_cast"));
            ModSprites.SPELL_HIT = event.getMap().registerSprite(new ResourceLocation(Constants.MOD_ID, "particles/spell_hit"));
        }
    }
}
