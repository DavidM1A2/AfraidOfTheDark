/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds
{
	public static final SoundEvent crossbowFire = create("crossbow_fire");
	public static final SoundEvent crossbowLoad = create("crossbow_load");
	public static final SoundEvent werewolfIdle = create("werewolf_idle");
	public static final SoundEvent werewolfAgro = create("werewolf_agro");
	public static final SoundEvent werewolfDeath = create("werewolf_death");
	public static final SoundEvent werewolfHurt = create("werewolf_hurt");
	public static final SoundEvent journalSign = create("journal_sign");
	public static final SoundEvent achievementUnlocked = create("achievement_unlocked");
	public static final SoundEvent pageTurn = create("page_turn");
	public static final SoundEvent buttonHover = create("button_hover");
	public static final SoundEvent spellCraftingButtonHover = create("spell_crafting_button_hover");
	public static final SoundEvent bells = create("bells");
	public static final SoundEvent erieEchos = create("erie_echos");
	public static final SoundEvent keyTyped = create("key_typed");

	public static void initialize()
	{
		GameRegistry.register(crossbowFire);
		GameRegistry.register(crossbowLoad);
		GameRegistry.register(werewolfIdle);
		GameRegistry.register(werewolfAgro);
		GameRegistry.register(werewolfDeath);
		GameRegistry.register(werewolfHurt);
		GameRegistry.register(journalSign);
		GameRegistry.register(achievementUnlocked);
		GameRegistry.register(pageTurn);
		GameRegistry.register(buttonHover);
		GameRegistry.register(spellCraftingButtonHover);
		GameRegistry.register(bells);
		GameRegistry.register(erieEchos);
		GameRegistry.register(keyTyped);
	}

	private static SoundEvent create(String name)
	{
		ResourceLocation location = new ResourceLocation(Reference.MOD_ID, name);
		return new SoundEvent(location).setRegistryName(location);
	}
}
