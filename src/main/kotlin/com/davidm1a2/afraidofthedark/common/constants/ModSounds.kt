package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent

/**
 * A class containing all sounds in AOTD
 */
object ModSounds {
    val CROSSBOW_FIRE = create("crossbow_fire")
    val WEREWOLF_IDLE = create("werewolf_idle")
    val WEREWOLF_AGRO = create("werewolf_agro")
    val WEREWOLF_DEATH = create("werewolf_death")
    val WEREWOLF_HURT = create("werewolf_hurt")
    val ENCHANTED_FROG_CROAK = create("enchanted_frog_croak")
    val ENCHANTED_FROG_DEATH = create("enchanted_frog_death")
    val ENCHANTED_FROG_HURT = create("enchanted_frog_hurt")
    val FROST_PHOENIX_HURT = create("frost_phoenix_hurt")
    val FROST_PHOENIX_FLY = create("frost_phoenix_fly")
    val FROST_PHOENIX_DEATH = create("frost_phoenix_death")
    val FROST_PHOENIX_AMBIENT = create("frost_phoenix_ambient")
    val FROST_PHOENIX_PROJECTILE_BREAK = create("frost_phoenix_projectile_break")
    val ARCANE_JOURNAL_OPEN = create("arcane_journal_open")
    val RESEARCH_UNLOCKED = create("research_unlocked")
    val PAGE_TURN = create("page_turn")
    val BELLS = create("bells")
    val EERIE_ECHOS = create("eerie_echos")
    val SPELL_CAST = create("spell_cast")
    val NIGHTMARE_MUSIC = create("nightmare_music")
    val NIGHTMARE_CHASE_MUSIC = create("nightmare_chase_music")
    val ENARIA_FIGHT_MUSIC = create("enaria_fight_music")
    val LENS_CUTTER = create("lens_cutter")

    // An array containing a list of sounds that AOTD adds
    val SOUND_LIST = arrayOf(
        CROSSBOW_FIRE,
        WEREWOLF_IDLE,
        WEREWOLF_AGRO,
        WEREWOLF_DEATH,
        WEREWOLF_HURT,
        ENCHANTED_FROG_CROAK,
        ENCHANTED_FROG_DEATH,
        ENCHANTED_FROG_HURT,
        FROST_PHOENIX_HURT,
        FROST_PHOENIX_FLY,
        FROST_PHOENIX_DEATH,
        FROST_PHOENIX_AMBIENT,
        FROST_PHOENIX_PROJECTILE_BREAK,
        ARCANE_JOURNAL_OPEN,
        RESEARCH_UNLOCKED,
        PAGE_TURN,
        BELLS,
        EERIE_ECHOS,
        SPELL_CAST,
        NIGHTMARE_MUSIC,
        NIGHTMARE_CHASE_MUSIC,
        ENARIA_FIGHT_MUSIC,
        LENS_CUTTER
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