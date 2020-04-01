package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent

/**
 * A class containing all sounds in AOTD
 */
object ModSounds {
    val CROSSBOW_FIRE = create("crossbow_fire")
    val CROSSBOW_LOAD = create("crossbow_load")
    val WEREWOLF_IDLE = create("werewolf_idle")
    val WEREWOLF_AGRO = create("werewolf_agro")
    val WEREWOLF_DEATH = create("werewolf_death")
    val WEREWOLF_HURT = create("werewolf_hurt")
    val ENCHANTED_FROG_CROAK = create("enchanted_frog_croak")
    val ENCHANTED_FROG_DEATH = create("enchanted_frog_death")
    val ENCHANTED_FROG_HURT = create("enchanted_frog_hurt")
    val JOURNAL_SIGN = create("journal_sign")
    val ACHIEVEMENT_UNLOCKED = create("achievement_unlocked")
    val PAGE_TURN = create("page_turn")
    val BUTTON_HOVER = create("button_hover")
    val SPELL_CRAFTING_BUTTON_HOVER = create("spell_crafting_button_hover")
    val BELLS = create("bells")
    val EERIE_ECHOS = create("eerie_echos")
    val KEY_TYPED = create("key_typed")
    val SPELL_CAST = create("spell_cast")
    val NIGHTMARE_MUSIC = create("nightmare_music")
    val NIGHTMARE_CHASE_MUSIC = create("nightmare_chase_music")

    // An array containing a list of sounds that AOTD adds
    val SOUND_LIST = arrayOf(
        CROSSBOW_FIRE,
        CROSSBOW_LOAD,
        WEREWOLF_IDLE,
        WEREWOLF_AGRO,
        WEREWOLF_DEATH,
        WEREWOLF_HURT,
        ENCHANTED_FROG_CROAK,
        JOURNAL_SIGN,
        ACHIEVEMENT_UNLOCKED,
        PAGE_TURN,
        BUTTON_HOVER,
        SPELL_CRAFTING_BUTTON_HOVER,
        BELLS,
        EERIE_ECHOS,
        KEY_TYPED,
        SPELL_CAST,
        NIGHTMARE_MUSIC,
        NIGHTMARE_CHASE_MUSIC
    )

    /**
     * Utility function to create sound events from the sounds.json file
     *
     * @param name The name of the sound
     * @return A sound event representing that sound
     */
    private fun create(name: String): SoundEvent {
        val location = ResourceLocation(Constants.MOD_ID, name)
        return SoundEvent(location).setRegistryName(location)
    }
}