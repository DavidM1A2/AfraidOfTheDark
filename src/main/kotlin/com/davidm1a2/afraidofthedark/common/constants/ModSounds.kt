package com.davidm1a2.afraidofthedark.common.constants

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent

/**
 * A class containing all sounds in AOTD
 */
object ModSounds {
    val CROSSBOW_FIRE = create("item.crossbow.fire")
    val WEREWOLF_HOWL = create("entity.werewolf.howl")
    val WEREWOLF_ROAR = create("entity.werewolf.roar")
    val WEREWOLF_DEATH = create("entity.werewolf.death")
    val WEREWOLF_HURT = create("entity.werewolf.hurt")
    val ENCHANTED_FROG_CROAK = create("entity.enchanted_frog.croak")
    val ENCHANTED_FROG_DEATH = create("entity.enchanted_frog.death")
    val ENCHANTED_FROG_HURT = create("entity.enchanted_frog.hurt")
    val FROST_PHOENIX_HURT = create("entity.frost_phoenix.hurt")
    val FROST_PHOENIX_FLY = create("entity.frost_phoenix.fly")
    val FROST_PHOENIX_DEATH = create("entity.frost_phoenix.death")
    val FROST_PHOENIX_AMBIENT = create("entity.frost_phoenix.ambient")
    val FROST_PHOENIX_PROJECTILE_BREAK = create("entity.frost_phoenix.projectile_break")
    val ARCANE_JOURNAL_OPEN = create("item.arcane_journal.open")
    val RESEARCH_UNLOCKED = create("ui.research_unlocked")
    val PAGE_TURN = create("ui.page_turn")
    val BELLS = create("ambient.bells")
    val EERIE_ECHOS = create("ambient.eerie_echos")
    val SPELL_CAST = create("entity.player.spell_cast")
    val NIGHTMARE_MUSIC = create("music.nightmare")
    val NIGHTMARE_CHASE_MUSIC = create("music.nightmare_chase")
    val ENARIA_FIGHT_MUSIC = create("music.enaria_fight")
    val LENS_CUTTER = create("block.lens_cutter.cut")
    val SCROLL_LEARNED = create("ui.scroll_learned")

    // An array containing a list of sounds that AOTD adds
    val SOUND_LIST = arrayOf(
        CROSSBOW_FIRE,
        WEREWOLF_HOWL,
        WEREWOLF_ROAR,
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
        LENS_CUTTER,
        SCROLL_LEARNED
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