package com.DavidM1A2.afraidofthedark.common.constants;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * A class containing all sounds in AOTD
 */
public class ModSounds
{
    public static final SoundEvent CROSSBOW_FIRE = create("crossbow_fire");
    public static final SoundEvent CROSSBOW_LOAD = create("crossbow_load");
    public static final SoundEvent WEREWOLF_IDLE = create("werewolf_idle");
    public static final SoundEvent WEREWOLF_AGRO = create("werewolf_agro");
    public static final SoundEvent WEREWOLF_DEATH = create("werewolf_death");
    public static final SoundEvent WEREWOLF_HURT = create("werewolf_hurt");
    public static final SoundEvent JOURNAL_SIGN = create("journal_sign");
    public static final SoundEvent ACHIEVEMENT_UNLOCKED = create("achievement_unlocked");
    public static final SoundEvent PAGE_TURN = create("page_turn");
    public static final SoundEvent BUTTON_HOVER = create("button_hover");
    public static final SoundEvent SPELL_CRAFTING_BUTTON_HOVER = create("spell_crafting_button_hover");
    public static final SoundEvent BELLS = create("bells");
    public static final SoundEvent ERIE_ECHOS = create("erie_echos");
    public static final SoundEvent KEY_TYPED = create("key_typed");

    // An array containing a list of sounds that AOTD adds
    public static final SoundEvent[] SOUND_LIST = new SoundEvent[]
            {
                    CROSSBOW_FIRE,
                    CROSSBOW_LOAD,
                    WEREWOLF_IDLE,
                    WEREWOLF_AGRO,
                    WEREWOLF_DEATH,
                    WEREWOLF_HURT,
                    JOURNAL_SIGN,
                    ACHIEVEMENT_UNLOCKED,
                    PAGE_TURN,
                    BUTTON_HOVER,
                    SPELL_CRAFTING_BUTTON_HOVER,
                    BELLS,
                    ERIE_ECHOS,
                    KEY_TYPED
            };

    /**
     * Utility function to create sound events from the sounds.json file
     *
     * @param name The name of the sound
     * @return A sound event representing that sound
     */
    private static SoundEvent create(String name)
    {
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID, name);
        return new SoundEvent(location).setRegistryName(location);
    }
}
